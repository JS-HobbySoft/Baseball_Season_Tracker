/*
 * Copyright (c) 2022. JS HobbySoft
 * All rights reserved.
 */

package org.jshobbysoft.baseballseasontracker

data class GameViewModel(val gameInfo: String,
                         val teamName: String?,
                         val homeScore: String,
                         val awayScore: String,
                         val awayTeamName: String?,
                         val homeHits: String,
                         val awayHits: String,
                         val gameNotes: String,
                         val gameNumSharedPref: Int)
