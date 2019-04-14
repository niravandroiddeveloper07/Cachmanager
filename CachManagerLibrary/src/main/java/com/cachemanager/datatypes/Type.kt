package com.cachemanager.datatypes

import com.cachemanager.listner.HttpListener
import com.cachemanager.utilities.CacheManagerInterface

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

abstract class Type<T> {
    abstract fun setCacheManager(cacheManager: CacheManagerInterface<T>): Type<*>

    abstract fun setCallback(callback: HttpListener<T>): Type<*>

    abstract fun cancel(): Boolean
}