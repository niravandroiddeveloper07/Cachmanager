package com.cachemanager.requestTasks

import android.graphics.Bitmap
import com.cachemanager.DownLoadManager
import com.cachemanager.listner.HttpListener
import com.cachemanager.parameters.HeaderParams
import com.cachemanager.parameters.RequestParams
import java.util.*

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

class BitMapTask(private val method: DownLoadManager.Method, private val mUrl: String, private val params: ArrayList<RequestParams>? = null, private val headers: ArrayList<HeaderParams>, private val callback: HttpListener<Bitmap>) : Task<String, Void, Bitmap>() {
    private var error = false

    override fun onPreExecute() {
        super.onPreExecute()

    }

    override fun doInBackground(vararg urls: String): Bitmap? {
        try {
            val response = makeRequest(mUrl!!, method!!, params!!, headers!!)
            val bitmap = response.asBitmap
            if (this.mCacheManager != null) {
                if (this.mCacheManager.getDataFromCache(mUrl) == null)
                    this.mCacheManager.addDataToCache(mUrl, bitmap)
            }
            return bitmap

        } catch (e: Exception) {
            e.printStackTrace()
            error = true
        }

        return null
    }

    override fun onPostExecute(data: Bitmap?) {
        if (data != null) {
            super.onPostExecute(data)
            if (!error)
                this.callback!!.onResponse(data)
            else
                this.callback!!.onError()
        }

    }

    override fun onCancelled() {
        super.onCancelled()
        if (this.mCacheManager != null) {
            this.mCacheManager.removeDataFromCache(mUrl!!)
        }

    }

}
