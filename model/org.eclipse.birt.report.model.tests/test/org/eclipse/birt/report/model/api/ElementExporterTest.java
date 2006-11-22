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

package org.eclipse.birt.report.model.api;

import java.io.File;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.metadata.PeerExtensionLoader;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.ModelUtil;

import com.ibm.icu.util.ULocale;

/**
 * Tests exporting one element in design file or library file to library file.
 */

public class ElementExporterTest extends BaseTestCase
{

	/**
	 * Tests exporting one label to library file.
	 * 
	 * @throws Exception
	 *             if any exception.
	 */

	public void testExportingLabel( ) throws Exception
	{
		openDesign( "ElementExporterTest.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "label1" ); //$NON-NLS-1$

		ElementExportUtil.exportElement( labelHandle, libraryHandle, false );

		saveLibrary( );
		assertTrue( compareFile( "ElementExporterTestLibrary_golden_1.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests exporting one label with style to library file.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testExportingLabelWithStyle( ) throws Exception
	{
		openDesign( "ElementExporterTest.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "label2" ); //$NON-NLS-1$

		ElementExportUtil.exportElement( labelHandle, libraryHandle, false );

		saveLibrary( );
		assertTrue( compareFile( "ElementExporterTestLibrary_golden_2.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests exporting one label with extends to library file.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testExportingLabelWithExtends( ) throws Exception
	{
		openDesign( "ElementExporterTest.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "label3" ); //$NON-NLS-1$

		ElementExportUtil.exportElement( labelHandle, libraryHandle, false );

		saveLibrary( );
		assertTrue( compareFile( "ElementExporterTestLibrary_golden_3.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests exporting table to library file.
	 * 
	 * @throws Exception
	 *             if any exception.
	 */

	public void testExportingTable( ) throws Exception
	{
		openDesign( "ElementExporterTest.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		TableHandle tableHandle = (TableHandle) designHandle
				.findElement( "table1" ); //$NON-NLS-1$

		ElementExportUtil.exportElement( tableHandle, libraryHandle, false );

		saveLibrary( );
		assertTrue( compareFile( "ElementExporterTestLibrary_golden_4.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests exporting the label which has duplicate name in library file.
	 * 
	 * @throws Exception
	 *             if any exception.
	 */

	public void testExportingDuplicateLabel( ) throws Exception
	{
		openDesign( "ElementExporterTest.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "label1" ); //$NON-NLS-1$

		// The label named "libLabel" already exists.

		labelHandle.setName( "libLabel" ); //$NON-NLS-1$

		try
		{
			ElementExportUtil.exportElement( labelHandle, libraryHandle, false );
			fail( );
		}
		catch ( Exception e )
		{
			assertTrue( e instanceof NameException );
		}
	}

	/**
	 * Tests exporting style to library file.
	 * 
	 * @throws Exception
	 *             if any exception.
	 */

	public void testExportingStyle( ) throws Exception
	{
		openDesign( "ElementExporterTest.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		SharedStyleHandle styleHandle = designHandle.findStyle( "style2" ); //$NON-NLS-1$

		ElementExportUtil.exportElement( styleHandle, libraryHandle, false );

		saveLibrary( );
		assertTrue( compareFile( "ElementExporterTestLibrary_golden_5.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests exporting data source/set to library file.
	 * 
	 * @throws Exception
	 *             if any exception.
	 */

	public void testExportingDataMumble( ) throws Exception
	{

		openDesign( "ElementExporterTest_5.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		DataSetHandle dataSetHandle = designHandle.findDataSet( "dataSet1" ); //$NON-NLS-1$

		ElementExportUtil.exportElement( dataSetHandle, libraryHandle, false );

		// The exported data set has unresolved data source

		dataSetHandle = libraryHandle.findDataSet( "dataSet1" ); //$NON-NLS-1$
		assertEquals( "dataSource1", dataSetHandle.getDataSourceName( ) ); //$NON-NLS-1$
		assertEquals( null, dataSetHandle.getDataSource( ) );

		DataSourceHandle dataSourceHandle = designHandle
				.findDataSource( "dataSource1" ); //$NON-NLS-1$

		ElementExportUtil
				.exportElement( dataSourceHandle, libraryHandle, false );

		saveLibrary( );
		assertTrue( compareFile( "ElementExporterTestLibrary_golden_6.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests exporting one structure to library file.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testExportingCustomColor( ) throws Exception
	{
		openDesign( "ElementExporterTest.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		PropertyHandle propHandle = designHandle
				.getPropertyHandle( Module.COLOR_PALETTE_PROP );
		CustomColorHandle colorHandle = (CustomColorHandle) propHandle
				.getAt( 0 );

		ElementExportUtil.exportStructure( colorHandle, libraryHandle, false );

		try
		{
			// This color is exported already, so the exception of duplicate
			// name will be thrown.

			ElementExportUtil.exportStructure( colorHandle, libraryHandle,
					false );
			fail( );
		}
		catch ( PropertyValueException e )
		{
			assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS,
					e.getErrorCode( ) );
		}

		saveLibrary( );
		assertTrue( compareFile( "ElementExporterTestLibrary_golden_7.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests exporting one label existing in one table.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testExportingOneLabelInTable( ) throws Exception
	{
		openDesign( "ElementExporterTest.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "innerLabel" ); //$NON-NLS-1$

		ElementExportUtil.exportElement( labelHandle, libraryHandle, false );

		saveLibrary( );
		assertTrue( compareFile( "ElementExporterTestLibrary_golden_8.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests exporting one label existing in masterpage.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testExportingOneLabelInMasterPage( ) throws Exception
	{
		openDesign( "ElementExporterTest.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "labelInMasterPage" ); //$NON-NLS-1$

		ElementExportUtil.exportElement( labelHandle, libraryHandle, false );

		saveLibrary( );
		assertTrue( compareFile( "ElementExporterTestLibrary_golden_9.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests exporting one element to new library file.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testExportingOneLabelToNewLibraryFile( ) throws Exception
	{
		openDesign( "ElementExporterTest.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "labelInMasterPage" ); //$NON-NLS-1$

		String libraryFile = getTempFolder( ) + OUTPUT_FOLDER
				+ "ElementExporterTestLibrary_out_10.xml"; //$NON-NLS-1$
		File file = new File( libraryFile );
		if ( file.exists( ) )
			file.delete( );

		ElementExportUtil.exportElement( labelHandle, libraryFile, false );
		assertTrue( compareFile(
				"ElementExporterTestLibrary_golden_10.xml", "ElementExporterTestLibrary_out_10.xml" ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * test to export a design handle to a given library file.
	 * 
	 * 
	 * <ul>
	 * <li>1. one sucessful case
	 * <li>2. one design file with template-data-set
	 * <li>3. one design file with template-label that directly resides in body
	 * <li>3. one design file with template-label that resides in the cell slot
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testExportDesignToNewLibraryFile( ) throws Exception
	{
		testExportDesignToNewLibraryFile( "ElementExporterTest.xml", //$NON-NLS-1$
				"ElementExporterTestLibrary_out_12.xml" ); //$NON-NLS-1$

		assertTrue( compareFile( "ElementExporterTestLibrary_golden_12.xml", //$NON-NLS-1$
				"ElementExporterTestLibrary_out_12.xml" ) ); //$NON-NLS-1$

		String libraryFile = getTempFolder( ) + OUTPUT_FOLDER
				+ "ElementExporterTestLibrary_out_12.xml"; //$NON-NLS-1$

		ReportDesignHandle newDesign = sessionHandle.createDesign( );
		ElementFactory factory = newDesign.getElementFactory( );

		// duplicate master page names.
		newDesign.getMasterPages( ).add(
				factory.newGraphicMasterPage( "My Page" ) ); //$NON-NLS-1$

		try
		{
			// export with duplicate names.

			ElementExportUtil
					.exportDesign( newDesign, libraryFile, false, true );
			fail( );
		}
		catch ( NameException e )
		{
		}

		newDesign.getMasterPages( ).drop( 0 );
		newDesign.getBody( ).add( factory.newLabel( "" ) ); //$NON-NLS-1$

		try
		{
			// export element without a name.

			ElementExportUtil
					.exportDesign( newDesign, libraryFile, true, false );
			fail( );
		}
		catch ( IllegalArgumentException e )
		{
		}

		// library cannot have template label

		try
		{
			testExportDesignToNewLibraryFile( "ElementExporterTest1.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_13.xml" ); //$NON-NLS-1$
			fail( );
		}
		catch ( ContentException e )
		{
			assertEquals( ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e
					.getErrorCode( ) );

			assertTrue( e.getElement( ) instanceof Library );
		}

		// library cannot have template data set

		try
		{
			testExportDesignToNewLibraryFile( "ElementExporterTest2.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_14.xml" ); //$NON-NLS-1$
			fail( );
		}
		catch ( ContentException e )
		{
			assertEquals( ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e
					.getErrorCode( ) );

			assertTrue( e.getElement( ) instanceof Library );
		}

		// cell with template label cannot be exported to library

		try
		{
			testExportDesignToNewLibraryFile( "ElementExporterTest3.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_15.xml" ); //$NON-NLS-1$
			fail( );
		}
		catch ( ContentException e )
		{
			assertEquals(
					ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT,
					e.getErrorCode( ) );
			assertTrue( e.getElement( ) instanceof Cell );
		}

		// library cannot have template label

		try
		{
			testExportDesignToNewLibraryFile( "ElementExporterTest1.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_13.xml" ); //$NON-NLS-1$
			fail( );
		}
		catch ( ContentException e )
		{
			assertEquals( ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e
					.getErrorCode( ) );

			assertTrue( e.getElement( ) instanceof Library );
		}

		// library cannot have template data set

		try
		{
			testExportDesignToNewLibraryFile( "ElementExporterTest2.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_14.xml" ); //$NON-NLS-1$
			fail( );
		}
		catch ( ContentException e )
		{
			assertEquals( ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e
					.getErrorCode( ) );

			assertTrue( e.getElement( ) instanceof Library );
		}

		// cell with template label cannot be exported to library

		try
		{
			testExportDesignToNewLibraryFile( "ElementExporterTest3.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_15.xml" ); //$NON-NLS-1$
			fail( );
		}
		catch ( ContentException e )
		{
			assertEquals(
					ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT,
					e.getErrorCode( ) );
			assertTrue( e.getElement( ) instanceof Cell );
		}

		// library cannot have template label

		try
		{
			testExportDesignToNewLibraryFile( "ElementExporterTest1.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_13.xml" ); //$NON-NLS-1$
			fail( );
		}
		catch ( ContentException e )
		{
			assertEquals( ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e
					.getErrorCode( ) );

			assertTrue( e.getElement( ) instanceof Library );
		}

		// library cannot have template data set

		try
		{
			testExportDesignToNewLibraryFile( "ElementExporterTest2.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_14.xml" ); //$NON-NLS-1$
			fail( );
		}
		catch ( ContentException e )
		{
			assertEquals( ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e
					.getErrorCode( ) );

			assertTrue( e.getElement( ) instanceof Library );
		}

		// cell with template label cannot be exported to library

		try
		{
			testExportDesignToNewLibraryFile( "ElementExporterTest3.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_15.xml" ); //$NON-NLS-1$
			fail( );
		}
		catch ( ContentException e )
		{
			assertEquals(
					ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT,
					e.getErrorCode( ) );
			assertTrue( e.getElement( ) instanceof Cell );
		}

	}

	/**
	 * test to export a design handle to a given library file.
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @throws Exception
	 */

	private void testExportDesignToNewLibraryFile( String inputFile,
			String outputFile ) throws Exception
	{
		openDesign( inputFile, ULocale.ENGLISH );
		String libraryFile = getTempFolder( ) + OUTPUT_FOLDER + outputFile;
		File file = new File( libraryFile );
		if ( file.exists( ) )
			file.delete( );

		ElementExportUtil.exportDesign( designHandle, libraryFile, true, true );
	}

	/**
	 * Tests exporting one label with user property.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testExportingOneLabelWithUserProperty( ) throws Exception
	{
		openDesign( "ElementExporterTest.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "label5" ); //$NON-NLS-1$

		ElementExportUtil.exportElement( labelHandle, libraryHandle, false );

		saveLibrary( );
		assertTrue( compareFile( "ElementExporterTestLibrary_golden_11.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests char is exported with a significative name.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testExportingExtendItemWithSignificativeName( )
			throws Exception
	{
		new PeerExtensionLoader( ).load( );

		testExportDesignToNewLibraryFile( "ElementExporterTest_4.xml", //$NON-NLS-1$
				"ElementExporterTestLibrary_out_13.xml" ); //$NON-NLS-1$
		assertTrue( compareFile( "ElementExporterTestLibrary_golden_13.xml", //$NON-NLS-1$
				"ElementExporterTestLibrary_out_13.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests exporting EmbeddedImage to library file.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testExportingEmbeddedImage( ) throws Exception
	{
		openDesign( "DesignUsesLibraryEmbeddedImage.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		PropertyHandle propHandle = designHandle
				.getPropertyHandle( Module.IMAGES_PROP );
		EmbeddedImageHandle embeddedImageHandle = (EmbeddedImageHandle) propHandle
				.getAt( 0 );

		ElementExportUtil.exportStructure( embeddedImageHandle, libraryHandle,
				true );

		saveLibrary( );
		assertTrue( compareFile( "ElementExporterTestLibrary_golden_14.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Test 'hasLibrary' method in <code>ModelUtil</code> contains in
	 * exportDesign method in <code>ElementExportUtil</code>.
	 * 
	 * <ul>
	 * <li>test relative path. Report Design and library have the same absolute
	 * path</li>
	 * <li>test relative paht. Report Design and library haven't the same
	 * absolut path</li>
	 * <li>test relative path. Report Design and library have the same absolute
	 * path</li>
	 * <li></li>
	 * </ul>
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testHasLibrary( ) throws Exception
	{
		openDesign( "ModelUtilTest_hasContainLibrary.xml" ); //$NON-NLS-1$

		openLibrary( "Containlibrary.xml" );//$NON-NLS-1$
		try
		{
			ElementExportUtil.exportDesign( designHandle, libraryHandle, true,
					true );
			fail( );

		}
		catch ( SemanticException e )
		{
			assertEquals(
					LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY,
					e.getErrorCode( ) );
		}

		libraryHandle.setFileName( "notcontainlibrary.xml" );//$NON-NLS-1$
		ModelUtil.hasLibrary( designHandle, libraryHandle );
		libraryHandle.close( );

		openLibrary( "RelativeContainlibrary.xml" );//$NON-NLS-1$
		try
		{
			ElementExportUtil.exportDesign( designHandle, libraryHandle, true,
					true );
			fail( );

		}
		catch ( SemanticException e )
		{
			assertEquals(
					LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY,
					e.getErrorCode( ) );
		}

	}

	/**
	 * Tests exportDesign method
	 * @throws Exception
	 */
	
	public void testExportDesign( ) throws Exception
	{
		openDesign( "ElementExporterTest_6.xml" ); //$NON-NLS-1$
		openLibrary( "ElementExporterTestLibrary.xml" ); //$NON-NLS-1$

		DataItemHandle itemHandle = (DataItemHandle) designHandle
				.getElementByID( 5 );
		assertNull( itemHandle.getName( ) );

		ElementExportUtil
				.exportDesign( designHandle, libraryHandle, true, true );
		
		itemHandle =(DataItemHandle)libraryHandle.findElement( "NewData1" );//$NON-NLS-1$
		assertEquals( 1, itemHandle.getListProperty( "boundDataColumns" ).size( ) );//$NON-NLS-1$
		itemHandle =(DataItemHandle)libraryHandle.findElement( "NewData" );//$NON-NLS-1$
		
		
	}
}
