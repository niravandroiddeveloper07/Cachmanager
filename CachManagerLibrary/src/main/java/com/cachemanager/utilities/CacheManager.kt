package com.cachemanager.utilities

import android.graphics.Bitmap
import android.os.SystemClock
import android.util.LruCache
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

class CacheManager<T>
/**
 * Constructor
 *
 * @param cacheSize size of the cache in bytes
 */
(cacheSize: Int) : LruCache<String, T>(cacheSize), CacheManagerInterface<T> {

    private val timeout = 10 * 1000 * 60

    //timestamp to remove unused items after 10 minutes
    internal var cacheHitTimestamp = HashMap<String, Long>()

    override fun sizeOf(key: String, data: T): Int {
        // The cache size will be measured in kilobytes rather than
        // number of items.
        val bytesCount: Int
        if (data is Bitmap) {
            bytesCount = (data as Bitmap).byteCount
        } else if (data is JSONObject) {
            bytesCount = (data as JSONObject).toString().toByteArray().size
        } else {
            bytesCount = (data as JSONArray).toString().toByteArray().size
        }

        return bytesCount / 1024

    }

    /**
     * Adds data to memory cache
     *
     * @param key  key to identify cache resource
     * @param data Data to be stored in cache
     */
    override fun addDataToCache(key: String, data: T) {
        if (getDataFromCache(key) == null) {
            synchronized(this) {
                put(key, data)
                cacheHitTimestamp.put(key, SystemClock.uptimeMillis()) //count to 0 when added
            }
        }
    }


    /**
     * Removes data from cache
     *
     * @param key identifier to resource in cache
     */
    override fun removeDataFromCache(key: String) {
        if (getDataFromCache(key) != null) {
            synchronized(this) {
                remove(key)
            }
        }
    }

    /**
     * Gets resource from cache
     *
     * @param key identifier
     * @return resource
     */
    override fun getDataFromCache(key: String): T {
        synchronized(this) {
            cacheHitTimestamp[key] = SystemClock.uptimeMillis()
            evictUnused()
        }
        return get(key)
    }

    /**
     * Removes items that are not used
     */
    override fun evictUnused() {
        val items = snapshot()
        for (key in items.keys) {
            val cacheTime = cacheHitTimestamp[key]
            if (cacheTime!! + timeout < SystemClock.uptimeMillis()) {
                remove(key)
            }

        }
    }


}
