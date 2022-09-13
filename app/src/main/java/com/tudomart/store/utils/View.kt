package com.tudomart.store.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.tudomart.store.R

fun View.hideKeyboard() {
    val inputMethodManager =
        context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

@SuppressLint("ClickableViewAccessibility")
fun View.onTap(block: (view: View) -> Boolean) {
    val gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                return block(this@onTap)
            }
        })

    setOnTouchListener { _, motionEvent -> gestureDetector.onTouchEvent(motionEvent) }
}

fun View.showPrimarySnackBar(context: Context, message: String) =
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(ContextCompat.getColor(context, R.color.colorPrimary)).show()

fun View.showNormalSnackBar(message: String) =
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()

fun View.hide(keepSpace: Boolean = false) {
    this.visibility = if (keepSpace) {
        INVISIBLE
    } else {
        GONE
    }
}

fun View.show() {
    this.visibility = VISIBLE
}