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

package org.eclipse.birt.data.engine.executor;

import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.dscache.DataSetResultCache;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;

/**
 * Implementation of ICandidateQuery
 */

public class CandidateQuery extends BaseQuery implements ICandidateQuery 
{

	private ICustomDataSet customDataSet;
	
	private IResultIterator resultObjsIterator;
	private int groupingLevel;
	
	private IResultClass	resultMetadata;
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#setCandidates(org.eclipse.birt.data.engine.odi.IResultIterator, int)
	 */
	public void setCandidates( IResultIterator resultObjsIterator,
			int groupingLevel ) throws DataException
	{
		assert resultObjsIterator != null;
		this.resultObjsIterator = resultObjsIterator;
		this.groupingLevel = groupingLevel;
		
		resultMetadata = resultObjsIterator.getResultClass();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#setCandidates(org.eclipse.birt.data.engine.odi.ICustomDataSet)
	 */
	public void setCandidates( ICustomDataSet customDataSet ) throws DataException
	{
		assert customDataSet != null;
		this.customDataSet = customDataSet;
		
		resultMetadata = customDataSet.getResultClass();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#getResultClass()
	 */
	public IResultClass getResultClass( )
	{
		return resultMetadata;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#execute()
	 */
	public IResultIterator execute( IEventHandler eventHandler )
			throws DataException
	{
		if ( customDataSet == null ) // sub query
		{
			// resultObjsIterator
			// for sub query, the event handler is no use
			return new CachedResultSet( this,
					resultMetadata,
					resultObjsIterator,
					groupingLevel,
					eventHandler);
		}
		else // scripted query
		{
			if ( DataSetCacheManager.getInstance( ).doesSaveToCache( ) == false )
				return new CachedResultSet( this, customDataSet, eventHandler );
			else
				return new CachedResultSet( this,
						resultMetadata,
						new DataSetResultCache( customDataSet, resultMetadata ),
						eventHandler );
			
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#close()
	 */
	public void close( )
	{
		// nothing
		
	}

}
