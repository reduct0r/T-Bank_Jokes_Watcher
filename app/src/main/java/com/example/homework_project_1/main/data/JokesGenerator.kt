package com.example.homework_project_1.main.data
import android.annotation.SuppressLint
import kotlinx.serialization.json.Json
import android.content.Context
import com.example.homework_project_1.R
import java.io.IOException
import kotlin.random.Random

// Класс для генерации шуток
object JokesGenerator {
    private var selectedJokes = mutableListOf<ViewTyped.Joke>()

    // Наборы аватарок по категориям
    private val defaultAvatars = listOf(
        R.drawable.def_ava1,
        R.drawable.def_ava2,
        R.drawable.def_ava3
    )
    private val programmingAvatars = listOf(
        R.drawable.prog_ava1,
        R.drawable.prog_ava2,
        R.drawable.prog_ava3,
        R.drawable.prog_ava4,
        R.drawable.prog_ava5,
        R.drawable.prog_ava6,
        R.drawable.prog_ava7
    )
    private val mathAvatars = listOf(
        R.drawable.math_ava1,
        R.drawable.math_ava2,
        R.drawable.math_ava3,
        R.drawable.math_ava4,
        R.drawable.math_ava5
    )
    private val scienceAvatars = listOf(
        R.drawable.sci_ava1,
        R.drawable.sci_ava2,
        R.drawable.sci_ava3,
        R.drawable.sci_ava4,
        R.drawable.math_ava1,
        R.drawable.math_ava4,
        R.drawable.math_ava5
    )
    private val techAvatars = listOf(
        R.drawable.tech_ava1,
        R.drawable.tech_ava2,
        R.drawable.tech_ava3,
        R.drawable.sci_ava2,
        R.drawable.sci_ava3,
        R.drawable.sci_ava4
    )

    // Карта, связывающая категории с их аватарками
    private val categoryAvatars: Map<String, List<Int>> = mapOf(
        "General" to defaultAvatars,
        "Programming" to programmingAvatars,
        "Math" to mathAvatars,
        "Science" to scienceAvatars,
        "Tech" to techAvatars
    )

    private var jokesList: MutableList<ViewTyped.Joke> = mutableListOf()
    private var ind = 0
    private var usedJokesIndices = mutableSetOf<Int>()

    // Инициализация списка шуток из JSON
    fun initialize(context: Context) {
        val jokesData = JsonReader.readJokesFromAsset(context)
        jokesData?.categories?.forEach { category ->
            category.jokes.forEach { jokeDto ->
                val avatarResId = if (jokeDto.avatar != null) {
                    getAvatarResourceId(context, jokeDto.avatar) // Если аватарка указана в JSON, то получить ее ресурс
                } else {
                    null
                }

                jokesList.add(
                    ViewTyped.Joke(
                        id = 0,
                        avatar = avatarResId,
                        category = category.name,
                        question = jokeDto.question,
                        answer = jokeDto.answer
                    )
                )
            }
        }
    }

    // Получение ресурса аватарки по имени
    @SuppressLint("DiscouragedApi")
    private fun getAvatarResourceId(context: Context, avatarName: String?): Int? {
        return avatarName?.let {
            val resId = context.resources.getIdentifier(it, "drawable", context.packageName)
            if (resId != 0) resId else null
        }
    }

    // Генерация данных для списка из рандомных шуток без повторения
    fun generateJokesData(): List<ViewTyped> {
        val newSelectedJokes = mutableListOf<ViewTyped.Joke>()
        val usedAvatarsPerCategory = mutableMapOf<String, MutableSet<Int>>()

        var attempts = 0
        val maxAttempts = jokesList.size * 3

        while (newSelectedJokes.size < 7 && jokesList.isNotEmpty() && attempts < maxAttempts) {
            attempts++

            val randomIndex = Random.nextInt(jokesList.size)

            if (randomIndex in usedJokesIndices)
                continue // Пропустить, если шутка уже была использована

            val joke = jokesList[randomIndex].copy() // Создаем копию шутки

            // Если аватарка не указана в json, то выбираем случайную из доступных по категории
            if (joke.avatar == null) {
                val avatars = categoryAvatars[joke.category] ?: defaultAvatars
                val usedAvatars = usedAvatarsPerCategory.getOrPut(joke.category) { mutableSetOf() }

                val availableAvatars = avatars.filter { it !in usedAvatars }
                val selectedAvatar = if (availableAvatars.isNotEmpty()) {
                    availableAvatars.random()
                } else {
                    defaultAvatars.random()
                }
                joke.avatar = selectedAvatar
                usedAvatars.add(selectedAvatar)
            }

            joke.id = ind++
            newSelectedJokes.add(joke)
            usedJokesIndices.add(randomIndex)
        }

        selectedJokes = newSelectedJokes
        return selectedJokes
    }

    // Сброс использованных шуток
    fun reset() {
        selectedJokes.clear()
        usedJokesIndices.clear()
    }

    // Получение списка выбранных шуток
    fun getSelectedJokes(): List<ViewTyped> {
        return selectedJokes.toList()
    }
}