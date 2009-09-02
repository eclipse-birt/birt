#!/bin/sh

echo starting tag datatools...

CVS_RSH=ssh
export CVS_RSH


#1.6
#ant -f src/fetch_org.eclipse.datatools.enablement-all.feature.xml -propertyfile cvsTag.properties > cvsTag.log

#1.7
ant -f src/fetch_org.eclipse.datatools.enablement.sdk.feature.xml -propertyfile cvsTag.properties > cvsTag.log



ant -f src/fetch_org.eclipse.datatools.sdk-all.feature.xml  -propertyfile cvsTag.properties >> cvsTag.log
#ant -f src/fetch_org.eclipse.datatools.sdk.feature.xml  -propertyfile cvsTag.properties >> cvsTag.log

ant -f src/fetch_org.eclipse.datatools.enablement.oda.ecore.feature.xml -propertyfile cvsTag.properties >> cvsTag.log
ant -f src/fetch_org.eclipse.datatools.enablement.oda.ecore.sdk.feature.xml -propertyfile cvsTag.properties >> cvsTag.log

