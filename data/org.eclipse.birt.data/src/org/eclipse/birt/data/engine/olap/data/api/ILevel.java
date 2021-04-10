
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public String getName();

	/**
	 * 
	 * @return
	 */
	public String[] getAttributeNames();

	/**
	 * 
	 * @return
	 */
	public int size();

	/**
	 * @return
	 */
	public String[] getKeyNames();

	/**
	 * 
	 * @return
	 */
	public int getKeyDataType(String keyName);

	/**
	 * 
	 * @param attributeName
	 * @return
	 */
	public int getAttributeDataType(String attributeName);

	/**
	 * 
	 * @return
	 */
	public String getLeveType();
}
