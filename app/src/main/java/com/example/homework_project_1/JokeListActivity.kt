package com.example.homework_project_1;
import android.app.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.homework_project_1.databinding.ActivityJokeListBinding
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.recycler.JokeAdapter
import com.example.homework_project_1.main.recycler.util.JokeItemCallback


class JokeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJokeListBinding
    private val itemCallback = JokeItemCallback()
    private val adapter = JokeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJokeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //createLinearLayoutList()

        createRecyclerViewList()

        val generator = JokesGenerator()

        binding.button.setOnClickListener {
            val data = generator.generateData()
            adapter.setNewData(data)
        }
    }

    private fun createRecyclerViewList() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 1)
    }
}