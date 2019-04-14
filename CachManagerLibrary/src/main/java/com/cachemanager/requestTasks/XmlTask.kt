package com.cachemanager.requestTasks

import com.cachemanager.DownLoadManager
import com.cachemanager.listner.HttpListener
import com.cachemanager.parameters.HeaderParams
import com.cachemanager.parameters.RequestParams

import org.w3c.dom.Document
import org.xml.sax.InputSource

import java.io.StringReader
import java.util.ArrayList

import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by saurabhkumar on 09/08/17.
 */

class XmlTask(private val method: DownLoadManager.Method, private val mUrl: String, private val params: ArrayList<RequestParams>, private val headers: ArrayList<HeaderParams>, private val callback: HttpListener<Document>) : Task<String, Void, Document>() {
    private var error = false

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg urls: String): Document? {
        try {
            val response = makeRequest(mUrl, method, params, headers)
            val xmlfile = response.dataAsString
            val doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(
                            InputSource(
                                    StringReader(xmlfile)
                            )
                    )

            if (this.mCacheManager != null) {
                if (this.mCacheManager.getDataFromCache(mUrl) == null)
                    this.mCacheManager.addDataToCache(mUrl, doc)
            }
            return doc

        } catch (e: Exception) {
            e.printStackTrace()
            error = true
        }

        return null
    }

    override fun onPostExecute(data: Document) {
        super.onPostExecute(data)
        if (!error)
            this.callback.onResponse(data)
        else
            this.callback.onError()
    }

    /**
     * Sometimes users may cancel at almost end, so lets remove if data is in cache
     */
    override fun onCancelled() {
        super.onCancelled()
        if (this.mCacheManager != null) {
            this.mCacheManager.removeDataFromCache(mUrl)
        }
    }
}
