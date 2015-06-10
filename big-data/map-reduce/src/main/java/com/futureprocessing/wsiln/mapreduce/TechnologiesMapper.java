package com.futureprocessing.wsiln.mapreduce;

import com.futureprocessing.wsiln.mapreduce.map.MappingType;
import com.futureprocessing.wsiln.mapreduce.map.RelationKey;
import com.futureprocessing.wsiln.mapreduce.map.TechnologyMap;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

import static com.futureprocessing.wsiln.mapreduce.TechnologiesFormatter.removeVersionFromName;

public class TechnologiesMapper extends Mapper<LongWritable, Text, Text, Text> {
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

        String splitRegex = "&gt;&lt;<>";

        String[] elements = splitInputString(splitRegex, value);
        if (elements == null) {
            return true;
        }

        for (int i = 0; i < elements.length; i++) {
            String firstTag = removeVersionFromName(elements[i]);

            for (int j = 0; j < elements.length; j++) {
                if (i != j) {
                    String secondTag = removeVersionFromName(elements[j]);
                    TechnologyMap map = new TechnologyMap(new RelationKey(firstTag, secondTag), MappingType.TAG);
                    context.write(map.getKey().toText(), map.getValue().getText());
                }
            }
        }
        return false;
    }

    private boolean mapPosts(String value, Context context) throws IOException, InterruptedException {

        String splitRegex = "&lt;|&gt;|&#xA;|/p|[ ;\"'\\/.,_=&<>]";
        String post = value;
        String[] elements = splitInputString(splitRegex, post);
        if (elements == null) {
            return true;
        }

        for (int i = 0; i < elements.length; i++) {
            String firstElement = removeVersionFromName(elements[i]).toLowerCase();
            int scope = (i + MAPPING_SCOPE) < elements.length - 1 ? i + MAPPING_SCOPE : elements.length - 1;
            for (int j = i; j < scope + 1; j++) {
                if (i != j) {
                    if (firstElement.length() <= 1) {
                        break;
                    }
                    String secondElement = removeVersionFromName(elements[j]).toLowerCase();
                    if (secondElement.length() > 1 && !firstElement.equals(secondElement)) {

                        TechnologyMap connection1 = new TechnologyMap(firstElement, secondElement, MappingType.POST);
                        TechnologyMap connection2 = new TechnologyMap(secondElement, firstElement, MappingType.POST);
                        context.write(connection1.getKey().toText(), connection1.getValue().getText());
                        context.write(connection2.getKey().toText(), connection2.getValue().getText());
                    }
                }
            }
        }
        return false;
    }

    private String[] splitInputString( String splitRegex, String value) {

        if (value == null) {
            return null;
        }
        String formattedString = value.replaceAll(splitRegex, " ");
        return formattedString.split(" +");
    }


}
