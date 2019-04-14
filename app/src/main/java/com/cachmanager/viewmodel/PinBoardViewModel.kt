package com.cachmanager.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.cachemanager.DownLoadManager
import com.cachemanager.listner.HttpListener
import com.cachemanager.utilities.CacheManager
import com.cachmanager.model.User
import com.cachmanager.util.FOURTY_MB
import org.json.JSONArray
import org.json.JSONException


class PinBoardViewModel : ViewModel() {

    var pinBoardList = MutableLiveData<ArrayList<User>>()
    var isSwipeRefresh = MutableLiveData<Boolean>()

    private var jsonArrayCacheManager: CacheManager<JSONArray>? = null

    fun getPinboardList(context: Context): MutableLiveData<ArrayList<User>> {

        jsonArrayCacheManager = CacheManager(FOURTY_MB) // 40mb
        pinBoardList.value = ArrayList<User>()
        loadPinBoard(context)

        return pinBoardList
    }


    private fun loadPinBoard(context: Context) {

        // Do an asynchronous get data
        DownLoadManager.from(context)
            .load(DownLoadManager.Method.GET, com.cachemanager.BuildConfig.BASE_URL)
            .asJsonArray()
            .setCacheManager(jsonArrayCacheManager!!)
            .setCallback(object : HttpListener<JSONArray> {
                override fun onRequest() {
                    isSwipeRefresh.postValue(true)
                }

                override fun onResponse(data: JSONArray) {

                    var lengthofdata = data.length()

                    for (i in 0..lengthofdata) {
                        try {
                            //Toast.makeText(MainActivity.this,"here",Toast.LENGTH_SHORT).show();
                            val jUser = data.getJSONObject(i)
                            val ObjectforNames = jUser.getJSONObject("user")
                            val CategoryArray = jUser.getJSONArray("categories")
                            val ObjectforUrls = jUser.getJSONObject("urls")
                            val ObjectforProfilePic = ObjectforNames.getJSONObject("profile_image")
                            val nameofuser = ObjectforNames.get("name").toString()
                            val username = "@" + ObjectforNames.get("username").toString()
                            val categories = ArrayList<String>()
                            val lengthofcategories = CategoryArray.length()
                            for (j in 0..lengthofcategories) {
                                try {
                                    val Category = CategoryArray.getJSONObject(j)
                                    val CategoryName = Category.getString("title")
                                    categories.add(CategoryName)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                            }
                            val uploadedphotourl = ObjectforUrls.getString("regular")
                            val urltosend = ObjectforUrls.getString("full")
                            val profilepicurl = ObjectforProfilePic.getString("large")
                            val islikedbyuser = jUser.getBoolean("liked_by_user")
                            val numberOfLikes = jUser.getInt("likes")
                            val newUser = User(
                                nameofuser,
                                profilepicurl,
                                uploadedphotourl,
                                islikedbyuser,
                                username,
                                numberOfLikes,
                                categories,
                                urltosend
                            )

                            pinBoardList.value!!.add(newUser)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                    isSwipeRefresh.postValue(false)
                    pinBoardList.postValue(pinBoardList.value)
                }

                override fun onError() {
                    isSwipeRefresh.postValue(false)
                }

                override fun onCancel() {
                    isSwipeRefresh.postValue(false)
                }
            })
    }

}