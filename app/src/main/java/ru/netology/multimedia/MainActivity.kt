package ru.netology.multimedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.multimedia.adapter.OnInteractionListener
import ru.netology.multimedia.adapter.SongsAdapter
import ru.netology.multimedia.databinding.ActivityMainBinding
import ru.netology.multimedia.dto.Song
import ru.netology.multimedia.utils.MediaLifecycleObserver
import ru.netology.multimedia.viewmodel.SongViewModel

class MainActivity : AppCompatActivity() {
    private val mediaObserver = MediaLifecycleObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(mediaObserver)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: SongViewModel by viewModels()
        viewModel.loadPosts()

        val adapter = SongsAdapter(object : OnInteractionListener {
            override fun onPause() {
                resetMediaPlayer()
                viewModel.pause()
            }

            override fun onPlay(song: Song) {
                resetMediaPlayer()
                startSong(viewModel, song)
            }

            override fun changeRepeat() {
                viewModel.changeRepeat()
            }

            override fun changeOrder() {
                viewModel.changeOrder()
            }
        })
        binding.list.adapter = adapter
        viewModel.data.observe(this) {
            adapter.submitList(it.songs)
            with(binding) {
                if (!(viewModel.data.value?.loading
                        ?: throw RuntimeException("Can't check loading state"))
                ) {
                    list.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                }
            }
        }

        mediaObserver.player?.setOnCompletionListener {
            it.stop()
            it.reset()
            val songs = viewModel.getSongs()
            val indexOfPlayingSong = songs.indexOfFirst { song -> song.isPlaying }
            if (indexOfPlayingSong >= 0) {
                val nextSongIndex = when {
                    viewModel.isRepeating() -> indexOfPlayingSong
                    viewModel.isReverseOrder() -> if (indexOfPlayingSong == 0) songs.size - 1 else indexOfPlayingSong - 1
                    else -> if (indexOfPlayingSong < songs.size - 1) indexOfPlayingSong + 1 else 0
                }

                val nextPlayingSong = songs[nextSongIndex]
                startSong(viewModel, nextPlayingSong)
            }
        }
    }

    private fun resetMediaPlayer() {
        mediaObserver.player?.apply {
            if (isPlaying) {
                stop()
                reset()
            }
        }
    }

    private fun startSong(viewModel: SongViewModel, song: Song) {
        viewModel.play(song.id)
        mediaObserver.apply {
            player?.setDataSource(
                "https://github.com/netology-code/andad-homeworks/blob/master/09_multimedia/data/" + song.file + "?raw=true"
            )
        }.play()
    }
}