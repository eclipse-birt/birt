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

#export WTP_REPO_URL=http://download.eclipse.org/webtools/downloads/drops/R3.3.0/S-3.3.0RC2-20110520234832/repository/

export WTP_REPO_URL=jar:file://$REPO_LOCATION/wtp-repo-M-3.3.2-20120210195245.zip!/

#==================== COMMON SET UP ================================

#install for win
$ECLIPSE_RUNNER \
 -application org.eclipse.equinox.p2.director \
 -metadataRepository $WTP_REPO_URL \
 -artifactRepository $WTP_REPO_URL \
 -installIU org.eclipse.wst.xml_ui.feature.feature.group \
 -destination $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -profile PlatformProfile \
 -bundlepool $TARGET_LOCATION/$PACKAGE_TYPE/eclipse \
 -p2.os  $P2_OS \
 -p2.ws  $P2_WS \
 -p2.arch $P2_ARCH \
 -roaming -vmargs \
 -Declipse.p2.data.area=$TARGET_LOCATION/$PACKAGE_TYPE/eclipse/p2


