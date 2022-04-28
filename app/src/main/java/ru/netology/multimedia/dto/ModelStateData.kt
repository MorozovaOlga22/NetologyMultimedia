package ru.netology.multimedia.dto

data class ModelStateData(
    val songs: List<Song> = emptyList(),
    val loading: Boolean = false
)