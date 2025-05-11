package com.common

import java.util.*

class Optional <M> (private val optional: M?) {
    fun isEmpty(): Boolean =  optional == null

    fun isNotEmpty(): Boolean = optional != null

    fun get(): M {
        if (optional == null) {
            throw NoSuchElementException("No value present")
        }
        return optional
    }
}