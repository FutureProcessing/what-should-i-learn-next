How to run map-reduce job Amazon EMR
====================================

[Amazon EMR - Amazon Elastic Map Reduce](http://aws.amazon.com/elasticmapreduce/)

How to Amazon EMR
-----------------
If you want to run our job within Amazon EMR first get familiar with Amazon EMR by completing [tutorial](http://docs.aws.amazon.com/ElasticMapReduce/latest/DeveloperGuide/emr-get-started.html)

Run Map-Reduce job
------------------

### Prepare data
Upload `Posts.xml` file to S3 into `hadoop-input` bucket.

### Prepare job jar
Build map-reduce jar-with-dependencies.
Upload it to S3.

### Prepare directory structure on S3.

### Create new Cluster within Amazon EMR.
1. Add Custom Jar Step.
2. Name it as you will.
3. Select `JAR location` from your S3 bucket.
4. Pass arguments in form: <inputPath> <outputPath>
```
s3://wsiln-hadoop/input/Posts.xml s3://wsiln-hadoop/output/output02
```
Your job will be distributed on Hadoop cluster. After a while, you should see status `Completed` next to step details.
