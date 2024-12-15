package com.example.homework_project_1.main.presentation.joke_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.homework_project_1.R
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.utils.JsonReader
import javax.inject.Inject

class JokeListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Загрузка начального фрагмента один раз
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, JokeListFragment())
                .commit()
        }
    }
}

