/*
 * Copyright (c) 2022. JS HobbySoft
 * All rights reserved.
 */

package org.jshobbysoft.baseballseasontracker

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


class GameAdapter (private val prefs: SharedPreferences, private val gaContext: Context): RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    var data =  arrayListOf<GameViewModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // return the number of the items in the list
    override fun getItemCount() = data.size

    // delete an item
    private fun removeItem (position: Int) {
//        println("in delete function")
        data.removeAt(position)
        notifyDataSetChanged()
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design_game, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val gameViewModel = data[position]

        holder.labelView.text = gameViewModel.gameInfo
        holder.myTeamNameView.text = gameViewModel.teamName + ":"
        holder.homeScoreView.text = gameViewModel.homeScore
        holder.awayTeamNameView.text = gameViewModel.awayTeamName + ":"
        holder.awayScoreView.text = gameViewModel.awayScore

        holder.itemView.setOnClickListener {v ->
//            println("clicked ${gameViewModel.gameNumSharedPref} position $position")
            val bundle = bundleOf("prefNum" to gameViewModel.gameNumSharedPref.toString())
            v.findNavController().navigate(R.id.action_FirstFragment_to_ThirdFragment, bundle)
        }

        holder.itemView.setOnLongClickListener {
//            println("clicked ${gameViewModel.gameNumSharedPref} position $position")
            val builder = AlertDialog.Builder(gaContext)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    // Delete selected note from database
                    removeItem(position)
                    prefs.edit().remove(gameViewModel.gameNumSharedPref.toString()).commit()
                }
                .setNegativeButton("No") { dialog, _ ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            true
        }
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val labelView: TextView = itemView.findViewById(R.id.textViewLabel)
        val myTeamNameView: TextView = itemView.findViewById(R.id.myTeamNameLabel)
        val homeScoreView: TextView = itemView.findViewById(R.id.textViewHomeScore)
        val awayScoreView: TextView = itemView.findViewById(R.id.textViewAwayScore)
        val awayTeamNameView: TextView = itemView.findViewById(R.id.awayTeamNameView)
    }
}

class GameListener(val clickListener: (gameId: Int) -> Unit){
    fun onClick(game: GameViewModel) = clickListener(game.gameNumSharedPref)
}
