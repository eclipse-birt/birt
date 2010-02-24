# User specific environment and startup programs
umask 002

BASE_PATH=.:/bin:/usr/bin:/usr/bin/X11:/usr/local/bin:/usr/bin:/usr/X11R6/bin
LD_LIBRARY_PATH=.
BASH_ENV=$HOME/.bashrc
USERNAME=`whoami`
xhost +$HOSTNAME
DISPLAY=:0.0
export DISPLAY

CVSROOT=:ext:xgu@dev.eclipse.org:/cvsroot/birt
CVS_RSH=ssh
ulimit -c unlimited
export CVSROOT CVS_RSH USERNAME BASH_ENV LD_LIBRARY_PATH DISPLAY

cvs update -r BIRT_2_5_x_Branch -C buildAll.xml eclipse extras
chmod -R +x buildAll.xml eclipse extras

if [ "x"$ANT_HOME = "x" ]; then export ANT_HOME=/usr/local/apache-ant-1.7.0; fi
if [ "x"$JAVA_HOME = "x" ]; then export JAVA_HOME=/usr/local/jdk1.5.0_02; fi
export PATH=${PATH}:${ANT_HOME}/bin:/usr/local/bin

proc=$$

#notification list
recipients=

#sets skip.performance.tests Ant property
skipPerf=""

#sets skip.tests Ant property
skipTest=""

#sets sign Ant property
sign=""

tagMaps=""

#sets fetchTag="HEAD" for nightly builds if required
tag=""

#buildProjectTags=v20060524
buildProjectTags=v20060529

#updateSite property setting
updateSite=""

#flag indicating whether or not mail should be sent to indicate build has started
mail=""

#flag used to build based on changes in map files
compareMaps=""

#buildId - build name
buildId=""

#buildLabel - name parsed in php scripts <buildType>-<buildId>-<datestamp>
buildLabel=""

# tag for build contribution project containing .map files
mapVersionTag=HEAD

# directory in which to export builder projects
builderDir=/home/adb/releng.250/org.eclipse.birt.releng.birtbuilder
export builderDir

#check disk space usage
source /home/adb/releng.250/org.eclipse.birt.releng.birtbuilder/extras/DiskSpaceCheck.sh

# buildtype determines whether map file tags are used as entered or are replaced with HEAD
buildType=I

# directory where to copy build
postingDirectory=/home/adb/releng/BIRTOutput/BIRT2.5-download/2.5.2

# flag to indicate if test build
testBuild=""

# path to javadoc executable
javadoc=""

# value used in buildLabel and for text replacement in index.php template file
builddate=`date +%Y%m%d`
buildtime=`date +%H%M`

buildinfoDate=`date +%F%t%H:%M:%S`
buildinfounivDate=`date +%c%z`

timestamp=$builddate$buildtime

echo "======[builddate]: $builddate " > adb.log
echo "======[buildtime]: $buildtime " >> adb.log
echo "======[timestamp]: $timestamp " >> adb.log

# process command line arguments
usage="usage: $0 [-notify emailaddresses][-test][-buildDirectory directory][-buildId name][-buildLabel directory name][-tagMapFiles][-mapVersionTag tag][-builderTag tag][-bootclasspath path][-compareMaps][-skipPerf] [-skipTest][-updateSite site][-sign][-noUnitTest][CheckNewJars][-test] M|N|I|S|R"

if [ $# -lt 1 ]
then
		 echo >&2 "$usage"
		 exit 1
fi

while [ $# -gt 0 ]
do
		 case "$1" in
		 		 -buildId) buildId="$2"; shift;;
		 		 -buildLabel) buildLabel="$2"; shift;;
		 		 -mapVersionTag) mapVersionTag="$2"; shift;;
		 		 -tagMapFiles) tagMaps="-DtagMaps=true";;
		 		 -skipPerf) skipPerf="-Dskip.performance.tests=true";;
		 		 -skipTest) skipTest="-Dskip.tests=true";;
		 		 -buildDirectory) builderDir="$2"; shift;;
		 		 -notify) recipients="$2"; shift;;
		 		 -test) testBuild="-Dnomail=true";;
		 		 -javadoc) javadoc="-DgenJavaDoc=true";;
		 		 -builderTag) buildProjectTags="$2"; shift;;
		 		 -noUnitTest) unitTest="-Dskip.unit.test=true";;
		 		 -compareMaps) compareMaps="-DcompareMaps=true";;
		 		 -updateSite) updateSite="-DupdateSite=$2";shift;;
		 		 -sign) sign="-Dsign=true";;
                                 -CheckNewJars) CheckNewJars="-DCheckNewJars=true";;
		 		 -*)
		 		 		 echo >&2 $usage
		 		 		 exit 1;;
		 		 *) break;;		 # terminate while loop
		 esac
		 shift
done

# After the above the build type is left in $1.
buildType=$1
echo "======[buildType]: $buildType " >> adb.log 
# Set default buildId and buildLabel if none explicitly set
if [ "$buildId" = "" ]
then
		 #buildId=$buildType$builddate-$buildtime
		 buildId=v$builddate-$buildtime
fi

if [ "$buildLabel" = "" ]
then
		 buildLabel=$buildId
fi
echo "======[buildId]: $buildId " >> adb.log

#Set the tag to HEAD for Nightly builds
if [ "$buildType" = "N" ]
then
        tag="-DfetchTag=HEAD"
        versionQualifier="-DforceContextQualifier=$buildId"
fi

echo "======[tag]: $tag" >> adb.log
echo "======[versionQualifier]: $versionQualifier" >> adb.log

# tag for eclipseInternalBuildTools on ottcvs1
internalToolsTag=$buildProjectTags
echo "======[internalToolsTag]: $internalToolsTag" >> adb.log

# tag for exporting org.eclipse.releng.basebuilder
baseBuilderTag=$buildProjectTags
echo "======[baseBuilderTag]: $baseBuilderTag" >> adb.log

# tag for exporting the custom builder
customBuilderTag=$buildProjectTags
echo "======[customBuilderTag]: $customBuilderTag" >> adb.log

#if [ -e $builderDir ]
#then
#	 builderDir=$builderDir$timestamp
#fi

# directory where features and plugins will be compiled
buildDirectory=/home/adb/farrah/BIRT_Build_Dir

echo "======[buildDirectory]: $buildDirectory" >> adb.log

mkdir $builderDir
cd $builderDir



mkdir -p $postingDirectory/$buildLabel
chmod -R 755 $builderDir

#default value of the bootclasspath attribute used in ant javac calls.  
bootclasspath="/usr/local/j2sdk1.4.2_13/jre/lib/rt.jar:/usr/local/j2sdk1.4.2_13/jre/lib/jsse.jar"
bootclasspath_15="/usr/local/jdk1.5.0_02/jre/lib/rt.jar:/usr/local/jdk1.5.0_02/jre/lib/jsse.jar"
bootclasspath_16="/usr/local/jdk1.6.0/jre/lib/rt.jar:/usr/local/jdk1.6.0/jre/lib/jsse.jar"
jvm15_home="/usr/local/jdk1.5.0_02"

cd /home/adb/releng.250/org.eclipse.birt.releng.birtbuilder

echo buildId=$buildId >> monitor.properties 
echo timestamp=$timestamp >> monitor.properties 
echo buildLabel=$buildLabel >> monitor.properties 
echo currentDay=$builddate >> monitor.properties 
echo recipients=$recipients >> monitor.properties
echo log=$postingDirectory/$buildLabel/index.php >> monitor.properties

#the base command used to run AntRunner headless
antRunner="/usr/local/jdk1.5.0_02/bin/java -Xmx512m -jar ../org.eclipse.releng.basebuilder/plugins/org.eclipse.equinox.launcher.jar -Dosgi.os=linux -Dosgi.ws=gtk -Dosgi.arch=ppc -application org.eclipse.ant.core.antRunner"

echo "==========[antRunner]: $antRunner" >> adb.log


/home/adb/releng.250/org.eclipse.birt.releng.birtbuilder/replaceBuildInfo.sh $buildinfoDate $buildinfounivDate

#clean drop directories
#ant -buildfile eclipse/helper.xml cleanBuild -propertyfile build.properties>> adb.log
#ant -buildfile eclipse/helper.xml getDTPDownloads -propertyfile build.properties>> adb.log
#ant -buildfile eclipse/helper.xml CheckoutFromP4 >> adb.log

#full command with args
#buildId=v20080523-0630
echo "tagMaps flag:" $tagMaps >> adb.log
echo $compareMaps >> adb.log
echo $sign >> adb.log

cp /home/adb/releng.dtp.172/dtpURLmonitor.properties /home/adb/releng.250/src/

buildCommand="$antRunner -q -buildfile buildAll.xml $mail $testBuild $compareMaps $unitTest $CheckNewJars \
-DmapVersionTag=$mapVersionTag -DpostingDirectory=$postingDirectory \
-Dbootclasspath=$bootclasspath_15 -DbuildType=$buildType -D$buildType=true \
-DbuildId=$buildId -Dbuildid=$buildId -DbuildLabel=$buildId -Dtimestamp=$timestamp $skipPerf $skipTest $tagMaps \
-DJ2SE-1.5=$bootclasspath_15 -DJavaSE-1.6=$bootclasspath_16 -DlogExtension=.xml $javadoc $updateSite $sign  \
-Djava15-home=$bootclasspath_15 -DbuildDirectory=/home/adb/releng.250/src \
-DbaseLocation=/home/adb/releng.250/baseLocation -DbaseLocation.emf=/home/adb/releng.250/baseLocation \
-DgroupConfiguration=true -DjavacVerbose=true \
-Dbasebuilder=/home/adb/releng.250/org.eclipse.releng.basebuilder -DpostPackage=BIRTOutput  \
-Dtest.dir=/home/adb/releng.250/unittest -Dp4.home=/home/adb/releng.250/P4 \
-Djvm15_home=$jvm15_home  -DmapTag.properties=/home/adb/releng.250/org.eclipse.birt.releng.birtbuilder/mapTag.properties \
-Dbuild.date=$builddate -DmapCvsRoot=:ext:xgu@dev.eclipse.org:/cvsroot/birt -Dpackage.version=2_5_2 \
-DmapVersionTag=HEAD -DBranchVersion=2.5.2 -Dant.dir=$ANT_HOME/bin \
-Dusername.sign=slee -Dpassword.sign="" -Dhostname.sign=build.eclipse.org -Dhome.dir=/home/data/users/slee -Dsign.dir=/home/data/httpd/download-staging.priv/birt"

#skipPreBuild

#capture command used to run the build
echo $buildCommand>command.txt

#run the build
$buildCommand >> adb.log
#retCode=$?
#
#if [ $retCode != 0 ]
#then
#        echo "Build failed (error code $retCode)."
#        exit -1
#fi

#clean up
#rm -rf $builderDir
rm -rf /home/adb/releng.250/src/$buildId


