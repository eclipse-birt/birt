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
import java.util.Date;
import java.util.logging.Level;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;

/**
 * Utility class for handling data types in the ODI layer of the Data 
 * Engine.
 */
public final class DataTypeUtil
{
	// trace logging variables
	private static String sm_className = DataTypeUtil.class.getName();
	private static String sm_loggerName = ConnectionManager.sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance( sm_loggerName );

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
	 * Date -> java.util.Date<br>
	 * Time -> java.sql.Time<br>
	 * Timestamp -> java.sql.Timestamp<br>
	 * Blob -> org.eclipse.datatools.connectivity.oda.IBlob<br>
	 * Clob -> org.eclipse.datatools.connectivity.oda.IClob<br></i>
	 * @param odaDataType	the ODA data type.
	 * @return	the Java class that corresponds with the ODA data type.
	 * @throws IllegalArgumentException	if the ODA data type is not a supported type.
	 */
	public static Class toTypeClass( int odaDataType )
	{
		final String methodName = "toTypeClass";		

		if( odaDataType != Types.INTEGER &&
			odaDataType != Types.DOUBLE &&
			odaDataType != Types.CHAR &&
			odaDataType != Types.DECIMAL &&
			odaDataType != Types.DATE &&
			odaDataType != Types.TIME &&
			odaDataType != Types.TIMESTAMP &&
			odaDataType != Types.BLOB &&
			odaDataType != Types.CLOB &&
			odaDataType != Types.NULL )
		{
			String localizedMessage = 
				DataResourceHandle.getInstance().getMessage( ResourceConstants.UNRECOGNIZED_ODA_TYPE, 
				                                             new Object[] { new Integer( odaDataType ) } );
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
					"Invalid ODA data type: {0}", new Integer( odaDataType ) );
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
				
			case Types.BLOB:
				fieldClass = IBlob.class;
				break;
				
			case Types.CLOB:
				fieldClass = IClob.class;
				break;
				
			case Types.NULL:
				fieldClass = null;
				break;				    
		}
		
		if( sm_logger.isLoggable( Level.FINEST ) )
		    sm_logger.logp( Level.FINEST, sm_className, methodName, 
				"Converted from ODA data type {0} to Java data type class {1}.", 
				new Object[] { new Integer( odaDataType ), fieldClass } );
		
		return fieldClass;
	}
	
	/**
	 * Converts a Java class to an ODA data type. <br>
	 * <b>Java Class -> ODA Data Type</b><br>
	 * <i>java.lang.Integer -> Integer<br>
	 * java.lang.Double -> Double<br>
	 * java.lang.String -> Character<br>
	 * java.math.BigDecimal -> Decimal<br>
	 * java.util.Date -> Date<br>
	 * java.sql.Time -> Time<br>
	 * java.sql.Timestamp -> Timestamp<br>
	 * org.eclipse.datatools.connectivity.oda.IBlob -> Blob<br>
	 * org.eclipse.datatools.connectivity.oda.IClob -> Clob<br></i><br>
	 * All other Java classes are mapped to the ODA character type.
	 * @param javaClass	the Java class.
	 * @return	the ODA data type that maps to the Java class.
	 */
	public static int toOdaType( Class javaClass )
	{
		final String methodName = "toOdaType";		

		int odaType = Types.CHAR;	// default
		
		// returns Types.CHAR if the hint didn't have data type information
		if( javaClass == null )
		    odaType = Types.CHAR;		
		else if( javaClass == Integer.class )
		    odaType = Types.INTEGER;
		else if( javaClass == Double.class )
		    odaType = Types.DOUBLE;
		else if( javaClass == BigDecimal.class )
		    odaType = Types.DECIMAL;
		else if( javaClass == String.class )
		    odaType = Types.CHAR;
		else if( javaClass == Date.class )
		    odaType = Types.DATE;
		else if( javaClass == Time.class )
		    odaType = Types.TIME;
		else if( javaClass == Timestamp.class )
		    odaType = Types.TIMESTAMP;
		else if( javaClass == IBlob.class )
		    odaType = Types.BLOB;
		else if( javaClass == IClob.class )
		    odaType = Types.CLOB;
		
		if( sm_logger.isLoggable( Level.FINEST ) )
		    sm_logger.logp( Level.FINEST, sm_className, methodName, 
				"Converted from Java data type class {0} to ODA data type {1}.", 
				new Object[] { javaClass, new Integer( odaType ) } );

		return odaType;
	}
}
