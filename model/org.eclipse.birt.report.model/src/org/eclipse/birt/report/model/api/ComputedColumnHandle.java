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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;

/**
 * Represents the handle of computed column. A computed column is a ¡°virtual¡±
 * column produced as an expression of other columns within the data set. It
 * includes the column name and the expression used to define a computed column.
 * 
 */

public class ComputedColumnHandle extends StructureHandle
{

	/**
	 * Constructs the handle of computed column.
	 * 
	 * @param valueHandle
	 *            the value handle for computed column list of one property
	 * @param index
	 *            the position of this computed column in the list
	 */

	public ComputedColumnHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Returns the column name.
	 * 
	 * @return the column name
	 * @deprecated using {@link #getName()} instead.
	 */

	public String getColumnName( )
	{
		return getName( );
	}

	/**
	 * Returns column display name.
	 * 
	 * @return column display name.
	 */

	public String getDisplayName( )
	{
		return getStringProperty( ComputedColumn.DISPLAY_NAME_MEMBER );
	}

	/**
	 * Returns the column name.
	 * 
	 * @return the column name
	 */

	public String getName( )
	{
		return getStringProperty( ComputedColumn.NAME_MEMBER );
	}

	/**
	 * Sets the column name.
	 * 
	 * @param columnName
	 *            the column name to set
	 * @deprecated using {@link #setName(String)} instead.
	 */

	public void setColumnName( String columnName )
	{
	}

	/**
	 * Sets the column display name.
	 * 
	 * @param columnDisplayName
	 *            the column display name to set.
	 * @throws SemanticException
	 *             if the new column display name duplicates with the existed
	 *             ones.
	 * 
	 */

	public void setDisplayName( String columnDisplayName ) throws SemanticException
	{
		setProperty( ComputedColumn.DISPLAY_NAME_MEMBER, columnDisplayName );
	}

	/**
	 * Sets the column name.
	 * 
	 * @param columnName
	 *            the column name to set.
	 * @throws SemanticException
	 *             if the new column name duplicates with the existed ones.
	 * 
	 */

	public void setName( String columnName ) throws SemanticException
	{
		setProperty( ComputedColumn.NAME_MEMBER, columnName );
	}

	/**
	 * Returns the expression used to define this computed column.
	 * 
	 * @return the expression used to define this computed column
	 */

	public String getExpression( )
	{
		return getStringProperty( ComputedColumn.EXPRESSION_MEMBER );
	}

	/**
	 * Sets the expression used to define this computed column.
	 * 
	 * @param expression
	 *            the expression to set
	 * @throws SemanticException
	 *             value required exception
	 */

	public void setExpression( String expression ) throws SemanticException
	{
		setProperty( ComputedColumn.EXPRESSION_MEMBER, expression );
	}

	/**
	 * Returns the aggregateOn expression used to define this computed column.
	 * 
	 * @return the aggregateOn expression used to define this computed column
	 * 
	 * @deprecated by {@link #getAggregateOn()}
	 */

	public String getAggregrateOn( )
	{
		return getAggregateOn( );
	}

	/**
	 * Sets the aggregateOn expression used to define this computed column.
	 * 
	 * @param aggregateOn
	 *            the aggregateOn expression to set
	 * @deprecated by {@link #setAggregateOn(String)}
	 */

	public void setAggregrateOn( String aggregateOn )
	{
		setAggregateOn( aggregateOn );
	}

	/**
	 * Returns the aggregateOn expression used to define this computed column.
	 * 
	 * @return the aggregateOn expression used to define this computed column
	 */

	public String getAggregateOn( )
	{
		return getStringProperty( ComputedColumn.AGGREGATEON_MEMBER );
	}

	/**
	 * Sets the aggregateOn expression used to define this computed column.
	 * 
	 * @param aggregateOn
	 *            the aggregateOn expression to set
	 */

	public void setAggregateOn( String aggregateOn )
	{
		setPropertySilently( ComputedColumn.AGGREGATEON_MEMBER, aggregateOn );
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
		return getStringProperty( ComputedColumn.DATA_TYPE_MEMBER );
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
		setProperty( ComputedColumn.DATA_TYPE_MEMBER, dataType );
	}
}