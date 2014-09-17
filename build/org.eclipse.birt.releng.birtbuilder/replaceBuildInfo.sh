# Input Arguments:
# 
#

workspace=/home/adb/releng.441

rm -f $workspace/PackageFiles/template/build_info_template_new.txt

sed "s/#build_time#/$1 $2/g" $workspace/PackageFiles/template/build_info_template.txt > build_info_template_new.txt

sed "s/#univBDate#/$3 $4 $5 $6 $7 $8 $9/g" build_info_template_new.txt > $workspace/PackageFiles/template/build_info_template_new.txt
rm -f build_info_template_new.txt
