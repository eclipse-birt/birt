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

package org.eclipse.birt.report.model.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.command.LibraryReloadedEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests whether report design can handle the cases of loading libraires.
 */

public class ReloadLibraryTest extends BaseTestCase
{

	/**
	 * Test reloading a library.
	 * <p>
	 * 1) If library is reloaded, its element references are updated.
	 * <p>
	 * <strong>Case1:</strong>
	 * <p>
	 * Design.table1 -> lib1.libTable1
	 * <p>
	 * The structure is synchronized with table in library. Local values of
	 * virtual elements are kept.
	 * <p>
	 * <strong>Case2:</strong>
	 * <p>
	 * lib1.theme1.style1
	 * <p>
	 * If style is removed, the design can only find the style name, not style
	 * instance.
	 * <p>
	 * <strong>Case3:</strong>
	 * <p>
	 * name space
	 * <p>
	 * If the libTable1 drops 2 rows, Designl.table1 will not see these rows any
	 * more. And names of report elements in these rows are removed from the
	 * namespace.
	 * <p>
	 * 
	 * @throws Exception
	 */

	public void testReloadLibrary( ) throws Exception
	{
		openDesign( "DesignToReloadLibrary.xml" ); //$NON-NLS-1$

		// tests in name sapces,

		NameSpace ns = designHandle.getModule( ).getNameSpace(
				ReportDesign.ELEMENT_NAME_SPACE );
		assertEquals( 7, ns.getCount( ) );

		// tests element references.

		TableHandle table1 = (TableHandle) designHandle.findElement( "table1" ); //$NON-NLS-1$
		LabelHandle label1 = (LabelHandle) designHandle.findElement( "label1" ); //$NON-NLS-1$

		assertEquals( ColorPropertyType.RED, table1
				.getStringProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.RED, label1
				.getStringProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.RED, label1
				.getStringProperty( Style.COLOR_PROP ) );

		// verify resolved element references.

		TableHandle parent = (TableHandle) table1.getExtends( );
		assertNotNull( parent );

		RowHandle rowHandle = (RowHandle) table1.getHeader( ).get( 0 );
		CellHandle cellHandle = (CellHandle) rowHandle.getCells( ).get( 0 );
		assertEquals( cellHandle.getStringProperty( StyleHandle.COLOR_PROP ),
				ColorPropertyType.BLUE );

		TableHandle table2 = (TableHandle) designHandle.findElement( "table2" );//$NON-NLS-1$
		assertNotNull( table2.getExtends( ) );
		assertEquals( table2.getStringProperty( StyleHandle.COLOR_PROP ),
				ColorPropertyType.LIME );

		// make modification on its library.

		openLibrary( "LibraryToReload.xml" );//$NON-NLS-1$

		TableHandle libTable1 = (TableHandle) libraryHandle
				.findElement( "libTable1" ); //$NON-NLS-1$

		// drop the style1 in the theme

		( (ThemeHandle) libraryHandle.getThemes( ).get( 0 ) ).getStyles( )
				.drop( 0 );

		// drop 1st and 2nd rows in table detail.

		libTable1.getDetail( ).drop( 0 );
		libTable1.getDetail( ).drop( 0 );

		assertEquals( 0, libTable1.getDetail( ).getCount( ) );

		// parent element is removed

		TableHandle libTable2 = (TableHandle) libraryHandle
				.findElement( "libTable2" ); //$NON-NLS-1$
		libTable2.drop( );
		
		libraryHandle.save( );

		// setup the listener

		MyLibraryListener libraryListener = new MyLibraryListener( );
		designHandle.addListener( libraryListener );

		designHandle.reloadLibrary( libraryHandle );

		assertEquals( 1, libraryListener.events.size( ) );
		assertTrue( libraryListener.events.get( 0 ) instanceof LibraryReloadedEvent );

		// test the count in namespace

		assertEquals( 3, ns.getCount( ) );

		// test element references. Theme was dropped so that cannot get color.

		assertEquals( ColorPropertyType.BLACK, table1
				.getStringProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.BLACK, label1
				.getStringProperty( Style.COLOR_PROP ) );

		assertNull( label1.getStyle( ) );
		assertEquals( "style1", label1 //$NON-NLS-1$
				.getStringProperty( StyledElement.STYLE_PROP ) );

		// test the structure change.

		assertEquals( 0, table1.getDetail( ).getCount( ) );
		assertEquals( 1, table1.getHeader( ).getCount( ) );

		// test undo/redo, activity stack is flused after reloadLibrary.

		assertFalse( designHandle.getCommandStack( ).canRedo( ) );
		assertFalse( designHandle.getCommandStack( ).canUndo( ) );

		// recover the original library file.

		recoverOriginalLibrary( "LibraryToReload_backup.xml", //$NON-NLS-1$
				"LibraryToReload.xml" ); //$NON-NLS-1$

		save( ); 
		compareTextFile( "DesignToReloadLibrary_golden.xml" ); //$NON-NLS-1$
	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>remove the table lib1.libTable1 from the library and add a grid
	 * lib1.libTable1. Design.table1 can not be resolved to gri lib1.libTable1.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testReloadLibraryWithInvalidExtends( ) throws Exception
	{
		openDesign( "DesignToReloadLibrary.xml" ); //$NON-NLS-1$

		// make modification on its library.

		openLibrary( "LibraryToReload.xml" );//$NON-NLS-1$

		libraryHandle.findElement( "libTable2" ).drop( ); //$NON-NLS-1$

		GridHandle grid = libraryHandle.getElementFactory( ).newGridItem(
				"libTable2", 2, 2 ); //$NON-NLS-1$

		libraryHandle.getComponents( ).add( grid );
		libraryHandle.save( );

		designHandle.reloadLibrary( libraryHandle );

		TableHandle table2 = (TableHandle) designHandle.findElement( "table2" );//$NON-NLS-1$
		assertNull( table2.getExtends( ) );

		assertEquals( "Lib1.libTable2", table2.getElement( ).getExtendsName( ) ); //$NON-NLS-1$

		// recover the original library file.

		recoverOriginalLibrary( "LibraryToReload_backup.xml", //$NON-NLS-1$
				"LibraryToReload.xml" ); //$NON-NLS-1$

	}

	/**
	 * Reloads the library with exceptions.
	 * 
	 * @throws Exception
	 */

	public void testReloadLibraryWithException( ) throws Exception
	{
		openDesign( "DesignToReloadLibrary.xml" ); //$NON-NLS-1$

		// make modification on its library.

		openLibrary( "Library_1.xml" );//$NON-NLS-1$

		try
		{
			designHandle.reloadLibrary( libraryHandle );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals( LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND,
					e.getErrorCode( ) );
		}

		recoverOriginalLibrary( "LibraryToReload_backup.xml", //$NON-NLS-1$
				"LibraryToReload.xml" ); //$NON-NLS-1$

		openLibrary( "LibraryToReload.xml" ); //$NON-NLS-1$
		File f = new File( getClassFolder( ) + INPUT_FOLDER
				+ "LibraryToReload.xml" ); //$NON-NLS-1$
		if ( f.exists( ) )
			f.delete( );

		designHandle.reloadLibrary( libraryHandle );
		assertNull( designHandle.findElement( "table1" ).getExtends( ) ); //$NON-NLS-1$
		assertNotNull( designHandle
				.findElement( "table1" ).getStringProperty( DesignElementHandle.EXTENDS_PROP ) ); //$NON-NLS-1$

		File f1 = new File( getClassFolder( ) + INPUT_FOLDER
				+ "LibraryToReload_errors.xml" ); //$NON-NLS-1$

		FileInputStream fis = new FileInputStream( f1 );
		FileOutputStream fos = new FileOutputStream( f );

		byte[] data = new byte[10000];
		fis.read( data );
		fos.write( data );

		fis.close( );
		fos.close( );

		try
		{
			designHandle.reloadLibrary( libraryHandle );
			fail( );
		}
		catch ( LibraryException e )
		{
			save( ); 
		}

		recoverOriginalLibrary( "LibraryToReload_backup.xml", //$NON-NLS-1$
				"LibraryToReload.xml" ); //$NON-NLS-1$

		compareTextFile( "DesignToReloadLibrary_golden_1.xml"); //$NON-NLS-1$
	}

	/**
	 * Test reloading a library, in which there is only a label.
	 * <p>
	 * <strong>Case1:</strong>
	 * <p>
	 * lib1.label1
	 * <p>
	 * if change the color and text of the label, after the loading, it should
	 * show the effect.
	 * <p>
	 * 
	 * @throws Exception
	 */

	public void testReloadLibrary1( ) throws Exception
	{
		openDesign( "DesignToReloadLibrary1.xml" ); //$NON-NLS-1$
		LabelHandle label1 = (LabelHandle) designHandle.findElement( "label1" ); //$NON-NLS-1$

		assertEquals( "aaa", label1.getText( ) ); //$NON-NLS-1$

		openLibrary( "LibraryToReload1.xml" ); //$NON-NLS-1$

		LabelHandle libLabel1 = (LabelHandle) libraryHandle
				.findElement( "libLabel1" ); //$NON-NLS-1$

		libLabel1.setText( "bbb" ); //$NON-NLS-1$
		libLabel1 = (LabelHandle) libraryHandle.findElement( "libPageLabel1" ); //$NON-NLS-1$

		libLabel1.setText( "ccc" ); //$NON-NLS-1$

		libraryHandle.save( );

		designHandle.reloadLibrary( libraryHandle );
		libraryHandle = designHandle.getLibrary( "Lib1" ); //$NON-NLS-1$

		libLabel1 = (LabelHandle) libraryHandle.findElement( "libLabel1" ); //$NON-NLS-1$

		assertEquals( "bbb", libLabel1.getText( ) ); //$NON-NLS-1$
		assertEquals( "bbb", label1.getText( ) ); //$NON-NLS-1$

		SimpleMasterPageHandle page = (SimpleMasterPageHandle) designHandle
				.findMasterPage( "My Page" ); //$NON-NLS-1$
		assertEquals( 1, page.getPageHeader( ).getCount( ) );
		libLabel1 = (LabelHandle) page.getPageHeader( ).get( 0 );
		assertEquals( "ccc", libLabel1.getText( ) ); //$NON-NLS-1$

		recoverOriginalLibrary( "LibraryToReload1_backup.xml", //$NON-NLS-1$
				"LibraryToReload1.xml" ); //$NON-NLS-1$
	}

	/**
	 * Test reloading a library, in which there is only a label.
	 * <p>
	 * <strong>Case1:</strong>
	 * <p>
	 * both resource path and report folder has the library file.
	 * <p>
	 * Changed resource path to null. And try to reload(). No exception. And the
	 * library location becomes library in the report folder.
	 * <p>
	 * 
	 * @throws Exception
	 */

	public void testReloadLibrary2( ) throws Exception
	{
		String fileName = getClassFolder( ) + INPUT_FOLDER;
		sessionHandle = new DesignEngine( new DesignConfig( ) )
				.newSessionHandle( null );
		assertNotNull( sessionHandle );
		sessionHandle.setResourceFolder( getClassFolder( ) + "/../api" //$NON-NLS-1$
				+ INPUT_FOLDER );

		designHandle = sessionHandle.openDesign( fileName
				+ "DesignToReloadLibrary.xml" ); //$NON-NLS-1$
		design = (ReportDesign) designHandle.getModule( );

		libraryHandle = designHandle.getLibrary( "Lib1" ); //$NON-NLS-1$		
		assertNotNull( libraryHandle );

		String location1 = libraryHandle.getModule( ).getLocation( );
		sessionHandle.setResourceFolder( null );

		designHandle.reloadLibrary( libraryHandle );

		libraryHandle = designHandle.getLibrary( "Lib1" ); //$NON-NLS-1$
		assertNotNull( libraryHandle );

		String location2 = libraryHandle.getModule( ).getLocation( );

		assertFalse( location1.equalsIgnoreCase( location2 ) );
	}
	

	private static class MyLibraryListener implements Listener
	{

		List events = new ArrayList( );

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt.report.model.core.DesignElement,
		 *      org.eclipse.birt.report.model.activity.NotificationEvent)
		 */

		public void elementChanged( DesignElementHandle focus,
				NotificationEvent ev )
		{
			events.add( ev );
		}
	}
	

	/**
	 * Restores changed library to the original one.
	 * 
	 * @throws Exception
	 *             any error occurs
	 */

	private void recoverOriginalLibrary( String backup, String target )
			throws Exception
	{
		openLibrary( backup );
		libraryHandle.save( );
	}
	

	/**
	 * Tests needSave method.
	 * 
	 * Only change happens directly on report design, isDirty mark of report
	 * design is true. So when library changed, isDirty mark of report design
	 * should be false.
	 * 
	 * <ul>
	 * <li>reload error library and throw out exception</li>
	 * <li>isDirty not changed</li>
	 * 
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testErrorLibraryNeedsSave( ) throws Exception
	{
		openDesign( "DesignWithSaveStateTest.xml" ); //$NON-NLS-1$
		openLibrary( "LibraryWithSaveStateTest.xml" );//$NON-NLS-1$

		LabelHandle labelHandle = designHandle.getElementFactory( ).newLabel(
				"new test label" );//$NON-NLS-1$
		designHandle.getBody( ).add( labelHandle );

		assertTrue( designHandle.needsSave( ) );
		assertTrue( designHandle.getCommandStack( ).canUndo( ) );
		assertFalse( designHandle.getCommandStack( ).canRedo( ) );

		ActivityStack stack = (ActivityStack) designHandle.getCommandStack( );

		File f = new File( getClassFolder( ) + INPUT_FOLDER
				+ "LibraryWithSaveStateTest.xml" );//$NON-NLS-1$
		RandomAccessFile raf = new RandomAccessFile( f, "rw" );//$NON-NLS-1$

		// Seek to end of file
		raf.seek( 906 );

		// Append to the end
		raf.writeBytes( "<label id=\"21\"/>" );//$NON-NLS-1$
		raf.close( );

		// reloadlibrary

		try
		{
			designHandle.reloadLibrary( libraryHandle );
			fail( );
		}
		catch ( DesignFileException e )
		{

		}

		assertTrue( stack.canUndo( ) );
		assertFalse( stack.canRedo( ) );
		assertEquals( 1, stack.getCurrentTransNo( ) );
		assertTrue( designHandle.needsSave( ) );

		// restore file

		libraryHandle.close( );
		recoverOriginalLibrary(
				"LibraryWithSaveStateTest_BackUP.xml", "LibraryWithSaveStateTest.xml" );//$NON-NLS-1$//$NON-NLS-2$

	}
}