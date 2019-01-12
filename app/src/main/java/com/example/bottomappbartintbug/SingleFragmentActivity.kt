package com.example.bottomappbartintbug

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

/**
 * Created by chw on 06.01.17.
 */

open class SingleFragmentActivity : AppCompatActivity(), View.OnClickListener {

    private var currentFragment: Fragment? = null
    private var toolbar: Toolbar? = null

    private val placeholderLayoutId: Int
        get() = R.id.activity_single_fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isLightThemeSelected()) {
            setTheme(R.style.Demo_Dark)
        }

        //Setting content view
        setContentView(R.layout.activity_single_fragment)

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar!!.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        setSupportActionBar(toolbar)

        if (intent.extras != null && intent.extras!!.containsKey(EXTRA_TOOLBAR_TITLE) && supportActionBar != null) {
            supportActionBar!!.title = intent.getStringExtra(EXTRA_TOOLBAR_TITLE)
        }

        if (savedInstanceState == null) {
            //Single Fragment
            val extras = intent.extras

            //Checking for additional bundle
            var internalBundle: Bundle? = null
            if (extras != null && extras.containsKey(EXTRA_BUNDLE)) {
                internalBundle = extras.getBundle(EXTRA_BUNDLE)
            }

            //Instancing and showing
            var f: Fragment? = null

            if (extras != null && extras.containsKey(EXTRA_FRAGMENT_NAME)) {
                f = Fragment.instantiate(this, extras.getString(EXTRA_FRAGMENT_NAME), internalBundle)
            } else if (f == null) {
                throw IllegalArgumentException("SingleFragmentActivity called without any fragment.")
            }

            showFragment(f, false)
        } else {
            currentFragment = supportFragmentManager.findFragmentById(placeholderLayoutId)
        }

    }

    private fun showFragment(f: Fragment?, addToBackStack: Boolean) {

        currentFragment = f

        val transaction = supportFragmentManager.beginTransaction()

        if (addToBackStack) {
            transaction.addToBackStack(TAG_BACK_STACK)
        }

        transaction.replace(placeholderLayoutId, currentFragment!!)

        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // hack for demo
        setResult(0)
    }

    protected fun shouldShowToolbar(): Boolean {
        return true
    }

    protected fun shouldApplyAnimationToRootFragment(): Boolean {
        return false
    }

    override fun onClick(view: View) {
        finish()
    }

    fun setToolbarIcon(resId: Int) {
        toolbar!!.setNavigationIcon(resId)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setToolbarElevation(elevation: Float) {
        supportActionBar!!.elevation = elevation
    }

    companion object {

        val EXTRA_BUNDLE = "SingleFragmentActivity.bundle"
        val EXTRA_TOOLBAR_TITLE = "extra_toolbar_title"
        protected val EXTRA_FRAGMENT_NAME = "SingleFragmentActivity.fragmentName"
        private val TAG_BACK_STACK = "SingleFragmentActivity.backstack"

        @JvmOverloads
        fun buildIntent(context: Context, fragmentClass: Class<out Fragment>, bundle: Bundle? = null): Intent {
            return buildIntent(context, null, fragmentClass, bundle, SingleFragmentActivity::class.java)
        }

        @JvmOverloads
        fun buildIntent(
            context: Context,
            toolbarTitle: String?,
            fragmentClass: Class<out Fragment>,
            bundle: Bundle?,
            activityClass: Class<out SingleFragmentActivity> = SingleFragmentActivity::class.java
        ): Intent {
            val intent = Intent(context, activityClass)
            if (!TextUtils.isEmpty(toolbarTitle)) {
                intent.putExtra(EXTRA_TOOLBAR_TITLE, toolbarTitle)
            }
            intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentClass.name)
            intent.putExtra(EXTRA_BUNDLE, bundle)
            return intent
        }
    }
}

fun Context.isLightThemeSelected(): Boolean {
    val lightValue = this.getString(R.string.settings_theme_light_value)

    return PreferenceManager.getDefaultSharedPreferences(this).getString(
        this.getString(R.string.pref_key_theme),
        lightValue
    ) == lightValue
}
