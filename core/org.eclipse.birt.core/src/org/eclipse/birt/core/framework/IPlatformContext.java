/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
 * Defines an interface to access OSGi framework. The OSGi framewok is a folder
 * in the disk which has following strcutre: platform /configuration/config.ini
 * /plugins/ plugins in the framework osgi.jar
 */
public interface IPlatformContext {

	/**
	 * return the folder of the platform.
	 * 
	 * @return the folder represent the root of the platform.
	 */
	public String getPlatform();
}
