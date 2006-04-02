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
package org.eclipse.birt.data.engine.odi;

import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Event handler for ODI layer. In ODI layer, since higher caller sometimes
 * needs to do something in the process of ODI, it can be achieved by creating
 * an instance of IEventHandler, and pass it to the ODI as the paramter of query
 * execution.
 */
public interface IEventHandler
{
	/**
	 * The result set transformation can be divided into two phrases, one is for
	 * data set, and the other is for table/list. This function will be called
	 * in the middle of the two phrases.
	 * 
	 * @param resultIterator
	 *            current result iterator in processed
	 */
	void handleEndOfDataSetProcess( IResultIterator resultIterator );
	
	/**
	 * 
	 * @param rsObject
	 * @param name
	 * @return the value for the specified column name
	 */
	Object getValue( IResultObject rsObject, int index, String name )
			throws DataException;
	
	/**
	 * row[0], row._rowPosition
	 * @param columnName
	 * @return
	 */
	boolean isRowID(int index, String columnName);
	
	/**
	 * @return
	 */
	IBaseExpression getBaseExpr(String name);

}
