package com.example.homework_project_1.main.recycler.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.databinding.JokeItemBinding
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.data.ViewTyped.*
import com.example.homework_project_1.main.recycler.JokeViewHolder
import com.example.homework_project_1.main.recycler.util.JokeItemCallback


class JokeListAdapter(itemCallback: JokeItemCallback): ListAdapter<Joke, JokeViewHolder>(itemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JokeItemBinding.inflate(inflater)
        return JokeViewHolder(binding).apply { binding.root.setOnClickListener {
            handlePersonClick(parent.context, adapterPosition)
        } }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private fun handlePersonClick(context: Context, position: Int){
        if (position != RecyclerView.NO_POSITION){
            val item = currentList[position]
            val message = "Joke ${item.id} clicked"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}