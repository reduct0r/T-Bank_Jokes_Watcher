package com.example.homework_project_1.main.ui.joke_add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.UUID
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class AddJokeViewModel : ViewModel() {

    private val _addJokeStatus = MutableLiveData<AddJokeStatus>()
    val addJokeStatus: LiveData<AddJokeStatus> get() = _addJokeStatus

    fun addJoke(question: String, answer: String, category: String, avatarUri: Uri?) {
        // Создание входных данных для менеджера
        val data = Data.Builder()
            .putInt("id", UUID.randomUUID().hashCode())
            .putString("avatar", null)
            .putString("question", question)
            .putString("answer", answer)
            .putString("category", category)
            .putString("avatarUri", avatarUri?.toString())
            .build()

        // Создание задачи для добавления шутки
        val addJokeRequest = OneTimeWorkRequestBuilder<AddJokeWorker>()
            .setInputData(data)
            .build()

        // Добавление задачи в очередь
        WorkManager.getInstance().enqueue(addJokeRequest)

        // Наблюдение за состоянием задачи
        WorkManager.getInstance().getWorkInfoByIdLiveData(addJokeRequest.id)
            .observeForever { workInfo ->
                if (workInfo != null) {
                    when (workInfo.state) {
                        androidx.work.WorkInfo.State.SUCCEEDED -> {
                            _addJokeStatus.value = AddJokeStatus.Success
                        }
                        androidx.work.WorkInfo.State.FAILED -> {
                            _addJokeStatus.value = AddJokeStatus.Error("Can't add joke")
                        }
                        else -> {
                            // do nothing
                        }
                    }
                }
            }
    }
}

sealed class AddJokeStatus {
    data object Success : AddJokeStatus()
    data class Error(val message: String) : AddJokeStatus()
}