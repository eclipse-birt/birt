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
package org.eclipse.birt.report.engine.api;

public interface IResultSetItem {

	/**
	 * return the result set name.
	 * 
	 * @return the result set name
	 */
	public String getResultSetName();

	/**
	 * return the display name from externalization
	 * 
	 * @return the display name
	 */
	public String getResultSetDisplayName();

	/**
	 * return the result meta data.
	 * 
	 * @return the result meta data
	 */
	public IResultMetaData getResultMetaData();

}