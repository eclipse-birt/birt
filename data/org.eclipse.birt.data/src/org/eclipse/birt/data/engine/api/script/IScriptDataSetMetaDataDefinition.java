/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.api.script;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Interface for event handler to define a script data set's metadata. The event
 * handler calls the addColumn() method to add the definition of its data set
 * columns
 */
public interface IScriptDataSetMetaDataDefinition {
	/**
	 * Adds a column to the metadata definition. A column is required to have a name
	 * and a data type. Acceptable data types are one of these classes or their
	 * derived classes: Integer, Double, BigDecimal. String, Date or byte[].
	 * 
	 * @param name     Name of column; must not be null
	 * @param dataType data type of column; must be one of the supported types
	 *                 listed in method description.
	 * @throws BirtException if invalid name or data type is provided
	 */
	public void addColumn(String name, Class dataType) throws BirtException;

}
