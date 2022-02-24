
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
package org.eclipse.birt.data.engine.olap.data.api;

/**
 *
 */

public interface ILevel {
	/**
	 *
	 * @return
	 */
	String getName();

	/**
	 *
	 * @return
	 */
	String[] getAttributeNames();

	/**
	 *
	 * @return
	 */
	int size();

	/**
	 * @return
	 */
	String[] getKeyNames();

	/**
	 *
	 * @return
	 */
	int getKeyDataType(String keyName);

	/**
	 *
	 * @param attributeName
	 * @return
	 */
	int getAttributeDataType(String attributeName);

	/**
	 *
	 * @return
	 */
	String getLeveType();
}
