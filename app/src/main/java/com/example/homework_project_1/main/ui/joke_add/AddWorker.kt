package com.example.homework_project_1.main.ui.joke_add

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.homework_project_1.main.data.AvatarProvider
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.JokesRepository
import com.example.homework_project_1.main.data.model.FlagsDTO
import com.example.homework_project_1.main.data.model.JokeDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

// Для фонового добавления шутки в хранилище (когда activity закрыта)
class AddJokeWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val question = inputData.getString("question") ?: return Result.failure()
        val answer = inputData.getString("answer") ?: return Result.failure()
        val category = inputData.getString("category") ?: return Result.failure()
        val avatarUriString = inputData.getString("avatarUri")
        val avatarUri = avatarUriString?.let { Uri.parse(it) }
        val source = inputData.getString("source")?.let { JokeSource.valueOf(it) } ?: return Result.failure()

        val joke = JokeDTO(
            id = UUID.randomUUID().hashCode(),
            question = question,
            answer = answer,
            category = category,
            avatarUri = avatarUri?.toString(),
            avatar = if (avatarUri == null) AvatarProvider.getAvatarsByCategory(category).random() else null,
            flags = FlagsDTO(
                nsfw = false,
                religious = false,
                political = false,
                racist = false,
                sexist = false,
                explicit = false
            ),
            lang = "en",
            source = source
        )

        return try {
            withContext(Dispatchers.IO) {
                JokesRepository.addNewJoke(joke)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
