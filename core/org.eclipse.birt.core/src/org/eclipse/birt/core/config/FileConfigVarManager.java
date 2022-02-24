/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.core.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Defines a configuration manager class that loads configuration variables from
 * a file. In case the file does not contain a specified config var, the system
 * config var manager is used as backup.
 */
public class FileConfigVarManager extends SystemConfigVarManager {

	/**
	 * Configuration variables retrieved from file
	 */
	protected Properties fileConfigVars;

	/**
	 * configuration file name
	 */
	protected String configFileName;

	/**
	 * is the configuration file loaded
	 */
	protected boolean configFileLoaded = false;

	/**
	 * Constructor
	 */
	public FileConfigVarManager() {
		super();
		configFileName = getDefaultConfigFileName();
	}

	/**
	 * @param configFileName the configuration file name
	 */
	public FileConfigVarManager(String configFileName) {
		super();
		this.configFileName = configFileName;
	}

	/**
	 * gets the default configuration file location. The file is in the same
	 * directory as birtcore.jar.
	 * 
	 * @return the default configuration file name
	 */
	private String getDefaultConfigFileName() {
		// use the following code if we want to use class loaded
		// ClassLoader loader = Thread.currentThread().getContextClassLoader();
		// is = loader.getResourceAsStream(DEFAULT_PROPERTIES);
		// TODO add code to calculate default config file name

		return null;
	}

	/**
	 * load configuration file
	 */
	synchronized private void load() {
		if (configFileLoaded)
			return;

		fileConfigVars = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(configFileName);
			if (is != null)
				fileConfigVars.load(is);
		} catch (Exception e) // IOException or FileNotFoundException
		{
			fileConfigVars = null;
			// Log me, then neglect the exception
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception ex) {
					// do nothing
				}
				is = null;
			}
		}

		configFileLoaded = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.core.config.IConfigVarManager#getConfigVar(java.lang.String)
	 */
	public String getConfigVar(String key) {
		String ret = null;
		if (!configFileLoaded)
			load();

		if (fileConfigVars != null) {
			ret = fileConfigVars.getProperty(key);
			if (ret == null)
				ret = System.getProperty(key);
		} else
			ret = System.getProperty(key);

		return ret;
	}
}
