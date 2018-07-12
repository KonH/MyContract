package com.konh.mycontract.model

import com.konh.mycontract.utils.getNormalized

data class ScoresModel(val current:Int, val max:Int) {
    val normalized = getNormalized(this)
}