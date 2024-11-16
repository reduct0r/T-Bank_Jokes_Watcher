package com.example.homework_project_1.main.ui.joke_list.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.databinding.HeaderItemBinding
import com.example.homework_project_1.databinding.JokeItemBinding
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.data.ViewTyped.*
import com.example.homework_project_1.main.ui.joke_list.recycler.HeaderViewHolder
import com.example.homework_project_1.main.ui.joke_list.recycler.JokeViewHolder
import com.example.homework_project_1.main.ui.joke_list.recycler.util.ViewTypedCallback

class ViewTypedListAdapter(
    private val clickListener: (Int) -> Unit
) : ListAdapter<ViewTyped, RecyclerView.ViewHolder>(ViewTypedCallback()) {

    companion object {
        private const val JOKE_VIEW_TYPE = 0
        private const val HEADER_VIEW_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            JOKE_VIEW_TYPE -> {
                val binding = JokeItemBinding.inflate(inflater, parent, false)
                JokeViewHolder(binding).apply {
                    itemView.setOnClickListener {
                        val position = bindingAdapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            clickListener(position)
                        }
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is JokeUIModel -> (holder as JokeViewHolder).bind(item)
            is Header -> (holder as HeaderViewHolder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is JokeUIModel -> JOKE_VIEW_TYPE
            is Header -> HEADER_VIEW_TYPE
            else -> throw IllegalArgumentException("Unknown type!")
        }
    }
}