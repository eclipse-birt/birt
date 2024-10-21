#!/bin/bash

# Copyright (c) 2011, 2024, Actuate, Remain Software and others
# 
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License 2.0 which is available at
# https://www.eclipse.org/legal/epl-2.0/.
#
# SPDX-License-Identifier: EPL-2.0

set -e

# Get the directory of the script
THIS_DIR=$(dirname "$(realpath "$0")")

# Usage function
usage() {
  echo
  echo "genReport.sh calls the BIRT ReportRunner"
  echo "example: ./genReport.sh -f PDF -o hello.pdf -p \"paramList=1\" -p \"paramInteger=123\" -p \"paramString=\\\"Hello,Birt\\\"\" samples/hello_world.rptdesign"
  echo "--help this help"
  echo
  echo "org.eclipse.birt.report.engine.impl.ReportRunner Usage:"
  echo "--mode/-m [ runrender | render | run ] the default is runrender"
  echo
  echo "For runrender mode:"
  echo "    --help runrender"
  echo "    --format/-f [ HTML | PDF ]"
  echo "    --output/-o <target file>"
  echo "    --htmlType/-t < HTML | ReportletNoCSS >"
  echo "    --locale/-l <locale>"
  echo "    --parameter/-p <\"parameterName=parameterValue\">"
  echo "    --file/-F <parameter file>"
  echo "    --encoding/-e <target encoding>"
  echo
  echo "Locale: default is English"
  echo "parameters in command line will override parameters in parameter file"
  echo "parameter name cannot include characters such as ' ', '=', ':'"
  echo
  echo "For RUN mode:"
  echo "    --help run"
  echo "    --output/-o <target file>"
  echo "    --locale/-l <locale>"
  echo "    --parameter/-p <\"parameterName=parameterValue\">"
  echo "    --file/-F <parameter file>"
  echo
  echo "Locale: default is English"
  echo "parameters in command line will override parameters in parameter file"
  echo "parameter name cannot include characters such as ' ', '=', ':'"
  echo
  echo "For RENDER mode:"
  echo "    --help render"
  echo "    --output/-o <target file>"
  echo "    --page/-p <pageNumber>"
  echo "    --locale/-l <locale>"
  echo
  echo "Locale: default is English"
}

# Run function
run() {
  # Set common variables
  WORK_DIR="$THIS_DIR"

  if [[ -d "$THIS_DIR/platform" ]]; then
    BIRT_HOME="$THIS_DIR/platform"
  fi

  if [[ -n "$BIRT_HOME" ]]; then
    echo "OSGI-Mode, BIRT_HOME=$BIRT_HOME"
  fi

  # Set temporary directories
  export java_io_tmpdir="$WORK_DIR/tmpdir"
  export org_eclipse_datatools_workspacepath="$java_io_tmpdir/workspace_dtp"

  mkdir -p "$java_io_tmpdir"
  mkdir -p "$org_eclipse_datatools_workspacepath"

  # Set BIRT class path
  BIRTCLASSPATH="$THIS_DIR/lib/*"

  # Set Java command
  JAVACMD="java"

  # Execute the command
  echo "Java command=$JAVACMD -cp \"$BIRTCLASSPATH\" -Djava.awt.headless=true -DBIRT_HOME=$BIRT_HOME org.eclipse.birt.report.engine.api.ReportRunner $@"
  $JAVACMD -cp "$BIRTCLASSPATH" -DBIRT_HOME="$BIRT_HOME" org.eclipse.birt.report.engine.api.ReportRunner "$@"
}

# Handle arguments
if [[ -z "$1" || ( "$1" == "--help" && -z "$2" ) ]]; then
  usage
  exit 0
else
  run "$@"
fi

exit 0

