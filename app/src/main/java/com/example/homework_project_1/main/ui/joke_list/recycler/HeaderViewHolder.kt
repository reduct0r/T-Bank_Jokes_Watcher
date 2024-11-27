package com.example.homework_project_1.main.ui.joke_list.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.databinding.HeaderItemBinding
import com.example.homework_project_1.main.data.ViewTyped

// Класс для отображения заголовка (в будущем)
class HeaderViewHolder(private val binding: HeaderItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bindHeader(item: ViewTyped.Header) {
        binding.headerName.text = item.title
    }
}
