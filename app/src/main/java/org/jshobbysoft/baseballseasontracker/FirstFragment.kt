/*
 * Copyright (c) 2022. JS HobbySoft
 * All rights reserved.
 */

package org.jshobbysoft.baseballseasontracker

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import org.jshobbysoft.baseballseasontracker.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        // this creates a vertical layout Manager
        binding.recyclerviewGame.layoutManager = LinearLayoutManager(activity)

        // ArrayList of class GameViewModel
        val dataGame = ArrayList<GameViewModel>()

        // get all key-value pairs from shared preferences
        val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireActivity())

        // get team name if set in Setting
        val myTeamNameFromSettings = sharedPreferences?.getString("myTeamName","My Team")

        // initialize Shared Preferences if not already created
        if (sharedPreferences?.getString("162",null) == null) {
            for (gameNumber in 1..162) {
                sharedPreferences?.edit()?.putString(gameNumber.toString(),"0")?.apply()
            }
        }

        // create data array
        for (k in 1..162) {
            val gameNumbersArray = sharedPreferences?.getString(k.toString(),"0")?.split("^")
            if (gameNumbersArray!!.size >= 7) {
                dataGame.add(
                    GameViewModel(gameNumbersArray[0],myTeamNameFromSettings,gameNumbersArray[1],
                    gameNumbersArray[2],gameNumbersArray[5],gameNumbersArray[3],
                    gameNumbersArray[4],gameNumbersArray[6],k)
                )
            }
        }

        // sort the games
        val sortInDescOrder = sharedPreferences.getBoolean("sortDescKey",false)
        if (sortInDescOrder) {
            when (val sortOptionFromSettings =
                sharedPreferences?.getString("sortOptionKey", "sortDate")) {
                "sortDate" -> dataGame.sortByDescending { it.gameInfo }
                "sortMyTeamScore" -> dataGame.sortByDescending { it.homeScore.toInt() }
                "sortOppTeamName" -> dataGame.sortByDescending { it.awayTeamName }
                "sortOppTeamScore" -> dataGame.sortByDescending { it.awayScore.toInt() }
                else -> println(sortOptionFromSettings)
            }
        } else {
            when (val sortOptionFromSettings =
                sharedPreferences?.getString("sortOptionKey", "sortDate")) {
                "sortDate" -> dataGame.sortBy { it.gameInfo }
                "sortMyTeamScore" -> dataGame.sortBy { it.homeScore.toInt() }
                "sortOppTeamName" -> dataGame.sortBy { it.awayTeamName }
                "sortOppTeamScore" -> dataGame.sortBy { it.awayScore.toInt() }
                else -> println(sortOptionFromSettings)
            }
        }

        // This will pass the ArrayList to our Adapter
        val adapterGame = GameAdapter(sharedPreferences, requireContext())
        adapterGame.data = dataGame

        // Setting the Adapter with the recyclerview
        binding.recyclerviewGame.adapter = adapterGame

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_settings -> {
                        val settingsIntent = Intent(requireContext(), MySettingsActivity::class.java)
                        startActivity(settingsIntent)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}