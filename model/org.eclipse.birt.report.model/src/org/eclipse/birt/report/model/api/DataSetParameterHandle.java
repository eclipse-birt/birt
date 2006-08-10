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
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

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
 * <dt><strong>Is Nullable </strong></dt>
 * <dd>whether the value of this parameter can be nullable.</dd>
 * 
 * <dt><strong>Is Input </strong></dt>
 * <dd>whether this parameter is an input parameter.</dd>
 * 
 * <dt><strong>Is Output </strong></dt>
 * <dd>whether this parameter is an output parameter.</dd>
 * </dl>
 * 
 */

public class DataSetParameterHandle extends StructureHandle
{

	/**
	 * Constructs the handle of data set parameter.
	 * 
	 * @param valueHandle
	 *            the value handle for data set parameter list of one property
	 * @param index
	 *            the position of this data set parameter in the list
	 */

	public DataSetParameterHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Returns the data type of this parameter. The possible values are:
	 * 
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
	 * @return the data type of this parameter.
	 */

	public String getDataType( )
	{
		return getStringProperty( DataSetParameter.DATA_TYPE_MEMBER );
	}

	/**
	 * Sets the data type of this parameter. The allowed values are:
	 * 
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
	 *             if the value is not in the above list.
	 */

	public void setDataType( String dataType ) throws SemanticException
	{
		setProperty( DataSetParameter.DATA_TYPE_MEMBER, dataType );
	}

	/**
	 * Returns the parameter name.
	 * 
	 * @return the parameter name
	 */

	public String getName( )
	{
		return getStringProperty( DataSetParameter.NAME_MEMBER );
	}

	/**
	 * Sets the parameter name.
	 * 
	 * @param name
	 *            the name to set
	 * @throws SemanticException
	 *             value required exception
	 */

	public void setName( String name ) throws SemanticException
	{
		String oldName = getName( );

		setProperty( DataSetParameter.NAME_MEMBER, name );

		if ( oldName != null )
		{
			updateParamBindings( oldName, name );
		}
	}

	private void updateParamBindings( String oldParamName, String newParamName )
	{
		DataSetHandle.DataSetParametersPropertyHandle propHandle = (DataSetHandle.DataSetParametersPropertyHandle) getElementHandle( )
				.getPropertyHandle( getPropertyDefn( ).getName( ) );

		propHandle.updateParamBindings( oldParamName, newParamName );

	}

	/**
	 * Returns the position of this parameter in parameter list.
	 * 
	 * @return the position of this parameter.
	 */

	public Integer getPosition( )
	{
		return (Integer) getProperty( DataSetParameter.POSITION_MEMBER );
	}

	/**
	 * Sets the position of this parameter in parameter list.
	 * 
	 * @param position
	 *            the position to set
	 */

	public void setPosition( Integer position )
	{
		setPropertySilently( DataSetParameter.POSITION_MEMBER, position );
	}

	/**
	 * Whether the parameter is optional.
	 * 
	 * @return whether the parameter is optional
	 */

	public boolean isOptional( )
	{
		return ( (Boolean) getProperty( DataSetParameter.IS_OPTIONAL_MEMBER ) )
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
		setPropertySilently( DataSetParameter.IS_OPTIONAL_MEMBER, Boolean
				.valueOf( value ) );
	}

	/**
	 * Sets the default value of the input parameter.
	 * 
	 * @param expr
	 *            the default value
	 */

	public void setDefaultValue( String expr )
	{
		setPropertySilently( DataSetParameter.DEFAULT_VALUE_MEMBER, expr );
	}

	/**
	 * Gets the default value of the input parameter.
	 * 
	 * @return the default value
	 */

	public String getDefaultValue( )
	{
		return (String) getProperty( DataSetParameter.DEFAULT_VALUE_MEMBER );
	}

	/**
	 * Checks whether this parameter is an input parameter.
	 * 
	 * @return <code>true</code> if it is an input parameter. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isInput( )
	{
		return ( (Boolean) getProperty( DataSetParameter.IS_INPUT_MEMBER ) )
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
		setPropertySilently( DataSetParameter.IS_INPUT_MEMBER, Boolean
				.valueOf( isInput ) );
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
		return ( (Boolean) getProperty( DataSetParameter.ALLOW_NULL_MEMBER ) )
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
		setPropertySilently( DataSetParameter.ALLOW_NULL_MEMBER, Boolean
				.valueOf( allowNull ) );
	}

	/**
	 * Checks whether this parameter is an output parameter.
	 * 
	 * @return <code>true</code> if it is an output parameter. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isOutput( )
	{
		return ( (Boolean) getProperty( DataSetParameter.IS_OUTPUT_MEMBER ) )
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
		setPropertySilently( DataSetParameter.IS_OUTPUT_MEMBER, Boolean
				.valueOf( isOutput ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.StructureHandle#getMember(java.lang.String)
	 */
	public MemberHandle getMember( String memberName )
	{
		StructPropertyDefn memberDefn = (StructPropertyDefn) getDefn( )
				.getMember( memberName );
		if ( memberDefn == null )
			return null;

		if ( DesignSchemaConstants.NAME_ATTRIB.equalsIgnoreCase( memberName ) )
			return new NameMemberHandle( this, memberDefn );
		else
			return new MemberHandle( this, memberDefn );

	}

	/**
	 * Returns the native data type.
	 * 
	 * @return the parameter native data type.
	 * 
	 * @deprecated
	 */

	public Integer getNativeDataType( )
	{
		return null;
	}

	/**
	 * Sets the parameter native data type.
	 * 
	 * @param dataType
	 *            the native data type to set.
	 * 
	 * @deprecated
	 */

	public void setNativeDataType( Integer dataType )
	{

	}

	/**
	 * Represents the member handle which handles the "name" member in the data
	 * set parameter structure.
	 */

	final static class NameMemberHandle extends MemberHandle
	{

		DataSetParameterHandle paramHandle = null;

		/**
		 * Constructs a member handle with the given structure handle and the
		 * member property definition.
		 * 
		 * @param structHandle
		 *            a handle to the structure
		 * @param member
		 *            definition of the member within the structure
		 */

		public NameMemberHandle( StructureHandle structHandle,
				StructPropertyDefn member )
		{
			super( structHandle, member );
			paramHandle = (DataSetParameterHandle) structHandle;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.SimpleValueHandle#setValue(java.lang.Object)
		 */

		public void setValue( Object value ) throws SemanticException
		{
			String oldName = this.getStringValue( );

			getModule( ).getActivityStack( ).startTrans( );

			try
			{
				super.setValue( value );
			}
			catch ( PropertyValueException e )
			{
				getModule( ).getActivityStack( ).rollback( );
				throw e;
			}

			// Drop the parameter binding for data set parameters

			DataSetHandle.DataSetParametersPropertyHandle propHandle = (DataSetHandle.DataSetParametersPropertyHandle) getElementHandle( )
					.getPropertyHandle( getPropertyDefn( ).getName( ) );

			propHandle.updateParamBindings( oldName, value.toString( ) );

			getModule( ).getActivityStack( ).commit( );

		}
	}

}