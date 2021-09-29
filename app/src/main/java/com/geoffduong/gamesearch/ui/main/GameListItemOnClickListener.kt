package com.geoffduong.gamesearch.ui.main

import android.view.View
import com.geoffduong.gamesearch.data.Game

interface GameListItemOnClickListener {
    fun onClick(view: View, game: Game?)
}