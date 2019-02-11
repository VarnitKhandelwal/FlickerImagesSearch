package com.varnit.android.hikeassignment.utils

import android.app.Activity
import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.support.v4.app.Fragment
import android.view.View
import kotlin.reflect.KProperty

/**
 * Represents a value with lifecycleAwareLazy initialization.
 *
 * To create an instance of [LifecycleAwareLazy] use the [lifecycleAwareLazy] function.
 */
public interface LifecycleAwareLazy<out T> {
    /**
     * Gets the lazily initialized value of the current LifecycleAwareLazy instance.
     * Once the value was initialized it must not change during the rest of lifetime of this LifecycleAwareLazy instance.
     */
    public val value: T

    /**
     * Returns `true` if a value for this LifecycleAwareLazy instance has been already initialized, and `false` otherwise.
     * Once this function has returned `true` it stays `true` for the rest of lifetime of this LifecycleAwareLazy instance.
     */
    public fun isInitialized(): Boolean
}

/**
 * Lifecycle aware
 */
public fun <T> lifecycleAwareLazy(lifecycle: Lifecycle, initializer: () -> T): LifecycleAwareLazy<T> = LifecycleAwareLazyImpl(lifecycle, initializer)

public fun <T, U> findViewLazy(fragment: U, viewId: Int): LifecycleAwareLazy<T>
        where T : View, U : Fragment, U : LifecycleOwner {
    return lifecycleAwareLazy(fragment.lifecycle) {
        fragment.view!!.findViewById<T>(viewId)
    }
}

public fun <T, U> findViewLazy(activity: U, viewId: Int): LifecycleAwareLazy<T>
        where T : View, U : Activity, U : LifecycleOwner {
    return lifecycleAwareLazy(activity.lifecycle) {
        activity.findViewById<T>(viewId)
    }
}

/**
 * An extension to delegate a read-only property of type [T] to an instance of [LifecycleAwareLazy].
 *
 * This extension allows to use instances of LifecycleAwareLazy for property delegation:
 * `val property: TextView by lifecycleAwareLazy(lifecycle) { initializer }`
 */
public inline operator fun <T> LifecycleAwareLazy<T>.getValue(thisRef: Any?, property: KProperty<*>): T = value


private object UNINITIALIZED_VALUE

private class LifecycleAwareLazyImpl<out T>(lifecycle: Lifecycle, initializer: () -> T)
    : LifecycleAwareLazy<T>, GenericLifecycleObserver {
    fun getReceiver() = this

    override fun onStateChanged(source: LifecycleOwner?, event: Lifecycle.Event?) {
        when (event) {
            Lifecycle.Event.ON_STOP -> _value = UNINITIALIZED_VALUE
//            Lifecycle.Event.ON_DESTROY -> initializer = null
            else -> return
        }
    }

    init {
        lifecycle.addObserver(this)
    }

    private var initializer: (() -> T)? = initializer
    private var _value: Any? = UNINITIALIZED_VALUE

    override val value: T
        get() {
            if (_value === UNINITIALIZED_VALUE) {
                _value = initializer!!()
            }
            @Suppress("UNCHECKED_CAST")
            return _value as T
        }

    override fun isInitialized(): Boolean = _value !== UNINITIALIZED_VALUE

    override fun toString(): String = if (isInitialized()) value.toString() else "LifecycleAwareLazy value not initialized yet."
}

public interface LifecycleAwareFindView<out T : View> {
    /**
     * Gets the lazily initialized value of the current LifecycleAwareLazy instance.
     * Once the value was initialized it must not change during the rest of lifetime of this LifecycleAwareLazy instance.
     */
    public val value: T

    /**
     * Returns `true` if a value for this LifecycleAwareLazy instance has been already initialized, and `false` otherwise.
     * Once this function has returned `true` it stays `true` for the rest of lifetime of this LifecycleAwareLazy instance.
     */
    public fun isInitialized(): Boolean
}

public fun <T, U> findView(fragment: U, viewId: Int): LifecycleAwareFindView<T>
        where T : View, U : Fragment, U : LifecycleOwner = FindFragmentViewImpl(fragment, viewId)

private class FindFragmentViewImpl<out T, out U>(fragment: U, val viewId: Int)
    : LifecycleAwareFindView<T>, GenericLifecycleObserver where T : View, U : Fragment, U : LifecycleOwner {
    fun getReceiver() = this

    override fun onStateChanged(source: LifecycleOwner?, event: Lifecycle.Event?) {
        when (event) {
            Lifecycle.Event.ON_STOP -> _value = UNINITIALIZED_VALUE
            Lifecycle.Event.ON_DESTROY -> if (fragment?.retainInstance == false) fragment = null
            else -> return
        }
    }

    init {
        fragment.lifecycle.addObserver(this)
    }

    private var fragment: U? = fragment
    private var _value: Any? = UNINITIALIZED_VALUE

    override val value: T
        get() {
            if (_value === UNINITIALIZED_VALUE) {
                _value = fragment!!.view!!.findViewById(viewId)
            }
            @Suppress("UNCHECKED_CAST")
            return _value as T
        }

    override fun isInitialized(): Boolean = _value !== UNINITIALIZED_VALUE

    override fun toString(): String = if (isInitialized()) value.toString() else "LifecycleAwareLazy value not initialized yet."
}

/**
 * An extension to delegate a read-only property of type [T] to an instance of [LifecycleAwareLazy].
 *
 * This extension allows to use instances of LifecycleAwareLazy for property delegation:
 * `val property: TextView by lifecycleAwareLazy(lifecycle) { initializer }`
 */
public inline operator fun <T : View> LifecycleAwareFindView<T>.getValue(thisRef: Any?, property: KProperty<*>): T = value