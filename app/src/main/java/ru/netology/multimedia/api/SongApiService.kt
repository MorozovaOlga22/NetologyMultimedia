package ru.netology.multimedia.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import ru.netology.multimedia.dto.SongsFromServerInfo

private const val SONGS_URL =
    "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(SONGS_URL)
    .build()

interface SongApiService {
    @GET("album.json")
    fun getSongs(): Call<SongsFromServerInfo>
}

object SongsApi {
    val retrofitService: SongApiService by lazy {
        retrofit.create(SongApiService::class.java)
    }
}