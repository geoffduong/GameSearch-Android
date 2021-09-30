package com.geoffduong.gamesearch.ui.recyclerview

import android.graphics.BitmapFactory
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.geoffduong.gamesearch.api.GiantBombService
import com.geoffduong.gamesearch.data.Game

class GamePagingSource(val service: GiantBombService, val filter: String) :
    PagingSource<Int, Game>() {
    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        try {
            val offset = params.key ?: 0
            val response = service.getGame(
                filter = filter,
                offset = offset
            )

            var nextKey = response.number_of_page_results?.plus(offset)
            if (nextKey != null && nextKey >= response.number_of_total_results!!) {
                nextKey = null
            }

            response.results!!.map { game ->
                val response = service.getImage(
                    game.image!!.icon_url!!.removePrefix(
                        "https://www.giantbomb.com/a/uploads/"
                    )
                )
                val bitmap =
                    BitmapFactory.decodeStream(
                        response.body()?.byteStream()
                    )
                game.image?.icon_bitmap = bitmap
                game
            }

            return LoadResult.Page(
                response.results!!,
                prevKey = null,
                nextKey = nextKey
            )

        } catch (e: Exception) {
            Log.e("game.paging.source", e.message.orEmpty())
        }
        throw RuntimeException("Error occurred when loading games")
    }

    override val keyReuseSupported: Boolean
        get() = true
}