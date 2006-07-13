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

package org.eclipse.birt.report.data.oda.xml.impl;

import java.sql.Types;
import java.util.HashMap;

import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * This class hosts the information of data types that are supported by flat
 * file driver
 */
public final class DataTypes
{
	//
	public static final int INT = Types.INTEGER;
	public static final int DOUBLE = Types.DOUBLE;
	public static final int STRING = Types.VARCHAR;
	public static final int DATE = Types.DATE;
	public static final int TIME = Types.TIME;
	public static final int TIMESTAMP = Types.TIMESTAMP;
	public static final int BLOB = Types.BLOB;
	public static final int BIGDECIMAL = Types.NUMERIC;

	//
	private static HashMap typeStringIntPair = new HashMap( );
	
	private static HashMap typeIntStringPair = new HashMap( );
	
	static
	{
		typeStringIntPair.put( "Int", new Integer( INT ) ); //$NON-NLS-1$
		typeStringIntPair.put( "Double", new Integer( DOUBLE ) ); //$NON-NLS-1$
		typeStringIntPair.put( "String", new Integer( STRING ) ); //$NON-NLS-1$
		typeStringIntPair.put( "Date", new Integer( DATE ) ); //$NON-NLS-1$
		typeStringIntPair.put( "Time", new Integer( TIME ) ); //$NON-NLS-1$
		typeStringIntPair.put( "Timestamp", new Integer( TIMESTAMP ) ); //$NON-NLS-1$
		typeStringIntPair.put( "Bigdecimal", new Integer( BIGDECIMAL ) ); //$NON-NLS-1$
		
		typeIntStringPair.put( new Integer( INT ),"Int" ); //$NON-NLS-1$
		typeIntStringPair.put( new Integer( DOUBLE ),"Double" ); //$NON-NLS-1$
		typeIntStringPair.put( new Integer( STRING ),"String" ); //$NON-NLS-1$
		typeIntStringPair.put( new Integer( DATE ),"Date" ); //$NON-NLS-1$
		typeIntStringPair.put( new Integer( TIME ),"Time" ); //$NON-NLS-1$
		typeIntStringPair.put( new Integer( TIMESTAMP ),"Timestamp" ); //$NON-NLS-1$
		typeIntStringPair.put( new Integer( BIGDECIMAL ),"Bigdecimal" ); //$NON-NLS-1$
	}

	/**
	 * Return the int which stands for the type specified by input argument
	 * 
	 * @param typeName
	 *            the String value of a Type
	 * @return the int which stands for the type specified by input typeName
	 * @throws OdaException
	 *             Once the input arguement is not a valid type name
	 */
	public static int getType( String typeName ) throws OdaException
	{
		String preparedTypeName = typeName == null ? "":typeName.trim( );
		if ( typeStringIntPair.containsKey( preparedTypeName ) )
			return ( (Integer) typeStringIntPair.get( preparedTypeName ) ).intValue( );
		throw new OdaException( ); //$NON-NLS-1$
	}
	
	/**
	 * Return the String which stands for the type specified by input argument
	 * 
	 * @param typeName
	 *            the int value of a Type
	 * @return the String which stands for the type specified by input typeName
	 * @throws OdaException
	 *             Once the input arguement is not a valid type name
	 */
	public static String getTypeString( int type ) throws OdaException
	{
		Integer typeInteger = new Integer( type );
		if ( typeIntStringPair.containsKey( typeInteger ) )
			return typeIntStringPair.get( typeInteger ).toString();
		throw new OdaException( ); //$NON-NLS-1$
	}
	/**
	 * Evalute whether an input String is a valid type that is supported by flat
	 * file driver
	 * 
	 * @param typeName
	 * @return
	 */
	public static boolean isValidType( String typeName )
	{
		return typeStringIntPair.containsKey( typeName.trim( ) );
	}

	private DataTypes( )
	{
	}
	
}