package com.futureprocessing.wsiln.mapreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TechnologiesMapper extends Mapper<LongWritable, Text, Text, Text> {
    static String VERSION_PATTERN = "-\\d";
    private Text outputKey = new Text();
    private Text outputValue = new Text();

    static Logger log = Logger.getLogger(TechnologiesMapper.class);

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
            outputKey.set(removeVersionFromName(elements[i]));

            for (int j = 0; j < elements.length; j++) {
                if (i != j) {
                    outputValue.set(removeVersionFromName(elements[j]));
                    context.write(outputKey, outputValue);
                }
            }
        }
    }

    public String removeVersionFromName(String text) {

        Pattern pattern = Pattern.compile(VERSION_PATTERN);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            log.info("Removed " + text.substring(matcher.start()) + " from " + text);
            return text.substring(0, matcher.start());
        }
        return text;
    }
}
