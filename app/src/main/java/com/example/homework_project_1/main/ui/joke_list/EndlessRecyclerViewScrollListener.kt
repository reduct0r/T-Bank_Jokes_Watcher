package com.example.homework_project_1.main.ui.joke_list

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener(
    private val layoutManager: RecyclerView.LayoutManager
) : RecyclerView.OnScrollListener() {

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // The minimum amount of items to have below your current scroll position before loading more.
    private val visibleThreshold = 5

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)

        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition = when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> throw UnsupportedOperationException("Unsupported layout manager.")
        }

        // If the total item count is zero and the previous isn't, assume the list is invalidated and reset
        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }

        // If it's still loading, check if the dataset count has changed
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If not loading, check if we need to load more data
        if (!loading && (lastVisibleItemPosition + visibleThreshold) >= totalItemCount) {
            onLoadMore()
            loading = true
        }
    }

    abstract fun onLoadMore()
}