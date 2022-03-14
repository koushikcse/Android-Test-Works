package com.kusu.contactspicker

import android.graphics.Bitmap

/**
 * Created by Koushik on 2/14/22.
 */
data class Contact(
    val name: String,
    val phone: String,
    val email: String,
    val photo: String?,
    var selected: Boolean = false
)