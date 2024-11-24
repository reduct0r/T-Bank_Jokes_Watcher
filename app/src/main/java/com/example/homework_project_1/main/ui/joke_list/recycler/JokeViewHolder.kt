package com.example.homework_project_1.main.ui.joke_list.recycler

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.JokeItemBinding
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.ViewTyped

class JokeViewHolder(private val binding: JokeItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(joke: ViewTyped.JokeUIModel) {
        bindQuestion(joke.question)
        bindAnswer(joke.answer)
        bindTitle(joke.category)

        if (joke.avatarUri != null) {
            bindURIAvatar(joke.avatarUri)
        } else {
            bindAvatar(joke.avatar)
        }

        when (joke.source) {
            JokeSource.USER -> {
                binding.sourceLabel.text = "Own"
                binding.sourceLabel.setTextColor(binding.root.context.getColor(R.color.purple_200))
            }
            JokeSource.NETWORK -> {
                binding.sourceLabel.text = "Network"
                binding.sourceLabel.setTextColor(binding.root.context.getColor(R.color.teal_200))
            }
            JokeSource.DEFAULT -> {
                binding.sourceLabel.text = "Default"
                binding.sourceLabel.setTextColor(binding.root.context.getColor(R.color.black))
            }
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
