package com.geoffduong.gamesearch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.geoffduong.gamesearch.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: GameViewModel
    var queryTextChangedJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                performSearch(query)
                return true
            }
        })
    }

    fun performSearch(query: String?) {
        queryTextChangedJob?.cancel()

        queryTextChangedJob = lifecycle.coroutineScope.launch {
            delay(400)
            viewModel.updateQuery(query)
        }
    }
}