MapReduceDriver - integration test
==================================

## What it does
MRUnit provides the MapReduceDriver class which allows user to test Mapper and Reducer together.

## How to use it

```
MapReduceDriver<K1,V1,K2,V2,K3,V3>
```

MapReduceDriver uses [org.apache.hadoop.mrunit.mapreduce.MapReduceDriver](https://mrunit.apache.org/documentation/javadocs/0.9.0-incubating/org/apache/hadoop/mrunit/MapReduceDriver.html)  library.

User should provide:
- the input key and value that should be sent to the Mapper,
- output that is expected to be sent by the Reducer to the collector for those inputs.

### example

 ``` java

     @Test
    public void shouldMapTagsAndCountAllPopularTechnologies() throws IOException {
        //given
		// preparing input data

        Text input = new Text("<row value=\"blabla\" Tags=\"&lt;java&gt;&lt;spring&gt;&lt;spring&gt;\" title=\"Java is awesome\" />");

        //when
		// instantiation of MapReduceDriver and declaration of key-value datatypes

        new MapReduceDriver<LongWritable, Text, Text, Text, Text, IntWritable>()

		// calling TechnologiesMapper method

                .withMapper(new TechnologiesMapper())

		// passing input to the Mapper

                .withInput(new LongWritable(11), input)

		// calling TechnologiesReducer method

                .withReducer(new TechnologiesReducer())

		// assertion of expected results

                .withOutput(new Text("java\tspring"), new IntWritable(2))
                .withOutput(new Text("spring\tjava"), new IntWritable(2))
                .withOutput(new Text("spring\tspring"), new IntWritable(2))

		// calling this method will send the input to the Mapper and provide the results to the Reducer. Finally the output will be compared with the results which will be expected by the Reducer.

                .runTest();
    }

```
