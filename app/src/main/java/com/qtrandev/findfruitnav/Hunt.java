package com.qtrandev.findfruitnav;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Quyen on 6/13/2015.
 */
public class Hunt {
    private static String PROP_NAME = "name";
    private static String PROP_TYPE = "type";
    private static String PROP_TREES = "trees";

    private String name;
    private String type = "Mango";
    private String[] trees;

    public Hunt(String name, String type, String[] trees) {
        this.name = name;
        this.type = type;
        this.trees = trees;
    }

    public Map<String, Object> getMapToWrite() {
        Map<String, Object> newTree = new HashMap<String, Object>();
        newTree.put(PROP_NAME, name);
        newTree.put(PROP_TYPE, type);
        newTree.put(PROP_TREES, trees);
        return newTree;
    }
}
