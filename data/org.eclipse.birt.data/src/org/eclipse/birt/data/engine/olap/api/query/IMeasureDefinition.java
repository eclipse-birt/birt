
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
package org.eclipse.birt.data.engine.olap.api.query;

/**
 * IMeasureDefinition is a signature class which is used to ensure the type
 * safe. It extends INamedObject interface.
 */

public interface IMeasureDefinition extends INamedObject {
	/**
	 * Return the name of aggr function used by this measure during cube query.
	 * 
	 * @return
	 */
	public String getAggrFunction();

	/**
	 * Set the name of aggr function used by this measure during cube query.
	 * 
	 * @param name
	 */
	public void setAggrFunction(String name);

	/**
	 * Set the data type for this measure.
	 * 
	 * @param name
	 */
	public void setDataType(int type);

	/**
	 * get the data type for this measure.
	 * 
	 * @return
	 */
	public int getDataType();
}
