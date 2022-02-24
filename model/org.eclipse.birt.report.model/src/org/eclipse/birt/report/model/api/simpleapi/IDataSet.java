/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.simpleapi;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;

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
	 * @throws SemanticException if this property is locked.
	 */
	void setQueryText(String query) throws SemanticException;

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
	 * @throws SemanticException if <code>name</code> is <code>null</code> or an
	 *                           empty string after trimming.
	 */

	void setPrivateDriverProperty(String name, String value) throws SemanticException;

	/**
	 * Gets result set column of cached metadata.
	 * 
	 * @return collection each iteam is <code>IResultSetColumn</code>.
	 */

	List getCachedResultSetColumns();

}
