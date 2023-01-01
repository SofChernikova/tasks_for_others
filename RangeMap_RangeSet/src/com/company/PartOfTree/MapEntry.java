package com.company.PartOfTree;

public class MapEntry implements Comparable<MapEntry>{ //что храним в узле
    private Range key;
    private String value;

    public MapEntry(Range key, String value) {
        this.key = key;
        this.value = value;
    }

    public Range getKey() {
        return key;
    }

    public void setKey(Range key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(MapEntry o) {
        return this.key.compareTo(o.key);
    }
}
