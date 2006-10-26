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

package org.eclipse.birt.report.engine.parser;

import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.GraphicMasterPageDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
import org.eclipse.birt.report.engine.ir.Report;

/**
 * Test Parser.
 * 
 * @version $Revision: 1.9 $ $Date: 2005/11/11 06:26:49 $
 */
public class PageSetupTest extends TestCase
{

	protected Report report;

	public void setUp( ) throws Exception
	{
		InputStream in = this.getClass( ).getResourceAsStream( "pagesetup.xml" );
		assertTrue( in != null );
		ReportParser parser = new ReportParser( );
		report = parser.parse( "pagesetup.xml", in );
		assertTrue( report != null );
		assertTrue( report.getErrors( ).isEmpty( ) );

	}

	/**
	 * test case to test the parser,especially the capability to parse the Page
	 * Setup. To get the content about Page Setup from an external file and then
	 * compare the expected result with the real result of each property of Page
	 * Setup. If they are the same,that means the IR is correct, otherwise,
	 * there exists errors in the parser
	 */
	public void testMasterPage( ) throws Exception
	{
		PageSetupDesign pageSetup = report.getPageSetup( );
		assertEquals( pageSetup.getMasterPageCount( ), 1 );

		GraphicMasterPageDesign page = (GraphicMasterPageDesign) pageSetup
				.getMasterPage( 0 );
		assertEquals( "page", page.getName( ) );
		assertEquals( 29.7, page.getPageHeight( ).convertTo(
				DimensionType.UNITS_CM ), 0.1 );
		assertEquals( 21, page.getPageWidth( ).convertTo(
				DimensionType.UNITS_CM ), 0.1 );

		assertEquals( "2.5cm", page.getBottomMargin( ).toString( ) );
		assertEquals( "2.5cm", page.getTopMargin( ).toString( ) );
		assertEquals( "0.5cm", page.getLeftMargin( ).toString( ) );
		assertEquals( "0.5cm", page.getRightMargin( ).toString( ) );

		assertEquals( page.getContentCount( ), 2 );
		LabelItemDesign label = (LabelItemDesign) page.getContent( 0 );
		assertEquals( "0cm", label.getX( ).toString( ) );
		assertEquals( "10cm", label.getY( ).toString( ) );
		assertEquals( "1.2cm", label.getHeight( ).toString( ) );
		assertEquals( "10cm", label.getWidth( ).toString( ) );
		assertEquals( "PAGE HEADER", label.getText( ) );

	}
}
