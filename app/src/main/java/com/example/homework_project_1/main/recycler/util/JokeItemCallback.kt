package com.example.homework_project_1.main.recycler.util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.example.homework_project_1.main.data.ViewTyped.*

// Класс для сравнения элементов списка для DiffUtil
class JokeItemCallback: DiffUtil.ItemCallback<Joke>() {
    override fun areItemsTheSame(oldItem: Joke, newItem: Joke): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Joke, newItem: Joke): Boolean {
        return oldItem == newItem
    }
}