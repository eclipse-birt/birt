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
import org.eclipse.birt.data.engine.api.ISubqueryDefn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * A prepared Subquery
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
	PreparedSubquery( ISubqueryDefn subquery, PreparedQuery parentQuery, int groupLevel )
		throws DataException
	{
		super( parentQuery.engine, subquery);
		this.groupLevel = groupLevel;
		this.parentQuery = parentQuery;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.PreparedQuery#createOdiQuery()
	 */
	protected IQuery createOdiQuery() throws DataException, DataException
	{
		return getOdiDataSource().newCandidateQuery();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.PreparedQuery#executeOdiQuery()
	 */
	protected IResultIterator executeOdiQuery(IQueryResults outerResults,Scriptable scope) throws DataException
	{
		assert parentIterator != null;
		ICandidateQuery cdQuery = (ICandidateQuery) getOdiQuery(); 
		cdQuery.setCandidates( parentIterator, groupLevel );
		IResultIterator ret = cdQuery.execute();
		parentIterator = null;
		
		return ret;
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
	protected PreparedReportQuery getReportQuery()
	{
		// Gets the parent's report query
		return parentQuery.getReportQuery();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.PreparedQuery#getDataSet()
	 */
	protected DataSetDefn getDataSet()
	{
		// Subquery does not have its own data set
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.PreparedQuery#getOdiDataSource()
	 */
	protected IDataSource getOdiDataSource()
	{
		// Subquery uses parent's Odi data source
		return parentQuery.getOdiDataSource();
	}
}
