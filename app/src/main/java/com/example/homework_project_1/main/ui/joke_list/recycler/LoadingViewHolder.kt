package com.example.homework_project_1.main.ui.joke_list.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.main.data.ViewTyped

class LoadingViewHolder(view: View): RecyclerView.ViewHolder(view) {
    fun bindLoader(item: ViewTyped.Loading) {
        // No need to bind anything if you don't want to change the view
    }
}