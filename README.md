# The WikiAnnotationCollector 
(a.k.a. XMLParserSAXStyle)

#Intorduction:

This little tool collects annotations from given enwiki.xml file 
and store only the url markdown annotations without square brackets "[[...]]" 
and the corresponding (generated) url's of these annotations. 
These results gonna get saved inside a new created xml file.

#Example:

enwiki.xml 
-> from text node we get [[Steve Jobs]] 
-> the collector gather "Steve Jobs" 
-> and generate https://en.wikipedia.org/wiki/Steve_Jobs

#How to start:

The tool will be started on the command line using the main method of XMLParserSAXStyle.class 
and also a enwiki.xml filename as argument. (The file and the tool need to be in the same folder.)
The result xml appears also in the tool folder. Have some fun with it. :)

Greetings from ReleasedBrainiac
