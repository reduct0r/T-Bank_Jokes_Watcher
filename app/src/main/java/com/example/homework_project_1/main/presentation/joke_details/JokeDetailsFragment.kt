package com.example.homework_project_1.main.presentation.joke_details

import android.content.Context
import android.graphics.BitmapFactory
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
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.presentation.utils.ViewTyped
import javax.inject.Inject

class JokeDetailsFragment : Fragment() {

    @Inject
    lateinit var jokeDetailsFactory: JokeDetailsViewModel.Factory

    companion object {
        private const val JOKE_EXTRA = "joke_extra"

        fun newInstance(joke: ViewTyped.JokeUIModel): JokeDetailsFragment {
            val fragment = JokeDetailsFragment()
            val bundle = Bundle().apply {
                putSerializable(JOKE_EXTRA, joke)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as App).appComponent.inject(this)
        super.onAttach(context)
    }

    private var _binding: FragmentJokeDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JokeDetailsViewModel by viewModels {
        val joke = arguments?.getSerializable(JOKE_EXTRA) as? ViewTyped.JokeUIModel
            ?: throw IllegalArgumentException("JOKE_EXTRA is missing")

        JokeDetailsViewModelFactory(jokeDetailsFactory, joke)
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


        // Наблюдение за изменениями в LiveData
        viewModel.joke.observe(viewLifecycleOwner) { joke ->
            setupJokesData(joke)
        }

        // Наблюдение за ошибками
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            handleError(errorMessage)
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            binding.addToFavorites.text = if (isFavorite) {
                getString(R.string.remove_from_favorites)
            } else {
                getString(R.string.add_to_favorites)
            }

            binding.favoriteStar.isSelected = isFavorite
        }

        binding.addToFavorites.setOnClickListener {
            if (viewModel.isFavorite.value == true) {
                viewModel.removeFromFavorites()
            } else {
                viewModel.addToFavorites()
            }
        }

        binding.favoriteStar.setOnClickListener {
            if (viewModel.isFavorite.value == true) {
                viewModel.removeFromFavorites()
            } else {
                viewModel.addToFavorites()
            }
        }

        // Обработка нажатия на кнопку "Назад"
        binding.buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Установка данных в лейбл
        when (viewModel.joke.value?.source) {
            JokeSource.DEFAULT -> {
                binding.sourceLabel.text = getString(R.string.default_label)
            }
            JokeSource.NETWORK -> {
                binding.sourceLabel.text = getString(R.string.network)
            }
            JokeSource.USER -> {
                binding.sourceLabel.text = getString(R.string.own)
            }
            JokeSource.DATABASE -> {
                binding.sourceLabel.text = getString(R.string.database)
            }
            JokeSource.CACHE -> {
                binding.sourceLabel.text = getString(R.string.cache)
            }
            else -> {
                binding.sourceLabel.text = getString(R.string.unknown)
            }
        }
    }

    // Установка данных в элементы экрана
    private fun setupJokesData(item: ViewTyped.JokeUIModel) {
        with(binding) {
            question.text = item.question
            answer.text = item.answer
            category.text = item.category
            if (item.avatarByteArr != null) {
                val bitmap = BitmapFactory.decodeByteArray(item.avatarByteArr, 0, item.avatarByteArr.size)
                avatar.setImageBitmap(bitmap)
            } else {
                item.avatar?.let {
                    avatar.setImageResource(it)
                }
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