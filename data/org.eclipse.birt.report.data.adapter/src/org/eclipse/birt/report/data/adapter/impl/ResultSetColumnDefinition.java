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
package org.eclipse.birt.report.data.adapter.impl;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IColumnDefinition;

/**
 * resultset column from resultset property
 *
 */
class ResultSetColumnDefinition implements IColumnDefinition
{
	String 		name;
	int 		position = -1;
	int			dataType = DataType.UNKNOWN_TYPE;
    int         nativeDataType = 0;     // unknown
	String 	 	alias;
	String      displayName;
	boolean     computedCol = false;
	String      dataTypeName;

	/**
	 * Construct a Column definition for a named column
	 */
	ResultSetColumnDefinition( String name ) 
	{
	    this.name = name;
	}

	/**
	 * Assigns the indexed position to a Column definition.
	 * @param position 1-based position of column in the data row
	 */
	void setColumnPosition( int position ) 
	{
	    this.position = position;
	}
	
	
	/**
	 * Gets the column name 
	 */
	public String getColumnName() 
	{
		return name;
	}
	
	/**
	 * Gets the column position 
	 */
	public int getColumnPosition() 
	{
		return position;
	}
	
	/**
	 * Gets the data type of the column.
	 * @return Data type as an integer. 
	 */
	public int getDataType() 
	{
		return dataType;
	}
	
	/**
	 * @param dataType The dataType to set.
	 */
	void setDataType(int dataType) 
	{
		this.dataType = dataType;
	}
	
	/**
	 * Gets the data type of the column.
	 * @return Data type as an integer. 
	 */
	String getDataTypeName( )
	{
		return dataTypeName;
	}

	/**
	 * @param dataType
	 *            The dataType to set.
	 */
	void setDataTypeName( String dataTypeName )
	{
		this.dataTypeName = dataTypeName;
	}

	/* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IColumnDefinition#getNativeDataType()
     */
    public int getNativeDataType()
    {
        return nativeDataType;
    }
    
    /**
     * @param dataType The native data type to set.
     */
    void setNativeDataType( int typeCode ) 
    {
        nativeDataType = typeCode;
    }

    /** 
	 * Gets the alias of the column. An alias is a string that can be used interchangably as 
	 * the name to refer to a column.
	 */
	public String getAlias() 
	{
		return alias;
	}

	/**
	 * @param alias The alias to set.
	 */
	void setAlias(String alias) 
	{
		this.alias = alias;
	}

	/**
	 * 
	 * @param displayName
	 */
	void setLableName(String displayName)
	{
		this.displayName = displayName;
	}
	
	/**
	 * 
	 * @return
	 */
	String getLableName()
	{
		return this.displayName;
	}
	
	/**
	 * 
	 * @param computedCol
	 */
	void setComputedColumn( boolean computedCol )
	{
		this.computedCol = computedCol;
	}
	
	/**
	 * 
	 * @return
	 */
	boolean isComputedColumn()
	{
		return this.computedCol;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IColumnDefinition#getExportHint()
	 */
	public int getExportHint( )
	{
		return -1;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IColumnDefinition#getSearchHint()
	 */
	public int getSearchHint( )
	{
		return -1;
	}
}
