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

echo "#"!/bin/sh > $2/genReport.sh
echo "" >> $2/genReport.sh

echo "################ USAGE OF REPORTRUNNER #########################" >> $2/genReport.sh
echo "# org.eclipse.birt.report.engine.impl.ReportRunner Usage:" >> $2/genReport.sh
echo "# --mode/-m [ run | render | runrender] the default is runrender" >> $2/genReport.sh
echo "# for runrender mode:" >> $2/genReport.sh
echo "# we should add it in the end <design file>" >> $2/genReport.sh
echo "#    --format/-f [ HTML | PDF ]" >> $2/genReport.sh
echo "#    --output/-o <target file>" >> $2/genReport.sh
echo "#    --htmlType/-t < HTML | ReportletNoCSS >" >> $2/genReport.sh
echo "#    --locale/-l<locale>" >> $2/genReport.sh
echo "#    --parameter/-p <\"parameterName=parameterValue\">" >> $2/genReport.sh
echo "#    --file/-F <parameter file>" >> $2/genReport.sh
echo "#    --encoding/-e <target encoding>" >> $2/genReport.sh
echo "#" >> $2/genReport.sh
echo "# Locale: default is English" >> $2/genReport.sh
echo "# parameters in command line will overide parameters in parameter file" >> $2/genReport.sh
echo "# parameter name cannot include characters such as \' \'\, \'=\'\, \':\'" >> $2/genReport.sh
echo "#" >> $2/genReport.sh
echo "# For RUN mode:" >> $2/genReport.sh
echo "# we should add it in the end <design file>" >> $2/genReport.sh
echo "#    --output/-o <target file>" >> $2/genReport.sh
echo "#    --locale/-l<locale>" >> $2/genReport.sh
echo "#    --parameter/-p <parameterName=parameterValue>" >> $2/genReport.sh
echo "#    --file/-F <parameter file>" >> $2/genReport.sh
echo "#" >> $2/genReport.sh
echo "# Locale: default is English" >> $2/genReport.sh
echo "# parameters in command line will overide parameters in parameter file" >> $2/genReport.sh
echo "# parameter name cannot include characters such as \' \'\, \'=\'\, \':\'" >> $2/genReport.sh
echo "#" >> $2/genReport.sh
echo "# For RENDER mode:" >> $2/genReport.sh
echo "# we should add it in the end <design file>" >> $2/genReport.sh
echo "#    --output/-o <target file>" >> $2/genReport.sh
echo "#    --page/-p <pageNumber>" >> $2/genReport.sh
echo "#    --locale/-l<locale>" >> $2/genReport.sh
echo "#" >> $2/genReport.sh
echo "# Locale: default is English" >> $2/genReport.sh
echo "################ END OF USAGE #########################">> $2/genReport.sh
echo "" >> $2/genReport.sh

echo "#" echo set common variables >> $2/genReport.sh
echo export BIRT_HOME=\$PWD/platform >> $2/genReport.sh
echo export BIRT_API=\$PWD/lib/actuate-birt-api.jar >> $2/genReport.sh
echo export WORK_DIR=\$PWD >> $2/genReport.sh
echo "" >> $2/genReport.sh

echo echo BIRT_HOME=\$BIRT_HOME >> $2/genReport.sh
echo echo BIRT_API=\$BIRT_API >> $2/genReport.sh
echo echo WORK_DIR=\$WORK_DIR >> $2/genReport.sh
echo "" >> $2/genReport.sh

echo if [ \"\$BIRT_HOME\" = \"\" ] >> $2/genReport.sh
echo "" >> $2/genReport.sh

echo then >> $2/genReport.sh
echo echo \"BIRT_HOME must be set before ReportRunner can run\"\; >> $2/genReport.sh
echo else >> $2/genReport.sh
echo "" >> $2/genReport.sh

echo java_io_tmpdir=\$WORK_DIR/tmpdir >> $2/genReport.sh
echo org_eclipse_datatools_workspacepath=\$java_io_tmpdir/workspace_dtp >> $2/genReport.sh
echo mkdir -p \$org_eclipse_datatools_workspacepath >> $2/genReport.sh
echo "" >> $2/genReport.sh

echo JAVACMD=\'java\'\; >> $2/genReport.sh
echo  \$JAVACMD -Djava.awt.headless=true -cp \"\$BIRT_API\" -DBIRT_HOME=\"\$BIRT_HOME\" -Dorg.eclipse.datatools_workspacepath=\"\$org_eclipse_datatools_workspacepath\" org.eclipse.birt.report.engine.api.ReportRunner \${1+\"\$@\"} >> $2/genReport.sh
echo "" >> $2/genReport.sh

echo fi >> $2/genReport.sh

chmod +x $2/genReport.sh


########################################
#Generate $2/genReport.bat
########################################

echo ECHO off > $2/genReport.bat
echo "" >> $2/genReport.bat

echo "REM ################## USAGE OF REPORT ENGINE ################" >> $2/genReport.bat
echo "REM org.eclipse.birt.report.engine.impl.ReportRunner Usage:" >> $2/genReport.bat
echo "REM --mode/-m [ run | render | runrender] the default is runrender" >> $2/genReport.bat
echo "REM for runrender mode:" >> $2/genReport.bat
echo "REM we should add it in the end <design file>" >> $2/genReport.bat
echo "REM    --format/-f [ HTML | PDF ]" >> $2/genReport.bat
echo "REM    --output/-o <target file>" >> $2/genReport.bat
echo "REM    --htmlType/-t < HTML | ReportletNoCSS >" >> $2/genReport.bat
echo "REM    --locale/-l <locale>" >> $2/genReport.bat
echo "REM    --parameter/-p <\"parameterName=parameterValue\">" >> $2/genReport.bat
echo "REM    --file/-F <parameter file>" >> $2/genReport.bat
echo "REM    --encoding/-e <target encoding>" >> $2/genReport.bat
echo "REM" >> $2/genReport.bat
echo "REM Locale: default is English" >> $2/genReport.bat
echo "REM parameters in command line will overide parameters in parameter file" >> $2/genReport.bat
echo "REM parameter name cannot include characters such as \' \'\, \'=\'\, \':\'" >> $2/genReport.bat
echo "REM"  >> $2/genReport.bat
echo "REM For RUN mode:" >> $2/genReport.bat
echo "REM we should add it in the end <design file>" >> $2/genReport.bat
echo "REM    --output/-o <target file>" >> $2/genReport.bat
echo "REM    --locale/-l <locale>" >> $2/genReport.bat
echo "REM    --parameter/-p <parameterName=parameterValue>" >> $2/genReport.bat
echo "REM    --file/-F <parameter file>" >> $2/genReport.bat
echo "REM" >> $2/genReport.bat
echo "REM Locale: default is English" >> $2/genReport.bat
echo "REM parameters in command line will overide parameters in parameter file" >> $2/genReport.bat
echo "REM parameter name cannot include characters such as \' \'\, \'=\'\, \':\'" >> $2/genReport.bat
echo "REM" >> $2/genReport.bat
echo "REM For RENDER mode:" >> $2/genReport.bat
echo "REM we should add it in the end <design file>" >> $2/genReport.bat
echo "REM    --output/-o <target file>" >> $2/genReport.bat
echo "REM    --page/-p <pageNumber>" >> $2/genReport.bat
echo "REM    --locale/-l <locale>" >> $2/genReport.bat
echo "REM" >> $2/genReport.bat
echo "REM Locale: default is English" >> $2/genReport.bat
echo "REM ################## USAGE OF REPORT ENGINE END ################" >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo REM set common variables >> $2/genReport.bat
echo SET BIRT_HOME=%~dp0\\platform >> $2/genReport.bat
echo SET BIRT_API=%~dp0\\lib\\actuate-birt-api.jar >> $2/genReport.bat
echo SET WORK_DIR=%~dp0 >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo ECHO BIRT_HOME=%BIRT_HOME% >> $2/genReport.bat
echo ECHO BIRT_API=%BIRT_API% >> $2/genReport.bat
echo ECHO WORK_DIR=%WORK_DIR% >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo IF not \"%BIRT_HOME%\" == \"\" GOTO runBirt >> $2/genReport.bat
echo ECHO \"BIRT_HOME must be set before ReportRunner can run\" >> $2/genReport.bat
echo GOTO end >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo :runBirt >> $2/genReport.bat 
echo "" >> $2/genReport.bat

echo SET java.io.tmpdir=%WORK_DIR%\\tmpdir >> $2/genReport.bat
echo SET org.eclipse.datatools_workspacepath=%java.io.tmpdir%\\workspace_dtp >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo IF not exist %java.io.tmpdir% mkdir %java.io.tmpdir% >> $2/genReport.bat
echo IF not exist %org.eclipse.datatools_workspacepath% mkdir %org.eclipse.datatools_workspacepath% >> $2/genReport.bat
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

echo %JAVACMD% -cp \"%BIRT_API%\" -DBIRT_HOME=\"%BIRT_HOME%\" org.eclipse.birt.report.engine.api.ReportRunner %p1% %p2% %p3% %p4% %p5% %p6% %p7% %p8% %p9% %p10% %p11% %p12% %p13% %p14% %p15% %p16% %p17% %p18% %p19% >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo :end >> $2/genReport.bat
