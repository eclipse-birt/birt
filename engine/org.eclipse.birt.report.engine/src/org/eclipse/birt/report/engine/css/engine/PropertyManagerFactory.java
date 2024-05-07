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

/**
 * Factory of the property manager
 *
 * @since 3.3
 *
 */
public interface PropertyManagerFactory {

	/**
	 * Returns the number of properties.
	 *
	 * @return Returns the number of properties.
	 */
	int getNumberOfProperties();

	/**
	 * Returns the property index, or -1.
	 *
	 * @param name name of the property
	 * @return Returns the property index, or -1.
	 */
	int getPropertyIndex(String name);

	/**
	 * Returns the ValueManagers.
	 *
	 * @param idx index of the value manager
	 * @return Returns the ValueManagers.
	 */
	ValueManager getValueManager(int idx);

	/**
	 * Returns the name of the property at the given index.
	 *
	 * @param idx index of the property
	 * @return Returns the name of the property at the given index.
	 */
	String getPropertyName(int idx);
}
