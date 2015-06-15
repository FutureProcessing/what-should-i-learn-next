package com.futureprocessing.wsiln.mapreduce;


import com.futureprocessing.wsiln.mapreduce.map.MappingType;
import com.futureprocessing.wsiln.mapreduce.map.RelationKey;
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
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class TechnologiesMapperTest {

    @Test
    public void shouldMapTagsFromRow() throws IOException {
        //given
        Text input = new Text("<row value=\"blabla\" Tags=\"&lt;java&gt;&lt;spring&gt;\" title=\"Java is awesome\" />");
        List<Pair<RelationKey, MappingType>> map = new ArrayList<Pair<RelationKey, MappingType>>();
        map.add(new Pair(new RelationKey("java", "spring"), MappingType.TAG));
        map.add(new Pair(new RelationKey("spring", "java"), MappingType.TAG));
        //when
        new MapDriver<LongWritable, Text, RelationKey, MappingType>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(1l), input)

                        //then
                .withAllOutput(map)
                .runTest();
    }


    @Test
    public void shouldNotMapTagsWithDuplicatedTechnologies() throws IOException {
        //given
        Text input = new Text("<row value=\"blabla\" Tags=\"&lt;java-7&gt;&lt;java-8&gt;\" title=\"Java is awesome\" />");
        List<Pair<RelationKey, MappingType>> map = new ArrayList<Pair<RelationKey, MappingType>>();
        //when
        new MapDriver<LongWritable, Text, RelationKey, MappingType>()
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
        Text input = new Text("<row Id=\"1\" Body=\"&lt;p&gt;One plus one is two, but one.&lt;/p&gt;&#xA;\"  />");

        List<Pair<RelationKey, MappingType>> map = new ArrayList<Pair<RelationKey, MappingType>>();

        map.add(new Pair(new RelationKey("one", "plus"), MappingType.POST));
        map.add(new Pair(new RelationKey("plus", "one"), MappingType.POST));

        map.add(new Pair(new RelationKey("one", "is"), MappingType.POST));
        map.add(new Pair(new RelationKey("is", "one"), MappingType.POST));


        map.add(new Pair(new RelationKey("one", "two"), MappingType.POST));
        map.add(new Pair(new RelationKey("two", "one"), MappingType.POST));

        map.add(new Pair(new RelationKey("one", "but"), MappingType.POST));
        map.add(new Pair(new RelationKey("but", "one"), MappingType.POST));


        map.add(new Pair(new RelationKey("plus", "one"), MappingType.POST));
        map.add(new Pair(new RelationKey("one", "plus"), MappingType.POST));


        map.add(new Pair(new RelationKey("plus", "is"), MappingType.POST));
        map.add(new Pair(new RelationKey("is", "plus"), MappingType.POST));


        map.add(new Pair(new RelationKey("plus", "two"), MappingType.POST));
        map.add(new Pair(new RelationKey("two", "plus"), MappingType.POST));


        map.add(new Pair(new RelationKey("plus", "but"), MappingType.POST));
        map.add(new Pair(new RelationKey("but", "plus"), MappingType.POST));


        map.add(new Pair(new RelationKey("plus", "one"), MappingType.POST));
        map.add(new Pair(new RelationKey("one", "plus"), MappingType.POST));


        map.add(new Pair(new RelationKey("one", "is"), MappingType.POST));
        map.add(new Pair(new RelationKey("is", "one"), MappingType.POST));


        map.add(new Pair(new RelationKey("one", "two"), MappingType.POST));
        map.add(new Pair(new RelationKey("two", "one"), MappingType.POST));


        map.add(new Pair(new RelationKey("one", "but"), MappingType.POST));
        map.add(new Pair(new RelationKey("but", "one"), MappingType.POST));


        map.add(new Pair(new RelationKey("is", "two"), MappingType.POST));
        map.add(new Pair(new RelationKey("two", "is"), MappingType.POST));


        map.add(new Pair(new RelationKey("is", "but"), MappingType.POST));
        map.add(new Pair(new RelationKey("but", "is"), MappingType.POST));


        map.add(new Pair(new RelationKey("is", "one"), MappingType.POST));
        map.add(new Pair(new RelationKey("one", "is"), MappingType.POST));


        map.add(new Pair(new RelationKey("two", "but"), MappingType.POST));
        map.add(new Pair(new RelationKey("but", "two"), MappingType.POST));


        map.add(new Pair(new RelationKey("two", "one"), MappingType.POST));
        map.add(new Pair(new RelationKey("one", "two"), MappingType.POST));


        map.add(new Pair(new RelationKey("but", "one"), MappingType.POST));
        map.add(new Pair(new RelationKey("one", "but"), MappingType.POST));


        //when
        new MapDriver<LongWritable, Text, RelationKey, MappingType>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(1l), input)

                        //then
                .withAllOutput(map)
                .runTest();
    }

}