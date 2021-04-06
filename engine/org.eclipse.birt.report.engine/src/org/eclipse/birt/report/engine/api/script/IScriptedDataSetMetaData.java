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

package org.eclipse.birt.report.engine.api.script;

/**
 * Interface used by Scripted Data Set's describe handler to define column
 * metadata of the data set.
 */
public interface IScriptedDataSetMetaData {
	/**
	 * Adds a column to the metadata definition. A column is required to have a name
	 * and a data type. Acceptable data types are one of these classes or their
	 * derived classes: Integer, Double, BigDecimal. String, Date or byte[].
	 * 
	 * @param name     Name of column; must not be null or empty
	 * @param dataType data type of column; must be one of the supported types
	 *                 listed in method description.
	 */
	public void addColumn(String name, Class dataType);
}
