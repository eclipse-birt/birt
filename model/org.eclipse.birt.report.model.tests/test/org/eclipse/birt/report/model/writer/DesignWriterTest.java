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

package org.eclipse.birt.report.model.writer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Unit test for DesignWriter, ReportDesignHandle.
 * <p>
 * <strong>Test Cases </strong>
 * <p>
 * <table border="1" style="border-collapse: collapse" cellpadding="2"
 * cellspacing="2" bordercolor="black">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected Result</th>
 * 
 * <tr>
 * <td><a name="T1">testSerializeOutputStream </a></td>
 * <td>Save the opened design file by calling
 * {@link ReportDesignHandle#serialize}</td>
 * <td>Get the output stream after save, reopen the design from the output
 * stream, then save again by calling {@link ReportDesignHandle#save()},
 * finally compare the final output file to a golden file, they should be
 * identical except the modification date</td>
 * </tr>
 * 
 * <tr>
 * <td>testUTF8Encoding</td>
 * <td>Tests to write some character that are not UTF-8.</td>
 * <td>The file can be written and read correctly.</td>
 * </tr>
 * 
 * </table>
 * 
 * 
 * @see org.eclipse.birt.report.model.writer.DesignWriter
 * @see org.eclipse.birt.report.model.api.ReportDesignHandle
 * @see org.eclipse.birt.report.model.util.XMLWriter
 * 
 */
public class DesignWriterTest extends BaseTestCase
{

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp( ) throws Exception
	{
		openDesign( "DesignWriterTest.xml" ); //$NON-NLS-1$
		assertEquals( 0, design.getErrorList( ).size( ) );
	}

	/**
	 * Save the design by calling Please see <a href="#T1">here </a> for detail
	 * test case description.
	 * 
	 * @throws Exception
	 */
	public void testSerializeOutputStream( ) throws Exception
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		designHandle.serialize( out );

		ByteArrayInputStream is = new ByteArrayInputStream( out.toByteArray( ) );
		openDesign( "", is ); //$NON-NLS-1$
		assertNotNull( design );

		save( );
		assertTrue( compareFile( "DesignWriterTest_golden.xml" ) ); //$NON-NLS-1$

	}

	/**
	 * Tests UTF-8 writer and DesignReader.
	 * 
	 * @throws Exception
	 */
	public void testUTF8Encoding( ) throws Exception
	{
		try
		{
			openDesign("DesignWriterTest_1.xml", ULocale.ENGLISH); //$NON-NLS-1$
			fail( );
		}
		catch ( DesignFileException e )
		{
			List<ErrorDetail> list = e.getErrorList();
			assertTrue(list.get(0).getExceptionName().endsWith("MalformedByteSequenceException"));
		}

		createDesign( );

		ElementFactory factory = new ElementFactory( design );
		TextItemHandle handle = factory.newTextItem( "bodyText" ); //$NON-NLS-1$
		designHandle.getBody( ).add( handle );

		LabelHandle labelHandle = factory.newLabel( "bodyLabel" ); //$NON-NLS-1$
		designHandle.getBody( ).add( labelHandle );

		labelHandle.setText( "doesn\u2019t have" ); //$NON-NLS-1$
		labelHandle.setName( "he\u0020llo<&\"" ); //$NON-NLS-1$

		labelHandle = factory.newLabel( "bodyLabel1" ); //$NON-NLS-1$
		designHandle.getBody( ).add( labelHandle );
		labelHandle.setText( "<><>" ); //$NON-NLS-1$
		labelHandle.setName( "\u4E2D\u6587" ); //$NON-NLS-1$

		// set two ' in chinese.

		handle.setContent( "doesn\u2019t have" ); //$NON-NLS-1$
		save( );

		readOutputFile( "DesignWriterTest_1_out.xml" ); //$NON-NLS-1$

		handle = (TextItemHandle) designHandle.findElement( "bodyText" ); //$NON-NLS-1$
		assertEquals( "doesn\u2019t have", handle.getContent( ) ); //$NON-NLS-1$

		labelHandle = (LabelHandle) designHandle.findElement( "he\u0020llo<&\"" ); //$NON-NLS-1$
		assertNotNull( labelHandle );

		labelHandle = (LabelHandle) designHandle.findElement( "\u4E2D\u6587" ); //$NON-NLS-1$
		assertNotNull( labelHandle );
	}

	/**
	 * Tests UTF signature.
	 * 
	 * @throws Exception
	 */
	public void testBOMSignature( ) throws Exception
	{
		openDesign( "DesignWriterTest_UTF8BOM.xml" ); //$NON-NLS-1$
		assertNotNull( designHandle );
		save( );
		createDesign( );

		readOutputFile( "DesignWriterTest_UTF8BOM_out.xml" ); //$NON-NLS-1$

		assertEquals( UnicodeUtil.SIGNATURE_UTF_8, design.getUTFSignature( ) );
		assertNotNull( designHandle );
	}

	/**
	 * @throws Exception
	 */
	public void testWriter( ) throws Exception
	{
		// test that writer will out write out default value for element or
		// structure

		createDesign( );
		ElementFactory elemFactory = new ElementFactory( design );

		StyleHandle style = elemFactory.newStyle( "Style1" ); //$NON-NLS-1$
		style.setBorderBottomStyle( DesignChoiceConstants.LINE_STYLE_SOLID );
		designHandle.getStyles( ).add( style );

		ImageHandle image = elemFactory.newImage( "Image1" ); //$NON-NLS-1$
		Action action = StructureFactory.createAction( );
		image.setAction( action );

		designHandle.getBody( ).add( image );

		save( );
		assertTrue( compareFile( "testWriter_golden.xml" ) ); //$NON-NLS-1$  

	}

	/**
	 * Tests the save() to give a file name like "file:/c:/test" -- containing
	 * file schema.
	 * 
	 * @throws Exception
	 */
	public void testSave( ) throws Exception
	{
		openDesign( "DesignWriterTest.xml" ); //$NON-NLS-1$

		String folder = getTempFolder( ) + OUTPUT_FOLDER;

		File f = new File( folder );
		if ( !f.exists( ) )
			f.mkdirs( );

		designHandle.setFileName(f.toURI().toURL() + "DesignWriterTest.xml"); //$NON-NLS-1$
		designHandle.save( );
	}

	/**
	 * Reads the content in the output stream as a design file. Design handle
	 * and design are updated.
	 * 
	 * @throws Exception
	 */
	private void readOutputFile( String outputFileName ) throws Exception
	{
		String fileContent = os.toString( "utf-8" ); //$NON-NLS-1$
		ByteArrayInputStream is = new ByteArrayInputStream( fileContent.getBytes( "utf-8" ) ); //$NON-NLS-1$

		// the design name can be empty

		openDesign( outputFileName, is );
	}

	/**
	 * Test item in structure contain another structure. for example: report
	 * item has toc structure, and toc can contain other structure such as
	 * DateTimeFormat , StringFormat.
	 * 
	 * @throws Exception
	 */
	public void testStructContainStrucut( ) throws Exception
	{
		// toc

		createDesign( );
		ElementFactory elemFactory = new ElementFactory( design );
		LabelHandle labelHandle = elemFactory.newLabel( "label1" );//$NON-NLS-1$
		designHandle.getBody( ).add( labelHandle );
		TOC toc = StructureFactory.createTOC( "toc" );//$NON-NLS-1$\
		FormatValue formatValueToSet = new DateTimeFormatValue( );
		formatValueToSet.setCategory( "Short Date" );//$NON-NLS-1$
		formatValueToSet.setPattern( "yyyy/mm/dd" );//$NON-NLS-1$
		toc.setProperty( TOC.DATE_TIME_FORMAT_MEMBER, formatValueToSet );

		labelHandle.addTOC( toc );

		save( );
		// save successfully and no assert error.
		compareFile( "DesignWriterTest_1_golden.xml" );//$NON-NLS-1$

	}

	/**
	 * Test item in structure list contain another structure. for example: style
	 * has highlightrule list. and each highlightrule can contain other
	 * structure such as DateTimeFormat, StringFormat.
	 * 
	 * @throws Exception
	 */
	public void testStructListContainStruct( ) throws Exception
	{
		// hightlightrule

		createDesign( );
		ElementFactory elemFactory = new ElementFactory( design );
		StyleHandle styleHandle = elemFactory.newStyle( "style1" );//$NON-NLS-1$
		designHandle.getStyles( ).add( styleHandle );

		HighlightRule rule = StructureFactory.createHighlightRule( );
		FormatValue formatValueToSet = new DateTimeFormatValue( );
		formatValueToSet.setCategory( "Short Date" );//$NON-NLS-1$
		formatValueToSet.setPattern( "yyyy/mm/dd" );//$NON-NLS-1$
		rule.setProperty( TOC.DATE_TIME_FORMAT_MEMBER, formatValueToSet );

		List<HighlightRule> list = new ArrayList<HighlightRule>();
		list.add( rule );
		styleHandle.getElement( )
				.setProperty( IStyleModel.HIGHLIGHT_RULES_PROP, list );

		save( );

		// save successfully and no assert error.
		compareFile( "DesignWriterTest_2_golden.xml" );//$NON-NLS-1$
	}
}