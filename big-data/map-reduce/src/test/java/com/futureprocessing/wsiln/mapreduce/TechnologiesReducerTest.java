package com.futureprocessing.wsiln.mapreduce;

import com.futureprocessing.wsiln.mapreduce.map.MappingType;
import com.futureprocessing.wsiln.mapreduce.map.RelationKey;
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
        List<MappingType> input = Arrays.asList(MappingType.POST, MappingType.TAG);

        //when
        new ReduceDriver<RelationKey, MappingType, Text, IntWritable>()
                .withReducer(new TechnologiesReducer())
                .withInput(new RelationKey("java", "spring"), input)

                        //then
                .withOutput(new Text("java\tspring"), new IntWritable(2))
                .runTest();
    }

    @Test
    public void shouldNotCountNotPopularTechnologies() throws IOException {
        //given
        List<MappingType> input = Arrays.asList(MappingType.TAG);

        //when
        new ReduceDriver<RelationKey, MappingType, Text, IntWritable>()
                .withReducer(new TechnologiesReducer())
                .withInput(new RelationKey("java", "spring"), input)
                .runTest();
    }

    @Test
    public void shouldNotCountIfNotExistInTags() throws IOException {
        List<MappingType> input = Arrays.asList(MappingType.POST, MappingType.POST, MappingType.POST, MappingType.POST, MappingType.POST);

        //when
        new ReduceDriver<RelationKey, MappingType, Text, IntWritable>()
                .withReducer(new TechnologiesReducer())
                .withInput(new RelationKey("java", "spring"), input)
                        //then
                .runTest();

    }

    @Test
    public void shouldNotCountIfRelationKeyIsIncorrect() throws IOException {
        //given
        List<MappingType> input = Arrays.asList(MappingType.POST, MappingType.TAG);

        //when
        new ReduceDriver<RelationKey, MappingType, Text, IntWritable>()
                .withReducer(new TechnologiesReducer())
                .withInput(new RelationKey("java", "\t"), input)
                .runTest();
    }



    @Test
    public void shouldCountAllPopularTechnologies() throws IOException {
        //given
        List<Text> input = Arrays.asList(new Text("c#"), new Text("c#"), new Text("c#"), new Text("c#"), new Text("VB"), new Text("VB"));

        //when
        new ReduceDriver<Text, Text, Text, IntWritable>()
                .withReducer(new TechnologiesReducer())
                .withInput(new Text(".net"), input)

                        //then
                .withOutput(new Text(".net\tVB"), new IntWritable(2))
                .withOutput(new Text(".net\tc#"), new IntWritable(4))
                .runTest();
    }

}
