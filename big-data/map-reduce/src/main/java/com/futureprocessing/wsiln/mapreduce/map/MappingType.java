package com.futureprocessing.wsiln.mapreduce.map;

import org.apache.hadoop.io.BinaryComparable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public enum MappingType  implements WritableComparable<MappingType> {

    TAG(new String("T")),
    POST(new String("P"));

    private String value;

    private MappingType(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeBytes(toString());
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        value = in.readLine();
    }
}
