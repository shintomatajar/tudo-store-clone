package com.tudomart.store.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

typealias OnTextChanged = (s: CharSequence, start: Int, before: Int, count: Int) -> Unit
typealias BeforeTextChanged = (s: CharSequence, start: Int, count: Int, after: Int) -> Unit
typealias AfterTextChanged = (editable: Editable?) -> Unit

fun EditText.textChanged(
    onTextChanged: OnTextChanged? = null,
    beforeTextChanged: BeforeTextChanged? = null,
    afterTextChanged: AfterTextChanged? = null
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged?.invoke(editable)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            beforeTextChanged?.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            onTextChanged?.invoke(s, start, before, count)
        }
    })
}