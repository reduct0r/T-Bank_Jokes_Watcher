package com.example.homework_project_1.main.data
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

// Класс для генерации шуток
object JokesGenerator {
    private var jokesList: List<Joke> = emptyList()          // Список всех шуток
    private val _loading = MutableLiveData<Boolean>()
    //val loading: LiveData<Boolean> get() = _loading
    private var selectedJokes = mutableListOf<Joke>()         // Список выбранных шуток
    private val categoryAvatars = AvatarProvider.getCategoryAvatars()   // Наборы аватарок по категориям
    private var ind = 0                                                 // Уникальный индекс (счетчик) для шуток
    private var usedJokesIndices = mutableSetOf<Int>()                  // Индексы использованных шуток

    // Генерация данных для списка из рандомных шуток без повторения
    suspend fun generateJokesData(): List<Joke> {
        _loading.value = true
        withContext(Dispatchers.IO) {
            jokesList = JokesRepository.getJokes()
            _loading.postValue(false)
        }

        val newSelectedJokes = mutableListOf<Joke>()
        val usedAvatarsPerCategory = mutableMapOf<String, MutableSet<Int>>()

        var attempts = 0
        val maxAttempts = jokesList.size * 3

        while (newSelectedJokes.size < 20 && jokesList.isNotEmpty() && attempts < maxAttempts) {
            attempts++

            val randomIndex = Random.nextInt(jokesList.size)

            if (randomIndex in usedJokesIndices) {
                continue // Пропустить, если шутка уже была использована
            }
            val joke = jokesList[randomIndex].copy() // Создаем копию шутки

            // Если аватарка не указана в json, то выбираем случайную из доступных по категории
            if (joke.avatar == null && joke.avatarUri == null) {
                val avatars = categoryAvatars[joke.category] ?: AvatarProvider.getDefaultAvatars()
                val usedAvatars = usedAvatarsPerCategory.getOrPut(joke.category) { mutableSetOf() }

                val availableAvatars = avatars.filter { it !in usedAvatars }
                val selectedAvatar = if (availableAvatars.isNotEmpty()) {
                    availableAvatars.random()
                } else {
                    AvatarProvider.getDefaultAvatars().random()
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
    fun getSelectedJokes(): List<Joke> {
        return selectedJokes.toList()
    }
}