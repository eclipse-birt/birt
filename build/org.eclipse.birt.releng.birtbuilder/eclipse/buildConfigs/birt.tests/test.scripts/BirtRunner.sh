usage="usage: $0 start|help"

if [ $# -lt 1 ]
then
 echo >&2 "$usage"
 exit 1
fi

if [ $1 == "help" ]
then

    echo >&2 "===================================================================="
    echo >&2 "NOTE:"
    echo >&2 " 1.Make sure Ant and JDK1.5 are installed on your linux environment"
    echo >&2 " 2.Download birt-report-designer-all-in-one-2_2_1-linux-gtk.tar.gz,"
    echo >&2 "            birt-tests-suite-2_2_1.zip,"
    echo >&2 "            and clipse-test-framework-3.3M4.zip"
    echo >&2 " 3.Extracted to same folder as ""$""{test.home} eg. /home/adb/unittest"
    echo >&2 " 4.Change ""$""{test.home}, ""$""{jvm.15.home} settting in"
    echo >&2 "          ""$""{test.home}/BirtRunner.properties"
    echo >&2 " 5.Use BirtRunner.sh start to run the BIRT test.
    echo >&2 "===================================================================="
    exit 1
fi

if [ $1 == "start" ]
then
    ant -f BirtRunner.xml -propertyfile BirtRunner.properties -logfile BirtTest.log
else
    echo >&2 "$usage"
    exit 1
fi
