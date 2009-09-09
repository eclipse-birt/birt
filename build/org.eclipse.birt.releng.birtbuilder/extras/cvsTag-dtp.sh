#!/bin/sh

echo starting tag datatools...

CVS_RSH=ssh
export CVS_RSH
export SrcDir=/home/adb/farrah/cvsTag/src

#1.6
#ant -f $SrcDir/fetch_org.eclipse.datatools.enablement-all.feature.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true > cvsTag.log

#1.7
ant -f $SrcDir/fetch_org.eclipse.datatools.enablement.sdk.feature.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true > cvsTag.log



ant -f $SrcDir/fetch_org.eclipse.datatools.sdk-all.feature.xml  -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> cvsTag.log

ant -f $SrcDir/fetch_org.eclipse.datatools.enablement.oda.ecore.feature.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> cvsTag.log
ant -f $SrcDir/fetch_org.eclipse.datatools.enablement.oda.ecore.sdk.feature.xml -propertyfile cvsTag.properties -DfeatureOnly=true -DfeatureAndPlugins=true -DfeaturesRecursively=true >> cvsTag.log

