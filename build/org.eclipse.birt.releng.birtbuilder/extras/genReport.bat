@ECHO off

if [%1]==[help] goto usage
if [%1]==[/?] goto usage
if not [%1]==[] goto run

:usage
REM ################## USAGE OF REPORT ENGINE ################
echo " org.eclipse.birt.report.engine.impl.ReportRunner Usage:                  "
echo " --mode/-m [ runrender | render | run] the default is runrender           "
echo "                                                                          "
echo " For runrender mode:                                                      "
echo "  we should add it in the end <design file>                               "
echo "     --format/-f [ HTML | PDF ]                                           "
echo "     --output/-o <target file>                                            "
echo "     --htmlType/-t < HTML | ReportletNoCSS >                              "
echo "     --locale/-l <locale>                                                 "
echo "     --parameter/-p <"parameterName=parameterValue">                      "
echo "     --file/-F <parameter file>                                           "
echo "     --encoding/-e <target encoding>                                      "
echo "                                                                          " 
echo " example: genReport.bat -m runrender -f PDF samples\hello_world.rptdesign "
echo "                                                                          "
echo "  Locale: default is English                                              "
echo "  parameters in command line will overide parameters in parameter file    "
echo "  parameter name cannot include characters such as \' \'\, \'=\'\, \':\'  "
echo "                                                                          "
echo " For RUN mode:                                                            "
echo "  we should add it in the end <design file>                               "
echo "     --output/-o <target file>                                            "
echo "     --locale/-l <locale>                                                 "
echo "     --parameter/-p <parameterName=parameterValue>                        "
echo "     --file/-F <parameter file>                                           "
echo "                                                                          "
echo "  Locale: default is English                                              "
echo "  parameters in command line will overide parameters in parameter file    "
echo "  parameter name cannot include characters such as \' \'\, \'=\'\, \':\'  "
echo "                                                                          "
echo " For RENDER mode:                                                         "
echo "  we should add it in the end <design file>                               "
echo "     --output/-o <target file>                                            "
echo "     --page/-p <pageNumber>                                               "
echo "     --locale/-l <locale>                                                 "
echo "                                                                          "
echo "  Locale: default is English                                              "
goto end
REM ################## USAGE OF REPORT ENGINE END ################

:run
REM ################## Start ################
REM set common variables
if exist %~dp0\platform (
SET BIRT_HOME=%~dp0\platform
) else (
SET BIRT_HOME=%~dp0
)
SET WORK_DIR=%~dp0

ECHO BIRT_HOME=%BIRT_HOME%
ECHO WORK_DIR=%WORK_DIR%

IF not "%BIRT_HOME%" == "" GOTO runBirt
ECHO "Please set BIRT_HOME first."
GOTO end
:runBirt


SET java.io.tmpdir=%WORK_DIR%\tmpdir
SET org.eclipse.datatools_workspacepath=%java.io.tmpdir%\workspace_dtp


IF not exist %java.io.tmpdir% mkdir %java.io.tmpdir%
IF not exist %org.eclipse.datatools_workspacepath% mkdir %org.eclipse.datatools_workspacepath%


REM set the birt class path.
setlocal enabledelayedexpansion
set BIRTCLASSPATH=%BIRT_HOME%\lib\*

REM set command
SET JAVACMD=java
set p1=%1
set p2=%2
set p3=%3
set p4=%4
set p5=%5
set p6=%6
set p7=%7
set p8=%8
set p9=%9
shift
set p10=%9
shift
set p11=%9
shift
set p12=%9
shift
set p13=%9
shift
set p14=%9
shift
set p15=%9
shift
set p16=%9
shift
set p17=%9
shift
set p18=%9
shift
set p19=%9

echo Java command=%JAVACMD% -cp "%BIRTCLASSPATH%" -DBIRT_HOME='%BIRT_HOME%' org.eclipse.birt.report.engine.api.ReportRunner %p1% %p2% %p3% %p4% %p5% %p6% %p7% %p8% %p9% %p10% %p11% %p12% %p13% %p14% %p15% %p16% %p17% %p18% %p19%

%JAVACMD% -cp "%BIRTCLASSPATH%" -DBIRT_HOME='%BIRT_HOME%' org.eclipse.birt.report.engine.api.ReportRunner %p1% %p2% %p3% %p4% %p5% %p6% %p7% %p8% %p9% %p10% %p11% %p12% %p13% %p14% %p15% %p16% %p17% %p18% %p19%

:end
