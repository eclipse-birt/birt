/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionRow;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;

/**
 * 
 */

public class DimensionRowAccessor extends AbstractRowAccessor
{

	private Dimension dimension;
	private DimensionRow dimRow;

	/**
	 * 
	 * @param dimension
	 */
	public DimensionRowAccessor( Dimension dimension )
	{
		this.dimension = dimension;
		populateFieldIndexMap( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AbstractRowAccessor#populateFieldIndexMap()
	 */
	protected void populateFieldIndexMap( )
	{
		fieldIndexMap = new HashMap( );
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		for ( int i = 0; i < levels.length; i++ )
		{
			String[] keyNames = levels[i].getKeyNames( );
			if ( keyNames != null )
			{
				for ( int j = 0; j < keyNames.length; j++ )
				{
					String keyName = OlapExpressionUtil.getAttrReference( dimension.getName( ),
							levels[i].getName( ),
							keyNames[j] );
					fieldIndexMap.put( keyName, new DimensionKeyIndex( i, j ) );
				}
			}

			String[] attrNames = levels[i].getAttributeNames( );
			if ( attrNames != null )
			{
				for ( int j = 0; j < attrNames.length; j++ )
				{
					String attrName = OlapExpressionUtil.getAttrReference( dimension.getName( ),
							levels[i].getName( ),
							attrNames[j] );
					fieldIndexMap.put( attrName, new DimensionAttrIndex( i, j ) );
				}
			}
		}

	}

	/**
	 * 
	 * @param position
	 * @throws IOException
	 */
	public void seek( int position ) throws IOException
	{
		dimRow = dimension.getRowByPosition( position );
	}

	/**
	 * 
	 * @return
	 */
	public DimensionRow getCurrentRow( )
	{
		return dimRow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IResultRow#getAggrValue(java.lang.String)
	 */
	public Object getAggrValue( String aggrName ) throws DataException
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IResultRow#getFieldValue(java.lang.String)
	 */
	public Object getFieldValue( String fieldName ) throws DataException
	{
		if ( dimRow == null )
			throw new DataException( ResourceConstants.CANNOT_ACCESS_NULL_DIMENSION_ROW );
		FieldIndex index = (FieldIndex) fieldIndexMap.get( fieldName );
		return index != null ? index.getValue( ) : null;
	}

	/**
	 * 
	 */
	class DimensionKeyIndex extends KeyIndex
	{

		/**
		 * 
		 * @param levelIndex
		 * @param keyIndex
		 */
		DimensionKeyIndex( int levelIndex, int keyIndex )
		{
			super( levelIndex, keyIndex );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AbstractRowAccessor.FieldIndex#getValue()
		 */
		Object getValue( ) throws DataException
		{
			return dimRow.getMembers( )[levelIndex].getKeyValues( )[keyIndex];
		}
	}

	/**
	 * 
	 */
	class DimensionAttrIndex extends AttributeIndex
	{

		/**
		 * 
		 * @param levelIndex
		 * @param keyIndex
		 */
		DimensionAttrIndex( int levelIndex, int keyIndex )
		{
			super( levelIndex, keyIndex );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AbstractRowAccessor.FieldIndex#getValue()
		 */
		Object getValue( ) throws DataException
		{
			return dimRow.getMembers( )[levelIndex].getAttributes( )[attrIndex];
		}
	}

}
