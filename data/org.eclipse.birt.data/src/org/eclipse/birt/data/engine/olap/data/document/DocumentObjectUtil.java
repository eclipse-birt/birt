
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.document;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.util.Bytes;
import org.eclipse.birt.data.engine.olap.data.util.DataType;

/**
 * 
 */

public class DocumentObjectUtil
{
	/**
	 * 
	 * @param documentObject
	 * @param dataType
	 * @param value
	 * @throws IOException
	 */
	public static void writeValue( IDocumentObject documentObject, int[] dataType, Object[] value ) throws IOException
	{
		for( int i=0;i<dataType.length;i++)
		{
			writeValue( documentObject, dataType[i], value[i] );
		}
	}
	/**
	 * 
	 * @param documentObject
	 * @param dataType
	 * @param value
	 * @throws IOException
	 */
	public static void writeValue( IDocumentObject documentObject, int dataType, Object value ) throws IOException
	{
		switch ( dataType )
		{
			case DataType.BOOLEAN_TYPE :
				documentObject.writeBoolean( ( (Boolean) value ).booleanValue( ) );
				break;
			case DataType.INTEGER_TYPE :
				documentObject.writeInt( ( (Integer) value ).intValue( ) );
				break;
			case DataType.DOUBLE_TYPE :
				documentObject.writeDouble( ( (Double) value ).doubleValue( ) );
				break;
			case DataType.STRING_TYPE :
				documentObject.writeString( ( (String) value ) );
				break;
			case DataType.DATE_TYPE :
				documentObject.writeDate( ( (Date) value ) );
				break;
			case DataType.BIGDECIMAL_TYPE :
				documentObject.writeBigDecimal( ( (BigDecimal) value ) );
				break;
			case DataType.BYTES_TYPE :
				documentObject.writeBytes( ( (Bytes) value ) );
				break;
			default :
				assert false;
				break;
		}
	}
	
	/**
	 * 
	 * @param documentObject
	 * @param dataType
	 * @return
	 * @throws IOException
	 */
	public static Object[] readValue( IDocumentObject documentObject, int[] dataType ) throws IOException
	{
		Object[] result = new Object[dataType.length];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = readValue( documentObject, dataType[i] );
		}
		return result;
	}
	/**
	 * 
	 * @param documentObject
	 * @param dataType
	 * @return
	 * @throws IOException
	 */
	public static Object readValue( IDocumentObject documentObject, int dataType ) throws IOException
	{
		switch ( dataType )
		{
			case DataType.BOOLEAN_TYPE :
				return new Boolean( documentObject.readBoolean( ) );
			case DataType.INTEGER_TYPE :
				return new Integer( documentObject.readInt( ) );
			case DataType.DOUBLE_TYPE :
				return new Double( documentObject.readDouble( ) );
			case DataType.STRING_TYPE :
				return documentObject.readString( );
			case DataType.DATE_TYPE :
				return documentObject.readDate( );
			case DataType.BIGDECIMAL_TYPE :
				return documentObject.readBigDecimal( );
			case DataType.BYTES_TYPE :
				return documentObject.readBytes( );
			default :
				assert false;
				return null;
		}
	}
}
