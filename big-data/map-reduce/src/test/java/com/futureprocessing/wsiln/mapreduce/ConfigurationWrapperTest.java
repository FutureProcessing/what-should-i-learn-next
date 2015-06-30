package com.futureprocessing.wsiln.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationWrapperTest {

    private ConfigurationWrapper configurationWrapper;

    @Before
    public void prepareWrapper() {
        configurationWrapper = new ConfigurationWrapper(new Configuration());
    }

    @Test
    public void shouldGetInputPath() {
        //given
        String[] args = toArgs("s3://some/file/on/S3");

        //when
        configurationWrapper.parseArguments(args);

        //then
        assertThat(configurationWrapper.getInputPath()).isEqualTo("s3://some/file/on/S3");
    }

    private String[] toArgs(String s) {
        return s.split(" ");
    }

    @Test
    public void shouldGetElasticHost() {
        //given
        String[] args = toArgs("s3://some/file/on/S3 --elasticHost=some-host");

        //when
        configurationWrapper.parseArguments(args);

        //then
        assertThat(configurationWrapper.getElasticHost()).isEqualTo("some-host");
    }

    @Test
    public void shouldGetDefaultElasticPort() {
        //given
        String[] args = toArgs("s3://some/file/on/S3 --elasticHost=some-host");

        //when
        configurationWrapper.parseArguments(args);

        //then
        assertThat(configurationWrapper.getElasticPort()).isEqualTo(9300);
    }

    @Test
    public void shouldGetCustomElasticPort() {
        //given
        String[] args = toArgs("s3://some/file/on/S3 --elasticHost=some-host --elasticPort=6666");

        //when
        configurationWrapper.parseArguments(args);

        //then
        assertThat(configurationWrapper.getElasticPort()).isEqualTo(6666);
    }


    @Test
    public void shouldNotUseElasticWhenNoElasticHostOptionProvided() {
        //given
        String[] args = toArgs("s3://some/file/on/S3");

        //when
        configurationWrapper.parseArguments(args);

        //then
        assertThat(configurationWrapper.isUseElastic()).isFalse();
    }

    @Test
    public void shouldUseElasticWhenElasticHostOptionProvided() {
        //given
        String[] args = toArgs("s3://some/file/on/S3 --elasticHost");

        //when
        configurationWrapper.parseArguments(args);

        //then
        assertThat(configurationWrapper.isUseElastic()).isTrue();
    }

    @Test
    public void shouldDefaultToLocalhostWhenNoArgumentOnElasticHost() {
        //given
        String[] args = toArgs("s3://some/file/on/S3 --elasticHost");

        //when
        configurationWrapper.parseArguments(args);

        //then
        assertThat(configurationWrapper.getElasticHost()).isEqualTo("localhost");
    }

    @Test
    public void shouldDefaultToTwoMinimumConncectionBetweenTechnologies() {
        //given
        String[] args = toArgs("s3://some/file/on/S3 --elasticHost");

        //when
        configurationWrapper.parseArguments(args);

        //then
        assertThat(configurationWrapper.getMinimumNumberOfTechnologiesConnections()).isEqualTo(2);
    }


    @Test
    public void shouldNotUsePostsBodyInMapperWhenNoIncludePostsBodyOptionProvided() {
        //given
        String[] args = toArgs("s3://some/file/on/S3");

        //when
        configurationWrapper.parseArguments(args);

        //then
        assertThat(configurationWrapper.isIncludePostsBody()).isFalse();
    }

    @Test
    public void shouldUsePostsBodyInMapperWhenIncludePostsBodyOptionProvided() {
        //given
        String[] args = toArgs("s3://some/file/on/S3 --includePostsBody");

        //when
        configurationWrapper.parseArguments(args);

        //then
        assertThat(configurationWrapper.isIncludePostsBody()).isTrue();
    }


    @Test
    public void shouldDefaultMappingScopeTo5() {
        //given
        String[] args = toArgs("s3://some/file/on/S3");

        //when
        configurationWrapper.parseArguments(args);

        //then
        assertThat(configurationWrapper.getMappingScope()).isEqualTo(5);
    }

    @Test
    public void shouldSetMappingScopeToFour() {
        //given
        String[] args = toArgs("s3://some/file/on/S3 --mappingScope=4");

        //when
        configurationWrapper.parseArguments(args);

        //then
        assertThat(configurationWrapper.getMappingScope()).isEqualTo(4);
    }

}
