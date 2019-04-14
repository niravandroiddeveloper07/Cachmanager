package com.cachemanager.listner

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

interface HttpListener<T> {
    /**
     * callback starts
     */
    fun onRequest()

    /**
     * Callback that's fired after response
     *
     * @param data of the type T holds the response
     */
    fun onResponse(data: T)

    fun onError()

    fun onCancel()
}
