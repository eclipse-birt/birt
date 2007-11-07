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

package org.eclipse.birt.data.engine.olap.util;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class DataJSObjectPopulator implements IJSObjectPopulator
{

	private DummyJSAggregationAccessor dataObj;
	private Scriptable scope;
	private List bindings;
	private boolean hasAggrLevels;

	public DataJSObjectPopulator( Scriptable scope, List bindings,
			boolean hasAggrLevels )
	{
		this.scope = scope;
		this.bindings = bindings;
		this.hasAggrLevels = hasAggrLevels;
	}

	public void doInit( ) throws DataException
	{
		this.dataObj = new DummyJSAggregationAccessor( );
		if ( hasAggrLevels )
		{
			this.scope.put( "data", this.scope, this.dataObj );//$NON-NLS-1$
		}
		else
		{
			this.scope.put( "data",//$NON-NLS-1$
					this.scope,
					new DummyJSDataAccessor( bindings, this.scope ) );
		}

	}

	public void setResultRow( IResultRow resultRow )
	{
		this.dataObj.setResultRow( resultRow );

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator#close()
	 */
	public void cleanUp( )
	{
		this.scope.delete( "data" );//$NON-NLS-1$
		this.scope.setParentScope( null );
	}

}
