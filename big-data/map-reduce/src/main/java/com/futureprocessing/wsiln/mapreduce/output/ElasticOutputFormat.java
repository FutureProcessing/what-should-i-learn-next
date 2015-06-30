package com.futureprocessing.wsiln.mapreduce.output;

import com.futureprocessing.wsiln.mapreduce.ConfigurationWrapper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class ElasticOutputFormat extends OutputFormat<Text, IntWritable> {

    @Override
    public RecordWriter<Text, IntWritable> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {
        ConfigurationWrapper configuration = new ConfigurationWrapper(context.getConfiguration());
        return new ElasticRecordWriter(configuration.getElasticIndexName(),
                configuration.getElasticHost(),
                configuration.getElasticPort());
    }

    @Override
    public void checkOutputSpecs(JobContext context) throws IOException, InterruptedException {

    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {
        return new FileOutputCommitter(FileOutputFormat.getOutputPath(context),
                context);
    }


}
