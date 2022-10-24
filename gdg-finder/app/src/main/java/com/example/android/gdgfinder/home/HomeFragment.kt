package com.example.android.gdgfinder.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.gdgfinder.R
import com.example.android.gdgfinder.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    lateinit var binding: HomeFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
//        val view = inflater.inflate(R.layout.home_fragment, container, false)
//        val binding = HomeFragmentBinding.inflate(inflater)
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.navigateToSearch.observe(viewLifecycleOwner, Observer{
            Log.i("Felo", "Start Check")
            if(it == true)
            {
                Log.i("Felo", "condition is true")
                val action = HomeFragmentDirections.actionHomeFragmentToGdgListFragment()
                findNavController().navigate(action)
                viewModel.onNavigatedToSearch()
            }
        })

        return binding.root
    }
}
