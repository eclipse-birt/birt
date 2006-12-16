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

package org.eclipse.birt.report.tests.model.regression;

import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Steps to reproduce:</b>
 * <ol>
 * <li>New a library "Lib1", add a table
 * <li>New a library "Lib2", extends Lib1.table
 * <li>New a report, extends Lib2.table
 * <li>Delete table in Lib1, and publish the library
 * <li>Refresh the report
 * </ol>
 * <b>Actual result:</b>
 * <p>
 * org.eclipse.swt.SWTException: Failed to execute runnable
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Follow the steps in bug description, no error thrown out. and the reference
 * of table in report is unresolved
 * 
 */
public class Regression_153220 extends BaseTestCase
{
	private String filename = "Regression_153220.xml"; //$NON-NLS-1$
	private String lib1name = "Regression_153220_lib1.xml"; //$NON-NLS-1$
	private String lib2name = "Regression_153220_lib2.xml"; //$NON-NLS-1$
	
	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
			
		copyInputToFile ( INPUT_FOLDER + "/" + filename );
		copyInputToFile ( INPUT_FOLDER + "/" + lib1name );
		copyInputToFile ( INPUT_FOLDER + "/" + lib2name );
	}
	
	public void tearDown( )
	{
		removeResource( );
	}
	/**
	 * @throws IOException
	 * @throws SemanticException
	 * @throws DesignFileException
	 */
	public void test_Regression_153220( ) throws IOException,
			DesignFileException, SemanticException
	{
		String fileOutput = getTempFolder() + "/" + INPUT_FOLDER + "/" + filename;
		String lib1Output = getTempFolder() + "/" + INPUT_FOLDER + "/" + lib1name;
		String lib2Output = getTempFolder() + "/" + INPUT_FOLDER + "/" + lib2name;
		
		String TempFile = this.genOutputFile( filename );
		saveAs( TempFile );
		String TempLib1 = this.genOutputFile( lib1name );
		saveAs( TempLib1 );
		String TempLib2 = this.genOutputFile( lib2name  );
		saveAs( TempLib2 );	
		
		// open and modify the library files under the output folder.

		//copyFile( lib1Input, lib1name );
		//copyFile( lib2Input, lib2name );
		//copyFile( fileInput, filename );

		sessionHandle = new DesignEngine( new DesignConfig( ) )
				.newSessionHandle( ULocale.ENGLISH );
		designHandle = sessionHandle.openDesign( fileOutput );

		designHandle.includeLibrary( lib2name, "lib2" ); //$NON-NLS-1$
		libraryHandle = designHandle.getLibrary( "lib2" ); //$NON-NLS-1$
		TableHandle table = (TableHandle) libraryHandle.findElement( "table2" ); //$NON-NLS-1$

		ElementFactory factory = designHandle.getElementFactory( );
		TableHandle rtable = (TableHandle) factory.newElementFrom( table,
				"Rtable" ); //$NON-NLS-1$
		designHandle.getBody( ).add( rtable );

		// drop table in lib1 and save the library
		libraryHandle = sessionHandle.openLibrary( lib1name );
		libraryHandle.findElement( "table1" ).drop( ); //$NON-NLS-1$
		//libraryHandle.save( );
		libraryHandle.saveAs(lib1Output );
		
		// reload lib2, no error, and table reference is unresolved
		designHandle.reloadLibrary( lib2Output );
		assertFalse( rtable.isValidReferenceForCompoundElement( ) );
	}
}
