package com.example.homework_project_1.main.ui.joke_list.recycler

import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.core.content.ContextCompat
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
                binding.sourceLabel.text = binding.sourceLabel.context.getString(R.string.own)
                binding.sourceLabel.setTextColor(binding.root.context.getColor(R.color.purple_500))
            }
            JokeSource.NETWORK -> {
                binding.sourceLabel.text = binding.sourceLabel.context.getString(R.string.network)
                binding.sourceLabel.setTextColor(binding.root.context.getColor(R.color.teal_200))
            }
            JokeSource.DEFAULT -> {
                binding.sourceLabel.text = binding.sourceLabel.context.getString(R.string.default_label)
                binding.sourceLabel.setTextColor(binding.root.context.getColor(R.color.light_gray))
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
