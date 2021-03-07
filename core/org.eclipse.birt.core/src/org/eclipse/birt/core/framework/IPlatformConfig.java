/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.framework;

/**
 * Defines an interface to access OSGi framework.
 */
public interface IPlatformConfig {
	/**
	 * the birt home defines the platform folder, the value is a string object
	 */
	String BIRT_HOME = "BIRT_HOME";

	/**
	 * defines the platform context object used to startup the platform. the value
	 * is a instance of IPlatformContext.
	 */
	String PLATFORM_CONTEXT = "PLATFORM_CONTEXT";

	/**
	 * defines the launch arguments to access OSGi framework
	 */
	String OSGI_ARGUMENTS = "OSGI_ARGUMENTS";

	/**
	 * defines the configuration used to launch the OSGi framework
	 */
	String OSGI_CONFIGURATION = "OSGI_CONFIGURATION";

	/**
	 * defines the temporary folder for the platform if it is difference with JVM
	 */
	String TEMP_DIR = "tmpDir"; //$NON-NLS-1$

}