package com.example.homework_project_1.main.ui.joke_list

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.FragmentJokeListBinding
import com.example.homework_project_1.main.ui.joke_add.AddJokeActivity
import com.example.homework_project_1.main.ui.joke_details.JokeDetailsFragment
import com.example.homework_project_1.main.ui.joke_list.recycler.adapter.ViewTypedListAdapter

class JokeListFragment : Fragment() {
    private var _binding: FragmentJokeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JokeListViewModel by viewModels {
        JokesViewModelFactory()
    }
    private var next = false
    private var lastErrorTime: Long = 0
    private val errorInterval: Long = 6000 // Интервал

    private val handler = Handler(Looper.getMainLooper())
    private val retryRunnable = Runnable {
        viewModel.loadMoreJokes()
    }

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

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
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // Наблюдение за данными шуток
        viewModel.jokes.observe(viewLifecycleOwner) { jokes ->
            adapter.submitList(jokes)

            if (jokes.isEmpty()) {
                showError("No new jokes are available.")
                binding.buttonGenerateJokes.text = getString(R.string.reset_used_jokes)
                binding.progressBar.visibility = View.GONE
            }
            else {
                adapter.submitList(jokes) {
                    binding.recyclerView.post {
                        adapter.removeLoadingFooter()
                        checkFooter()
                        checkIfLoadMoreNeeded()
                    }
                }
            }
        }

        // Наблюдение за ошибками
        viewModel.error.observe(viewLifecycleOwner) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastErrorTime >= errorInterval) {
                showError(it)
                lastErrorTime = currentTime
            }
            if (viewModel.error.value == "Unknown error occurred while loading more jokes.") {
                if (!adapter.getIsLoadingAdded() && !viewModel.isLoading.value!!) {
                    adapter.addLoadingFooter()
                    viewModel.setLoadingAdded(true)
                }
                // Проверяем, не запланирована ли уже повторная попытка
                if (!handler.hasCallbacks(retryRunnable) && !viewModel.isLoading.value!!) {
                    handler.postDelayed(retryRunnable, 2000) // Задержка в 2 секунды
                }
            } else {
                handler.removeCallbacks(retryRunnable)
                adapter.removeLoadingFooter()
                viewModel.setLoadingAdded(false)
            }
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

        // Наблюдение за состоянием загрузки новых шуток из api
        viewModel.isLoadingEl.observe(viewLifecycleOwner) { isLoadingEl: Boolean ->
            if (isLoadingEl) {
                adapter.addLoadingFooter()
                viewModel.setLoadingAdded(true)
            } else if (!handler.hasCallbacks(retryRunnable)) {
                adapter.removeLoadingFooter()
                viewModel.setLoadingAdded(false)
            }
        }

        // Обработка нажатия на кнопку генерации шуток
        binding.buttonGenerateJokes.setOnClickListener {
            handler.removeCallbacks(retryRunnable)
            adapter.removeLoadingFooter()
            viewModel.setLoadingAdded(false)
            if (binding.buttonGenerateJokes.text == getString(R.string.reset_used_jokes)) {
                viewModel.resetJokes()
            }
            if (!viewModel.isLoadingEl.value!!) {
                viewModel.generateJokes()
            }
        }

        // Обработка нажатия на кнопку добавления шутки
        binding.buttonAddJoke.setOnClickListener {
            val intent = Intent(requireContext(), AddJokeActivity::class.java)
            startActivity(intent)
        }

        // Обработка скролла, подгрузка начинается при достижении 4-го элемента до конца списка
        scrollListener = object : EndlessRecyclerViewScrollListener(binding.recyclerView.layoutManager!!, 0) {
            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                if (isEndOfList() && !viewModel.isLoadingEl.value!!
                    && !adapter.getIsLoadingAdded() && !handler.hasCallbacks(retryRunnable)) {
                    viewModel.loadMoreJokes()
                }
            }
        }
        binding.recyclerView.addOnScrollListener(scrollListener)
    }

    private fun createRecyclerViewList() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
    }

    private fun checkIfLoadMoreNeeded() {
        binding.recyclerView.post {
            val layoutManager = binding.recyclerView.layoutManager as? LinearLayoutManager
            if (layoutManager != null) {
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                // Проверяем, достаточно ли элементов для прокрутки
                if (totalItemCount <= lastVisibleItemPosition + 1) {
                    viewModel.loadMoreJokes()
                }
            }
        }
    }

    private fun checkFooter() {
        if (viewModel.isLoadingAdded.value!!) {
            adapter.addLoadingFooter()
        } else {
            adapter.removeLoadingFooter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(retryRunnable)
        _binding = null
    }
}
