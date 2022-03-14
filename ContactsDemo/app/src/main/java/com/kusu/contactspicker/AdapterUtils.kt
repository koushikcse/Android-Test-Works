package com.kusu.contactspicker


import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import androidx.databinding.BindingAdapter
import kotlin.random.Random


/**
 * Created by Koushik on 2/15/22.
 */
@BindingAdapter("setAvatar")
fun setAvatar(view: TextView, name: String) {
    val rnd = Random
    val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    val shapeDrawable = view.background as GradientDrawable
    shapeDrawable.setColor(color)
    view.text = name.substring(0, 1).uppercase()
}
