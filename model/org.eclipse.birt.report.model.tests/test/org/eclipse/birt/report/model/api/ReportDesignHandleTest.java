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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.command.CustomMsgException;
import org.eclipse.birt.report.model.api.core.AttributeEvent;
import org.eclipse.birt.report.model.api.core.DisposeEvent;
import org.eclipse.birt.report.model.api.core.IAttributeListener;
import org.eclipse.birt.report.model.api.core.IDisposeListener;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

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
 * <tr>
 * <td>Test add / drop css style sheet 
 * </tr>
 * 
 * </table>
 * 
 * 
 */
public class ReportDesignHandleTest extends BaseTestCase
{

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
	 * Tests css style sheet.
	 * 
	 * @throws Exception
	 */

	public void testCssStyleSheet( ) throws Exception
	{
		openDesign( "BlankReportDesign.xml" ); //$NON-NLS-1$

		// test add css

		designHandle.addCss( "base.css" );//$NON-NLS-1$
		List styles = designHandle.getAllStyles( );
		assertEquals( 5, styles.size( ) );

		try
		{
			designHandle.addCss( "base.css" );//$NON-NLS-1$
			fail( );
		}
		catch ( CssException e )
		{
			assertEquals( CssException.DESIGN_EXCEPTION_DUPLICATE_CSS, e
					.getErrorCode( ) );
		}

		// label use it
		LabelHandle labelHandle = designHandle.getElementFactory( ).newLabel(
				"label" );//$NON-NLS-1$
		designHandle.getBody( ).add( labelHandle );
		labelHandle.setStyle( (SharedStyleHandle) styles.get( 0 ) );
		
		// drop css
		IncludedCssStyleSheetHandle sheetHandle = (IncludedCssStyleSheetHandle) designHandle
				.includeCssesIterator( ).next( );
		
		// before drop , element is resolved. after drop element is unresolved
		ElementRefValue value = (ElementRefValue) labelHandle.getElement( )
				.getLocalProperty( designHandle.getModule( ), "style" );//$NON-NLS-1$
		assertTrue( value.isResolved( ) );

		designHandle.dropCss( sheetHandle );
		assertFalse( value.isResolved( ) );
		assertNull( designHandle.includeCssesIterator( ).next( ) );
		assertNull( labelHandle.getStyle( ) );
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
		assertEquals(
				getResource( INPUT_FOLDER + "ReportDesignHandleTest.xml" ).toString( ), designHandle.getFileName( ) ); //$NON-NLS-1$

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
		assertFalse( iter.hasNext( ) );

		iter = designHandle.configVariablesIterator( );
		assertNotNull( iter.next( ) );
		assertNotNull( iter.next( ) );
		assertFalse( iter.hasNext( ) );
	}

	/**
	 * Tests the save state of a report design after several undo and redo.
	 * 
	 * @throws Exception
	 */

	public void testNeedsSave( ) throws Exception
	{

		String outputPath = getTempFolder( ) + OUTPUT_FOLDER;
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
		save( designHandle );

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
		openDesign( "ReportDesignHandleTest.xml", ULocale.CHINA ); //$NON-NLS-1$

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
						"ResourceKey.ReportDesign.Title", new ULocale( "zh", "CN", "tai" ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
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
		openDesign( "ReportDesignHandleTest.xml", ULocale.CHINA ); //$NON-NLS-1$

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
		openDesign( "ReportDesignHandleTest.xml", ULocale.CHINA ); //$NON-NLS-1$

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

	/**
	 * 
	 * @throws Exception
	 */
	public void testEmbeddedImage( ) throws Exception
	{
		openDesign( "EmbeddedImageTest.xml", ULocale.ENGLISH ); //$NON-NLS-1$
		PropertyHandle images = designHandle
				.getPropertyHandle( ReportDesign.IMAGES_PROP );
		assertNotNull( images );

		// get the embedded images

		EmbeddedImageHandle image1 = (EmbeddedImageHandle) images.getAt( 0 );
		EmbeddedImageHandle image2 = (EmbeddedImageHandle) images.getAt( 1 );
		String image1Name = image1.getName( );
		String image2Name = image2.getName( );
		ArrayList imageList = new ArrayList( );
		imageList.add( image1 );
		imageList.add( image2 );
		designHandle.dropImage( imageList );

		// undo and test again
		design.getActivityStack( ).undo( );
		image1 = (EmbeddedImageHandle) images.getAt( 0 );
		image2 = (EmbeddedImageHandle) images.getAt( 1 );

		design.getActivityStack( ).redo( );
		assertEquals( 0, images.getListValue( ).size( ) );
		try
		{
			designHandle.dropImage( image1Name );
			designHandle.dropImage( image2Name );
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
	 * Tests attribute and dispose listeners in module.
	 * 
	 * @throws Exception
	 */

	public void testFileNameAndDisposeListener( ) throws Exception
	{
		FileNameListener fListener = new FileNameListener( );
		designHandle.addAttributeListener( fListener );
		DisposeListener dListener = new DisposeListener( );
		designHandle.addDisposeListener( dListener );

		designHandle.setFileName( "test file" ); //$NON-NLS-1$
		designHandle.close( );
		assertEquals( "test file", fListener.getStatus( ) ); //$NON-NLS-1$
		assertEquals( "disposed", dListener.getStatus( ) ); //$NON-NLS-1$
		openDesign( "ReportDesignHandleTest.xml" ); //$NON-NLS-1$

		SelfDisposeListener dropListener = new SelfDisposeListener( );

		designHandle.addDisposeListener( dropListener );
		designHandle.getModule( ).broadcastDisposeEvent(
				new DisposeEvent( designHandle.getModule( ) ) );
		assertEquals( 1, dropListener.getStatus( ) );

		designHandle.getModule( ).broadcastDisposeEvent(
				new DisposeEvent( designHandle.getModule( ) ) );
		assertEquals( 1, dropListener.getStatus( ) );
	}

	/**
	 * Tests the function to find the resource with the given file name. Test
	 * cases are:
	 * 
	 * <ul>
	 * <li>Uses the file path to find the relative resource.</li>
	 * <li>Uses network protocol to find the relative resource.</li>
	 * <li>Uses the file protcol to find the relative resource</li>
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testFindResource( ) throws Exception
	{
		designHandle.setFileName( null );
		designHandle.getModule( ).setSystemId( null );

		// uses the file path to find, file exists

		URL filePath = getResource( INPUT_FOLDER + "ReportDesignHandleTest.xml" ); //$NON-NLS-1$

		designHandle.setFileName( filePath.toString( ) );
		URL url = designHandle.findResource( "ReportDesignHandleTest.xml", //$NON-NLS-1$
				IResourceLocator.LIBRARY );
		assertNotNull( url );

		// a file not existed.

		url = designHandle.findResource( "NoExistedDesign.xml", //$NON-NLS-1$
				IResourceLocator.LIBRARY );
		assertNull( url );

		// resources with relative uri file path

		designHandle.setFileName( getResource( INPUT_FOLDER ).toString( )
				+ "NoExistedDesign.xml" ); //$NON-NLS-1$

		url = designHandle.findResource( "ReportDesignHandleTest.xml", //$NON-NLS-1$
				IResourceLocator.LIBRARY );
		assertNotNull( url );

		url = designHandle.findResource( "NoExistedDesign.xml", //$NON-NLS-1$
				IResourceLocator.LIBRARY );
		assertNull( url );

		// resources with HTTP protocols.

		designHandle.getModule( ).setSystemId(
				new URL( "http://www.eclipse.org/" ) ); //$NON-NLS-1$

		url = designHandle.findResource( "images/EclipseBannerPic.jpg", //$NON-NLS-1$
				IResourceLocator.IMAGE );

		assertEquals( "http://www.eclipse.org/images/EclipseBannerPic.jpg", //$NON-NLS-1$
				url.toString( ) );

		// resources with HTTP protocols.

		url = designHandle.findResource( "NoExistedDir/NoExistedDesign.xml", //$NON-NLS-1$
				IResourceLocator.LIBRARY );
		assertNotNull( url );

		// TODO:
		// // resources with both system id and path.
		//
		// File f = new File( filePath ).getParentFile( );
		//
		// designHandle.getModule( ).setSystemId( f.toURL( ) );
		//
		// url = designHandle.findResource( "ReportDesignHandleTest.xml",
		// //$NON-NLS-1$
		// IResourceLocator.LIBRARY );
		// assertNotNull( url );
		//
		// url = designHandle.findResource( "NoExistedDesign.xml", //$NON-NLS-1$
		// IResourceLocator.LIBRARY );
		// assertNull( url );
		//
		// f = new File( filePath );
		// url = designHandle.findResource( f.toURL( ).toString( ),
		// IResourceLocator.LIBRARY );
		// assertNotNull( url );
		//
		// // test with new feature "deploy resource in resource path"
		//
		// designHandle.setFileName( null );
		// designHandle.getModule( ).setSystemId( null );
		//
		// url = designHandle.findResource( getClassFolder( ) + GOLDEN_FOLDER
		// + "ActionHandleTest2_golden.xml", IResourceLocator.LIBRARY );
		// assertNotNull( url );
		//
		// url = null;
		// designHandle.getModule( ).getSession( ).setResourceFolder(
		// getClassFolder( ) + GOLDEN_FOLDER );
		// url = designHandle.findResource( "ActionHandleTest2_golden.xml",
		// //$NON-NLS-1$
		// IResourceLocator.LIBRARY );
		// assertNotNull( url );
		//
		// designHandle.getModule( ).getSession( ).setResourceFolder( null );
		// url = designHandle.findResource( "ActionHandleTest2_golden.xml",
		// //$NON-NLS-1$
		// IResourceLocator.LIBRARY );
		// assertNull( url );
	}

	/**
	 * Tests <code>setFileName</code> function. Cases are
	 * 
	 * <ul>
	 * <li>setFileName with HTTP protocol</li>
	 * <li>setFileName with HTTP protocol and Chinese character.</li>
	 * <li>setFileName with unix file schema.</li>
	 * <li>setFileName with windows file schema.</li>
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testSetFileName( ) throws Exception
	{

		// resources with HTTP protocols.

		designHandle.setFileName( "http://www.eclipse.org/ima#ge  \\s/" ); //$NON-NLS-1$
		assertEqualsOnWindows(
				"http://www.eclipse.org/ima%23ge  /s/", designHandle //$NON-NLS-1$
						.getSystemId( ).toString( ) );

		designHandle
				.setFileName( "http://hello.com/\u4e0d\u5b58\u5728\u7684\u5355\u4f4d" ); //$NON-NLS-1$
		assertEquals( "http://hello.com/", designHandle //$NON-NLS-1$
				.getSystemId( ).toString( ) );

		designHandle
				.setFileName( "http://hello.com/\u4e0d\u5b58/index.rtpdesign" ); //$NON-NLS-1$
		assertEquals( "http://hello.com/\u4e0d\u5b58/", designHandle //$NON-NLS-1$
				.getSystemId( ).toString( ) );

		designHandle.setFileName( "/usr/home/birt/report.xml" ); //$NON-NLS-1$
		assertEquals(
				new File( "/usr/home/birt/report.xml" ).getParentFile( ).toURL( ) //$NON-NLS-1$
						.toString( ), designHandle.getSystemId( ).toString( ) );

		designHandle.setFileName( "C:\\reports\\1.xml" ); //$NON-NLS-1$
		assertEqualsOnWindows( "file:/C:/reports", designHandle //$NON-NLS-1$
				.getSystemId( ).toString( ) );

		designHandle.setFileName( "1.xml" ); //$NON-NLS-1$
		assertNotNull( designHandle.getModule( ).getSystemId( ) );
		assertEquals( new File( "1.xml" ).getAbsoluteFile( ).getParentFile( ) //$NON-NLS-1$
				.toURL( ).toExternalForm( ), designHandle.getSystemId( )
				.toString( ) );
	}

	/**
	 * Tests the copy-rename-add methods about the embedded images.
	 * 
	 * @throws Exception
	 */

	public void testCopyAndPasteEmbeddedImage( ) throws Exception
	{
		openDesign( "EmbeddedImageTest.xml" ); //$NON-NLS-1$

		EmbeddedImage image = designHandle.findImage( "image one" ); //$NON-NLS-1$
		assertNotNull( image );

		EmbeddedImage newImage = (EmbeddedImage) image.copy( );
		assertNotNull( newImage );
		assertEquals( image.getName( ), newImage.getName( ) );
		designHandle.rename( newImage );
		assertEquals( image.getName( ) + "1", newImage.getName( ) ); //$NON-NLS-1$
		designHandle.addImage( newImage );
		assertEquals( newImage, designHandle.findImage( image.getName( ) + "1" ) ); //$NON-NLS-1$
	}

	class FileNameListener implements IAttributeListener
	{

		private String status = null;

		public void fileNameChanged( ModuleHandle targetElement,
				AttributeEvent ev )
		{
			status = targetElement.getFileName( );
		}

		public String getStatus( )
		{
			return status;
		}
	}

	class DisposeListener implements IDisposeListener
	{

		private String status = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.core.IDisposeListener#elementDisposed(org.eclipse.birt.report.model.api.ModuleHandle,
		 *      org.eclipse.birt.report.model.api.core.DisposeEvent)
		 */
		public void moduleDisposed( ModuleHandle targetElement, DisposeEvent ev )
		{
			status = "disposed"; //$NON-NLS-1$

		}

		public String getStatus( )
		{
			return status;
		}

	}

	class SelfDisposeListener implements IDisposeListener
	{

		private int status = 0;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.core.IDisposeListener#elementDisposed(org.eclipse.birt.report.model.api.ModuleHandle,
		 *      org.eclipse.birt.report.model.api.core.DisposeEvent)
		 */

		public void moduleDisposed( ModuleHandle targetElement, DisposeEvent ev )
		{
			targetElement.removeDisposeListener( this );
			status++;
		}

		/**
		 * @return
		 */
		protected int getStatus( )
		{
			return status;
		}

	}

	/**
	 * @throws DesignFileException
	 * 
	 * 
	 */

	public void testCascadingParameters( ) throws DesignFileException
	{
		openDesign( "ReportDesignHandleTest2.xml" ); //$NON-NLS-1$
		CascadingParameterGroupHandle group1 = designHandle
				.findCascadingParameterGroup( "Country-State-City" ); //$NON-NLS-1$
		assertNotNull( group1 );
		assertEquals( 3, group1.getParameters( ).getCount( ) );

		CascadingParameterGroupHandle group2 = designHandle
				.findCascadingParameterGroup( "group2" ); //$NON-NLS-1$
		assertNotNull( group2 );
		assertEquals( "Group2 displayName", group2.getDisplayName( ) ); //$NON-NLS-1$

		CascadingParameterGroupHandle group3 = designHandle
				.findCascadingParameterGroup( "group3" ); //$NON-NLS-1$
		assertNotNull( group3 );
		assertEquals( "Group3 displayName", group3.getDisplayName( ) ); //$NON-NLS-1$

		CascadingParameterGroupHandle group4 = designHandle
				.findCascadingParameterGroup( "non-exsit-group" ); //$NON-NLS-1$
		assertNull( group4 );
	}

	/**
	 * Test cases: Test the getAllBookmarks method on the ModuleHandle which
	 * returns all the bookmarks defined in the report design.
	 * 
	 * @throws Exception
	 */

	public void testGetBookmarksAndTocs( ) throws Exception
	{
		openDesign( "ReportDesignBookmark.xml" ); //$NON-NLS-1$
		List bookmarks = designHandle.getAllBookmarks( );

		assertEquals( 4, bookmarks.size( ) );
		assertEquals( "bookmark_label", bookmarks.get( 0 ) ); //$NON-NLS-1$
		assertEquals( "\"bookmark_group\"", bookmarks.get( 1 ) ); //$NON-NLS-1$
		assertEquals( "bookmark_detail_row", bookmarks.get( 2 ) ); //$NON-NLS-1$
		assertEquals( "bookmark_detail_text", bookmarks.get( 3 ) ); //$NON-NLS-1$

		List tocs = designHandle.getAllTocs( );
		assertEquals( 3, tocs.size( ) );
		assertEquals( "Toc_label", tocs.get( 0 ) ); //$NON-NLS-1$
		assertEquals( "DateTimeSpan.days(2005/01/01, 2006/01/01)", tocs.get( 1 ) ); //$NON-NLS-1$
		assertEquals( "toc_detail_text", tocs.get( 2 ) ); //$NON-NLS-1$
	}

	/**
	 * Test the rerpot design initialize method. After the initialize method is
	 * called, there should be one master page created for the report. The
	 * properties values given by the argument should be set to the report
	 * deisgn element. All thos operation should not go into command stack.
	 * 
	 * @throws SemanticException
	 * @throws IOException
	 */
	public void testInitializeReportDesign( ) throws SemanticException,
			IOException
	{

		SessionHandle sessionHandle = new SessionHandle( (ULocale) null );
		designHandle = sessionHandle.createDesign( );

		Map properties = new HashMap( );
		String createdBy = "test initialize"; //$NON-NLS-1$

		// bad property key value.

		properties.put( "Build", "2006-12-25" );//$NON-NLS-1$//$NON-NLS-2$

		// good property key value.

		properties.put( ReportDesign.CREATED_BY_PROP, createdBy );

		designHandle.initializeModule( properties );

		assertEquals( 0, designHandle.getMasterPages( ).getCount( ) );
		assertNull( designHandle.getProperty( "Build" ) );//$NON-NLS-1$
		assertEquals( createdBy, designHandle
				.getProperty( ReportDesign.CREATED_BY_PROP ) );

		CommandStack stack = designHandle.getCommandStack( );

		assertFalse( stack.canRedo( ) );
		assertFalse( stack.canUndo( ) );

	}

	/**
	 * Does the equal assert only when the platform is windows.
	 * 
	 * @param expected
	 * @param actual
	 */

	private void assertEqualsOnWindows( String expected, String actual )
	{
		if ( isWindowsPlatform( ) )
			assertEquals( expected, actual );
	}

	/**
	 * Tests the getReportItemsBasedonTempalates method.
	 * 
	 * This method is supposed to return report items which holds a template
	 * definition, that is, report item in body slot and page slot. Notice,
	 * nested template items is excluded.
	 * 
	 * @throws DesignFileException
	 * 
	 */
	public void testGetReportItemsBasedonTempalates( )
			throws DesignFileException
	{
		openDesign( "ReportDesignHandleTest3.xml" ); //$NON-NLS-1$

		List result = designHandle.getReportItemsBasedonTempalates( );

		assertEquals( 6, result.size( ) );
		assertEquals(
				"template table 1", ( (DesignElementHandle) result.get( 0 ) ).getName( ) ); //$NON-NLS-1$
		assertEquals(
				"template inner table", ( (DesignElementHandle) result.get( 1 ) ).getName( ) ); //$NON-NLS-1$
		assertEquals(
				"inner label", ( (DesignElementHandle) result.get( 2 ) ).getName( ) ); //$NON-NLS-1$
		assertEquals(
				"tamplate label 2", ( (DesignElementHandle) result.get( 3 ) ).getName( ) ); //$NON-NLS-1$
		assertEquals(
				"label 3", ( (DesignElementHandle) result.get( 4 ) ).getName( ) ); //$NON-NLS-1$
		assertEquals(
				"label in master page", ( (DesignElementHandle) result.get( 5 ) ).getName( ) ); //$NON-NLS-1$

	}

	/**
	 * if the message file with the current locale existed, the defult one will
	 * not be allocated.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void testLoadMessageFiles( ) throws DesignFileException,
			SemanticException
	{

		openDesign( "TestLoadMessageFiles.xml", ULocale.CHINA ); //$NON-NLS-1$

		LabelHandle label1 = (LabelHandle) designHandle.findElement( "label1" ); //$NON-NLS-1$
		LabelHandle label2 = (LabelHandle) designHandle.findElement( "label2" ); //$NON-NLS-1$
		LabelHandle label3 = (LabelHandle) designHandle.findElement( "label3" ); //$NON-NLS-1$

		assertNotNull( label1 );
		assertNotNull( label2 );
		assertNotNull( label3 );

		label1.setTextKey( "keyone" ); //$NON-NLS-1$
		label2.setTextKey( "keytwo" ); //$NON-NLS-1$
		label2.setTextKey( "keythree" ); //$NON-NLS-1$

		assertEquals( "zh CN message file", label1.getDisplayText( ) ); //$NON-NLS-1$
		assertEquals( "key two", label2.getDisplayText( ) ); //$NON-NLS-1$
		assertEquals( "key three", label3.getDisplayText( ) ); //$NON-NLS-1$

	}
}