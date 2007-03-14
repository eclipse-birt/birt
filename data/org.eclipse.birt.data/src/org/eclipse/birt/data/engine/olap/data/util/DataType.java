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

package org.eclipse.birt.data.engine.olap.data.util;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 */

public class DataType
{
	public static final int UNKNOWN_TYPE = -1;
	public static final int BOOLEAN_TYPE = 0;
	public static final int INTEGER_TYPE = 1;
	public static final int DOUBLE_TYPE = 2;
	public static final int STRING_TYPE = 3;
	public static final int DATE_TYPE = 4;
	public static final int BIGDECIMAL_TYPE = 5;
	public static final int BYTES_TYPE = 6;

	private static final String[] names = {
			"Boolean", "Integer", "Double", "String", "Date", "BigDecimal", "Bytes"
	};

	public static final String BOOLEAN_TYPE_NAME = names[0];
	public static final String INTEGER_TYPE_NAME = names[1];
	public static final String DOUBLE_TYPE_NAME = names[2];
	public static final String STRING_TYPE_NAME = names[3];
	public static final String DATE_TYPE_NAME = names[4];
	public static final String BIGDECIMAL_TYPE_NAME = names[5];
	public static final String BYTES_TYPE_NAME = names[6];

	private static final Class[] classes = {
			Boolean.class,
			Integer.class,
			Double.class,
			String.class,
			Date.class,
			BigDecimal.class,
			Bytes.class
	};

	/**
	 * Gets the description of a data type.
	 * @param typeCode Data type enumeration value
	 * @return Textual description of data type. "Unknown" if an undefined data type is passed in.
	 */
	public static String getName( int typeCode )
	{
		if ( typeCode < 0 || typeCode >= names.length )
		{
			return new String( "Unknown" );
		}
		return names[typeCode];
	}

	/**
	 * Gets the Java class used to represent the specified data type.
	 * @return Class for the specified data type. If data type is unknown or ANY, returns null. 
	 */
	public static Class getClass( int typeCode )
	{
		if ( typeCode < 0 || typeCode >= classes.length )
		{
			return null;
		}
		return classes[typeCode];
	}

	public static int getDataType( Class objClass )
	{
		for ( int i = 0; i < classes.length; i++ )
		{
			if ( classes[i].equals( objClass ) )
			{
				return i;
			}
		}
		return UNKNOWN_TYPE;
	}

}
