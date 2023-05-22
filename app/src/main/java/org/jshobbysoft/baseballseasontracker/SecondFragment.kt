/*
 * Copyright (c) 2022. JS HobbySoft
 * All rights reserved.
 */

package org.jshobbysoft.baseballseasontracker

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.jshobbysoft.baseballseasontracker.databinding.FragmentSecondBigHitsBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBigHitsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var gameString = "0"
    private var homeScore = 0
    private var homeHits = 0
    private var awayScore = 0
    private var awayHits = 0
    private var inning = 0
    private var outs = 0
    private var strikes = 0
    private var balls = 0
    private val innings = listOf("1 top","1 bottom","2 top","2 bottom","3 top","3 bottom","4 top","4 bottom",
        "5 top","5 bottom","6 top","6 bottom","7 top","7 bottom","8 top","8 bottom",
        "9 top","9 bottom","10 top","10 bottom","11 top","11 bottom","12 top","12 bottom",
        "13 top","13 bottom","14 top","14 bottom","15 top","15 bottom")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        _binding = FragmentSecondBigHitsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireActivity())
        binding.tVHomeScore.text = sharedPref?.getString("myTeamName",getString(R.string.textview_home_score_label))

        binding.buttonPlusHomeScore.setOnClickListener {
            homeScore++
            binding.tVHomeScoreValue.text = String.format((homeScore).toString())
        }

        binding.buttonMinusHomeScore.setOnClickListener {
            homeScore--
            if (homeScore < 0) { homeScore = 0 }
            binding.tVHomeScoreValue.text = String.format((homeScore).toString())
        }

        binding.buttonPlusAwayScore.setOnClickListener {
            awayScore++
            binding.tVAwayScoreValue.text = String.format((awayScore).toString())
        }

        binding.buttonMinusAwayScore.setOnClickListener {
            awayScore--
            if (awayScore < 0) { awayScore = 0 }
            binding.tVAwayScoreValue.text = String.format((awayScore).toString())
        }

        binding.buttonPlusInning.setOnClickListener {
            inning++
            if (inning > innings.count() - 1) { inning = innings.count()-1 }
            binding.tVInningValue.text = String.format((innings[inning]))
        }

        binding.buttonMinusInning.setOnClickListener {
            inning--
            if (inning < 0) { inning = 0 }
            binding.tVInningValue.text = String.format((innings[inning]))
        }

        binding.buttonPlusOut.setOnClickListener {
            outs++
            balls = 0
            binding.tVBallsValue.text = String.format((balls).toString())
            strikes = 0
            binding.tVStrikeValue.text = String.format((strikes).toString())
            if (outs == 3) {
                outs = 0
                inning++
                binding.tVInningValue.text = String.format((innings[inning]))
            }
            binding.tVOutValue.text = String.format(outs.toString())
        }

        binding.buttonMinusOut.setOnClickListener {
            outs--
            if (outs < 0) { outs = 0 }
            binding.tVOutValue.text = String.format(outs.toString())
        }

        binding.buttonPlusStrike.setOnClickListener {
            strikes++
            if (strikes == 3) {
                strikes = 0
                balls = 0
                binding.tVBallsValue.text = String.format((balls).toString())
                outs++
                if (outs == 3) {
                    outs = 0
                    balls = 0
                    binding.tVBallsValue.text = String.format((balls).toString())
                    inning++
                    binding.tVInningValue.text = String.format((innings[inning]))
                }
                binding.tVOutValue.text = String.format(outs.toString())
                binding.tVStrikeValue.text = String.format((strikes).toString())
            }
            binding.tVStrikeValue.text = String.format((strikes).toString())
        }

        binding.buttonMinusStrike.setOnClickListener {
            strikes--
            if (strikes < 0) { strikes = 0 }
            binding.tVStrikeValue.text = String.format((strikes).toString())
        }

        binding.buttonPlusBalls.setOnClickListener {
            balls++
            if (balls == 10) { balls = 0 }
            binding.tVBallsValue.text = String.format((balls).toString())
        }

        binding.buttonMinusBalls.setOnClickListener {
            balls--
            if (balls < 0) { balls = 0 }
            binding.tVBallsValue.text = String.format((balls).toString())
        }

        binding.buttonPlusHomeHits.setOnClickListener {
            homeHits++
            binding.tVHomeHitsValue.text = String.format((homeHits).toString())
            balls = 0
            binding.tVBallsValue.text = String.format((balls).toString())
            strikes = 0
            binding.tVStrikeValue.text = String.format((strikes).toString())
        }

        binding.buttonMinusHomeHits.setOnClickListener {
            homeHits--
            if (homeHits < 0) { homeHits = 0 }
            binding.tVHomeHitsValue.text = String.format((homeHits).toString())
        }

        binding.buttonPlusAwayHits.setOnClickListener {
            awayHits++
            binding.tVAwayHitsValue.text = String.format((awayHits).toString())
            balls = 0
            binding.tVBallsValue.text = String.format((balls).toString())
            strikes = 0
            binding.tVStrikeValue.text = String.format((strikes).toString())
        }

        binding.buttonMinusAwayHits.setOnClickListener {
            awayHits--
            if (awayHits < 0) { awayHits = 0 }
            binding.tVAwayHitsValue.text = String.format((awayHits).toString())
        }

        val editText = binding.overallLabel
        editText.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(childFragmentManager, "datePicker")
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
                menuInflater.inflate(R.menu.menu_main_second_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_settings -> {
                        // User chose the "Settings" item, show the app settings UI...
                        true
                    }

                    R.id.action_done -> {
                        //        val uniqueID = UUID.randomUUID().toString()
                        if (binding.overallLabel.text.toString().isNotEmpty()) {
                            gameString = (binding.overallLabel.text.toString() + "^" +
                                    String.format((homeScore).toString()) + "^" + String.format((awayScore).toString()) + "^" +
                                    String.format((homeHits).toString()) + "^" + String.format((awayHits).toString())+ "^" +
                                    binding.eTAwayName.text.toString() + "^" + binding.eTNotes.text.toString())

                            val sharedPrefGame = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireActivity())

                            var gameNumSP = "0"
                            for (j in 1..162) {
                                if (sharedPrefGame?.getString(j.toString(), null) == "0") {
                                    gameNumSP = j.toString()
                                    break
                                }
                            }
                            with(sharedPrefGame!!.edit()) {
                                putString(gameNumSP, gameString)
                                commit()
                            }
                        }
                        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        childFragmentManager.setFragmentResultListener("requestKey",this) { _, bundle ->
            val result = bundle.getString("bundleKey")
            // Do something with the result
//            println("Result: " + result.toString())
            binding.overallLabel.setText(String.format((result).toString()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}