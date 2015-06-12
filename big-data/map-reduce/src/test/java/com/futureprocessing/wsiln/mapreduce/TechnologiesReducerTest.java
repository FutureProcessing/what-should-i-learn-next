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

}
