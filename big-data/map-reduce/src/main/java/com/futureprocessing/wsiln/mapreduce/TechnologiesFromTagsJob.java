package com.futureprocessing.wsiln.mapreduce;

import com.futureprocessing.wsiln.mapreduce.map.MappingType;
import com.futureprocessing.wsiln.mapreduce.map.RelationKey;
import com.futureprocessing.wsiln.mapreduce.output.ElasticOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechnologiesFromTagsJob extends Configured implements Tool {

    private static Logger log = LoggerFactory.getLogger(TechnologiesFromTagsJob.class);

    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(getConf());
        job.setJarByClass(getClass());
        job.setJobName(getClass().getSimpleName());

        ConfigurationWrapper configuration = parseArguments(args, job.getConfiguration());

        FileInputFormat.addInputPath(job, new Path(configuration.getInputPath()));

        job.setMapperClass(TechnologiesMapper.class);
        job.setMapOutputKeyClass(RelationKey.class);
        job.setMapOutputValueClass(MappingType.class);

        job.setReducerClass(TechnologiesReducer.class);

        setUpOutput(job, configuration);

        logConfiguration(configuration);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    private void setUpOutput(Job job, ConfigurationWrapper configuration) {
        job.setOutputKeyClass(RelationKey.class);
        job.setOutputValueClass(IntWritable.class);
        if (configuration.isUseElastic()) {
            job.setOutputFormatClass(ElasticOutputFormat.class);
        } else {
            FileOutputFormat.setOutputPath(job, new Path(configuration.getOutputPath()));
        }
    }

    private void logConfiguration(ConfigurationWrapper configuration) {
        log.info("Starting job with following configuration: ()", configuration);
    }

    private ConfigurationWrapper parseArguments(String[] args, Configuration configuration) {
        ConfigurationWrapper configurationWrapper = new ConfigurationWrapper(configuration);
        configurationWrapper.parseArguments(args);

        return configurationWrapper;
    }

    public static void main(String[] args) throws Exception {
        int rc = ToolRunner.run(new TechnologiesFromTagsJob(), args);
        System.exit(rc);
    }
}
