package com.futureprocessing.wsiln.mapreduce.output;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

import static com.futureprocessing.wsiln.mapreduce.ConfigurationConstants.*;

public class ElasticOutputFormat extends OutputFormat<Text, IntWritable> {

    @Override
    public RecordWriter<Text, IntWritable> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {

        Configuration configuration = context.getConfiguration();
        return new ElasticRecordWriter(configuration.get(ELASTIC_INDEX_NAME),
                configuration.get(ELASTIC_HOST),
                configuration.getInt(ELASTIC_PORT, 9300));
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
