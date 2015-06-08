Map-reduce algorithm:
========================

Algorithm working on all Posts.xml files (270 files, 40,8 gigabytes of data). Each files contain multiple posts, each post in one row.
Attribute "Tags" contain tags data, attribute "Body" contain post text.

#Map
###Tags
Create map (where key = "first_tag-second_tag", value = T) for all tags in all posts. Map contain data with two way binding.
Remove version from tags (e.g. before: java-7, after: java), and reformat to lower case.

###Posts
Create map (where key = "first_word-second_word", value = W) for all words in all posts. where distance between words is equal or less than 5. Map contain data with two way binding.
Remove version from words, and reformat to lower case.
	
#Reduce
Create map where key = "first_technology-second_technology", value = number of connection between technologies. Add new object only if exist connection between technologies in tags.
Remove objects, where value is equal to 1.