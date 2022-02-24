/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.querymodel;

/**
 * A mapped column The compound of <code>ATTR_COLUMN_NAME</code> attribute and
 * <code>ATTR_COLUMN_ODADATATYPE</code> attribute in
 * <code>ElEMENT_COLUMN_MAPPING</code> element in POJO query text.
 */
public class Column {
	private String name;
	private String odaType;
	private int index;

	/**
	 * @param name
	 * @param odaType
	 * @throws IllegalArgumentException if <code>name</code> or <code>odaType</code>
	 *                                  is empty
	 */
	public Column(String name, String odaType, int index) {
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException("name is empty"); //$NON-NLS-1$
		}
		if (odaType == null || odaType.length() == 0) {
			throw new IllegalArgumentException("odaType is empty"); //$NON-NLS-1$
		}
		this.name = name;
		this.odaType = odaType;
		this.index = index;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the odaType
	 */
	public String getOdaType() {
		return odaType;
	}

	public int getIndex() {
		return index;
	}
}
