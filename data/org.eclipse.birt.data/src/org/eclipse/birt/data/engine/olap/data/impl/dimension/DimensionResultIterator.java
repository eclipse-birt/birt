
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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionFilterDefn;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
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
	
	public DimensionResultIterator( Dimension dimension, IDiskArray dimensionPosition,
			String[] levelNames ) throws IOException
	{
		this.dimension = dimension;
		this.dimensionPosition = dimensionPosition;
		this.levels = dimension.getHierarchy( ).getLevels( );
		dimensionRows = dimension.getDimensionRowByPositions( dimensionPosition );
		
		this.currentPosition = 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#close()
	 */
	public void close( ) throws BirtException, IOException
	{
		dimensionPosition.close( );
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
		return ((Integer)(dimensionPosition.get( currentPosition ))).intValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getLevelAttribute(int, int)
	 */
	public Object getLevelAttribute( int levelIndex, int attributeIndex ) throws IOException
	{
		return ((DimensionRow)dimensionRows.get( currentPosition )).
			members[levelIndex].attributes[attributeIndex];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getLevelAttributeDataType(java.lang.String, java.lang.String)
	 */
	public int getLevelAttributeDataType( String levelName, String attributeName )
	{
		return levels[getLevelIndex(levelName)].getAttributeDataType( attributeName );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimesionResulttSet#getLevelAttributeIndex(java.lang.String, java.lang.String)
	 */
	public int getLevelAttributeIndex( String levelName, String attributeName )
	{
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
		int levelIndex = getLevelIndex(levelName);
		if ( levelIndex < 0 )
		{
			return null;
		}
		String[] keyNames = levels[levelIndex].getKeyName( );
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
		return ((DimensionRow)dimensionRows.get( currentPosition )).
			members[levelIndex].keyValues;
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
		return dimensionRows.size( );
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
		return ((DimensionRow)dimensionRows.get( currentPosition )).
			members[levelIndex];
	}
	
	public DimensionRow getDimensionRow( ) throws IOException
	{
		return (DimensionRow)dimensionRows.get( currentPosition );
	}
}
