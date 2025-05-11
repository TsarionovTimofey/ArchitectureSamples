package com.common.extensions.string
import java.util.UUID

private const val NOT_DIGIT_PATTERN = "[^0-9]"
private const val SQUARE_BRACKET = "(\\[|\\])"
private const val NULL_PATTERN = "0"
private const val X_PATTERN = 'X'
private const val DOT = "."

fun String.removeNotDigits() = replace(Regex(NOT_DIGIT_PATTERN), "")
fun String.clearPhoneFormat() = "+${removeNotDigits()}"

fun generateUUID() = UUID.randomUUID().toString()

fun String.formatHint(
    mask: String?
): String {
    var result = mask ?: ""
    result = result.replace(Regex(SQUARE_BRACKET), "").replace(
        Regex(NULL_PATTERN),
        X_PATTERN.toString()
    )
    forEach {
        result = result.replaceFirst(X_PATTERN, it)
    }

    return result.replace(Regex(X_PATTERN.toString()), NULL_PATTERN)
}

fun String.toCamelCase(delimiter: String = " "): String {
    return split(delimiter).joinToString(delimiter) { word ->
        word.replaceFirstChar(Char::titlecaseChar)
    }
}

fun String.toSafelyDouble() = try {
    toDouble()
} catch (e: Throwable) {
    if (this == DOT) {
        0.0
    } else {
        null
    }
}

fun String.uppercaseFirstLetter() = this.replaceFirst(this.first(), this.first().uppercaseChar())
