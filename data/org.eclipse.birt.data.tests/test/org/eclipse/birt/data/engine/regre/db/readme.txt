				DtE regression test suite readme

Purpose:
	We provide this test suite to avoid the unexpected bug introduction.
	
Usage:
1.Get drivers:
	Due to licence issue, Driver libs can not be checked into Eclipse CVS. 
	Please manually download these driver libs from the URLs below.
	In some cases, downloads are available for no fee, but require registration.
	Actuate stuff can simply download from P4 under directory //Actuate/JRP/QA/TestEnv/DtERegressionTest/lib. 
	The following is a list of Drivers and their downloading address:
	db2jcc.jar
	db2jcc_license_c.jar
	  http://www14.software.ibm.com/webapp/download/preconfig.jsp?id=2004-09-21+14%3A20%3A09.014993R&S_TACT=104CBW71&S_CMP=&s=

	db2jcc_license_cu.jar
	  http://www14.software.ibm.com/webapp/download/preconfig.jsp?id=2004-09-20+10%3A09%3A21.003415R&cat=database&fam=&s=c&S_TACT=104AH%20W42&S_CMP=

	derby.jar
	derbynet.jar
	  http://incubator.apache.org/derby/derby_downloads.html#Snapshot+Jars

	ifxjdbc.jar
	  http://www14.software.ibm.com/webapp/download/preconfig.jsp?id=2005-03-09+16%3A56%3A12.268164R&S_TACT=104CBW71&S_CMP=&s=

	jtds-0.9.jar
	  http://sourceforge.net/project/showfiles.php?group_id=33291&package_id=25350

	mysql-connector-java-3.0.16-ga-bin.jar
	  http://dev.mysql.com/downloads/connector/j/3.0.html

	ojdbc14.jar
	  http://www.oracle.com/technology/software/tech/java/sqlj_jdbc/htdocs/jdbc9201.html

	postgresql-8.0-310.jdbc3.jar
	  http://jdbc.postgresql.org/download.html#jars
	
2.Copy driver lib
	Put these libs into org.eclipse.birt.data/test/plugins/org.eclipse.birt.report.data.oda.jdbc/drivers.

3.Set Configuration Info
	User who can not access our sample DB has to modify the default configuration,
	specify the Driver, URL, user name and password for the DBMS that he wants to connect.

4.Run test suite
	AllRegressionTests -> Run -> JUnit Test
