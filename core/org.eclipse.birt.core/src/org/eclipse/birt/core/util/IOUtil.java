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

package org.eclipse.birt.core.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A util class to read or write primitive Java data type. Please notice, every
 * method has a stream which might be input stream or output stream as
 * parameters. This stream should be already added a buffered layer underlying
 * it.
 */
public class IOUtil
{

	/**
	 * Read an int value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static int readInt( InputStream inputStream )
			throws IOException
	{
		int ch1 = inputStream.read( );
		int ch2 = inputStream.read( );
		int ch3 = inputStream.read( );
		int ch4 = inputStream.read( );

		return ( ( ch1 << 24 ) + ( ch2 << 16 ) + ( ch3 << 8 ) + ( ch4 << 0 ) );
	}

	/**
	 * Write an int value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeInt( OutputStream outputStream, int value )
			throws IOException
	{
		outputStream.write( ( value >>> 24 ) & 0xFF );
		outputStream.write( ( value >>> 16 ) & 0xFF );
		outputStream.write( ( value >>> 8 ) & 0xFF );
		outputStream.write( ( value >>> 0 ) & 0xFF );
	}
	
	/**
	 * Read a bool value from an input stream
	 * 
	 * @param inputStream
	 * @return boolean value
	 * @throws IOException
	 */
	public final static boolean readBool( InputStream inputStream )
			throws IOException
	{
		return inputStream.read( ) == 0 ? false : true;
	}

	/**
	 * Write a boolean value to an output stream
	 * 
	 * @param outputStream
	 * @param bool
	 * @throws IOException
	 */
	public final static void writeBool( OutputStream outputStream, boolean bool )
			throws IOException
	{
		outputStream.write( bool == false ? 0 : 1 );
	}
	
	/**
	 * Read a float value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static float readFloat( DataInputStream inputStream )
			throws IOException
	{
		return inputStream.readFloat( );
	}

	/**
	 * Write a float value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeFloat( DataOutputStream outputStream,
			float value ) throws IOException
	{
		outputStream.writeFloat( value );
	}

	/**
	 * Read a double value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static double readDouble( DataInputStream inputStream )
			throws IOException
	{
		return inputStream.readDouble( );
	}

	/**
	 * Write a double value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeDouble( DataOutputStream outputStream,
			double value ) throws IOException
	{
		outputStream.writeDouble( value );
	}

	/**
	 * Read a long value from an input stream
	 * 
	 * @param inputStream
	 * @return int value
	 * @throws IOException
	 */
	public final static long readLong( DataInputStream inputStream )
			throws IOException
	{
		return inputStream.readLong( );
	}

	/**
	 * Write a long value to an output stream
	 * 
	 * @param outputStream
	 * @param value
	 * @throws IOException
	 */
	public final static void writeLong( DataOutputStream outputStream,
			long value ) throws IOException
	{
		outputStream.writeLong( value );
	}

	/**
	 * Read a String from an input stream
	 * 
	 * @param inputStream
	 * @return an String
	 * @throws IOException
	 */
	public final static String readString( DataInputStream inputStream )
			throws IOException
	{
		return inputStream.read( ) == 0 ? null : inputStream.readUTF( );
	}

	/**
	 * Write a String value to an output stream
	 * 
	 * @param outputStream
	 * @param str
	 * @throws IOException
	 */
	public final static void writeString( DataOutputStream outputStream,
			String str ) throws IOException
	{
		outputStream.write( str == null ? 0 : 1 );

		if ( str != null )
			outputStream.writeUTF( str );
	}

}