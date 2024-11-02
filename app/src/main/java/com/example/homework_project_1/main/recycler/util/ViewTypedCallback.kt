package com.example.homework_project_1.main.recycler.util

import androidx.recyclerview.widget.DiffUtil
import com.example.homework_project_1.main.data.ViewTyped

// Класс для сравнения элементов списка для DiffUtil
class ViewTypedCallback : DiffUtil.ItemCallback<ViewTyped>() {
    override fun areItemsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return when {
            oldItem is ViewTyped.Joke && newItem is ViewTyped.Joke -> oldItem.id == newItem.id
            oldItem is ViewTyped.Header && newItem is ViewTyped.Header -> oldItem.title == newItem.title
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return oldItem == newItem
    }
}