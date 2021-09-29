package com.geoffduong.gamesearch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.geoffduong.gamesearch.api.GiantBombService
import com.geoffduong.gamesearch.data.Game
import com.geoffduong.gamesearch.databinding.ListFragmentBinding
import com.geoffduong.gamesearch.ui.recyclerview.GameListAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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

        val service = Retrofit.Builder().baseUrl("https://www.giantbomb.com")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                )
            )
            .build().create(GiantBombService::class.java)

        val viewModel by viewModels<GameViewModel>(factoryProducer = {
            GameViewModelProviderFactory(
                service
            )
        })

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }

    class GameViewModelProviderFactory(val service: GiantBombService) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass == GameViewModel::class.java) {
                return GameViewModel(service) as T
            }
            return null as T
        }
    }
}