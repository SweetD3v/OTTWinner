package com.example.ottwinner.utils

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.ottwinner.R
import com.google.android.material.imageview.ShapeableImageView


fun Context.toastCustomView(message: String?, length: Int, colorStr: String) {
    val view = LayoutInflater.from(this).inflate(R.layout.toast_layout, null)
    val toast = Toast(this)
    view?.findViewById<ShapeableImageView>(R.id.imgToastColor)?.setColorFilter(
        Color.parseColor(colorStr),
        PorterDuff.Mode.SRC_ATOP
    )
    view?.findViewById<TextView>(R.id.txtToast)?.text = message
    toast.setGravity(80, 0, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION)
    toast.duration = length
    toast.view = view
    toast.show()
}