package com.agera.hometools.utils

/**
 * Created by Agera on 2017/12/14.
 */
class AppendList<T> {
    val list: ArrayList<T> = ArrayList()

    init {
        list.clear()
    }

    fun add(value: T): AppendList<T> {
        list.add(value)
        return this
    }

    fun compile(): ArrayList<T> {
        return list
    }
}