package com.futureprocessing.wsiln.mapreduce;

import com.futureprocessing.wsiln.mapreduce.map.MappingType;
import com.futureprocessing.wsiln.mapreduce.map.RelationKey;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;


public class TechnologiesReducer extends Reducer<RelationKey, MappingType, Text, IntWritable> {
    static Logger log = Logger.getLogger(TechnologiesReducer.class);

    private static final Integer MIN_NUMBER_OF_CONECTION = 2;
    private Text outputKey = new Text();
    private IntWritable outputValue = new IntWritable();

    @Override
    protected void reduce(RelationKey key, Iterable<MappingType> values, Context context) throws IOException, InterruptedException {
        boolean tagExists = false;
        Integer count = 0;
        if (!key.hasCorrectData()) {
            log.warn("Incorrect key: '" + key.toString()+"'");
            return;
        }
        for (MappingType value : values) {
            if (value.toString().equals(MappingType.TAG.toString())) {
                tagExists = true;
            }
            count++;
        }
        if (tagExists && (count) >= MIN_NUMBER_OF_CONECTION) {
            outputKey.set(key.toString());
            outputValue.set(count);
            context.write(outputKey, outputValue);
        }
    }
}
