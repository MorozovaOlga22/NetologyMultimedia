package ru.netology.multimedia.dto

data class SongsFromServerInfo(
    val id: Int,
    val title: String,
    val subtitle: String,
    val artist: String,
    val published: String,
    val genre: String,
    val tracks: List<Song>
)