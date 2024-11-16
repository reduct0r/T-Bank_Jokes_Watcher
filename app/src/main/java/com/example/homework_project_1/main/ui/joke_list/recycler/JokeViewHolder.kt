package com.example.homework_project_1.main.ui.joke_list.recycler

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.databinding.JokeItemBinding
import com.example.homework_project_1.main.data.ViewTyped

class JokeViewHolder(private val binding: JokeItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(jokeUIModel: ViewTyped.JokeUIModel) {
        bindQuestion(jokeUIModel.question)
        bindAnswer(jokeUIModel.answer)
        bindTitle(jokeUIModel.category)

        if (jokeUIModel.avatarUri != null) {
            bindURIAvatar(jokeUIModel.avatarUri)
        } else {
            bindAvatar(jokeUIModel.avatar)
        }
    }

    private fun bindQuestion(question: String){
        binding.question.text = question
    }

    private fun bindAnswer(answer: String){
        binding.answer.text = answer
    }

    private fun bindTitle(title: String){
        binding.title.text = title
    }

    private fun bindAvatar(id: Int?){
        id?.let {
            binding.avatar.setImageResource(it)
        }
    }

    private fun bindURIAvatar(uri: Uri?){
        uri?.let {
            binding.avatar.setImageURI(it)
        }
    }
}