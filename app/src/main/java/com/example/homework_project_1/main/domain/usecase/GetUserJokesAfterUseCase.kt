package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.data.repository.JokesRepositoryImpl
import com.example.homework_project_1.main.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp
import javax.inject.Inject

class GetUserJokesAfterUseCase @Inject constructor(
    private var repository: Repository
) {
    operator fun invoke(timestamp: Long): Flow<List<JokeDbEntity>> {
        return repository.getUserJokesAfter(timestamp)
    }
}