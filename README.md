# BIRT
The open source Eclipse BIRT reporting and data visualization project. 


##Building BIRT
BIRT is build using [Apache Maven] (http://maven.apache.org).
To build BIRT with latest Eclipse platform, run:

    mvn package -DskipTests 
    
To build BIRT with Eclipse Neon, run:

    mvn package -Pneon -DskipTests

To build BIRT with Eclipse Mars, run:

    mvn package -Pmars -DskipTests
