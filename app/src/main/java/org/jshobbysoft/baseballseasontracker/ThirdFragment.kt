/*
 * Copyright (c) 2022. JS HobbySoft
 * All rights reserved.
 */

package org.jshobbysoft.baseballseasontracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jshobbysoft.baseballseasontracker.databinding.FragmentThirdDetailBinding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "prefNum"

class ThirdFragment : Fragment() {
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    private var _binding: FragmentThirdDetailBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentThirdDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        println("shared pref index: $param1")

        val sharedPref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val gameNumbersArray = sharedPref?.getString(param1,"0")?.split("^")
        if (gameNumbersArray!!.size >= 7) {
            binding.overallLabel.text = gameNumbersArray[0]
            binding.tVHomeScoreValue.text = gameNumbersArray[1]
            binding.tVAwayScoreValue.text = gameNumbersArray[2]
            binding.tVHomeHitsValue.text = gameNumbersArray[3]
            binding.tVAwayHitsValue.text = gameNumbersArray[4]
            binding.tVAwayName.text = gameNumbersArray[5]
            binding.tVNotes.text = gameNumbersArray[6]
            binding.tVHomeName.text = sharedPref.getString("myTeamName","My Team")
        }
    }
}