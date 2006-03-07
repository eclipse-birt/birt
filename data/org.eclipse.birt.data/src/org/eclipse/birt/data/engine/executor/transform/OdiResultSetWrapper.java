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

package org.eclipse.birt.data.engine.executor.transform;

import org.eclipse.birt.data.engine.executor.dscache.DataSetResultCache;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * This is a wrapper class of all odi result set.It is used to enhance 
 * result-set processing algorithm.
 */
class OdiResultSetWrapper
{
	private Object resultSource;
	
	/**
	 * 
	 * @param rs
	 */
	OdiResultSetWrapper( ResultSet rs )
	{
		this.resultSource = rs;
	}

	/**
	 * 
	 * @param rs
	 */
	OdiResultSetWrapper( DataSetResultCache rs )
	{
		this.resultSource = rs;
	}
	
	/**
	 * 
	 * @param rs
	 */
	OdiResultSetWrapper( ICustomDataSet rs )
	{
		this.resultSource = rs;
	}
	
	/**
	 * 
	 * @param rs
	 */
	OdiResultSetWrapper( IResultIterator rs )
	{
		this.resultSource = rs;
	}
	
	/**
	 * 
	 * @param rs
	 */
	OdiResultSetWrapper( Object[] rs )
	{
		this.resultSource = rs;
	}
	
	/**
	 * 
	 * @return
	 */
	Object getWrappedOdiResultSet()
	{
		return this.resultSource;
	}
}
