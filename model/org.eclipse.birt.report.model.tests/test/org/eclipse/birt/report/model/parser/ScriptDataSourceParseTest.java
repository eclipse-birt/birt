/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testParser()}</td>
 * <td>Get all properties.</td>
 * <td>The property values are right.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testWriter()}</td>
 * <td>Set all properties with new value and write.</td>
 * <td>The output file is as same as golden file.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testSemanticCheck()}</td>
 * <td>Read a design file with semantic errors.</td>
 * <td>Errors of missing script open and close are found.</td>
 * </tr>
 * </table>
 *
 */
public class ScriptDataSourceParseTest extends BaseTestCase {

	String fileName = "ScriptDataSourceTest.xml"; //$NON-NLS-1$
	String outFileName = "ScriptDataSourceTest_out.xml"; //$NON-NLS-1$
	String goldenFileName = "ScriptDataSourceTest_golden.xml"; //$NON-NLS-1$
	String semanticCheckFileName = "ScriptDataSourceTest_1.xml"; //$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * This test reads the design file, and checks the properties and style
	 * properties of line.
	 *
	 * @throws Exception if any exception.
	 */

	public void testParser() throws Exception {
		ScriptDataSourceHandle dataSource = getDataSource();

		assertEquals("script_open", dataSource.getOpen()); //$NON-NLS-1$
		assertEquals("script_close", dataSource.getClose()); //$NON-NLS-1$
	}

	/**
	 * This test sets properties, writes the design file and compares it with golden
	 * file.
	 *
	 * @throws Exception if any exception.
	 */

	public void testWriter() throws Exception {
		ScriptDataSourceHandle dataSource = getDataSource();

		dataSource.setOpen("My open script"); //$NON-NLS-1$
		dataSource.setClose("My close script"); //$NON-NLS-1$

		save();
		assertTrue(compareFile(goldenFileName));
	}

//	/**
//	 * Test semantic errors.
//	 *
//	 * @throws Exception
//	 *             if any exception.
//	 */
//
//	public void testSemanticCheck( ) throws Exception
//	{
//		openDesign( semanticCheckFileName );
//		assertEquals( 2, design.getErrorList( ).size( ) );
//		List errors = design.getErrorList( );
//		int i = 0;
//		ErrorDetail error = ( (ErrorDetail) errors.get( i++ ) );
//		assertEquals( "myDataSource1", error.getElement( ).getName( ) ); //$NON-NLS-1$
//		assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, error.getErrorCode( ) );
//
//		error = ( (ErrorDetail) errors.get( i++ ) );
//		assertEquals( "myDataSource2", error.getElement( ).getName( ) ); //$NON-NLS-1$
//		assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, error.getErrorCode( ) );
//	}

	/**
	 * Returns the data source.
	 *
	 * @return the data source for test
	 * @throws Exception if any exception.
	 */

	private ScriptDataSourceHandle getDataSource() throws Exception {
		openDesign(fileName);

		ScriptDataSourceHandle dataSource = (ScriptDataSourceHandle) designHandle.findDataSource("myDataSource"); //$NON-NLS-1$
		assertNotNull(dataSource);

		return dataSource;
	}

}
