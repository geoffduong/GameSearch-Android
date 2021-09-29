package com.geoffduong.gamesearch.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.geoffduong.gamesearch.api.GiantBombService
import com.geoffduong.gamesearch.data.Game
import com.geoffduong.gamesearch.ui.recyclerview.GamePagingSource
import kotlinx.coroutines.flow.Flow

class GameViewModel(val service: GiantBombService) : ViewModel() {

    val flow =
        Pager(PagingConfig(20)) {
            GamePagingSource(service, "name:P")
        }.flow.cachedIn(viewModelScope)

}