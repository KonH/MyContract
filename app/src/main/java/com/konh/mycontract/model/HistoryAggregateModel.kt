package com.konh.mycontract.model

import com.konh.mycontract.utils.getNormalized
import java.util.*

data class HistoryAggregateModel(val day:Calendar, val dayStr:String, val curScore:Int, val maxScore:Int) {
    val normalized = getNormalized(this)

    override fun toString(): String {
        return "HistoryAggregateModel($dayStr, $curScore/$maxScore=$normalized)"
    }
}