package com.futureprocessing.wsiln.mapreduce;

import com.futureprocessing.wsiln.mapreduce.map.MappingType;
import com.futureprocessing.wsiln.mapreduce.map.RelationKey;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;

import static com.futureprocessing.wsiln.mapreduce.TechnologiesFormatter.log;
import static com.futureprocessing.wsiln.mapreduce.TechnologiesFormatter.removeVersion;

public class TechnologiesMapper extends Mapper<LongWritable, Text, RelationKey, MappingType> {
    private final static int MAPPING_SCOPE = 5;
    private boolean omitPost;
    private int mappingScope;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        omitPost = configuration.getBoolean(ConfigurationConstants.OMIT_POSTS, false);
        mappingScope = configuration.getInt(ConfigurationConstants.MAPPING_SCOPE, 5);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try {
            ParserXML parser = new ParserXML(value.toString());
            mapTags(parser.getTags(), context);
            if (!omitPost) {
                mapPosts(parser.getBody(), context);
            }
        } catch (Exception e) {
            log.error("Error on map", e);
        }
    }

    private boolean mapTags(String value, Context context) throws IOException, InterruptedException {

        String[] tags = InputFormatter.splitInputString(value);
        if (tags == null) {
            return true;
        }
        List<String> tagsList = removeVersion(tags);
        for (int i = 0; i < tagsList.size(); i++) {
            String firstTag = tagsList.get(i);
            for (int j = i + 1; j < tagsList.size(); j++) {
                String secondTag = tagsList.get(j);
                if (!firstTag.equals(secondTag)) {
                    context.write(new RelationKey(firstTag, secondTag), MappingType.TAG);
                    context.write(new RelationKey(secondTag, firstTag), MappingType.TAG);
                }
            }
        }
        return false;
    }

    private void mapPosts(String value, Context context) throws IOException, InterruptedException {
        String[] words = InputFormatter.splitInputString(value);
        if (words == null) {
            return;
        }

        List<String> postsList = removeVersion(words);
        createPairs(context, postsList);
    }

    private void createPairs(Context context, List<String> postsList) throws IOException, InterruptedException {
        for (int i = 0; i < postsList.size(); i++) {

            String firstElement = postsList.get(i);
            int scope = (i + mappingScope) < postsList.size() - 1 ? i + mappingScope : postsList.size() - 1;
            for (int j = i + 1; j < scope + 1; j++) {
                String secondElement = postsList.get(j);
                if (!firstElement.equals(secondElement)) {
                    context.write(new RelationKey(firstElement, secondElement), MappingType.POST);
                    context.write(new RelationKey(secondElement, firstElement), MappingType.POST);

                }
            }
        }
    }

}
