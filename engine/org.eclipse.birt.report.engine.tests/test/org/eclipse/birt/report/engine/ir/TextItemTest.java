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

package org.eclipse.birt.report.engine.ir;

/**
 * Text element test
 * 
 * @version $Revision: 1.7 $ $Date: 2005/11/11 06:26:43 $
 */
public class TextItemTest extends ReportItemTestCase
{

	public TextItemTest( )
	{
		super( new TextItemDesign( ) );
	}

	/**
	 * Test get/setStaticText methods
	 * 
	 * set a static text
	 * 
	 * then get the texts and check the text type to test if they work correctly
	 */
	public void testStaticText( )
	{
		TextItemDesign text = new TextItemDesign( );
		//Set
		text.setText( "TestKey", "TestText" );

		//Get
		assertEquals( text.getText( ), "TestText" );
		assertEquals( text.getTextKey( ), "TestKey" );
	}

}
