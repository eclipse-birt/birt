
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

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.DiskIndex;

/**
 * Describes a level. A level is composed of memeber located at this level.
 */

public class Level implements ILevel
{
	String name;
	int[] keyDataType;
	String[] keyColNames;
	int[] attributeDataTypes;
	String[] attributeColNames;
	int size;
	
	DiskIndex diskIndex = null;
	
	/**
	 * 
	 * @param documentManager
	 * @param levelDef
	 * @param keyDataType
	 * @param attributeDataTypes
	 * @param size
	 * @throws IOException
	 * @throws DataException
	 */
	public Level( IDocumentManager documentManager, ILevelDefn levelDef, int[] keyDataType,
			int[] attributeDataTypes, int size, DiskIndex diskIndex ) throws IOException, DataException
	{
		this.name = levelDef.getLevelName( );
		this.keyDataType = keyDataType;
		this.keyColNames = levelDef.getKeyColumns( );
		this.attributeDataTypes = attributeDataTypes;
		this.attributeColNames = levelDef.getAttributeColumns( );
		this.size = size;
		this.diskIndex = diskIndex;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ILevel#getKeyDataType(java.lang.String)
	 */
	public int getKeyDataType( String keyName )
	{
		for ( int i = 0; i < keyColNames.length; i++ )
		{
			if ( keyColNames[i].equals( keyName ) )
			{
				return this.keyDataType[i];
			}
		}
		return DataType.UNKNOWN_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ILevel#getName()
	 */
	public String getName( )
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ILevel#size()
	 */
	public int size( )
	{
		return size;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object o )
	{
		Level other = (Level)o;
		return this.name.equals( other.name );
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ILevel#getAttributeDataType(java.lang.String)
	 */
	public int getAttributeDataType( String attributeName )
	{
		for ( int i = 0; i < attributeColNames.length; i++ )
		{
			if ( attributeColNames[i].equals( attributeName ) )
			{
				return this.attributeDataTypes[i];
			}
		}
		return DataType.UNKNOWN_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ILevel#getAttributeNames()
	 */
	public String[] getAttributeNames( )
	{
		return attributeColNames;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.ILevel#getKeyName()
	 */
	public String[] getKeyName( )
	{
		return keyColNames;
	}
}
