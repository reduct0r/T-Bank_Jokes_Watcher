package com.example.homework_project_1.main.presentation.joke_add

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.JokeSource
import java.util.UUID

class AddJokeViewModel(application: Application) : AndroidViewModel(application) {

    private val _addJokeStatus = MutableLiveData<AddJokeStatus>()
    val addJokeStatus: LiveData<AddJokeStatus> get() = _addJokeStatus

    fun addJoke(question: String, answer: String, category: String, avatarUri: Uri?, source: JokeSource) {
        // Создание входных данных для менеджера
        val data = Data.Builder()
            .putInt("id", UUID.randomUUID().hashCode())
            .putString("avatar", null)
            .putString("question", question)
            .putString("answer", answer)
            .putString("category", category)
            .putString("avatarByteArr", avatarUri?.toString())
            .putString("source", source.name)
            .build()

        // Создание задачи для добавления шутки
        val workRequest = OneTimeWorkRequestBuilder<AddJokeWorker>()
            .setInputData(data)
            .build()

        // Получение экземпляра WorkManager с использованием контекста приложения
        val workManager = WorkManager.getInstance(App.instance)

        // Добавление задачи в очередь
        workManager.enqueue(workRequest)

        // Наблюдение за состоянием задачи
        workManager.getWorkInfoByIdLiveData(workRequest.id)
            .observeForever { workInfo ->
                if (workInfo != null) {
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            _addJokeStatus.value = AddJokeStatus.Success
                        }
                        WorkInfo.State.FAILED -> {
                            _addJokeStatus.value = AddJokeStatus.Error("Can't add joke: Maybe it already exists")
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

    sealed class AddJokeStatus {
        data object Success : AddJokeStatus()
        data class Error(val message: String) : AddJokeStatus()
    }
}

