package com.agera.hometools.utils

import java.util.*

/**
 * Created by Agera on 2017/11/8.
 */
class AppendMap {
    val map: HashMap<String, String> = HashMap()

    init {
        map.clear()
    }

    fun put(key: String, value: String): AppendMap {
        map.put(key, value)
        return this
    }

    fun compile(): HashMap<String, String> {
        return map
    }
}