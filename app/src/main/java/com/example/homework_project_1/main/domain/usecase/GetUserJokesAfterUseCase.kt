package com.example.homework_project_1.main.domain.usecase

import com.example.homework_project_1.main.data.database.JokeDbEntity
import com.example.homework_project_1.main.domain.repository.JokesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserJokesAfterUseCase @Inject constructor(
    private var repository: JokesRepository
) {
    operator fun invoke(timestamp: Long): Flow<List<JokeDbEntity>> {
        return repository.getUserJokesAfter(timestamp)
    }
}