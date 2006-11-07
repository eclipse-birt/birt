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

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.core.MultiElementSlot;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The Test Case of text item parse.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testHandlerError()}</td>
 * <td>test the parse errors in design file, such as the static text and value
 * expression both exsit.</td>
 * <td>The error count is 1</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testProperties()}</td>
 * <td>parse the design file and check the related content of text item, such
 * as static text which has CDATA feature, value expr, help text key and so on.
 * </td>
 * <td>Content of the properties are consistent with the design file</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testWrite()}</td>
 * <td>parse, write and parse, write again. The result of two writer files is
 * the same.</td>
 * <td>The two writer file is the same.</td>
 * </tr>
 * 
 * </table>
 *  
 */

public class TextItemParseTest extends BaseTestCase
{

	/*
	 * @see BaseTestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );

	}

	/**
	 * Test the exceptions parsing the user-defined properties.
	 */

	public void testHandlerError( )
	{
		try
		{
			openDesign( "TextItemParseTest_1.xml" ); //$NON-NLS-1$
		}
		catch ( DesignFileException e )
		{
			assertEquals( 1, e.getErrorList( ).size( ) );
			assertEquals( DesignParserException.DESIGN_EXCEPTION_CHOICE_RESTRICTION_VIOLATION,
					( (ErrorDetail) e.getErrorList( ).get( 0 ) ).getErrorCode( ) );
		}
	}

	/**
	 * Test the write for user-defined properties.
	 * 
	 * @throws Exception
	 */

	public void testWrite( ) throws Exception
	{
		openDesign( "TextItemParseTest.xml" ); //$NON-NLS-1$ 	

		MultiElementSlot pages = (MultiElementSlot) design
				.getSlot( ReportDesign.PAGE_SLOT );
		assertEquals( 1, pages.getCount( ) );
		MasterPage page = (MasterPage) design.findPage( "My Page" ); //$NON-NLS-1$
		assertNotNull( page );

		MultiElementSlot content = (MultiElementSlot) page
				.getSlot( GraphicMasterPage.CONTENT_SLOT );
		TextItem text = (TextItem) content.getContent( 1 );
		TextItemHandle textHandle = text.handle( design );
		String contentType = textHandle.getContentType( );
		assertEquals( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML, contentType );

		textHandle.setContentType( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML );
		textHandle.setContent( "new content hello <> <html></html>" ); //$NON-NLS-1$

		text = (TextItem) content.getContent( 2 );
		textHandle = text.handle( design );
		textHandle.setContent( "    text & < > ' \" static    " ); //$NON-NLS-1$
		assertEquals( "    text & < > ' \" static    ", textHandle.getProperty( TextItem.CONTENT_PROP ) ); //$NON-NLS-1$
		assertEquals( "    text & < > ' \" static    ", textHandle.getStringProperty( TextItem.CONTENT_PROP ) ); //$NON-NLS-1$

		text = (TextItem) content.getContent( 3 );
		textHandle = text.handle( design );
		textHandle.setContentKey( "odd 1" ); //$NON-NLS-1$

		save(); 
		assertTrue( compareTextFile( "TextItemParseTest_golden.xml") ); //$NON-NLS-1$
	}

	/**
	 * Test the properties for user-defined properties.
	 * 
	 * @throws Exception
	 */

	public void testProperties( ) throws Exception
	{
		openDesign( "TextItemParseTest.xml" ); //$NON-NLS-1$ 	
		MultiElementSlot pages = (MultiElementSlot) design
				.getSlot( ReportDesign.PAGE_SLOT );
		assertEquals( 1, pages.getCount( ) );
		MasterPage page = (MasterPage) design.findPage( "My Page" ); //$NON-NLS-1$
		assertNotNull( page );

		MultiElementSlot content = (MultiElementSlot) page
				.getSlot( GraphicMasterPage.CONTENT_SLOT );
		TextItem text = (TextItem) content.getContent( 1 );
		TextItemHandle textHandle = text.handle( design );
		String contentType = textHandle.getContentType( );
		assertEquals( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML, contentType );

		assertEquals( "text & < > ' \" static", //$NON-NLS-1$
				textHandle.getContent( ) );
		text = (TextItem) content.getContent( 2 );
		textHandle = text.handle( design );
		assertNull( textHandle.getContentKey( ) );
		assertEquals( DesignChoiceConstants.TEXT_CONTENT_TYPE_AUTO, textHandle
				.getContentType( ) );
		assertEquals( "    text value expr    ", textHandle.getContent( ) ); //$NON-NLS-1$

		text = (TextItem) content.getContent( 3 );
		textHandle = text.handle( design );
		assertEquals( "dynamic", textHandle.getContentKey( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.TEXT_CONTENT_TYPE_AUTO, textHandle
				.getContentType( ) );
		assertEquals(
				"text &amp; &lt; &gt; &apos; &quot; static", textHandle.getContent( ) ); //$NON-NLS-1$
		text = (TextItem) content.getContent( 4 );
		textHandle = text.handle( design );
		assertEquals(
				"<hello>text &amp; </hello>&lt; <hello>&gt; &apos; &quot; static</hello>", textHandle.getContent( ) ); //$NON-NLS-1$

	}

}