package com.cachemanager.datatypes

import com.cachemanager.DownLoadManager
import com.cachemanager.listner.HttpListener
import com.cachemanager.parameters.HeaderParams
import com.cachemanager.parameters.RequestParams
import com.cachemanager.requestTasks.JsonObjectTask
import com.cachemanager.utilities.CacheManagerInterface
import org.json.JSONObject
import java.util.*

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

class JsonObject
/**
 * Constructor to load json datatyes
 */
(private val method: DownLoadManager.Method, private val url: String, private val params: ArrayList<RequestParams>, private val headers: ArrayList<HeaderParams>) : Type<JSONObject>() {
    private var mListener: HttpListener<JSONObject>? = null
    private var mTask: JsonObjectTask? = null
    private var mCacheManager: CacheManagerInterface<JSONObject>? = null

    /**
     * Sets future callback
     */

    override fun setCallback(listener: HttpListener<JSONObject>): JsonObject {
        this.mListener = listener
        mListener!!.onRequest()
        val data: JSONObject?
        if (mCacheManager != null) {
            data = mCacheManager!!.getDataFromCache(url)
            if (data != null) {
                mListener!!.onResponse(data)
                return this
            }
        }

        mTask = JsonObjectTask(method, url, params, headers, mListener!!)
        mTask!!.execute()
        return this
    }

    /**
     * Cancels the current request
     *
     * @return True if cancelled
     */
    override fun cancel(): Boolean {
        if (mTask != null) {
            mTask!!.cancel(true)
            if (mTask!!.isCancelled) {
                mListener!!.onCancel()
                return true
            } else {
                return false
            }
        }

        return false
    }


    override fun setCacheManager(cache: CacheManagerInterface<JSONObject>): JsonObject {
        this.mCacheManager = cache
        return this
    }


}
