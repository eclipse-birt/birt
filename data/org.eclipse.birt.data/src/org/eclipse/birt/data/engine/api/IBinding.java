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

package org.eclipse.birt.data.engine.api;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */
public interface IBinding
{
	public String getBindingName() throws DataException;
	
	public int getDataType( ) throws DataException;

	public void setDataType( int type ) throws DataException;

	public IBaseExpression getExpression( ) throws DataException;

	public void setExpression( IBaseExpression expr ) throws DataException;

	public List getAggregatOns( ) throws DataException;

	public void addAggregateOn( String levelName ) throws DataException;

	public List getArguments( ) throws DataException;

	public void addArgument( IBaseExpression expr ) throws DataException;

	public void setFilter( IScriptExpression expr ) throws DataException;

	public IScriptExpression getFilter( ) throws DataException;

	public String getAggrFunction( ) throws DataException;

	public void setAggrFunction( String functionName ) throws DataException;

}
