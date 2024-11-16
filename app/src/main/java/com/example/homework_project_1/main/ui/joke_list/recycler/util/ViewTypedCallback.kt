package com.example.homework_project_1.main.ui.joke_list.recycler.util

import androidx.recyclerview.widget.DiffUtil
import com.example.homework_project_1.main.data.ViewTyped

// Класс для сравнения элементов списка для DiffUtil
class ViewTypedCallback : DiffUtil.ItemCallback<ViewTyped>() {
    override fun areItemsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return if (oldItem is ViewTyped.JokeUIModel && newItem is ViewTyped.JokeUIModel) {
            oldItem.id == newItem.id
        } else {
            oldItem::class == newItem::class
        }
    }

    override fun areContentsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return oldItem == newItem
    }
}
