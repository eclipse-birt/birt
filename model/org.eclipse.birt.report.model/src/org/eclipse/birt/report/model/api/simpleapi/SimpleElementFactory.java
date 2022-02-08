/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
