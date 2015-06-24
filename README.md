What Should I Learn Next
========================

# What it is
This is a sample Proof of Concept for BIG DATA analysis using [Hadoop](https://hadoop.apache.org/) and [Elastic](https://www.elastic.co/).

## What it does
The application allows user to browse through technologies that he might be interested in based on technologies that he already knows.


## How does it work
When user enters some technology he already knows, an application searches through its database (elastic) for technologies related to the given one. This relationship is mainly determined by how often those technologies are mentioned together on StackOverflow. User can further expand list of technologies he knows and likes or dislikes and the application updates results accordingly.

 The whole process of preparing data and running the application looks like:
1.	Data dump from [StackOverflow](https://archive.org/details/stackexchange) is downloaded;
2.	[The data is analyzed](big-data/map-reduce/README.md) with Hadoop. During the analysis connections between terms related to technologies are found;
3. Results of the analysis are processed by Elastic;
4. Final results from Elastic are presented in the application. You can search for a particular technology and we’ll list others, closely related to it.
5. Then you can select which technologies you’d enjoy learning, which you already know and which you would not be interested in at all.


# Data
### Input
* 35 GB XML dump file – StackOverflow posts.

### Output
* 135MB file – Map of technologies and connections between them based on tags added by StackOverflow users;
* There are ca. 6 000 000 mappings;
* 32000 unique technology entries.


# DIY tutorial
## Try to use example yourself
1. Configure and run [Hadoop Single Node in Docker](big-data/hadoop-in-docker/README.md);
2. Try to understand how our map-reduce algorithm works: [Map-reduce algorithm](big-data/map-reduce/README.md);
3. Run node.js application to visualize results and play with it: [GUI application](webapp/README.md).


