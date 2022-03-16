# Eclipse BIRT
The open source Eclipse BIRT reporting and data visualization project. 

## Website
https://eclipse.org/birt

[![Build Status](https://github.com/eclipse/birt/workflows/CI/badge.svg)](https://github.com/eclipse/birt/actions)

## Building BIRT
BIRT is built with [Apache Maven](http://maven.apache.org) through [Tycho](https://github.com/eclipse/tycho).

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




## Create an Eclipse Development Environment
* [![yt](https://user-images.githubusercontent.com/180969/143874274-9221c016-846b-4e60-8e06-7f90cb72fc8f.png)](https://www.youtube.com/watch?v=FqfrG2I0AIw)

* [![Create Eclipse Development Environment for Eclipse BIRT](https://download.eclipse.org/oomph/www/setups/svg/birt.svg)](https://www.eclipse.org/setups/installer/?url=https://raw.githubusercontent.com/eclipse/birt/master/build/org.eclipse.birt.releng/BIRTConfiguration.setup&show=true "Click to open Eclipse-Installer Auto Launch or drag into your running installer")

## Current version 4.8.0
https://download.eclipse.org/birt/downloads/drops/R-R1-4.8.0-201806261756/

## Latest snapshot repository towards 4.9.0
https://download.eclipse.org/birt/update-site/snapshot/

## Latest downloads towards 4.9.0
https://download.eclipse.org/birt/downloads/drops/snapshot/

