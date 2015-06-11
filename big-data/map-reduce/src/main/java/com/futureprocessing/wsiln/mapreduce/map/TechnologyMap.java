package com.futureprocessing.wsiln.mapreduce.map;

public class TechnologyMap {
    private RelationKey key;
    MappingTypeWrapper value;

    public TechnologyMap(RelationKey key, MappingTypeWrapper value) {
        this.key = key;
        this.value = value;
    }

    public TechnologyMap(String firstTechnology, String secondTechnology, MappingTypeWrapper value) {
        this.key = new RelationKey(firstTechnology, secondTechnology);
        this.value = value;
    }

    public RelationKey getKey() {
        return key;
    }

    public MappingTypeWrapper getValue() {
        return value;
    }
}
