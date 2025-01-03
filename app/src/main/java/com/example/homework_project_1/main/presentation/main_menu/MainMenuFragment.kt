package com.example.homework_project_1.main.presentation.main_menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.FragmentMainMenuBinding
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.presentation.favourite_jokes.FavoriteJokeListFragment
import com.example.homework_project_1.main.presentation.joke_list.JokeListActivity
import javax.inject.Inject

class MainMenuFragment : Fragment() {
    @Inject
    lateinit var mainViewModelFactory: MainMenuViewModelFactory

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainMenuViewModel by viewModels {
        mainViewModelFactory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.textLine.observe(viewLifecycleOwner) { newText ->
            animateTextChange(newText)
        }

        binding.jokesButton.setOnClickListener {
            val intent = Intent(context, JokeListActivity::class.java)
            startActivity(intent)
        }

        binding.favoritesButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragment_container, FavoriteJokeListFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun animateTextChange(newText: String) {
        binding.textView.animate()
            .alpha(0f)
            .setDuration(500)
            .withEndAction {
                binding.textView.text = newText
                binding.textView.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .start()
            }
            .start()
    }

    override fun onStart() {
        super.onStart()
        viewModel.startUpdatingLines()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
