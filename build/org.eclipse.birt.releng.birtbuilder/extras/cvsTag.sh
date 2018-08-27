#!/bin/sh

echo starting tag BIRT...

CVS_RSH=ssh
export CVS_RSH
export SrcDir=/home/adb/farrah/cvsTag/src

##-DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true

echo "================== Tagging sdk feature =======================" > tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.sdk.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagSDK.log

echo "================== Tagging OSGI feature ONLY =======================" > tagOSGI.log
ant -f $SrcDir/fetch_org.eclipse.birt.osgi.runtime.sdk.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeaturesRecursively=true >> tagOSGI.log

echo "================== Tagging tests feature =======================" > tagTest.log
ant -f $SrcDir/fetch_org.eclipse.birt.tests.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagTest.log

echo "================== Tagging wtp integration feature =======================" > tagWTP.log
ant -f $SrcDir/fetch_org.eclipse.birt.integration.wtp.sdk.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagWTP.log

echo "================== Tagging designer.rcp =======================" > tagRCP.log
ant -f $SrcDir/fetch_org.eclipse.birt.report.designer.ui.rcp.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagRCP.log

echo "================== Tagging nl feature ======================="> tagNL.log
ant -f $SrcDir/fetch_org.eclipse.birt.nl.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagNL.log

echo "================== Tagging rcp nl feature ======================="> tagNL.log
ant -f $SrcDir/fetch_org.eclipse.birt.rcp.nl.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagNL.log

echo "================== Tagging wtp nl feature ======================="> tagNL.log
ant -f $SrcDir/fetch_org.eclipse.birt.wtp.nl.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagNL.log

echo "================== Tagging rcp feature =======================">> tagRCP.log
ant -f $SrcDir/fetch_org.eclipse.pde.build.container.feature.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagRCP.log

echo "================== Tagging birt finished!! ======================="



