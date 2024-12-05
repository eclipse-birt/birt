# Eclipse BIRT [![Build Status](https://github.com/eclipse-birt/birt/workflows/CI/badge.svg)](https://github.com/eclipse-birt/birt/actions)
The open source Eclipse BIRT reporting and data visualization project. 

## Website
https://eclipse.org/birt  

## Create a BIRT Development Environment
* [![yt](https://user-images.githubusercontent.com/180969/143874274-9221c016-846b-4e60-8e06-7f90cb72fc8f.png)](https://www.youtube.com/watch?v=FqfrG2I0AIw)

* [![Create Eclipse Development Environment for Eclipse BIRT](https://download.eclipse.org/oomph/www/setups/svg/birt.svg)](https://www.eclipse.org/setups/installer/?url=https://raw.githubusercontent.com/eclipse/birt/master/build/org.eclipse.birt.releng/BIRTConfiguration.setup&show=true "Click to open Eclipse-Installer Auto Launch or drag into your running installer")

## Building BIRT
BIRT is built with [Apache Maven](http://maven.apache.org) through [Tycho](https://github.com/eclipse/tycho) on [Eclipse CI](https://ci.eclipse.org/birt).

To build BIRT with the latest Eclipse platform, run:

    mvn clean verify -DskipTests=true
    
### Building environment
* JDK 17
* Maven 3.9.0

## Eclipse Version
If you want to install BIRT as a plug-in, please be aware that BIRT 4.14 requires Eclipse 4.30 or higher, and Java 17 or higher.

## Latest Version 4.18.0
* https://download.eclipse.org/birt/updates/release/latest

## Current Version 4.18.0
* https://download.eclipse.org/birt/updates/release/4.18.0

## Latest Snapshots towards 4.19.0
* https://download.eclipse.org/birt/updates/nightly/latest

