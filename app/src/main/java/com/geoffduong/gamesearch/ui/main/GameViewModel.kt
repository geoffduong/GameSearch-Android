package com.geoffduong.gamesearch.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.geoffduong.gamesearch.api.GiantBombService
import com.geoffduong.gamesearch.ui.recyclerview.GamePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel() : ViewModel() {

    private lateinit var service: GiantBombService

    @Inject
    constructor(service: GiantBombService) : this() {
        this.service = service
    }

    val flow =
        Pager(PagingConfig(20)) {
            GamePagingSource(service, "name:Planetfall")
        }.flow.cachedIn(viewModelScope)
}