package com.example.homework_project_1.main.ui.joke_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.FragmentJokeDetailsBinding
import com.example.homework_project_1.main.data.ViewTyped


class JokeDetailsFragment : Fragment() {

    companion object {
        private const val JOKE_POSITION_EXTRA = "JOKE_POSITION"

        fun newInstance(jokePosition: Int) = JokeDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(JOKE_POSITION_EXTRA, jokePosition)
            }
        }
    }

    private var _binding: FragmentJokeDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JokeDetailsViewModel by viewModels {
        arguments?.getInt(JOKE_POSITION_EXTRA)?.let { JokesDetailsViewModelFactory(it) }
            ?: throw IllegalArgumentException("JOKE_POSITION is missing")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentJokeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Настройка данных и обработка событий
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadJoke()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        if (viewModel.getPosition() == -1) {
            handleError("Incorrect joke position.")
        }

        // Наблюдение за изменениями в LiveData
        viewModel.joke.observe(viewLifecycleOwner) { joke ->
            setupJokesData(joke)
        }

        // Наблюдение за ошибками
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            handleError(errorMessage)
        }

        // Установка текста на кнопку "Добавить в избранное"
        binding.addToFavorites.text = getString(R.string.add_to_favorites)
        // Обработка нажатия на кнопку "Назад"
        binding.buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    // Установка данных в элементы экрана
    private fun setupJokesData(item: ViewTyped.Joke) {
        with(binding) {
            question.text = item.question
            answer.text = item.answer
            category.text = item.category
            item.avatar?.let {
                avatar.setImageResource(it)
            }
        }
    }

    // Обработка ошибок
    private fun handleError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}