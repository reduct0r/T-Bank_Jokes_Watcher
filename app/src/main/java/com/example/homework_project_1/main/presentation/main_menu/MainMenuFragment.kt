package com.example.homework_project_1.main.presentation.main_menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.homework_project_1.databinding.FragmentMainMenuBinding
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.presentation.joke_add.AddJokeActivity
import com.example.homework_project_1.main.presentation.joke_list.JokeListActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.util.Timer
import java.util.TimerTask
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

    override fun onStop() {
        super.onStop()
        viewModel.stopUpdatingLines()
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
