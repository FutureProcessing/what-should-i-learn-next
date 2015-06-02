package com.futureprocessing.wsiln.mapreduce;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;

import java.io.IOException;

public class TechnologiesMapperTest {

    @Test
    public void shouldMapTagsFromRow() throws IOException {
        //given
        Text input = new Text("<row value=\"blabla\" Tags=\"&lt;java&gt;&lt;spring&gt;\" title=\"Java is awesome\" />");

        //when
        new MapDriver<LongWritable, Text, Text, Text>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(1l), input)

                        //then
                .withOutput(new Text("java"), new Text("spring"))
                .withOutput(new Text("spring"), new Text("java"))
                .runTest();
    }

    @Test
    public void shouldMapTagsFromRowWithoutVersionNumber() throws IOException {
        //given
        Text input = new Text("<row value=\"blabla\" Tags=\"&lt;java-7&gt;&lt;spring-3b41-3-52&gt;\" title=\"Java is awesome\" />");

        //when
        new MapDriver<LongWritable, Text, Text, Text>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(1l), input)

                        //then
                .withOutput(new Text("java"), new Text("spring"))
                .withOutput(new Text("spring"), new Text("java"))
                .runTest();
    }

}