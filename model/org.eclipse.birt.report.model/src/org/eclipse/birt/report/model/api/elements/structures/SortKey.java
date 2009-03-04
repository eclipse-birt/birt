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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

/**
 * This class represents a sort entry for a table or list item, it defines the
 * column and sort direction pair. Each sort key has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Column Name </strong></dt>
 * <dd>the name of the column that is sorted.</dd>
 * 
 * <dt><strong>Direction </strong></dt>
 * <dd>the sort direction:asc or desc.</dd>
 * </dl>
 *  
 */

public class SortKey extends Structure
{

	/**
	 * Name of this structure. Matches the definition in the meta-data
	 * dictionary.
	 */

	public static final String SORT_STRUCT = "SortKey"; //$NON-NLS-1$

	/**
	 * Name of the "key" member. An expression that gives the sort key on which
	 * to sort.
	 */

	public static final String KEY_MEMBER = "key"; //$NON-NLS-1$

	/**
	 * Name of the "direction" member.
	 */

	public static final String DIRECTION_MEMBER = "direction"; //$NON-NLS-1$

	/**
	 * Value of the "key" member.
	 */

	private Expression key = null;

	/**
	 * Value of the "direction" member.
	 */

	private String direction = null;

	/**
	 * Constructs the sort key with the key to sort and the direction.
	 * 
	 * @param key
	 *            the key of the sort entry
	 * @param direction
	 *            sort direction: Ascending or descending order
	 */

	public SortKey( String key, String direction )
	{
		this.key = convertObjectToExpression( key );
		this.direction = direction;
	}

	/**
	 * Default constructor.
	 *  
	 */

	public SortKey( )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName( )
	{
		return SORT_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( KEY_MEMBER.equals( propName ) )
			return key;
		else if ( DIRECTION_MEMBER.equals( propName ) )
			return direction;

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
		if ( KEY_MEMBER.equals( propName ) )
			key = convertObjectToExpression( value );
		else if ( DIRECTION_MEMBER.equals( propName ) )
			direction = (String) value;
		else
			assert false;
	}

	/**
	 * Returns the expression that gives the sort key on which to sort.
	 * 
	 * @return the sort key on which to sort
	 */

	public String getKey( )
	{
		return getStringProperty( KEY_MEMBER );
	}

	/**
	 * Sets the expression that gives the sort key on which to sort.
	 * 
	 * @param key
	 *            the sort key to set
	 */

	public void setKey( String key )
	{
		setProperty( KEY_MEMBER, key );
	}

	/**
	 * Returns the sort direction. The possible values are define in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li>SORT_DIRECTION_ASC
	 * <li>SORT_DIRECTION_DESC
	 * </ul>
	 * 
	 * @return the sort direction
	 */

	public String getDirection( )
	{
		return (String) getProperty( null, DIRECTION_MEMBER );

	}

	/**
	 * Sets the sort direction. The allowed values are define in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li>SORT_DIRECTION_ASC
	 * <li>SORT_DIRECTION_DESC
	 * </ul>
	 * 
	 * @param direction
	 *            the direction to set
	 */

	public void setDirection( String direction )
	{
		setProperty( DIRECTION_MEMBER, direction );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.model.api.SimpleValueHandle,
	 *      int)
	 */

	public StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		return new SortKeyHandle( valueHandle, index );
	}

	/**
	 * Returns the name of the column that needs sort.
	 * 
	 * @return the column name.
	 * 
	 * @deprecated This property has been removed. See the method
	 *             {@link #getKey()}.
	 */

	public String getColumnName( )
	{
		return getKey( );
	}

	/**
	 * Sets the name of the column that needs sort.
	 * 
	 * @param columnName
	 *            the column name to set
	 * 
	 * @deprecated This property has been removed. See the method
	 *             {@link #setKey(String)}.
	 */

	public void setColumnName( String columnName )
	{
		setKey( columnName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */
	public List validate( Module module, DesignElement element )
	{
		ArrayList list = new ArrayList( );

		if ( StringUtil.isBlank( getKey( ) ) )
		{
			list.add( new PropertyValueException( element,
					getDefn( ).getMember( KEY_MEMBER ),
					getColumnName( ),
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED ) );
		}
		return list;
	}
}