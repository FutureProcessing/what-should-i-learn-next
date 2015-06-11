package com.futureprocessing.wsiln.mapreduce;

import com.futureprocessing.wsiln.mapreduce.map.MappingType;
import com.futureprocessing.wsiln.mapreduce.map.RelationKey;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

import static com.futureprocessing.wsiln.mapreduce.TechnologiesFormatter.removeVersionFromName;

public class TechnologiesMapper extends Mapper<LongWritable, Text, RelationKey, MappingType> {
    private final static int MAPPING_SCOPE = 5;


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try {
            ParserXML parser = new ParserXML(value.toString());
            mapTags(parser.getTags(), context);
            mapPosts(parser.getBody(), context);
        } catch (Exception e) {
            return;
        }
    }

    private boolean mapTags(String value, Context context) throws IOException, InterruptedException {

        String[] elements = InputFormatter.splitInputString(value);
        if (elements == null) {
            return true;
        }

        for (int i = 0; i < elements.length; i++) {
            String firstTag = removeVersionFromName(elements[i]);
            for (int j = 0; j < elements.length; j++) {
                if (i != j) {
                    String secondTag = removeVersionFromName(elements[j]);
                        context.write(new RelationKey(firstTag, secondTag), MappingType.TAG);
                }
            }
        }
        return false;
    }

    private boolean mapPosts(String value, Context context) throws IOException, InterruptedException {

        String[] elements = InputFormatter.splitInputString(value);
        if (elements == null) {
            return true;
        }

        for (int i = 0; i < elements.length; i++) {
            String firstElement = removeVersionFromName(elements[i]).toLowerCase();
            int scope = (i + MAPPING_SCOPE) < elements.length - 1 ? i + MAPPING_SCOPE : elements.length - 1;
            for (int j = i; j < scope + 1; j++) {
                if (i != j) {
                    String secondElement = removeVersionFromName(elements[j]);
                    if (!firstElement.equals(secondElement)) {
                        context.write(new RelationKey(firstElement, secondElement), MappingType.POST);
                        context.write(new RelationKey(secondElement, firstElement), MappingType.POST);
                    }
                }
            }
        }
        return false;
    }




}
