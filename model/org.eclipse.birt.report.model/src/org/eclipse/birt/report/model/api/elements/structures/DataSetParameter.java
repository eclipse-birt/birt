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

import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

/**
 * Represents the parameter for data set drivers. The parameter is the part of
 * the data set definition, if defined. A parameter can be an input or output
 * parameter. A parameter can also be input and output parameter. Each data set
 * parameter has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Name </strong></dt>
 * <dd>a data set parameter has a required name.</dd>
 * 
 * <dt><strong>Position </strong></dt>
 * <dd>a data set parameter has an optional position for it.</dd>
 * 
 * <dt><strong>Data Type </strong></dt>
 * <dd>a data set parameter has a choice data type: any, integer, string, data
 * time, decimal, float, structure or table.</dd>
 * 
 * <dt><strong>Is optional </strong></dt>
 * <dd>whether this parameter is optional.</dd>
 * 
 * <dt><strong>Allow Null </strong></dt>
 * <dd>whether the value of this parameter can be nullable.</dd>
 * 
 * <dt><strong>Is Input </strong></dt>
 * <dd>whether this parameter is an input parameter.</dd>
 * 
 * <dt><strong>Is Output </strong></dt>
 * <dd>whether this parameter is an output parameter.</dd>
 * </dl>
 * 
 * 
 */

public class DataSetParameter extends Structure
{

	/**
	 * Name of this structure. Matches the definition in the meta-data
	 * dictionary.
	 */

	public static final String STRUCT_NAME = "DataSetParam"; //$NON-NLS-1$

	/**
	 * Name of the position member.
	 */

	public static final String POSITION_MEMBER = "position"; //$NON-NLS-1$

	/**
	 * Name of the parameter name member.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * Name of the parameter data type member.
	 */

	public static final String DATA_TYPE_MEMBER = "dataType"; //$NON-NLS-1$

	/**
	 * Name of the member indicating that whether the report must provide a
	 * value for this parameter.
	 */

	public static final String IS_OPTIONAL_MEMBER = "isOptional"; //$NON-NLS-1$

	/**
	 * Name of the parameter default value member.
	 */

	public static final String DEFAULT_VALUE_MEMBER = "defaultValue"; //$NON-NLS-1$

	/**
	 * Name of the member indicating that whether the value of this parameter
	 * can be <code>null</code>.
	 * 
	 * @deprecated
	 */

	public static final String IS_NULLABLE_MEMBER = "isNullable"; //$NON-NLS-1$

	/**
	 * Name of the member indicating that whether the value of this parameter
	 * can be <code>null</code>.
	 */

	public static final String ALLOW_NULL_MEMBER = "allowNull"; //$NON-NLS-1$

	/**
	 * Name of the member indicating that whether this is an input parameter.
	 */

	public static final String IS_INPUT_MEMBER = "isInput"; //$NON-NLS-1$

	/**
	 * Name of the member indicating that whether this is an output parameter.
	 */

	public static final String IS_OUTPUT_MEMBER = "isOutput"; //$NON-NLS-1$

	/**
	 * The parameter position.
	 */

	private Integer position = null;

	/**
	 * The parameter name.
	 */

	private String name = null;

	/**
	 * The parameter data type.
	 */

	private String dataType = null;

	/**
	 * Whether this parameter is optional.
	 */

	private Boolean isOptional;

	/**
	 * Whether this parameter allows null.
	 */

	private Boolean allowNull;

	/**
	 * Whether this parameter is an input parameter.
	 */

	private Boolean isInput;

	/**
	 * Whether this parameter is an output parameter.
	 */

	private Boolean isOutput;

	/**
	 * The default value of the parameter.
	 */

	private String defaultValue;

	public String getStructName( )
	{
		return STRUCT_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( POSITION_MEMBER.equals( propName ) )
			return position;
		if ( NAME_MEMBER.equals( propName ) )
			return name;
		if ( DATA_TYPE_MEMBER.equals( propName ) )
			return dataType;
		if ( IS_OPTIONAL_MEMBER.equals( propName ) )
			return isOptional;
		if ( DEFAULT_VALUE_MEMBER.equals( propName ) )
			return defaultValue;
		if ( IS_NULLABLE_MEMBER.equals( propName )
				|| ALLOW_NULL_MEMBER.equals( propName ) )
			return allowNull;
		if ( IS_INPUT_MEMBER.equals( propName ) )
			return isInput;
		if ( IS_OUTPUT_MEMBER.equals( propName ) )
			return isOutput;

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
		if ( POSITION_MEMBER.equals( propName ) )
			position = (Integer) value;
		else if ( NAME_MEMBER.equals( propName ) )
			name = (String) value;
		else if ( DATA_TYPE_MEMBER.equals( propName ) )
			dataType = (String) value;
		else if ( IS_OPTIONAL_MEMBER.equals( propName ) )
			isOptional = (Boolean) value;
		else if ( DEFAULT_VALUE_MEMBER.equals( propName ) )
			defaultValue = (String) value;
		else if ( IS_NULLABLE_MEMBER.equals( propName )
				|| ALLOW_NULL_MEMBER.equals( propName ) )
			allowNull = (Boolean) value;
		else if ( IS_INPUT_MEMBER.equals( propName ) )
			isInput = (Boolean) value;
		else if ( IS_OUTPUT_MEMBER.equals( propName ) )
			isOutput = (Boolean) value;
		else
			assert false;
	}

	/**
	 * Whether the parameter is optional.
	 * 
	 * @return whether the parameter is optional
	 */

	public boolean isOptional( )
	{
		return ( (Boolean) getProperty( null, IS_OPTIONAL_MEMBER ) )
				.booleanValue( );
	}

	/**
	 * Sets whether the parameter is optional.
	 * 
	 * @param value
	 *            the value to set
	 */

	public void setIsOptional( boolean value )
	{
		setProperty( IS_OPTIONAL_MEMBER, Boolean.valueOf( value ) );
	}

	/**
	 * Sets the default value of the input parameter.
	 * 
	 * @param expr
	 *            the default value
	 */

	public void setDefaultValue( String expr )
	{
		setProperty( DEFAULT_VALUE_MEMBER, expr );
	}

	/**
	 * Gets the default value of the input parameter.
	 * 
	 * @return the default value
	 */

	public String getDefaultValue( )
	{
		return (String) getProperty( null, DEFAULT_VALUE_MEMBER );
	}

	/**
	 * Checks whether this parameter is an input parameter.
	 * 
	 * @return <code>true</code> if it is an input parameter. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isInput( )
	{
		return ( (Boolean) getProperty( null, IS_INPUT_MEMBER ) )
				.booleanValue( );
	}

	/**
	 * Sets whether this parameter is an input parameter.
	 * 
	 * @param isInput
	 *            <code>true</code> if it is an input parameter. Otherwise
	 *            <code>false</code>.
	 */

	public void setIsInput( boolean isInput )
	{
		setProperty( IS_INPUT_MEMBER, Boolean.valueOf( isInput ) );
	}

	/**
	 * Checks whether the value of this parameter can be <code>null</code>.
	 * 
	 * @return <code>true</code> if the value can be <code>null</code>.
	 *         Otherwise <code>false</code>.
	 * @deprecated Use <code>allowNull()</code>
	 */

	public boolean isNullable( )
	{
		return allowNull( );
	}

	/**
	 * Sets whether the value of this parameter can be <code>null</code>.
	 * 
	 * @param isNullable
	 *            <code>true</code> if the value can be <code>null</code>.
	 *            Otherwise <code>false</code>.
	 * @deprecated Use <code>setAllowNull(boolean)</code>
	 */

	public void setIsNullable( boolean isNullable )
	{
		setAllowNull( isNullable );
	}

	/**
	 * Checks whether the value of this parameter can be <code>null</code>.
	 * 
	 * @return <code>true</code> if the value can be <code>null</code>.
	 *         Otherwise <code>false</code>.
	 */

	public boolean allowNull( )
	{
		return ( (Boolean) getProperty( null, ALLOW_NULL_MEMBER ) )
				.booleanValue( );
	}

	/**
	 * Sets whether the value of this parameter can be <code>null</code>.
	 * 
	 * @param allowNull
	 *            <code>true</code> if the value can be <code>null</code>.
	 *            Otherwise <code>false</code>.
	 */

	public void setAllowNull( boolean allowNull )
	{
		setProperty( ALLOW_NULL_MEMBER, Boolean.valueOf( allowNull ) );
	}

	/**
	 * Checks whether this parameter is an output parameter.
	 * 
	 * @return <code>true</code> if it is an output parameter. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isOutput( )
	{
		return ( (Boolean) getProperty( null, IS_OUTPUT_MEMBER ) )
				.booleanValue( );
	}

	/**
	 * Sets whether this parameter is an output parameter.
	 * 
	 * @param isOutput
	 *            <code>true</code> if it is an output parameter. Otherwise
	 *            <code>false</code>.
	 */

	public void setIsOutput( boolean isOutput )
	{
		setProperty( IS_OUTPUT_MEMBER, Boolean.valueOf( isOutput ) );
	}

	/**
	 * Returns the parameter data type.
	 * 
	 * @return the parameter dataType
	 */

	public String getDataType( )
	{
		return (String) getProperty( null, DATA_TYPE_MEMBER );
	}

	/**
	 * Sets the parameter data type.
	 * 
	 * @param dataType
	 *            the data type to set
	 */

	public void setDataType( String dataType )
	{
		setProperty( DATA_TYPE_MEMBER, dataType );
	}

	/**
	 * Returns the parameter name.
	 * 
	 * @return the parameter name
	 */

	public String getName( )
	{
		return (String) getProperty( null, NAME_MEMBER );
	}

	/**
	 * Sets the parameter name.
	 * 
	 * @param name
	 *            the name to set
	 */

	public void setName( String name )
	{
		setProperty( NAME_MEMBER, name );
	}

	/**
	 * Returns the position of this parameter.
	 * 
	 * @return the position of this parameter
	 */

	public Integer getPosition( )
	{
		return (Integer) getProperty( null, POSITION_MEMBER );
	}

	/**
	 * Sets the position of this parameter.
	 * 
	 * @param position
	 *            the position to set
	 */

	public void setPosition( Integer position )
	{
		setProperty( POSITION_MEMBER, position );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.model.api.SimpleValueHandle,
	 *      int)
	 */

	public StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		return new DataSetParameterHandle( valueHandle, index );
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

		if ( StringUtil.isBlank( getName( ) ) )
		{
			list.add( new PropertyValueException( element, getDefn( )
					.getMember( NAME_MEMBER ), getName( ),
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED ) );
		}
		return list;
	}
}