package com.konh.mycontract.model

import kotlin.math.min

data class ScoresModel(val current:Int, val max:Int) {
    val normalized = ((min(current, max).toFloat() / max) * 100).toInt()
}