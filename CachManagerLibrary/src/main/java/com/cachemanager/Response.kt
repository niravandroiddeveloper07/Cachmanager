package com.cachemanager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

class Response {
    var code: Int? = 0
    private var inputStream: InputStream? = null

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @return String
     * @throws IOException
     */
    val dataAsString: String
        @Throws(IOException::class)
        get() {
            val bufferSize = 1024
            val buffer = CharArray(bufferSize)
            val out = StringBuilder()
            val `in` = InputStreamReader(inputStream!!, "UTF-8")
            while (true) {
                val i = `in`.read(buffer, 0, buffer.size)
                if (i < 0)
                    break
                out.append(buffer, 0, i)
            }
            if (inputStream != null) {
                inputStream!!.close()
            }
            return out.toString()

        }

    /**
     * Converts input Stream to bitmap
     *
     * @return Bitmap
     */
    val asBitmap: Bitmap
        get() {
            val bitmap = BitmapFactory.decodeStream(this.inputStream)
            if (inputStream != null) {
                try {
                    inputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return bitmap
        }

    fun getCode(): Int {
        return code!!
    }

    fun setCode(code: Int): Response {
        this.code = code
        return this
    }

    fun getData(): InputStream? {
        return inputStream
    }

    fun setData(data: InputStream): Response {
        this.inputStream = data
        return this
    }

}


