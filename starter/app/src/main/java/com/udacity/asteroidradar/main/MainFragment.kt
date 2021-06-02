package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var asteroidAdapter: AsteroidListAdapter
    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setupAsteroidAdapter()

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer { list ->
            asteroidAdapter.submitList(list)
            Log.v("MainFragment", "New List Submitted $list")
        })
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun setupAsteroidAdapter() {
        asteroidAdapter = AsteroidListAdapter(AsteroidClickListener { asteroid ->
            // Pass the asteroid into the DetailFragment
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
            Toast.makeText(context, "You Clicked on ${asteroid.codename}", Toast.LENGTH_SHORT).show()
        })

        binding.asteroidRecycler.adapter = asteroidAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.asteroidRecycler.layoutManager = layoutManager
    }
}
