# Eclipse BIRT 
Introduction to use the configuration for the JDBC driver manager.

## Reason
The JDBC driver manager handle the load process of the configured JDBC database driver.
Each driver use an own set of supported connection properties to handle the communication with the database.

BIRT supports a standard set of connection properties which will be exchanged to the JDBC driver.
The driver which fulfill the requirements of Oracle Database Appliance (ODA) will support all BIRT connection properties.
For a qualified JDBC driver it isn't required to fulfill the ODA requirements or support all of these properties (e.g. the BiDi support).

For this reason BIRT validate the driver information to exchange only the supported connection properties to the driver.
The driver validation can be configured with 2 options at JVM level.

### JVM configuration for the connection property validation of JDBC driver

The configuration will be done as a global starting parameter of the JVM.

**birt.driver.property.validation.enabled**

	Content    	Configuration to enable or disable the connection property validation of JDBC drivers
	Parameter  	-Dbirt.driver.property.validation.enabled
	Location   	JVM
	Data type  	boolean
	Values     	true, the connection property validation is enabled
	           	false, the connection property validation is disabled
	Supported   	true, false
	Default    	true
	Version    	4.17


**birt.driver.property.validation.excluded.drivers**

	Content    	Configuration to exclude drivers from the validation based on the driver class name.
				  	The parameter can include a list of JDBC drivers concatenated with comma. 
	Parameter  	-Dbirt.driver.property.validation.excluded.drivers
	Location   	JVM
	Data type  	string
	Values     	driver class name, list of driver class names delimited with comma
	Supported   	driver class name
	Default    	unset, all drivers will be validated
	Example 01 	-Dbirt.driver.property.validation.excluded.drivers=com.microsoft.sqlserver.jdbc.SQLServerDriver
	Example 02 	-Dbirt.driver.property.validation.excluded.drivers=org.duckdb.DuckDBDriver,com.microsoft.sqlserver.jdbc.SQLServerDriver
	Version    	4.17

