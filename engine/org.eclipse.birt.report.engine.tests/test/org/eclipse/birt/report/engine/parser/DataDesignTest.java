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

import org.eclipse.birt.report.engine.ir.DataItemDesign;

/**
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DataDesignTest extends AbstractDesignTestCase {
	protected DataItemDesign data;

	public void setUp() throws Exception {
		loadDesign("DataItem_test.xml");
		;
		data = (DataItemDesign) report.getContent(0);
		assertTrue(data != null);
	}

	public void testDataBasic() {
		assertEquals(3, data.getHeight().getMeasure(), Double.MIN_VALUE);
		assertEquals(3, data.getWidth().getMeasure(), Double.MIN_VALUE);
		assertEquals(2, data.getX().getMeasure(), Double.MIN_VALUE);
		assertEquals(3, data.getY().getMeasure(), Double.MIN_VALUE);
		assertEquals("myData", data.getName());
		assertEquals("data help", data.getHelpText());
		assertEquals("http://www.msn.com", data.getAction().getHyperlink().getScriptText());
	}

	// public void testDataStyle( )
//	{
//		IStyle style = report.findStyle( data.getStyleName( ) );
//		assertEquals( "#008000", style.getBackgroundColor( ) );
//		assertEquals( "black", style.getBorderLeftColor( ) );
//		assertEquals( "black", style.getBorderRightColor( ) );
//		assertEquals( "#0000ff", style.getBorderTopColor( ) );
//		assertEquals( "#ff0000", style.getBorderBottomColor( ) );
//		assertEquals( "dashed", style.getBorderLeftStyle( ) );
//		assertEquals( "dashed", style.getBorderRightStyle( ) );
//		assertEquals( "dashed", style.getBorderTopStyle( ) );
//		assertEquals( "dashed", style.getBorderBottomStyle( ) );
//		assertEquals( "4pt", style.getBorderLeftWidth( ) );
//		assertEquals( "3pt", style.getBorderTopWidth( ) );
//		assertEquals( "2pt", style.getBorderRightWidth( ) );
//		assertEquals( "1pt", style.getBorderBottomWidth( ) );
//		assertEquals( "20%", style.getPaddingBottom( ) );
//		assertEquals( "12pt", style.getPaddingTop( ) );
//		assertEquals( "1pt", style.getPaddingLeft( ) );
//		assertEquals( "23%", style.getPaddingRight( ) );
//
//		assertEquals( "#008000", style.getColor( ) );
//		assertEquals( "Times", style.getFontFamily( ) );
//		assertEquals( "23%", style.getFontSize( ) );
//		assertEquals( "bolder", style.getFontWeight( ) );
//		assertEquals( "overline", style.getTextOverline( ) );
//		assertEquals( "line-through", style.getTextLineThrough( ) );
//
//		assertEquals( "justify", style.getTextAlign( ) );
//
//		assertEquals( "fff", style.getStringFormat( ) );
//		assertEquals( "##", style.getNumberFormat( ) );
//		assertEquals( "dd-yy", style.getDateFormat( ) );
//		assertEquals( "justify", style.getNumberAlign( ) );
//	}

}
