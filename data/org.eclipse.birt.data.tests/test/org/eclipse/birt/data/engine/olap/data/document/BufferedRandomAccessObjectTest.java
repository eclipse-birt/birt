
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

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.document.BlockRandomAccessObject;
import org.eclipse.birt.data.engine.olap.data.document.BufferedRandomDataAccessObject;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.document.IObjectAllocTable;
import org.eclipse.birt.data.engine.olap.data.document.SimpleRandomAccessObject;
import org.eclipse.birt.data.engine.olap.data.util.BufferedRandomAccessFile;

import junit.framework.TestCase;


/**
 * 
 */

public class BufferedRandomAccessObjectTest extends TestCase
{
	IDocumentManager documentManager = null;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		documentManager = DocumentManagerFactory.createFileDocumentManager( );
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
	}
	
	public void testInteger( ) throws IOException
	{
		int objectNumber = 1001;
		assertTrue(documentManager.createDocumentObject( "testInteger" )!=null);
		IDocumentObject documentObject = documentManager.openDocumentObject( "testInteger" );
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeInt( i );
		}
		documentObject.seek( 0 );
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readInt( ), i );
		}
		documentObject.seek( 400 );
		assertEquals( documentObject.readInt( ), 100 );
		documentObject.seek( 804 );
		assertEquals( documentObject.readInt( ), 201 );
		assertEquals( documentObject.readInt( ), 202 );
		documentObject.seek( 2804 );
		documentObject.writeInt( 1000001 );
		assertEquals( documentObject.readInt( ), 702 );
		documentObject.seek( 2804 );
		assertEquals( documentObject.readInt( ), 1000001 );
		documentObject.close( );
	}

	public void testInteger1( ) throws IOException
	{
		int objectNumber = 1001;
		BufferedRandomDataAccessObject documentObject = new BufferedRandomDataAccessObject( 
				new SimpleRandomAccessObject( new File("D:\\tmp\\documents\\testInteger1"),
				"rw" ),
				1024 );
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeInt( i );
		}
		documentObject.seek( 0 );
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readInt( ), i );
		}
		documentObject.seek( 400 );
		assertEquals( documentObject.readInt( ), 100 );
		documentObject.seek( 804 );
		assertEquals( documentObject.readInt( ), 201 );
		assertEquals( documentObject.readInt( ), 202 );
		documentObject.seek( 2804 );
		documentObject.writeInt( 1000001 );
		assertEquals( documentObject.readInt( ), 702 );
		documentObject.seek( 2804 );
		assertEquals( documentObject.readInt( ), 1000001 );
		documentObject.close( );
	}
	
	public void testInteger2( ) throws IOException
	{
		BlockRandomAccessObject documentObject = new BlockRandomAccessObject( 
				new BufferedRandomAccessFile( new File("D:\\tmp\\documents\\testInteger1"),
				"rw", 1024 ),
				"testInteger2", 0, 0, new DocumentObjectAllocatedTable( ) );
		byte[] bytes = new byte[1024];
		bytes[0] = 1;
		bytes[1] = 2;
		documentObject.seek( 0 );
		documentObject.write( bytes, 0, bytes.length );
		documentObject.write( bytes, 0, bytes.length );
		documentObject.write( bytes, 0, bytes.length );
		bytes = new byte[932];
		documentObject.write( bytes, 0, bytes.length );
		
		bytes = new byte[1024];
		documentObject.seek( 0 );
		assertEquals(documentObject.read( bytes, 0, bytes.length ), 1024);
		assertEquals(bytes[0], 1);
		assertEquals(bytes[1], 2);
		documentObject.close( );
	}
	
	public void testInteger3( ) throws IOException
	{
		BlockRandomAccessObject documentObject = new BlockRandomAccessObject( 
				new BufferedRandomAccessFile( new File("D:\\tmp\\documents\\testInteger1"),
				"rw", 1024 ),
				"testInteger2", 0, 0, new DocumentObjectAllocatedTable( ) );
		byte[] bytes = new byte[1024];
		bytes[0] = 1;
		bytes[1] = 2;
		documentObject.seek( 0 );
		documentObject.write( bytes, 0, bytes.length );
		documentObject.write( bytes, 0, bytes.length );
		documentObject.write( bytes, 0, bytes.length );
		bytes = new byte[932];
		documentObject.write( bytes, 0, bytes.length );
		
		assertEquals(documentObject.read( bytes, 0, bytes.length ), -1);
		documentObject.close( );
	}

	public void testString( ) throws IOException
	{
		int objectNumber = 3000;
		assertTrue(documentManager.createDocumentObject( "testString" )!=null);
		IDocumentObject documentObject = documentManager.openDocumentObject( "testString" );
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeString( "string" + i );
		}
		documentObject.seek( 0 );
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readString( ), "string" + i );
		}
		documentObject.close( );
	}

	public void testBigDecimal( ) throws IOException
	{
		int objectNumber = 3000;
		assertTrue(documentManager.createDocumentObject( "testBigDecimal" )!=null);
		IDocumentObject documentObject = documentManager.openDocumentObject( "testBigDecimal" );
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeBigDecimal( new BigDecimal( "1010101010101010101010" + i ) );
		}
		documentObject.seek( 0 );
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readBigDecimal( ),
					new BigDecimal( "1010101010101010101010" + i ) );
		}
		documentObject.close( );
	}

	public void testDate( ) throws IOException
	{
		int objectNumber = 4101;
		assertTrue(documentManager.createDocumentObject( "testDate" )!=null);
		IDocumentObject documentObject = documentManager.openDocumentObject( "testDate" );
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeDate( new Date( 1900100000 + i * 1000 ) );
		}
		documentObject.seek( 0 );
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readDate( ), new Date( 1900100000 + i * 1000 ) );
		}
		documentObject.close( );
	}

	public void testMixed( ) throws IOException
	{
		int objectNumber = 1001;
		assertTrue(documentManager.createDocumentObject( "testMixed" )!=null);
		IDocumentObject documentObject = documentManager.openDocumentObject( "testMixed" );
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeInt( i );
		}
		documentObject.seek( 0 );
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readInt( ), i );
		}
		documentObject.writeBigDecimal( new BigDecimal( "1010101010101" ) );
		documentObject.writeDate( new Date( 12202000 ) );
		documentObject.writeString( "testString" );
		documentObject.writeShort( 1300 );
		documentObject.writeInt( 30000011 );
		documentObject.seek( 0 );
		documentObject.skipBytes( objectNumber * 4 );
		assertEquals( documentObject.readBigDecimal( ), new BigDecimal( "1010101010101" ) );
		assertEquals( documentObject.readDate( ), new Date( 12202000 ) );
		assertEquals( documentObject.readString( ), "testString" );
		assertEquals( documentObject.readShort( ), 1300 );
		assertEquals( documentObject.readInt( ), 30000011 );
	}
}

class DocumentObjectAllocatedTable implements IObjectAllocTable
{
	int maxBlockNumber = 0;
	public int allocateBlock( int blockNumber ) throws IOException
	{
		maxBlockNumber = Math.max( maxBlockNumber, blockNumber + 1 );
		return blockNumber + 1;
	}

	public int getNextBlock( int blockNumber ) throws IOException
	{
		if( blockNumber + 1 > maxBlockNumber )
			return 0;
		else
			return blockNumber + 1;
	}

	public void setObjectLength( String name, long length ) throws IOException
	{
		
	}
	
}