package com.example.homework_project_1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.homework_project_1.databinding.ActivityJokeListBinding
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.recycler.adapter.ViewTypedListAdapter

class JokeListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJokeListBinding
    private val adapter = ViewTypedListAdapter{
        startActivity(JokeDetailsActivity.getInstance(this, it))
    }

    // Создаем RecyclerView и устанавливаем слушатель на кнопку
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJokeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createRecyclerViewList()
        val generator = JokesGenerator

        // При нажатии на кнопку генерируем новые данные и обновляем список
        binding.button.setOnClickListener {
            val data =  listOf(
                generator.generateJokesData()
            ).flatten()
            adapter.submitList(data)
            Log.d("JokeListActivity", "Data: ${data}")
        }
    }

    // Создаем RecyclerView
    private fun createRecyclerViewList() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 1)
    }


}