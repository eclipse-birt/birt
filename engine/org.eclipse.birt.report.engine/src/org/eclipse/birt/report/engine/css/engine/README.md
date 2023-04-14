# Eclipse BIRT 
Introduction to generate new version of the PerfectHash-class and the corresponding StyleConstants.

## Reason
The BIRT-designer needs for further new designer properties the according property values into files:
* PerfectHash.java
* StyleConstants.java

After the whole process both files have to include the designer-properties with the same index numbers at property side.
The follwoing steps will be introduce which steps are necessary to create the correct files.

## Processing steps
The target of the following steps is to create the correct versions of PerfectHash.java and StyleConstants.java.
The files are lockated at the directory:
* engine/org.eclipse.birt.report.engine/src/org/eclipse/birt/report/engine/css/engine: 

Additional files which are required for the correct creation:
* token.gperf - input file, ASCII encoding, Linux line ending required
* token.cpp - generated source file like base template for PerfectHash.java

###1. Step: Create the word-list of properties based on PerfectHash.java
* Copy the wordlist from PerfectHash.java into a text editor
* Sort the word list and modified it with a good editor (e.g. Notepad++)
* Save the text file as token.gperf. Be aware that the line end is linux based end (line feed = \n).
* Remain the original command lines at the top of the original file.

###2. Step: Build the source template token.cpp
* Required is the build of the intermediate file token.cpp with a working version of the GNU gperf utility.
* Under MS Windows and option would is "cygwin" (https://www.cygwin.com/install.html). This tool is an Unix terminal command line simulator.
* Open a command line, inside the directory containing token.gperf
* Execute the following command line:
	gperf -d -7 -l -D -L C++ token.gperf > token.cpp
or used by path settings
	gperf -d -7 -l -D -L C++ /home/token.gperf > /home/token.cpp
	
* The result is the created token.cpp like an intermediate file.

###3. Step: Renew the java-files
* The next step is to copy texts from token.cpp into PerfectHash.java.
* The copied parts are oriented of the given structure of the original java-file and token.cpp. The files have a very similar structure.
* At last update the StyleConstants.java with the integer values of the properies. Match the indexes of the corresponding words in the wordlist array in PerfectHash.java.
  Fortunately, the list contains the indexes as comments.
