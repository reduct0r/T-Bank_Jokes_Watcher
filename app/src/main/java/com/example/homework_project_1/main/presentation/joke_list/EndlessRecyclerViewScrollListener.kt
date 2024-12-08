package com.example.homework_project_1.main.presentation.joke_list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    private val visibleThreshold: Int = 1 // Минимальное количество элементов до конца, чтобы загрузить больше
) : RecyclerView.OnScrollListener() {

    private var isLoading = false // Флаг состояния загрузки
    private var previousTotalItemCount = 0 // Общее количество элементов после последней загрузки

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)

        val totalItemCount = layoutManager.itemCount

        // Определение последней видимой позиции
        val lastVisibleItemPosition = when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> throw UnsupportedOperationException("Unsupported layout manager.")
        }

        // Проверка, требуется ли сброс состояния загрузки
        if (isLoading && totalItemCount > previousTotalItemCount) {
            isLoading = false
            previousTotalItemCount = totalItemCount
        }

        // Если не происходит загрузка и достигнут порог видимости, инициировать загрузку
        if (!isLoading && (lastVisibleItemPosition + visibleThreshold) >= totalItemCount) {
            isLoading = true
            onLoadMore()
        }
    }

    // Абстрактный метод, который необходимо реализовать для загрузки данных
    abstract fun onLoadMore()

    // Метод для сброса состояния, если требуется
    fun resetState() {
        isLoading = false
        previousTotalItemCount = 0
    }
}