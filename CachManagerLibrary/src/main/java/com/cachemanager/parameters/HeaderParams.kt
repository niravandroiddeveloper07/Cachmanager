package com.cachemanager.parameters

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

class HeaderParams {
    internal var key: String? = null
    internal var value: String? = null

    fun getKey(): String? {
        return key
    }

    fun setKey(key: String): HeaderParams {
        this.key = key
        return this
    }

    fun getValue(): String? {
        return value
    }

    fun setValue(value: String): HeaderParams {
        this.value = value
        return this
    }
}
