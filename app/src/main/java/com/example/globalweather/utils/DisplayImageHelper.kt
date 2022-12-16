package com.example.globalweather.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

class DisplayImageHelper {
    companion object {
        fun displayImage(context: Context, imageView: ImageView, path: String) {
            Glide.with(context)
                .load(path)
                .centerCrop()
                .into(imageView)
        }
    }
}