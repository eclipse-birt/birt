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

import java.io.EOFException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;


import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.util.Bytes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class FileDocumentManagerTest {

	protected IDocumentManager documentManager;
	private static final String DOCUMENT_OBJECT = "docObj";

	private int[] ints = {
			0, 1, 2, 3, 4, 5
	};
	private double[] doubles = {
			0.00, 1.11, 2.22, 3.33, 4.44, 5.55
	};
	private String[] strings = {
			"aaa", "bbb", "ccc", "ddd", "eee", "fff"
	};
	private boolean[] booleans = {
			true, false, false, true, true, true
	};
	private Date[] dates = {
			new Date( ),
			new Date( ),
			new Date( ),
			new Date( ),
			new Date( ),
			new Date( )
	};
	private BigDecimal[] bigDecimals = {
			new BigDecimal( 0.00 ),
			new BigDecimal( 1.11 ),
			new BigDecimal( 2.22 ),
			new BigDecimal( 3.33 ),
			new BigDecimal( 4.44 ),
			new BigDecimal( 5.55 )
	};
	private Bytes[] bytes = new Bytes[6];
	{
		for ( int i = 0; i < bytes.length; i++ )
			bytes[i] = new Bytes( generateBytes( generateRandomInt( 100 ) ) );
	}
	@Before
    public void fileDocumentManagerSetUp() throws Exception
	{
		documentManager = DocumentManagerFactory.createFileDocumentManager( );
	}
	@After
    public void fileDocumentManagerTearDown() throws Exception
	{
		documentManager.close( );
	}

	private IDocumentObject[] generateDocumentObjects( int length )
			throws IOException
	{
		String[] docObjNames = generateDocumentObjectName( length );
		IDocumentObject[] docObjs = new IDocumentObject[length];

		for ( int i = 0; i < docObjs.length; i++ )
		{
			documentManager.createDocumentObject( docObjNames[i] );
			docObjs[i] = openIDocumentObject( docObjNames[i] );
		}

		return docObjs;
	}

	protected IDocumentObject openIDocumentObject( String documentObjectName )
			throws IOException
	{
		return documentManager.openDocumentObject( documentObjectName );
	}

	private String[] generateDocumentObjectName( int length )
	{
		String[] docObjNames = new String[length];
		for ( int i = 0; i < docObjNames.length; i++ )
		{
			docObjNames[i] = DOCUMENT_OBJECT + i;
		}

		return docObjNames;
	}

	private byte[] generateBytes( int length )
	{
		byte[] bytes = new byte[length];
		for ( int i = 0; i < bytes.length; i++ )
		{
			bytes[i] = new Integer( i ).byteValue( );
		}

		return bytes;
	}
	
	private void closeDocumentObjects( IDocumentObject[] dos ) throws IOException
	{
		for ( IDocumentObject ido : dos )
		{
			ido.close( );
		}
	}

	protected int generateRandomInt( int limits )
	{
		return new Double( Math.random( ) * limits ).intValue( );
	}
	@Test
    public void testEOFException( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		long pointer = docObjs[0].getFilePointer( );

		docObjs[0].writeDouble( doubles[0] );
		docObjs[0].seek( pointer );
		docObjs[0].readDouble( );

		try
		{
			docObjs[0].readDouble( );
			fail( );
		}
		catch ( IOException e )
		{
			assertTrue( e instanceof EOFException );
		}
		closeDocumentObjects( docObjs );
	}
	@Test
    public void testZero( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		long pointer = docObjs[0].getFilePointer( );
		byte[] out = generateBytes( 0 );

		docObjs[0].write( out, 0, out.length );
		docObjs[0].seek( pointer );

		try
		{
			docObjs[0].readByte( );
			fail( );
		}
		catch ( IOException e )
		{
			assertTrue( e instanceof EOFException );
		}

		closeDocumentObjects( docObjs );
	}
	@Test
    public void testMinusOne( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		byte[] in = new byte[5];

		docObjs[0].writeInt( ints[1] );
		assertEquals( -1, docObjs[0].read( in, 0, in.length ) );

		closeDocumentObjects( docObjs );
	}
	@Test
    public void testDouble( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		long pointer = docObjs[0].getFilePointer( );

		for ( int i = 0; i < doubles.length; i++ )
			docObjs[0].writeDouble( doubles[i] );

		docObjs[0].seek( pointer );

		for ( int i = 0; i < doubles.length; i++ )
			assertEquals( docObjs[0].readDouble( ), doubles[i], 2 );

		closeDocumentObjects( docObjs );
	}
	@Test
    public void testInt( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		long pointer = docObjs[0].getFilePointer( );

		for ( int i = 0; i < ints.length; i++ )
			docObjs[0].writeInt( ints[i] );

		docObjs[0].seek( pointer );

		for ( int i = 0; i < ints.length; i++ )
			assertEquals( docObjs[0].readInt( ), ints[i] );

		closeDocumentObjects( docObjs );
	}
	@Test
    public void testString( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		long pointer = docObjs[0].getFilePointer( );

		for ( int i = 0; i < strings.length; i++ )
			docObjs[0].writeString( strings[i] );

		docObjs[0].seek( pointer );

		for ( int i = 0; i < strings.length; i++ )
			assertEquals( docObjs[0].readString( ), strings[i] );

		closeDocumentObjects( docObjs );
	}
	@Test
    public void testBoolean( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		long pointer = docObjs[0].getFilePointer( );

		for ( int i = 0; i < booleans.length; i++ )
			docObjs[0].writeBoolean( booleans[i] );

		docObjs[0].seek( pointer );

		for ( int i = 0; i < booleans.length; i++ )
			assertEquals( docObjs[0].readBoolean( ), booleans[i] );

		closeDocumentObjects( docObjs );
	}
	@Test
    public void testDate( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		long pointer = docObjs[0].getFilePointer( );

		for ( int i = 0; i < dates.length; i++ )
			docObjs[0].writeDate( dates[i] );

		docObjs[0].seek( pointer );

		for ( int i = 0; i < dates.length; i++ )
			assertEquals( docObjs[0].readDate( ), dates[i] );

		closeDocumentObjects( docObjs );

	}
	@Test
    public void testBigDecimal( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		long pointer = docObjs[0].getFilePointer( );

		for ( int i = 0; i < bigDecimals.length; i++ )
			docObjs[0].writeBigDecimal( bigDecimals[i] );

		docObjs[0].seek( pointer );

		for ( int i = 0; i < bigDecimals.length; i++ )
			assertEquals( docObjs[0].readBigDecimal( ), bigDecimals[i] );

		closeDocumentObjects( docObjs );

	}
	@Test
    public void testBytes( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		long pointer = docObjs[0].getFilePointer( );

		for ( int i = 0; i < bytes.length; i++ )
			docObjs[0].writeBytes( bytes[i] );

		docObjs[0].seek( pointer );

		for ( int i = 0; i < bytes.length; i++ )
			assertEquals( docObjs[0].readBytes( ), bytes[i] );

		closeDocumentObjects( docObjs );
	}
	@Test
    public void testShort( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		long pointer = docObjs[0].getFilePointer( );

		for ( int i = 0; i < ints.length; i++ )
			docObjs[0].writeShort( ints[i] );

		docObjs[0].seek( pointer );

		for ( int i = 0; i < ints.length; i++ )
			assertEquals( docObjs[0].readShort( ), ints[i] );

		closeDocumentObjects( docObjs );
	}
	@Test
    public void testMixed( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		long pointer = docObjs[0].getFilePointer( );

		for ( int i = 0; i < bigDecimals.length; i++ )
		{
			docObjs[0].writeBigDecimal( bigDecimals[i] );
			docObjs[0].writeBoolean( booleans[i] );
			docObjs[0].writeBytes( bytes[i] );
			docObjs[0].writeDate( dates[i] );
			docObjs[0].writeDouble( doubles[i] );
			docObjs[0].writeInt( ints[i] );
			docObjs[0].writeShort( ints[i] );
			docObjs[0].writeString( strings[i] );
		}

		docObjs[0].seek( pointer );

		for ( int i = 0; i < bigDecimals.length; i++ )
		{
			assertEquals( docObjs[0].readBigDecimal( ), bigDecimals[i] );
			assertEquals( docObjs[0].readBoolean( ), booleans[i] );
			assertEquals( docObjs[0].readBytes( ), bytes[i] );
			assertEquals( docObjs[0].readDate( ), dates[i] );
			assertEquals( docObjs[0].readDouble( ), doubles[i], 2 );
			assertEquals( docObjs[0].readInt( ), ints[i] );
			assertEquals( docObjs[0].readShort( ), ints[i] );
			assertEquals( docObjs[0].readString( ), strings[i] );
		}

		closeDocumentObjects( docObjs );
	}
	@Test
    public void testFilePointer( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 1 );
		byte[] out = generateBytes( 1023 );

		docObjs[0].write( out, 0, out.length );
		assertEquals( out.length, docObjs[0].getFilePointer( ) );

		docObjs[0].write( out, 0, 25 );
		assertEquals( 1048, docObjs[0].getFilePointer( ) );

		closeDocumentObjects( docObjs );
	}
	@Test
    public void testBuffer( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 2 );

		// test critial point 1024
		long pointer = docObjs[0].getFilePointer( );
		byte[] out = generateBytes( 1025 );

		docObjs[0].write( out, 0, 1023 );
		docObjs[0].write( out, 1023, 2 );
		docObjs[0].seek( pointer );

		for ( int i = 0; i < out.length; i++ )
			assertEquals( docObjs[0].readByte( ), out[i] );

		docObjs[0].close( );

		// test byteSize>1024
		pointer = docObjs[1].getFilePointer( );

		docObjs[1].write( out, 0, out.length );
		docObjs[1].seek( pointer );

		for ( int i = 0; i < out.length; i++ )
			assertEquals( docObjs[1].readByte( ), out[i] );

		closeDocumentObjects( docObjs );
	}
	@Test
    public void testChunk( ) throws IOException
	{
		IDocumentObject[] docObjs = generateDocumentObjects( 2 );
		long pointer0 = docObjs[0].getFilePointer( );
		long pointer1 = docObjs[1].getFilePointer( );
		byte[] out = generateBytes( 5120 );

		// docObjs[0] takes twice to overSize a single chunk
		// while docObjs[1] only takes once
		docObjs[0].write( out, 0, 4000 );
		docObjs[1].write( out, 0, 5120 );
		docObjs[0].write( out, 4000, 1120 );

		docObjs[0].seek( pointer0 );
		for ( int i = 0; i < out.length; i++ )
			assertEquals( docObjs[0].readByte( ), out[i] );

		docObjs[1].seek( pointer1 );
		for ( int i = 0; i < out.length; i++ )
			assertEquals( docObjs[1].readByte( ), out[i] );

		closeDocumentObjects( docObjs );
	}
	@Test
    public void testStressVariable( ) throws IOException
	{
		int length = 50;	//
		IDocumentObject[] docObjs = generateDocumentObjects( length );
		long[] pointer = new long[length];
		byte[][] out = new byte[length][];

		for ( int i = 0; i < docObjs.length; i++ )
		{
			out[i] = generateBytes( 1024 * generateRandomInt( 200 ) );
			pointer[i] = docObjs[i].getFilePointer( );
			docObjs[i].write( out[i], 0, out[i].length );
		}

		for ( int i = 0; i < docObjs.length; i++ )
		{
			docObjs[i].seek( pointer[i] );

			for ( int j = 0; j < out[i].length; j++ )
				assertEquals( docObjs[i].readByte( ), out[i][j] );

			docObjs[i].close( );
		}
	}
	@Test
    public void testStressFixed( ) throws IOException
	{
		int length = 100;	//10000
		IDocumentObject[] docObjs = generateDocumentObjects( length );
		long[] pointer = new long[length];
		byte[] out = generateBytes( 1024 * 100 );

		for ( int i = 0; i < docObjs.length; i++ )
		{
			pointer[i] = docObjs[i].getFilePointer( );
			docObjs[i].write( out, 0, out.length );
		}

		for ( int i = 0; i < docObjs.length; i++ )
		{
			docObjs[i].seek( pointer[i] );

			for ( int j = 0; j < out.length; j++ )
				assertEquals( docObjs[i].readByte( ), out[j] );

			docObjs[i].close( );
		}
	}
	@Test
    public void testLoadFileDocumentManager( ) throws IOException,
			DataException
	{
		documentManager.createDocumentObject( DOCUMENT_OBJECT );
		IDocumentObject obj = documentManager.openDocumentObject( DOCUMENT_OBJECT );
		obj.writeBoolean( true );
		obj.close( );
		documentManager.close( );

		documentManager = DocumentManagerFactory.loadFileDocumentManager( );
		obj = documentManager.openDocumentObject( DOCUMENT_OBJECT );
		assertTrue( obj.readBoolean( ) );
		obj.close( );
	}

}
