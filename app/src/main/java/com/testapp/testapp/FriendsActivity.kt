package com.testapp.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import com.vk.sdk.VKAccessToken
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKList
import org.json.JSONException
import android.widget.ArrayAdapter

class FriendsActivity : AppCompatActivity() {

    private lateinit var userName: TextView
    private lateinit var friendsList: ListView
    private lateinit var requestUserName: VKRequest
    private lateinit var requestFriendName: VKRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        userName = findViewById(R.id.userName)
        friendsList= findViewById(R.id.friendsList)

        val token = VKAccessToken.currentToken().accessToken
        val userParams = VKParameters.from(VKApiConst.ACCESS_TOKEN, token)
        requestUserName = VKRequest("account.getProfileInfo", userParams)
        requestUserName.executeWithListener(object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                try {
                    val jsonObject = response!!.json.getJSONObject("response")
                    val parseUserName = jsonObject.getString("first_name") + " " + jsonObject.getString("last_name")
                    userName.text = parseUserName
                } catch (e: JSONException) {}
            }

            override fun onError(error: VKError?) {
                super.onError(error)
            }
        })

        requestFriendName = VKApi.friends().get(VKParameters.from(VKApiConst.COUNT, "5", VKApiConst.FIELDS, "first_name, last_name"))
        requestFriendName.executeSyncWithListener(object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse) {
                super.onComplete(response)
                try {
                    val list = response.parsedModel as VKList<*>
                    Log.d("Debug", "message")
                    val arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, list)

                    friendsList.adapter = arrayAdapter

                } catch (e: JSONException) {}
            }

            override fun onError(error: VKError?) {
                super.onError(error)
            }
        })
    }
}
