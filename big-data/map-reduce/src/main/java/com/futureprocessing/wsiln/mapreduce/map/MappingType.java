package com.futureprocessing.wsiln.mapreduce.map;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class MappingType implements WritableComparable<MappingType> {

    private Type type;
    public final static MappingType TAG = new MappingType(Type.TAG);
    public final static MappingType POST = new MappingType(Type.POST);

    private enum Type {
        TAG, POST
    }

    private MappingType(Type type) {
        this.type = type;
    }

    public MappingType() {
    }

    @Override
    public int compareTo(MappingType o) {
        return type.compareTo(o.type);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeBytes(type.toString());
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        type = Type.valueOf(in.readLine());
    }

    @Override
    public String toString(){
       return type.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MappingType that = (MappingType) o;

        return type == that.type;

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
