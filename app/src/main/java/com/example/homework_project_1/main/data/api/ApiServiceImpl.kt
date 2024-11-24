package com.example.homework_project_1.main.data.api

import com.example.homework_project_1.main.data.model.JokeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiServiceImpl private constructor(private val client: HttpClient) : ApiService {
    companion object {
        private const val BASE_URL = "https://v2.jokeapi.dev/joke/Any"
        private const val BLACKLIST_FLAGS = "nsfw,religious,political,racist,sexist,explicit"

        @Volatile
        private var instance: ApiServiceImpl? = null

        fun getInstance(): ApiServiceImpl {
            return instance ?: synchronized(this) {
                instance ?: ApiServiceImpl(ApiClient.httpClient).also { instance = it }
            }
        }
    }

    override suspend fun getJokes(amount: Int): JokeResponse {
        return client.get(BASE_URL) {
            parameter("blacklistFlags", BLACKLIST_FLAGS)
            parameter("type", "twopart")
            parameter("amount", amount)
        }.body()
    }
}