# Eclipse BIRT
The open source Eclipse BIRT reporting and data visualization project. 

## Website
https://eclipse.org/birt

[![Build Status](https://github.com/eclipse/birt/workflows/CI/badge.svg)](https://github.com/eclipse/birt/actions)

## Building BIRT
BIRT is built with [Apache Maven](http://maven.apache.org).

To build BIRT with the latest Eclipse platform, run:

    mvn package -DskipTests 
    
To build BIRT with Eclipse Neon, run:

    mvn package -Pneon -DskipTests

To build BIRT with Eclipse Oxygen, run:

    mvn package -Poxygen -DskipTests
    
### Building environment
* JDK 11
* Maven 3.6.3
* Internet access

## Latest snapshot repository towards 4.9.0
https://download.eclipse.org/birt/update-site/snapshot/

## Latest designer download towards 4.9.0
https://ci.eclipse.org/birt/job/birt-master/lastSuccessfulBuild/artifact/build/birt-packages/birt-report-all-in-one/target/products/
