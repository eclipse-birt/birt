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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * <code>ParameterHint</code> provides hints to map static  
 * parameter definitions to runtime parameters.
 */
public class ParameterHint
{
	private String m_name;
	private int m_position;
	private Class m_dataType;
	private boolean m_isInputOptional;
	private String m_defaultInputValue;
	private boolean m_isInputMode;
	private boolean m_isOutputMode;
	private boolean m_isNullable;
	
	/**
	 * Constructs a <code>ParameterHint</code> with the specified name.
	 * @param parameterName	the parameter name.
	 * @param isInputMode	whether this is an input parameter.
	 * @param isOutputMode	whether this is an output parameter.
	 * @throws IllegalArgumentException	if the parameter name is null or 
	 * 									empty.
	 */
	public ParameterHint( String parameterName, boolean isInputMode, boolean isOutputMode )
	{
		if( parameterName == null || parameterName.length() == 0 )
		{
			String localizedMessage = 
				DataResourceHandle.getInstance().getMessage( ResourceConstants.PARAMETER_NAME_CANNOT_BE_EMPTY_OR_NULL );
			throw new IllegalArgumentException( localizedMessage );
		}
		
		m_name = parameterName;
		m_isInputMode = isInputMode;
		m_isOutputMode = isOutputMode;
		
		m_isInputOptional = true;
		m_isNullable = true;
	}

	/**
	 * Returns the parameter name for this parameter hint.
	 * @return	the name of the parameter.
	 */
	public String getName()
	{
		return m_name;
	}
	
	/**
	 * Sets the parameter 1-based position for this parameter hint.
	 * @param position	the 1-based position of the parameter.
	 * @throws IllegalArgumentException	if the parameter position is less 
	 * 									than 1.
	 */
	public void setPosition( int position )
	{
		if( position < 1 )
		{
			String localizedMessage = 
				DataResourceHandle.getInstance().getMessage( ResourceConstants.PARAMETER_POSITION_CANNOT_BE_LESS_THAN_ONE );
			throw new IllegalArgumentException( localizedMessage );
		}
		
		m_position = position;
	}
	
	/**
	 * Returns the parameter 1-based position for this parameter hint.
	 * @return	the 1-based position of the parameter; 0 if no position was 
	 * 			specified.
	 */
	public int getPosition()
	{
		return m_position;
	}
	
	/**
	 * Sets the data type for this parameter hint.
	 * @param dataType	the data type of the parameter.
	 */
	public void setDataType( Class dataType )
	{
		// data type for a hint may be null
		assert( dataType == null || 
		        dataType == Integer.class ||
		        dataType == Double.class ||
		        dataType == String.class ||
		        dataType == BigDecimal.class ||
		        dataType == Date.class ||
		        dataType == Time.class ||
		        dataType == Timestamp.class );
		
		m_dataType = dataType;
	}
	
	/**
	 * Returns the parameter data type for this parameter hint.
	 * @return	the data type of the parameter.
	 */
	public Class getDataType()
	{
		return m_dataType;
	}

	/**
	 * Sets whether the input parameter is optional.  Has 
	 * no effect on non-input parameters.
	 * @param isInputOptional	whether this input parameter is optional.
	 */
	public void setIsInputOptional( boolean isInputOptional )
	{
		if( m_isInputMode )
			m_isInputOptional = isInputOptional;
	}
	
	/**
	 * Returns whether the input parameter is optional.  Returns true 
	 * for non-input parameters.
	 * @return	true if the input parameter is optional or if this is a 
	 * 			non-input parameter, false otherwise.
	 */
	public boolean isInputOptional()
	{
		return ( m_isInputMode ) ? m_isInputOptional : true;
	}
	
	/**
	 * Sets whether the parameter can be null.
	 * @param isNullable	whether this parameter can be null.
	 */
	public void setIsNullable( boolean isNullable )
	{
		m_isNullable = isNullable;
	}
	
	/**
	 * Returns whether the parameter can be null.
	 * @return	true if the parameter can be null, false otherwise.
	 */
	public boolean isNullable()
	{
		return m_isNullable;
	}
	
	/**
	 * Sets the default value of the input parameter.  Has no effect on 
	 * non-input parameters.
	 * @param defaultInputValue	the default value.
	 */
	public void setDefaultInputValue( String defaultInputValue )
	{
		if( m_isInputMode )
			m_defaultInputValue = defaultInputValue;
	}
	
	/**
	 * Gets the default vlaue of the input parameter.  Returns null for 
	 * non-input parameters.
	 * @return	the default value of the input parameter, or null for 
	 * 			non-input parameters.
	 */
	public String getDefaultInputValue()
	{
		return ( m_isInputMode ) ? m_defaultInputValue : null;
	}
	
	/**
	 * Returns whether the parameter is an input parameter.
	 * @return	true if the parameter is an input parameter, false otherwise.
	 */
	public boolean isInputMode()
	{
		return m_isInputMode;
	}

	/**
	 * Returns whether the parameter is an output parameter.
	 * @return	true if the parameter is an output parameter, false otherwise.
	 */
	public boolean isOutputMode()
	{
		return m_isOutputMode;
	}
	
	/**
	 * Helper method to update this <code>ParameterHint</code> with 
	 * information from another <code>ParameterHint</code>.
	 * @param hint	the <code>ParameterHint</code> instance.
	 */
	void updateHint( ParameterHint hint )
	{
		m_name = hint.m_name;
		
		// don't update if the other hint has the default values
		if( hint.m_position != 0 )
			m_position = hint.m_position;
		
		if( hint.m_dataType != null )
			m_dataType = hint.m_dataType;
		
		m_isInputOptional = hint.m_isInputOptional;
		m_defaultInputValue = hint.m_defaultInputValue;
		m_isInputMode = hint.m_isInputMode;
		m_isOutputMode = hint.m_isOutputMode;
		m_isNullable = hint.m_isNullable;
	}
}
