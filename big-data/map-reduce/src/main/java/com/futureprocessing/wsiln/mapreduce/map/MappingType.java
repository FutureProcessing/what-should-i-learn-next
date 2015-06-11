package com.futureprocessing.wsiln.mapreduce.map;

public enum MappingType {
    TAG(new String("T")),
    POST(new String("P"));

    String value;

    private MappingType(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
