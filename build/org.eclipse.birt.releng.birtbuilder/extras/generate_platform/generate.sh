DISPLAY=:0.0
export DISPLAY

#==================== COMMON SET UP ================================

export SDK_LOCATION=/home/adb/farrah/director/sdk
export TARGET_LOCATION=/home/adb/farrah/director/target
export REPO_LOCATION=/home/adb/farrah/director
export ECLIPSE_RUNNER=$SDK_LOCATION/eclipse/eclipse

usage="usage: $0 [win|linux]"

case "$1" in
  win)
       export PACKAGE_TYPE=win
       export P2_OS=win32
       export P2_WS=win32
       export P2_ARCH=x86;;
  linux)
       export PACKAGE_TYPE=linux
       export P2_OS=linux
       export P2_WS=gtk
       export P2_ARCH=x86;;
  *)
       echo >&2 "$usage"
       exit 1;;
esac

#export Profile=PlatformProfile
export Profile=SDKProfile

#export ECLIPSE_CVS_URL=http://download.eclipse.org/eclipse/updates/3.7milestones/S-3.7RC2-201105191138/
#export ECLIPSE_JDT_URL=http://download.eclipse.org/eclipse/updates/3.7milestones/S-3.7RC2-201105191138/
#export ECLIPSE_PDE_URL=http://download.eclipse.org/eclipse/updates/3.7milestones/S-3.7RC2-201105191138/
#export EMF_XSD_URL=http://download.eclipse.org/modeling/emf/emf/updates/2.7milestones/
#export GEF_URL=http://download.eclipse.org/tools/gef/updates/milestones/

export ECLIPSE_JDT_URL=jar:file://$REPO_LOCATION/org.eclipse.jdt-4.4M6.zip!/
export ECLIPSE_PDE_URL=jar:file://$REPO_LOCATION/org.eclipse.pde-4.4M6.zip!/
export EMF_XSD_URL=jar:file://$REPO_LOCATION/emf-xsd-Update-2.10.0M6.zip!/
export GEF_URL=jar:file://$REPO_LOCATION/GEF-Update-3.9.2M6.zip!/

#==================== COMMON SET UP ================================

$ECLIPSE_RUNNER \
 -application org.eclipse.equinox.p2.director \
 -metadataRepository $ECLIPSE_CVS_URL \
 -artifactRepository $ECLIPSE_CVS_URL \
 -destination $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -profile $Profile \
 -bundlepool $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -p2.os  $P2_OS \
 -p2.ws  $P2_WS \
 -p2.arch $P2_ARCH \
 -roaming -vmargs \
 -Declipse.p2.data.area=$TARGET_LOCATION/$PACKAGE_TYPE/eclipse/p2

$ECLIPSE_RUNNER \
 -application org.eclipse.equinox.p2.director \
 -metadataRepository $ECLIPSE_JDT_URL \
 -artifactRepository $ECLIPSE_JDT_URL \
 -installIU org.eclipse.jdt.feature.group \
 -destination $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -profile $Profile \
 -bundlepool $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -p2.os  $P2_OS \
 -p2.ws  $P2_WS \
 -p2.arch $P2_ARCH \
 -roaming -vmargs \
 -Declipse.p2.data.area=$TARGET_LOCATION/$PACKAGE_TYPE/eclipse/p2

$ECLIPSE_RUNNER \
 -application org.eclipse.equinox.p2.director \
 -metadataRepository $ECLIPSE_PDE_URL \
 -artifactRepository $ECLIPSE_PDE_URL \
 -installIU org.eclipse.pde.feature.group \
 -destination $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -profile $Profile \
 -bundlepool $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -p2.os  $P2_OS \
 -p2.ws  $P2_WS \
 -p2.arch $P2_ARCH \
 -roaming -vmargs \
 -Declipse.p2.data.area=$TARGET_LOCATION/$PACKAGE_TYPE/eclipse/p2

$ECLIPSE_RUNNER \
 -application org.eclipse.equinox.p2.director \
 -metadataRepository $EMF_XSD_URL \
 -artifactRepository $EMF_XSD_URL \
 -installIU org.eclipse.emf.feature.group \
 -destination $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -profile $Profile \
 -bundlepool $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -p2.os  $P2_OS \
 -p2.ws  $P2_WS \
 -p2.arch $P2_ARCH \
 -roaming -vmargs \
 -Declipse.p2.data.area=$TARGET_LOCATION/$PACKAGE_TYPE/eclipse/p2

$ECLIPSE_RUNNER \
 -application org.eclipse.equinox.p2.director \
 -metadataRepository $EMF_XSD_URL \
 -artifactRepository $EMF_XSD_URL \
 -installIU org.eclipse.xsd.feature.group \
 -destination $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -profile $Profile \
 -bundlepool $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -p2.os  $P2_OS \
 -p2.ws  $P2_WS \
 -p2.arch $P2_ARCH \
 -roaming -vmargs \
 -Declipse.p2.data.area=$TARGET_LOCATION/$PACKAGE_TYPE/eclipse/p2

$ECLIPSE_RUNNER \
 -application org.eclipse.equinox.p2.director \
 -metadataRepository $EMF_XSD_URL \
 -artifactRepository $EMF_XSD_URL \
 -installIU org.eclipse.xsd.edit.feature.group \
 -destination $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -profile $Profile \
 -bundlepool $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -p2.os  $P2_OS \
 -p2.ws  $P2_WS \
 -p2.arch $P2_ARCH \
 -roaming -vmargs \
 -Declipse.p2.data.area=$TARGET_LOCATION/$PACKAGE_TYPE/eclipse/p2

$ECLIPSE_RUNNER \
 -application org.eclipse.equinox.p2.director \
 -metadataRepository $GEF_URL \
 -artifactRepository $GEF_URL \
 -installIU org.eclipse.gef.feature.group \
 -destination $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -profile $Profile \
 -bundlepool $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -p2.os  $P2_OS \
 -p2.ws  $P2_WS \
 -p2.arch $P2_ARCH \
 -roaming -vmargs \
 -Declipse.p2.data.area=$TARGET_LOCATION/$PACKAGE_TYPE/eclipse/p2

$ECLIPSE_RUNNER \
 -application org.eclipse.equinox.p2.director \
 -metadataRepository $GEF_URL \
 -artifactRepository $GEF_URL \
 -installIU org.eclipse.draw2d.feature.group \
 -destination $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -profile $Profile \
 -bundlepool $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -p2.os  $P2_OS \
 -p2.ws  $P2_WS \
 -p2.arch $P2_ARCH \
 -roaming -vmargs \
 -Declipse.p2.data.area=$TARGET_LOCATION/$PACKAGE_TYPE/eclipse/p2
