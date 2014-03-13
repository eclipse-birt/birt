workspace=releng.440
pde_build_version=3.7.0.v20110512-1320

rm -rf $workspace/src/plugins/*
rm -rf $workspace/src/updateJars
rm -rf $workspace/src/features/*
rm -rf $workspace/src/maps/*
rm -rf $workspace/src/source
rm -rf $workspace/src/*tmpsite* $workspace/src/*.properties $workspace/src/*.xml $workspace/src/nestedJars
rm -rf $workspace/src/buildRepo rm -rf $workspace/src/rootfiles
rm -f $workspace/src/*.zip
rm -f $workspace/src/*.log
rm -f $workspace/src/*.txt
rm -f $workspace/src/*.jar
rm -f  $workspace/org.eclipse.birt.releng.birtbuilder/monitor.properties
rm -f  $workspace/org.eclipse.birt.releng.birtbuilder/monitor.log
rm -f downloads/4_4_0/*.zip
rm -f downloads/4_4_0/*.txt
rm -f downloads/4_4_0/*.gz

rm -rf $workspace/src/scmCache
rm -rf $workspace/org.eclipse.releng.basebuilder/plugins/org.eclipse.pde.build_$pde_build_version/scripts/productBuild/scmCache/*
rm -rf $workspace/org.eclipse.releng.basebuilder/plugins/org.eclipse.pde.build_$pde_build_version/scripts/scmCache/*
rm -rf  $workspace/org.eclipse.birt.releng.birtbuilder/eclipse/buildConfigs/birt.rcp/scmCache/*

#CLEAN TEMP FOLDER FOR UNIT TEST
rm -rf /tmp/DataEngine_*
rm -rf /tmp/outputtestData*
rm -f /tmp/BasicDiskList*
rm -f /tmp/*.png
rm -f /tmp/*.jpg
rm -f /tmp/design*
rm -rf /tmp/BIRTSampleDB_*
rm -rf /tmp/org.eclipse.birt.report.model*
rm -rf /tmp/DTETest*

#ant -f prepare.xml -logfile prepareBuilder.log

