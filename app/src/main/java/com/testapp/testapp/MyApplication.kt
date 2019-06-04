package com.testapp.testapp

import android.app.Application
import android.widget.Toast
import com.vk.sdk.VKSdk
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker


class MyApplication : Application() {

    var vkAccessTokenTracker: VKAccessTokenTracker = object : VKAccessTokenTracker() {
        override fun onVKAccessTokenChanged(oldToken: VKAccessToken?, newToken: VKAccessToken?) {
            if (newToken == null) {
                Toast.makeText(applicationContext, "Авторизуйтесь заново", Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        vkAccessTokenTracker.startTracking()
        VKSdk.initialize(applicationContext)
    }
}