package com.varnit.android.hikeassignment.ui.images.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.varnit.android.hikeassignment.R
import com.varnit.android.hikeassignment.data.images.FlickerPhotosResponseModel

class FlickerImageAdapter : RecyclerView.Adapter<FlickerImageAdapter.ViewHolder>() {
    var flickerImagesList = mutableListOf<FlickerPhotosResponseModel.Photos.Photo?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.flicker_image_item, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return flickerImagesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(flickerImagesList[position])
    }

    //This method is updating the list
    fun updateData(data: ArrayList<FlickerPhotosResponseModel.Photos.Photo?>) {
        flickerImagesList.addAll(data)
        notifyDataSetChanged()
    }

    fun clearList() {
        flickerImagesList.clear()
    }

    //This Class is holding the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context

        fun bindItems(flickerImage: FlickerPhotosResponseModel.Photos.Photo?) {
            val imageUrl = "https://farm" + flickerImage?.farm + ".static.flickr.com/" + flickerImage?.server + "/" + flickerImage?.id + "_" + flickerImage?.secret + ".jpg"
            val flickerImage = itemView.findViewById(R.id.item_image) as ImageView
            Picasso.with(context).load(imageUrl).into(flickerImage)
        }
    }
}