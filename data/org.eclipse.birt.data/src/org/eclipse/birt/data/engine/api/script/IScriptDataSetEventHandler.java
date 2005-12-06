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
 * Event handler for a Script Data Set
 */
public interface IScriptDataSetEventHandler extends IDataSetEventHandler
{
	public void open( IDataSetInstance dataSet ) throws BirtException;
	public void close( IDataSetInstance dataSet ) throws BirtException;
	
	/**
	 * Called by data engine to obtain the next data row. Implementation should
	 * fill in row data by using the IDataRow interface.
	 * @return true if current data row is available and has been populated;
	 * false if no more data row is unavailable; row has not been populated
	 */
	public boolean fetch( IDataSetInstance dataSet, IDataRow row ) throws BirtException;
	
	/**
	 * Called by data engine before the open() event is fired. Implementation should
	 * return the metadata of the script data set's data row. If the script data set
	 * defines its metadata statically (i.e., in the report design), this method should
	 * return null.
	 */
	public IScriptDataSetColumnMetaData[] describe( IDataSetInstance dataSet ) throws BirtException;
}
