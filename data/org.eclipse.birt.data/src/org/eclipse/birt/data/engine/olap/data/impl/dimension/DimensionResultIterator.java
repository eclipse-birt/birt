
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
package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionFilterDefn;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.TimeDimensionUtil;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;


/**
 * 
 */

public class DimensionResultIterator implements IDimensionResultIterator
{
	private Dimension dimension;
	private IDiskArray dimensionPosition;
	private IDiskArray dimensionRows;
	private int currentPosition;
	private ILevel[] levels;
	private static Logger logger = Logger.getLogger( DimensionResultIterator.class.getName( ) );
	private int[] memoryDimensionPosition;
	
	public DimensionResultIterator( Dimension dimension,
			IDiskArray dimensionPosition, StopSign stopSign )
			throws IOException
	{
		Object[] params = {
				dimension, dimensionPosition
		};
		logger.entering( DimensionResultIterator.class.getName( ),
				"DimensionResultIterator",
				params );
		this.dimension = dimension;
		this.dimensionPosition = dimensionPosition;
		this.levels = dimension.getHierarchy( ).getLevels( );
		this.currentPosition = 0;
		logger.exiting( DimensionResultIterator.class.getName( ),
				"DimensionResultIterator" );
	}
	
	private void initDimensionRows( ) throws IOException
	{
		if( dimensionRows == null )
		{
			if( dimensionPosition == null )
			{
				dimensionPosition = dimension.findAll( );
			}
			dimensionRows = dimension.getDimensionRowByPositions( dimensionPosition, new StopSign( ) );
//			if( dimension.length( ) < Constants.MAX_DIMENSION_LENGTH )
			{
				memoryDimensionPosition = new int[dimensionPosition.size( )];
				for( int i = 0; i < dimensionPosition.size( ); i++ )
				{
					memoryDimensionPosition[i] = (Integer)dimensionPosition.get( i );
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#close()
	 */
	public void close( ) throws BirtException, IOException
	{
		if( dimensionPosition != null )
			dimensionPosition.close( );
		if( dimensionRows != null )
			dimensionRows.close( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#filter(org.eclipse.birt.data.olap.data.api.IDimensionSortDefinition)
	 */
	public IDimensionResultIterator filter( IDimensionFilterDefn filterDef )
			throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getDimesion()
	 */
	public IDimension getDimesion( )
	{
		return dimension;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getDimesionPosition()
	 */
	public int getDimesionPosition( )
			throws BirtException, IOException
	{
		initDimensionRows( );
		return ((Integer)(dimensionPosition.get( currentPosition ))).intValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getLevelAttribute(int, int)
	 */
	public Object getLevelAttribute( int levelIndex, int attributeIndex ) throws IOException
	{
		if( dimension.isTime( ) )
		{
			return null;
		}
		initDimensionRows( );
		return ((DimensionRow)dimensionRows.get( currentPosition )).
			getMembers()[levelIndex].getAttributes()[attributeIndex];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getLevelAttributeDataType(java.lang.String, java.lang.String)
	 */
	public int getLevelAttributeDataType( String levelName, String attributeName )
	{
		if( dimension.isTime( ) )
		{
			return -1;
		}
		return levels[getLevelIndex(levelName)].getAttributeDataType( attributeName );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getLevelAttributeIndex(java.lang.String, java.lang.String)
	 */
	public int getLevelAttributeIndex( String levelName, String attributeName )
	{
		if( dimension.isTime( ) )
		{
			return -1;
		}
		String[] attributeNames = levels[getLevelIndex( levelName )].getAttributeNames( );
		for ( int i = 0; i < attributeNames.length; i++ )
		{
			if ( attributeNames[i].equals( attributeName ) )
			{
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getLevelIndex(java.lang.String)
	 */
	public int getLevelIndex( String levelName )
	{
		if( dimension.isTime( ) )
		{
			return TimeDimensionUtil.getFieldIndex( levelName );
		}
		for( int i=0;i<levels.length;i++)
		{
			if(levels[i].getName( ).equals( levelName ))
			{
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getLevelKeyDataType(java.lang.String)
	 */
	public int[] getLevelKeyDataType( String levelName )
	{
		if( dimension.isTime( ) )
		{
			return new int[]{ DataType.INTEGER_TYPE };
		}
		int levelIndex = getLevelIndex(levelName);
		if ( levelIndex < 0 )
		{
			return null;
		}
		String[] keyNames = levels[levelIndex].getKeyNames( );
		int[] result = new int[keyNames.length];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = levels[levelIndex].getKeyDataType( keyNames[i] );
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getLevelKeyValue(int)
	 */
	public Object[] getLevelKeyValue( int levelIndex ) throws IOException
	{
		initDimensionRows( );
		if( dimension.isTime( ) )
		{
			Date timeValue = getCurrentTimeValue( );
			return new Object[]{ TimeDimensionUtil.getFieldVaule( timeValue, levelIndex ) };
		}
		else
		{
			return ((DimensionRow)dimensionRows.get( currentPosition )).
				getMembers()[levelIndex].getKeyValues();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getLevels()
	 */
	public ILevel[] getLevels( )
	{
		return levels;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#length()
	 */
	public int length( )
	{
		if( dimensionPosition == null )
		{
			return dimension.length( );
		}
		else
		{
			return dimensionPosition.size( );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#seek(int)
	 */
	public void seek( int index )
	{
		currentPosition = index;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#sort(org.eclipse.birt.data.olap.data.api.IDimensionSortDefinition)
	 */
	public void sort( IDimensionSortDefn sortDef ) throws BirtException
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getLevelMember(int)
	 */
	public Member getLevelMember( int levelIndex ) throws IOException
	{
		initDimensionRows( );
		if( dimension.isTime( ) )
		{
			Date timeValue = getCurrentTimeValue( );
			Member member = new Member( );
			member.setKeyValues( new Object[]{ TimeDimensionUtil.getFieldVaule( timeValue, levelIndex ) } );
			return member;
		}
		else
		{
			return ((DimensionRow)dimensionRows.get( currentPosition )).
				getMembers()[levelIndex];
		}
	}

	private Date getCurrentTimeValue( ) throws IOException {
		Date timeValue = ( Date )((( DimensionRow)dimensionRows.get( currentPosition ) ).
				getMembers( )[0].getKeyValues( )[0]);
		return timeValue;
	}
	
	public DimensionRow getDimensionRow( ) throws IOException
	{
		initDimensionRows( );
		return (DimensionRow)dimensionRows.get( currentPosition );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.IDimensionResultIterator#locate(int)
	 */
	public boolean locate( int dimPosition ) throws BirtException, IOException
	{
		int curDimPosition = getDimesionPosition( );
		if ( curDimPosition == dimPosition )
			return true;
		
		if( memoryDimensionPosition != null )
		{
			int pos = Arrays.binarySearch( memoryDimensionPosition, dimPosition );
			if( pos < 0 )
				return false;
			else
			{
				seek( pos );
				return true;
			}
		}
		else
		{
			while ( true )
			{
				curDimPosition = getDimesionPosition( );
				if ( curDimPosition > dimPosition )
				{
					if ( currentPosition - 1 < 0 )
					{
						return false;
					}
					seek( currentPosition - 1 );
				}
				else if ( curDimPosition < dimPosition )
				{
					if ( currentPosition + 1 >= length( ) )
					{
						return false;
					}
					seek( currentPosition + 1 );
				}
				else
				{
					return true;
				}
			}
		}
	}
}
