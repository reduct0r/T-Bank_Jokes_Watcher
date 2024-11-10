package com.example.homework_project_1.main.ui.joke_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.ActivityJokeDetailsBinding
import com.example.homework_project_1.main.data.ViewTyped

class JokeDetailsActivity : AppCompatActivity() {

    companion object {
        private const val JOKE_POSITION_EXTRA = "JOKE_POSITION"

        fun getInstance(context: Context, jokePosition: Int): Intent {
            return Intent(context, JokeDetailsActivity::class.java).apply {
                putExtra(JOKE_POSITION_EXTRA, jokePosition)
            }
        }
    }

    private lateinit var binding: ActivityJokeDetailsBinding

    private val viewModel: JokeDetailsViewModel by viewModels {
        JokesDetailsViewModelFactory(intent.getIntExtra(JOKE_POSITION_EXTRA, -1))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide() // Скрытие ActionBar
        super.onCreate(savedInstanceState)
        binding = ActivityJokeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadJoke()

        if (viewModel.getPosition() == -1) {
            handleError("Incorrect joke position.")
        }

        // Наблюдение за изменениями в LiveData
        viewModel.joke.observe(this) { joke ->
            setupJokesData(joke)
        }

        // Наблюдение за ошибками
        viewModel.error.observe(this) { errorMessage ->
            handleError(errorMessage)
        }

        // Установка текста на кнопку "Добавить в избранное"
        binding.addToFavorites.text = getString(R.string.add_to_favorites)

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

