/*
 *****************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *
 ******************************************************************************
 */ 

package org.eclipse.birt.data.engine.odaconsumer;

import java.sql.Types;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.oda.IParameterMetaData;
import org.eclipse.birt.data.oda.OdaException;

/**
 * Contains the metadata information of a single parameter.
 */
public class ParameterMetaData
{
	private int m_position = -1;
	private String m_name;
	private int m_dataType = Types.NULL;
	private String m_nativeTypeName;
	private String m_defaultValue;
	private Boolean m_isOptional;
	private Boolean m_isInput;
	private Boolean m_isOutput;
	private int m_scale = -1;
	private int m_precision = -1;
	private Boolean m_isNullable;
	
	private ParameterMetaData( ParameterHint paramHint )
	{
		m_name = paramHint.getName();
		
		int position = paramHint.getPosition();
		m_position = ( position > 0 ) ? position : -1;
		
		Class paramHintType = paramHint.getDataType();
		
		m_dataType = 
			DataTypeUtil.toOdaType( paramHint.getDataType() );
	}
	
	ParameterMetaData( InputParameterHint inputParamHint )
	{
		this( (ParameterHint) inputParamHint );
		
		m_isOptional = Boolean.valueOf( inputParamHint.isOptional() );
		
		m_isInput = Boolean.TRUE;
	}
	
	ParameterMetaData( OutputParameterHint outputParamHint )
	{
		this( (ParameterHint) outputParamHint );
		
		m_isOutput = Boolean.TRUE;
	}
	
	ParameterMetaData( IParameterMetaData parameterMetaData, int index, 
	                   String driverName, String dataSetType ) 
		throws DataException
	{
		m_position = index;
		int nativeType = getRuntimeParameterType( parameterMetaData, index );
		
		// if the native type of the parameter is unknown (Types.NULL), then 
		// we can't simply default to the ODA character type because we may 
		// have a parameter hint that could provide the type
		if( nativeType != Types.NULL )
			m_dataType = 
				DriverManager.getInstance().getNativeToOdaMapping( driverName, 
				                                                   dataSetType, 
				                                                   nativeType );
		
		m_nativeTypeName = getRuntimeParamTypeName( parameterMetaData, index );
		
		int mode = getRuntimeParameterMode( parameterMetaData, index );
		if( mode == IParameterMetaData.parameterModeIn )
		{
			m_isInput = Boolean.TRUE;
			m_isOutput = Boolean.FALSE;
		}
		else if( mode == IParameterMetaData.parameterModeOut )
		{
			m_isInput = Boolean.FALSE;
			m_isOutput = Boolean.TRUE;
		}
		else if( mode == IParameterMetaData.parameterModeInOut )
		{
			m_isInput = Boolean.TRUE;
			m_isOutput = Boolean.TRUE;
		}
		
		m_scale = getRuntimeParameterScale( parameterMetaData, index );
		m_precision = getRuntimeParameterPrecision( parameterMetaData, index );
		
		int isNullable = getRuntimeIsNullable( parameterMetaData, index );
		if( isNullable == IParameterMetaData.parameterNullable )
			m_isNullable = Boolean.TRUE;
		else if( isNullable == IParameterMetaData.parameterNoNulls )
		{
			m_isNullable = Boolean.FALSE;
			m_isOptional = Boolean.FALSE;
		}
	}
	
	private void updateWith( ParameterHint paramHint )
	{
		// check that the position in the parameter hint either has not been 
		// set or this parameter metadata (from a hint) has not been set, or 
		// it's the same as the current parameter metadata.
		// note that if this parameter metadata came from the runtime parameter 
		// metadata, then it will have a valid position index.
		int position = paramHint.getPosition();
		assert( position <= 0 || m_position <= 0 || position == m_position );
		
		// if this parameter metadata (from a previous hint) doesn't have the 
		// position set and the new hint has a valid position, then update
		if( m_position <= 0 && position > 0 )
			m_position = position;
		
		String name = paramHint.getName();
		assert( name != null && name.length() > 0 );
		
		// if the name is already set, then ensure they're the same name
		assert( m_name == null || m_name.equals( name ) );
		
		if( m_name == null )
			m_name = name;
		
		// if the parameter type was previously unknown, then use the type from 
		// the hint if present or default to the character type
		if( m_dataType == Types.NULL )
		{
			Class paramHintType = paramHint.getDataType();
			m_dataType = 
				DataTypeUtil.toOdaType( paramHintType );
		}
	}
	
	void updateWith( InputParameterHint inputParamHint ) throws DataException
	{
		if( m_isInput == Boolean.FALSE )
			throw new DataException( ResourceConstants.NON_INPUT_PARAM_MERGE_WITH_INPUT_HINT, 
                                     new Object[] { inputParamHint.getName() } );
		
		if( m_isInput == null )		// was unknown
			m_isInput = Boolean.TRUE;
		
		updateWith( (ParameterHint) inputParamHint );
		
		m_isOptional = ( inputParamHint.isOptional() ) ? Boolean.TRUE : Boolean.FALSE;
	}
	
	void updateWith( OutputParameterHint outputParamHint ) throws DataException
	{
		if( m_isOutput == Boolean.FALSE )
			throw new DataException( ResourceConstants.NON_OUTPUT_PARAM_MERGE_WITH_OUTPUT_HINT, 
                                     new Object[] { outputParamHint.getName() } );
		
		if( m_isOutput == null )	// was unknown
			m_isOutput = Boolean.TRUE;
		
		updateWith( (ParameterHint) outputParamHint );
	}
	
	private int getRuntimeParameterType( IParameterMetaData parameterMetaData, 
										 int index ) throws DataException
	{
		try
		{
			return parameterMetaData.getParameterType( index );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_TYPE, ex, 
			                         new Object[] { new Integer( index ) } );
		}
	}
	
	private String getRuntimeParamTypeName( IParameterMetaData parameterMetaData,
											int index ) throws DataException
	{
		try
		{
			return parameterMetaData.getParameterTypeName( index );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_TYPE_NAME, ex, 
			                         new Object[] { new Integer( index ) } );
		}
	}
	
	private int getRuntimeParameterMode( IParameterMetaData parameterMetaData, 
										 int index ) throws DataException
	{
		try
		{
			return parameterMetaData.getParameterMode( index );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_MODE, ex, 
			                         new Object[] { new Integer( index ) } );
		}
	}

	private int getRuntimeParameterScale( IParameterMetaData parameterMetaData,
										  int index ) throws DataException
	{
		try
		{
			return parameterMetaData.getScale( index );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANOOT_GET_PARAMETER_SCALE, ex, 
			                         new Object[] { new Integer( index ) } );
		}
	}
	
	private int getRuntimeParameterPrecision( IParameterMetaData parameterMetaData, 
											  int index ) throws DataException
	{
		try
		{
			return parameterMetaData.getPrecision( index );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_PRECISION, ex, 
			                         new Object[] { new Integer( index ) } );
		}
	}
	
	private int getRuntimeIsNullable( IParameterMetaData parameterMetaData, 
									  int index ) throws DataException
	{
		try
		{
			return parameterMetaData.isNullable( index );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_ISNULLABLE, ex, 
			                         new Object[] { new Integer( index ) } );
		}
	}
	
	/**
	 * Returns the 1-based parameter index of this parameter.
	 * @return 	the 1-based parameter index of this parameter, or -1 if 
	 * 			the index is unspecified or unknown.
	 */
	public int getPosition()
	{
		return m_position;
	}
	
	/**
	 * Returns the parameter name of this parameter.
	 * @return	the parameter name of this parameter, or null if the 
	 * 			name is unspecified or unknown.
	 */
	public String getName()
	{
		return m_name;
	}
	
	/**
	 * Returns the java.sql.Types type of this parameter.
	 * @return	the data type of this parameter.
	 */
	public int getDataType()
	{
		// if this is still an unknown parameter type, that means there were 
		// no hints or the hints didn't provide a data type.  so default to 
		// the ODA character type
		return ( m_dataType == Types.NULL ) ? Types.CHAR : m_dataType;
	}
	
	/**
	 * Returns the data provider specific type name of this parameter.
	 * @return	the native data type name, or null if the type name 
	 * 			is unspecified or unknown.
	 */
	public String getNativeTypeName()
	{
		return m_nativeTypeName;
	}

	/**
	 * Returns whether this parameter is optional.
	 * @return	Boolean.TRUE if this parameter is optional. Boolean.FALSE if this 
	 * 			this parameter is not optional.  Null if it is unspecified or 
	 * 			unknown whether this parameter is optional.  The default value is 
	 * 			null.
	 */
	public Boolean isOptional()
	{
		return m_isOptional;
	}

	/**
	 * Returns the default input value for this parameter.
	 * @return	the default input value, or null if the default input value 
	 * 			is unspecified or unknown.
	 */
	public String getDefaultValue()
	{
		return m_defaultValue;
	}
	
	/**
	 * Returns whether this parameter is an input parameter. A parameter can be 
	 * of both input and output modes.
	 * @return	Boolean.TRUE if this parameter is an input parameter. Boolean.FALSE 
	 * 			if this parameter is not an input parameter. Null if it is unspecified 
	 * 			or unknown whether this parameter is an input parameter. The default 
	 * 			value is null.
	 */
	public Boolean isInputMode()
	{
		return m_isInput;
	}
	
	/**
	 * Returns whether this parameter is an output parameter. A parameter can be 
	 * of both input and output modes.
	 * @return	Boolean.TRUE if this parameter is an output parameter. Boolean.FALSE 
	 * 			if this parameter is not an output parameter. Null if it is unspecified 
	 * 			or unknown whether this parameter is an output parameter. The default 
	 * 			value is null.
	 */
	public Boolean isOutputMode()
	{
		return m_isOutput;
	}
	
	/**
	 * Returns the maximum number of digits to the right of the decimal point 
	 * for this parameter.
	 * @return	the scale of the parameter, or -1 if the scale is unspecified 
	 * 			or unknown.
	 */
	public int getScale()
	{
		return m_scale;
	}
	
	/**
	 * Returns the maximum number of decimal digits for this parameter.
	 * @return	the precision of the parameter, or -1 if the precision is 
	 * 			unspecified or unknown.
	 */
	public int getPrecision()
	{
		return m_precision;
	}
	
	/**
	 * Returns whether null values are allowed for this parameter.
	 * @return	Boolean.TRUE if null is allowed for this parameter. Boolean.FALSE 
	 * 			if null is not allowed for this parameter. Null if it is unspecified 
	 * 			or unknown whether null is allowed for this parameter. The default 
	 * 			value is null.
	 */
	public Boolean isNullable()
	{
		return m_isNullable;
	}
}
