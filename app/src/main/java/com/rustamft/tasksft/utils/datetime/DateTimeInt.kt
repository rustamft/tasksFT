package com.rustamft.tasksft.utils.datetime

/**
 * Contains date and time as separate integers.
 */
class DateTimeInt {

    private var _year = 0
    private var _month = 0
    private var _day = 0
    private var _hour = 0
    private var _minute = 0
    val year get() = _year
    val month get() = _month
    val day get() = _day
    val hour get() = _hour
    val minute get() = _minute

    constructor(
        mth: Int,
        d: Int,
        h: Int,
        m: Int
    ) {
        _month = mth
        _day = d
        _hour = h
        _minute = m
    }

    constructor(
        yr: Int,
        mth: Int,
        d: Int
    ) {
        _year = yr
        _month = mth
        _day = d
    }

    constructor(
        h: Int,
        m: Int
    ) {
        _hour = h
        _minute = m
    }
}