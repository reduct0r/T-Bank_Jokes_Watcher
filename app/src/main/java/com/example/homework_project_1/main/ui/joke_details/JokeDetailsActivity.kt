package com.example.homework_project_1.main.ui.joke_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.ActivityJokeDetailsBinding
import com.example.homework_project_1.main.data.ViewTyped

class JokeDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJokeDetailsBinding
    private lateinit var viewModel: JokeDetailsViewModel

    companion object {
        private const val JOKE_POSITION_EXTRA = "JOKE_POSITION"

        fun getInstance(context: Context, jokePosition: Int): Intent {
            return Intent(context, JokeDetailsActivity::class.java).apply {
                putExtra(JOKE_POSITION_EXTRA, jokePosition)
            }
        }
    }

    private fun initViewModel(jokePosition: Int) {
        val factory = JokesDetailsViewModelFactory(jokePosition)
        viewModel = ViewModelProvider(this, factory)[JokeDetailsViewModel::class.java]

        // Наблюдение за изменениями в LiveData
        viewModel.joke.observe(this) { joke ->
            setupJokesData(joke)
        }

        // Наблюдение за ошибками
        viewModel.error.observe(this) { errorMessage ->
            handleError(errorMessage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide() // Скрытие ActionBar
        super.onCreate(savedInstanceState)
        binding = ActivityJokeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addToFavorites.text = getString(R.string.add_to_favorites)

        // Получение jokePosition из Intent
        val jokePosition = intent.getIntExtra(JOKE_POSITION_EXTRA, -1)

        if (jokePosition == -1) {
            handleError("Incorrect joke position.")
        }
        else {
            initViewModel(jokePosition)
        }

        // Обработка нажатия на кнопку "Назад"
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    // Установка данных в элементы экрана
    private fun setupJokesData(item: ViewTyped.Joke) {
        with(binding) {
            question.text = item.question
            answer.text = item.answer
            category.text = item.category
            item.avatar?.let {
                avatar.setImageResource(it)
            }
        }
    }

    // Обработка ошибок
    private fun handleError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }
}