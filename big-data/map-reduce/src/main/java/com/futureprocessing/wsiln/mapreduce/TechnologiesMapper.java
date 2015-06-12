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

        String[] tags = InputFormatter.splitInputString(value);
        if (tags == null) {
            return true;
        }

        for (int i = 0; i < tags.length; i++) {
            String firstTag = removeVersionFromName(tags[i]);
            for (int j = 0; j < tags.length; j++) {
                if (i != j) {
                    String secondTag = removeVersionFromName(tags[j]);
                    if (!firstTag.equals(secondTag)) {
                        context.write(new RelationKey(firstTag, secondTag), MappingType.TAG);
                    }
                }
            }
        }
        return false;
    }

    private boolean mapPosts(String value, Context context) throws IOException, InterruptedException {

        String[] words = InputFormatter.splitInputString(value);
        if (words == null) {
            return true;
        }

        for (int i = 0; i < words.length; i++) {
            String firstElement = removeVersionFromName(words[i]).toLowerCase();
            int scope = (i + MAPPING_SCOPE) < words.length - 1 ? i + MAPPING_SCOPE : words.length - 1;
            for (int j = i; j < scope + 1; j++) {
                if (i != j) {
                    String secondElement = removeVersionFromName(words[j]);
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
