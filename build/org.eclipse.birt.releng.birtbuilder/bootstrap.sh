# User specific environment and startup programs
umask 002

BASE_PATH=.:/bin:/usr/bin:/usr/bin/X11:/usr/local/bin:/usr/bin:/usr/X11R6/bin
LD_LIBRARY_PATH=.
BASH_ENV=$HOME/.bashrc
USERNAME=`whoami`
export HOSTNAME=qa-build.actuate.com
xhost +$HOSTNAME
DISPLAY=:0.0
export DISPLAY

ulimit -c unlimited
export CVSROOT CVS_RSH USERNAME BASH_ENV LD_LIBRARY_PATH DISPLAY

GitRepo=ssh://git@192.168.218.226/gitroot/birt/org.eclipse.birt.git
BranchName=master
dtp_BranchName=master

if [ "x"$ANT_HOME = "x" ]; then export ANT_HOME=/usr/local/apache-ant-1.7.0; fi
if [ "x"$JAVA_HOME = "x" ]; then export JAVA_HOME=/usr/local/jdk1.5.0_09; fi
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
builderDir=$HOME/releng.420/org.eclipse.birt.releng.birtbuilder
export builderDir

#check disk space usage
source $HOME/releng.420/org.eclipse.birt.releng.birtbuilder/extras/DiskSpaceCheck.sh

# buildtype determines whether map file tags are used as entered or are replaced with HEAD
buildType=I

# directory where to copy build
postingDirectory=$HOME/releng/BIRTOutput/BIRT4.2-download/4.2.2

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

echo "======[builddate]: $builddate " > $USER.log
echo "======[buildtime]: $buildtime " >> $USER.log
echo "======[timestamp]: $timestamp " >> $USER.log

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
		 		 -BranchName) BranchName="$2"; shift;;
		 		 -buildId) buildId="$2"; shift;;
		 		 -buildLabel) buildLabel="$2"; shift;;
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
                 -skipNL) skipNL="-Dskip.build.NL=true";;
		 		 -*)
		 		 		 echo >&2 $usage
		 		 		 exit 1;;
		 		 *) break;;		 # terminate while loop
		 esac
		 shift
done

echo "======[BranchName]: $BranchName " >> $USER.log
echo "======[dtp_BranchName]: $dtp_BranchName " >> $USER.log
echo "======[GitRepo]: $GitRepo " >> $USER.log

#sync build script
rm -rf build
git archive --format=tar --remote=$GitRepo $BranchName build/org.eclipse.birt.releng.birtbuilder | tar -xf -
cp -f build/org.eclipse.birt.releng.birtbuilder/buildAll.xml ./
cp -f build/org.eclipse.birt.releng.birtbuilder/build.xml ./
cp -rf build/org.eclipse.birt.releng.birtbuilder/eclipse ./
cp -rf build/org.eclipse.birt.releng.birtbuilder/extras ./
chmod -R +x buildAll.xml eclipse extras

# After the above the build type is left in $1.
buildType=$1
echo "======[buildType]: $buildType " >> $USER.log
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
echo "======[buildId]: $buildId " >> $USER.log

#Set the tag to HEAD for Nightly builds
if [ "$buildType" = "N" ]
then
        tag="-DfetchTag=CVS=HEAD,GIT=$BranchName"
        versionQualifier="-DforceContextQualifier=$buildId"
fi

echo "======[tag]: $tag" >> $USER.log
echo "======[versionQualifier]: $versionQualifier" >> $USER.log

# tag for eclipseInternalBuildTools on ottcvs1
internalToolsTag=$buildProjectTags
echo "======[internalToolsTag]: $internalToolsTag" >> $USER.log

# tag for exporting org.eclipse.releng.basebuilder
baseBuilderTag=$buildProjectTags
echo "======[baseBuilderTag]: $baseBuilderTag" >> $USER.log

# tag for exporting the custom builder
customBuilderTag=$buildProjectTags
echo "======[customBuilderTag]: $customBuilderTag" >> $USER.log

# directory where features and plugins will be compiled
buildDirectory=$HOME/farrah/BIRT_Build_Dir

echo "======[buildDirectory]: $buildDirectory" >> $USER.log

mkdir $builderDir
cd $builderDir

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
	echo git fetch
        git fetch
        echo git checkout $2
        git checkout $2
        echo git pull origin $2
        git pull origin $2
        popd
}

if [ "$buildType" == "N" -o "$noAutoTag" ]; then
        echo "Skipping auto plugins tagging for nightly build or -noAutoTag build"
else
        pushd $builderDir

        #remove comments
        rm -f repos-clean.txt clones.txt
        GitRoot=ssh://git@192.168.218.226/gitroot/birt
        echo "$GitRoot/org.eclipse.birt.git $BranchName" > repos-clean.txt

	#clone or pull each repository and checkout the appropriate branch
        while read line; do
                #each line is of the form <repository> <branch>
                set -- $line
                pull $1 $2
                echo $1 | sed 's/ssh:.*@192.168.218.226/git:\/\/git.eclipse.org/g' >> clones.txt
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

mkdir -p $postingDirectory/$buildLabel
#chmod -R 755 $builderDir

#default value of the bootclasspath attribute used in ant javac calls.  
bootclasspath="/usr/local/j2sdk1.4.2_13/jre/lib/rt.jar:/usr/local/j2sdk1.4.2_13/jre/lib/jsse.jar"
bootclasspath_15="/usr/local/jdk1.5.0_09/jre/lib/rt.jar:/usr/local/jdk1.5.0_09/jre/lib/jsse.jar"
bootclasspath_16="/usr/local/jdk1.6.0/jre/lib/rt.jar:/usr/local/jdk1.6.0/jre/lib/jsse.jar"
jvm15_home="/usr/local/jdk1.5.0_09"

cd $HOME/releng.420/org.eclipse.birt.releng.birtbuilder

echo buildId=$buildId >> monitor.properties 
echo timestamp=$timestamp >> monitor.properties 
echo buildLabel=$buildLabel >> monitor.properties 
echo currentDay=$builddate >> monitor.properties 
echo recipients=$recipients >> monitor.properties
echo log=$postingDirectory/$buildLabel/index.php >> monitor.properties

#the base command used to run AntRunner headless
#antRunner="/usr/local/jdk1.5.0_09/bin/java -Xmx512m -jar ../org.eclipse.releng.basebuilder/plugins/org.eclipse.equinox.launcher.jar -Dosgi.os=linux -Dosgi.ws=gtk -Dosgi.arch=ppc -application org.eclipse.ant.core.antRunner"
antRunner="/usr/local/jdk1.6.0/bin/java -Xmx512m -jar ../org.eclipse.releng.basebuilder/plugins/org.eclipse.equinox.launcher.jar -Dosgi.os=linux -Dosgi.ws=gtk -Dosgi.arch=ppc -application org.eclipse.ant.core.antRunner"

echo "==========[antRunner]: $antRunner" >> $USER.log


$HOME/releng.420/org.eclipse.birt.releng.birtbuilder/replaceBuildInfo.sh $buildinfoDate $buildinfounivDate

#clean drop directories
#ant -buildfile eclipse/helper.xml cleanBuild -propertyfile build.properties>> $USER.log
#ant -buildfile eclipse/helper.xml getDTPDownloads -propertyfile build.properties>> $USER.log
#ant -buildfile eclipse/helper.xml CheckoutFromP4 >> $USER.log

#full command with args
#buildId=v20110523-2201
echo "tagMaps flag:" $tagMaps >> $USER.log
echo $compareMaps >> $USER.log
echo $sign >> $USER.log

cp $HOME/releng.dtp.1102/dtpURLmonitor.properties $HOME/releng.420/src/

buildCommand="$antRunner -q -buildfile buildAll.xml $mail $testBuild $compareMaps $unitTest $CheckNewJars $skipNL \
-Dbuild.runtimeOSGI=true \
-DpostingDirectory=$postingDirectory \
-Dbootclasspath=$bootclasspath_15 -DbuildType=$buildType -D$buildType=true \
-DbuildId=$buildId -Dbuildid=$buildId -DbuildLabel=$buildId -Dtimestamp=$timestamp $skipPerf $skipTest $tagMaps \
-DJ2SE-1.5=$bootclasspath_15 -DJavaSE-1.6=$bootclasspath_16 -DlogExtension=.xml $javadoc $updateSite $sign  $prepareSrc \
-Djava15-home=$bootclasspath_15 -DbuildDirectory=$HOME/releng.420/src \
-DbaseLocation=$HOME/releng.420/baseLocation -DbaseLocation.emf=$HOME/releng.420/baseLocation \
-DgroupConfiguration=true -DjavacVerbose=true \
-Dbasebuilder=$HOME/releng.420/org.eclipse.releng.basebuilder -DpostPackage=BIRTOutput  \
-Dtest.dir=$HOME/releng.420/unittest -Dp4.home=$HOME/releng.420/P4 \
-Djvm15_home=$jvm15_home  -DmapTag.properties=$HOME/releng.420/org.eclipse.birt.releng.birtbuilder/mapTag.properties \
-Dbuild.date=$builddate -Dpackage.version=4_2_2 -DBranchVersion=4.2.2 -Dant.dir=$ANT_HOME/bin \
-DmapVersionTag=$BranchName \
-Ddtp.mapVersionTag=$dtp_BranchName \
-Dusername.sign= -Dpassword.sign= -Dhostname.sign=build.eclipse.org \
-Dhome.dir=/home/data/users/xgu -Dsign.dir=/home/data/httpd/download-staging.priv/birt \
-Dbuilder.dir=$builderDir -DupdateSiteConfig=gtk.linux.x86 \
-DmapGitRoot=ssh://git@192.168.218.226/gitroot/birt \
-Ddtp.mapGitRoot=ssh://git@192.168.218.226/gitroot/datatools \
-Dbirt.url.token=git://git.eclipse.org \
-Dbirt.url.newvalue=git://192.168.218.226 \
-Ddtp.url.token=git://git.eclipse.org \
-Ddtp.url.newvalue=git://192.168.218.226 \
-DBirtRepoCache=git___192_168_218_226_gitroot_birt_org_eclipse_birt_git"

#skipPreBuild

#capture command used to run the build
echo $buildCommand>command.txt

#run the build
$buildCommand >> $USER.log

#retCode=$?
#
#if [ $retCode != 0 ]
#then
#        echo "Build failed (error code $retCode)."
#        exit -1
#fi

#clean up
#rm -rf $builderDir
#rm -rf $HOME/releng.420/src/$buildId


