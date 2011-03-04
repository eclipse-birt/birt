/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.ICacheable;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 *
 */
public class EngineExecutionHints implements IEngineExecutionHints
{
	private Set cachedDataSetNames;
	private List<IDataQueryDefinition> queryDefns = new ArrayList<IDataQueryDefinition>();
	/**
	 * 
	 */
	EngineExecutionHints( )
	{
		this.cachedDataSetNames = new HashSet();
	}
	
	/**
	 * 
	 * @param dataEngine
	 * @param queryDefns
	 * @throws DataException
	 */
	void populateCachedDataSets( DataEngineImpl dataEngine, IDataQueryDefinition[] qds ) throws DataException
	{
		if( qds!= null )
		{
			queryDefns.addAll( Arrays.asList( qds ) );
			List temp = new ArrayList();
			for( IDataQueryDefinition query: queryDefns )
			{
				if( query instanceof IQueryDefinition )
				{
					IQueryDefinition qd = (IQueryDefinition )query;
					String dataSetName = qd.getDataSetName( );
					if( dataSetName != null )
					{
						IBaseDataSetDesign design = dataEngine.getDataSetDesign( dataSetName );
						if( design instanceof ICacheable  )
							this.populateDataSetNames( dataEngine.getDataSetDesign( dataSetName ), dataEngine, temp );
					}
				} 
			}
			this.cachedDataSetNames.clear( );
			Set tempSet = new HashSet();
			for( int i = 0; i < temp.size( ); i++ )
			{
				if( tempSet.contains( temp.get( i )))
					this.cachedDataSetNames.add( temp.get( i ) );
				else
					tempSet.add( temp.get( i ) );
			}
		}
	}
	
	/**
	 * 
	 * @param design
	 * @param names
	 * @throws DataException
	 */
	private void populateDataSetNames( IBaseDataSetDesign design, DataEngineImpl engine, List names ) throws DataException
	{
		if( design == null )
			return;
		names.add( design.getName( ) );
		if( design instanceof IJointDataSetDesign )
		{
			IJointDataSetDesign jointDesign = ( IJointDataSetDesign )design;
			
			populateDataSetNames( engine.getDataSetDesign( jointDesign.getLeftDataSetDesignQulifiedName( ) ), engine, names );
			populateDataSetNames( engine.getDataSetDesign( jointDesign.getRightDataSetDesignQulifiedName( ) ), engine, names );
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IQueryExecutionHints#needCacheDataSet(java.lang.String)
	 */
	public boolean needCacheDataSet( String dataSetName )
	{
		return this.cachedDataSetNames.contains( dataSetName );
	}

}
