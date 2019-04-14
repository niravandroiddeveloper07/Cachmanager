package com.cachemanager.utilities

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

interface CacheManagerInterface<T> {
    fun addDataToCache(key: String, data: T)

    fun removeDataFromCache(key: String)

    fun getDataFromCache(key: String): T

    fun evictUnused()
}
