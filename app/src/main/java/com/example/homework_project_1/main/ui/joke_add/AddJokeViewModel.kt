import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.homework_project_1.main.ui.joke_add.AddJokeWorker
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
                        androidx.work.WorkInfo.State.ENQUEUED -> {
                            // Handle ENQUEUED state if needed
                        }
                        androidx.work.WorkInfo.State.RUNNING -> {
                            // Handle RUNNING state if needed
                        }
                        androidx.work.WorkInfo.State.SUCCEEDED -> {
                            _addJokeStatus.value = AddJokeStatus.Success
                        }
                        androidx.work.WorkInfo.State.FAILED -> {
                            _addJokeStatus.value = AddJokeStatus.Error("Can't add joke")
                        }
                        androidx.work.WorkInfo.State.BLOCKED -> {
                            _addJokeStatus.value = AddJokeStatus.Error("Work blocked")
                        }
                        androidx.work.WorkInfo.State.CANCELLED -> {
                            _addJokeStatus.value = AddJokeStatus.Error("Work cancelled")
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