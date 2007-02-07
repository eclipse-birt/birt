# User specific environment and startup programs
umask 002

BASE_PATH=.:/bin:/usr/bin:/usr/bin/X11:/usr/local/bin:/usr/bin:/usr/X11R6/bin
LD_LIBRARY_PATH=.
BASH_ENV=$HOME/.bashrc

CVS_RSH=ssh
ulimit -c unlimited
export CVS_RSH  BASH_ENV LD_LIBRARY_PATH 
if [ "x"$ANT_HOME = "x" ]; then export ANT_HOME=/usr/local/apache-ant-1.6.5; fi
if [ "x"$JAVA_HOME = "x" ]; then export JAVA_HOME=/usr/local/j2sdk1.4.2_13; fi
export PATH=${PATH}:${ANT_HOME}/bin:/usr/local/bin

proc=$$

# buildtype determines whether map file tags are used as entered or are replaced with HEAD
buildType=I

# value used in buildLabel and for text replacement in index.php template file
builddate=`date +%Y%m%d`
buildtime=`date +%H%M`

buildinfoDate=`date +%F%t%H:%M:%S`
buildinfounivDate=`date +%c%z`

timestamp=$builddate$buildtime

echo "======[builddate]: $builddate " > adb.log
echo "======[buildtime]: $buildtime " >> adb.log
echo "======[timestamp]: $timestamp " >> adb.log


# Set default buildId and buildLabel if none explicitly set
if [ "$buildId" = "" ]
then
		 buildId=v$builddate-$buildtime
fi

if [ "$buildLabel" = "" ]
then
		 buildLabel=$buildId
fi


#default value of the bootclasspath attribute used in ant javac calls.  
bootclasspath="/usr/local/j2sdk1.4.2_13/jre/lib/rt.jar:/usr/local/j2sdk1.4.2_13/jre/lib/jsse.jar"

cd /home/adb/releng/org.eclipse.birt.releng.util

#the base command used to run AntRunner headless
antRunner="/usr/local/j2sdk1.4.2_13/bin/java -Xmx500m -jar ../org.eclipse.releng.basebuilder/startup.jar -Dosgi.os=linux -Dosgi.ws=gtk -Dosgi.arch=ppc -application org.eclipse.ant.core.antRunner"

echo "==========[antRunner]: $antRunner" >> adb.log


#full command with args
buildCommand="$antRunner -q -buildfile getSourceCodeByBuildTag.xml -propertyfile build.properties -Dbootclasspath=$bootclasspath -DbuildType=I -D$buildType=true -DbuildId=$buildId -Dbuildid=$buildId -DbuildLabel=$buildId -Dtimestamp=$timestamp  -DlogExtension=.xml -DgroupConfiguration=true -DjavacSource=1.4 -DjavacTarget=1.4 -DjavacVerbose=true  -DpostPackage=BIRTOutput -DmapCvsRoot=:pserver:anonymous@dev.eclipse.org:/cvsroot/birt"

#capture command used to run the build
echo $buildCommand>command.txt

#run the build
$buildCommand >> adb.log

#clean up
rm -rf /home/adb/releng/src/$buildId
