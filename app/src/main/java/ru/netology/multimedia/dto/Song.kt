package ru.netology.multimedia.dto

data class Song(
    val id: Int,
    val file: String,
    val isPlaying: Boolean = false,
    val isRepeating: Boolean = false,
    val isReverseOrder: Boolean = false
)