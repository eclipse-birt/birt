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

import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class DataJSObjectPopulator implements IJSObjectPopulator
{
	//
	private DummyJSAggregationAccessor dataObj;
	private Scriptable scope;
	private List bindings;
	private IBaseQueryResults outResults;
	private boolean hasAggrLevels;

	/**
	 * 
	 * @param scope
	 * @param bindings
	 * @param hasAggrLevels
	 */
	public DataJSObjectPopulator( IBaseQueryResults outResults, Scriptable scope, List bindings,
			boolean hasAggrLevels )
	{
		this.scope = scope;
		this.bindings = bindings;
		this.hasAggrLevels = hasAggrLevels;
		this.outResults = outResults;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator#doInit()
	 */
	public void doInit( ) throws DataException
	{
		this.dataObj = new DummyJSAggregationAccessor( this.outResults );
		if ( hasAggrLevels )
		{
			this.scope.put( ScriptConstants.DATA_BINDING_SCRIPTABLE, this.scope, this.dataObj );//$NON-NLS-1$
		}
		else
		{
			this.scope.put( ScriptConstants.DATA_BINDING_SCRIPTABLE,//$NON-NLS-1$
					this.scope,
					new DummyJSDataAccessor( this.outResults, bindings, this.scope ) );
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator#setData(java.lang.Object)
	 */
	public void setData( Object resultRow )
	{
		assert resultRow instanceof IResultRow;
		this.dataObj.setResultRow( ( IResultRow ) resultRow );

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator#close()
	 */
	public void cleanUp( )
	{
		this.scope.delete( ScriptConstants.DATA_BINDING_SCRIPTABLE );//$NON-NLS-1$
		this.scope.setParentScope( null );
	}

}
