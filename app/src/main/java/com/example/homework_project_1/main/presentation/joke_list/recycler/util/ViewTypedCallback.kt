package com.example.homework_project_1.main.presentation.joke_list.recycler.util

import androidx.recyclerview.widget.DiffUtil
import com.example.homework_project_1.main.presentation.utils.ViewTyped

// Класс для сравнения элементов списка для DiffUtil
class ViewTypedCallback : DiffUtil.ItemCallback<ViewTyped>() {
    override fun areItemsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return oldItem::class == newItem::class && oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return oldItem == newItem
    }
}
