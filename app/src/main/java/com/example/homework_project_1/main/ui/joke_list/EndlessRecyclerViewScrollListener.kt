package com.example.homework_project_1.main.ui.joke_list

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener(
    private val layoutManager: RecyclerView.LayoutManager
) : RecyclerView.OnScrollListener() {

    // Минимальное количество элементов, которые должны быть ниже текущей позиции прокрутки перед загрузкой дополнительных данных
    private val visibleThreshold = 4

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)
        val totalItemCount = layoutManager.itemCount // Общее количество элементов в наборе данных

        // Последняя видимая позиция элемента
        val lastVisibleItemPosition = when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> throw UnsupportedOperationException("Unsupported layout manager.")
        }

        if (lastVisibleItemPosition >= totalItemCount - visibleThreshold &&  (isLoading() == null || isLoading() == false)) {
            onLoadMore()
        }
    }

    abstract fun onLoadMore()
    abstract fun isLoading(): Boolean?
}