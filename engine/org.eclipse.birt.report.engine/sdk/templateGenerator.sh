#!/bin/sh
###
# $1 template folder
# $2 template header path
# $3 result template path
###

###########################
# filtet the comment line
###########################
cp $1/mf.template  $1/bbb
awk -F "#" '{if($1!="") print  $1 }' $1/mf.template > $1/mf.template.tmp

###########################
# filtet the comment line
###########################
awk -F "." '{if($1!="" && $1!=" ") print $0 }' $1/mf.template.tmp > $1/mf.template

###########################
# filtet the last comma(,)
###########################
wc -l $1/mf.template > $1/mf.template.count
count=`awk '{print $1}' $1/mf.template.count`
countHead=`expr $count - 1`
echo $countHead

head -$countHead $1/mf.template > $1/mf.template.head
tail -1 $1/mf.template | sed 's/,//' >> $1/mf.template.head


mv $1/mf.template.head $1/mf.template

rm -f $1/mf.template.count
rm -f $1/mf.template.tmp

###########################
# Generate manifest
###########################

cat $2 > $3
cat $1/mf.template >> $3

