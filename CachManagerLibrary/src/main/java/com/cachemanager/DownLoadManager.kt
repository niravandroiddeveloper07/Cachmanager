package com.cachemanager

import android.content.Context
import com.cachemanager.datatypes.BitMap
import com.cachemanager.datatypes.JsonArray
import com.cachemanager.datatypes.JsonObject
import com.cachemanager.datatypes.XmlType
import com.cachemanager.parameters.HeaderParams
import com.cachemanager.parameters.RequestParams
import java.util.*

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

class DownLoadManager
/**
 * Constructor
 *
 * @param c it is the context
 */
(private val context: Context) {
    private var url: String? = null
    private var method: Method? = null
    private val params = ArrayList<RequestParams>()
    private val headers = ArrayList<HeaderParams>()

    /**
     * Assigns Url to be loaded
     *
     * @param m, url
     * @return instance
     */
    fun load(m: Method, url: String): DownLoadManager {
        this.url = url
        this.method = m
        return this
    }

    /**
     * Sets json datatype for request
     *
     * @return Json Type
     */
    fun asJsonObject(): JsonObject {
        return JsonObject(method!!, url!!, params, headers)
    }

    /**
     * Sets json datatype for request
     *
     * @return Json Array Type
     */
    fun asJsonArray(): JsonArray {
        return JsonArray(method!!, url!!, params, headers)
    }

    /**
     * Sets bitmap type for request
     *
     * @return Bitmap Type
     */

    fun asBitmap(): BitMap {
        return BitMap(method!!, url!!, params, headers)
    }

    fun asXml(): XmlType {
        return XmlType(method!!, url!!, params, headers)
    }

    /**
     * Sets request body parameters
     *
     * @param key   Parameter key
     * @param value Parameter value
     * @return DownLoadManager instance
     */

    fun setRequestParameter(key: String, value: String): DownLoadManager {
        val pram = RequestParams()
        pram.key = key
        pram.value = value
        this.params.add(pram)
        return this
    }

    /**
     * Sets request header parameters
     *
     * @param key   Parameter key
     * @param value Parameter value
     * @return DownLoadManager instance
     */

    fun setHeaderParameter(key: String, value: String): DownLoadManager {
        val pram = HeaderParams()
        pram.key = key
        pram.value = value
        this.headers.add(pram)
        return this
    }


    enum class Method {
        GET,
        POST,
        PUT,
        DELETE
    }

    companion object {
        private var instance: DownLoadManager? = null

        fun from(c: Context): DownLoadManager {
            return getInstance(c)
        }

        /**
         * Returns singleton instance for network call
         *
         * @param context it is the context of activity
         */
        fun getInstance(context: Context?): DownLoadManager {
            if (context == null)
                throw NullPointerException("Error")


            synchronized(DownLoadManager::class.java) {
                if (instance == null)
                    instance = DownLoadManager(context)
            }

            return instance!!
        }
    }


}
