package com.cachmanager.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cachemanager.DownLoadManager
import com.cachemanager.R
import com.cachemanager.listner.HttpListener
import com.cachemanager.utilities.CacheManager
import com.cachmanager.model.User
import com.cachmanager.util.FOURTY_MB
import kotlinx.android.synthetic.main.adapter_pinboard.view.*

/**
 * Created by Nirav Dhanani on 09/04/19.
 */

class PinBoardAdapter(internal var context: Context, var pinboadList: MutableList<User>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var bitmapCacheManager: CacheManager<Bitmap> = CacheManager(FOURTY_MB)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PinBoardVIewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_pinboard, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val current = pinboadList[position]
// for Name of User
        holder.itemView.txtNameofUser.text = current.name
        // for username
        holder.itemView.txtUserName.text = current.userName
        // for number of likes
        val likes = current.numberOfLikes.toString() + ""
        holder.itemView.txtNumberOfLikes.text = likes

        //for image
        DownLoadManager.from(context)
                .load(DownLoadManager.Method.GET, current.uploadedImageUrl)
                .asBitmap()
                .setCacheManager(bitmapCacheManager)
                .setCallback(object : HttpListener<Bitmap> {
                    override fun onRequest() {
                        holder.itemView.loadingbar.visibility = View.VISIBLE
                    }

                    override fun onResponse(data: Bitmap) {
                        holder.itemView.loadingbar.visibility = View.GONE
                        holder.itemView.uploadedImage.setImageBitmap(data)
                    }

                    override fun onError() {
                        holder.itemView.loadingbar.visibility = View.GONE

                    }

                    override fun onCancel() {
                        holder.itemView.loadingbar.visibility = View.GONE

                    }
                })

        // for profile picture
        DownLoadManager
                .from(context)
                .load(DownLoadManager.Method.GET, current.profilePicUrl)
                .asBitmap()
                .setCacheManager(bitmapCacheManager)
                .setCallback(object : HttpListener<Bitmap> {
                    override fun onRequest() {
                        holder.itemView.loadingbar.visibility = View.VISIBLE
                    }

                    override fun onResponse(data: Bitmap) {
                        holder.itemView.loadingbar.visibility = View.GONE
                        holder.itemView.imgProfile.setImageBitmap(data)
                    }

                    override fun onError() {
                       holder.itemView.loadingbar.visibility = View.GONE

                    }

                    override fun onCancel() {
                        holder.itemView.loadingbar.visibility = View.GONE
                    }
                })
        //for Onclick listner


    }

    override fun getItemCount(): Int {
        return pinboadList.size
    }

    fun setList(t: ArrayList<User>) {
        pinboadList=t
    }

    class PinBoardVIewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
    }
}
