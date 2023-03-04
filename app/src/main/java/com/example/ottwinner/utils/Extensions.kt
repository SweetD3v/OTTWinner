package com.example.ottwinner.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.View
import androidx.core.net.toUri
import kotlin.math.roundToInt


val predChampUrl = "https://play.predchamp.com".toUri()
val qurekaUrl = "https://play.qureka.com".toUri()

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Context.createBitmapFromString(speed: String): Bitmap {
    val ctx = this

    val fontSize = dpToPx(150)
    val pad: Int = fontSize / 9

    val paint = Paint()
    paint.isAntiAlias = true
    paint.textSize = fontSize.toFloat()
    paint.textAlign = Paint.Align.CENTER
    paint.style = Paint.Style.FILL
    paint.typeface = Typeface.createFromAsset(ctx.assets, "fonts/roboto_bold.ttf")

    val textWidth = (paint.measureText(speed) + pad * 2).toInt()
    val bitmap = Bitmap.createBitmap(
        textWidth,
        (fontSize.toFloat() / 0.75f).toInt(),
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    val xOriginal = pad.toFloat()
    canvas.drawText(speed, xOriginal, fontSize.toFloat(), paint)

    return bitmap
}


fun Context.dpToPx(dp: Int): Int {
    val displayMetrics = this.resources.displayMetrics
    return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}