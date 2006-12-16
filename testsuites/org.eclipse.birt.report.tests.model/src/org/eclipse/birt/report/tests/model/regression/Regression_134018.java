/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * <p>
 * Report item in extended master page lost when refreshing the report
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>New a library, add a label with content "aaa" in master page header
 * <li>New a report, extend lib.masterpage
 * <li>Switch to library, change content of label to "bbb"
 * <li>Switch to report, refresh it
 * </ol>
 * <b>Expected result:</b>
 * <p>
 * The content of label in report is changed to "bbb"
 * <p>
 * <b>Actual result:</b>
 * <p>
 * Label lost in outline untill reopen the report
 * 
 * <p>
 * Test description:
 * <p>
 * Follow the steps, make sure that label still exist in report when refreshing
 * the report
 * </p>
 */
public class Regression_134018 extends BaseTestCase
{

	private final static String LIBRARY = "regression_134018_lib.xml"; //$NON-NLS-1$

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		
		// retrieve two input files from tests-model.jar file
		
		//copyResource_INPUT( LIBRARY , LIBRARY );
		copyInputToFile (INPUT_FOLDER+"/"+LIBRARY);
		
		
		
	}
	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_134018( ) throws DesignFileException, SemanticException
	{
		openLibrary( LIBRARY );
		
		DesignEngine engine = new DesignEngine( new DesignConfig( ) );
		
		SessionHandle session = engine.newSessionHandle( ULocale.ENGLISH );
		ReportDesignHandle designHandle = session.createDesign( );
		
		//designHandle.setFileName( this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER
		//		+ "/" + LIBRARY ); //$NON-NLS-1$

		// include the lib and extends lib.masterpage.
		
		
		//designHandle.includeLibrary( this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER	
		//		+ "/" + LIBRARY, "regression_134018_lib" ); //$NON-NLS-1$
		//designHandle.includeLibrary( LIBRARY, "regression_134018_lib" );
		
		LibraryHandle lib = designHandle.getLibrary( "regression_134018_lib" ); //$NON-NLS-1$
		MasterPageHandle basePage = lib.findMasterPage( "basePage" ); //$NON-NLS-1$

		ElementFactory factory = designHandle.getElementFactory( );
		SimpleMasterPageHandle newPage = (SimpleMasterPageHandle) factory
				.newElementFrom( basePage, "newMasterPage" ); //$NON-NLS-1$

		designHandle.getMasterPages( ).add( newPage );

		// Switch to library, change content of label to "bbb"

		//openLibrary( LIBRARY );
		LabelHandle baseLabel = (LabelHandle) ( (SimpleMasterPageHandle) libraryHandle
				.findMasterPage( "basePage" ) ).getPageHeader( ).get( 0 ); //$NON-NLS-1$

		assertEquals( "baseLabel", baseLabel.getName( ) ); //$NON-NLS-1$
		assertEquals( "aaa", baseLabel.getText( ) ); //$NON-NLS-1$

		baseLabel.setText( "bbb" ); //$NON-NLS-1$

		// refresh the report, reload the library.

		designHandle.reloadLibrary( libraryHandle );
		LabelHandle childLabel = (LabelHandle) ( (SimpleMasterPageHandle) designHandle
				.findMasterPage( "newMasterPage" ) ).getPageHeader( ).get( 0 ); //$NON-NLS-1$

		assertNotNull( childLabel );

	}
}
