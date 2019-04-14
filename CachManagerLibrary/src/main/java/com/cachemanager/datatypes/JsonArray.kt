package com.cachemanager.datatypes

import com.cachemanager.DownLoadManager
import com.cachemanager.listner.HttpListener
import com.cachemanager.parameters.HeaderParams
import com.cachemanager.parameters.RequestParams
import com.cachemanager.requestTasks.JsonArrayTask
import com.cachemanager.utilities.CacheManagerInterface
import org.json.JSONArray
import java.util.*

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

class JsonArray
/**
 * Constructor to load json datatyes
 *
 * @param url
 * @param params
 * @param headers
 */
(private val method: DownLoadManager.Method, private val url: String, private val params: ArrayList<RequestParams>, private val headers: ArrayList<HeaderParams>) : Type<JSONArray>() {
    private var mListener: HttpListener<JSONArray>? = null
    private var mTask: JsonArrayTask? = null
    private var mCacheManager: CacheManagerInterface<JSONArray>? = null

    /**
     * Sets future callback after Http response is received
     *
     * @param listener
     */
    override fun setCallback(listener: HttpListener<JSONArray>): JsonArray {
        this.mListener = listener
        this.mListener!!.onRequest()
        val data: JSONArray?
        if (mCacheManager != null) {
            data = mCacheManager!!.getDataFromCache(url)
            if (data != null) {
                mListener!!.onResponse(data)
                return this
            }
        }

        mTask = JsonArrayTask(method, url, params, headers, mListener!!)
        mTask!!.setmCachemanager(mCacheManager!!)
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

    /**
     * Lets depend on abstraction
     * Sets CacheManager for this
     *
     * @param cache
     * @return JsonObjectType
     */
    override fun setCacheManager(cache: CacheManagerInterface<JSONArray>): JsonArray {
        this.mCacheManager = cache
        return this
    }

}
