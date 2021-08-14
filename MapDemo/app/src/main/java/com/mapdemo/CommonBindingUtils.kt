package com.mapdemo

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter


@BindingAdapter("setImageViewResource")
fun setImageViewResource(imageView: ImageView, resource: String?) {
    if (resource != null) {
        if (resource.equals("circle")) {
            imageView.setImageDrawable(imageView.context.getDrawable(R.drawable.ic_circle))
        } else if (resource.equals("polyline")) {
            imageView.setImageDrawable(imageView.context.getDrawable(R.drawable.ic_polyline))
        } else {
            imageView.setImageDrawable(imageView.context.getDrawable(R.drawable.ic_polygon))
        }
    }
}
