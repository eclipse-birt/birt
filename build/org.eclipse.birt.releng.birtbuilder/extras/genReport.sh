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
#    --parameter/-p <parameterName=parameterValue>
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
export BIRT_HOME=$PWD/platform
export WORK_DIR=$PWD

echo BIRT_HOME=$BIRT_HOME
echo WORK_DIR=$WORK_DIR

java_io_tmpdir=$WORK_DIR/tmpdir
org_eclipse_datatools_workspacepath=$java_io_tmpdir/workspace_dtp
mkdir -p $org_eclipse_datatools_workspacepath

BIRTCLASSPATH=

for filename in $WORK_DIR/lib/*.jar; do
    BIRTCLASSPATH="$BIRTCLASSPATH:$filename"
done

java -classpath "$BIRTCLASSPATH" -Djava.awt.headless=true -DBIRT_HOME="$BIRT_HOME" -Dorg.eclipse.datatools_workspacepath="$org_eclipse_datatools_workspacepath" org.eclipse.birt.report.engine.api.ReportRunner $@
