package ru.netology.multimedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.multimedia.R
import ru.netology.multimedia.databinding.SongLineBinding
import ru.netology.multimedia.dto.Song

interface OnInteractionListener {
    fun onPause()
    fun onPlay(song: Song)
    fun changeRepeat()
    fun changeOrder()
}

class SongsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Song, SongViewHolder>(SongDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SongLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(
        holder: SongViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val song = getItem(position)
            payloads.forEach {
                if (it is Payload) {
                    holder.bind(it, song)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = getItem(position)
        holder.bind(song)
    }
}


class SongViewHolder(
    private val binding: SongLineBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(payload: Payload, song: Song) {
        with(binding) {
            payload.isPlaying?.also { isPlaying ->
                playPauseButton.setImageResource(
                    if (isPlaying) R.drawable.ic_baseline_pause_circle_filled_64 else R.drawable.ic_baseline_play_circle_filled_64
                )
                playPauseButton.setOnClickListener {
                    if (isPlaying) {
                        onInteractionListener.onPause()
                    } else {
                        onInteractionListener.onPlay(song)
                    }
                }
                additionalButtonsGroup.visibility = if (isPlaying) View.VISIBLE else View.GONE
            }
            payload.isRepeating?.also { isRepeating ->
                repeatButton.setImageResource(
                    if (isRepeating) R.drawable.ic_baseline_replay_48_on else R.drawable.ic_baseline_replay_48_off
                )
            }
            payload.isReverseOrder?.also { isReverseOrder ->
                reverseButton.setImageResource(
                    if (isReverseOrder) R.drawable.ic_baseline_arrow_upward_48_on else R.drawable.ic_baseline_arrow_upward_48_off
                )
            }
        }
    }

    fun bind(song: Song) {
        binding.apply {
            songName.text = song.file

            playPauseButton.setImageResource(
                if (song.isPlaying) R.drawable.ic_baseline_pause_circle_filled_64 else R.drawable.ic_baseline_play_circle_filled_64
            )
            playPauseButton.setOnClickListener {
                if (song.isPlaying) {
                    onInteractionListener.onPause()
                } else {
                    onInteractionListener.onPlay(song)
                }
            }

            repeatButton.setImageResource(
                if (song.isRepeating) R.drawable.ic_baseline_replay_48_on else R.drawable.ic_baseline_replay_48_off
            )
            repeatButton.setOnClickListener {
                onInteractionListener.changeRepeat()
            }

            reverseButton.setImageResource(
                if (song.isReverseOrder) R.drawable.ic_baseline_arrow_upward_48_on else R.drawable.ic_baseline_arrow_upward_48_off
            )
            reverseButton.setOnClickListener {
                onInteractionListener.changeOrder()
            }

            additionalButtonsGroup.visibility = if (song.isPlaying) View.VISIBLE else View.GONE
        }
    }
}

class SongDiffCallback : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Song, newItem: Song): Any =
        Payload(
            isPlaying = newItem.isPlaying.takeIf { oldItem.isPlaying != it },
            isRepeating = newItem.isRepeating.takeIf { oldItem.isRepeating != it },
            isReverseOrder = newItem.isReverseOrder.takeIf { oldItem.isReverseOrder != it }
        )
}

data class Payload(
    val isPlaying: Boolean? = null,
    val isRepeating: Boolean? = null,
    val isReverseOrder: Boolean? = null
)