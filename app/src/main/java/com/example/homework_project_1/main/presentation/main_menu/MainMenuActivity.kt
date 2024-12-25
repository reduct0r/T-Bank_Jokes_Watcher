package com.example.homework_project_1.main.presentation.main_menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.homework_project_1.R
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.utils.JsonReader
import javax.inject.Inject

class MainMenuActivity : AppCompatActivity() {
    @Inject
    lateinit var jsonReader: JsonReader

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jsonReader.parseJSON(this)
        // Загрузка начального фрагмента один раз
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainMenuFragment())
                .commit()
        }
    }
}