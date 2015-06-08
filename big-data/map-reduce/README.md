Map-reduce algorithm:
========================

#Map
##Tags
Create map (where key = "first_tag-second_tag", value = T) for all tags in all posts.
Remove version from tags (e.g. before: java-7, after: java)

##Posts
Create map (where key = "first_word-second_word", value = W) for all words in all posts. where distance between words is equal or less than 5.
Remove version from words.
	
#Reduce
Create map where key = "first_technology-second_technology", value = number of connection between technologies. Add new object only if exist connection between technologies in tags.
Remove objects, where value is equal to 1.