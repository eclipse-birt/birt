/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.simpleapi;

/**
 * 
 */

public class SimpleElementFactory {

	private static ISimpleElementFactory factory;

	/**
	 * Sets the factory instance.
	 * 
	 * @param inFactory the factory instance
	 */

	public synchronized static void setInstance(ISimpleElementFactory inFactory) {
		if (factory == null)
			factory = inFactory;
	}

	/**
	 * Returns the factory instance.
	 * 
	 * @return the factory
	 */

	public static ISimpleElementFactory getInstance() {
		return factory;
	}

}
