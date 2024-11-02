package com.example.homework_project_1.main.ui.joke_list.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.databinding.HeaderItemBinding
import com.example.homework_project_1.databinding.JokeItemBinding
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.ui.joke_list.recycler.HeaderViewHolder
import com.example.homework_project_1.main.ui.joke_list.recycler.JokeViewHolder
import com.example.homework_project_1.main.ui.joke_list.recycler.util.ViewTypedCallback

class ViewTypedListAdapter(
    private val clickListener: (Int) -> Unit        // Слушатель для обработки клика по элементу списка
) : ListAdapter<ViewTyped, RecyclerView.ViewHolder>(ViewTypedCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            JOKE_VIEW_TYPE -> {
                val binding = JokeItemBinding.inflate(inflater, parent, false)
                JokeViewHolder(binding)
                return JokeViewHolder(binding).apply {
                    itemView.setOnClickListener {
                        handleClick(parent.context, adapterPosition)
                    }
                }
            }

            HEADER_VIEW_TYPE -> {
                val binding = HeaderItemBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }

    }

    // Привязка данных к элементу списка
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ViewTyped.Joke -> (holder as JokeViewHolder).bind(item)
            is ViewTyped.Header -> (holder as HeaderViewHolder).bindHeader(item)
        }
    }

    // Определение типа элемента списка
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ViewTyped.Joke -> JOKE_VIEW_TYPE
            is ViewTyped.Header -> HEADER_VIEW_TYPE
            else -> throw IllegalArgumentException("Unknown type!")
        }
    }

    companion object {
        private const val JOKE_VIEW_TYPE = 0
        private const val HEADER_VIEW_TYPE = 1
    }

    // Обработка клика по элементу списка
    private fun handleClick(context: Context, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            clickListener(position)
            Toast.makeText(context, "Clicked on joke with position: ${position}", Toast.LENGTH_SHORT).show()
        }
    }
}