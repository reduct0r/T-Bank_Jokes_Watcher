package com.example.homework_project_1.main.ui.joke_add

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.homework_project_1.main.data.Joke
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.JokesRepository
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

        val joke = Joke(
            id = UUID.randomUUID().hashCode(),
            question = question,
            answer = answer,
            category = category,
            avatarUri = avatarUri,
            avatar = null
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
