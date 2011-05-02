
/*******************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.transform;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.impl.document.StreamWrapper;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;


/**
 * 
 */

public class ResultSetWrapper implements IResultIterator
{
	private SimpleResultSet source;
	private int index;
	private List<ResultObjectHolder> cachedRows;
	private boolean startSave;
	
	public ResultSetWrapper( SimpleResultSet source ) throws DataException
	{
		this.source = source;
		this.index = source.getCurrentResultIndex( );
		this.cachedRows = new ArrayList<ResultObjectHolder>();
		if( this.index == 0 )
		{
			this.cachedRows.add( new ResultObjectHolder( source.getCurrentResult( ),
					source.getStartingGroupLevel( ),
					source.getEndingGroupLevel( ) ) );
		}
	}
	
	public IResultClass getResultClass( ) throws DataException
	{
		return this.source.getResultClass( );
	}

	public boolean next( ) throws DataException
	{
		if( this.index < this.cachedRows.size( ) - 1 )
		{
			this.index++;
			return true;
		}
		else if( this.index == this.cachedRows.size( ) - 1 )
		{
			boolean result = this.source.next( );
			if( result )
			{
				this.index++;
				this.cachedRows.add( new ResultObjectHolder( this.source.getCurrentResult( ),
						this.source.getStartingGroupLevel( ),
						this.source.getEndingGroupLevel( ) ) );
			}
			return result;
		}
		else 
		{
			return false;
		}
	}

	public void first( int groupingLevel ) throws DataException
	{
		if( groupingLevel!= 0 )
			throw new UnsupportedOperationException();
	}

	public void last( int groupingLevel ) throws DataException
	{
		throw new UnsupportedOperationException();
	}

	public IResultObject getCurrentResult( ) throws DataException
	{
		assert this.index < this.cachedRows.size( );
		return this.cachedRows.get( this.index ).getResultObject( );
	}

	public int getCurrentResultIndex( ) throws DataException
	{
		return this.index;
	}

	public int getCurrentGroupIndex( int groupLevel ) throws DataException
	{
		throw new UnsupportedOperationException();
	}

	public int getStartingGroupLevel( ) throws DataException
	{
		assert this.index < this.cachedRows.size( );
		return this.cachedRows.get( this.index ).getStartingGroupIndex( );
	}

	public int getEndingGroupLevel( ) throws DataException
	{
		assert this.index < this.cachedRows.size( );
		return this.cachedRows.get( this.index ).getEndingGroupIndex( );
	}

	public void close( ) throws DataException
	{
		this.source.close( );
	}

	public int[] getGroupStartAndEndIndex( int groupLevel )
			throws DataException
	{
		return this.source.getGroupStartAndEndIndex( groupLevel );
	}
	
	public ResultSetCache getResultSetCache( )
	{
		throw new UnsupportedOperationException();
	}

	public int getRowCount( ) throws DataException
	{
		return this.source.getRowCount( );
	}

	public IExecutorHelper getExecutorHelper( )
	{
		return this.source.getExecutorHelper( );
	}

	public void doSave( StreamWrapper streamsWrapper, boolean isSubQuery )
			throws DataException
	{
		this.startSave = true;
		this.source.doSave( streamsWrapper, isSubQuery );
	}

	public void incrementalUpdate( StreamWrapper streamsWrapper, int rowCount,
			boolean isSubQuery ) throws DataException
	{
		this.source.incrementalUpdate( streamsWrapper, rowCount, isSubQuery );
	}

	public Object getAggrValue( String aggrName ) throws DataException
	{
		return null;
		/*if( !this.startSave )
			return null;
		while( !this.source.aggrValueAvailable( aggrName, this.index ))
		{
			if( this.source.next( ) )
			{
				this.cachedRows.add( new ResultObjectHolder( this.source.getCurrentResult( ),
						this.source.getStartingGroupLevel( ),
						this.source.getEndingGroupLevel( ) ) );
			}
			else
			{
				return null;
			}
		}
		return null;*/
	}
	
	private class ResultObjectHolder
	{
		private IResultObject ro;
		private int startingGroupIndex;
		private int endingGroupIndex;
		
		public ResultObjectHolder( IResultObject ro, int startingGroupIndex, int endingGroupIndex )
		{
			this.ro = ro;
			this.startingGroupIndex = startingGroupIndex;
			this.endingGroupIndex = endingGroupIndex;
		}
		
		public IResultObject getResultObject()
		{
			return this.ro;
		}
		
		public int getStartingGroupIndex()
		{
			return this.startingGroupIndex;
		}
		
		public int getEndingGroupIndex()
		{
			return this.endingGroupIndex;
		}
	}

}
