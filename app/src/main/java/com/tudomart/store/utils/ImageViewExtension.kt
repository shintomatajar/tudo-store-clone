package com.tudomart.store.utils

import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tudomart.store.R

fun ImageView.loadImage(url: String, isLargeImage: Boolean = false) {
    Glide.with(this.context)
        .load(url)
        .placeholder(
            if (isLargeImage) {
                R.drawable.img_placeholder_wide
            } else {
                R.drawable.img_placeholder
            }
        )
        .transition(
            DrawableTransitionOptions
                .withCrossFade()
        )
        .error(
            if (isLargeImage) {
                R.drawable.img_placeholder_wide
            } else {
                R.drawable.img_placeholder
            }
        )
        .into(this)
}

fun ImageView.loadCircleImage(url: String, isLargeImage: Boolean = false) {
    Glide.with(this.context)
        .load(url)
        .circleCrop()
        .placeholder(
            if (isLargeImage) {
                R.drawable.img_placeholder_wide
            } else {
                R.drawable.img_placeholder
            }
        )
        .transition(
            DrawableTransitionOptions
                .withCrossFade()
        )
        .error(
            if (isLargeImage) {
                R.drawable.img_placeholder_wide
            } else {
                R.drawable.img_placeholder
            }
        )
        .into(this)
}

fun ImageView.setTint(@ColorRes id: Int) {
    this.setColorFilter(
        ContextCompat.getColor(context, id),
        android.graphics.PorterDuff.Mode.SRC_IN
    )
}