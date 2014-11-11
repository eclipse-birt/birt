#Disk Space Check srcipt before build
#if available size < 2097152 KB (2 GB = 1024 * 1024 *2), send build cancel notification
#if available size < 7340032 KB (7 GB = 1024 * 1024 *7), send disk warning notification

export DiskUse=`df -P | awk '{print $4}' | sed -n '2p'`
if [ ${DiskUse} -lt 2097152 ]
then
  ant -f $builderDir/eclipse/helper.xml NoDiskSpace 
  exit
elif [ ${DiskUse} -lt 7340032 ]
then
 ant -f $builderDir/eclipse/helper.xml DiskSpaceWarning 
fi

