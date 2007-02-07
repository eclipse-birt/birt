# User specific environment and startup programs
umask 002

BASE_PATH=.:/bin:/usr/bin:/usr/bin/X11:/usr/local/bin:/usr/bin:/usr/X11R6/bin
LD_LIBRARY_PATH=.
BASH_ENV=$HOME/.bashrc
USERNAME=`whoami`
xhost +$HOSTNAME
DISPLAY=:0.0
export DISPLAY

CVS_RSH=ssh
ulimit -c unlimited
export CVS_RSH USERNAME BASH_ENV LD_LIBRARY_PATH DISPLAY

if [ "x"$ANT_HOME = "x" ]; then export ANT_HOME=/usr/local/apache-ant-1.6.5; fi
if [ "x"$JAVA_HOME = "x" ]; then export JAVA_HOME=/usr/local/j2sdk1.4.2_13; fi
export PATH=${PATH}:${ANT_HOME}/bin:/usr/local/bin

proc=$$

tagMaps=""

# directory in which to export builder projects
builderDir=/home/adb/releng/BIRTBuilder/

# buildtype determines whether map file tags are used as entered or are replaced with HEAD
buildType=I

# directory where to copy build
postingDirectory=/home/adb/releng/BIRTOutput/BIRT2.2-download


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
usage="usage: $0 [-notify emailaddresses][-test][-buildDirectory directory][-buildId name][-buildLabel directory name][-tagMapFiles][-mapVersionTag tag][-builderTag tag][-bootclasspath path][-compareMaps][-skipPerf] [-skipTest][-updateSite site][-sign] M|N|I|S|R"


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
echo "======[tag]: $tag" >> adb.log
echo "======[versionQualifier]: $versionQualifier" >> adb.log


# directory where features and plugins will be compiled
buildDirectory=/home/adb/farrah/BIRT_Build_Dir

echo "======[buildDirectory]: $buildDirectory" >> adb.log

mkdir $builderDir
cd $builderDir



mkdir -p $postingDirectory/$buildLabel
chmod -R 755 $builderDir

#default value of the bootclasspath attribute used in ant javac calls.  
bootclasspath="/usr/local/j2sdk1.4.2_13/jre/lib/rt.jar:/usr/local/j2sdk1.4.2_13/jre/lib/jsse.jar"
bootclasspath_15="/usr/local/jdk1.5.0_02/jre/lib/rt.jar"
jvm15_home="/usr/local/jdk1.5.0_02"

cd /home/adb/releng/BIRTBuilder

#the base command used to run AntRunner headless
antRunner="/usr/local/j2sdk1.4.2_13/bin/java -Xmx500m -jar ../BaseBuilder/startup.jar -Dosgi.os=linux -Dosgi.ws=gtk -Dosgi.arch=ppc -application org.eclipse.ant.core.antRunner"

echo "==========[antRunner]: $antRunner" >> adb.log


#full command with args
buildCommand="$antRunner -q -buildfile buildAll.xml -DmapVersionTag=$mapVersionTag -DpostingDirectory=$postingDirectory -Dbootclasspath=$bootclasspath -DbuildType=I -D$buildType=true -DbuildId=$buildId -Dbuildid=$buildId -DbuildLabel=$buildId -Dtimestamp=$timestamp -DJ2SE-1.5=$bootclasspath_15  -DlogExtension=.xml -Djava15-home=$bootclasspath_15 -DbuildDirectory=/home/adb/releng/src -DbaseLocation=/home/adb/releng/baseLocation -DgroupConfiguration=true -DjavacSource=1.4 -DjavacTarget=1.4 -DjavacVerbose=true -Dbasebuilder=/home/adb/releng/BaseBuilder -DpostPackage=BIRTOutput -DmapCvsRoot=:pserver:anonymous@dev.eclipse.org:/cvsroot/birt"

#capture command used to run the build
echo $buildCommand>command.txt

#run the build
$buildCommand >> adb.log

#clean up
rm -rf /home/adb/releng/src/$buildId
