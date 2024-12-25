package com.example.homework_project_1.main.domain.generator
import androidx.lifecycle.MutableLiveData
import com.example.homework_project_1.main.data.utils.JsonReader
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.data.provider.AvatarProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

// Класс для генерации шуток
class JokesGenerator @Inject constructor(
    private val jsonReader: JsonReader
) {
    private var jokesList: List<JokeDTO> = emptyList()                  // Список всех шуток
    private val _loading = MutableLiveData<Boolean>()
    private var selectedJokes = mutableListOf<JokeDTO>()                // Список выбранных шуток
    private var ind = 0                                                 // Уникальный индекс (счетчик) для шуток
    private var usedJokesIndices = mutableSetOf<Int>()                  // Индексы использованных шуток

    // Генерация данных для списка из рандомных шуток без повторения
    suspend fun generateJokesData(jokesAmount: Int = 15): List<JokeDTO> {
        _loading.value = true
        withContext(Dispatchers.IO) {
            jokesList = jsonReader.getJokes()
            _loading.postValue(false)
        }

        val newSelectedJokes = mutableListOf<JokeDTO>()
        val usedAvatarsPerCategory = mutableMapOf<String, MutableSet<Int>>()

        var attempts = 0
        val maxAttempts = jokesList.size * 3

        while (newSelectedJokes.size < jokesAmount && jokesList.isNotEmpty() && attempts < maxAttempts) {
            attempts++

            val randomIndex = Random.nextInt(jokesList.size)

            if (randomIndex in usedJokesIndices) {
                continue // Пропустить, если шутка уже была использована
            }
            val joke = jokesList[randomIndex].copy() // Создаем копию шутки

            // Если аватарка не указана в json, то выбираем случайную из доступных по категории
            if (joke.avatar == null && joke.avatarByteArr == null) {
                val avatars = AvatarProvider.getAvatarsByCategory(joke.category)
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

    fun setAvatar(newJokes: List<JokeDTO>): List<JokeDTO> {
        newJokes.forEach { joke ->
            if (joke.avatar == null && joke.avatarByteArr == null) {
                val avatars = AvatarProvider.getAvatarsByCategory(joke.category)
                val usedAvatars = mutableSetOf<Int>()
                val availableAvatars = avatars.filter { it !in usedAvatars }
                val selectedAvatar = if (availableAvatars.isNotEmpty()) {
                    availableAvatars.random()
                } else {
                    AvatarProvider.getDefaultAvatars().random()
                }
                joke.avatar = selectedAvatar
            }
        }
        return newJokes
    }
}
