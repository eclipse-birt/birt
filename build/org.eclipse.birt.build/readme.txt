BIRT Verisoning Tools 
===========================

BIRT Versioning Tools is used for versioning control in current BIRT nightly build. 
It is an extended ant task and should be used with ant script. 

How to compile this tools
--------------------------

1.Checkout project org.eclipse.birt.build using anonymous account.
2.Switch to command mode, change current dir to org.eclipse.birt.build
3.Download dom4j jars from: 
  http://sourceforge.net/projects/dom4j/files/ 
  Copy the dom4j jars to org.eclipse.birt.build/lib
4.Running ant command to start the auto build: ant jar
  Result folder 'bin' and zipped jar file will generated under org.eclipse.birt.build
Note: 
*The tools needs to be build on JDK 1.5
*Please make sure cvs tools has been installed on your machine


How to integrate this tool with ant script
------------------------------------------

Ant script template are in example folder of project.
1.Checkout the example folder under org.eclipse.birt.build. It contains 2 example plugins,2 example features and their version control files.
  In this demo, Feature A includes Feature B and plugin A and Feature B only include plugin B.
2.Switch to command mode, change current dir to org.eclipse.birt.build/example.
3.Running ant command without any param to see the help info
4.Running ant task GenerateCvsLog to generate cvs diff log for demo plugins:
  >ant GenerateCvsLog
  After building, 2 XX-changelog.xml file will be generated under current folder, which will be used in next step.
5.Running ant task UpdatePluginVersion to update the Bundle Version of demo plugins:
  >ant UpdatePluginVersion
  With successful message, MANIFEST.MF in demo plugins should have been updated. 
  And new version control file will be generated under NewVersionControl folder, which should be checked into CVS for next day's build.
  
6.Running ant task UpdateFeatureVersion to update the Feature version:
  >ant UpdateFeatureVersion
  With successful message, feature.xml in each demo should have been updated.
Note:
Default value used in the build template can be replaced by custom setting according to your environment.


Defination of VersionUpdate's Parameter
-----------------------------------

Defined VersionUpdate task has 7 parameter need to be set value:
1.projectPath -- Absolute filepath of the plugin which need to be updated
2.suffix -- New BuildId (.vYYYYMMDD-MMSS)
3.cvsLogPath -- Absolute filename of generated cvs difference log file
4.logPath -- Absolute folder path where the new generated cvs version control file should be stored.
5.plugId -- Plugin Id whose version need to be updated
6.checkFlag -- This flag indicates if the plugin need to have version control. (value: Y/N)
7.cvsControlPath -- Absolute version control filename of the plugin.


Schema of version control file
------------------------------

There is two entry in the version control file:
<LastDate> 
Represents the suffix used in last build. If the codes hasn't changed, this part will keep same and MANIFEST will be updated with this value.
<DayInPast>
This param will be used in cvschanglog task, which represents how many days the bits havn't changed. 
If they were changed, this value will be reset to 1, or it will be repaced by <current value>+1.
Note: 
All the changes will be seen in the generated cvs control file locates in ${logPath}

  