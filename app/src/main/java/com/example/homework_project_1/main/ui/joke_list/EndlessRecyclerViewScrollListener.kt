package com.example.homework_project_1.main.ui.joke_list

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener(
    private val layoutManager: RecyclerView.LayoutManager
) : RecyclerView.OnScrollListener() {

    // Общее количество элементов в наборе данных после последней загрузки
    private var previousTotalItemCount = 0

    // Загрузка последнего набора данных
    private var loading = true

    // Минимальное количество элементов, которые должны быть ниже текущей позиции прокрутки перед загрузкой дополнительных данных
    private val visibleThreshold = 1

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)
        val totalItemCount = layoutManager.itemCount // Общее количество элементов в наборе данных

        // Последняя видимая позиция элемента
        val lastVisibleItemPosition = when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> throw UnsupportedOperationException("Unsupported layout manager.")
        }

        // Если общее количество элементов равно нулю, а предыдущее не равно сбрасываем список
        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }

        // Если все еще загружается, проверяем, изменилось ли количество элементов в наборе данных
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // Если не загружается, проверяем, нужно ли загружать больше данных
        if (!loading && (lastVisibleItemPosition + visibleThreshold) >= totalItemCount) {
            onLoadMore()
            loading = true
        }
    }

    fun resetState() {
        this.previousTotalItemCount = 0
        this.loading = true
    }

    abstract fun onLoadMore()
}