package com.futureprocessing.wsiln.mapreduce;

import com.futureprocessing.wsiln.mapreduce.map.MappingType;
import com.futureprocessing.wsiln.mapreduce.map.RelationKey;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;

import static com.futureprocessing.wsiln.mapreduce.TechnologiesFormatter.removeVersion;

public class TechnologiesMapper extends Mapper<LongWritable, Text, RelationKey, MappingType> {
    private final static int MAPPING_SCOPE = 5;
    private boolean omitPost;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        omitPost = configuration.getBoolean(ConfigurationConstants.OMIT_POSTS, false);
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
            return;
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

    private boolean mapPosts(String value, Context context) throws IOException, InterruptedException {

        String[] words = InputFormatter.splitInputString(value);
        if (words == null) {
            return true;
        }

        List<String> postsList = removeVersion(words);
        for (int i = 0; i < postsList.size(); i++) {
            String firstElement = postsList.get(i);
            int scope = (i + MAPPING_SCOPE) < postsList.size() - 1 ? i + MAPPING_SCOPE : postsList.size() - 1;
            for (int j = i + 1; j < scope + 1; j++) {
                String secondElement = postsList.get(j);
                if (!firstElement.equals(secondElement)) {
                    context.write(new RelationKey(firstElement, secondElement), MappingType.POST);
                    context.write(new RelationKey(secondElement, firstElement), MappingType.POST);
                }
            }
        }
        return false;
    }


}
