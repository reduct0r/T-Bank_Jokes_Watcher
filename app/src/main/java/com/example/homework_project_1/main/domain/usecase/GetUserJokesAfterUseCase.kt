package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.repository.JokesRepositoryImpl
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp

class GetUserJokesAfterUseCase {
    operator fun invoke(timestamp: Long): Flow<List<JokeDbEntity>> {
        return JokesRepositoryImpl.getUserJokesAfter(timestamp)
    }
}