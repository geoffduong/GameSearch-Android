package com.geoffduong.gamesearch.api

import com.geoffduong.gamesearch.data.Game
import com.geoffduong.gamesearch.data.GiantBombResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GiantBombService {
    @GET("/api/games/")
    suspend fun getGame(
        @Query("api_key") apiKey: String = "9d45436f87d3848d2bdcce810bacb6df57dfd134",
        @Query("filter") filter: String,
        @Query("format") responseFormat: String = "json",
        @Query("offset") offset: Int = 0
    ): GiantBombResponse<Game>
}