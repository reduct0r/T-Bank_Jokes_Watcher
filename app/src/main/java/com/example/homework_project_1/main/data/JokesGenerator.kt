package com.example.homework_project_1.main.data

import androidx.annotation.DrawableRes
import com.example.homework_project_1.R
import com.example.homework_project_1.main.data.ViewTyped.*
import kotlin.random.Random

// Класс для генерации шуток
object JokesGenerator {
    // Наборы аватарок
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

    // Инициализация списка шуток
    private val jokesList: MutableList<Joke> = mutableListOf(
        createJoke("General", "Why did the duck go to the doctor?", "It had a quack."),
        createJoke("General", "Why did the bee become a singer?", "Because it had the buzz!"),
        createJoke("General", "Why did the skeleton go to the party alone?", "He had no body to go with."),
        createJoke("General", "Why don't scientists trust stairs?", "Because they're always up to something."),
        createJoke("Programming", "Why don't programmers like to write comments?", "Because the code should speak for itself."),
        createJoke("Programming", "Why did the developer disassemble the desk lamp?", "Because the manual said to debug."),
        createJoke("Programming", "Why was the function looking for an argument?", "It needed closure."),
        createJoke("Programming", "Why do Python developers prefer snakes?", "Because they are flexible with their syntax."),
        createJoke("Math", "Why is geometry never sad?", "Because it always finds the common denominator."),
        createJoke("Math", "Why did the equal simplify the equation?", "It felt it was dragging on too long."),
        createJoke("Math", "Why don't mathematicians argue?", "Because they always agree on the terms."),
        createJoke("Math", "Why is the number six afraid of seven?", "Because seven eight (ate) nine."),
        createJoke("Science", "Why is an electron never lonely?", "Because it always seeks a pair."),
        createJoke("Science", "Why does the chemist need a break?", "Because reactions take time."),
        createJoke("Science", "Why don't astronauts get together?", "They need space."),
        createJoke("Science", "Why can’t you trust a quark?", "Because it’s always changing."),
        createJoke("Tech", "Why does the server never laugh?", "Because it is busy processing data."),
        createJoke("Tech", "Why is the keyboard always in the spotlight?", "Because all of its keys are exposed."),
        createJoke("Tech", "Why was the computer cold?", "It left its Windows open."),
        createJoke("Tech", "Why did the smartphone go to school?", "It wanted to improve its connections."),
        createJoke("Programming", "Why is the algorithm so persistent?", "Because it always wants to find a solution."),
        createJoke("Programming", "Why does Python enjoy the cold?", "Because there's plenty of room for its modules in the freezer."),
        createJoke("Programming", "Why did the coder get kicked out of the casino?", "They kept counting cards."),
        createJoke("Programming", "Why do programmers hate nature?", "Because it has too many bugs."),
        createJoke("Math", "Why did the fraction go on vacation?", "To get itself in order."),
        createJoke("Math", "Why did the linear equation meet the quadratic?", "To find common points."),
        createJoke("Math", "Why was the math book sad?", "It had too many problems."),
        createJoke("Math", "Why didn't the antiderivative do well at the party?", "It couldn't find its integral."),
        createJoke("Science", "Why is a microbiologist never lost in conditions?", "Because they always have a control experiment on hand."),
        createJoke("Science", "Why is the astronomer always in trend?", "Because they follow all the new flares in the sky."),
        createJoke("Science", "Why can't you trust bacteria?", "They multiply in the dark."),
        createJoke("Science", "Why did the geologist go on a hike?", "To rock and roll.")
    )
    private var ind = 0;
    // Генерация данных для списка
    fun generateJokesData(): List<ViewTyped> {
        val selectedJokes = mutableListOf<Joke>()
        val usedAvatarsPerCategory = mutableMapOf<String, MutableSet<Int>>()

        // Копируем список шуток, чтобы не изменять оригинал при выборе
        val availableJokes = jokesList.toMutableList()

        // Пока не набрали 7 шуток или не исчерпали список
        while (selectedJokes.size < 7 && availableJokes.isNotEmpty()) {
            // Выбираем случайную шутку
            val randomIndex = Random.nextInt(availableJokes.size)
            val joke = availableJokes[randomIndex]

            // Получаем список аватарок для категории
            val avatars = categoryAvatars[joke.category] ?: defaultAvatars

            // Инициализируем набор использованных аватарок для категории, если еще не инициализирован
            val usedAvatars = usedAvatarsPerCategory.getOrPut(joke.category) { mutableSetOf() }

            // Фильтруем доступные аватарки, исключая уже использованные для этой категории
            val availableAvatars = avatars.filter { it !in usedAvatars }

            if (availableAvatars.isNotEmpty()) {

                val selectedAvatar = availableAvatars.random() // Выбираем случайную доступную аватарку
                joke.avatar = selectedAvatar
                joke.id = ind
                ind++
                // Добавляем аватарку в набор использованных для категории
                usedAvatars.add(selectedAvatar)

                // Добавляем шутку в выбранные
                selectedJokes.add(joke)

                // Удаляем шутку из доступных, чтобы избежать повторений
                availableJokes.removeAt(randomIndex)
            } else {
                // Если нет доступных аватарок для этой категории, исключаем шутку из выбора
                availableJokes.removeAt(randomIndex)
            }
        }

        return selectedJokes
    }


    // Добавление шутки с указанием категории, вопроса и ответа
    private fun createJoke(category: String, question: String, answer: String): Joke {

        return Joke(
            id = 0,
            avatar = null,
            category = category,
            question = question,
            answer = answer
        )
    }
}

