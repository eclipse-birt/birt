/*
 *************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor.transform;

import java.io.IOException;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.group.GroupBy;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.odi.IQuery.GroupSpec;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;


public class SimpleGroupCalculator implements IGroupCalculator
{
	private IResultObject previous;
	private IResultObject next;
	private IResultObject current;
	private GroupBy[] groupBys;
	private StreamManager streamManager;
	private RAOutputStream tempOutput;
	public SimpleGroupCalculator( DataEngineSession session, GroupSpec[] groups, IResultClass rsMeta ) throws DataException
	{
		groupBys = new GroupBy[groups.length];
		for ( int i = 0; i < groups.length; ++i )
		{
			int keyIndex = groups[i].getKeyIndex( );
			String keyColumn = groups[i].getKeyColumn( );

			// Convert group key name to index for faster future access
			// assume priority of keyColumn is higher than keyIndex
			if ( keyColumn != null )
				keyIndex = rsMeta.getFieldIndex( keyColumn );

			groupBys[i] = GroupBy.newInstance( groups[i],
					keyIndex,
					keyColumn,
					rsMeta.getFieldValueClass( keyIndex ) );
		}
	}
	
	private int getBreakingGroup( IResultObject obj1, IResultObject obj2 ) throws DataException
	{
		if( obj1 == null )
			return 0;
		
		if( obj2 == null )
			return 0;
		
		for( int i = 0; i < this.groupBys.length; i++ )
		{
			int columnIndex = groupBys[i].getColumnIndex( );
			if( !groupBys[i].isInSameGroup( obj1.getFieldValue( columnIndex ), obj2.getFieldValue( columnIndex ) ) )
			{
				return i + 1;
			}
		}
		
		return this.groupBys.length+1;
	}
	@Override
	public int getStartingGroup( ) throws DataException
	{
		return this.getBreakingGroup( previous, current );
	}
	
	@Override
	public int getEndingGroup( ) throws DataException
	{
		return this.getBreakingGroup( current, next );
	}

	@Override
	public void registerPreviousResultObject( IResultObject previous )
	{
		this.previous = previous;
	}

	@Override
	public void registerCurrentResultObject( IResultObject current )
	{
		this.current = current;
	}

	@Override
	public void registerNextResultObject( IResultObject next )
	{
		this.next = next;
	}
	
	/**
	 * Do grouping, and fill group indexes
	 * 
	 * @param stopsign
	 * @throws DataException
	 */
	public void next( ) throws DataException
	{
		if ( this.tempOutput != null )
		{
			try
			{
				IOUtil.writeInt( this.tempOutput, this.getStartingGroup( ) );
				IOUtil.writeInt( this.tempOutput, this.getEndingGroup( ) );
			}
			catch ( IOException e )
			{
				throw new DataException( e.getLocalizedMessage( ), e );
			}
		}
	}

	@Override
	public void close( ) throws DataException
	{
		try
		{
			if ( this.tempOutput!= null  )
			{
				this.tempOutput.close( );
			}
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
	}

	@Override
	public void doSave( StreamManager manager ) throws DataException
	{
		this.streamManager = manager;
		if( this.streamManager!= null )
		{
			this.tempOutput = (RAOutputStream)streamManager.getOutStream( DataEngineContext.PROGRESSIVE_VIEWING_GROUP_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE );
			try
			{
				IOUtil.writeInt( this.tempOutput, this.groupBys.length );
			}
			catch ( IOException e )
			{
				throw new DataException( e.getLocalizedMessage( ), e );
			}
		}
	}

}
