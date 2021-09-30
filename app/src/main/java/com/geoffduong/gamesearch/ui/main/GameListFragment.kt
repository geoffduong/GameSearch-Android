package com.geoffduong.gamesearch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.geoffduong.gamesearch.data.Game
import com.geoffduong.gamesearch.databinding.ListFragmentBinding
import com.geoffduong.gamesearch.ui.recyclerview.GameListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameListFragment : Fragment() {

    private lateinit var binding: ListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by viewModels<GameViewModel>()

        val pagingAdapter = GameListAdapter(object : GameListItemOnClickListener {
            override fun onClick(view: View, game: Game?) {
                val action = GameListFragmentDirections.actionListFragmentToDetailFragment(
                    gameIcon = game?.image?.icon_url,
                    gameDescription = game?.description
                )
                view.findNavController().navigate(action)
            }
        })
        val recyclerView = binding.listFragmentRecyclerView
        recyclerView.adapter = pagingAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }
}