package com.futureprocessing.wsiln.mapreduce;

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
    public void shouldMapTagsAndCountAllPopularTechnologies() throws IOException {
        //given
        Text input = new Text("<row value=\"blabla\" Tags=\"&lt;java&gt;&lt;spring&gt;&lt;spring&gt;\" title=\"Java is awesome\" />");

        //when
        new MapReduceDriver<LongWritable, Text, Text, Text, Text, IntWritable>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(11), input)
                .withReducer(new TechnologiesReducer())
                .withOutput(new Text("java\tspring"), new IntWritable(2))
                .withOutput(new Text("spring\tjava"), new IntWritable(2))
                .withOutput(new Text("spring\tspring"), new IntWritable(2))
                .runTest();
    }

    @Test
    public void shouldMapTagsAndNotCountNotPopularTechnologies() throws IOException {
        //given
        Text input = new Text("<row value=\"blabla\" Tags=\"&lt;java&gt;&lt;spring&gt;&lt;css&gt;\" title=\"Java is awesome\" />");

        //when
        new MapReduceDriver<LongWritable, Text, Text, Text, Text, IntWritable>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(11), input)
                .withReducer(new TechnologiesReducer())
                .runTest();
    }

    @Test
    public void shouldMapTagsRemoveVersionsAndCountAllPopularTechnologies() throws IOException {
        //given
        Text input = new Text("<row value=\"blabla\" Tags=\"&lt;java-8&gt;&lt;spring&gt;&lt;spring&gt;\" title=\"Java is awesome\" />");

        //when
        new MapReduceDriver<LongWritable, Text, Text, Text, Text, IntWritable>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(11), input)
                .withReducer(new TechnologiesReducer())
                .withOutput(new Text("java\tspring"), new IntWritable(2))
                .withOutput(new Text("spring\tjava"), new IntWritable(2))
                .withOutput(new Text("spring\tspring"), new IntWritable(2))
                .runTest();
    }

}