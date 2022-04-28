package ru.netology.multimedia.repository

import ru.netology.multimedia.api.SongsApi
import ru.netology.multimedia.dto.SongsFromServerInfo

class SongRepositoryImpl : SongRepository {
    override fun getAllAsync(callback: SongRepository.RepositoryCallback) {
        SongsApi.retrofitService.getSongs()
            .enqueue(object : retrofit2.Callback<SongsFromServerInfo> {
                override fun onResponse(
                    call: retrofit2.Call<SongsFromServerInfo>,
                    response: retrofit2.Response<SongsFromServerInfo>
                ) {
                    if (!response.isSuccessful) {
                        callback.onError()
                        return
                    }

                    callback.onSuccess(
                        response.body() ?: throw java.lang.RuntimeException("body is null")
                    )
                }

                override fun onFailure(call: retrofit2.Call<SongsFromServerInfo>, t: Throwable) {
                    callback.onError()
                }
            })
    }
}