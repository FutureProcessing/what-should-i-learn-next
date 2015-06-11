package com.futureprocessing.wsiln.mapreduce.map;

import org.apache.hadoop.io.BinaryComparable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Scanner;

public class RelationKey extends BinaryComparable implements WritableComparable<BinaryComparable> {
    String firstTechnology;
    String secondTechnology;

    public RelationKey() {
    }

    public RelationKey(String firstTechnology, String secondTechnology) {
        this.firstTechnology = firstTechnology;
        this.secondTechnology = secondTechnology;
    }

    public String getFirstTechnology() {
        return firstTechnology;
    }

    public String getSecondTechnology() {
        return secondTechnology;
    }

    public Text toText() {
        return new Text(firstTechnology + "\t" + secondTechnology);
    }

    public String toString() {
        return firstTechnology + "\t" + secondTechnology;
    }


    @Override
    public int getLength() {
        return firstTechnology.length() + secondTechnology.length() + 1;
    }

    @Override
    public byte[] getBytes() {
        return toString().getBytes();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeBytes(toString());
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        String line = in.readLine();
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("\t");

        firstTechnology = scanner.next();
        secondTechnology = scanner.next();
    }
}
