#!/bin/sh

echo starting tag BIRT...

CVS_RSH=ssh
export CVS_RSH

echo "================== Tagging third party =======================" > cvsTag.log
ant -f src/fetch_org.apache.derby.core.xml -propertyfile cvsTag.properties >> cvsTag.log
ant -f src/fetch_com.lowagie.itext.xml -propertyfile cvsTag.properties >> cvsTag.log
ant -f src/fetch_org.apache.commons.codec.xml -propertyfile cvsTag.properties >> cvsTag.log
ant -f src/fetch_org.mozilla.rhino.xml -propertyfile cvsTag.properties >> cvsTag.log
ant -f src/fetch_org.w3c.sac.xml -propertyfile cvsTag.properties >> cvsTag.log

echo "================== Tagging sdk feature =======================" >> cvsTag.log
ant -f src/fetch_org.eclipse.birt.sdk.xml -propertyfile cvsTag.properties >> cvsTag.log

echo "================== Tagging rcp feature =======================" >> cvsTag.log
ant -f src/fetch_org.eclipse.birt.rcp.xml -propertyfile cvsTag.properties >> cvsTag.log

echo "================== Tagging tests feature =======================" >> cvsTag.log
ant -f src/fetch_org.eclipse.birt.tests.xml -propertyfile cvsTag.properties >> cvsTag.log

echo "================== Tagging birt finished!! =======================" >> cvsTag.log