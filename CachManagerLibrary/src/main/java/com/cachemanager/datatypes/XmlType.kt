package com.cachemanager.datatypes

import com.cachemanager.DownLoadManager
import com.cachemanager.listner.HttpListener
import com.cachemanager.parameters.HeaderParams
import com.cachemanager.parameters.RequestParams
import com.cachemanager.requestTasks.XmlTask
import com.cachemanager.utilities.CacheManagerInterface
import org.w3c.dom.Document
import java.util.*

/**
 * Created by saurabhkumar on 09/08/17.
 */

class XmlType(private val method: DownLoadManager.Method, private val url: String, private val params: ArrayList<RequestParams>, private val headers: ArrayList<HeaderParams>) : Type<Document>() {
    private var listener: HttpListener<Document>? = null
    private var mTask: XmlTask? = null
    private var mCacheManager: CacheManagerInterface<Document>? = null

    override fun setCallback(listener: HttpListener<Document>): XmlType {
        this.listener = listener
        this.listener!!.onRequest()
        val data: Document?
        if (mCacheManager != null) {
            data = mCacheManager!!.getDataFromCache(url)
            if (data != null) {
                this.listener!!.onResponse(data)
                return this
            }
        }

        mTask = XmlTask(method, url, params, headers, this.listener!!)
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
    override fun setCacheManager(cache: CacheManagerInterface<Document>): XmlType {
        this.mCacheManager = cache
        return this
    }
}
