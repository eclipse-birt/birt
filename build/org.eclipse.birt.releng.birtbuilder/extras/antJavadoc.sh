#!/bin/sh

#Generate the classpath for antJavadoc.xml
#Arguments: $1: where ecilpse locates
#           $2: where classpath.properties be exported


eclipseDir=$1

classpath=`find $eclipseDir/plugins -name "*.jar" -printf "%p:"`; 

echo "classpath=" $classpath > $2/classpath.properties

