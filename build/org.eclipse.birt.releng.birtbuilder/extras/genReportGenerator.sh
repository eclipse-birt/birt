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

echo if [ \"\$BIRT_HOME\" == \"\" ]\; > $2/genReport.sh
echo "" >> $2/genReport.sh

echo then >> $2/genReport.sh
echo echo \" The BIRT_HOME need be set before BirtRunner can run.\"\; >> $2/genReport.sh
echo else >> $2/genReport.sh
echo "" >> $2/genReport.sh


awk  'NR==1 {printf "export BIRTCLASSPATH=\"$BIRT_HOME/ReportEngine/lib/"$1}'  lib.path >> $2/genReport.sh
awk  'NR>=2  {printf ":$BIRT_HOME/ReportEngine/lib/"$1}'  lib.path >> $2/genReport.sh
echo \" >> $2/genReport.sh

echo "" >> $2/genReport.sh
echo JAVACMD=\'java\'\; >> $2/genReport.sh

echo  \$JAVACMD -cp \"\$BIRTCLASSPATH\" -DBIRT_HOME=\"\$BIRT_HOME/ReportEngine\" org.eclipse.birt.report.engine.api.ReportRunner \$1 \$2 \$3 \$4 \$5 \$6 \$7 \$8 \$9 \${10} \${11} \${12} \${13} \${14} \${15} \${16} \${17} \${18} \${19} >> $2/genReport.sh
echo "" >> $2/genReport.sh
echo fi >> $2/genReport.sh

chmod +x $2/genReport.sh

########################################
#Generate $2/genReport.bat
########################################

echo ECHO off > $2/genReport.bat
echo "" >> $2/genReport.bat

echo IF not \"%BIRT_HOME%\" == \"\" GOTO runBirt >> $2/genReport.bat
echo   ECHO \"Please set BIRT_HOME first.\" >> $2/genReport.bat
echo   GOTO end >> $2/genReport.bat
echo :runBirt >> $2/genReport.bat 

echo "" >> $2/genReport.bat
echo "" >> $2/genReport.bat

echo REM set the birt class path. >> $2/genReport.bat

awk  'NR==1 {printf "SET BIRTCLASSPATH=%BIRT_HOME%\\ReportEngine\\lib\\"$1}'  lib.path >> $2/genReport.bat
awk  'NR>=2  {printf ";%BIRT_HOME%\\ReportEngine\\lib\\"$1}'  lib.path >> $2/genReport.bat
echo \; >> $2/genReport.bat

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
