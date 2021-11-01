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

[![Create Eclipse Development Environment for Eclipse BIRT](https://img.shields.io/static/v1?logo=eclipseide&label=Create%20Development%20Environment&message=Eclipse%20BIRT&style=for-the-badge&logoColor=white&labelColor=darkorange&color=gray)](https://www.eclipse.org/setups/installer/?url=https://raw.githubusercontent.com/eclipse/birt/master/build/org.eclipse.birt.releng/BIRT.setup&show=true "Click to open Eclipse-Installer Auto Launch or drag into your running installer")

## Latest snapshot repository towards 4.9.0
https://download.eclipse.org/birt/update-site/snapshot/

## Latest designer download towards 4.9.0
https://ci.eclipse.org/birt/job/birt-master/lastSuccessfulBuild/artifact/build/birt-packages/birt-report-all-in-one/target/products/
