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
package org.eclipse.birt.report.engine.css.engine;

public interface PropertyManagerFactory {

	/**
	 * Returns the number of properties.
	 */
	abstract public int getNumberOfProperties();

	/**
	 * Returns the property index, or -1.
	 */
	abstract public int getPropertyIndex(String name);

	/**
	 * Returns the ValueManagers.
	 */
	abstract public ValueManager getValueManager(int idx);

	/**
	 * Returns the name of the property at the given index.
	 */
	abstract public String getPropertyName(int idx);
}
