/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.odaconsumer;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * Utility class for handling data types in the ODI layer of the Data 
 * Engine.
 */
public final class DataTypeUtil
{
	private DataTypeUtil()
	{
		// not meant to be instantiated
	}

	/**
	 * Converts the ODA data type into the corresponding Java class. <br><br>
	 * <b>ODA Data Type -> Java Class</b><br>
	 * <i>Integer -> java.lang.Integer<br>
	 * Double -> java.lang.Double<br>
	 * Character -> java.lang.String<br>
	 * Decimal -> java.math.BigDecimal<br>
	 * Date -> java.sql.Date<br>
	 * Time -> java.sql.Time<br>
	 * Timestamp -> java.sql.Timestamp<br></i>
	 * @param odaDataType	the ODA data type.
	 * @return	the Java class that corresponds with the ODA data type.
	 * @throws IllegalArgumentException	if the ODA data type is not a supported type.
	 */
	public static Class toTypeClass( int odaDataType )
	{
		if( odaDataType != Types.INTEGER &&
			odaDataType != Types.DOUBLE &&
			odaDataType != Types.CHAR &&
			odaDataType != Types.DECIMAL &&
			odaDataType != Types.DATE &&
			odaDataType != Types.TIME &&
			odaDataType != Types.TIMESTAMP )
		{
			String localizedMessage = 
				DataResourceHandle.getInstance().getMessage( ResourceConstants.UNRECOGNIZED_ODA_TYPE, 
				                                             new Object[] { new Integer( odaDataType ) } );
			throw new IllegalArgumentException( localizedMessage );
		}
		
		Class fieldClass = null;
		switch( odaDataType )
		{
			case Types.INTEGER:
				fieldClass = Integer.class;
				break;
			
			case Types.DOUBLE:
				fieldClass = Double.class;
				break;
				
			case Types.CHAR:
				fieldClass = String.class;
				break;
				
			case Types.DECIMAL:
				fieldClass = BigDecimal.class;
				break;
				
			case Types.DATE:
				fieldClass = Date.class;
				break;
				
			case Types.TIME:
				fieldClass = Time.class;
				break;

			case Types.TIMESTAMP:
				fieldClass = Timestamp.class;
				break;
		}
		
		return fieldClass;
	}
	
	/**
	 * Converts a Java class to an ODA data type. <br>
	 * <b>Java Class -> ODA Data Type</b><br>
	 * <i>java.lang.Integer -> Integer<br>
	 * java.lang.Double -> Double<br>
	 * java.lang.String -> Character<br>
	 * java.math.BigDecimal -> Decimal<br>
	 * java.sql.Date -> Date<br>
	 * java.sql.Time -> Time<br>
	 * java.sql.Timestamp -> Timestamp<br></i><br>
	 * All other Java classes are mapped to the ODA character type.
	 * @param javaClass	the Java class.
	 * @return	the ODA data type that maps to the Java class.
	 */
	public static int toOdaType( Class javaClass )
	{
		// returns Types.CHAR if the hint didn't have data type information
		if( javaClass == null )
			return Types.CHAR;
		
		if( javaClass == Integer.class )
			return Types.INTEGER;
		else if( javaClass == Double.class )
			return Types.DOUBLE;
		else if( javaClass == BigDecimal.class )
			return Types.DECIMAL;
		else if( javaClass == String.class )
			return Types.CHAR;
		else if( javaClass == Date.class )
			return Types.DATE;
		else if( javaClass == Time.class )
			return Types.TIME;
		else if( javaClass == Timestamp.class )
			return Types.TIMESTAMP;
		else
			return Types.CHAR;
	}
}
