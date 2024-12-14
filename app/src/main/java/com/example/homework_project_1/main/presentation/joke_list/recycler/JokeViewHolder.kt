package com.example.homework_project_1.main.presentation.joke_list.recycler

import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.JokeItemBinding
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.presentation.utils.ViewTyped

class JokeViewHolder(private val binding: JokeItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(joke: ViewTyped.JokeUIModel) {
        bindQuestion(joke.question)
        bindAnswer(joke.answer)
        bindTitle(joke.category)

        if (joke.avatarByteArr != null) {
            bindAvatarFromByteArray(joke.avatarByteArr)
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
            JokeSource.DATABASE -> {
                binding.sourceLabel.text = binding.sourceLabel.context.getString(R.string.database)
                binding.sourceLabel.setTextColor(binding.root.context.getColor(R.color.teal_700))
            }
            JokeSource.CACHE -> {
                binding.sourceLabel.text = binding.sourceLabel.context.getString(R.string.cache)
                binding.sourceLabel.setTextColor(binding.root.context.getColor(R.color.light_green))
            }
        }
    }
    private fun bindAvatarFromByteArray(avatarByteArray: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.size)
        binding.avatar.setImageBitmap(bitmap)
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
}
