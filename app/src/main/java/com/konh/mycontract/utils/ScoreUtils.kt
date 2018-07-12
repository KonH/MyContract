package com.konh.mycontract.utils

import com.konh.mycontract.model.HistoryAggregateModel
import com.konh.mycontract.model.ScoresModel
import kotlin.math.min

fun getNormalized(cur:Int, max:Int) = ((min(cur, max).toFloat() / max) * 100).toInt()

fun ScoresModel.getNormalized(it:ScoresModel) = getNormalized(it.current, it.max)

fun HistoryAggregateModel.getNormalized(it:HistoryAggregateModel) = getNormalized(it.curScore, it.maxScore)