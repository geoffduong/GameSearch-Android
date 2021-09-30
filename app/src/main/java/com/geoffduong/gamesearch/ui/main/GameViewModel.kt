package com.geoffduong.gamesearch.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.geoffduong.gamesearch.api.GiantBombService
import com.geoffduong.gamesearch.data.Game
import com.geoffduong.gamesearch.ui.recyclerview.GamePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
class GameViewModel() : ViewModel() {

    private lateinit var service: GiantBombService
    private val _query: MutableLiveData<String> = MutableLiveData()
    val query: LiveData<String> get() = _query
    var flow: Flow<PagingData<Game>> = emptyFlow()


    @Inject
    constructor(service: GiantBombService) : this() {
        this.service = service
    }

    fun updateQuery(query: String?) {
        query?.let {
            val trimmedQuery = query.trim()
            if (trimmedQuery.isEmpty()) {
                flow = emptyFlow()
            } else if (!trimmedQuery.equals(_query.value)) {
                flow = Pager(PagingConfig(20)) {
                    GamePagingSource(service, "name:" + trimmedQuery)
                }.flow.cachedIn(viewModelScope)
            }
            this._query.postValue(query)
        }
    }
}