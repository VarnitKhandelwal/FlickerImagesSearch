package com.varnit.android.hikeassignment.utils

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.replaceFragment(@IdRes layoutId: Int, fragment: Fragment, allowStateLoss: Boolean = true, tag: String? = null) {
    val transaction = this.supportFragmentManager.beginTransaction().replace(layoutId, fragment, tag)

    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

fun Fragment.addFragment(@IdRes layoutId: Int, fragment: Fragment, allowStateLoss: Boolean = true, tag: String? = null) {

    val ft = this.fragmentManager?.beginTransaction()?.add(layoutId, fragment, tag)
    if (allowStateLoss) {
        ft?.commitAllowingStateLoss()
    } else {
        ft?.commit()
    }

}

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(activity!!.application)).get(viewModelClass)


fun openNavigation(lat: String, lng: String, ctx: Context) {
    // Create a Uri from an intent string. Use the result to create an Intent.
    val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")

    // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    // Make the Intent explicit by setting the Google Maps package
    mapIntent.setPackage("com.google.android.apps.maps")

    if (mapIntent.resolveActivity(ctx.packageManager) != null) {
        // Attempt to start an activity that can handle the Intent
        ctx.startActivity(mapIntent)
    }
}

fun shareContent(actvity: FragmentActivity?, context: String, title: String?) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.setType("text/plain")
    intent.putExtra(Intent.EXTRA_TEXT, context)
    intent.putExtra(Intent.EXTRA_SUBJECT, title)

    val chooser = Intent.createChooser(intent, "Share via")
    actvity?.startActivity(chooser)
}

