#!/bin/sh


#Due to the Internet connection problem, 
#replace the http://www.eclipse.org/jdt/core/compiler_32_002.dtd with local DTD defination
#Input argument: $1 -- compilefolder
# 

find $1 -name "*.xml" > compilelog.list

awk '{print "sed \"s/http:\\/\\/www.eclipse.org\\/jdt\\/core\\/compiler_32_002.dtd/http:\\/\\/qabee:8080\\/BuildCentral\\/compiler_32_002.dtd/g\" " $0  "> tmplog ; mv tmplog " $1    }' compilelog.list > compilelog.sh

chmod +x compilelog.sh

./compilelog.sh

rm -f compilelog.sh
rm -f compilelog.list

