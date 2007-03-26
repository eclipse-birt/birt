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

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.olap.api.cube.StopSign;
import org.eclipse.birt.data.engine.olap.data.document.DocumentObjectUtil;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.impl.NamingUtil;
import org.eclipse.birt.data.engine.olap.data.impl.Traversalor;
import org.eclipse.birt.data.engine.olap.data.util.Bytes;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

/**
 * An iterator on a result set from a executed fact table query.
 */

public class FactTableRowIterator implements IFactTableRowIterator
{
	private FactTable factTable;
	private MeasureInfo[] measureInfo;
	
	private IDiskArray[] selectedPos;
	private int[] dimensionIndex;
	private int[] currentSubDim;
	private List[] selectedSubDim = null;

	private IDocumentObject currentSegment;
	private int[] currentPos;
	private Object[] currentMeasures;

	private Traversalor traversalor;
	private StopSign stopSign;
	
	private int[][] selectedPosOfCurSegment;

	/**
	 * 
	 * @param factTable
	 * @param dimensionName
	 * @param dimensionPos
	 * @param stopSign
	 * @throws IOException
	 */
	public FactTableRowIterator( FactTable factTable, String[] dimensionName,
			IDiskArray[] dimensionPos, StopSign stopSign ) throws IOException
	{
		this.factTable = factTable;
		this.measureInfo = factTable.getMeasureInfo( );
		this.selectedPos = dimensionPos;
		this.selectedSubDim = new List[factTable.getDimensionInfo( ).length];
		this.selectedPosOfCurSegment = new int[factTable.getDimensionInfo( ).length][];
		this.stopSign = stopSign;
		
		assert dimensionName.length == dimensionPos.length;
		
		for ( int i = 0; i < selectedSubDim.length; i++ )
		{
			this.selectedSubDim[i] = new ArrayList( );
		}
		dimensionIndex = new int[factTable.getDimensionInfo( ).length];
		for ( int i = 0; i < dimensionIndex.length; i++ )
		{
			dimensionIndex[i] = -1;
		}
		for ( int i = 0; i < dimensionName.length; i++ )
		{
			dimensionIndex[factTable.getDimensionIndex( dimensionName[i] )] = i;

		}
		filterSubDimension( );
		this.currentPos = new int[factTable.getDimensionInfo( ).length];
		this.currentMeasures = new Object[factTable.getMeasureInfo( ).length];

		nextSegment( );
	}

	/**
	 * Filter sub dimensions by dimension position array. The filter result is
	 * saved in the variable selectedSubDim.
	 * 
	 * @throws IOException
	 */
	private void filterSubDimension( ) throws IOException
	{
		DimensionDivision[] dimensionDivisions = factTable.getDimensionDivision( );
		SelectedSubDimension selectedSubDimension = null;
		int[] selectedSubDimensionCount = new int[selectedSubDim.length];

		for ( int i = 0; i < selectedSubDim.length; i++ )
		{
			int pointer = 0;
			for ( int j = 0; j < dimensionDivisions[i].ranges.length; j++ )
			{
				if ( dimensionIndex[i] > -1 )
				{
					while ( pointer < selectedPos[dimensionIndex[i]].size( )
							&& ( (Integer) selectedPos[dimensionIndex[i]].get( pointer ) ).intValue( ) < dimensionDivisions[i].ranges[j].start )
					{
						pointer++;
					}
					if ( pointer >= selectedPos[dimensionIndex[i]].size( ) )
					{
						break;
					}
					if ( ( (Integer) selectedPos[dimensionIndex[i]].get( pointer ) ).intValue( ) > dimensionDivisions[i].ranges[j].end )
					{
						continue;
					}
					selectedSubDimension = new SelectedSubDimension( );
					selectedSubDimension.subDimensionIndex = j;
					selectedSubDimension.start = pointer;
					while ( pointer < selectedPos[dimensionIndex[i]].size( )
							&& ( (Integer) selectedPos[dimensionIndex[i]].get( pointer ) ).intValue( ) <= dimensionDivisions[i].ranges[j].end )
					{
						pointer++;
					}
					selectedSubDimension.end = pointer - 1;
					selectedSubDim[i].add( selectedSubDimension );
				}
				else
				{
					selectedSubDimension = new SelectedSubDimension( );
					selectedSubDimension.subDimensionIndex = j;
					selectedSubDimension.start = -1;
					selectedSubDimension.end = -1;
					selectedSubDim[i].add( selectedSubDimension );
				}
			}
			selectedSubDimensionCount[i] = selectedSubDim[i].size( );
		}
		traversalor = new Traversalor( selectedSubDimensionCount );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#next()
	 */
	public boolean next( ) throws IOException
	{
		while ( !stopSign.isStopped( ) )
		{
			try
			{
				if( currentSegment == null )
				{
					return false;
				}
				Bytes combinedDimensionPosition = currentSegment.readBytes( );
				
				currentPos = factTable.getCombinedPositionCalculator( )
						.calculateDimensionPosition( getSubDimensionIndex( ),
								combinedDimensionPosition.bytesValue( ) );
				 
				for ( int i = 0; i < this.currentMeasures.length; i++ )
				{
					currentMeasures[i] = DocumentObjectUtil.readValue( currentSegment,
							measureInfo[i].dataType );
				}
				if ( !isSelectedRow( ) )
				{
					continue;
				}
				else
				{
					return true;
				}
			}
			catch ( EOFException e )
			{
				break;
			}
		}
		if ( stopSign.isStopped( ) || !nextSegment( ) )
		{
			return false;
		}
		return next( );
	}

	/**
	 * 
	 * @return
	 */
	private int[] getSubDimensionIndex( )
	{
		int[] result = new int[selectedSubDim.length];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = ( (SelectedSubDimension) selectedSubDim[i].get( currentSubDim[i] ) ).subDimensionIndex;
		}
		return result;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean isSelectedRow( ) throws IOException
	{
		//this method can be made performance enhancement.
		boolean find;
		for ( int i = 0; i < currentPos.length; i++ )
		{
			if ( dimensionIndex[i] != -1 )
			{
				find = false;
				for ( int j = 0; j < selectedPosOfCurSegment[i].length; j++ )
				{
					if ( selectedPosOfCurSegment[i][j] > currentPos[i] )
					{
						return false;
					}
					if ( selectedPosOfCurSegment[i][j] == currentPos[i] )
					{
						find = true;
						break;
					}
				}
				if ( !find )
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
     * Moves down one segment from its current segment of the iterator.
	 * @return
	 * @throws IOException
	 */
	private boolean nextSegment( ) throws IOException
	{
		if ( stopSign.isStopped( ) )
		{
			return false;
		}
		if ( !traversalor.next( ) )
		{
			return false;
		}
		currentSubDim = traversalor.getIntArray( );
		currentSegment = factTable.getDocumentManager( )
				.openDocumentObject( FTSUDocumentObjectNamingUtil.getDocumentObjectName( 
						NamingUtil.getFactTableName(factTable.getName( )),
						getSubDimensionIndex( ) ) );
		for ( int i = 0; i < dimensionIndex.length; i++ )
		{
			if ( dimensionIndex[i] != -1 )
			{
				SelectedSubDimension selectedSubDimension = ( (SelectedSubDimension) selectedSubDim[i].get( currentSubDim[i] ) );
				selectedPosOfCurSegment[i] = new int[selectedSubDimension.end
						- selectedSubDimension.start + 1];
				for ( int j = 0; j < selectedSubDimension.end
						- selectedSubDimension.start + 1; j++ )
				{
					selectedPosOfCurSegment[i][j] = ( (Integer) selectedPos[dimensionIndex[i]].get( j ) ).intValue( );
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#getDimensionCount()
	 */
	public int getDimensionCount( )
	{
		return factTable.getDimensionInfo( ).length;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#getDimensionIndex(java.lang.String)
	 */
	public int getDimensionIndex( String dimensionName )
	{
		return factTable.getDimensionIndex( dimensionName );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#getMeasureIndex(java.lang.String)
	 */
	public int getMeasureIndex( String measureName )
	{
		return factTable.getMeasureIndex( measureName );
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
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#getMeasureCount()
	 */
	public int getMeasureCount( )
	{
		return currentMeasures.length;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#getMeasure(int)
	 */
	public Object getMeasure( int measureIndex )
	{
		return currentMeasures[measureIndex];
	}
}

class SelectedSubDimension
{
	int subDimensionIndex;
	int start;
	int end;
}