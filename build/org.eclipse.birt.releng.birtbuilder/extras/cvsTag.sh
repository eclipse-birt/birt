#!/bin/sh

echo starting tag BIRT...

CVS_RSH=ssh
export CVS_RSH
export SrcDir=/home/adb/farrah/cvsTag/src

##-DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true

echo "================== Tagging third party =======================" > third.log
#ant -f $SrcDir/fetch_org.eclipse.birt.thirdparty.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeaturesRecursively=true>> third.log

echo "================== Tagging sdk feature =======================" > tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.sdk.xml -propertyfile cvsTag.properties -DfeatureOnly=true >> tagSDK.log

echo "================== Tagging birt feature =======================" >> tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagSDK.log

echo "================== Tagging example feature =======================" >> tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.example.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagSDK.log

echo "================== Tagging chart.cshelp feature =======================" >> tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.chart.cshelp.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagSDK.log
echo "================== Tagging birt.cshelp feature =======================" >> tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.cshelp.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagSDK.log
echo "================== Tagging birt.doc feature =======================" >> tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.doc.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagSDK.log
echo "================== Tagging birt.doc.isv feature =======================" >> tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.doc.isv.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagSDK.log
echo "================== Tagging birt.chart.doc feature =======================" >> tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.chart.doc.isv.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagSDK.log
echo "================== Tagging birt.chart feature =======================" >> tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.chart.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagSDK.log
echo "================== Tagging birt.designer feature ONLY =======================" >> tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.report.designer.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeaturesRecursively=true >> tagSDK.log
echo "================== Tagging chart.runtime feature ONLY =======================" >> tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.chart.runtime.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeaturesRecursively=true >> tagSDK.log
echo "================== Tagging org.eclipse.birt.report.designer.debug feature ONLY =======================" >> tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.report.designer.debug.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeaturesRecursively=true >> tagSDK.log
echo "================== Tagging org.eclipse.birt.crosstab feature ONLY =======================" >> tagSDK.log
ant -f $SrcDir/fetch_org.eclipse.birt.crosstab.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeaturesRecursively=true >> tagSDK.log
echo "================== Tagging OSGI feature ONLY =======================" > tagOSGI.log
ant -f $SrcDir/fetch_org.eclipse.birt.osgi.runtime.sdk.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeaturesRecursively=true >> tagOSGI.log



echo "================== Tagging tests feature =======================" > tagTest.log
#ant -f $SrcDir/fetch_org.eclipse.birt.tests.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagTest.log

echo "================== Tagging chart.viewer feature =======================" >tagChartViewer.log
#ant -f $SrcDir/fetch_org.eclipse.birt.chart.viewer.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagChartViewer.log

echo "================== Tagging wtp integration feature =======================" > tagWTP.log
#ant -f $SrcDir/fetch_org.eclipse.birt.integration.wtp.sdk.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagWTP.log

echo "================== Tagging designer.rcp =======================" > tagRCP.log
#ant -f $SrcDir/fetch_org.eclipse.birt.report.designer.ui.rcp.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagRCP.log

echo "================== Tagging rcp feature =======================">> tagRCP.log
#ant -f $SrcDir/fetch_org.eclipse.pde.build.container.feature.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagRCP.log

echo "================== Tagging nl feature ======================="> tagNL.log
#ant -f $SrcDir/fetch_org.eclipse.birt.nl.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> tagNL.log

echo "================== Tagging birt finished!! ======================="



