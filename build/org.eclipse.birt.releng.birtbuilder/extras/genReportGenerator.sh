#!/bin/sh

######################################################
# Input argument: 
#      $1: ReportEngine/lib
#      $2: Target folder to put the genReport.sh/.bat
######################################################

ls $1 > lib.path
wc -l lib.path > lib.path.count
count=`awk '{print $1}' lib.path.count`
rm -f lib.path.count

########################################
#Generate $2/genReport.sh
########################################

echo "################USAGE OF REPORTRUNNER#########################"> $2/genReport.sh
echo "#" echo \"org.eclipse.birt.report.engine.impl.ReportRunner Usage:\"\; >> $2/genReport.sh
echo "#" echo \"--mode/-m [ run \| render \| runrender] the default is runrender \" >> $2/genReport.sh
echo "#" echo \"for runrender mode: \" >> $2/genReport.sh
echo "#" echo \"\"  \"we should add it in the end \<design file\>  \" >> $2/genReport.sh
echo "#" echo \"\"  \"--format/-f [ HTML \| PDF ]  \" >> $2/genReport.sh
echo "#" echo \"\"  \"--output/-o \<target file\>\" >> $2/genReport.sh
echo "#" echo \"\"  \"--htmlType/-t \< HTML \| ReportletNoCSS \>\" >> $2/genReport.sh
echo "#" echo \"\"  \"--locale /-l\<locale\>\" >> $2/genReport.sh
echo "#" echo \"\"  \"--parameter/-p \<\"parameterName=parameterValue\"\>\" >> $2/genReport.sh
echo "#" echo \"\"  \"--file/-F \<parameter file\>\" >> $2/genReport.sh
echo "#" echo \"\"  \"--encoding/-e \<target encoding\>\" >> $2/genReport.sh
echo "#" echo \" \" >> $2/genReport.sh
echo "#" echo \"Locale: default is english\" >> $2/genReport.sh
echo "#" echo \"parameters in command line will overide parameters in parameter file\" >> $2/genReport.sh
echo "#" echo \"parameter name can't include characters such as \' \'\, \'=\'\, \':\'\" >> $2/genReport.sh
echo "#" echo \" \" >> $2/genReport.sh
echo "#" echo \"For RUN mode:\" >> $2/genReport.sh
echo "#" echo \"we should add it in the end\<design file\>\" >> $2/genReport.sh
echo "#" echo \"\"  \"--output/-o \<target file\>\" >> $2/genReport.sh
echo "#" echo \"\"  \"--locale /-l\<locale\>\" >> $2/genReport.sh
echo "#" echo \"\"  \"--parameter/-p \<parameterName=parameterValue\>\" >> $2/genReport.sh
echo "#" echo \"\"  \"--file/-F \<parameter file\>\" >> $2/genReport.sh
echo "#" echo \" \" >> $2/genReport.sh
echo "#" echo \"Locale: default is english\" >> $2/genReport.sh
echo "#" echo \"parameters in command line will overide parameters in parameter file\" >> $2/genReport.sh
echo "#" echo \"parameter name can't include characters such as \' \'\, \'=\'\, \':\'\" >> $2/genReport.sh
echo "#" echo \" \" >> $2/genReport.sh
echo "#" echo \"For RENDER mode:\" >> $2/genReport.sh
echo "#" echo \"\" \"we should add it in the end\<design file\>\" >> $2/genReport.sh
echo "#" echo \"\" \"--output/-o \<target file\>\" >> $2/genReport.sh
echo "#" echo \"\" \"--page/-p \<pageNumber\>\" >> $2/genReport.sh
echo "#" echo \"\" \"--locale /-l\<locale\>\" >> $2/genReport.sh
echo "#" echo \" \" >> $2/genReport.sh
echo "#" echo \"Locale: default is english\" >> $2/genReport.sh
echo "################END OF USAGE #########################">> $2/genReport.sh

echo if [ \"\$BIRT_HOME\" = \"\" ]\; >> $2/genReport.sh
echo "" >> $2/genReport.sh

echo then >> $2/genReport.sh
echo echo \" The BIRT_HOME need be set before BirtRunner can run.\"\; >> $2/genReport.sh
echo else >> $2/genReport.sh

echo "" >> $2/genReport.sh
echo "" >> $2/genReport.sh

echo java_io_tmpdir=\$BIRT_HOME/ReportEngine/tmpdir >> $2/genReport.sh
echo org_eclipse_datatools_workspacepath=\$java_io_tmpdir/workspace_dtp >> $2/genReport.sh

echo "" >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo mkdir -p \$org_eclipse_datatools_workspacepath >> $2/genReport.sh

echo "" >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo unset BIRTCLASSPATH >> $2/genReport.sh
echo 'for i in `ls $BIRT_HOME/ReportEngine/lib/*.jar`;do export BIRTCLASSPATH=$i:$BIRTCLASSPATH;done' >> $2/genReport.sh

echo "" >> $2/genReport.sh
echo JAVACMD=\'java\'\; >> $2/genReport.sh

echo  \$JAVACMD -Djava.awt.headless=true -cp \"\$BIRTCLASSPATH\" -DBIRT_HOME=\"\$BIRT_HOME/ReportEngine\" -Dorg.eclipse.datatools_workspacepath=\"\$org_eclipse_datatools_workspacepath\" org.eclipse.birt.report.engine.api.ReportRunner \${1+\"\$@\"} >> $2/genReport.sh
echo "" >> $2/genReport.sh
echo fi >> $2/genReport.sh

chmod +x $2/genReport.sh

########################################
#Generate $2/genReport.bat
########################################

echo ECHO off > $2/genReport.bat
echo "" >> $2/genReport.bat

##################USAGE OF REPORT ENGINE################
echo REM org.eclipse.birt.report.engine.impl.ReportRunner Usage: >> $2/genReport.bat
echo REM --mode/-m [ run \| render \| runrender] the default is runrender >> $2/genReport.bat
echo REM for runrender mode: >> $2/genReport.bat
echo "REM     " "we should add it in the end <design file>" >> $2/genReport.bat
echo "REM     " "--format/-f [ HTML \| PDF ]" >> $2/genReport.bat
echo "REM     " "--output/-o <target file>" >> $2/genReport.bat
echo "REM     " "--htmlType/-t < HTML \| ReportletNoCSS >" >> $2/genReport.bat
echo "REM     " "--locale /-l <locale>" >> $2/genReport.bat
echo "REM     " "--parameter/-p <\"parameterName=parameterValue\">" >> $2/genReport.bat
echo "REM     " "--file/-F <parameter file>" >> $2/genReport.bat
echo "REM     " "--encoding/-e <target encoding>" >> $2/genReport.bat
echo REM >> $2/genReport.bat
echo REM Locale: default is english >> $2/genReport.bat
echo REM parameters in command line will overide parameters in parameter file >> $2/genReport.bat
echo REM parameter name can't include characters such as \' \'\, \'=\'\, \':\' >> $2/genReport.bat
echo REM  >> $2/genReport.bat
echo REM For RUN mode: >> $2/genReport.bat
echo "REM     " "we should add it in the end<design file>" >> $2/genReport.bat
echo "REM     " "--output/-o <target file>" >> $2/genReport.bat
echo "REM     " "--locale /-l <locale>" >> $2/genReport.bat
echo "REM     " "--parameter/-p <parameterName=parameterValue>" >> $2/genReport.bat
echo "REM     " "--file/-F <parameter file>" >> $2/genReport.bat
echo REM  >> $2/genReport.bat
echo REM Locale: default is english >> $2/genReport.bat
echo REM parameters in command line will overide parameters in parameter file\ >> $2/genReport.bat
echo REM parameter name can't include characters such as \' \'\, \'=\'\, \':\'\ >> $2/genReport.bat
echo REM >> $2/genReport.bat
echo REM For RENDER mode: >> $2/genReport.bat
echo "REM    " "we should add it in the end<design file>" >> $2/genReport.bat
echo "REM    " "--output/-o <target file>" >> $2/genReport.bat
echo "REM    " "--page/-p <pageNumber>" >> $2/genReport.bat
echo "REM    " "--locale /-l <locale>" >> $2/genReport.bat
echo REM >> $2/genReport.bat
echo REM Locale: default is english >> $2/genReport.bat
##################USAGE OF REPORT ENGINE END################

echo IF not \"%BIRT_HOME%\" == \"\" GOTO runBirt >> $2/genReport.bat
echo   ECHO \"Please set BIRT_HOME first.\" >> $2/genReport.bat
echo   GOTO end >> $2/genReport.bat
echo :runBirt >> $2/genReport.bat 

echo "" >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo SET java.io.tmpdir=%BIRT_HOME%\\ReportEngine\\tmpdir >> $2/genReport.bat
echo SET org.eclipse.datatools_workspacepath=%java.io.tmpdir%\\workspace_dtp >> $2/genReport.bat

echo "" >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo IF not exist %java.io.tmpdir% mkdir %java.io.tmpdir% >> $2/genReport.bat
echo IF not exist %org.eclipse.datatools_workspacepath% mkdir %org.eclipse.datatools_workspacepath% >> $2/genReport.bat

echo "" >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo REM set the birt class path. >> $2/genReport.bat

echo setlocal enabledelayedexpansion >> $2/genReport.bat
echo set BIRTCLASSPATH= >> $2/genReport.bat
echo 'for %%i in (%BIRT_HOME%\ReportEngine\lib\*.jar) do set BIRTCLASSPATH=%%i;!BIRTCLASSPATH!' >> $2/genReport.bat

echo "" >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo REM set command >> $2/genReport.bat
echo SET JAVACMD=java >> $2/genReport.bat

echo set p1=%1 >> $2/genReport.bat
echo set p2=%2 >> $2/genReport.bat
echo set p3=%3 >> $2/genReport.bat
echo set p4=%4 >> $2/genReport.bat
echo set p5=%5 >> $2/genReport.bat
echo set p6=%6 >> $2/genReport.bat
echo set p7=%7 >> $2/genReport.bat
echo set p8=%8 >> $2/genReport.bat
echo set p9=%9 >> $2/genReport.bat
echo shift >> $2/genReport.bat
echo set p10=%9 >> $2/genReport.bat
echo shift >> $2/genReport.bat
echo set p11=%9 >> $2/genReport.bat
echo shift >> $2/genReport.bat
echo set p12=%9 >> $2/genReport.bat
echo shift >> $2/genReport.bat
echo set p13=%9 >> $2/genReport.bat
echo shift >> $2/genReport.bat
echo set p14=%9 >> $2/genReport.bat
echo shift >> $2/genReport.bat
echo set p15=%9 >> $2/genReport.bat
echo shift >> $2/genReport.bat
echo set p16=%9 >> $2/genReport.bat
echo shift >> $2/genReport.bat
echo set p17=%9 >> $2/genReport.bat
echo shift >> $2/genReport.bat
echo set p18=%9 >> $2/genReport.bat
echo shift >> $2/genReport.bat
echo set p19=%9 >> $2/genReport.bat
echo ""  >> $2/genReport.bat
echo %JAVACMD% -cp \"%BIRTCLASSPATH%\" -DBIRT_HOME=\"%BIRT_HOME%\\ReportEngine\" org.eclipse.birt.report.engine.api.ReportRunner %p1% %p2% %p3% %p4% %p5% %p6% %p7% %p8% %p9% %p10% %p11% %p12% %p13% %p14% %p15% %p16% %p17% %p18% %p19% >> $2/genReport.bat
echo ""  >> $2/genReport.bat
echo :end >> $2/genReport.bat
