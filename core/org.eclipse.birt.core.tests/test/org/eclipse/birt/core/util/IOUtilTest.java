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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

import junit.framework.TestCase;

/**
 * Test case for IOUtil
 * @see org.eclipse.birt.core.util.IOUtil
 */
public class IOUtilTest extends TestCase
{
	// test value
	private Object[] testValues = new Object[]{
			new Integer( 1 ),
			new Float( 1 ),
			new Double( 1 ),
			new BigDecimal( "1.12" ),
			new Timestamp( System.currentTimeMillis( ) ),
			new Time( System.currentTimeMillis( ) ),
			new Date( System.currentTimeMillis( ) ),
			new Boolean( false ),
			"This is a test",
			new byte[]{ },
			new byte[]{
					1, 2, 3
			},
			new ArrayList( ),
			new HashMap( ),
			null
	};
	
	/*
	 * Test method for 'org.eclipse.birt.core.util.IOUtil.writeInt(OutputStream,
	 * int)'
	 */
	@Test
    public void testRWInt( ) throws IOException
	{
		int[] testValues = new int[]{
				1, 1000, 0, -1, -1000
		};

		final int size = 1000;
		ByteArrayOutputStream bos = null;
		byte[] content = null;
		ByteArrayInputStream bis = null;
		for ( int i = 0; i < testValues.length; i++ )
		{
			bos = new ByteArrayOutputStream( size );
			IOUtil.writeInt( bos, testValues[i] );
			content = bos.toByteArray( );

			bis = new ByteArrayInputStream( content );

			assertEquals( testValues[i], IOUtil.readInt( bis ) );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.util.IOUtil.readBool(InputStream)'
	 */
	@Test
    public void testRWBool( ) throws IOException
	{
		boolean[] testValues = new boolean[]{
				false, true
		};

		final int size = 1000;
		ByteArrayOutputStream bos = null;
		byte[] content = null;
		ByteArrayInputStream bis = null;
		for ( int i = 0; i < testValues.length; i++ )
		{
			bos = new ByteArrayOutputStream( size );
			IOUtil.writeBool( bos, testValues[i] );
			content = bos.toByteArray( );

			bis = new ByteArrayInputStream( content );

			assertEquals( testValues[i], IOUtil.readBool( bis ) );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.util.IOUtil.readFloat(DataInputStream)'
	 */
	@Test
    public void testRWFloat( ) throws IOException
	{
		float[] testValues = new float[]{
				(float) 1.1, 0, -1, (float) -1.1
		};

		final int size = 1000;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] content = null;
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		for ( int i = 0; i < testValues.length; i++ )
		{
			bos = new ByteArrayOutputStream( size );
			dos = new DataOutputStream( bos );
			IOUtil.writeFloat( dos, testValues[i] );
			content = bos.toByteArray( );

			bis = new ByteArrayInputStream( content );
			dis = new DataInputStream( bis );
			assertEquals( new Float( testValues[i] ),
					new Float( IOUtil.readFloat( dis ) ) );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.util.IOUtil.readDouble(DataInputStream)'
	 */
	@Test
    public void testRWDouble( ) throws IOException
	{
		double[] testValues = new double[]{
				(double) 1.1, 0, -1, (double) -1.1
		};

		final int size = 1000;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] content = null;
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		for ( int i = 0; i < testValues.length; i++ )
		{
			bos = new ByteArrayOutputStream( size );
			dos = new DataOutputStream( bos );
			IOUtil.writeDouble( dos, testValues[i] );
			content = bos.toByteArray( );

			bis = new ByteArrayInputStream( content );
			dis = new DataInputStream( bis );
			assertEquals( new Double( testValues[i] ),
					new Double( IOUtil.readDouble( dis ) ) );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.util.IOUtil.readLong(DataInputStream)'
	 */
	@Test
    public void testRWLong( ) throws IOException
	{
		long[] testValues = new long[]{
				1, 1000, 0, -1
		};

		final int size = 1000;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] content = null;
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		for ( int i = 0; i < testValues.length; i++ )
		{
			bos = new ByteArrayOutputStream( size );
			dos = new DataOutputStream( bos );
			IOUtil.writeLong( dos, testValues[i] );
			content = bos.toByteArray( );

			bis = new ByteArrayInputStream( content );
			dis = new DataInputStream( bis );
			assertEquals( testValues[i], IOUtil.readLong( dis ) );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.util.IOUtil.readString(DataInputStream)'
	 */
	@Test
    public void testRWString( ) throws IOException
	{
		String[] testValues = new String[]{
				null, "", "G", "GU", "GUI"
		};

		final int size = 1000;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] content = null;
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		for ( int i = 0; i < testValues.length; i++ )
		{
			bos = new ByteArrayOutputStream( size );
			dos = new DataOutputStream( bos );
			IOUtil.writeString( dos, testValues[i] );
			content = bos.toByteArray( );

			bis = new ByteArrayInputStream( content );
			dis = new DataInputStream( bis );
			assertEquals( testValues[i], IOUtil.readString( dis ) );
		}
	}
	/*
	 * Test method for 'org.eclipse.birt.core.util.IOUtil.readString(DataInputStream)'
	 */
	@Test
    public void testRWBytes( ) throws IOException
	{
		byte[] testValues = new byte[]{
				1,2,3,4,5,6,7,8,9,10
		};

		final int size = 1000;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] content = null;
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		bos = new ByteArrayOutputStream( size );
		dos = new DataOutputStream( bos );
		IOUtil.writeBytes( dos, testValues );
		content = bos.toByteArray( );

		bis = new ByteArrayInputStream( content );
		dis = new DataInputStream( bis );
		
		byte[] readValues = IOUtil.readBytes( dis );
		assertEquals( testValues.length, readValues.length );
		for ( int i = 0; i < testValues.length; i++ )
		{
			assertEquals( testValues[i], readValues[i] );
		}
		//assertEquals( testValues, readValues );
	}
	/*
	 * Test method for 'org.eclipse.birt.core.util.IOUtil.readObject(DataInputStream)'
	 */
	@Test
    public void testRWObject( ) throws IOException
	{
		final int size = 1000;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] content = null;
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		for ( int i = 0; i < testValues.length; i++ )
		{
			bos = new ByteArrayOutputStream( size );
			dos = new DataOutputStream( bos );
			IOUtil.writeObject( dos, testValues[i] );
			content = bos.toByteArray( );

			bis = new ByteArrayInputStream( content );
			dis = new DataInputStream( bis );
			
			if ( !( testValues[i] instanceof byte[] ) )
			{
				assertEquals( testValues[i], IOUtil.readObject( dis ) );
			}
			else
			{
				byte[] bytes = (byte[]) testValues[i];
				byte[] readBytes = (byte[]) IOUtil.readObject( dis );
				for ( int j = 0; j < bytes.length; j++ )
				{
					assertEquals( bytes[j], readBytes[j] );
				}
			}
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.util.IOUtil.readObject(DataInputStream)'
	 * Test method for 'org.eclipse.birt.core.util.IOUtil.writeObject(DataInputStream)'
	 * particularly, the read and write object is a String with length more than 65535
	 */
	@Test
    public void testRWLongString( ) throws IOException, FileNotFoundException
	{
		String begin = "��������The first several words for test";
		StringBuffer buffer = new StringBuffer( begin );
		for ( int i = 1; i < 65537; i++ )
		{
			buffer.append( i );
		}
		DataOutputStream dos = null;
		DataInputStream dis = null;
		boolean correct = true;

		final int size = 600000;
		byte[] content = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream( size );
		dos = new DataOutputStream( bos );
		IOUtil.writeString( dos, buffer.toString( ) );

		content = bos.toByteArray( );
		ByteArrayInputStream bis = new ByteArrayInputStream( content );
		dis = new DataInputStream( bis );

		String ret = IOUtil.readString( dis );
		StringBuffer buf = new StringBuffer( ret );
		if ( buf.length( ) != buffer.length( ) )
			correct = false;

		else
		{
			for ( int i = 1; i < buf.length( ); i++ )
			{
				if ( buffer.charAt( i ) != buf.charAt( i ) )
				{
					correct = false;
					break;
				}
			}
		}
		assertTrue( "I/O failed!!", correct );

		final int size2 = 600005;
		bos = new ByteArrayOutputStream( size2 );
		dos = new DataOutputStream( bos );

		IOUtil.writeObject( dos, ret );

		content = bos.toByteArray( );
		bis = new ByteArrayInputStream( content );
		dis = new DataInputStream( bis );

		Object obj = IOUtil.readObject( dis );

		assertTrue( "IOUtil test failed!!", ( (String) obj ).startsWith( begin ) );

		buf = new StringBuffer( (String) obj );
		if ( buf.length( ) != buffer.length( ) )
			correct = false;
		else
		{
			for ( int i = 1; i < buf.length( ); i++ )
			{
				if ( buffer.charAt( i ) != buf.charAt( i ) )
				{
					correct = false;
					break;
				}
			}
		}
		assertTrue( "IOUtil test failed!!", correct );
	}
	
	/*
	 * Test method for
	 * 'org.eclipse.birt.core.util.IOUtil.readList(DataInputStream)'
	 */
	@Test
    public void testRWList( ) throws IOException
	{
		List list = new ArrayList( );
		for ( int i = 0; i < testValues.length; i++ )
			list.add( testValues[i] );
		
		final int size = 1000;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] content = null;
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		
		bos = new ByteArrayOutputStream( size );
		dos = new DataOutputStream( bos );
		IOUtil.writeList( dos, list );
		content = bos.toByteArray( );

		bis = new ByteArrayInputStream( content );
		dis = new DataInputStream( bis );
		List readList = IOUtil.readList( dis );
		
		assertTrue( readList != null );
		assertTrue( list.size( ) == readList.size( ) );
		
		for ( int i = 0; i < testValues.length; i++ )
		{
			if ( !( testValues[i] instanceof byte[] ) )
			{
				assertEquals( testValues[i], readList.get( i ) );
			}
			else
			{
				byte[] bytes = (byte[]) testValues[i];
				byte[] readBytes = (byte[]) readList.get( i );
				for ( int j = 0; j < bytes.length; j++ )
				{
					assertEquals( bytes[j], readBytes[j] );
				}
			}
		}
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.util.IOUtil.readMap(DataInputStream)'
	 */
	@Test
    public void testRWMap( ) throws IOException
	{
		Object[] testKeys = this.testValues;
		
		Map map = new HashMap( );
		for ( int i = 0; i < testValues.length; i++ )
			map.put( testKeys[i], testValues[i] );
		
		final int size = 1000;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] content = null;
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		
		bos = new ByteArrayOutputStream( size );
		dos = new DataOutputStream( bos );
		IOUtil.writeMap( dos, map );
		content = bos.toByteArray( );

		bis = new ByteArrayInputStream( content );
		dis = new DataInputStream( bis );
		Map readMap = IOUtil.readMap( dis );
		
		assertTrue( readMap != null );
		
		Iterator it = map.entrySet( ).iterator( );
		while ( it.hasNext( ) )
		{
			Object ob = it.next( );
			Object value = map.get( ob );
			Object readValue = readMap.get( ob );
			if ( !( value instanceof byte[] ) )
			{
				assertEquals( value, readValue );
			}
			else
			{
				byte[] bytes = (byte[]) value;
				byte[] readBytes = (byte[]) readValue;
				for ( int j = 0; j < bytes.length; j++ )
				{
					assertEquals( bytes[j], readBytes[j] );
				}
			}
		}
	}
	
	/**
	 * Test java script object I/O
	 * @throws IOException
	 */
	@Test
    public void testNativeDate( ) throws IOException
	{
		final int size = 1000;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] content = null;
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		
		bos = new ByteArrayOutputStream( size );
		dos = new DataOutputStream( bos );
		
		Context cx = Context.enter( );
		Scriptable sharedScope = new ImporterTopLevel( cx );		
		Object ob = cx.evaluateString( sharedScope, "new Date", null, -1, null );
				
		IOUtil.writeObject( dos, ob );
		content = bos.toByteArray( );
		bis = new ByteArrayInputStream( content );
		dis = new DataInputStream( bis );
		Object ob2 = IOUtil.readObject( dis );
		
		assertTrue( ob instanceof IdScriptableObject );
		assertTrue( ob2 instanceof IdScriptableObject );
		
		assertEquals( JavascriptEvalUtil.convertJavascriptValue( ob ),
				JavascriptEvalUtil.convertJavascriptValue( ob2 ) );
		
		Context.exit( );
	}
	/**
	 * Test java script object I/O
	 * @throws IOException
	 */
	@Test
    public void testNativeJavaObject( ) throws IOException
	{
		final int size = 1000;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] content = null;
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		
		bos = new ByteArrayOutputStream( size );
		dos = new DataOutputStream( bos );
		
		Context cx = Context.enter( );
		Scriptable sharedScope = new ImporterTopLevel( cx );
		HashMap source = new HashMap( );
		source.put( "key1", "value1");
		source.put( "key2", "value2");
		Object ob = cx.javaToJS( source, sharedScope );
				
		IOUtil.writeObject( dos, ob );
		content = bos.toByteArray( );
		bis = new ByteArrayInputStream( content );
		dis = new DataInputStream( bis );
		Object ob2 = IOUtil.readObject( dis );
		
		assertTrue( ob2 instanceof HashMap );
		
		assertEquals( source, ob2 );
		
		Context.exit( );
	}
}
