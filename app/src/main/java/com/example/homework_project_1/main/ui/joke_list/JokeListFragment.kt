package com.example.homework_project_1.main.ui.joke_list

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    private val adapter = ViewTypedListAdapter { jokePos ->
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.fragment_container, JokeDetailsFragment.newInstance(jokePos))
            .addToBackStack(null)
            .commit()
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRecyclerViewList()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        // Наблюдение за данными шуток
        viewModel.jokes.observe(viewLifecycleOwner) { jokes ->
            adapter.submitList(jokes)
            if (viewModel.getRenderedJokesList().isEmpty()) {
                showError("No new jokes are available.")
                binding.buttonGenerateJokes.text = getString(R.string.reset_used_jokes)
                binding.progressBar.visibility = View.GONE
                viewModel.resetJokes()
            }
        }

        // Наблюдение за ошибками
        viewModel.error.observe(viewLifecycleOwner) {
            showError(it)
        }

        // Наблюдение за состоянием загрузки
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            binding.buttonGenerateJokes.isEnabled = !isLoading
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.buttonGenerateJokes.text = getString(R.string.loading)
            } else if (viewModel.getRenderedJokesList().isNotEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.buttonGenerateJokes.text = getString(R.string.generate_jokes)
            }
        }

        viewModel.isLoadingEl.observe(viewLifecycleOwner) { isLoadingEl: Boolean ->
            if (isLoadingEl) {
                adapter.addLoadingFooter()
            } else {
                adapter.removeLoadingFooter()
            }
        }

        // Обработка нажатия на кнопку генерации шуток
        binding.buttonGenerateJokes.setOnClickListener {
            lifecycleScope.launch {
                viewModel.generateJokes()
            }
        }

        // Обработка нажатия на кнопку добавления шутки
        binding.buttonAddJoke.setOnClickListener {
            val intent = Intent(requireContext(), AddJokeActivity::class.java)
            startActivity(intent)
            viewModel.loadMoreJokes()
        }

        binding.recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(binding.recyclerView.layoutManager!!) {
            override fun onLoadMore() {
                viewModel.loadMoreJokes()
            }
        })
    }

    private fun createRecyclerViewList() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
