package com.cachemanager.requestTasks

import android.net.Uri
import android.os.AsyncTask
import com.cachemanager.DownLoadManager
import com.cachemanager.parameters.HeaderParams
import com.cachemanager.parameters.RequestParams
import com.cachemanager.Response
import com.cachemanager.utilities.CacheManagerInterface
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

abstract class Task<Params, Progress, Result> : AsyncTask<Params, Progress, Result>() {
    internal val TAG = javaClass.getSimpleName()
    protected lateinit var mCacheManager: CacheManagerInterface<Result>
    internal lateinit var conn: HttpURLConnection

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    @Throws(IOException::class)
    protected fun makeRequest(url: String, m: DownLoadManager.Method, params: ArrayList<RequestParams>, headers: ArrayList<HeaderParams>): Response {
        var `is`: InputStream? = null
        // Only display the first 500 characters of the retrieved
        // web page content.

        val mUrl = URL(url)
        conn = mUrl.openConnection() as HttpURLConnection
        conn.readTimeout = READ_TIMEOUT
        conn.connectTimeout = TIMEOUT
        /*  time in milliseconds */

        when (m) {
            DownLoadManager.Method.GET -> conn.requestMethod = "GET"

            DownLoadManager.Method.POST -> conn.requestMethod = "POST"

            DownLoadManager.Method.PUT -> conn.requestMethod = "PUT"

            DownLoadManager.Method.DELETE -> conn.requestMethod = "DELETE"
        }


        conn.doInput = true
        conn.doOutput = m != DownLoadManager.Method.GET


        //write headers if any
        if (headers.size > 0) {
            for (header in headers) {
                conn.setRequestProperty(header.key, header.value)
            }
        }


        val builder = Uri.Builder()

        //write request parameters
        if (params.size > 0) {
            for (itm in params) {
                builder.appendQueryParameter(itm.key, itm.value)
            }

            val query = builder.build().encodedQuery

            val os = conn.outputStream
            val writer = BufferedWriter(
                    OutputStreamWriter(os, "UTF-8"))
            writer.write(query!!)
            writer.flush()
            writer.close()
            os.close()
        }


        conn.connect()


        val response = conn.responseCode
        `is` = conn.inputStream

        val resp = Response()
        resp.code = response
        resp.setData(`is`)
        return resp
    }


    fun setmCachemanager(cachemanager: CacheManagerInterface<Result>) {
        this.mCacheManager = cachemanager
    }

    companion object {

        internal val READ_TIMEOUT = 10000
        internal val TIMEOUT = 15000
    }


}
