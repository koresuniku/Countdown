package com.koresuniku.countdown

import android.annotation.SuppressLint

import java.text.SimpleDateFormat
import java.util.Date

/**
* Created by koresuniku on 13.03.18.
*/

object Counter {

    private const val THE_DAY = "27/07/2027"

    @SuppressLint("SimpleDateFormat")
    fun countDaysLeft(): Int {
        val theDay = SimpleDateFormat("dd/M/yyyy").parse(THE_DAY)
        val today = Date()

        val diff = theDay.time - today.time

        return (diff / (1000 * 60 * 60 * 24)).toInt()
    }
}
