/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.script.element;

import java.util.List;

import org.eclipse.birt.report.engine.api.script.ScriptException;

/**
 * Script wrapper of OdaDataSetHandle
 * 
 */
public interface IDataSet {

	/**
	 * Returns <code>IDataSource</code> for this data set.
	 * 
	 * @return IDataSource
	 */

	IDataSource getDataSource();

	/**
	 * Returns the query text.
	 * 
	 * @return the query text.
	 */

	String getQueryText();

	/**
	 * Sets the query text.
	 * 
	 * @param query the text to set
	 * @throws ScriptException if this property is locked.
	 */
	void setQueryText(String query) throws ScriptException;

	/**
	 * Returns a private driver property value with the given property name.
	 * 
	 * @param name the name of a public driver property
	 * 
	 * @return a public driver property value
	 */

	String getPrivateDriverProperty(String name);

	/**
	 * Sets a private driver property value with the given name and value. If the
	 * property does not exist, it will be added into the property list. If the
	 * property already exists, the value of the property will be overwritten.
	 * 
	 * @param name  the name of a public driver property
	 * @param value the value of a public driver property
	 * 
	 * @throws ScriptException if <code>name</code> is <code>null</code> or an empty
	 *                         string after trimming.
	 */

	void setPrivateDriverProperty(String name, String value) throws ScriptException;

	/**
	 * Gets result set column of cached metadata.
	 * 
	 * @return collection each iteam is <code>IResultSetColumn</code>.
	 */

	List getCachedResultSetColumns();

}
