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
class ParameterHint
{
	private String m_name;
	private int m_position;
	private Class m_dataType;
	
	/**
	 * Constructs a <code>ParameterHint</code> with the specified name.
	 * @param parameterName	the parameter name.
	 * @throws IllegalArgumentException	if the parameter name is null or 
	 * 									empty.
	 */
	ParameterHint( String parameterName )
	{
		if( parameterName == null || parameterName.length() == 0 )
		{
			String localizedMessage = 
				DataResourceHandle.getInstance().getMessage( ResourceConstants.PARAMETER_NAME_CANNOT_BE_EMPTY_OR_NULL );
			throw new IllegalArgumentException( localizedMessage );
		}
		
		m_name = parameterName;
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
	}
}
