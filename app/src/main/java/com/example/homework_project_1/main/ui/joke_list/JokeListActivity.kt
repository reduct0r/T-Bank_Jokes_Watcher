package com.example.homework_project_1.main.ui.joke_list

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.ActivityJokeListBinding
import com.example.homework_project_1.main.ui.joke_list.recycler.adapter.ViewTypedListAdapter
import com.example.homework_project_1.main.ui.joke_details.JokeDetailsActivity

class JokeListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJokeListBinding

    private lateinit var viewModel: JokeListViewModel

    private val adapter = ViewTypedListAdapter{
        startActivity(JokeDetailsActivity.getInstance(this, it))
    }

    private fun initViewModel() {
        val factory = JokesViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[JokeListViewModel::class.java]

        viewModel.jokes.observe(this) {
            adapter.submitList(it)
        }

        viewModel.error.observe(this) {
            showError(it)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Создаем RecyclerView и устанавливаем слушатель на кнопку
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJokeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createRecyclerViewList()
        initViewModel()

        // При нажатии на кнопку генерируем новые данные и обновляем список или выводим ошибку и сбрасываем использованные шутки
        binding.button.setOnClickListener {
            viewModel.generateJokes()
            if (viewModel.showGeneratedData().isEmpty()) {
                binding.button.text = getString(R.string.reset_used_jokes)
                showError("No new jokes are available.")
                  viewModel.resetJokes()
            }
          else {
            binding.button.text = getString(R.string.generate_jokes)
          }
        }
    }

    // Создаем RecyclerView
    private fun createRecyclerViewList() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 1)
    }


}