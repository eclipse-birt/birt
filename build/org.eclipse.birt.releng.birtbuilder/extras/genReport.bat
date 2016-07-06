ECHO off

REM ################## USAGE OF REPORT ENGINE ################
REM org.eclipse.birt.report.engine.impl.ReportRunner Usage:
REM --mode/-m [ run | render | runrender] the default is runrender
REM for runrender mode:
REM we should add it in the end <design file>
REM    --format/-f [ HTML | PDF ]
REM    --output/-o <target file>
REM    --htmlType/-t < HTML | ReportletNoCSS >
REM    --locale/-l <locale>
REM    --parameter/-p <"parameterName=parameterValue">
REM    --file/-F <parameter file>
REM    --encoding/-e <target encoding>
REM
REM Locale: default is English
REM parameters in command line will overide parameters in parameter file
REM parameter name cannot include characters such as \' \'\, \'=\'\, \':\'
REM
REM For RUN mode:
REM we should add it in the end <design file>
REM    --output/-o <target file>
REM    --locale/-l <locale>
REM    --parameter/-p <parameterName=parameterValue>
REM    --file/-F <parameter file>
REM
REM Locale: default is English
REM parameters in command line will overide parameters in parameter file
REM parameter name cannot include characters such as \' \'\, \'=\'\, \':\'
REM
REM For RENDER mode:
REM we should add it in the end <design file>
REM    --output/-o <target file>
REM    --page/-p <pageNumber>
REM    --locale/-l <locale>
REM
REM Locale: default is English
REM ################## USAGE OF REPORT ENGINE END ################

REM set common variables
SET BIRT_HOME=%~dp0\platform
SET BIRT_API=%~dp0\lib\actuate-birt-api.jar
SET WORK_DIR=%~dp0

ECHO BIRT_HOME=%BIRT_HOME%
ECHO BIRT_API=%BIRT_API%
ECHO WORK_DIR=%WORK_DIR%

IF not "%BIRT_HOME%" == "" GOTO runBirt
ECHO "BIRT_HOME must be set before ReportRunner can run"
GOTO end

:runBirt

SET java.io.tmpdir=%WORK_DIR%\tmpdir
SET org.eclipse.datatools_workspacepath=%java.io.tmpdir%\workspace_dtp

IF not exist %java.io.tmpdir% mkdir %java.io.tmpdir%
IF not exist %org.eclipse.datatools_workspacepath% mkdir %org.eclipse.datatools_workspacepath%

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

%JAVACMD% -cp "%BIRT_API%" -DBIRT_HOME="%BIRT_HOME%" org.eclipse.birt.report.engine.api.ReportRunner %p1% %p2% %p3% %p4% %p5% %p6% %p7% %p8% %p9% %p10% %p11% %p12% %p13% %p14% %p15% %p16% %p17% %p18% %p19%

:end
