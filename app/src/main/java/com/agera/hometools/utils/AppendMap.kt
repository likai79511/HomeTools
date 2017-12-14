package com.agera.hometools.utils

import java.util.*

/**
 * Created by Agera on 2017/11/8.
 */
class AppendMap<T> {
    val map: HashMap<String, T> = HashMap()

    init {
        map.clear()
    }

    fun put(key: String, value: T): AppendMap<T> {
        map.put(key, value)
        return this
    }

    fun compile(): HashMap<String, T> {
        return map
    }
}