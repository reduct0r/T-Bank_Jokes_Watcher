package com.example.homework_project_1.main.presentation.favourite_jokes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.FragmentFavoriteJokeListBinding
import com.example.homework_project_1.databinding.FragmentJokeListBinding
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.presentation.joke_details.JokeDetailsFragment
import com.example.homework_project_1.main.presentation.joke_list.JokeListViewModel
import com.example.homework_project_1.main.presentation.joke_list.JokesViewModelFactory
import com.example.homework_project_1.main.presentation.joke_list.recycler.adapter.ViewTypedListAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteJokeListFragment : Fragment() {
    @Inject
    lateinit var jokesViewModelFactory: JokesViewModelFactory

    private var _binding: FragmentFavoriteJokeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteJokesListViewModel by viewModels {
        jokesViewModelFactory
    }

    private val adapter = ViewTypedListAdapter(
        jokeClickListener = { joke ->
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragment_container, JokeDetailsFragment.newInstance(joke))
                .addToBackStack(null)
                .commit()
        },
        favoriteClickListener = { joke, bind ->
            bind.favoriteStar.isSelected = !bind.favoriteStar.isSelected
            viewModel.viewModelScope.launch {
                joke.isFavorite = !joke.isFavorite
                viewModel.toggleFavorite(joke)
            }
        }
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteJokeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRecyclerViewList()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        viewModel.favoriteJokes.observe(viewLifecycleOwner) { jokes ->
            adapter.submitList(jokes)
        }
    }

    private fun createRecyclerViewList() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
    }

    override fun onResume() {
        super.onResume()
        viewModel.viewModelScope.launch {
            viewModel.loadFavoriteJokes()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
