package com.example.homework_project_1.main.ui.joke_add

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.UUID

class AddJokeViewModel(application: Application) : AndroidViewModel(application) {

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

        // Получение экземпляра WorkManager с использованием контекста приложения
        val workManager = WorkManager.getInstance(getApplication())

        // Добавление задачи в очередь
        workManager.enqueue(addJokeRequest)

        // Наблюдение за состоянием задачи
        workManager.getWorkInfoByIdLiveData(addJokeRequest.id)
            .observeForever { workInfo ->
                if (workInfo != null) {
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            _addJokeStatus.value = AddJokeStatus.Success
                        }
                        WorkInfo.State.FAILED -> {
                            _addJokeStatus.value = AddJokeStatus.Error("Can't add joke")
                        }
                        WorkInfo.State.BLOCKED -> {
                            _addJokeStatus.value = AddJokeStatus.Error("Work blocked")
                        }
                        WorkInfo.State.CANCELLED -> {
                            _addJokeStatus.value = AddJokeStatus.Error("Work cancelled")
                        }

                        WorkInfo.State.ENQUEUED -> {}
                        WorkInfo.State.RUNNING -> {}
                    }
                }
            }
    }
}

sealed class AddJokeStatus {
    data object Success : AddJokeStatus()
    data class Error(val message: String) : AddJokeStatus()
}
