package com.futureprocessing.wsiln.mapreduce;


import junitparams.JUnitParamsRunner;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Test;
import junitparams.Parameters;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


import java.io.IOException;

import static com.futureprocessing.wsiln.mapreduce.TechnologiesFormatter.removeVersionFromName;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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
    @Parameters({
            "java-8, java",
            "spring-3b21, spring",
            "mongo-12-2332-342, mongo"
    })
    public void shouldMapTagsFromRowWithoutVersionNumber(String technologies, String formattedTechnologies) throws IOException {
        //given
        //when
        String formatted = removeVersionFromName(technologies);

        //then
        assertThat(formatted).isEqualTo(formattedTechnologies);
    }
    @Test
    public void shouldMapTagsFromRowWithMultipleTechnologies() throws IOException {
        //given
        Text input = new Text("<row value=\"blablabla\" Tags=\"&lt;ruby&gt;&lt;rails&gt;&lt;css&gt;\" title=\"Ruby is awesome\" />");

        //when
        new MapDriver<LongWritable, Text, Text, Text>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(11), input)

                        //then
                .withOutput(new Text("ruby"), new Text("rails"))
                .withOutput(new Text("ruby"), new Text("css"))
                .withOutput(new Text("rails"), new Text("ruby"))
                .withOutput(new Text("rails"), new Text("css"))
                .withOutput(new Text("css"), new Text("ruby"))
                .withOutput(new Text("css"), new Text("rails"))

                .runTest();
    }
    @Test
    public void shouldMapTagsFromRowUsingList() throws IOException {
        //given
        Text input = new Text("<row value=\"blablabla\" Tags=\"&lt;ruby&gt;&lt;rails&gt;\" title=\"Ruby is awesome\" />");

        //when
        List<Pair<Text, Text>> map = new ArrayList<Pair<Text, Text>>();
        map.add(new Pair(new Text("ruby"), new Text("rails")));
        map.add(new Pair(new Text("rails"), new Text("ruby")));
        new MapDriver<LongWritable, Text, Text, Text>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(1l), input)

                 //then
                .withAllOutput(map)

                .runTest();
    }

}