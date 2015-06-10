package com.futureprocessing.wsiln.mapreduce.map;

public class TechnologyMap {
    private RelationKey key;
    MappingType value;

    public TechnologyMap(RelationKey key, MappingType value) {
        this.key = key;
        this.value = value;
    }

    public TechnologyMap(String firstTechnology, String secondTechnology, MappingType value) {
        this.key = new RelationKey(firstTechnology, secondTechnology);
        this.value = value;
    }

    public RelationKey getKey() {
        return key;
    }

    public MappingType getValue() {
        return value;
    }
}
