package com.futureprocessing.wsiln.mapreduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TechnologiesReducerTest {

    @Test
    public void shouldCountTechnologies() throws IOException {
        //given
        List<Text> input = Arrays.asList(new Text("spring"), new Text("spring"));

        //when
        new ReduceDriver<Text, Text, Text, IntWritable>()
                .withReducer(new TechnologiesReducer())
                .withInput(new Text("java"), input)

                        //then
                .withOutput(new Text("java\tspring"), new IntWritable(2))
                .runTest();
    }

    @Test
    public void shouldNotCountNotPopularTechnologies() throws IOException {
        //given
        List<Text> input = Arrays.asList(new Text("mongo"), new Text("mongo"), new Text("mongo"), new Text("spring"));

        //when
        new ReduceDriver<Text, Text, Text, IntWritable>()
                .withReducer(new TechnologiesReducer())
                .withInput(new Text("java"), input)

                        //then
                .withOutput(new Text("java\tmongo"), new IntWritable(3))
                .runTest();
    }

}
