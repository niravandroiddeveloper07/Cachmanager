package com.cachemanager.datatypes

import android.graphics.Bitmap
import com.cachemanager.DownLoadManager
import com.cachemanager.listner.HttpListener
import com.cachemanager.parameters.HeaderParams
import com.cachemanager.parameters.RequestParams
import com.cachemanager.requestTasks.BitMapTask
import com.cachemanager.utilities.CacheManagerInterface
import java.util.*

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

class BitMap
/**
 * Constructor to load json datatypes
 */
(private val method: DownLoadManager.Method, private val url: String, private val params: ArrayList<RequestParams>, private val headers: ArrayList<HeaderParams>) : Type<Bitmap>() {
    private var listener: HttpListener<Bitmap>? = null
    private var mTask: BitMapTask? = null
    private var mCacheManager: CacheManagerInterface<Bitmap>? = null

    /**
     * Sets future callback after Http response is received
     *
     * @param listener
     */
    override fun setCallback(listener: HttpListener<Bitmap>): BitMap {
        try {
            this.listener = listener
            this.listener!!.onRequest()
            var data: Bitmap? = null
            if (mCacheManager != null) {
                data = mCacheManager!!.getDataFromCache(url)
                if (data != null) {
                    this.listener!!.onResponse(data)
                    return this
                }
            }

            mTask = BitMapTask(method, url, params, headers, this.listener!!)
            mTask!!.setmCachemanager(mCacheManager!!)
            mTask!!.execute()
        } catch (e: IllegalArgumentException) {

        }
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
                listener!!.onCancel()
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
    override fun setCacheManager(cache: CacheManagerInterface<Bitmap>): BitMap {
        this.mCacheManager = cache
        return this
    }


}
