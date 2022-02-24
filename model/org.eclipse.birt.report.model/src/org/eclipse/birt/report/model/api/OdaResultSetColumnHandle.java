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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;

/**
 * Represents the handle of one column in the oda result set. The result set
 * column defines the data in which column is in the result set.
 * <dl>
 * <dt><strong>Native name </strong></dt>
 * <dd>the oda defined result set name.</dd>
 * 
 * <dt><strong>Native data type </strong></dt>
 * <dd>the oda defined type of the result set column.</dd>
 * 
 * </dl>
 * 
 */

public class OdaResultSetColumnHandle extends ResultSetColumnHandle {

	/**
	 * Constructs the handle of oda result set column.
	 * 
	 * @param valueHandle the value handle for oda result set column list of one
	 *                    property
	 * @param index       the position of this oda result set column in the list
	 */

	public OdaResultSetColumnHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the native data type.
	 * 
	 * @return the result set column native data type.
	 */

	public Integer getNativeDataType() {
		return (Integer) getProperty(OdaResultSetColumn.NATIVE_DATA_TYPE_MEMBER);
	}

	/**
	 * Sets the result set column native data type.
	 * 
	 * @param dataType the native data type to set.
	 */

	public void setNativeDataType(Integer dataType) {
		setPropertySilently(OdaResultSetColumn.NATIVE_DATA_TYPE_MEMBER, dataType);
	}

	/**
	 * Returns the native name of this result set column.
	 * 
	 * @return the native name
	 */

	public String getNativeName() {
		return getStringProperty(OdaResultSetColumn.NATIVE_NAME_MEMBER);
	}

	/**
	 * Sets the native name for this result set column.
	 * 
	 * @param nativeName native name
	 * @throws SemanticException
	 */

	public void setNativeName(String nativeName) throws SemanticException {
		setProperty(OdaResultSetColumn.NATIVE_NAME_MEMBER, nativeName);
	}

}
