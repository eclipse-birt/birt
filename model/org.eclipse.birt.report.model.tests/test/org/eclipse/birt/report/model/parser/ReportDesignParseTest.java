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

package org.eclipse.birt.report.model.parser;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.IncludeScriptHandle;
import org.eclipse.birt.report.model.api.IncludedLibraryHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ScriptLibHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.ScriptLib;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * This class tests the property parsing and writing. Translation is test in
 * <code>ReportDesignUserDefinedMessagesTest</code> All slots will be tested
 * in the corresponding element parse test.
 * 
 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse:
 * collapse" bordercolor="#111111" width="100%" id="AutoNumber5" height="99">
 * <tr>
 * <td width="33%" height="16"><b>Method </b></td>
 * <td width="33%" height="16"><b>Test Case </b></td>
 * <td width="34%" height="16"><b>Expected Result </b></td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16">{@link #testParser()}</td>
 * <td width="33%" height="16">Test all propertyies</td>
 * <td width="34%" height="16">the correct value are returned</td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="14"></td>
 * <td width="33%" height="14">Use iterator to test the reportItems slot in
 * freeform</td>
 * <td width="34%" height="14">content can be retrieved.</td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16"></td>
 * <td width="33%" height="16">Test the freeform extends relationship</td>
 * <td width="34%" height="16">extend relationship correct.</td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16">{@link #testWriter()}</td>
 * <td width="33%" height="16">Set new value to properties and save it.</td>
 * <td width="34%" height="16">new value should be save into the output file.
 * </td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16">{@link #testConfigVars()}</td>
 * <td width="33%" height="16">Test add, find, replace and drop operation on
 * Config Variables.</td>
 * <td width="34%" height="16">Check it with find method.</td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16">{@link #testImages()}</td>
 * <td width="33%" height="16">Test add, find, replace and drop operation on
 * embedded images.</td>
 * <td width="34%" height="16">Check it with find method.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSemanticError()}</td>
 * <td>Test semantic errors with the design file input.</td>
 * <td>The errors are collected, such as the font-size is negative and there is
 * no pages in the page setup slot.</td>
 * </tr>
 * </table>
 * 
 * @see org.eclipse.birt.report.model.parser.ScalarParameterParseTest
 * @see org.eclipse.birt.report.model.parser.StyleParseTest
 * @see org.eclipse.birt.report.model.parser.ComponentScratchPadTest
 * @see org.eclipse.birt.report.model.elements.ReportDesignUserDefinedMessagesTest
 */

public class ReportDesignParseTest extends BaseTestCase
{

	String fileName = "ReportDesignParseTest.xml"; //$NON-NLS-1$
	String outFileName = "ReportDesigntParseTest_out.xml"; //$NON-NLS-1$
	String outFileName_2 = "ReportDesigntParseTest_out_2.xml"; //$NON-NLS-1$
	String goldenFileName = "ReportDesignParseTest_golden.xml"; //$NON-NLS-1$
	String goldenFileName_2 = "ReportDesignParseTest_golden_2.xml"; //$NON-NLS-1$
	String semanticCheckFileName = "ReportDesignParseTest_1.xml"; //$NON-NLS-1$
	String datasourceBindingsFileName = "ReportDesignParseTest_2.xml"; //$NON-NLS-1$

	String scriptLibFileName = "ReportDesignScriptLibParseTest.xml";//$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/**
	 * Tests all properties and slots.
	 * 
	 * @throws Exception
	 */
	public void testParser( ) throws Exception
	{
		openDesign( fileName, ULocale.ENGLISH );

		assertEquals(
				"W.C. Fields", designHandle.getStringProperty( ReportDesign.AUTHOR_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"http://company.com/reportHelp.html", design.getStringProperty( design, ReportDesign.HELP_GUIDE_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"Whiz-Bang Plus", design.getStringProperty( design, ReportDesign.CREATED_BY_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"30", design.getStringProperty( design, ReportDesign.REFRESH_RATE_PROP ) ); //$NON-NLS-1$
		assertEquals( "c:\\", designHandle.getBase( ) ); //$NON-NLS-1$
		assertEquals( "old_message", designHandle.getIncludeResource( ) ); //$NON-NLS-1$

		// title

		assertEquals(
				"TITLE_ID", design.getStringProperty( design, ReportDesign.TITLE_ID_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"Sample Report", design.getStringProperty( design, ReportDesign.TITLE_PROP ) ); //$NON-NLS-1$

		// comments

		assertEquals(
				"First sample report.", design.getStringProperty( design, ReportDesign.COMMENTS_PROP ) ); //$NON-NLS-1$

		// description

		assertEquals(
				"DESCRIP_ID", design.getStringProperty( design, ReportDesign.DESCRIPTION_ID_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"This is a first sample report.", design.getStringProperty( design, ReportDesign.DESCRIPTION_PROP ) ); //$NON-NLS-1$

		// display name

		assertEquals(
				"display name key", design.getStringProperty( design, ReportDesign.DISPLAY_NAME_ID_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"display name", design.getStringProperty( design, ReportDesign.DISPLAY_NAME_PROP ) ); //$NON-NLS-1$

		// icon file and cheet sheet

		assertEquals(
				"iconFile", design.getStringProperty( design, ReportDesign.ICON_FILE_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"cheatSheet", design.getStringProperty( design, ReportDesign.CHEAT_SHEET_PROP ) ); //$NON-NLS-1$

		// event handler class

		assertEquals(
				"on_Event", design.getStringProperty( design, ReportDesign.EVENT_HANDLER_CLASS_PROP ) ); //$NON-NLS-1$

		// keywords is not implemented
		// include libraries

		Iterator includeLibraries = designHandle.includeLibrariesIterator( );

		IncludedLibraryHandle lib = (IncludedLibraryHandle) includeLibraries
				.next( );
		assertEquals( "LibraryA.xml", lib.getFileName( ) ); //$NON-NLS-1$
		assertEquals( "LibA", lib.getNamespace( ) ); //$NON-NLS-1$

		lib = (IncludedLibraryHandle) includeLibraries.next( );
		assertEquals( "LibraryB.xml", lib.getFileName( ) ); //$NON-NLS-1$
		assertEquals( "LibB", lib.getNamespace( ) ); //$NON-NLS-1$

		lib = (IncludedLibraryHandle) includeLibraries.next( );
		assertEquals( "LibraryC.xml", lib.getFileName( ) ); //$NON-NLS-1$
		assertEquals( "LibC", lib.getNamespace( ) ); //$NON-NLS-1$

		// code-modules

		Iterator scripts = designHandle.includeScriptsIterator( );
		IncludeScriptHandle script = (IncludeScriptHandle) scripts.next( );
		assertEquals( "script first", script.getFileName( ) ); //$NON-NLS-1$

		script = (IncludeScriptHandle) scripts.next( );
		assertEquals( "script second", script.getFileName( ) ); //$NON-NLS-1$

		assertNull( scripts.next( ) );

		// test-config is not implemented

		// color-palette

		PropertyHandle colorPalette = designHandle
				.getPropertyHandle( ReportDesign.COLOR_PALETTE_PROP );
		List colors = colorPalette.getListValue( );
		assertEquals( 2, colors.size( ) );
		CustomColor color = (CustomColor) colors.get( 0 );
		assertEquals( "cus red", color.getName( ) ); //$NON-NLS-1$
		assertEquals( 111, color.getRGB( ) );
		assertEquals( "cus red key", color.getDisplayNameID( ) ); //$NON-NLS-1$
		assertEquals( "cus red display", color.getDisplayName( ) ); //$NON-NLS-1$
		color = (CustomColor) colors.get( 1 );
		assertEquals( "cus blue", color.getName( ) ); //$NON-NLS-1$
		assertEquals( 222, color.getRGB( ) );
		assertEquals( "cus blue key", color.getDisplayNameID( ) ); //$NON-NLS-1$

		// config-vars
		PropertyHandle configVarHandle = designHandle
				.getPropertyHandle( ReportDesign.CONFIG_VARS_PROP );
		List configVars = configVarHandle.getListValue( );
		assertEquals( 4, configVars.size( ) );
		ConfigVariable var = (ConfigVariable) configVars.get( 0 );
		assertEquals( "var1", var.getName( ) ); //$NON-NLS-1$
		assertEquals( "mumble.jpg", var.getValue( ) ); //$NON-NLS-1$
		var = (ConfigVariable) configVars.get( 1 );
		assertEquals( "var2", var.getName( ) ); //$NON-NLS-1$
		assertEquals( "abcdefg", var.getValue( ) ); //$NON-NLS-1$
		var = (ConfigVariable) configVars.get( 2 );
		assertEquals( "var3", var.getName( ) ); //$NON-NLS-1$
		assertEquals( "", var.getValue( ) ); //$NON-NLS-1$
		var = (ConfigVariable) configVars.get( 3 );
		assertEquals( "var4", var.getName( ) ); //$NON-NLS-1$
		assertEquals( null, var.getValue( ) );

		// images
		PropertyHandle imageHandle = designHandle
				.getPropertyHandle( ReportDesign.IMAGES_PROP );
		List images = imageHandle.getListValue( );
		assertEquals( 3, images.size( ) );
		EmbeddedImage image = (EmbeddedImage) images.get( 0 );
		assertEquals( "image1", image.getName( ) ); //$NON-NLS-1$
		assertEquals( "image/bmp", image.getType( design ) ); //$NON-NLS-1$		
		assertEquals( "imagetesAAA", //$NON-NLS-1$
				new String( Base64.encodeBase64( image.getData( design ) ) )
						.substring( 0, 11 ) );

		image = (EmbeddedImage) images.get( 1 );
		assertEquals( "image2", image.getName( ) ); //$NON-NLS-1$
		assertEquals( "image/gif", image.getType( design ) ); //$NON-NLS-1$
		assertEquals( "/9j/4AAQSkZJRgA", //$NON-NLS-1$
				new String( Base64.encodeBase64( image.getData( design ) ) )
						.substring( 0, 15 ) );

		image = (EmbeddedImage) images.get( 2 );
		assertEquals( "image3", image.getName( ) ); //$NON-NLS-1$
		assertEquals( "image/bmp", image.getType( design ) ); //$NON-NLS-1$
		assertEquals( "AAAA", //$NON-NLS-1$
				new String( Base64.encodeBase64( image.getData( design ) ) ) );

		// thumbnail

		assertTrue( new String( Base64.encodeBase64( designHandle
				.getThumbnail( ) ) ).startsWith( "thumbnailimage" ) ); //$NON-NLS-1$

		// custom is not implemented

		assertEquals( "script of initialize", designHandle.getInitialize( ) ); //$NON-NLS-1$

		assertEquals(
				"script of beforeFactory", designHandle.getBeforeFactory( ) ); //$NON-NLS-1$
		assertEquals( "script of afterFactory", designHandle.getAfterFactory( ) ); //$NON-NLS-1$

		assertEquals( "script of beforeRender", designHandle.getBeforeRender( ) ); //$NON-NLS-1$
		assertEquals( "script of afterRender", designHandle.getAfterRender( ) ); //$NON-NLS-1$

	}

	/**
	 * Tests writing the properties.
	 * 
	 * @throws Exception
	 *             if any error found.
	 */

	public void testWriter( ) throws Exception
	{
		openDesign( fileName, ULocale.ENGLISH );

		designHandle.setProperty( ReportDesign.AUTHOR_PROP, "Report Author" ); //$NON-NLS-1$
		designHandle.setProperty( ReportDesign.HELP_GUIDE_PROP, "Help guide" ); //$NON-NLS-1$
		designHandle.setProperty( ReportDesign.CREATED_BY_PROP,
				"Report Creator" ); //$NON-NLS-1$
		designHandle.setProperty( ReportDesign.REFRESH_RATE_PROP, "90" ); //$NON-NLS-1$
		designHandle.setBase( "" ); //$NON-NLS-1$
		designHandle.setIncludeResource( "new_message" ); //$NON-NLS-1$

		designHandle.setProperty( ReportDesign.TITLE_ID_PROP, "New title id" ); //$NON-NLS-1$
		designHandle.setProperty( ReportDesign.TITLE_PROP, "New title" ); //$NON-NLS-1$
		designHandle.setProperty( ReportDesign.COMMENTS_PROP, "New comments" ); //$NON-NLS-1$
		designHandle.setProperty( ReportDesign.DESCRIPTION_ID_PROP,
				"New description id" ); //$NON-NLS-1$
		designHandle.setProperty( ReportDesign.DESCRIPTION_PROP,
				"New description" ); //$NON-NLS-1$
		designHandle.setProperty( ReportDesign.EVENT_HANDLER_CLASS_PROP,
				"on event" ); //$NON-NLS-1$

		designHandle.setInitialize( "new initialize script" ); //$NON-NLS-1$

		designHandle.setBeforeFactory( "new beforeFactory script" ); //$NON-NLS-1$
		designHandle.setAfterFactory( "new afterFactory script" ); //$NON-NLS-1$

		designHandle.setBeforeRender( "new beforeRender script" ); //$NON-NLS-1$
		designHandle.setAfterRender( "new afterRender script" ); //$NON-NLS-1$

		designHandle.setDisplayName( "new display name" ); //$NON-NLS-1$
		designHandle.setDisplayNameKey( "new display name key" ); //$NON-NLS-1$
		designHandle.setIconFile( "new iconFile" ); //$NON-NLS-1$
		designHandle.setCheatSheet( "new cheetSheet" ); //$NON-NLS-1$

		// set thumbnail

		designHandle.setThumbnail( Base64.decodeBase64( new String(
				"newthumbnailimageAAA" ) //$NON-NLS-1$
				.getBytes( IReportDesignModel.CHARSET ) ) );

		saveAs( outFileName );
		assertTrue( compareTextFile( goldenFileName, outFileName ) );
	}

	/**
	 * Test config variable.
	 * 
	 * @throws Exception
	 *             if any error found.
	 */
	public void testConfigVars( ) throws Exception
	{
		openDesign( fileName, ULocale.ENGLISH );

		ConfigVariable configVar = new ConfigVariable( );
		configVar.setName( "VarToAdd" ); //$NON-NLS-1$
		configVar.setValue( "ValueToAdd" ); //$NON-NLS-1$

		ConfigVariable newConfigVar = new ConfigVariable( );
		newConfigVar.setName( "VarToReplace" ); //$NON-NLS-1$
		newConfigVar.setValue( "ValueToReplace" ); //$NON-NLS-1$

		ConfigVariable var = null;

		// Add new config variable and check it

		designHandle.addConfigVariable( configVar );
		var = designHandle.findConfigVariable( "VarToAdd" ); //$NON-NLS-1$
		assertNotNull( var );
		assertEquals( "ValueToAdd", var.getValue( ) ); //$NON-NLS-1$

		// Replace this config variable with new one

		designHandle.replaceConfigVariable( configVar, newConfigVar );
		var = designHandle.findConfigVariable( "VarToAdd" ); //$NON-NLS-1$
		assertNull( var );
		var = designHandle.findConfigVariable( "VarToReplace" ); //$NON-NLS-1$
		assertNotNull( var );
		assertEquals( "ValueToReplace", var.getValue( ) ); //$NON-NLS-1$

		// Remove this config variable and check it

		designHandle.dropConfigVariable( "VarToReplace" ); //$NON-NLS-1$
		var = designHandle.findConfigVariable( "ValueToReplace" ); //$NON-NLS-1$
		assertNull( var );

		// Add a config variable whose name is not provided

		try
		{
			configVar = new ConfigVariable( );
			configVar.setName( "   " ); //$NON-NLS-1$
			configVar.setValue( "value" ); //$NON-NLS-1$
			designHandle.addConfigVariable( configVar );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e
							.getErrorCode( ) );
		}

		// Add a config variable whose name exists.

		try
		{
			configVar = new ConfigVariable( );
			configVar.setName( "var1" ); //$NON-NLS-1$
			configVar.setValue( "value" ); //$NON-NLS-1$
			designHandle.addConfigVariable( configVar );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS,
					e.getErrorCode( ) );
		}

		// Delete a config variable which doesn't exist

		try
		{
			designHandle.dropConfigVariable( "NotExist" ); //$NON-NLS-1$
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e
							.getErrorCode( ) );
		}

		// Replace a non-exist config variable with new one

		try
		{
			configVar = new ConfigVariable( );
			configVar.setName( "NotExist" ); //$NON-NLS-1$
			configVar.setValue( "value" ); //$NON-NLS-1$
			designHandle.replaceConfigVariable( configVar, newConfigVar );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e
							.getErrorCode( ) );
		}

		// Replace a config variable with invalid one

		configVar = designHandle.findConfigVariable( "var1" ); //$NON-NLS-1$
		designHandle.replaceConfigVariable( configVar, newConfigVar );
	}

	/**
	 * Test jar file.
	 * 
	 * @throws Exception
	 */

	public void testScriptLibs( ) throws Exception
	{
		openDesign( scriptLibFileName, ULocale.ENGLISH );
		ScriptLib scriptLib = new ScriptLib( );
		scriptLib.setName( null );

		try
		{
			designHandle.addScriptLib( scriptLib );
			fail( "Not allowed set invalid value " );//$NON-NLS-1$

		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, e
							.getErrorCode( ) );
		}

		try
		{
			scriptLib.setName( "a.jar" );//$NON-NLS-1$
			designHandle.addScriptLib( scriptLib );
			fail( "Not allowed set invalid value " );//$NON-NLS-1$

		}
		catch ( SemanticException e )
		{
			assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS,
					e.getErrorCode( ) );
		}

		scriptLib.setName( "x.jar" );//$NON-NLS-1$
		assertEquals( 3, designHandle.getAllScriptLibs( ).size( ) );

		designHandle.addScriptLib( scriptLib );

		assertEquals( 4, designHandle.getAllScriptLibs( ).size( ) );

		scriptLib = designHandle.findScriptLib( "a.jar" );//$NON-NLS-1$
		assertNotNull( scriptLib );

		designHandle.dropScriptLib( scriptLib );
		assertEquals( 3, designHandle.getAllScriptLibs( ).size( ) );

		designHandle.shiftScriptLibs( 0, 3 );
		assertEquals(
				"c.jar", ( (ScriptLibHandle) designHandle.getAllScriptLibs( ).get( 0 ) ).getName( ) );//$NON-NLS-1$
		assertEquals(
				"x.jar", ( (ScriptLibHandle) designHandle.getAllScriptLibs( ).get( 1 ) ).getName( ) );//$NON-NLS-1$
		assertEquals(
				"b.jar", ( (ScriptLibHandle) designHandle.getAllScriptLibs( ).get( 2 ) ).getName( ) );//$NON-NLS-1$

		designHandle.dropAllScriptLibs( );
		assertEquals( 0, designHandle.getAllScriptLibs( ).size( ) );
	}

	/**
	 * Test embedded images.
	 * 
	 * @throws Exception
	 *             if any error found.
	 */
	public void testImages( ) throws Exception
	{
		openDesign( fileName, ULocale.ENGLISH );

		EmbeddedImage add = new EmbeddedImage( "VarToAdd", "image/bmp" ); //$NON-NLS-1$//$NON-NLS-2$
		EmbeddedImage replace = new EmbeddedImage( "VarToReplace", "image/gif" ); //$NON-NLS-1$//$NON-NLS-2$
		EmbeddedImage image = null;

		// Add new image and check it

		add.setData( "data".getBytes( EmbeddedImage.CHARSET ) ); //$NON-NLS-1$
		replace.setData( "data".getBytes( EmbeddedImage.CHARSET ) ); //$NON-NLS-1$

		designHandle.addImage( add );
		image = designHandle.findImage( "VarToAdd" ); //$NON-NLS-1$
		assertNotNull( image );
		assertEquals( "image/bmp", image.getType( design ) ); //$NON-NLS-1$

		// Replace this image with new one

		designHandle.replaceImage( add, replace );
		image = designHandle.findImage( "VarToAdd" ); //$NON-NLS-1$
		assertNull( image );
		image = designHandle.findImage( "VarToReplace" ); //$NON-NLS-1$
		assertNotNull( image );
		assertEquals( "image/gif", image.getType( design ) ); //$NON-NLS-1$

		// Remove this image and check it

		designHandle.dropImage( "VarToReplace" ); //$NON-NLS-1$
		image = designHandle.findImage( "ValToReplace" ); //$NON-NLS-1$
		assertNull( image );

		// Add an image whose name is not provided

		try
		{
			add = new EmbeddedImage( "  ", "value" ); //$NON-NLS-1$ //$NON-NLS-2$
			designHandle.addImage( add );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e
							.getErrorCode( ) );
		}

		// Add an image whose type is invalid

		try
		{
			add = new EmbeddedImage( " test ", "value" ); //$NON-NLS-1$ //$NON-NLS-2$
			designHandle.addImage( add );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e
							.getErrorCode( ) );
		}

		// Add an image whose name exists.

		try
		{
			add = new EmbeddedImage( "image1", "image/bmp" ); //$NON-NLS-1$ //$NON-NLS-2$
			add.setData( "data".getBytes( EmbeddedImage.CHARSET ) ); //$NON-NLS-1$
			designHandle.addImage( add );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS,
					e.getErrorCode( ) );
		}

		// Delete an image which doesn't exist

		try
		{
			designHandle.dropImage( "NotExist" ); //$NON-NLS-1$
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e
							.getErrorCode( ) );
		}

		// Replace a non-exist image with new one

		try
		{
			add = new EmbeddedImage( "NotExist", "image/bmp" ); //$NON-NLS-1$ //$NON-NLS-2$
			designHandle.replaceImage( add, replace );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e
							.getErrorCode( ) );
		}

		// Replace an image with invalid one

		try
		{
			add = designHandle.findImage( "image1" ); //$NON-NLS-1$
			replace = new EmbeddedImage( "replace", "wrong" ); //$NON-NLS-1$//$NON-NLS-2$
			designHandle.replaceImage( add, replace );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e
							.getErrorCode( ) );
		}

		try
		{
			EmbeddedImage tmp = new EmbeddedImage( "VarToAdd" ); //$NON-NLS-1$//$NON-NLS-2$
			tmp.setData( "data".getBytes( EmbeddedImage.CHARSET ) ); //$NON-NLS-1$
			designHandle.addImage( tmp );

			tmp = new EmbeddedImage(
					"VarToAdd", DesignChoiceConstants.IMAGE_TYPE_IMAGE_BMP ); //$NON-NLS-1$//$NON-NLS-2$
			tmp.setData( "data".getBytes( EmbeddedImage.CHARSET ) ); //$NON-NLS-1$
			designHandle.addImage( tmp );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS,
					e.getErrorCode( ) );
		}
	}

	/**
	 * Checks the semantic error of ReportDesign.
	 * 
	 * @throws Exception
	 */

	public void testSemanticError( ) throws Exception
	{
		openDesign( semanticCheckFileName );

		List errors = design.getAllErrors( );

		printSemanticErrors( );
		assertEquals( 5, errors.size( ) );

		int i = 0;

		assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				( (ErrorDetail) errors.get( i++ ) ).getErrorCode( ) );
		assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				( (ErrorDetail) errors.get( i++ ) ).getErrorCode( ) );
		assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS,
				( (ErrorDetail) errors.get( i++ ) ).getErrorCode( ) );
		assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				( (ErrorDetail) errors.get( i++ ) ).getErrorCode( ) );
		assertEquals( SemanticError.DESIGN_EXCEPTION_MISSING_MASTER_PAGE,
				( (ErrorDetail) errors.get( i++ ) ).getErrorCode( ) );
	}

	/**
	 * Tests opening a report file which is of the unsupported version or
	 * invalid version.
	 */

	public void testUnsupportedVersion( )
	{
		try
		{
			openDesign( "UnsupportedVersionTest.xml" ); //$NON-NLS-1$
			fail( );
		}
		catch ( DesignFileException e )
		{
			assertEquals(
					DesignParserException.DESIGN_EXCEPTION_UNSUPPORTED_VERSION,
					( (ErrorDetail) e.getErrorList( ).get( 0 ) ).getErrorCode( ) );
		}

		try
		{
			openDesign( "InvalidVersionTest.xml" ); //$NON-NLS-1$
			fail( );
		}
		catch ( DesignFileException e )
		{
			assertEquals(
					DesignParserException.DESIGN_EXCEPTION_INVALID_VERSION,
					( (ErrorDetail) e.getErrorList( ).get( 0 ) ).getErrorCode( ) );
		}
	}

	/**
	 * Tests data source parameter binding.
	 * 
	 * @throws Exception
	 */

	public void testCompatibilityDatasourceParamBinding( ) throws Exception
	{
		openDesign( datasourceBindingsFileName, ULocale.ENGLISH );

		saveAs( outFileName_2 );
		assertTrue( compareTextFile( goldenFileName_2, outFileName_2 ) );
	}

	/**
	 * Tests data source parameter binding.
	 * 
	 * @throws Exception
	 */

	public void testParseDesignInJarFile( ) throws Exception
	{
		SessionHandle session = DesignEngine.newSession( ULocale.getDefault( ) );
		designHandle = session
				.openDesign( "jar:file:" + getClassFolder( ) + INPUT_FOLDER + "testRead.jar!/test/testRead.rptdesign" ); //$NON-NLS-1$
		assertNotNull( designHandle );

		assertNotNull( designHandle.getSystemId( ) );

		LabelHandle label = (LabelHandle) designHandle
				.findElement( "labelfromLib" ); //$NON-NLS-1$
		assertNotNull( label );
		assertEquals( "a.labelfromLib", label.getElement( ).getExtendsName( ) );
		assertEquals( "blue", label.getProperty( IStyleModel.COLOR_PROP ) );

	}

	/**
	 * Tests open empty design file.
	 * 
	 * @throws Exception
	 */

	public void testParseEmptyDesignFile( )
	{
		try
		{
			openDesign( "EmptyDesignFile.xml" ); //$NON-NLS-1$
			fail( );
		}
		catch ( DesignFileException e )
		{
			assertEquals( DesignFileException.DESIGN_EXCEPTION_INVALID_XML, e
					.getErrorCode( ) );
		}
	}

	/**
	 * Tests open a design file will reading the line number.
	 * 
	 * @throws DesignFileException
	 * 
	 * @throws Exception
	 */

	public void testParseReadingLineNumber( ) throws DesignFileException
	{
		openDesign( "LineNumberParseTest.xml" ); //$NON-NLS-1$
		TableHandle table = (TableHandle) designHandle.findElement( "table1" );//$NON-NLS-1$
		assertEquals(373, design.getLineNoByID( table.getID( ) ));
		
		Cell cell = (Cell)design.getElementByID( 45 );
		assertEquals(395, design.getLineNoByID( cell.getID( ) ));
		
		ExtendedItem chart = (ExtendedItem)design.getElementByID( 34023 );
		assertEquals(400, design.getLineNoByID( chart.getID( ) ));
	}
}