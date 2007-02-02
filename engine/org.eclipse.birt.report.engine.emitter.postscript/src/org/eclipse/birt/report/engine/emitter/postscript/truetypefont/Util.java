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

package org.eclipse.birt.report.engine.emitter.postscript.truetypefont;


public class Util
{

	private static int toInt( byte b )
	{
		return 0xff & b;
	}

	private static int mergeInt( int ch1, int ch2, int ch3, int ch4 )
	{
		return ( ( ch1 << 24 ) + ( ch2 << 16 ) + ( ch3 << 8 ) + ch4 );
	}

	private static byte[] get4Bytes( int data )
	{
		byte[] result = new byte[4];
		result[0] = (byte) ( data >> 24 );
		result[1] = (byte) ( data >> 16 );
		result[2] = (byte) ( data >> 8 );
		result[3] = (byte) data;
		return result;
	}

	private static byte[] get2Bytes( int data )
	{
		byte[] result = new byte[2];
		result[0] = (byte) ( data >> 8 );
		result[1] = (byte) data;
		return result;
	}

	public static  String toHexString( byte[] bytes )
	{
		StringBuffer result = new StringBuffer( );
		for ( int i = 0; i < bytes.length; i++ )
		{
			result.append( toHexString( bytes[i] ) );
		}
		return result.toString( );
	}

	private static  String toHexString( byte b )
	{
		String result;
		result = Integer.toHexString( toInt( b ) );
		if ( result.length( ) == 1 )
		{
			result = "0" + result;
		}
		return result;
	}

	public static  void putInt16( byte[] bytes, int index, int data )
	{
		assert bytes.length > index + 1;
		byte[] intBytes = get2Bytes( data );
		for ( int i = 0; i < 2; i++ )
		{
			bytes[index + i] = intBytes[i];
		}
	}

	public static void putInt32( byte[] bytes, int index, int data )
	{
		assert bytes.length > index + 3;
		byte[] intBytes = get4Bytes( data );
		for ( int i = 0; i < 4; i++ )
		{
			bytes[index + i] = intBytes[i];
		}
	}

	public static int getUnsignedShort( byte[] source, int index )
	{
		assert ( source.length >= index + 2 );
		return ( ( (int) source[index] ) << 8 ) + source[index + 1];
	}

	public static int getInt( byte[] source, int index )
	{
		assert ( source.length >= index + 4 );
		return mergeInt( toInt( source[index] ), toInt( source[index + 1] ), toInt( source[index + 2] ),
				toInt( source[index + 3] ) );
	}

	public static float div( int dividend, int divisor )
	{
		return ( (float) dividend ) / ( (float) divisor );
	}

}
