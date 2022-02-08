/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.framework;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines an implementation of IPlatformConfig to access OSGi framework.
 * implements the interface IPlatformConfig
 */
public class PlatformConfig implements IPlatformConfig {

	/**
	 * the properties that needed when platfrom is running it's an instance of
	 * HashMap
	 */
	protected HashMap properties = new HashMap();

	public Object getProperty(String name) {
		return properties.get(name);
	}

	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}

	public HashMap getProperties() {
		return properties;
	}

	public String getBIRTHome() {
		Object birtHome = properties.get(BIRT_HOME);
		if (birtHome instanceof String) {
			return (String) birtHome;
		}
		return null;
	}

	public void setBIRTHome(String birtHome) {
		properties.put(BIRT_HOME, birtHome);
	}

	public String[] getOSGiArguments() {
		Object arguments = properties.get(OSGI_ARGUMENTS);
		if (arguments instanceof String[]) {
			return (String[]) arguments;
		}
		return null;
	}

	public void setOSGiArguments(String[] arguments) {
		properties.put(OSGI_ARGUMENTS, arguments);
	}

	/**
	 * set the configuration used by the OSGi framework. The configuration includes
	 * all valid osgi configs except some reserved as follow:
	 * <li>1. osgi.install.area, using the one defined by BIRT_HOME.</li>
	 * <li>2. eclipse.ignoreApp, be true.</li>
	 * <li>3. osgi.noShutDown, be true.</li>
	 * <li>4. osgi.framework, using the one defined by BIRT_HOME</li>
	 * <li>5. osgi.framework.useSystemProperties, be false.</li>
	 * 
	 * @param osgiConfigMap
	 */
	public void setOSGiConfig(Map osgiConfigMap) {
		properties.put(OSGI_CONFIGURATION, osgiConfigMap);
	}

	public Map getOSGiConfig() {
		Object config = properties.get(OSGI_CONFIGURATION);
		if (config instanceof Map) {
			return (Map) config;
		}
		return null;
	}

	public IPlatformContext getPlatformContext() {
		Object context = properties.get(PLATFORM_CONTEXT);
		if (context instanceof IPlatformContext) {
			return (IPlatformContext) context;
		}
		return null;
	}

	public void setPlatformContext(IPlatformContext context) {
		properties.put(PLATFORM_CONTEXT, context);
	}

	/**
	 * sets the directory for temporary files
	 * 
	 * @param tmpDir the directory for temporary files
	 */
	public void setTempDir(String tmpDir) {
		setProperty(TEMP_DIR, tmpDir);
	}

	/**
	 * returns engine temporary directory for temporary files
	 * 
	 * @return Returns the Temp Directory for engine to write temp files
	 */
	public String getTempDir() {
		String tempDir = (String) getProperty(TEMP_DIR);
		if (tempDir == null) {
			return AccessController.doPrivileged(new PrivilegedAction<String>() {

				public String run() {
					return System.getProperty("java.io.tmpdir");
				}
			});
		}
		return tempDir;
	}
}
