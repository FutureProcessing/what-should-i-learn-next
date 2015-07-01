Populate Elastic usage
======================

Use this command to populate elastic index with results from hadoop stored in s3

``` bash
java -cp map-reduce-1.0-SNAPSHOT-jar-with-dependencies.jar com.futureprocessing.wsiln.elastic.PopulateElastic --s3Bucket=s3bucket --s3Key=output/output01/part --elasticHost=your-host --indexName=technologies
```

`s3Bucket` - provide s3 bucket name

`s3Key` - provide pattern of files with results. Ususaly results files are named as `part-r-00001`, `part-r-00002` etc.
So option `--s3Key=output/output01/part` matches all files begining with `output/output01/part`.

`elasticHost` - provide address of machine where elastic is running 

`indexName` - specify name of index that should be populated

