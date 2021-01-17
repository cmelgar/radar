package com.udacity.asteroidradar.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModelAdapter = AsteroidsListAdapter(AsteroidClick {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })

        setHasOptionsMenu(true)

        binding.root.findViewById<RecyclerView>(R.id.asteroid_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        viewModel.apod.observe(viewLifecycleOwner, Observer {
            binding.activityMainImageOfTheDay.contentDescription = it.mediaType
            Picasso.get()
                .load(it.url).into(binding.activityMainImageOfTheDay)

        })

        return binding.root
    }

    private var viewModelAdapter: AsteroidsListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asteroids.observe(viewLifecycleOwner, Observer<List<Asteroid>> { asteroids ->
            asteroids?.apply {
                viewModelAdapter?.asteroids = asteroids
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
        return true
    }
}
