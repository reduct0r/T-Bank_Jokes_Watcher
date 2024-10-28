package com.example.homework_project_1.main.data

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import com.example.homework_project_1.R

import com.example.homework_project_1.main.data.ViewTyped.*
import kotlin.random.Random

class JokesGenerator() {
    private val avatarSet = mutableSetOf(
        R.drawable.smile1,
        R.drawable.smile2,
        R.drawable.smile3
    )
    val jokesList: MutableList<ViewTyped.Joke> = mutableListOf(
        // Ваши инициализированные шутки
        ViewTyped.Joke(1, null, "General", "Why did the chicken cross the road?", "To get to the other side.To get to the other side.To get to the other side."),
        ViewTyped.Joke(2, null, "Programming", "Why do programmers prefer dark mode?", "Because light attracts bugs."),
        ViewTyped.Joke(3, null, "Animal", "What do you call a bear with no teeth?", "A gummy bear."),
        ViewTyped.Joke(4, null, "Math", "Why was the equal sign so humble?", "Because it knew it wasn't less than or greater than anyone else."),
        ViewTyped.Joke(5, null, "Science", "Why can't you trust atoms?", "Because they make up everything."),
        ViewTyped.Joke(6, null, "Kids", "What do you give a sick bird?", "Tweetment."),
        ViewTyped.Joke(7, null, "Tech", "Why was the cell phone wearing glasses?", "Because it lost its contacts.")
    )

    // Добавляем шутки в список или обновляем существующие
    fun generateData(): List<ViewTyped> {
        return buildList {
            for (i in 0..10) {
                if (i % 3 == 0) {
                    add(Header("Default Header id$i"))
                } else {
                    add(addRandomJoke(i))
                }
            }
        }
    }

    fun generateJokesData(): List<Joke> {
        return buildList {
            for (i in 0..7) {
                add(addRandomJoke(i))
            }
        }
    }

    @SuppressLint("ResourceType")
    fun addRandomJoke(index: Int): ViewTyped.Joke {
        if (jokesList.isNotEmpty()) {
            val randomIndex = Random.nextInt(jokesList.size) // Получаем случайный индекс из доступных шуток
            val joke = jokesList[randomIndex]
            if (joke.avatar == null) {                      // Если у шутки нет аватара, назначаем случайный
                joke.avatar = getRandomAvatar()
            }
            jokesList.removeAt(randomIndex)         // Удаляем шутку из списка, чтобы не повторялась
            return joke
        }
        return ViewTyped.Joke(0, getRandomAvatar(), "General", "No jokes available", "Please add more jokes.")
    }

    @DrawableRes
    private fun getRandomAvatar(): Int {
        return avatarSet.random()
    }
}