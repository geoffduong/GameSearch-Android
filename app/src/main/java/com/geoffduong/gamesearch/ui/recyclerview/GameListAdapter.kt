package com.geoffduong.gamesearch.ui.recyclerview

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.geoffduong.gamesearch.data.Game
import com.geoffduong.gamesearch.databinding.GameListItemBinding
import com.geoffduong.gamesearch.ui.main.GameListItemOnClickListener

class GameListAdapter(val itemOnClickListener: GameListItemOnClickListener) :
    PagingDataAdapter<Game, GameListAdapter.GameListViewHolder>(GameComparator()) {
    inner class GameListViewHolder(val binding: GameListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Game?) {
            val bitmap =
                BitmapFactory.decodeByteArray(
                    game?.image?.iconByteArray,
                    0,
                    game?.image?.iconByteArray!!.size
                )
            binding.gameIcon.setImageBitmap(bitmap)
            binding.gameName.text = game.name
            binding.root.setOnClickListener { view -> itemOnClickListener.onClick(view, game) }
        }
    }

    class GameComparator : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id?.equals(newItem.id) ?: false
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.name?.equals(newItem.name) ?: false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameListViewHolder {
        val binding =
            GameListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}