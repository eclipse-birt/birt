# Eclipse BIRT 
Introduction to use the configuration of the JavaScript language version.

## Reason
The JavaScript engine of BIRT is based on the Rhino engine. The default language version of the Rhino engine is JavaScript version 1.6. Rhino supports different language versions of JavaScript including the latest version ECMAScript 6.

On **BIRT** side the Rhino engine will be run with the latest version **ECMAScript 6**.
To be compatible with earlier JavaScript versions a global system property is given which can be set at JVM level to change the JavaScript language version.

All supported JavaScript language versions of the Rhino engine can be configured.

### JVM configuration of the JavaScript language version

The configuration will be done as a global starting parameter of the JVM.

**birt.ecmascript.version**

	Content    	configuration of the JavaScript language version
	Parameter  	-Dbirt.ecmascript.version
	Location   	JVM
	Data type  	string
	Values     	value of the supported language version
	Supported   	1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, ES6
	Default    	ES6 (= EMCAScript 6)
	Version    	4.16


**birt.ecmascript.security.enabled**

	Content    	configuration to enable or disable the JavaScript security based on certificates (as replacement of the Security Manager)
	Parameter  	-Dbirt.ecmascript.security.enabled
	Location   	JVM
	Data type  	string
	Values     	on, the JavaScript code use security mechanism certificate based
	           	off, JavaScript without special JavaScript mechanism
	Supported   	on, off
	Default    	off
	Version    	4.17

** Function reference of Rhino**

- An overview of Rhino engine supported functions are listed here: [Rhino ES2015 Support](https://mozilla.github.io/rhino/compat/engines.html)


- The Rhino engine will be integrated at BIRT through the Orbit-project: [Orbit Aggregation Summary](https://download.eclipse.org/tools/orbit/simrel/orbit-aggregation/table.html)