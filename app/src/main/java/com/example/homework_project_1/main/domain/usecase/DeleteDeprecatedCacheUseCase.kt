package com.example.homework_project_1.main.domain.usecase

import android.util.Log
import com.example.homework_project_1.main.data.database.JokesWatcherDatabase
import javax.inject.Inject

class DeleteDeprecatedCacheUseCase @Inject constructor(
    private var jokeDb: JokesWatcherDatabase
) {
    suspend operator fun invoke(lastTimestamp: Long): Boolean {
        val deprecatedCache = jokeDb.jokeDao().getCachedJokesBefore(lastTimestamp)
        if (deprecatedCache.isEmpty()) {
            return false
        } else {
            deprecatedCache.forEach { jokeDb.jokeDao().deleteCache(it.id!!) }
            Log.d("DeleteDeprecatedCacheUseCase", deprecatedCache.map { it.id }.toString())
            return true
        }
    }
}