#########################################################################################################################
# Top-Level script to trigger the nightly build of BIRT 
#########################################################################################################################
#
# Example 1: Start a N-build from master branch, signing jars from build.eclipse.org(committer user/passwd required),
#            create update site under /local/home/birtbld/UpdateSite/4_2_2-N directory and skip unit test
#   ./bootstrap.sh -BranchName master -sign -updateSite /local/home/birtbld/UpdateSite/4_2_2-N -noUnitTest N 
# 
# Example 2: Start a I-build from master branch, no signing, no unit test, plugins will be tagged automatically based on 
#            content change. Map files will tagged accordingly. With prepareSrc argument, build script will clone the repo
#             before build starts and copy bundles into ${buildDirectory}, which will speed up the build process.
#   ./bootstrap.sh -BranchName master -prepareSrc -updateSite /local/home/birtbld/UpdateSite/4_2_2-N -noUnitTest I 
#
#########################################################################################################################

########################################################
# User specific environment and startup programs
########################################################

umask 002

BASE_PATH=.:/bin:/usr/bin:/usr/bin/X11:/usr/local/bin:/usr/bin:/usr/X11R6/bin
LD_LIBRARY_PATH=.
BASH_ENV=$HOME/.bashrc
USERNAME=`whoami`
xhost +$HOSTNAME
DISPLAY=:1.0
export DISPLAY

ulimit -c unlimited
export USERNAME BASH_ENV LD_LIBRARY_PATH DISPLAY

# no user name needed for github url, login credential controlled in local ssh config
GitRoot=ssh://git@github.com/eclipse
GitRoot_DTP=ssh://xgu@git.eclipse.org/gitroot/datatools

# change the branch name for different build
BranchName=master
dtp_BranchName=master

# change this when you change the build working folder
WORKING_DIR=/home/adb/releng.450
LOG_FILE=adb.log

#set the monitor file name which is used for uploading builds to eclipse.org
MONITOR_FILE=monitor.properties

if [ "x"$ANT_HOME = "x" ]; then export ANT_HOME=/usr/local/apache-ant-1.7.0; fi
if [ "x"$JAVA_HOME = "x" ]; then export JAVA_HOME=/usr/local/jdk1.5.0_09; fi
export PATH=${PATH}:${ANT_HOME}/bin:/usr/local/bin

#########################################################################
# Set default value of the bootclasspath attribute used in ant javac calls.  
#########################################################################

bootclasspath="/usr/local/j2sdk1.4.2_13/jre/lib/rt.jar:/usr/local/j2sdk1.4.2_13/jre/lib/jsse.jar"
bootclasspath_15="/usr/local/jdk1.5.0_09/jre/lib/rt.jar:/usr/local/jdk1.5.0_09/jre/lib/jsse.jar"
bootclasspath_16="/usr/local/jdk1.6.0/jre/lib/rt.jar:/usr/local/jdk1.6.0/jre/lib/jsse.jar"
jvm15_home="/usr/local/jdk1.5.0_09"
jvm16_home="/usr/local/jdk1.6.0"

proc=$$

##########################################################################
# sets sign Ant property
##########################################################################
sign=""
# remote target folder for BIRT
signDirectory=/home/data/httpd/download-staging.priv/birt
# remote working folder, change if you use a different signer account.
signHomeDir=/home/data/users/zqian
# credential is in local bash config
signUsername=$SIGN_USER
signPassword=$SIGN_PASSWD
signServer=build.eclipse.org

tagMaps=""

#updateSite property setting
updateSite=""

#flag used to build based on changes in map files
compareMaps=""

#buildId - build name
buildId=""

# tag for build contribution project containing .map files
mapVersionTag=$mapVersionTag

# directory in which to export builder projects
builderDir=$WORKING_DIR/org.eclipse.birt.releng.birtbuilder
export builderDir

# directory where to copy build
postingDirectory=$WORKING_DIR/../releng/BIRTOutput/BIRT4.4-download/4.4.2

# flag to indicate if test build
testBuild=""

# value used in buildLabel and for text replacement in index.php template file
builddate=`date +%Y%m%d`
buildtime=`date +%H%M`
buildinfoDate=`date +%F%t%H:%M:%S`
buildinfounivDate=`date +%c%z`
timestamp=$builddate$buildtime

echo "======[builddate]: $builddate " > $LOG_FILE
echo "======[buildtime]: $buildtime " >> $LOG_FILE
echo "======[timestamp]: $timestamp " >> $LOG_FILE

########################################################
# Check disk space before build starts
########################################################

source $builderDir/extras/DiskSpaceCheck.sh

########################################################
# process command line arguments
########################################################

usage="usage: $0 [-notify emailaddresses][-test][-buildDirectory directory][-buildId name][-tagMapFiles][-mapVersionTag tag][-builderTag tag][-bootclasspath path][-compareMaps][-skipPerf] [-skipTest][-updateSite site][-sign][-noUnitTest][CheckNewJars][-test] M|N|I|S|R"

if [ $# -lt 1 ]
then
		 echo >&2 "$usage"
		 exit 1
fi

while [ $# -gt 0 ]
do
		 case "$1" in
		 		 -BranchName) BranchName="$2"; shift;;
		 		 -buildId) buildId="$2"; shift;;
		 		 -mapVersionTag) mapVersionTag="$2"; shift;;
		 		 -noAutoTag) noAutoTag=true;;
		 		 -ForceBuild) ForceBuild=true;;
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
		 		 -prepareSrc) prepareSrc="-Dprepare.src.flag=true";;
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

echo "======[buildType]: $buildType " >> $LOG_FILE
echo "======[BranchName]: $BranchName " >> $LOG_FILE
echo "======[dtp_BranchName]: $dtp_BranchName " >> $LOG_FILE

###############################################################
# Sync build script from branch $BranchName before build starts
###############################################################

#Pull or clone a branch from a repository
#Usage: pull repositoryURL  branch
pull() {
        mkdir -p $builderDir/gitClones
        pushd $builderDir/gitClones
        directory=$(basename $1 .git)
        if [ ! -d $directory ]; then
                echo git clone $1
                git clone $1
        fi
        popd
        pushd $builderDir/gitClones/$directory
	    
	echo ">>git fetch"
        git fetch
        echo ">>git checkout $2"
        git checkout $2
        echo ">>git pull origin $2"
        git pull origin $2
        popd
}

#rm -rf build

# github doesn't support the "archive" command, we have to do a clone instead. 
#git archive --format=tar --remote=$GitRoot/birt.git $BranchName build/org.eclipse.birt.releng.birtbuilder | tar -xf -

echo ">>pulling latest source from $GitRoot/birt.git"
pull $GitRoot/birt.git $BranchName

# update build script from repo
#cp -f build/org.eclipse.birt.releng.birtbuilder/buildAll.xml ./
#cp -f build/org.eclipse.birt.releng.birtbuilder/build.xml ./
#cp -rf build/org.eclipse.birt.releng.birtbuilder/eclipse ./
#cp -rf build/org.eclipse.birt.releng.birtbuilder/extras ./

# Set default buildId if none explicitly set
if [ "$buildId" = "" ]
then
		 buildId=v$builddate-$buildtime
fi

echo "======[buildId]: $buildId " >> $LOG_FILE

#Set the tag to HEAD for Nightly builds
if [ "$buildType" = "N" ]
then
        tag="-DfetchTag=CVS=HEAD,GIT=$BranchName"
        versionQualifier="-DforceContextQualifier=$buildId"
fi

echo "======[tag]: $tag" >> $USER.log
echo "======[versionQualifier]: $versionQualifier" >> $LOG_FILE

# tag for exporting org.eclipse.releng.basebuilder
baseBuilderTag=$buildProjectTags
echo "======[baseBuilderTag]: $baseBuilderTag" >> $LOG_FILE

# directory where features and plugins will be compiled
buildDirectory=$WORKING_DIR/src

echo "======[buildDirectory]: $buildDirectory" >> $LOG_FILE

mkdir $builderDir
cd $builderDir

mkdir -p $postingDirectory/$buildId


###############################################################
# Auto tagging BIRT plugins and update mapfiles for I build
# If you are building outside BIRT, please use buildType 'N'
###############################################################

if [ "$buildType" == "N" -o "$noAutoTag" ]; then
        echo "Skipping auto plugins tagging for nightly build or -noAutoTag build"
else
        pushd $builderDir

        #remove comments
        rm -f repos-clean.txt clones.txt
        echo "$GitRoot/birt.git $BranchName" > repos-clean.txt

	    #clone or pull each repository and checkout the appropriate branch
        while read line; do
                #each line is of the form <repository> <branch>
                set -- $line
                pull $1 $2
                echo $1 | sed 's/ssh:.*@github.com/git:\/\/github.com/g' >> clones.txt
        done < repos-clean.txt

        cat clones.txt| xargs /bin/bash extras/git-map.sh $builderDir/gitClones \
        $builderDir/gitClones > maps.txt

        #Trim out lines that don't require execution
        grep -v ^OK maps.txt | grep -v ^Executed >run.txt
        if ( cat run.txt | grep sed ); then
                /bin/bash run.txt
                #mkdir -p $builderDir/report
                #cp report.txt $builderDir/report/report$buildId.txt
        elif [ "$ForceBuild" == "true" ]; then
                echo "Continue to build even if no bundles changed for -ForceBuild build"
        else
                echo "No change detected. Build ($buildId) is canceled"
                exit
        fi
        popd
fi


echo buildId=$buildId > $MONITOR_FILE 
echo timestamp=$timestamp >> $MONITOR_FILE  
echo currentDay=$builddate >> $MONITOR_FILE  
echo recipients=$recipients >> $MONITOR_FILE 

#the base command used to run AntRunner headless
antRunner="/usr/local/jdk1.6.0/bin/java -Xmx512m -jar ../org.eclipse.releng.basebuilder/plugins/org.eclipse.equinox.launcher.jar -Dosgi.os=linux -Dosgi.ws=gtk -Dosgi.arch=ppc -application org.eclipse.ant.core.antRunner"

echo "======[antRunner]: $antRunner" >> $LOG_FILE

#########################################################################
# Update the build info for build uploading. Please comment out this step
# if you are not going to upload this build to eclipse.org 
#########################################################################
cd $builderDir
$builderDir/replaceBuildInfo.sh $buildinfoDate $buildinfounivDate

#check out internal files from perforce
#ant -buildfile eclipse/helper.xml CheckoutFromP4 >> $LOG_FILE

#full command with args
echo "tagMaps flag:" $tagMaps >> $LOG_FILE
echo $compareMaps >> $LOG_FILE
echo $sign >> $LOG_FILE

########################################################################
# Replace the eclipse related server name with local server name to 
# speed up the build process.
# Please set $birtLocal to the same value as $birtEclipse if you are 
# building outside BIRT. Do the same to $dtpLocal and $orbitLocal.
#-Dbirt.url.token=$birtEclipse \
#-Dbirt.url.newvalue=$birtLocal \
#-Ddtp.url.token=$dtpEclipse \
#-Ddtp.url.newvalue=$dtpLocal \
#-Dorbit.url.token=$orbitEclipse \
#-Dorbit.url.newvalue=$orbitLocal \
########################################################################
#-Dbirt.url.token=$birtEclipse \
#-Dbirt.url.newvalue=$birtLocal \
#-Ddtp.url.token=$dtpEclipse \
#-Ddtp.url.newvalue=$dtpLocal \
#-Dorbit.url.token=$orbitEclipse \
#-Dorbit.url.newvalue=$orbitLocal \
#birtEclipse=git://git.eclipse.org
#birtLocal=git://192.168.218.226
#dtpEclipse=git://git.eclipse.org
#dtpLocal=git://192.168.218.226
#orbitEclipse=download.eclipse.org/tools
#orbitLocal=buildsha-win/software/platform

########################################################################
# Set up the full build command
# *remove the '-q' option if you want to see verbose info in the log*
########################################################################
buildCommand="$antRunner -q -buildfile buildAll.xml $testBuild $compareMaps $unitTest $CheckNewJars $skipNL \
-Dbuild.runtimeOSGI=true  -DallowBinaryCycles=true \
-DpostingDirectory=$postingDirectory \
-Dbootclasspath=$bootclasspath_16 -DbuildType=$buildType -D$buildType=true \
-DbuildId=$buildId -Dbuildid=$buildId -DbuildLabel=$buildId -Dtimestamp=$timestamp $skipTest $tagMaps \
-DJ2SE-1.5=$bootclasspath_15 -DJavaSE-1.6=$bootclasspath_16 -DlogExtension=.xml $javadoc $updateSite $sign  $prepareSrc \
-Djava15-home=$bootclasspath_15 -DbuildDirectory=$WORKING_DIR/src \
-DbaseLocation=$WORKING_DIR/baseLocation -DbaseLocation.emf=$WORKING_DIR/baseLocation \
-DgroupConfiguration=true -DjavacVerbose=true \
-Dbasebuilder=$WORKING_DIR/org.eclipse.releng.basebuilder \
-Dtest.dir=$WORKING_DIR/unittest -Dp4.home=$WORKING_DIR/P4 \
-Djvm15_home=$jvm15_home -Djvm16_home=$jvm16_home -DmapTag.properties=$builderDir/mapTag.properties \
-Dbuild.date=$builddate -Dpackage.version=4_5_0 -DBranchVersion=4.5.0 -Dant.dir=$ANT_HOME/bin \
-DmapVersionTag=$BranchName \
-Ddtp.mapVersionTag=$dtp_BranchName \
-Dusername.sign=$signUsername -Dpassword.sign=$signPassword -Dhostname.sign=$signServer \
-Dhome.dir=$signHomeDir -Dsign.dir=$signDirectory \
-Dbuilder.dir=$builderDir -DupdateSiteConfig=gtk.linux.x86 \
-DmapGitRoot=$GitRoot \
-Ddtp.mapGitRoot=$GitRoot_DTP \
-Dskip.unit.test=true \
-DBirtRepoCache=git___github_com_eclipse_birt_git"


#capture command used to run the build
echo $buildCommand>command.txt

#run the build
$buildCommand >> $LOG_FILE

#retCode=$?
#
#if [ $retCode != 0 ]
#then
#        echo "Build failed (error code $retCode)."
#        exit -1
#fi

#clean up
#rm -rf $builderDir
#rm -rf $WORKING_DIR/src/$buildId


