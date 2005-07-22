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

package org.eclipse.birt.report.tests.model.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TranslationHandle;
import org.eclipse.birt.report.model.api.activity.ActivityStack;
import org.eclipse.birt.report.model.api.command.CustomMsgException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * 
 * Tests cases for ReportDesignHandle.
 * <p>
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testConfigVars()}</td>
 * <td>Tests to read, remove, replace ConfigVars.</td>
 * <td>Operations are finished correctly and the output file matches the golden
 * file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testReportDesignOtherMethods()}</td>
 * <td>Tests to get element and design handle.</td>
 * <td>Returns the design and design handle correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Tests to get numbers of errors and warnings.</td>
 * <td>Returns numbers of errors and warnings correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testReportDesignProperties()}</td>
 * <td>Tests to get and set properties like base and default units.</td>
 * <td>Values are set correctly and the output file matches the golden file.
 * </td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testReportDesignSlots()}</td>
 * <td>Tests to get different kinds of slots like body, components, etc.</td>
 * <td>Information of slots matches with the input design file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testIteratorMethods()}</td>
 * <td>Tests to get iterators.</td>
 * <td>Information of iterators matches with the input design file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testTranslations()}</td>
 * <td>Tests to get translations.</td>
 * <td>Information of translations matches with the input design file.</td>
 * </tr>
 * 
 * </table>
 * 
 *  
 */
public class ReportDesignHandleTest extends BaseTestCase
{

	
	/**
	 * @param name
	 */
	public ReportDesignHandleTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public static Test suite(){
		return new TestSuite(ReportDesignHandleTest.class);	
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		openDesign( "ReportDesignHandleTest.xml" ); //$NON-NLS-1$
	}

	/**
	 * Tests cases for reading and setting ConfigVars.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testConfigVars( ) throws Exception
	{
		PropertyHandle configVars = designHandle
				.getPropertyHandle( ReportDesign.CONFIG_VARS_PROP );
		List list = configVars.getListValue( );
		assertEquals( 2, list.size( ) );

		ConfigVariable var = designHandle.findConfigVariable( "var1" ); //$NON-NLS-1$
		assertEquals( "mumble.jpg", var.getValue( ) ); //$NON-NLS-1$

		designHandle.dropConfigVariable( "var2" ); //$NON-NLS-1$

		list = configVars.getListValue( );
		assertEquals( 1, list.size( ) );

		ConfigVariable newvar = new ConfigVariable( );
		newvar.setName( "newvar2" ); //$NON-NLS-1$
		newvar.setValue( "new value 2" ); //$NON-NLS-1$
		designHandle.addConfigVariable( newvar );

		newvar = new ConfigVariable( );
		newvar.setName( "new var1" ); //$NON-NLS-1$
		newvar.setValue( "new value 1" ); //$NON-NLS-1$
		designHandle.replaceConfigVariable( var, newvar );

		list = configVars.getListValue( );
		assertEquals( 2, list.size( ) );
		designHandle.dropConfigVariable( "newvar2" ); //$NON-NLS-1$
		designHandle.dropConfigVariable( "new var1" ); //$NON-NLS-1$

		configVars.setValue( null );
		assertNull( configVars.getValue( ) );

		ConfigVariable structure3 = StructureFactory.createConfigVar( );
		structure3.setName( "myvar" ); //$NON-NLS-1$
		structure3.setValue( "my value" ); //$NON-NLS-1$		
		designHandle.addConfigVariable( structure3 );

		PropertyHandle propHandle = designHandle
				.getPropertyHandle( ReportDesign.CONFIG_VARS_PROP );
		Iterator iter = propHandle.iterator( );
		ConfigVariableHandle structureHandle3 = (ConfigVariableHandle) iter
				.next( );

		structureHandle3.setName( "new name" ); //$NON-NLS-1$
		structureHandle3.setValue( "new value" ); //$NON-NLS-1$

		assertEquals( "new name", structureHandle3.getName( ) ); //$NON-NLS-1$
		assertEquals( "new value", structureHandle3.getValue( ) ); //$NON-NLS-1$

		try
		{
			designHandle.replaceConfigVariable( structure3, structure3 );
			fail( );
		}
		catch ( PropertyValueException e )
		{
			assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS,
					e.getErrorCode( ) );
		}

		ConfigVariable structure4 = StructureFactory.createConfigVar( );
		structure4.setName( "myvar1" ); //$NON-NLS-1$
		structure4.setValue( "my value1" ); //$NON-NLS-1$	

		designHandle.replaceConfigVariable( structure3, structure4 );
		designHandle.dropConfigVariable( "myvar1" ); //$NON-NLS-1$

		try
		{
			designHandle.dropConfigVariable( "myvar" ); //$NON-NLS-1$
			fail( );
		}
		catch ( PropertyValueException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e
							.getErrorCode( ) );
		}
	}

	/**
	 * Tests cases for methods on ReportDesignHandle.
	 *  
	 */

	public void testReportDesignOtherMethods( )
	{
		DesignElement element = designHandle.getElement( );
		assertTrue( element instanceof ReportDesign );
		assertTrue( element == design );
		assertTrue( designHandle.getDesign( ) == design );

		assertTrue( designHandle.getDesignHandle( ) != null );

		// error for master page size

		List list = designHandle.getErrorList( );
		assertEquals( 1, list.size( ) );
		assertEquals( SemanticError.DESIGN_EXCEPTION_CANNOT_SPECIFY_PAGE_SIZE,
				( (ErrorDetail) list.get( 0 ) ).getErrorCode( ) );
		// assertEquals( SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION,
		// ( (ErrorDetail) list.get( 1 ) ).getErrorCode( ) );
		// assertEquals( SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION,
		// ( (ErrorDetail) list.get( 2 ) ).getErrorCode( ) );

		designHandle.semanticCheck( );

		// should keep the same errors and warnings.

		// error for master page size

		list = designHandle.getErrorList( );
		assertEquals( 1, list.size( ) );
		assertEquals( SemanticError.DESIGN_EXCEPTION_CANNOT_SPECIFY_PAGE_SIZE,
				( (ErrorDetail) list.get( 0 ) ).getErrorCode( ) );
		// oda data set and data source can have no extension
		// assertEquals( SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION,
		// ( (ErrorDetail) list.get( 1 ) ).getErrorCode( ) );
		// assertEquals( SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION,
		// ( (ErrorDetail) list.get( 2 ) ).getErrorCode( ) );

		ParameterHandle paramHandle = designHandle.findParameter( "Param 2" ); //$NON-NLS-1$
		assertNotNull( paramHandle );
	}

	/**
	 * Tests cases for methods on slots.
	 *  
	 */

	public void testReportDesignSlots( )
	{

		SlotHandle slotHandle = designHandle.getBody( );
		assertEquals( 1, slotHandle.getCount( ) );

		slotHandle = designHandle.getParameters( );
		assertEquals( 3, slotHandle.getCount( ) );

		List list = designHandle.getFlattenParameters( );
		assertEquals( 4, list.size( ) );

		slotHandle = designHandle.getComponents( );
		assertEquals( 0, slotHandle.getCount( ) );

		slotHandle = designHandle.getDataSets( );
		assertEquals( 1, slotHandle.getCount( ) );

		slotHandle = designHandle.getDataSources( );
		assertEquals( 1, slotHandle.getCount( ) );

		slotHandle = designHandle.getMasterPages( );
		assertEquals( 1, slotHandle.getCount( ) );

		slotHandle = designHandle.getScratchPad( );
		assertEquals( 0, slotHandle.getCount( ) );

		slotHandle = designHandle.getStyles( );
		assertEquals( 0, slotHandle.getCount( ) );

		// no custom color.

		PropertyHandle colorPalette = designHandle
				.getPropertyHandle( ReportDesign.COLOR_PALETTE_PROP );
		assertNull( colorPalette.getListValue( ) );

	}

	/**
	 * Tests cases for reading and setting properties of report design.
	 * 
	 * @throws Exception
	 *             if any exception.
	 */

	public void testReportDesignProperties( ) throws Exception
	{
		// get properties.

		assertEquals( "c:\\", designHandle.getBase( ) ); //$NON-NLS-1$
		assertEquals( getClassFolder( ) + INPUT_FOLDER
				+ "ReportDesignHandleTest.xml", designHandle.getFileName( ) ); //$NON-NLS-1$

		// sets properties.

		designHandle.setBase( "../test/input/" ); //$NON-NLS-1$
		assertEquals( "../test/input/", designHandle.getBase( ) ); //$NON-NLS-1$
		assertEquals( "W.C. Fields", designHandle.getAuthor( ) ); //$NON-NLS-1$
		assertEquals(
				"http://company.com/reportHelp.html", designHandle.getHelpGuide( ) ); //$NON-NLS-1$
		assertEquals( "Whiz-Bang Plus", designHandle.getCreatedBy( ) ); //$NON-NLS-1$
		assertEquals( 30, designHandle.getRefreshRate( ) );

		designHandle.setAuthor( "Eclipse BIRT 1.00" ); //$NON-NLS-1$
		designHandle.setHelpGuide( "http://www.eclipse.org/birt/help.html" ); //$NON-NLS-1$
		designHandle.setCreatedBy( "Eclipse BIRT" ); //$NON-NLS-1$
		designHandle.setRefreshRate( 50 );

		assertEquals( "Eclipse BIRT 1.00", designHandle.getAuthor( ) ); //$NON-NLS-1$
		assertEquals(
				"http://www.eclipse.org/birt/help.html", designHandle.getHelpGuide( ) ); //$NON-NLS-1$
		assertEquals( "Eclipse BIRT", designHandle.getCreatedBy( ) ); //$NON-NLS-1$
		assertEquals( 50, designHandle.getRefreshRate( ) );
	}

	/**
	 * Test case for rename method. Give a free-form element, check the name of
	 * all of elements within its slot. if the name is duplicate with the
	 * current name space
	 * 
	 * @throws Exception
	 *  
	 */
	public void testRename( ) throws Exception
	{

		openDesign( "ReportDesignHandleTest1.xml" ); //$NON-NLS-1$
		FreeFormHandle handle = (FreeFormHandle) designHandle.getBody( )
				.get( 0 );

		assertTrue( handle.getElement( ) instanceof FreeForm );

		FreeForm form = (FreeForm) handle.getElement( );
		designHandle.rename( form.getHandle( design ) );

		SimpleMasterPage page = (SimpleMasterPage) designHandle
				.getMasterPages( ).get( 0 ).getElement( );
		designHandle.rename( page.getHandle( design ) );

	}

	/**
	 * Tests iterator methods of a report design.
	 * 
	 * @throws Exception
	 *  
	 */

	public void testIteratorMethods( ) throws Exception
	{
		Iterator iter = designHandle.imagesIterator( );
		assertNull( iter.next( ) );

		iter = designHandle.configVariablesIterator( );
		assertNotNull( iter.next( ) );
		assertNotNull( iter.next( ) );
		assertNull( iter.next( ) );
	}

	/**
	 * Tests the save state of a report design after several undo and redo.
	 * 
	 * @throws Exception
	 */

	public void testNeedsSave( ) throws Exception
	{

		String outputPath = getClassFolder( ) + OUTPUT_FOLDER; //$NON-NLS-1$
		File outputFolder = new File( outputPath );
		if ( !outputFolder.exists( ) && !outputFolder.mkdir( ) )
		{
			throw new IOException( "Can not create the output folder" ); //$NON-NLS-1$
		}

		ElementFactory factory = new ElementFactory( design );
		GridHandle grid = factory.newGridItem( "new grid" ); //$NON-NLS-1$

		SlotHandle slot = designHandle.getBody( );
		slot.add( grid );

		assertTrue( designHandle.needsSave( ) );
		designHandle.saveAs( outputPath + "ReportDesignTestNew.xml" ); //$NON-NLS-1$
		assertFalse( designHandle.needsSave( ) );

		grid = factory.newGridItem( "new second grid" ); //$NON-NLS-1$
		slot.add( grid );
		assertTrue( designHandle.needsSave( ) );
		slot.dropAndClear( grid );
		assertTrue( designHandle.needsSave( ) );

		ActivityStack as = design.getActivityStack( );
		as.undo( );
		assertTrue( designHandle.needsSave( ) );
		as.undo( );
		assertFalse( designHandle.needsSave( ) );

		as.undo( );
		assertFalse( as.canUndo( ) );
		assertTrue( designHandle.needsSave( ) );

	}

	/**
	 * Execute an command and undo it, the state should be clean.
	 * 
	 * @throws Exception
	 */

	public void testNeedsSave2( ) throws Exception
	{
		ElementFactory factory = new ElementFactory( design );
		LabelHandle label = factory.newLabel( "Label1" ); //$NON-NLS-1$

		SlotHandle slotHandle = designHandle.getBody( );
		slotHandle.add( label );

		assertTrue( designHandle.needsSave( ) );
		design.getActivityStack( ).undo( );

		assertFalse( designHandle.needsSave( ) );
	}

	/**
	 * Tests translations on a report design.
	 * 
	 * @throws Exception
	 *             if any exception.
	 */

	public void testTranslations( ) throws Exception
	{
		openDesign( "ReportDesignHandleTest.xml", Locale.CHINA ); //$NON-NLS-1$

		List translations = designHandle.getTranslations( );
		assertTrue( translations.size( ) == 4 );

		TranslationHandle translation = (TranslationHandle) translations
				.get( 0 );

		assertEquals( "ResourceKey.ReportDesign.Title", translation //$NON-NLS-1$
				.getResourceKey( ) );
		assertEquals( null, translation.getLocale( ) );
		assertEquals( "My Sample design(default)", translation.getText( ) ); //$NON-NLS-1$

		TranslationHandle handle = designHandle.getTranslation(
				"ResourceKey.ReportDesign.Title", null ); //$NON-NLS-1$
		assertEquals( "My Sample design(default)", handle.getText( ) ); //$NON-NLS-1$

		translation = (TranslationHandle) translations.get( 1 );

		assertEquals( "ResourceKey.ReportDesign.Title", translation //$NON-NLS-1$
				.getResourceKey( ) );
		assertEquals( "zh_CN", translation.getLocale( ) ); //$NON-NLS-1$
		assertEquals( "zh_CN:\u7B80\u5355\u62A5\u8868.", translation.getText( ) ); //$NON-NLS-1$

		String[] keys = designHandle.getTranslationKeys( );
		assertEquals( 2, keys.length );

		String str = design
				.getMessage(
						"ResourceKey.ReportDesign.Title", new Locale( "zh", "CN", "tai" ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals( "zh_CN:\u7B80\u5355\u62A5\u8868.", str ); //$NON-NLS-1$
	}

	/**
	 * Tests adding translation.
	 * 
	 * @throws DesignFileException
	 * @throws CustomMsgException
	 */
	public void testAddTranslation( ) throws DesignFileException,
			CustomMsgException
	{
		openDesign( "ReportDesignHandleTest.xml", Locale.CHINA ); //$NON-NLS-1$

		List translations = designHandle.getTranslations( );
		assertTrue( translations.size( ) == 4 );

		designHandle.addTranslation( "resourceKey.test1", "en", "text1" ); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

		translations = designHandle.getTranslations( );
		assertTrue( translations.size( ) == 5 );

		try
		{
			designHandle.addTranslation( "resourceKey.test1", "en", "text2" ); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
			fail( );
		}
		catch ( CustomMsgException e )
		{
			assertEquals( CustomMsgException.DESIGN_EXCEPTION_DUPLICATE_LOCALE,
					e.getErrorCode( ) );
		}
	}

	/**
	 * Tests dropping translation.
	 * 
	 * @throws DesignFileException
	 *             if failed to open design file.
	 * @throws CustomMsgException
	 *             if any translation operation error.
	 */

	public void testDropTranslation( ) throws DesignFileException,
			CustomMsgException
	{
		openDesign( "ReportDesignHandleTest.xml", Locale.CHINA ); //$NON-NLS-1$

		List translations = designHandle.getTranslations( );
		assertTrue( translations.size( ) == 4 );

		designHandle.dropTranslation( "ResourceKey.ReportDesign.Title", null ); //$NON-NLS-1$
		translations = designHandle.getTranslations( );
		assertTrue( translations.size( ) == 3 );

		try
		{
			designHandle.dropTranslation( "resourceKey.unknown", "unknow" ); //$NON-NLS-1$//$NON-NLS-2$
			fail( );
		}
		catch ( CustomMsgException e )
		{
			assertEquals(
					CustomMsgException.DESIGN_EXCEPTION_TRANSLATION_NOT_FOUND,
					e.getErrorCode( ) );
		}

	}

	public void testEmbeddedImage( ) throws Exception
	{
		openDesign( "EmbeddedImageTest.xml", Locale.ENGLISH ); //$NON-NLS-1$
		PropertyHandle images = designHandle
				.getPropertyHandle( ReportDesign.IMAGES_PROP );
		assertNotNull( images );
		
		// get the embedded images
		
		EmbeddedImageHandle image1 = (EmbeddedImageHandle)images.getAt( 0 );
		EmbeddedImageHandle image2 = (EmbeddedImageHandle)images.getAt( 1 );
		ArrayList imageList = new ArrayList( );
		imageList.add( image1 );
		imageList.add( image2 );
		designHandle.dropImage( imageList );
		
		// unod and test again
		design.getActivityStack( ).undo( );
		image1 = (EmbeddedImageHandle)images.getAt( 0 );
		image2 = (EmbeddedImageHandle)images.getAt( 1 );
		
		design.getActivityStack( ).redo( );
		assertEquals( 0, images.getListValue( ).size( ) );
		try
		{
		designHandle.dropImage( image1.getName( ) );
		designHandle.dropImage( image2.getName( ) );
		fail( );
		}
		catch( PropertyValueException e )
		{
			assertEquals( PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e.getErrorCode( ) );
		}
	}

}