package com.futureprocessing.wsiln.mapreduce;

import com.futureprocessing.wsiln.mapreduce.map.MappingType;
import com.futureprocessing.wsiln.mapreduce.map.RelationKey;
import junitparams.JUnitParamsRunner;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(JUnitParamsRunner.class)
public class TechnologiesMapReduceTest {

    @Test
    public void shouldFindConnectionInTags() throws IOException {
        //given
        Text input = new Text("<row  Tags=\"&lt;java&gt;&lt;spring&gt;&lt;\" Body=\"&lt;p&gt;One java spring/p&gt;&#xA;\"/>");

        //when
        new MapReduceDriver<LongWritable, Text, RelationKey, MappingType, Text, IntWritable>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(11), input)
                .withReducer(new TechnologiesReducer())
                .withOutput(new Text("java\tspring"), new IntWritable(2))
                .withOutput(new Text("spring\tjava"), new IntWritable(2))
                .runTest();
    }

    @Test
    public void shouldNotFindConnectionForNotPopularTechnologiesInTags() throws IOException {
        //given
        Text input = new Text("<row Tags=\"&lt;java&gt;&lt;spring&gt;&lt;css&gt;\"  />");

        //when
        new MapReduceDriver<LongWritable, Text, RelationKey, MappingType, Text, IntWritable>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(11), input)
                .withReducer(new TechnologiesReducer())
                .runTest();
    }

    @Test
    public void shouldFindConnectionInTagsWithoutVersion() throws IOException {
        //given
        Text input = new Text("<row value=\"blabla\" Tags=\"&lt;java-8&gt;&lt;spring&gt;\" title=\"Java is awesome\"  Body=\"&lt;p&gt;One java spring/p&gt;&#xA;\"/>");

        //when
        new MapReduceDriver<LongWritable, Text, RelationKey, MappingType, Text, IntWritable>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(11), input)
                .withReducer(new TechnologiesReducer())
                .withOutput(new Text("java\tspring"), new IntWritable(2))
                .withOutput(new Text("spring\tjava"), new IntWritable(2))
                .runTest();
    }


    @Test
    public void shouldFindConnectionInTagsAndPosts() throws IOException {
        //given
        Text input = new Text("<row Tags=\"&lt;java&gt;&lt;spring&gt;&lt;mongo&gt;\"  Body=\"&lt;p&gt;One java plus spring one mongo is two, but java  one.&lt;/p&gt;&#xA;\"/>");

        //when
        new MapReduceDriver<LongWritable, Text, RelationKey, MappingType, Text, IntWritable>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(11), input)
                .withReducer(new TechnologiesReducer())
                .withOutput(new Text("java\tmongo"), new IntWritable(3))
                .withOutput(new Text("java\tspring"), new IntWritable(2))
                .withOutput(new Text("mongo\tjava"), new IntWritable(3))
                .withOutput(new Text("mongo\tspring"), new IntWritable(2))
                .withOutput(new Text("spring\tjava"), new IntWritable(2))
                .withOutput(new Text("spring\tmongo"), new IntWritable(2))
                .runTest();
    }




}