package com.example.myapplication.util

import android.text.Editable
import android.widget.EditText
import androidx.fragment.app.Fragment

fun EditText.setEditableText(text:String) {
    this.text = Editable.Factory.getInstance().newEditable(text)
}

fun Int.toEditable(): Editable {
    return Editable.Factory.getInstance().newEditable(this.toString())
}