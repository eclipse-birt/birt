@ECHO OFF

IF not "%ANT_HOME%" == "" GOTO ANTOK
    set ANT_HOME=C:\apache-ant-1.6.2
:ANTOK

IF not "%JAVA_HOME%" == "" GOTO JAVAOK
    set JAVA_HOME=C:\j2sdk1.4.2_07
:JAVAOK


REM the base command used to run AntRunner headless
set antRunner=%JAVA_HOME%/bin/java -Xmx500m -jar ../org.eclipse.releng.basebuilder/startup.jar -Dosgi.os=linux -Dosgi.ws=gtk -Dosgi.arch=ppc -application org.eclipse.ant.core.antRunner

REM set the %buildId% variable to enable pde build. Do not need to be change.
set buildId=20070101

REM full command with args
set buildCommand="%antRunner% -q -buildfile getSourceCodeByBuildTag.xml -propertyfile build.properties -DbuildType=I -DI=true -DbuildId=%buildId% -Dbuildid=%buildId% -DbuildLabel=%buildId% -DlogExtension=.xml -DgroupConfiguration=true -DjavacSource=1.4 -DjavacTarget=1.4 -DjavacVerbose=true  -DpostPackage=BIRTOutput -DmapCvsRoot=:pserver:anonymous@dev.eclipse.org:/cvsroot/birt"

REM capture command used to run the build
echo %buildCommand% >command.txt

REM run the build
%antRunner% -q -buildfile getSourceCodeByBuildTag.xml -propertyfile build.properties -DbuildType=I -DI=true -DbuildId=%buildId% -Dbuildid=%buildId% -DbuildLabel=%buildId% -DlogExtension=.xml -DgroupConfiguration=true -DjavacSource=1.4 -DjavacTarget=1.4 -DjavacVerbose=true  -DpostPackage=BIRTOutput -DmapCvsRoot=:pserver:anonymous@dev.eclipse.org:/cvsroot/birt
>> adb.log

