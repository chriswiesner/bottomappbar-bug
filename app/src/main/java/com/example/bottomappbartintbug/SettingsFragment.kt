package com.example.bottomappbartintbug

/**
 * Created by chw on 06.01.17.
 */

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private var themePrefKey: String? = null
    private var rootContainer: ViewGroup? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themePrefKey = getString(R.string.pref_key_theme)

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        setSummaries()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootContainer = container
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun setDivider(divider: Drawable) {
        super.setDivider(ColorDrawable(Color.TRANSPARENT))
    }

    override fun setDividerHeight(height: Int) {
        super.setDividerHeight(0)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val pref = findPreference<Preference>(key)


        if (key == getString(R.string.pref_key_theme)) {
            onThemeSettingChanged()
        }
    }

    private fun setSummaries() {

        setThemeSummary(context!!.isLightThemeSelected())

    }

    private fun onThemeSettingChanged() {
        val isLightThemeSet = context!!.isLightThemeSelected()

        if (!isLightThemeSet) {
            AppUtils.clearLightStatusBar(activity!!.window.decorView)
        }

        activity!!.recreate()
    }

    private fun setThemeSummary(isLightTheme: Boolean) {
        if (isLightTheme) {
            findPreference<Preference>(themePrefKey).setSummary(R.string.settings_theme_light_label)
        } else {
            findPreference<Preference>(themePrefKey).setSummary(R.string.settings_theme_dark_label)
        }
    }

    companion object {

        private const val REQUEST_CODE_ALERT_RINGTONE = 22

        fun getIntent(context: Context) = SingleFragmentActivity.buildIntent(
            context,
            context.getString(R.string.settings),
            SettingsFragment::class.java,
            null
        )
    }
}