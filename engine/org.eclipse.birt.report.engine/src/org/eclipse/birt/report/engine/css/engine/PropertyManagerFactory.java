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
package org.eclipse.birt.report.engine.css.engine;

public interface PropertyManagerFactory {

	/**
	 * Returns the number of properties.
	 */
	int getNumberOfProperties();

	/**
	 * Returns the property index, or -1.
	 */
	int getPropertyIndex(String name);

	/**
	 * Returns the ValueManagers.
	 */
	ValueManager getValueManager(int idx);

	/**
	 * Returns the name of the property at the given index.
	 */
	String getPropertyName(int idx);
}
