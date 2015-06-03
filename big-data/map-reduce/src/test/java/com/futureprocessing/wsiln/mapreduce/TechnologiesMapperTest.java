package com.futureprocessing.wsiln.mapreduce;


import junitparams.JUnitParamsRunner;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;
import junitparams.Parameters;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
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
        String formatted = TechnologiesFormatter.removeVersionFromName(technologies);

        //then
        assertThat(formatted).isEqualTo(formattedTechnologies);
    }

}