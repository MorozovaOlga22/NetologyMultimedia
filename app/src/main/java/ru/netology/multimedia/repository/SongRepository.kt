package ru.netology.multimedia.repository

import ru.netology.multimedia.dto.SongsFromServerInfo

interface SongRepository {
    fun getAllAsync(callback: RepositoryCallback)

    interface RepositoryCallback {
        fun onSuccess(songsFromServerInfo: SongsFromServerInfo)
        fun onError()
    }
}