# BIRT [![Build Status](https://travis-ci.org/eclipse/birt.svg?branch=master)](https://travis-ci.org/eclipse/birt)
The open source Eclipse BIRT reporting and data visualization project. 

## Building BIRT
BIRT is built with [Apache Maven](http://maven.apache.org).

To build BIRT with the latest Eclipse platform, run:

    mvn package -DskipTests 
    
To build BIRT with Eclipse Neon, run:

    mvn package -Pneon -DskipTests

To build BIRT with Eclipse Oxygen, run:

    mvn package -Poxygen -DskipTests
    
### Building environment
* JDK 1.8
* Maven 3.5+
* toolchains.xml with Java-SE-1.8

