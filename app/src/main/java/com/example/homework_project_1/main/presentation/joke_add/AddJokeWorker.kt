package com.example.homework_project_1.main.presentation.joke_add

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.homework_project_1.main.data.provider.AvatarProvider
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.model.Flags
import com.example.homework_project_1.main.data.model.JokeDTO
import com.example.homework_project_1.main.di.annotations.JokesRepositoryA
import com.example.homework_project_1.main.domain.usecase.InsertJokeUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

// Для фонового добавления шутки в хранилище
class AddJokeWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    @JokesRepositoryA private val insertJokeUseCase: InsertJokeUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val id = inputData.getInt("id", -1)
        if (id == -1) return Result.failure()

        val question = inputData.getString("question") ?: return Result.failure()
        val answer = inputData.getString("answer") ?: return Result.failure()
        val category = inputData.getString("category") ?: return Result.failure()
        val avatarUriString = inputData.getString("avatarByteArr")
        val source = inputData.getString("source")?.let { JokeSource.valueOf(it) } ?: return Result.failure()

        val avatarByteArr: ByteArray? = avatarUriString?.let { uriString ->
            try {
                val uri = Uri.parse(uriString)
                applicationContext.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            } catch (e: Exception) {
                null
            }
        }

        val joke = JokeDTO(
            id = id,
            question = question,
            answer = answer,
            category = category,
            avatarByteArr = avatarByteArr,
            avatar = if (avatarByteArr == null) AvatarProvider.getAvatarsByCategory(category).random() else null,
            flags = Flags(
                nsfw = false,
                religious = false,
                political = false,
                racist = false,
                sexist = false,
                explicit = false
            ),
            lang = "en",
            source = source,
            isFavorite = false
        )

        return try {
            insertJokeUseCase(joke)
            Result.success()
        } catch (e: SQLiteConstraintException) {
            Result.failure()
        }
        catch (e: Exception) {
            Result.failure()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted context: Context, @Assisted params: WorkerParameters): AddJokeWorker
    }
}