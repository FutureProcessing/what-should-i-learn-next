package com.futureprocessing.wsiln.mapreduce.output;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ElasticRecordWriter extends RecordWriter<Text, IntWritable> {

    private final Client client;
    private final String indexName;

    public ElasticRecordWriter(String indexName, String elasticHost, int elasticPort) {
        this.indexName = indexName;
        System.out.println("Preparing client to Elastic");
        System.out.println("Client connection to host and port: " + elasticHost + " : " + elasticPort);
        System.out.println("Index name: " + indexName);
        this.client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(elasticHost, elasticPort));
        System.out.println("Client created");
        System.out.println(client);
    }

    @Override
    public void write(Text key, IntWritable value) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(key.toString()).useDelimiter("\t");
        String t1 = scanner.next();
        String t2 = scanner.next();

        ListenableActionFuture<IndexResponse> execute = client.prepareIndex(indexName, "relations", t1 + "_" + t2)
                .setSource(jsonBuilder()
                                .startObject()
                                .field("t1", t1)
                                .field("t2", t2)
                                .field("v", value.get())
                )
                .execute();


        ListenableActionFuture<IndexResponse> execute1 = client.prepareIndex(indexName, "list", t1)
                .setSource(jsonBuilder()
                                .startObject()
                                .field("name", t1)
                )
                .execute();

        try {
            IndexResponse indexResponse = execute.get();
            IndexResponse indexResponse1 = execute1.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        client.close();
    }
}
