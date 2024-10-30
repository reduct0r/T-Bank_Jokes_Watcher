package com.example.homework_project_1.main.data

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import com.example.homework_project_1.R

import com.example.homework_project_1.main.data.ViewTyped.*
import kotlin.random.Random

// Класс для генерации шуток
class JokesGenerator {
    // Набор аватарок
    private val avatarSet = setOf(
        R.drawable.smile1,
        R.drawable.smile2,
        R.drawable.smile3
    )

    // Изменяемый список доступных шуток
    private val jokesList: MutableList<Joke> = mutableListOf(
        Joke(1, null, "General", "Why did the chicken cross the road?", "To get to the other side."),
        Joke(2, null, "Programming", "Why do programmers prefer dark mode?", "Because light attracts bugs."),
        Joke(3, null, "Animal", "What do you call a bear with no teeth?", "A gummy bear."),
        Joke(4, null, "Math", "Why was the equal sign so humble?", "Because it knew it wasn't less than or greater than anyone else."),
        Joke(5, null, "Science", "Why can't you trust atoms?", "Because they make up everything."),
        Joke(6, null, "Kids", "What do you give a sick bird?", "Tweetment."),
        Joke(7, null, "Tech", "Why was the cell phone wearing glasses?", "Because it lost its contacts."),
        Joke(8, null, "General", "Why don’t scientists trust atoms?", "Because they make up everything."),
        Joke(9, null, "Programming", "Why don't programmers like nature?", "Too many bugs."),
        Joke(10, null, "Animal", "Why don't seagulls fly over the bay?", "Because then they'd be bagels."),
        Joke(11, null, "Math", "Why was the math book sad?", "Because it had too many problems."),
        Joke(12, null, "Science", "Why does a physicist like science fiction?", "Because it's all about space."),
        Joke(13, null, "Kids", "What do you call a snowman with a six pack?", "An abdominal snowman."),
        Joke(14, null, "Tech", "Why was the computer cold?", "It left its Windows open.")

    )

    // Генерация данных для списка
    fun generateJokesData(): List<Joke> {
        return buildList {
            for (i in 0..6) {
                add(addRandomJoke())
            }
        }
    }

    // Добавление случайной шутки
    @SuppressLint("ResourceType")
    fun addRandomJoke(): Joke {
        if (jokesList.isNotEmpty()) {
            val randomIndex = Random.nextInt(jokesList.size)    // Получаем случайный индекс из доступных шуток
            val joke = jokesList[randomIndex]
            if (joke.avatar == null) {                          // Если у шутки нет аватара, назначаем случайный
                joke.avatar = getRandomAvatar()
            }
            jokesList.removeAt(randomIndex)                     // Удаляем шутку из списка, чтобы не повторялась
            return joke
        }
        return Joke(0, getRandomAvatar(), "General", "No jokes available", "Please add more jokes.")
    }

    // Получение случайной аватарки
    @DrawableRes
    private fun getRandomAvatar(): Int {
        return avatarSet.random()
    }
}