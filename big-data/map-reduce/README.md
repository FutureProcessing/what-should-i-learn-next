Map-reduce algorithm:
========================

Algorithm working on stackoverflow.com-Posts/Posts.xml file (32,2 gigabytes of data). This file contains all posts from stackoverflow, each post in one row.
Attribute "Tags" contains tags data, attribute "Body" contains post text.

# Map
### Tags
Create map (where key = "first_tag-second_tag", value = T) for all tags in all posts. Map contains data with two way binding.
Remove version from tags (e.g. before: java-7, after: java), and reformat to lower case.

### Posts body
Create map (where key = "first_word-second_word", value = W) for all words in all posts,
where distance between words is equal or less than specified scope. By default it's 5.
Map contains data with two way binding.
Remove version from words, and reformat to lower case.

This step is disabled by default. Add `--includePostsBody` to include it. 
	
# Reduce
Create map where key = "first_technology-second_technology", value = number of connection between technologies. Add new object only if exist connection between technologies in tags.
Remove objects, where value is equal to 1.