package com.futureprocessing.wsiln.elastic;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import static com.futureprocessing.wsiln.mapreduce.ConfigurationConstants.*;

public class PopulateElasticConfig {

    private static final String S3_BUCKET = "s3Bucket";
    private static final String S3_KEY = "s3Key";

    private final OptionSet optionSet;
    private final OptionSpec<String> s3BucketOption;
    private final OptionSpec<String> s3KeyOption;
    private final OptionSpec<String> elasticIndexNameOption;
    private final OptionSpec<String> elasticHostOption;
    private final OptionSpec<Integer> elasticPortOption;

    public PopulateElasticConfig(String[] args) {
        OptionParser optionParser = new OptionParser();
        optionParser.allowsUnrecognizedOptions();
        s3BucketOption = optionParser.accepts(S3_BUCKET).withRequiredArg().ofType(String.class);
        s3KeyOption = optionParser.accepts(S3_KEY).withRequiredArg().ofType(String.class);

        elasticHostOption = optionParser.accepts(ELASTIC_HOST).withOptionalArg().ofType(String.class).defaultsTo("localhost");
        elasticPortOption = optionParser.accepts(ELASTIC_PORT).withOptionalArg().ofType(Integer.class).defaultsTo(9300);
        elasticIndexNameOption = optionParser.accepts(ELASTIC_INDEX_NAME).withOptionalArg().ofType(String.class).defaultsTo("technologies");
        OptionSpec<Integer> minTechnologiesConnections = optionParser.accepts(MIN_NUMBER_OF_TECHNOLOGIES_CONNECTIONS).withOptionalArg().ofType(Integer.class).defaultsTo(2);
        OptionSpec<Boolean> omitPostOption = optionParser.accepts(OMIT_POSTS).withOptionalArg().ofType(Boolean.class).defaultsTo(true);
        OptionSpec<Integer> mappingScope = optionParser.accepts(MAPPING_SCOPE).withOptionalArg().ofType(Integer.class).defaultsTo(5);

        optionSet = optionParser.parse(args);
    }

    public String getS3Bucket() {
        return optionSet.valueOf(s3BucketOption);
    }

    public String getS3Key() {
        return optionSet.valueOf(s3KeyOption);
    }

    public String getElasticIndexName() {
        return optionSet.valueOf(elasticIndexNameOption);
    }

    public String getElasticHost() {
        return optionSet.valueOf(elasticHostOption);
    }

    public int getElasticPort() {
        return optionSet.valueOf(elasticPortOption);
    }
}
