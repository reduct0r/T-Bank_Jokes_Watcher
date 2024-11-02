package com.example.homework_project_1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.homework_project_1.databinding.ActivityJokeDetailsBinding
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.ViewTyped.*

class JokeDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJokeDetailsBinding
    private val generator = JokesGenerator
    private var jokePosition: Int = -1

    companion object {

        private const val JOKE_POSITION_EXTRA = "JOKE_POSITION"

        fun getInstance(context: Context, jokePosition: Int): Intent {
            return Intent(context, JokeDetailsActivity::class.java).apply {
                putExtra(JOKE_POSITION_EXTRA, jokePosition)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()                                        // Скрытие ActionBar
        super.onCreate(savedInstanceState)
        binding = ActivityJokeDetailsBinding.inflate(layoutInflater)    // Инициализация ViewBinding
        setContentView(binding.root)                                    // Установка корневого элемента
        handleExtra()                                                   // Обработка переданных данных

        // Обработка нажатия на кнопку "Назад"
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    // Обработка переданных данных
    private fun handleExtra(){
        jokePosition = intent.getIntExtra(JOKE_POSITION_EXTRA, -1)

        if (jokePosition == -1) {
            handleError()
        }
        else {
            val item = generator.getSelectedJokes()[jokePosition] as? Joke

            if (item != null){
                setupJokesData(item)
            }
            else{
                handleError()
            }
        }
    }

    // Установка данных шутки
    @SuppressLint("ResourceType")
    private fun setupJokesData(item: Joke) {
        with(binding){
            question.text = item.question
            answer.text = item.answer
            category.text = item.category
            item.avatar?.let {
                avatar.setImageResource(item.avatar!!)
            }
        }
    }

    // Обработка ошибки
    private fun handleError() {
        Toast.makeText(this, "Invalid person data", Toast.LENGTH_SHORT).show()
        finish()
    }


}