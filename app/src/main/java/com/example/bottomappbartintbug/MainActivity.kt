package com.example.bottomappbartintbug

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isLightThemeSelected()) {
            setTheme(R.style.Demo_Dark)
        }

        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.bar))
    }


    private fun openSettings() {
        startActivityForResult(SettingsFragment.getIntent(this), REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // usually we'd check for the result, skipping for demo....
        recreate()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        openSettings()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val REQUEST_CODE = 123
    }

}
