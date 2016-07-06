# The WikiAnnotationCollector 

#Intorduction:

This little tool collects text sentenes with annotations from given enwiki.xml file 
and store them twich. The 1st file you get is a full collection of the sentences with annotations, there annotation words and the corresponding url matkdown. (2 types of xml files different at the name tag "sentence-")

#Example: (For full Annotation)

enwiki.xml 
-> from text node we get [[Steve Jobs]]   (This is stored also as solo information for each kind of this sentence in the text 2nd xml)
-> the collector gather "Steve Jobs" 
-> and generate https://en.wikipedia.org/wiki/Steve_Jobs

#How to start:

The Programm has 2 primar parts. The 1st is the Datacollector (ExecuteParsingDatacollection.java) and the 2nd ist the Datsetcreator (ExecuteParsingFullDataset.java). If want to execute both in a one step pipeline there is a class named "Execute.java".
You need to setup somevariables about paths and the report update in the code, please keep that in mind. The single use is similiar but you need to use the spezific classes from above. (There are som example main() methods.)

Greetings 
ReleasedBrainiac
