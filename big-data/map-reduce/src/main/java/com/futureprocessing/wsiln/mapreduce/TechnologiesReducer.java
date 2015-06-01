package com.futureprocessing.wsiln.mapreduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class TechnologiesReducer extends Reducer<Text, Text, Text, IntWritable>{

    private Text outputKey = new Text();
    private IntWritable outputValue = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Map<String, Integer> map = new HashMap<String, Integer>();

        for(Text value: values) {
            String v = value.toString();

            Integer count = map.get(v);
            if(count == null) {
                count = 0;
            }

            map.put(v, count + 1);
        }

        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            outputKey.set(key.toString() + "\t" + entry.getKey());
            outputValue.set(entry.getValue());
            context.write(outputKey, outputValue);
        }
    }
}
