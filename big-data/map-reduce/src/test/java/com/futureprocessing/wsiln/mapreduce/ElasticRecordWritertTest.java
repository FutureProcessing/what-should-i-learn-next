package com.futureprocessing.wsiln.mapreduce;

import com.futureprocessing.wsiln.mapreduce.output.ElasticRecordWriter;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.junit.Test;

import java.io.IOException;

public class ElasticRecordWritertTest {

    @Test
//    @Ignore
    public void shouldPutRecordIntElastic() throws IOException, InterruptedException {
        //given
        ElasticRecordWriter elasticRecordWriter = new ElasticRecordWriter("testindex", "fp-pc938", 9300);

        //when
        elasticRecordWriter.write(new Text("java\tspring"), new IntWritable(20));

        //then
        elasticRecordWriter.close(null);
    }
}
