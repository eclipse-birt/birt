#!/bin/sh

echo starting tag BIRT...

CVS_RSH=ssh
export CVS_RSH

#echo "================== Tagging third party ======================="
#ant -f src/fetch_org.eclipse.birt.thirdparty.xml -propertyfile cvsTag.properties > third.log

echo "================== Tagging sdk feature ======================="
ant -f src/fetch_org.eclipse.birt.sdk.xml -propertyfile cvsTag.properties > tagSDK.log

echo "================== Tagging rcp feature ======================="
ant -f src/fetch_org.eclipse.birt.report.designer.ui.rcp.xml -propertyfile cvsTag.properties > cvsRCP.log

echo "================== Tagging tests feature ======================="
ant -f src/fetch_org.eclipse.birt.tests.xml -propertyfile cvsTag.properties > cvsTest.log

echo "================== Tagging chart.viewer feature ======================="

ant -f src/fetch_org.eclipse.birt.chart.viewer.xml -propertyfile cvsTag.properties > cvsChartViewer.log

echo "================== Tagging wtp integration feature ======================="
ant -f src/fetch_org.eclipse.birt.integration.wtp.sdk.xml -propertyfile cvsTag.properties > cvsWTP.log
echo "================== Tagging rcp feature ======================="

ant -f src/fetch_org.eclipse.pde.build.container.feature.xml -propertyfile cvsTag.properties > cvsRCPFeature.log
echo "================== Tagging birt finished!! ======================="

