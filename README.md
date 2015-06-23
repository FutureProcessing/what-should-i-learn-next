What Should I Learn Next
========================

# What it is
This is a sample Proof of Concept of operating on BIG DATA using Hadoop and Elastic.

## What it does
Front-end application allows user to search for technologies that he might be interested in based on technologies that he already knows. 


## How does it work:
1.	Data dump from [StackOverflow](https://archive.org/details/stackexchange) is downloaded;
2.	Using Hadoop [the data is analyzed](big-data/map-reduce/README.md) in terms of connections between technologies;
3.	Results of the analysis is processed by Elastic (search index works like a database);
4.	Final results from Elastic are displayed in a simple webapp.


# Data
### Input:
* 35 GB xml file – StackOverflow posts.

### Output:
* 135MB file – Map of technologies and connections between them based on tags added by SO users;
* There are ca. 6 000 000 mappings;
* 32000 unique technology entries.


# DIY tutorial
## Try to use example yourself
1. Configure and run [Hadoop Single Node in Docker](big-data/hadoop-in-docker/README.md);
2. Try understand how map-reduce works [Map-reduce algorithm](big-data/map-reduce/README.md);
3. Run node.js application to visualize results [GUI application](webapp/README.md).


