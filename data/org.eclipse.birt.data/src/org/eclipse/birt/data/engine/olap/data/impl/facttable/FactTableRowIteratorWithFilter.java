
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
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.io.IOException;

import org.eclipse.birt.data.engine.olap.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionRow;

/**
 * 
 */

public class FactTableRowIteratorWithFilter implements IFactTableRowIterator
{
	private DimensionResultIterator[] dimesionResultIterators;
	private IFactTableRowIterator facttableRowIterator;
	private int[] currentPos = null;
	private Object[] currentMeasures;
	private IDimension[] dimensions;
	
	FactTableRowIteratorWithFilter( IDimension[] dimensions,
			IFactTableRowIterator facttableRowIterator ) throws IOException
	{
		this.dimensions = dimensions;
		this.dimesionResultIterators = getDimesionResultIterators( );
		this.facttableRowIterator = facttableRowIterator;
		this.currentPos = new int[dimesionResultIterators.length];
		this.currentMeasures = new Object[facttableRowIterator.getMeasureCount( )];
	}
	
	/**
	 * 
	 * @param lCube
	 * @return
	 * @throws IOException
	 */
	private DimensionResultIterator[] getDimesionResultIterators( ) throws IOException
	{
		DimensionResultIterator[] dimesionResultIterators = new DimensionResultIterator[dimensions.length];
		for( int i=0;i<dimensions.length;i++)
		{
			dimesionResultIterators[i] = new DimensionResultIterator( (Dimension) dimensions[i],
					dimensions[i].findAll( ),
					getAllLevelName(dimensions[i]) );
		}
		return dimesionResultIterators;
	}
	
	/**
	 * 
	 * @param dimension
	 * @return
	 */
	private String[] getAllLevelName( IDimension dimension )
	{
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		String[] result = new String[levels.length];
		for( int j=0;j<levels.length;j++)
		{
			result[j] = levels[j].getName( );
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#getDimensionCount()
	 */
	public int getDimensionCount( )
	{
		return dimensions.length;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#getDimensionIndex(java.lang.String)
	 */
	public int getDimensionIndex( String dimensionName )
	{
		for(int i=0;i<dimensions.length;i++)
		{
			if(dimensions[i].getName( ).equals( dimensionName ))
			{
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#getDimensionPosition(int)
	 */
	public int getDimensionPosition( int dimensionIndex )
	{
		return currentPos[dimensionIndex];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#getMeasure(int)
	 */
	public Object getMeasure( int measureIndex )
	{
		return currentMeasures[measureIndex];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#getMeasureCount()
	 */
	public int getMeasureCount( )
	{
		return currentMeasures.length;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#getMeasureIndex(java.lang.String)
	 */
	public int getMeasureIndex( String measureName )
	{
		return facttableRowIterator.getMeasureIndex( measureName );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#next()
	 */
	public boolean next( ) throws IOException
	{
		boolean hasNext = facttableRowIterator.next( );
		while( hasNext )
		{
			for(int i=0;i<currentPos.length;i++)
			{
				currentPos[i] = facttableRowIterator.getDimensionPosition( i );
			}
			for ( int i = 0; i < currentMeasures.length; i++ )
			{
				currentMeasures[i] = facttableRowIterator.getMeasure( i );
			}
			if( filter() )
			{
				break;
			}
			hasNext = facttableRowIterator.next( );
		}
		return hasNext;
	}
	
	private boolean filter( ) throws IOException
	{
		DimensionRow[] dimRows = new DimensionRow[currentPos.length];
		for(int i=0;i<currentPos.length;i++)
		{
			dimesionResultIterators[i].seek( currentPos[i] );
			dimRows[i] = dimesionResultIterators[i].getDimensionRow( );
		}
		return true;
	}

}
