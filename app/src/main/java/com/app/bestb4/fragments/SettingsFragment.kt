package com.app.bestb4.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.app.bestb4.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}