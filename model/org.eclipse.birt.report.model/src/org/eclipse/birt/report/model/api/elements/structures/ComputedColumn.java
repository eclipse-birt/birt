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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.List;

import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

/**
 * Represents one computed column. A computed column is a ¡°virtual¡± column
 * produced as an expression of other columns within the data set.
 * <p>
 * This is a managed object, meaning that all changes should be made though the
 * command layer so that they can be undone and redone. Each computed column has
 * the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Column Name </strong></dt>
 * <dd>a computed column has a required column name.</dd>
 * 
 * <dt><strong>Expression </strong></dt>
 * <dd>expression of the computation for the column.</dd>
 * </dl>
 * 
 */

public class ComputedColumn extends Structure
{

	/**
	 * Name of this structure. Matches the definition in the meta-data
	 * dictionary.
	 */

	public static final String COMPUTED_COLUMN_STRUCT = "ComputedColumn"; //$NON-NLS-1$

	/**
	 * Name of the column name member.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$
	
	/**
	 * DisplayName of the column name member.
	 */

	public static final String DISPLAY_NAME_MEMBER = "displayName"; //$NON-NLS-1$

	/**
	 * Name of the column name member.
	 * 
	 * @deprecated using {@link #NAME_MEMBER} instead.
	 */

	public static final String COLUMN_NAME_MEMBER = "columnName"; //$NON-NLS-1$

	/**
	 * Name of the expression member.
	 */

	public static final String EXPRESSION_MEMBER = "expression"; //$NON-NLS-1$

	/**
	 * Name of the data-type member.
	 */

	public static final String DATA_TYPE_MEMBER = "dataType"; //$NON-NLS-1$

	/**
	 * Name of the aggregateOn member.
	 */

	public static final String AGGREGATEON_MEMBER = "aggregateOn"; //$NON-NLS-1$

	/**
	 * Name of the aggregateOn member.
	 * 
	 * @deprecated
	 */

	public static final String AGGREGRATEON_MEMBER = AGGREGATEON_MEMBER;

	/**
	 * The column name.
	 */

	private String columnName = null;
	
	/**
	 * The column display name.
	 */
	
	private String columnDisplayName = null;
	

	/**
	 * The expression for this computed column.
	 */

	private String expression = null;

	/**
	 * The aggregrateOn expression for the computed column.
	 */

	private String aggregrateOn = null;

	/**
	 * The data type of this column.
	 */

	private String dataType = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName( )
	{
		return COMPUTED_COLUMN_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String memberName )
	{
		if ( NAME_MEMBER.equals( memberName ) )
			return columnName;
		if ( EXPRESSION_MEMBER.equals( memberName ) )
			return expression;
		if ( DATA_TYPE_MEMBER.equals( memberName ) )
			return dataType;
		if ( AGGREGRATEON_MEMBER.equalsIgnoreCase( memberName ) )
			return aggregrateOn;
		if( DISPLAY_NAME_MEMBER.equalsIgnoreCase( memberName ))
			return columnDisplayName;

		assert false;
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.String,
	 *      java.lang.Object)
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( NAME_MEMBER.equals( propName ) )
			columnName = (String) value;
		else if ( EXPRESSION_MEMBER.equals( propName ) )
			expression = (String) value;
		else if ( DATA_TYPE_MEMBER.equals( propName ) )
			dataType = (String) value;
		else if ( AGGREGRATEON_MEMBER.equals( propName ) )
			aggregrateOn = (String) value;
		else if( DISPLAY_NAME_MEMBER.equalsIgnoreCase( propName ))
			columnDisplayName = (String)value;
		else
			assert false;
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
	 * Returns the column name.
	 * 
	 * @return the column name
	 */

	public String getName( )
	{
		return (String) getProperty( null, NAME_MEMBER );
	}
	
	/**
	 * Returns column display name.
	 * 
	 * @return column display name.
	 */

	public String getDisplayName( )
	{
		return (String)getProperty( null , ComputedColumn.DISPLAY_NAME_MEMBER );
	}
	
	/**
	 * Sets the column display name.
	 * 
	 * @param columnDisplayName
	 *            the column display name to set.
	 * 
	 */

	public void setDisplayName( String columnDisplayName )
	{
		setProperty( ComputedColumn.DISPLAY_NAME_MEMBER, columnDisplayName );
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
		setName( columnName );
	}

	/**
	 * Sets the column name
	 * 
	 * @param name
	 *            the column name to set.
	 */

	public void setName( String name )
	{
		setProperty( NAME_MEMBER, name );
	}

	/**
	 * Returns the expression to compute.
	 * 
	 * @return the expression to compute
	 */

	public String getExpression( )
	{
		return (String) getProperty( null, EXPRESSION_MEMBER );
	}

	/**
	 * Sets the expression.
	 * 
	 * @param expression
	 *            the expression to set
	 */

	public void setExpression( String expression )
	{
		setProperty( EXPRESSION_MEMBER, expression );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.model.api.SimpleValueHandle,
	 *      int)
	 */

	public StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		return new ComputedColumnHandle( valueHandle, index );
	}

	/**
	 * Validates this structure. The following are the rules:
	 * <ul>
	 * <li>The column name is required.
	 * </ul>
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(Module,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate( Module module, DesignElement element )
	{
		List list = super.validate( module, element );

		String columnName = getName( );
		if ( StringUtil.isBlank( columnName ) )
		{
			list.add( new PropertyValueException( element, getDefn( )
					.getMember( NAME_MEMBER ), columnName,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED ) );
		}

		return list;
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
		return (String) getProperty( null, DATA_TYPE_MEMBER );
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
	 */

	public void setDataType( String dataType )
	{
		setProperty( DATA_TYPE_MEMBER, dataType );
	}

	/**
	 * Returns the aggregrateOn expression to compute.
	 * 
	 * @return the aggregrateOn expression to compute.
	 * 
	 * @deprecated by {@link #getAggregateOn()}
	 */

	public String getAggregrateOn( )
	{
		return (String) getProperty( null, AGGREGRATEON_MEMBER );
	}

	/**
	 * Sets the aggregateOn expression.
	 * 
	 * @param aggregateOn
	 *            the aggregateOn expression to set
	 * @deprecated by {@link #setAggregateOn(String)}
	 * 
	 */

	public void setAggregrateOn( String aggregateOn )
	{
		setAggregateOn( aggregateOn );
	}

	/**
	 * Returns the aggregateOn expression to compute.
	 * 
	 * @return the aggregateOn expression to compute.
	 */

	public String getAggregateOn( )
	{
		return (String) getProperty( null, AGGREGATEON_MEMBER );
	}

	/**
	 * Sets the aggregateOn expression.
	 * 
	 * @param aggregateOn
	 *            the aggregateOn expression to set
	 */

	public void setAggregateOn( String aggregateOn )
	{
		setProperty( AGGREGATEON_MEMBER, aggregateOn );
	}
}
