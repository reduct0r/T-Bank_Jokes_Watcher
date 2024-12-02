package com.example.homework_project_1.main.ui.joke_list

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.homework_project_1.R
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.JokesRepository
import com.example.homework_project_1.main.data.repository.RepositoryImpl
import kotlinx.coroutines.launch

class JokeListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JokesRepository.parseJSON(this)

        // Загрузка начального фрагмента только один раз
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, JokeListFragment())
                .commit()
        }
    }
}
