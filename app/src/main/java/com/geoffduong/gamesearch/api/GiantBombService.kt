package com.geoffduong.gamesearch.api

import com.geoffduong.gamesearch.data.Game
import com.geoffduong.gamesearch.data.GiantBombResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface GiantBombService {
    @GET("/api/games/")
    suspend fun getGame(
        @Query("api_key") apiKey: String = "9d45436f87d3848d2bdcce810bacb6df57dfd134",
        @Query("filter") filter: String,
        @Query("format") responseFormat: String = "json",
        @Query("offset") offset: Int = 0
    ): GiantBombResponse<Game>

    @Streaming
    @GET("a/uploads/{imageUri}")
    suspend fun getImage(@Path("imageUri", encoded = true) imageUri: String): Response<ResponseBody>
}