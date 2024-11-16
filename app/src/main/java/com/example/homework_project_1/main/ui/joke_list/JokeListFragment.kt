package com.example.homework_project_1.main.ui.joke_list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.FragmentJokeListBinding
import com.example.homework_project_1.main.data.JokesGenerator
import com.example.homework_project_1.main.data.JokesRepository
import com.example.homework_project_1.main.ui.joke_add.AddJokeActivity
import com.example.homework_project_1.main.ui.joke_details.JokeDetailsFragment
import com.example.homework_project_1.main.ui.joke_list.recycler.adapter.ViewTypedListAdapter
import kotlinx.coroutines.launch

class JokeListFragment : Fragment() {
    private var _binding: FragmentJokeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JokeListViewModel by viewModels {
        JokesViewModelFactory()
    }

    private val adapter = ViewTypedListAdapter { joke ->
        // Навигация к деталям шутки с использованием идентификатора
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,  // Анимация для входа нового фрагмента
                R.anim.slide_out_left,  // Анимация для выхода текущего фрагмента
                R.anim.slide_in_left,   // Анимация для возврата к предыдущему фрагменту
                R.anim.slide_out_right  // Анимация для снятия нового фрагмента
            )
            .replace(R.id.fragment_container, JokeDetailsFragment.newInstance(joke))
            .addToBackStack(null)
            .commit()
    }

    // Вывод ошибки
    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentJokeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Создаем RecyclerView и устанавливаем слушатель на кнопку
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRecyclerViewList()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        JokesRepository.parseJSON(requireContext()) // Чтение данных из JSON файла

        // Наблюдение за изменениями в LiveData
        viewModel.jokes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // Наблюдение за ошибками
        viewModel.error.observe(viewLifecycleOwner) {
            showError(it)
        }

        JokesGenerator.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.buttonGenerateJokes.isEnabled = !isLoading
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.buttonGenerateJokes.text = getString(R.string.loading)
            } else {
                binding.progressBar.visibility = View.GONE
                binding.buttonGenerateJokes.text = getString(R.string.generate_jokes)
            }
        }

        // Обработка нажатия на кнопку генерации шуток
        binding.buttonGenerateJokes.setOnClickListener {
            lifecycleScope.launch {
                viewModel.generateJokes()
                if (viewModel.showGeneratedData().isEmpty() && binding.progressBar.visibility != View.VISIBLE) {
                    binding.buttonGenerateJokes.text = getString(R.string.reset_used_jokes)
                    showError("No new jokes are available.")
                    viewModel.resetJokes()
                } else if (binding.progressBar.visibility != View.VISIBLE){
                    binding.buttonGenerateJokes.text = getString(R.string.generate_jokes)
                }
            }
        }

        // Обработка нажатия на кнопку добавления шутки
        binding.buttonAddJoke.setOnClickListener {
            // Запуск AddJokeActivity
            val intent = Intent(requireContext(), AddJokeActivity::class.java)
            startActivity(intent)
        }
    }

    // Создаем RecyclerView
    private fun createRecyclerViewList() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
