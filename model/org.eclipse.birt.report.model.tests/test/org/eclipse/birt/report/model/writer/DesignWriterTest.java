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
import java.util.List;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
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
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.XMLParserException;

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
 * stream, then save again by calling
 * {@link ReportDesignHandle#saveAs( String )}, finally compare the final
 * output file to a golden file, they should be identical except the
 * modification date</td>
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

		saveAs( "DesignWriterTest_out.xml" ); //$NON-NLS-1$
		assertTrue( compareTextFile( "DesignWriterTest_golden.xml", //$NON-NLS-1$
				"DesignWriterTest_out.xml" ) ); //$NON-NLS-1$

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
			openDesign( "DesignWriterTest_1.xml" ); //$NON-NLS-1$	
			fail( );
		}
		catch ( DesignFileException e )
		{
			List list = e.getErrorList( );
			ErrorDetail tmp = (ErrorDetail) list.get( 0 );
			assertEquals( tmp.getErrorCode( ),
					XMLParserException.DESIGN_EXCEPTION_SAX_ERROR );
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
		saveAs( "DesignWriterTest_1_out.xml" ); //$NON-NLS-1$

		designHandle = sessionHandle.openDesign( getClassFolder( )
				+ OUTPUT_FOLDER + "DesignWriterTest_1_out.xml" ); //$NON-NLS-1$
		design = designHandle.getDesign( );

		handle = (TextItemHandle) designHandle.findElement( "bodyText" ); //$NON-NLS-1$
		assertEquals( "doesn\u2019t have", handle.getContent( ) ); //$NON-NLS-1$

		labelHandle = (LabelHandle) designHandle
				.findElement( "he\u0020llo<&\"" ); //$NON-NLS-1$
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
		saveAs( "DesignWriterTest_UTF8BOM_out.xml" ); //$NON-NLS-1$

		createDesign( );

		designHandle = sessionHandle.openDesign( getClassFolder( )
				+ OUTPUT_FOLDER + "DesignWriterTest_UTF8BOM_out.xml" ); //$NON-NLS-1$
		design = designHandle.getDesign( );

		assertEquals( UnicodeUtil.SIGNATURE_UTF_8, design.getUTFSignature( ) );
		assertNotNull( designHandle );
	}

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

		saveAs( "testWriter_out.xml" ); //$NON-NLS-1$
		assertTrue( compareTextFile(
				"testWriter_golden.xml", "testWriter_out.xml" ) ); //$NON-NLS-1$ //$NON-NLS-2$ 

	}
	
	/**
	 * Write page break inside
	 * since 3.2.8
	 * @throws Exception
	 */
	
	public void testWriterPageBreak( ) throws Exception
	{
		createDesign( );
		ElementFactory elemFactory = new ElementFactory( design );
		
		LabelHandle label = elemFactory.newLabel( "label" );//$NON-NLS-1$
		label.setProperty( IStyleModel.PAGE_BREAK_INSIDE_PROP , DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID );
		
		designHandle.getBody( ).add( label );
		
		saveAs("testWriterPageBreak_out.xml");//$NON-NLS-1$
		assertTrue( compareTextFile(
				"testWriterPageBreak_golden.xml", "testWriterPageBreak_out.xml" ) ); //$NON-NLS-1$ //$NON-NLS-2$ 

	}

	/**
	 * Tests the saveAs() to give a file name like "file:/c:/test" -- containing
	 * file schema.
	 * 
	 * @throws Exception
	 */

	public void testSave( ) throws Exception
	{
		String fileName = URIUtil.FILE_SCHEMA
				+ ":" + getClassFolder( ) + INPUT_FOLDER + "DesignWriterTest.xml"; //$NON-NLS-1$ //$NON-NLS-2$
		sessionHandle = new DesignEngine( new DesignConfig( ) )
				.newSessionHandle( null );
		assertNotNull( sessionHandle );

		designHandle = sessionHandle.openDesign( fileName );
		design = designHandle.getDesign( );
		designHandle
				.saveAs( URIUtil.FILE_SCHEMA
						+ ":" + getClassFolder( ) + OUTPUT_FOLDER + "DesignWriterTest_out_2.xml" ); //$NON-NLS-1$ //$NON-NLS-2$

	}
}