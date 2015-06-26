package com.futureprocessing.wsiln.elastic;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ElasticWriter {

    private static Logger log = LoggerFactory.getLogger(ElasticWriter.class);

    private final Client client;
    private final String indexName;
    private final Semaphore semaphore;
    private final ElasticResponseHandler elasticResponseHandler;

    public ElasticWriter(String indexName, String elasticHost, int elasticPort) {
        this.indexName = indexName;
        log.info("Preparing client to Elastic");
        log.info("Client connection to host and port: " + elasticHost + " : " + elasticPort);
        log.info("Index name: " + indexName);
        this.client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(elasticHost, elasticPort));
        log.info("Client created: " + client.toString());
        this.semaphore = new Semaphore(50, true);
        this.elasticResponseHandler = new ElasticResponseHandler();
    }


    public void write(String t1, String t2, Integer value) throws InterruptedException, IOException {

        final String elasticKey = t1 + "_" + t2;

        semaphore.acquire();
        client.prepareIndex(indexName, "relations", elasticKey + "1")
                .setSource(jsonBuilder()
                                .startObject()
                                .field("t1", t1)
                                .field("t2", t2)
                                .field("plus", true)
                                .field("v", value)
                )
                .execute().addListener(elasticResponseHandler);

        semaphore.acquire();
        client.prepareIndex(indexName, "relations", elasticKey + "0")
                .setSource(jsonBuilder()
                                .startObject()
                                .field("t1", t1)
                                .field("t2", t2)
                                .field("plus", false)
                                .field("v", 0 - value)
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

    public void close() {
        client.close();
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


}


