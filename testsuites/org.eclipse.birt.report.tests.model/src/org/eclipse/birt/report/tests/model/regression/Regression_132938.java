/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Steps to reproduce:
 * <ol>
 * <li>New a library "LibB", and a label with content "bbb"
 * <li>New a library "LibA", includes LibB, and extends LibB.label
 * <li>New a report, includes LibA, and extends LibA.label
 * <li>Open LibB, change the content of label to "aaa"
 * <li>Switch to the report, refresh it
 * </ol>
 * <p>
 * Expected result: The content of label in report is "aaa"
 * 
 * Actual result: java.lang.NullPointerException
 * </p>
 * Test description:
 * <p>
 * Report include LibA, LibA include LibB, and extends an label from LibB.
 * Follow the description ensure no exception occur when doing refresh.
 * </p>
 */
public class Regression_132938 extends BaseTestCase
{

	private final static String INPUT = "regression_132938.xml"; //$NON-NLS-1$
	private final static String LIBRARY_A = "regression_132938_libA.xml";
	private final static String LIBRARY_B = "regression_132938_libB.xml"; //$NON-NLS-1$

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		
		// retrieve two input files from tests-model.jar file
		copyResource_INPUT( INPUT , INPUT );
		copyResource_INPUT( LIBRARY_A , LIBRARY_A );
		copyResource_INPUT( LIBRARY_B , LIBRARY_B );
		
		
	}
	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 */
	public void test_regression_132938( ) throws DesignFileException, SemanticException,
			IOException
	{
		openDesign( INPUT );

		// backup the libraryB file, as we need to modify the input file during
		// test case, the backed-up one will be copied back when case finished.

		//makeOutputDir( );
		copyFile( this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + LIBRARY_A, this
				.getFullQualifiedClassName( )
				+ "/" + OUTPUT_FOLDER + "/" + LIBRARY_A );

		
		// Open LibB, change the content of label to "bbb", write to disk.

		openLibrary( LIBRARY_A );

		LabelHandle baseLabel = (LabelHandle) libraryHandle
				.findElement( "NewLabel" ); //$NON-NLS-1$
		baseLabel.setText( "aaa" ); //$NON-NLS-1$
		libraryHandle.saveAs( this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + LIBRARY_A );

		System.out.println (( (LabelHandle) designHandle.findElement( "childlabel" ) ).getText( ) );
		// refresh report

		designHandle.reloadLibraries( );
		assertEquals( "aaa", ( (LabelHandle) designHandle //$NON-NLS-1$
				.findElement( "childlabel" ) ).getText( ) ); //$NON-NLS-1$
		
		
		// we recover the libraryB file, copied back from backup.
		
		copyFile( this.getFullQualifiedClassName( ) + "/" + OUTPUT_FOLDER + "/" + LIBRARY_A, this
				.getFullQualifiedClassName( )
				+ "/" + INPUT_FOLDER + "/" + LIBRARY_A );
	}
}
