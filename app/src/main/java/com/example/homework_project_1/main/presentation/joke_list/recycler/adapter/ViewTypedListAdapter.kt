package com.example.homework_project_1.main.presentation.joke_list.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.HeaderItemBinding
import com.example.homework_project_1.databinding.JokeItemBinding
import com.example.homework_project_1.main.presentation.utils.ViewTyped
import com.example.homework_project_1.main.presentation.joke_list.recycler.HeaderViewHolder
import com.example.homework_project_1.main.presentation.joke_list.recycler.JokeViewHolder
import com.example.homework_project_1.main.presentation.joke_list.recycler.LoadingViewHolder
import com.example.homework_project_1.main.presentation.joke_list.recycler.util.ViewTypedCallback

class ViewTypedListAdapter(
    private val jokeClickListener: (ViewTyped.JokeUIModel) -> Unit,
    private val favoriteClickListener: (ViewTyped.JokeUIModel, binding: JokeItemBinding) -> Unit
) : ListAdapter<ViewTyped, RecyclerView.ViewHolder>(ViewTypedCallback()) {

    private var isLoadingAdded = false

    companion object {
        private const val JOKE_VIEW_TYPE = 0
        private const val HEADER_VIEW_TYPE = 1
        private const val LOADING_VIEW_TYPE = 2
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
                            val joke = getItem(position) as? ViewTyped.JokeUIModel
                            joke?.let { jokeClickListener(it) }
                        }
                    }
                    binding.favoriteStar.setOnClickListener {
                        val position = bindingAdapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            val joke = getItem(position) as? ViewTyped.JokeUIModel
                            joke?.let { favoriteClickListener(it, binding) }
                        }
                    }
                }
            }
            HEADER_VIEW_TYPE -> {
                val binding = HeaderItemBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }
            LOADING_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
                LoadingViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ViewTyped.JokeUIModel -> (holder as JokeViewHolder).bind(item)
            is ViewTyped.Header -> (holder as HeaderViewHolder).bindHeader(item)
            is ViewTyped.Loading -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ViewTyped.JokeUIModel -> JOKE_VIEW_TYPE
            is ViewTyped.Header -> HEADER_VIEW_TYPE
            is ViewTyped.Loading -> LOADING_VIEW_TYPE
            else -> LOADING_VIEW_TYPE
        }
    }

    fun addLoadingFooter() {
        if (!isLoadingAdded) {
            isLoadingAdded = true
            val currentList = currentList.toMutableList()
            currentList.add(ViewTyped.Loading)
            submitList(currentList)
        }
    }

    fun removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false
            val currentList = currentList.toMutableList()
            if (currentList.isNotEmpty() && currentList.last() is ViewTyped.Loading) {
                currentList.removeAt(currentList.size - 1)
                submitList(currentList)
            }
        }
    }

    fun getIsLoadingAdded(): Boolean {
        return if (currentList.isEmpty()) {
            false
        } else {
            currentList.last() is ViewTyped.Loading
        }
    }
}
