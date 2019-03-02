/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Display text for extend elelment in master page is not found
 * <p>
 * Steps to reproduce:
 * <p>
 * <ol>
 * <li>New a library, add a properties file for the library, add
 * pair"k1=actuate" to the properties file
 * <li>Add a label "aaa" in master page header, set "k1" as the text key
 * <li>Publish the library
 * <li>New a report, extend lib.masterpage
 * <li>Open extended master page in report
 * </ol>
 * <p>
 * <b>Expected result:</b>
 * <p>
 * label shows display text "actuate"
 * <p>
 * <b>Actual result:</b>
 * <p>
 * label shows text "aaa"
 * <p>
 * Test description:
 * <p>
 * Follow the steps, extends a master page from the library. Make sure the
 * display content of the text item (in master page header) shows the correct
 * text from message file.
 * <p>
 */
public class Regression_149783 extends BaseTestCase
{

	private final static String REPORT = "regression_149783.xml"; //$NON-NLS-1$

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		//copyResource_INPUT( REPORT , REPORT );
		copyInputToFile ( INPUT_FOLDER + "/" + REPORT );
	}
	
	public void tearDown( )
	{
		removeResource( );
	}
	
	/**
	 * @throws DesignFileException
	 */
	public void test_regression_149783( ) throws DesignFileException
	{
		openDesign( REPORT );
		SimpleMasterPageHandle extendMasterPage = (SimpleMasterPageHandle) designHandle
				.getMasterPages( ).get( 0 );
		TextItemHandle text = (TextItemHandle) extendMasterPage.getPageHeader( )
				.get( 0 );

		assertEquals( "actuate", text.getDisplayContent( ) ); //$NON-NLS-1$
	}
}
