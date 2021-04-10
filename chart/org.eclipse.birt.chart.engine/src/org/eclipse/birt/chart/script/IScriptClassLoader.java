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

package org.eclipse.birt.chart.script;

/**
 * This interface defines the functions to load script classes for chart
 * scripting.
 */
public interface IScriptClassLoader {

	/**
	 * Loads the class by given name.
	 * 
	 * @param className    Class name.
	 * @param parentLoader Parent loader.
	 * @return Loaded class.
	 */
	Class loadClass(String className, ClassLoader parentLoader) throws ClassNotFoundException;
}
