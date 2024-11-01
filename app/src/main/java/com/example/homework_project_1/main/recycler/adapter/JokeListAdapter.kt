package com.example.homework_project_1.main.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.databinding.JokeItemBinding
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.recycler.JokeViewHolder
import com.example.homework_project_1.main.recycler.util.JokeItemCallback

class JokeListAdapter : ListAdapter<ViewTyped.Joke, JokeViewHolder>(JokeItemCallback()) {

    // Создаем ViewHolder и устанавливаем слушатель на него
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JokeItemBinding.inflate(inflater, parent, false)

        return JokeViewHolder(binding).apply {
            itemView.setOnClickListener {
                handleJokeClick(parent.context, adapterPosition)
            }
        }
    }

    // Привязываем данные к ViewHolder
    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Обработка клика по элементу списка
    private fun handleJokeClick(context: Context, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val item = getItem(position)
            val message = "Joke ${item.id} clicked"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}