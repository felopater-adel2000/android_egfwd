package com.example.android.navigation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.FragmentTitleBinding

class TitleFragment : Fragment()
{
    lateinit var binding: FragmentTitleBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        //return inflater.inflate(R.layout.fragment_title2, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)

        setHasOptionsMenu(true)

        binding.playButton.setOnClickListener{
            //Navigation.findNavController(it).navigate(R.id.action_titleFragment2_to_gameFragment)

            //fragmentManager!!.beginTransaction().replace(R.id.titleFragment, GameFragment()).commit()

            //it.findNavController().navigate(R.id.action_titleFragment3_to_gameFragment2)
            findNavController().navigate(TitleFragmentDirections.actionTitleFragment3ToGameFragment2())
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_overflow, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, findNavController())
                || super.onOptionsItemSelected(item)
    }


}