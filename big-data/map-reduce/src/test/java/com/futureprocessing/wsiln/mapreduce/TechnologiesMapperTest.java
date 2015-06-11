package com.futureprocessing.wsiln.mapreduce;


import com.futureprocessing.wsiln.mapreduce.map.MappingType;
import com.futureprocessing.wsiln.mapreduce.map.MappingTypeWrapper;
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
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class TechnologiesMapperTest {

    @Test
    public void shouldMapTagsFromRow() throws IOException {
        //given
        Text input = new Text("<row value=\"blabla\" Tags=\"&lt;java&gt;&lt;spring&gt;\" title=\"Java is awesome\" />");
        List<Pair<RelationKey, MappingTypeWrapper>> map = new ArrayList<Pair<RelationKey, MappingTypeWrapper>>();
        map.add(new Pair(new RelationKey("java", "spring"), new MappingTypeWrapper(MappingType.TAG)));
        map.add(new Pair(new RelationKey("spring", "java"), new MappingTypeWrapper(MappingType.TAG)));
        //when
        new MapDriver<LongWritable, Text, RelationKey, MappingTypeWrapper>()
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

        List<Pair<RelationKey, MappingTypeWrapper>> map = new ArrayList<Pair<RelationKey, MappingTypeWrapper>>();

        map.add(new Pair(new RelationKey("one", "plus"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("plus", "one"), new MappingTypeWrapper(MappingType.POST)));

        map.add(new Pair(new RelationKey("one", "is"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("is", "one"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("one", "two"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("two", "one"), new MappingTypeWrapper(MappingType.POST)));

        map.add(new Pair(new RelationKey("one", "but"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("but", "one"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("plus", "one"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("one", "plus"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("plus", "is"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("is", "plus"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("plus", "two"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("two", "plus"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("plus", "but"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("but", "plus"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("plus", "one"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("one", "plus"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("one", "is"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("is", "one"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("one", "two"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("two", "one"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("one", "but"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("but", "one"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("is", "two"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("two", "is"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("is", "but"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("but", "is"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("is", "one"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("one", "is"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("two", "but"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("but", "two"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("two", "one"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("one", "two"), new MappingTypeWrapper(MappingType.POST)));


        map.add(new Pair(new RelationKey("but", "one"), new MappingTypeWrapper(MappingType.POST)));
        map.add(new Pair(new RelationKey("one", "but"), new MappingTypeWrapper(MappingType.POST)));


        //when
        new MapDriver<LongWritable, Text, RelationKey, MappingTypeWrapper>()
                .withMapper(new TechnologiesMapper())
                .withInput(new LongWritable(1l), input)

                        //then
                .withAllOutput(map)
                .runTest();
    }

}