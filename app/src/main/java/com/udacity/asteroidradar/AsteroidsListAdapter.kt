package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemBinding

class AsteroidsListAdapter(val callback: AsteroidClick):
        RecyclerView.Adapter<AsteroidsListAdapter.AsteroidsListViewHolder>() {

    var asteroids: List<Asteroid> = emptyList()
        set(value) {
            field = value

            notifyDataSetChanged()
        }

    class AsteroidsListViewHolder(val binding: ListItemBinding):
            RecyclerView.ViewHolder(binding.root) {
                companion object {
                    @LayoutRes
                    val LAYOUT = R.layout.list_item
                }
//        fun bind(listener: AsteroidClickListener, asteroid: Asteroid) {
//            binding.asteroid = asteroid
//            binding.clickListener = listener
//
//            binding.executePendingBindings()
//        }
//
//        companion object{
//            fun from(parent: ViewGroup): AsteroidsListViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ListItemBinding.inflate(layoutInflater, parent, false)
//                return AsteroidsListViewHolder(binding)
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsListViewHolder {
        //return AsteroidsListViewHolder.from(parent)
        val withDataBinding: ListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                AsteroidsListViewHolder.LAYOUT,
                parent,
                false)

        return AsteroidsListViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AsteroidsListViewHolder, position: Int) {
        holder.binding.also {
            it.asteroid = asteroids[position]
            it.asteroidCallback = callback
        }
    }

    override fun getItemCount() = asteroids.size
}

class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}

class AsteroidClick(val block: (Asteroid) -> Unit) {
    /**
     * Called when a video is clicked
     *
     * @param video the video that was clicked
     */
    fun onClick(asteroid: Asteroid) = block(asteroid)
}