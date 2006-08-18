/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IColumnDefinition;

/**
 * Default implementation of {@link org.eclipse.birt.data.engine.api.IColumnDefinition} interface. <p>
 */
public class ColumnDefinition implements IColumnDefinition
{
	String 		name;
	int 		position = -1;
	int			dataType = DataType.UNKNOWN_TYPE;
    int         nativeDataType = 0;     // unknown
	String 	 	alias;
	int			searchHint = NOT_SEARCHABLE;
	int 		exportHint = DONOT_EXPORT;
	
	/**
	 * Construct a Column definition for a named column
	 */
	public ColumnDefinition( String name ) 
	{
	    this.name = name;
	}

	/**
	 * Assigns the indexed position to a Column definition.
	 * @param position 1-based position of column in the data row
	 */
	public void setColumnPosition( int position ) 
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

	/* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IColumnDefinition#getNativeDataType()
     */
    public int getNativeDataType()
    {
        return nativeDataType;
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
	 * Gets the search hint for the column
	 */
	public int getSearchHint() 
	{
		return searchHint;
	}

	/**
	 * Gets the export hint for the column
	 */
	public int getExportHint() 
	{
		return searchHint;
	}


	/**
	 * @param alias The alias to set.
	 */
	public void setAlias(String alias) 
	{
		this.alias = alias;
	}
	
	/**
	 * @param dataType The dataType to set.
	 */
	public void setDataType(int dataType) 
	{
		this.dataType = dataType;
	}
    
    /**
     * Set the column native data type.
     * @param typeCode  a data type code defined by an underlying data source.
     */
    public void setNativeDataType( int typeCode )
    {
        nativeDataType = typeCode;
    }
    
	/**
	 * @param exportHint The exportHint to set.
	 */
	public void setExportHint(int exportHint) 
	{
		this.exportHint = exportHint;
	}
	
	/**
	 * @param searchHint The searchHint to set.
	 */
	public void setSearchHint(int searchHint) 
	{
		this.searchHint = searchHint;
	}
}
