package com.futureprocessing.wsiln.elastic;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class PopulateElastic {

    private final PopulateElasticConfig config;

    public PopulateElastic(PopulateElasticConfig config) {
        this.config = config;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        PopulateElasticConfig populateElasticConfig = new PopulateElasticConfig(args);

        PopulateElastic populateElastic = new PopulateElastic(populateElasticConfig);
        populateElastic.start();
    }

    private void start() throws IOException, InterruptedException {
        AmazonS3Client s3Client = new AmazonS3Client();

        S3Objects s3Objects = S3Objects.withPrefix(s3Client, config.getS3Bucket(), config.getS3Key());

        final ElasticWriter elasticWriter = new ElasticWriter(config.getElasticIndexName(), config.getElasticHost(), config.getElasticPort());

        for (S3ObjectSummary s3ObjectSummary : s3Objects) {
            s3ObjectSummary.getKey();

            S3Object object = s3Client.getObject(s3ObjectSummary.getBucketName(), s3ObjectSummary.getKey());
            S3ObjectInputStream objectContentStream = object.getObjectContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(objectContentStream));

            String line = null;
            while ((line = reader.readLine()) != null) {
                Scanner scanner = new Scanner(line).useDelimiter("\t");

                String t1 = scanner.next();
                String t2 = scanner.next();
                Integer value = scanner.nextInt();

                elasticWriter.write(t1, t2, value);
            }
        }
    }
}
