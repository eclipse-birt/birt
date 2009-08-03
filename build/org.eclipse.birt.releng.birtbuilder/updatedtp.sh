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

# directory in which to export builder projects
builderDir=/home/adb/releng.250/org.eclipse.birt.releng.birtbuilder/

# buildtype determines whether map file tags are used as entered or are replaced with HEAD
buildType=I

# directory where to copy build
postingDirectory=/home/adb/releng/BIRTOutput/BIRT2.5-download

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

# After the above the build type is left in $1.
buildType=$1

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

#Set the tag to HEAD for Nightly builds
if [ "$buildType" = "N" ]
then
        tag="-DfetchTag=HEAD"
        versionQualifier="-DforceContextQualifier=$buildId"
fi


# tag for eclipseInternalBuildTools on ottcvs1
internalToolsTag=$buildProjectTags

# tag for exporting org.eclipse.releng.basebuilder
baseBuilderTag=$buildProjectTags

# tag for exporting the custom builder
customBuilderTag=$buildProjectTags

# directory where features and plugins will be compiled
buildDirectory=/home/adb/farrah/BIRT_Build_Dir


cd $builderDir

chmod -R 755 $builderDir

#default value of the bootclasspath attribute used in ant javac calls.  
bootclasspath="/usr/local/j2sdk1.4.2_13/jre/lib/rt.jar:/usr/local/j2sdk1.4.2_13/jre/lib/jsse.jar"
bootclasspath_15="/usr/local/jdk1.5.0_02/jre/lib/rt.jar"
jvm15_home="/usr/local/jdk1.5.0_02"

cd /home/adb/releng.250/org.eclipse.birt.releng.birtbuilder

#the base command used to run AntRunner headless
antRunner="/usr/local/jdk1.5.0_02/bin/java -Xmx500m -jar ../org.eclipse.releng.basebuilder/plugins/org.eclipse.equinox.launcher.jar -Dosgi.os=linux -Dosgi.ws=gtk -Dosgi.arch=ppc -application org.eclipse.ant.core.antRunner"




ant -buildfile eclipse/helper.xml getDTPDownloads.fromlocal -propertyfile build.properties -propertyfile config.properties > updatedtp.log

