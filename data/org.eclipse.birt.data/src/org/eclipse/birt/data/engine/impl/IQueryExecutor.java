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
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */
public interface IQueryExecutor
{

	void execute( ) throws DataException;
	
	Scriptable getSharedScope( );

	Scriptable getJSAggrValueObject( );
	
	int getNestedLevel( );

	IDataSourceInstanceHandle getDataSourceInstanceHandle( );

	DataSetRuntime getDataSet( );

	DataSetRuntime[] getDataSetRuntime( int count );

	IResultIterator getOdiResultSet( );
	
	IResultMetaData getResultMetaData( ) throws DataException;
	
	void close( );
	
}
