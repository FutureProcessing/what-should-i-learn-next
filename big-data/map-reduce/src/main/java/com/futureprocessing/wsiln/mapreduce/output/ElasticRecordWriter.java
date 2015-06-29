package com.futureprocessing.wsiln.mapreduce.output;

import com.futureprocessing.wsiln.elastic.ElasticWriter;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ElasticRecordWriter extends RecordWriter<Text, IntWritable> {


    private final ElasticWriter elasticWriter;

    public ElasticRecordWriter(String indexName, String elasticHost, int elasticPort) {
        elasticWriter = new ElasticWriter(indexName, elasticHost, elasticPort);
    }

    @Override
    public void write(Text key, IntWritable value) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(key.toString()).useDelimiter("\t");
        String t1 = scanner.next();
        String t2 = scanner.next();

        elasticWriter.write(t1, t2, value.get());

    }


    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        elasticWriter.close();
    }
}
