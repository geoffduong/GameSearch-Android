package com.geoffduong.gamesearch.data

import android.graphics.Bitmap

data class Image(var icon_url: String?) {
    @Transient
    var icon_bitmap: Bitmap? = null
}
