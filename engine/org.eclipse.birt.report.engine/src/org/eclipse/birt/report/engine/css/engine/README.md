# Eclipse BIRT 
Introduction to generate new version of the PerfectHash class and the corresponding StyleConstants.

## Reason
Accessing properties is optimized in BIRT.
The idea is to avoid costly string comparisons if possible.
To achieve this goal, BIRT uses a perfect hash and a utility class.
These are implemented in two classes:
* PerfectHash.java
* StyleConstants.java

In order to add new properties, these classes must be extended. The process is described below.
After the whole process both files have to include the properties with the same index numbers at property side.

## Processing steps
The target of the following steps is to create the correct versions of `PerfectHash.java` and `StyleConstants.java`.
The files are located at the directory:
* engine/org.eclipse.birt.report.engine/src/org/eclipse/birt/report/engine/css/engine: 

Additional files which are required for the correct creation:
* `token.gperf` - input file, ASCII encoding, Linux line ending required
* `token.cpp` - generated source file like base template for PerfectHash.java

### Step 1: Create the word-list of properties based on `PerfectHash.java`
* Copy the wordlist from PerfectHash.java into a text editor
* Sort the word list and modify it with a good editor (e.g. Notepad++)
* (For new properties, add the property names to the text)
* Save the text file ASCII-encoded as `token.gperf`. Be aware that the line end is linux based end (line feed = \n).
* Restore the comment lines at the top of the file from the original `token.perf`.

### Step 2: Build the source `template token.cpp`
* Required is the build of the intermediate file `token.cpp` with a working version of the GNU gperf utility.
* Under MS Windows one option is [cygwin] (https://www.cygwin.com/install.html). This tool is an Unix terminal command line simulator.
* Open a command line and CD into the directory containing `token.gperf`
* Execute the following command line:

    gperf -d -7 -l -D -L C++ token.gperf > token.cpp

* This will overwrite the file `token.cpp`.

### Step 3: Renew the Java source files
* The next step is to copy the arrays and numbers from `token.cpp` into `PerfectHash.java`. This should be obvious, if you look at the original versions of both files - they have a very similar structure.
* Finally, update the file `StyleConstants.java` with the integer values of the properties. Match the indexes of the corresponding words in the wordlist array in `PerfectHash.java`.
  Fortunately, the list contains the indexes as comments.

(If you introduced new properties, add a corresponding entry to `StyleConstants.java`)

Example for the relation between `StyleConstants.java` and `PerfectHash.java`:

StyleConstants.java:

    int STYLE_MARGIN_TOP = 6;
    
PerfectHash.java:

	static String wordlist[] = { ...
			"border-diagonal-color" /* hash value = 21, index = 7 */,
			...


