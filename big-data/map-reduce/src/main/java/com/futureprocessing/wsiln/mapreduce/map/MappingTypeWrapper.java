package com.futureprocessing.wsiln.mapreduce.map;

import org.apache.hadoop.io.BinaryComparable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class MappingTypeWrapper  extends BinaryComparable implements WritableComparable<BinaryComparable> {

    private MappingType mappingType;

    MappingTypeWrapper() {
        mappingType = MappingType.POST;
    }

    public MappingTypeWrapper(MappingType mappingType) {
        this.mappingType = mappingType;
    }

    @Override
    public int getLength() {
        return mappingType.value.length();
    }

    @Override
    public byte[] getBytes() {
        return mappingType.value.getBytes();
    }


    @Override
    public void write(DataOutput out) throws IOException {
        out.writeBytes(mappingType.toString());
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        mappingType.value = in.readLine();
    }

}
