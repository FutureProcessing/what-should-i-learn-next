Populate Elastic usage
======================
``` bash
java -cp map-reduce-1.0-SNAPSHOT-jar-with-dependencies.jar com.futureprocessing.wsiln.elastic.PopulateElastic --s3Bucket=wsiln-hadoop --s3Key=output/output01/part --elasticHost=vm-wsiln-01 --indexName=technologies
```
