package com.mapdemo

interface ListCallback {
    fun onItemClick(pos: Int)
    fun onDeleteClick(area: Area)
}