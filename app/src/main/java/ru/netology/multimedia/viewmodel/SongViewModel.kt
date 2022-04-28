package ru.netology.multimedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.multimedia.dto.ModelStateData
import ru.netology.multimedia.dto.Song
import ru.netology.multimedia.dto.SongsFromServerInfo
import ru.netology.multimedia.repository.SongRepository
import ru.netology.multimedia.repository.SongRepositoryImpl
import java.lang.RuntimeException

class SongViewModel : ViewModel() {
    private val repository: SongRepository = SongRepositoryImpl()
    val data: MutableLiveData<ModelStateData> = MutableLiveData(ModelStateData(loading = true))

    private var defaultSongs = listOf(
        Song(
            id = 1,
            file = "1.mp3"
        ),
        Song(
            id = 2,
            file = "2.mp3"
        ),
        Song(
            id = 3,
            file = "3.mp3"
        )
    )

    fun loadPosts() {
        repository.getAllAsync(object : SongRepository.RepositoryCallback {
            override fun onSuccess(songsFromServerInfo: SongsFromServerInfo) {
                data.value = ModelStateData(songsFromServerInfo.tracks)
            }

            override fun onError() {
                data.value = ModelStateData(defaultSongs)
            }
        })
    }

    fun getSongs() = (data.value ?: throw RuntimeException("Can't get songs")).songs

    fun pause() {
        val songs = getSongs()
        val updatedSongs = songs.map { song ->
            song.copy(isPlaying = false)
        }
        data.value = ModelStateData(updatedSongs)
    }

    fun play(id: Int) {
        val songs = getSongs()
        val isRepeating = songs.any { song -> song.isRepeating }
        val isReverseOrder = songs.any { song -> song.isReverseOrder }

        val updatedSongs = songs.map { song ->
            song.copy(
                isPlaying = song.id == id,
                isRepeating = if (song.id == id) isRepeating else false,
                isReverseOrder = if (song.id == id) isReverseOrder else false
            )
        }
        data.value = ModelStateData(updatedSongs)
    }

    fun isRepeating() =
        getSongs().any { song -> song.isRepeating }

    fun isReverseOrder() =
        getSongs().any { song -> song.isReverseOrder }

    fun changeRepeat() {
        val songs = getSongs()
        val updatedSongs = songs.map { song ->
            song.copy(isRepeating = if (song.isPlaying) !song.isRepeating else false)
        }
        data.value = ModelStateData(updatedSongs)
    }

    fun changeOrder() {
        val songs = getSongs()
        val updatedSongs = songs.map { song ->
            song.copy(isReverseOrder = if (song.isPlaying) !song.isReverseOrder else false)
        }
        data.value = ModelStateData(updatedSongs)
    }
}
