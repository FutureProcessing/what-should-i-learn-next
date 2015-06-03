package com.futureprocessing.wsiln.mapreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TechnologiesMapper extends Mapper<LongWritable, Text, Text, Text> {
    private Text outputKey = new Text();
    private Text outputValue = new Text();


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();

        String tagsKey = "Tags=\"";

        int start = line.indexOf(tagsKey);
        if (start < 0) {
            return;
        }

        start += tagsKey.length();
        int end = line.indexOf('"', start);

        String cutOffLine = line.substring(start + 4, end - 4);
        String[] elements = cutOffLine.split("&gt;&lt;");

        for (int i = 0; i < elements.length; i++) {
            outputKey.set(TechnologiesFormatter.removeVersionFromName(elements[i]));

            for (int j = 0; j < elements.length; j++) {
                if (i != j) {
                    outputValue.set(TechnologiesFormatter.removeVersionFromName(elements[j]));
                    context.write(outputKey, outputValue);
                }
            }
        }
    }


}
