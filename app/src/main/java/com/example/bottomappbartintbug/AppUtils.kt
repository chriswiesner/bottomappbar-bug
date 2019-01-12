package com.example.bottomappbartintbug

import android.os.Build
import android.view.View

object AppUtils {
    fun clearLightStatusBar(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            view.systemUiVisibility = flags
        }
    }

}
