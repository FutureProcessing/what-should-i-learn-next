package com.futureprocessing.wsiln.mapreduce;


import com.futureprocessing.wsiln.mapreduce.map.MappingType;
import com.futureprocessing.wsiln.mapreduce.map.TechnologyMap;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.futureprocessing.wsiln.mapreduce.TechnologiesFormatter.removeVersionFromName;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class TechnologiesMapperTest {

    @Test
    public void shouldMapTagsFromRow() throws IOException {
        //given
        Text input = new Text("<row value=\"blabla\" Tags=\"&lt;java&gt;&lt;spring&gt;\" title=\"Java is awesome\" />");
        List<Pair<Text, Text>> map = new ArrayList<Pair<Text, Text>>();
        map.add(getPair(new TechnologyMap("java", "spring", MappingType.TAG)));
        map.add(getPair(new TechnologyMap("spring", "java", MappingType.TAG)));
        //when
        new MapDriver<LongWritable, Text, Text, Text>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(1l), input)

                        //then
                .withAllOutput(map)
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
    public void shouldMapWordPairFromText() throws IOException  {
        //given
        Text input = new Text("<row Id=\"1\" PostTypeId=\"1\" CreationDate=\"2012-07-31T20:13:55.293\" Score=\"2\" ViewCount=\"321\" Body=\"&lt;p&gt;One plus one is two, but one.&lt;/p&gt;&#xA;\" OwnerUserId=\"14\" LastEditorUserId=\"10\" LastEditDate=\"2012-07-31T20:16:01.033\" LastActivityDate=\"2014-09-17T16:42:39.197\" Title=\"Live Agent ports/ip addresses\" Tags=\"&lt;liveagent&gt;\" AnswerCount=\"2\" CommentCount=\"0\" />");

        List<Pair<Text, Text>> map = new ArrayList<Pair<Text, Text>>();
        map.add(getPair(new TechnologyMap("one", "plus", MappingType.POST)));
        map.add(getPair(new TechnologyMap("plus", "one", MappingType.POST)));

        map.add(getPair(new TechnologyMap("one", "is", MappingType.POST)));
        map.add(getPair(new TechnologyMap("is", "one", MappingType.POST)));


        map.add(getPair(new TechnologyMap("one", "two", MappingType.POST)));
        map.add(getPair(new TechnologyMap("two", "one", MappingType.POST)));

        map.add(getPair(new TechnologyMap("one", "but", MappingType.POST)));
        map.add(getPair(new TechnologyMap("but", "one", MappingType.POST)));


        map.add(getPair(new TechnologyMap("plus", "one", MappingType.POST)));
        map.add(getPair(new TechnologyMap("one", "plus", MappingType.POST)));


        map.add(getPair(new TechnologyMap("plus", "is", MappingType.POST)));
        map.add(getPair(new TechnologyMap("is", "plus", MappingType.POST)));


        map.add(getPair(new TechnologyMap("plus", "two", MappingType.POST)));
        map.add(getPair(new TechnologyMap("two", "plus", MappingType.POST)));


        map.add(getPair(new TechnologyMap("plus", "but", MappingType.POST)));
        map.add(getPair(new TechnologyMap("but", "plus", MappingType.POST)));


        map.add(getPair(new TechnologyMap("plus", "one", MappingType.POST)));
        map.add(getPair(new TechnologyMap("one", "plus", MappingType.POST)));


        map.add(getPair(new TechnologyMap("one", "is", MappingType.POST)));
        map.add(getPair(new TechnologyMap("is", "one", MappingType.POST)));


        map.add(getPair(new TechnologyMap("one", "two", MappingType.POST)));
        map.add(getPair(new TechnologyMap("two", "one", MappingType.POST)));


        map.add(getPair(new TechnologyMap("one", "but", MappingType.POST)));
        map.add(getPair(new TechnologyMap("but", "one", MappingType.POST)));


        map.add(getPair(new TechnologyMap("is", "two", MappingType.POST)));
        map.add(getPair(new TechnologyMap("two", "is", MappingType.POST)));


        map.add(getPair(new TechnologyMap("is", "but", MappingType.POST)));
        map.add(getPair(new TechnologyMap("but", "is", MappingType.POST)));


        map.add(getPair(new TechnologyMap("is", "one", MappingType.POST)));
        map.add(getPair(new TechnologyMap("one", "is", MappingType.POST)));


        map.add(getPair(new TechnologyMap("two", "but", MappingType.POST)));
        map.add(getPair(new TechnologyMap("but", "two", MappingType.POST)));


        map.add(getPair(new TechnologyMap("two", "one", MappingType.POST)));
        map.add(getPair(new TechnologyMap("one", "two", MappingType.POST)));


        map.add(getPair(new TechnologyMap("but", "one", MappingType.POST)));
        map.add(getPair(new TechnologyMap("one", "but", MappingType.POST)));


        //when
        new MapDriver<LongWritable, Text, Text, Text>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(1l), input)

                        //then
                .withAllOutput(map)
                .runTest();
    }

    private Pair<Text, Text> getPair(TechnologyMap map){
        return new Pair<Text, Text>(map.getKey().toText(), map.getValue().getText());

    }

}