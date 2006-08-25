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

package org.eclipse.birt.report.model.api;

import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.ReferenceValue;

/**
 * Represents the handle of one column in the result set. The result set column
 * defines the data in which column is in the result set.
 * <dl>
 * <dt><strong>Name </strong></dt>
 * <dd>a result set column has an optional name.</dd>
 * 
 * <dt><strong>Position </strong></dt>
 * <dd>a result set column has an optional position for it.</dd>
 * 
 * <dt><strong>Data Type </strong></dt>
 * <dd>a result set column has a choice data type: any, integer, string, data
 * time, decimal, float, structure or table.</dd>
 * </dl>
 * 
 */

public class ResultSetColumnHandle extends StructureHandle
{

	/**
	 * Constructs the handle of result set column.
	 * 
	 * @param valueHandle
	 *            the value handle for result set column list of one property
	 * @param index
	 *            the position of this result set column in the list
	 */

	public ResultSetColumnHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Returns the column name.
	 * 
	 * @return the column name
	 */

	public String getColumnName( )
	{
		if ( !isExtendedJointDataSet( ) )
			return getStringProperty( ResultSetColumn.NAME_MEMBER );

		return getPrefixStringProperty( ResultSetColumn.NAME_MEMBER );
	}

	private boolean isExtendedJointDataSet( )
	{
		if ( elementHandle instanceof JointDataSetHandle
				&& elementHandle.getExtends( ) != null
				&& getPropertyDefn( ).getName( ).equalsIgnoreCase(
						DataSetHandle.CACHED_METADATA_PROP ) )
			return true;

		return false;
	}

	/**
	 * Returns the property value where a library name space is required.
	 * 
	 * @param memberName
	 *            the structure name
	 * @return the property value. If this value is not defined in the current
	 *         module, the library namespace is added.
	 */

	private String getPrefixStringProperty( String memberName )
	{
		if ( !ResultSetColumn.NAME_MEMBER.equalsIgnoreCase( memberName ) )
			return super.getStringProperty( memberName );

		String resultSetName = super.getStringProperty( memberName );
		if ( StringUtil.isBlank( resultSetName ) )
			return resultSetName;

		List dataSetRefs = elementHandle
				.getListProperty( JointDataSetHandle.DATA_SETS_PROP );
		if ( dataSetRefs == null || dataSetRefs.isEmpty( ) )
			return super.getStringProperty( ResultSetColumn.NAME_MEMBER );

		Module tmpRoot = null;

		String[] dataSetNames = ExpressionUtil
				.getSourceDataSetNames( resultSetName );

		for ( int i = 0; i < dataSetNames.length; i++ )
		{
			String dataSetName = dataSetNames[i];
			if ( dataSetName == null )
				continue;
			for ( int j = 0; j < dataSetRefs.size( ); j++ )
			{
				ElementRefValue refValue = (ElementRefValue) dataSetRefs
						.get( j );
				if ( refValue.getName( ).equalsIgnoreCase( dataSetName )
						&& refValue.getElement( ) != null )
				{
					tmpRoot = refValue.getElement( ).getRoot( );
					break;
				}
			}

			if ( tmpRoot != null )
				break;
		}

		if ( tmpRoot == getModule( ) || tmpRoot == null )
			return resultSetName;

		return ( (Library) tmpRoot ).getNamespace( )
				+ ReferenceValue.NAMESPACE_DELIMITER + resultSetName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.StructureHandle#getProperty(java.lang.String)
	 */

	public Object getProperty( String memberName )
	{
		if ( !isExtendedJointDataSet( ) )
			return super.getProperty( memberName );

		return getPrefixStringProperty( memberName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.StructureHandle#getStringProperty(java.lang.String)
	 */

	protected String getStringProperty( String memberName )
	{
		if ( !isExtendedJointDataSet( ) )
			return super.getStringProperty( memberName );

		return getPrefixStringProperty( memberName );
	}

	/**
	 * Sets the column name.
	 * 
	 * @param columnName
	 *            the column name to set
	 * @throws SemanticException
	 *             value required exception
	 */

	public void setColumnName( String columnName ) throws SemanticException
	{
		setProperty( ResultSetColumn.NAME_MEMBER, columnName );
	}

	/**
	 * Returns the data type of this column. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li>COLUMN_DATA_TYPE_ANY
	 * <li>COLUMN_DATA_TYPE_INTEGER
	 * <li>COLUMN_DATA_TYPE_STRING
	 * <li>COLUMN_DATA_TYPE_DATETIME
	 * <li>COLUMN_DATA_TYPE_DECIMAL
	 * <li>COLUMN_DATA_TYPE_FLOAT
	 * <li>COLUMN_DATA_TYPE_STRUCTURE
	 * <li>COLUMN_DATA_TYPE_TABLE
	 * </ul>
	 * 
	 * @return the data type of this column.
	 */

	public String getDataType( )
	{
		return getStringProperty( ResultSetColumn.DATA_TYPE_MEMBER );
	}

	/**
	 * Sets the data type of this column. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li>COLUMN_DATA_TYPE_ANY
	 * <li>COLUMN_DATA_TYPE_INTEGER
	 * <li>COLUMN_DATA_TYPE_STRING
	 * <li>COLUMN_DATA_TYPE_DATETIME
	 * <li>COLUMN_DATA_TYPE_DECIMAL
	 * <li>COLUMN_DATA_TYPE_FLOAT
	 * <li>COLUMN_DATA_TYPE_STRUCTURE
	 * <li>COLUMN_DATA_TYPE_TABLE
	 * </ul>
	 * 
	 * @param dataType
	 *            the data type to set
	 * @throws SemanticException
	 *             if the dataType is not in the choice list.
	 */

	public void setDataType( String dataType ) throws SemanticException
	{
		setProperty( ResultSetColumn.DATA_TYPE_MEMBER, dataType );
	}

	/**
	 * Returns the position that this column is in the result set.
	 * 
	 * @return the position that this column is in the result set.
	 */

	public Integer getPosition( )
	{
		return (Integer) getProperty( ResultSetColumn.POSITION_MEMBER );
	}

	/**
	 * Sets the position that this column is in the result set.
	 * 
	 * @param position
	 *            the position to set
	 */

	public void setPosition( Integer position )
	{
		setPropertySilently( ResultSetColumn.POSITION_MEMBER, position );
	}

	/**
	 * Returns the native data type.
	 * 
	 * @return the result set column native data type.
	 */

	public Integer getNativeDataType( )
	{
		return (Integer) getProperty( ResultSetColumn.NATIVE_DATA_TYPE_MEMBER );
	}

	/**
	 * Sets the result set column native data type.
	 * 
	 * @param dataType
	 *            the native data type to set.
	 */

	public void setNativeDataType( Integer dataType )
	{
		setPropertySilently( ResultSetColumn.NATIVE_DATA_TYPE_MEMBER, dataType );
	}

}