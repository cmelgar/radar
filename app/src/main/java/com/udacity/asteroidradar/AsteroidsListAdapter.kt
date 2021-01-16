package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemBinding

class AsteroidsListAdapter(private val clickListener: AsteroidClickListener):
        ListAdapter<Asteroid, AsteroidsListAdapter.AsteroidsListViewHolder>(DiffCallback) {

    var asteroids: List<Asteroid> = emptyList()
        set(value) {
            field = value

            notifyDataSetChanged()
        }

    companion object DiffCallback: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    class AsteroidsListViewHolder(private var binding: ListItemBinding):
            RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: AsteroidClickListener, asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.clickListener = listener

            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): AsteroidsListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBinding.inflate(layoutInflater, parent, false)
                return AsteroidsListViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsListViewHolder {
        return AsteroidsListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidsListViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }
}

class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}