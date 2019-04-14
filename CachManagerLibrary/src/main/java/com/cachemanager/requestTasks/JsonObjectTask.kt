package com.cachemanager.requestTasks

import com.cachemanager.DownLoadManager
import com.cachemanager.listner.HttpListener
import com.cachemanager.parameters.HeaderParams
import com.cachemanager.parameters.RequestParams
import org.json.JSONObject
import java.util.*

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

class JsonObjectTask(private val method: DownLoadManager.Method, private val mUrl: String, private val params: ArrayList<RequestParams>, private val headers: ArrayList<HeaderParams>, private val callback: HttpListener<JSONObject>) : Task<String, Void, JSONObject>() {
    private var error = false

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg urls: String): JSONObject? {
        try {
            val response = makeRequest(mUrl, method, params, headers)
            val json = JSONObject(response.dataAsString)
            if (this.mCacheManager != null) {
                if (this.mCacheManager.getDataFromCache(mUrl) == null)
                    this.mCacheManager.addDataToCache(mUrl, json)
            }
            return json

        } catch (e: Exception) {
            e.printStackTrace()
            error = true
        }

        return null
    }

    override fun onPostExecute(data: JSONObject) {
        super.onPostExecute(data)
        if (!error)
            this.callback.onResponse(data)
        else
            this.callback.onError()
    }

    /**
     * Sometimes users may cancel at almost end, so lets remove if data is in cache
     */
    override fun onCancelled() {
        super.onCancelled()
        if (this.mCacheManager != null) {
            this.mCacheManager.removeDataFromCache(mUrl)
        }
    }
}
