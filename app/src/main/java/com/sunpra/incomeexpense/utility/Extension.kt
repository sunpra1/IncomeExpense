package com.sunpra.incomeexpense.utility

fun Number.to2d(): Double {
    val doubleStr = this.toString()
    return if (doubleStr.contains(".")) {
        val splitted = doubleStr.split(".")
        val first = splitted.first()
        var last = splitted.last()
        while (last.length < 2) {
            last += "0"
        }
        "$first.${last.take(2)}".toDouble()
    } else {
        "$this.00".toDouble()
    }
}