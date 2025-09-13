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
* JDK 21
* Maven 3.9.11
* [https://ci.eclipse.org/birt/job/build](https://ci.eclipse.org/birt/job/build/)

## Latest Version 4.21.0

* https://download.eclipse.org/birt/updates/release/latest
* https://download.eclipse.org/birt/updates/release/4.21.0

## Latest Snapshots towards 4.22.0

* https://download.eclipse.org/birt/updates/nightly/latest

## Runtime Requirements

The current version of BIRT 4.21 is runnable with JDK:
* JDK 21 (LTS)

Tomcat version support:

* Tomcat 9.0.7x, 9.0.8x, 9.0.9x, 9.0.10x (incl. 9.0.109)
  - BIRT 4.21.0 requires Tomcat to be launched with JVM argument `-add-opens=java.base/java.net=ALL-UNNAMED`.
    - This can be accomplished by setting the following environment variable before starting Tomcat.
      - `CATALINA_OPTS="-add-opens=java.base/java.net=ALL-UNNAMED"`
    - See [issue 2271](https://github.com/eclipse-birt/birt/issues/2271). 
    - See [discussion 2278](https://github.com/eclipse-birt/birt/discussions/2278). 
* Tomcat 10 & 11 is not supported due to Jakarta EE dependencies
