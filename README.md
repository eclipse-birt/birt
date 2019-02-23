# Eclipse BIRT [![Build Status](https://travis-ci.org/Flugtiger/birt.svg?branch=flugtiger_master)](https://travis-ci.org/Flugtiger/birt)
The open source Eclipse BIRT reporting and data visualization project.
This is not the official Eclipse repository, nevertheless feel free to open tickets and pull requests!

## Building BIRT
BIRT is built with [Apache Maven](http://maven.apache.org).

To build BIRT with the latest Eclipse platform (currently still Photon), run:

    mvn package -DskipTests 
    
To build BIRT with Eclipse Neon, run:

    mvn package -Pneon -DskipTests

To build BIRT with Eclipse Oxygen, run:

    mvn package -Poxygen -DskipTests
    
### Building environment
* JDK 1.8
* Maven 3.5+
* toolchains.xml with Java-SE-1.8 (located in your .m2 folder, e.g. ~/.m2/toolchains.xml), see http://maven.apache.org/plugins/maven-toolchains-plugin/toolchains/jdk.html