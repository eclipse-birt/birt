#!/bin/sh

################ USAGE OF REPORTRUNNER #########################
# org.eclipse.birt.report.engine.impl.ReportRunner Usage:
# --mode/-m [ run | render | runrender] the default is runrender
# for runrender mode:
# we should add it in the end <design file>
#    --format/-f [ HTML | PDF ]
#    --output/-o <target file>
#    --htmlType/-t < HTML | ReportletNoCSS >
#    --locale/-l<locale>
#    --parameter/-p <"parameterName=parameterValue">
#    --file/-F <parameter file>
#    --encoding/-e <target encoding>
#
# Locale: default is English
# parameters in command line will overide parameters in parameter file
# parameter name cannot include characters such as \' \'\, \'=\'\, \':\'
#
# For RUN mode:
# we should add it in the end <design file>
#    --output/-o <target file>
#    --locale/-l<locale>
#    --parameter/-p <parameterName=parameterValue>
#    --file/-F <parameter file>
#
# Locale: default is English
# parameters in command line will overide parameters in parameter file
# parameter name cannot include characters such as \' \'\, \'=\'\, \':\'
#
# For RENDER mode:
# we should add it in the end <design file>
#    --output/-o <target file>
#    --page/-p <pageNumber>
#    --locale/-l<locale>
#
# Locale: default is English
################ END OF USAGE #########################

# echo set common variables
#export BIRT_HOME=BIRT-runtime_install_dir
BIRT_HOME=""
WORK_DIR=$PWD
# class path : don't forget to add your connection drivers in the classpath directory
BIRTCLASSPATH=$BIRT_HOME/ReportEngine/lib/*


echo BIRT_HOME=$BIRT_HOME
echo WORK_DIR=$WORK_DIR
echo BIRTCLASSPATH=$BIRTCLASSPATH

if [ "$BIRT_HOME" = "" ]

then
echo "BIRT_HOME must be set before ReportRunner can run";
else

java_io_tmpdir=$WORK_DIR/tmpdir
org_eclipse_datatools_workspacepath=$java_io_tmpdir/workspace_dtp
mkdir -p $org_eclipse_datatools_workspacepath

# JAVA version >= 17
JAVACMD='java';
$JAVACMD -Djava.awt.headless=true -cp "$BIRTCLASSPATH" -DBIRT_HOME="$BIRT_HOME" -Dorg.eclipse.datatools_workspacepath="$org_eclipse_datatools_workspacepath" org.eclipse.birt.report.engine.api.ReportRunner ${1+"$@"}

fi
