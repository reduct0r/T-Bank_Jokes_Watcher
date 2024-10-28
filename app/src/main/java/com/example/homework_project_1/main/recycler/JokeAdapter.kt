package com.example.homework_project_1.main.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.HeaderItemBinding
import com.example.homework_project_1.databinding.JokeItemBinding
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.data.ViewTyped.*
import com.example.homework_project_1.main.recycler.util.JokeDiffUtilCallback


class JokeAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data  = emptyList<ViewTyped>()

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(newData: List<ViewTyped>) {
        //val diffUtilCallback = JokeDiffUtilCallback(data, newData)
        //val calculateDiff = DiffUtil.calculateDiff(diffUtilCallback)
        data = newData
        notifyDataSetChanged()
        //calculateDiff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType){
            R.layout.joke_item -> {
                val binding = JokeItemBinding.inflate(inflater)
                JokeViewHolder(binding).apply {
                    binding.root.setOnClickListener {
                        handleJokeClick(parent.context, adapterPosition)
                    }
                }
            }
            R.layout.header_item -> {
                val binding = HeaderItemBinding.inflate(inflater)
                HeaderViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (holder){
            is JokeViewHolder -> holder.bind(item as Joke)
            is HeaderViewHolder -> holder.bindHeader(item as Header)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        return when (item) {
            is Joke -> R.layout.joke_item
            is Header -> R.layout.header_item
        }
    }

    private fun handleJokeClick(context: Context, position: Int){
        if (position != RecyclerView.NO_POSITION){
            val item = data[position]
            val message = "Joke ${item} clicked in adapter"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}