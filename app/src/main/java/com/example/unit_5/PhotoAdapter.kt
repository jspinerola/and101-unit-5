package com.example.unit_5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PhotoAdapter(private val photoList: List<PhotoItem>) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoImageView: ImageView = view.findViewById(R.id.itemImage)
        val authorTextView: TextView = view.findViewById(R.id.authorTextView)
        val urlTextView: TextView = view.findViewById(R.id.urlTextView)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photoList[position]

        holder.authorTextView.text = "Author: ${photo.author}"
        holder.urlTextView.text = photo.url

        Glide.with(holder.itemView.context)
            .load(photo.imageUrl)
            .fitCenter()
            .into(holder.photoImageView)
    }
}