package com.agera.hometools.utils;

import java.util.HashMap;

/**
 * Created by  Agera K on 2017/9/22.
 */
public class AppendMap {

    private HashMap map = new HashMap();

    public AppendMap() {
        map.clear();
    }

    public AppendMap put(String key, String value) {
        map.put(key, value);
        return this;
    }

    public HashMap compile() {
        return map;
    }
}
