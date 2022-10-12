/*
 * Copyright (c) 2022. JS HobbySoft
 * All rights reserved.
 */

package org.jshobbysoft.baseballseasontracker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class MySettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, MySettingsFragment())
            .commit()
    }
}

class MySettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val pref1 : EditTextPreference? = findPreference("myTeamName")
        pref1?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                val newTeamNameString = newValue?.toString()
                if (newTeamNameString!!.length <= 8) {
                    true
                } else {
                    Toast.makeText(requireActivity(),R.string.toastBadName, Toast.LENGTH_LONG).show()
                    false
                }
            }
    }
}
