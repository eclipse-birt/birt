/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSourceFactory;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * A prepared Sub query, which does not have its own data set, but rather queries a subset of 
 * data produced by its a parent query.
 */
class PreparedSubquery extends PreparedQuery
{
	private int groupLevel;
	private PreparedQuery parentQuery;
	private IResultIterator parentIterator;
	
	/**
	 * @param subquery Subquery definition
	 * @param parentQuery Parent query (which can be a subquery itself, or a PreparedReportQuery)
	 * @param groupLevel Index of group in which this subquery is defined within the parent query.
	 * If 0, subquery is defined outside of any groups.
	 * @throws DataException
	 */
	PreparedSubquery( ISubqueryDefinition subquery, PreparedQuery parentQuery, int groupLevel )
		throws DataException
	{
		super( parentQuery.getDataEngine(), subquery);
		
		this.groupLevel = groupLevel;
		this.parentQuery = parentQuery;
	}
	
	/**
	 * Executes this subquery
	 */
	QueryResults execute( IResultIterator parentIterator, Scriptable scope ) 
		throws DataException
	{
		this.parentIterator = parentIterator;
		return doExecute( null,scope );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.PreparedQuery#getReportQuery()
	 */
	protected PreparedDataSourceQuery getDataSourceQuery()
	{
		// Gets the parent's report query
		return parentQuery.getDataSourceQuery();
	}
	
	
	/**
	 * @see org.eclipse.birt.data.engine.impl.PreparedQuery#newExecutor()
	 */
	protected Executor newExecutor()
	{
		return new SubQueryExecutor();
	}
	
	class SubQueryExecutor extends PreparedQuery.Executor
	{
		protected IDataSource createOdiDataSource( )
		{
			// Subqueries don't have its own data source
			return null;
		}
		
		protected DataSourceRuntime findDataSource( )
		{
			// Subqueries don't have its own data source
			return null;
		}
		
		protected DataSetRuntime newDataSetRuntime()
		{
			// Subqueries don't have its own data set
			return null;
		}
		
		protected IQuery createOdiQuery( ) throws DataException
		{
			// An empty odi data source is used for sub query data set
			return DataSourceFactory.getFactory().newDataSource( null ).
					newCandidateQuery();
		}
		
		protected IResultIterator executeOdiQuery(IQueryResults outerResults ) 
				throws DataException
		{
			assert parentIterator != null;
			ICandidateQuery cdQuery = (ICandidateQuery) odiQuery; 
			cdQuery.setCandidates( parentIterator, groupLevel );
			IResultIterator ret = cdQuery.execute();
			parentIterator = null;
			
			return ret;
		}
	}
}
