package com.common.extensions.view

import android.annotation.SuppressLint
import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.google.android.material.internal.TextWatcherAdapter

fun EditText.doOnTextUpdated(action: (String) -> Unit) {
    addTextChangedListener(DistinctTextWatcher(action))
}

fun EditText.doOnTextDirectionUpdated(
    action: ((isBack: Boolean, isNext: Boolean, text: CharSequence) -> Unit)
) {
    doOnTextChanged { _: CharSequence?, start: Int, before: Int, count: Int ->
        action.invoke((before - count) == 1, (start == 0), text)
    }
}

@SuppressLint("RestrictedApi")
private class DistinctTextWatcher(private val action: (String) -> Unit) : TextWatcherAdapter() {
    private var lastValue: String? = null

    @SuppressLint("RestrictedApi")
    override fun afterTextChanged(s: Editable) {
        val content = s.toString()
        if (lastValue != content) {
            lastValue = content
            action(content)
        }
    }
}

fun EditText.doOnActionKeyClick(actionKey: Int = EditorInfo.IME_ACTION_DONE, action: () -> Unit) {
    setOnEditorActionListener { _, keyCode, _ ->
        when (keyCode) {
            actionKey -> {
                action.invoke()
                true
            }
            else -> {
                false
            }
        }
    }
}

object Patterns {
    const val ONLY_DIGITAL_VALID = "[0-9]+"
    const val ONLY_DIGITAL_NOT_VALID = "[^0-9]+"
}

fun EditText.addRegexTextWatcher(
    invalidRegex: Regex,
    validRegex: Regex,
    action: (String) -> Unit
) {
    addTextChangedListener(
        RegexTextWatcher(
            editText = this,
            invalidRegex = invalidRegex,
            validRegex = validRegex,
            action = action
        )
    )
}

@SuppressLint("RestrictedApi")
class RegexTextWatcher(
    private val editText: EditText,
    private val invalidRegex: Regex,
    private val validRegex: Regex,
    private val action: (String) -> Unit
) : TextWatcherAdapter() {

    override fun afterTextChanged(editable: Editable) {
        if (editable.matches(validRegex) || editable.isEmpty()) {
            action(editable.toString())
        } else {
            editText.removeTextChangedListener(this)
            val text =
                editable.replace(0, editable.length, editable.toString().replace(invalidRegex, ""))
                    .toString()
            action(text)
            editText.addTextChangedListener(this)
        }
    }
}
