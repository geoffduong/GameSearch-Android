package com.geoffduong.gamesearch.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.geoffduong.gamesearch.data.Game
import com.geoffduong.gamesearch.databinding.ListFragmentBinding
import com.geoffduong.gamesearch.ui.recyclerview.GameListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class GameListFragment : Fragment() {

    private lateinit var binding: ListFragmentBinding
    private val iconFileName = "icon_%s"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(requireActivity()).get(GameViewModel::class.java)

        val pagingAdapter = GameListAdapter(object : GameListItemOnClickListener {
            override fun onClick(view: View, game: Game?) {
                val file = File(view.context.filesDir, String.format(iconFileName, game?.id))
                if (!file.exists()) {
                    view.context.openFileOutput(
                        String.format(iconFileName, game?.id),
                        Context.MODE_PRIVATE
                    ).use {
                        it.write(game?.image?.iconByteArray)
                    }
                }

                val action = GameListFragmentDirections.actionListFragmentToDetailFragment(
                    gameIcon = String.format(iconFileName, game?.id),
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

        viewModel.query.observe(viewLifecycleOwner, {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.flow.onEmpty { pagingAdapter.submitData(PagingData.empty()) }
                    .collectLatest { pagingData ->
                        pagingAdapter.submitData(pagingData)
                    }
            }
        })
    }
}