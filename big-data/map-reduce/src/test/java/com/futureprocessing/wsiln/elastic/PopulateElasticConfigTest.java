package com.futureprocessing.wsiln.elastic;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PopulateElasticConfigTest {
    @Test
    public void shouldParseArguments(){
        //given
        String argLine = "--s3Bucket=some-bucket-name --s3Key=key-name --elasticHost=elastic-host --elasticPort=123456 --indexName=fairytale";
        String[] args = argLine.split(" ");

        //when
        PopulateElasticConfig config = new PopulateElasticConfig(args);

        //then
        assertThat(config.getS3Bucket()).isEqualTo("some-bucket-name");
        assertThat(config.getS3Key()).isEqualTo("key-name");
        assertThat(config.getElasticHost()).isEqualTo("elastic-host");
        assertThat(config.getElasticPort()).isEqualTo(123456);
        assertThat(config.getElasticIndexName()).isEqualTo("fairytale");
    }


}
