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

package org.eclipse.birt.data.engine.executor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * <code>ResultFieldMetadata</code> contains metadata about a 
 * column that is needed by <code>ResultClass</code>.
 */
public class ResultFieldMetadata
{
	private int m_driverPosition;
	private String m_name;
	private String m_label;
	private String m_alias;
	private Class m_dataType;
	private boolean m_isCustom;

	public ResultFieldMetadata( int driverPosition, String name, 
						 		String label, Class dataType, boolean isCustom )
	{
		m_driverPosition = driverPosition;
		m_name = name;
		m_label = label;
		m_dataType = dataType;
		m_isCustom = isCustom;
	}
	
	// returns the driver position from the runtime metadata
	public int getDriverPosition()
	{
		return m_driverPosition;
	}
	
	public String getName()
	{
		return m_name;
	}
	
	public void setName( String name )
	{
		m_name = name;
	}
	
	public String getAlias()
	{
		return m_alias;
	}
	
	public void setAlias( String alias )
	{
		m_alias = alias;
	}
	
	public Class getDataType()
	{
		return m_dataType;
	}
	
	public void setDataType( Class dataType )
	{
		assert( dataType == Integer.class ||
		        dataType == Double.class ||
		        dataType == String.class ||
		        dataType == BigDecimal.class ||
		        dataType == Date.class ||
		        dataType == Time.class ||
		        dataType == Timestamp.class );
		
		m_dataType = dataType;
	}
	
	public String getLabel()
	{
		return m_label;
	}
	
	public boolean isCustom()
	{
		return m_isCustom;
	}
}
