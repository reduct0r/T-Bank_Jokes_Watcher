package com.example.homework_project_1.main.ui.joke_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.homework_project_1.R

class JokeListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Загрузка начального фрагмента только один раз
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, JokeListFragment())
                .commit()
        }
    }
}
