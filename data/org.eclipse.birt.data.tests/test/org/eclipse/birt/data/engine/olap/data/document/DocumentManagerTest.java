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

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * 
 */

public class DocumentManagerTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
/*
	 * @see TestCase#tearDown()
	 */
@Test
    public void testFilesDocumentManager( ) throws IOException, DataException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		assertTrue( documentManager.createDocumentObject( "dimension_student" )!=null );
		assertTrue( documentManager.createDocumentObject( "dimension_student_index_ID" )!=null );
		assertTrue( documentManager.createDocumentObject( "dimension_time" )!=null );
		assertTrue( documentManager.createDocumentObject( "dimension_level_year" )!=null );
		IDocumentObject documentObject = documentManager.openDocumentObject( "dimension_student" );
		assertTrue( documentObject != null );
		testDocumentObject1( documentObject );
		documentObject.close( );
		documentObject = documentManager.openDocumentObject( "dimension_student_index_ID" );
		assertTrue( documentObject != null );
		testDocumentObject2( documentObject );
		documentObject.close( );
		documentObject = documentManager.openDocumentObject( "dimension_time" );
		assertTrue( documentObject != null );
		testDocumentObject3( documentObject );
		documentObject.close( );
		documentObject = documentManager.openDocumentObject( "dimension_level_year" );
		assertTrue( documentObject != null );
		testDocumentObject4( documentObject );
		documentObject.close( );
		documentManager.close( );
	}
	@Test
    public void testFilesDocumentManager2( ) throws IOException, DataException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		assertTrue( documentManager.createDocumentObject( "dimension_student" )!=null );
		IDocumentObject documentObject = documentManager.openDocumentObject( "dimension_student" );
		assertTrue( documentObject != null );
		documentObject.writeInt( 4 );
		byte[] b = new byte[10];
		assertEquals( documentObject.read( b, 0, b.length ), -1 );
		documentObject.close( );
		documentManager.close( );
	}
	@Test
    public void testFilesDocumentManager3( ) throws IOException, DataException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		assertTrue( documentManager.createDocumentObject( "dimension_student" )!=null );
		IDocumentObject documentObject = documentManager.openDocumentObject( "dimension_student" );
		assertTrue( documentObject != null );
		documentObject.writeInt( 4 );
		byte[] b = new byte[10];
		assertEquals( documentObject.read( b, 0, b.length ), -1 );
		documentObject.close( );
		documentManager.close( );
	}
	@Test
    public void testFilesDocumentManager4( ) throws IOException, DataException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		assertTrue( documentManager.createDocumentObject( "dimension_student" )!=null );
		IDocumentObject documentObject = documentManager.openDocumentObject( "dimension_student" );
		assertTrue( documentObject != null );
		byte[] b = new byte[100000];
		documentObject.write( b, 0 , b.length );
		documentObject.close( );
		documentManager.close( );
		
	}
	
	private void testDocumentObject1( IDocumentObject documentObject )
			throws IOException
	{
		int objectNumber = 200;
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeString( "string" + i );
		}
		documentObject.seek( 0 );
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readString( ), "string" + i );
		}
	}

	private void testDocumentObject2( IDocumentObject documentObject )
			throws IOException
	{
		int objectNumber = 200;
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeString( "string" + i );
		}
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeInt( i );
		}
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeBigDecimal( new BigDecimal( "1010101010101010101010"
					+ i ) );
		}
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeDouble( 100.0 + i );
		}
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeBoolean( i % 2 == 0 ? true : false );
		}
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeDate( new Date( 190001000 + i * 1000 ) );
		}

		documentObject.seek( 0 );

		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readString( ), "string" + i );
		}
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readInt( ), i );
		}
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readBigDecimal( ),
					new BigDecimal( "1010101010101010101010" + i ) );
		}
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readDouble( ), 100.0 + i, 0.001 );
		}
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readBoolean( ), i % 2 == 0 ? true
					: false );
		}
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeDate( new Date( 190001000 + i * 1000 ) );
		}
	}

	private void testDocumentObject3( IDocumentObject documentObject )
			throws IOException
	{
		int objectNumber = 200;
		for ( int i = 0; i < objectNumber; i++ )
		{
			documentObject.writeString( i + "string" + i );
			documentObject.writeInt( i );
			documentObject.writeBigDecimal( new BigDecimal( "1010101010101010101010"
					+ i ) );
			documentObject.writeDouble( 100.0 + i );
			documentObject.writeBoolean( i % 2 == 0 ? true : false );
			documentObject.writeDate( new Date( 1000 + i * 1000 ) );
		}

		documentObject.seek( 0 );

		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( documentObject.readString( ), i + "string" + i );
			assertEquals( documentObject.readInt( ), i );
			assertEquals( documentObject.readBigDecimal( ),
					new BigDecimal( "1010101010101010101010" + i ) );
			assertEquals( documentObject.readDouble( ), 100.0 + i, 0.001 );
			assertEquals( documentObject.readBoolean( ), i % 2 == 0 ? true
					: false );
			documentObject.writeDate( new Date( 1000 + i * 1000 ) );
		}

	}
	
	private void testDocumentObject4( IDocumentObject documentObject )
			throws IOException
	{
		documentObject.setLength( 4000 );
		documentObject.seek( 0 );
		documentObject.writeInt( 1 );
		documentObject.seek( 400 );
		documentObject.writeInt( 401 );
		documentObject.seek( 80 );
		documentObject.writeInt( 21 );
		documentObject.seek( 160 );
		documentObject.writeInt( 41 );
		documentObject.seek( 0 );
		assertEquals( documentObject.readInt( ), 1 );
		documentObject.seek( 80 );
		assertEquals( documentObject.readInt( ), 21 );
		documentObject.seek( 160 );
		assertEquals( documentObject.readInt( ), 41 );
	}

}
