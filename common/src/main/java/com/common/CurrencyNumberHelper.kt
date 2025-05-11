package com.common

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class CurrencyNumberHelper {

    companion object {
        const val MAX_FRACTION_DIGITS = 0
        const val MIN_FRACTION_DIGITS = 0
        const val MAX_INTEGER_DIGITS = Int.MAX_VALUE
        const val MIN_INTEGER_DIGITS = 1
    }

    fun getString(format: String, input: Double): String =
        String.format(Locale.UK, format, input)
}

fun Double.formatCurrency(
    currencyId: String?,
    maxFractionDigits: Int = CurrencyNumberHelper.MAX_FRACTION_DIGITS,
    minFractionDigits: Int = CurrencyNumberHelper.MIN_FRACTION_DIGITS
): String? {
    return try { // todo check when refactor transaction response
        val format = NumberFormat.getCurrencyInstance(Locale.UK)
        format.maximumFractionDigits = maxFractionDigits
        format.minimumFractionDigits = getMinimumFractionDigits(minFractionDigits, this)
        format.maximumIntegerDigits = CurrencyNumberHelper.MAX_INTEGER_DIGITS
        format.minimumIntegerDigits = CurrencyNumberHelper.MIN_INTEGER_DIGITS
        format.currency = Currency.getInstance(currencyId)
        format.format(this)
    } catch (e: Exception) {
        null
    }
}

private fun getMinimumFractionDigits(minFractionDigits: Int, value: Double) =
    if (minFractionDigits == CurrencyNumberHelper.MIN_FRACTION_DIGITS) {
        if (value - value.toInt() == 0.0) 0 else 2
    } else {
        minFractionDigits
    }

fun Int.formatCurrency(
    currencyId: String?,
    maxFractionDigits: Int = CurrencyNumberHelper.MAX_FRACTION_DIGITS
): String? {
    val format = NumberFormat.getCurrencyInstance(Locale.UK)
    format.maximumFractionDigits = maxFractionDigits
    format.minimumFractionDigits = 0
    format.maximumIntegerDigits = CurrencyNumberHelper.MAX_INTEGER_DIGITS
    format.minimumIntegerDigits = CurrencyNumberHelper.MIN_INTEGER_DIGITS
    format.currency = Currency.getInstance(currencyId)
    return format.format(this)
}

fun String.formatCurrency(value: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.UK)
    format.maximumFractionDigits = CurrencyNumberHelper.MAX_FRACTION_DIGITS
    format.maximumIntegerDigits = CurrencyNumberHelper.MAX_INTEGER_DIGITS
    format.minimumIntegerDigits = CurrencyNumberHelper.MIN_INTEGER_DIGITS
    format.currency = Currency.getInstance(this)
    return format.format(value)
}

fun Double.formatDigits(
    maxFractionDigits: Int = CurrencyNumberHelper.MAX_FRACTION_DIGITS,
    minFractionDigits: Int = CurrencyNumberHelper.MIN_FRACTION_DIGITS
): String? {
    return try {
        val format = NumberFormat.getCurrencyInstance(Locale.UK)
        format.maximumFractionDigits = maxFractionDigits
        format.minimumFractionDigits = getMinimumFractionDigits(minFractionDigits, this)
        format.maximumIntegerDigits = CurrencyNumberHelper.MAX_INTEGER_DIGITS
        format.minimumIntegerDigits = CurrencyNumberHelper.MIN_INTEGER_DIGITS
        format.format(this)
    } catch (e: Exception) {
        null
    }
}

fun Double.formatDigitsOrZero(
    default: String
): String {
    return try {
        val format = NumberFormat.getCurrencyInstance(Locale.UK)
        format.maximumFractionDigits = CurrencyNumberHelper.MAX_FRACTION_DIGITS
        format.minimumFractionDigits =
            getMinimumFractionDigits(CurrencyNumberHelper.MIN_FRACTION_DIGITS, this)
        format.maximumIntegerDigits = CurrencyNumberHelper.MAX_INTEGER_DIGITS
        format.minimumIntegerDigits = CurrencyNumberHelper.MIN_INTEGER_DIGITS
        format.currency = Currency.getInstance("GBP")
        format.format(this)
    } catch (e: Exception) {
        default
    }
}

private const val DOUBLE_DIVIDER = '.'
fun String.formatDigitsOrZero(): String {
    return try {
        val input = filter { it.isDigit() || it == DOUBLE_DIVIDER }
        val parts = input.split(DOUBLE_DIVIDER.toString())
        val valueInout = if (parts.size > 1) {
            if (parts[1].length > 2) {
                input.dropLast(1).toDouble()
            } else {
                input.toDouble()
            }
        } else {
            input.toDouble() / 100
        }
        "%,.2f".format(Locale.UK, valueInout)
    } catch (e: Exception) {
        "0"
    }
}
