package com.futureprocessing.wsiln.mapreduce.output;

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

    private static Logger log = LoggerFactory.getLogger(ElasticRecordWriter.class);

    private final Client client;
    private final String indexName;
    private final Semaphore semaphore;
    private final ElasticResponseHandler elasticResponseHandler;

    public ElasticRecordWriter(String indexName, String elasticHost, int elasticPort) {
        this.indexName = indexName;
        log.info("Preparing client to Elastic");
        log.info("Client connection to host and port: " + elasticHost + " : " + elasticPort);
        log.info("Index name: " + indexName);
        this.client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(elasticHost, elasticPort));
        log.info("Client created: " + client.toString());
        this.semaphore = new Semaphore(50, true);
        this.elasticResponseHandler = new ElasticResponseHandler();
    }

    @Override
    public void write(Text key, IntWritable value) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(key.toString()).useDelimiter("\t");
        String t1 = scanner.next();
        String t2 = scanner.next();

        semaphore.acquire();
        client.prepareIndex(indexName, "relations", t1 + "_" + t2)
                .setSource(jsonBuilder()
                                .startObject()
                                .field("t1", t1)
                                .field("t2", t2)
                                .field("v", value.get())
                )
                .execute().addListener(elasticResponseHandler);


        semaphore.acquire();
        client.prepareIndex(indexName, "list", t1)
                .setSource(jsonBuilder()
                                .startObject()
                                .field("name", t1)
                )
                .execute().addListener(elasticResponseHandler);
    }

    private class ElasticResponseHandler implements ActionListener<IndexResponse> {

        public void onResponse(IndexResponse indexResponse) {
            semaphore.release();
        }

        public void onFailure(Throwable e) {
            log.error("Error happened while waiting for response from elastic", e);
            semaphore.release();
        }
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        client.close();
    }
}
