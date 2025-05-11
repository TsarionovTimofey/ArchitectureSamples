package com.common.extensions

inline fun Boolean.ifTrue(action: () -> Unit): Boolean {
    if (this) action()
    return this
}

inline fun Boolean.ifFalse(action: () -> Unit): Boolean {
    if (!this) action()
    return this
}

inline fun Boolean?.orFalse() = this ?: false

inline fun Boolean?.orTrue() = this ?: true

inline fun Boolean?.isTrue() = this == true

inline fun Boolean?.isFalse() = this == false

