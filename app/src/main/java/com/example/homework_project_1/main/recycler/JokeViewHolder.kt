package com.example.homework_project_1.main.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.databinding.JokeItemBinding
import com.example.homework_project_1.main.data.ViewTyped


class JokeViewHolder(val binding: JokeItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(joke: ViewTyped.Joke) {
        bindQuestion(joke.question)
        bindAnswer(joke.answer)
        bindTitle(joke.category)
        bindAvatar(joke.avatar)
    }

    fun bindQuestion(question: String){
        binding.question.text = question
    }

    fun bindAnswer(answer: String){
        binding.answer.text = answer
    }

    fun bindTitle(title: String){
        binding.title.text = title
    }

    fun bindAvatar(id: Int?){
        id?.let {
            binding.avatar.setImageResource(it)
        }
    }
}