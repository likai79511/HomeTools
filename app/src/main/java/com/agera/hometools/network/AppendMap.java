package com.agera.hometools.network;

import java.util.HashMap;

/**
 * Created by 43992639 on 2017/9/22.
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
