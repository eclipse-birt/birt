#!/bin/bash

find $1/plugins/org.eclipse.birt.* -name "*.jar" > $1/thirdPartyJar.log
diff $1/thirdPartyJar.log $2/thirdPartyJar.log.template > $1/thirdPartyJarDiff.log

