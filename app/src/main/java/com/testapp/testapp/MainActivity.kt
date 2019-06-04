package com.testapp.testapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var vkLogo: ImageView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vkLogo = findViewById(R.id.logo)
        vkLogo.setOnClickListener(this)
        sharedPref = getSharedPreferences("token", Context.MODE_PRIVATE)
        try {
            if (sharedPref.getString("token", VKAccessToken.currentToken().accessToken) != null) {
                val intent = Intent(applicationContext, FriendsActivity::class.java)
                startActivity(intent)
            }
        } catch (e: NullPointerException) {
            Log.d("First Launch", "Пользователь еще не авторизован")}
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.logo -> VKSdk.login(this, VKScope.FRIENDS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken) {
                        editor = sharedPref.edit()
                        editor.putString("token", VKAccessToken.currentToken().accessToken).apply()
                        val intent = Intent(applicationContext, FriendsActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onError(error: VKError) {
                        Toast.makeText(applicationContext, "Провал", Toast.LENGTH_SHORT).show()
                    }
                })
        ) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
