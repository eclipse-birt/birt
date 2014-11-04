#!/bin/bash

######################
#rename compile log
######################

find $1 -name "*.html" > $1/compile.log

awk -F "/" '{print "cp " $0 " "$1 FS $2 FS $3 FS $4 FS $5 FS $6 FS $7 FS $8 FS $9 FS $10 FS"_compilelog.html"  }' $1/compile.log >  $1/plugins.rename
chmod +x $1/plugins.rename
$1/plugins.rename



echo $1
find $1 -name "*.html" -exec grep "ERROR&nbsp;in" -c {} \; -print > $1/error.plugins

awk ' /\/org/ {print $1}' $1/error.plugins > $1/error.plugins.tmp
mv $1/error.plugins.tmp  $1/error.plugins

######################################
#Get compile error plugins total count
######################################

wc -l $1/error.plugins > $1/error.plugins.count
count=`awk '{print $1}' $1/error.plugins.count`

#############################################
#found compile error, generate notify list
#############################################

if [ $count -gt 0 ] ; then
        echo "Compile has error in $1"
        awk -F "/" '{print "cp " $0 " "$1 FS $2 FS $3 FS $4 FS $5 FS $6 FS $7 FS $8 FS $9 FS $10 "_compilelog.html"  }' $1/error.plugins >  $1/error.plugins.rename
        chmod +x $1/error.plugins.rename
        $1/error.plugins.rename

        #set error plugins or notification
        awk -F "/" '{print $10}' $1/error.plugins  > $1/notify.list
        awk -F "_" '{print " "$1"\\n\\"}' $1/notify.list > $1/notify.list.tmp
        mv $1/notify.list.tmp $1/notify.list

        echo "compileHasError=true" >> /home/adb/releng.230/BIRTBuilder/monitor.properties
        echo "error.plugin.list=\\" >> /home/adb/releng.230/BIRTBuilder/monitor.properties
        cat $1/notify.list >> /home/adb/releng.230/BIRTBuilder/monitor.properties
        echo " "  >> /home/adb/releng.230/BIRTBuilder/monitor.properties
else
        echo "No compile error in $1"
        echo "compileHasError=false" >> /home/adb/releng.230/BIRTBuilder/monitor.properties
fi
