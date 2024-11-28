package com.example.homework_project_1.main.ui.joke_list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    private val visibleThreshold: Int = 1   // Минимальное количество элементов, которые должны быть ниже текущей позиции прокрутки перед загрузкой дополнительных данных
) : RecyclerView.OnScrollListener() {

    private var _isEndOfList = false    // Флаг того, что список дошел до конца

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)
        val totalItemCount = layoutManager.itemCount    // Общее количество элементов в списке

        // Последняя видимая позиция элемента
        val lastVisibleItemPosition = when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> throw UnsupportedOperationException("Unsupported layout manager.")
        }

        if (lastVisibleItemPosition + visibleThreshold >= totalItemCount - 1) {
            _isEndOfList = true
        } else {
            _isEndOfList = false
        }
    }

    fun isEndOfList(): Boolean {
        return _isEndOfList
    }
}