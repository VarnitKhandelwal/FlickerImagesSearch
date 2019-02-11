package com.varnit.android.hikeassignment.utils


import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView

private val TAG: String = PaginationScrollListener::class.java.name

abstract class PaginationScrollListener(val layoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {
    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
    protected open fun trackViewEvents(newState: Int) {}

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        trackViewEvents(newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (allowLoadMore() && isNearToLastItem(layoutManager)) {
            loadMoreItems()
        }
    }

    private fun allowLoadMore() = !isLoading() && !isLastPage()

    private fun isNearToLastItem(layoutManager: GridLayoutManager): Boolean {
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        return visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                firstVisibleItemPosition >= 0
    }
}