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

import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The test case of <code>ExtendedDataSource</code> parser and writer.
 * <code>DataSource</code> is also tested in this test case.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testParser()}</td>
 * <td>Test properties of ExtendedDataSource and DataSource after parsing
 * design file</td>
 * <td>All properties are right</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testWriter()}</td>
 * <td>Set all properties and compare the written file with the golden file
 * </td>
 * <td>Two files are same</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSemanticCheck()}</td>
 * <td>Nothing to test this moment</td>
 * <td>No error found</td>
 * </tr>
 * </table>
 * 
 * @see DataSource
 * @see OdaDataSource
 */

public class OdaDataSourceParseTest extends BaseTestCase
{

	String fileName = "OdaDataSourceParseTest.xml"; //$NON-NLS-1$
	String outFileName = "OdaDataSourceParseTest_out.xml"; //$NON-NLS-1$
	String goldenFileName = "OdaDataSourceParseTest_golden.xml"; //$NON-NLS-1$
	String semanticCheckFileName = "OdaDataSourceParseTest_1.xml"; //$NON-NLS-1$

	private String unknowOdafileName = "OdaDataSourceParseTest_2.xml"; //$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */
/*
	protected void setUp( ) throws Exception
	{
		// do not call super.setUp() since we don't want to load extension.

		ThreadResources.setLocale( ULocale.ENGLISH );
		MetaDataDictionary.reset( );
		try
		{
			MetaDataReader.read( ReportDesign.class
					.getResourceAsStream( ROM_DEF_NAME ) );
		}
		catch ( MetaDataParserException e )
		{
			assert false;
		}

		engine = new DesignEngine( new DesignConfig( ) );
		createDesign( );
	}*/

	/**
	 * This test reads the design file, and checks the properties and style
	 * properties of line.
	 * 
	 * @throws Exception
	 *             if any exception
	 */
	public void testParser( ) throws Exception
	{
		// Test ExtendedDataSource

		OdaDataSourceHandle dataSourceHandle = getDataSource( "myDataSource" ); //$NON-NLS-1$

		// DataSource properties

		assertEquals( "script_beforeopen", dataSourceHandle.getBeforeOpen( ) ); //$NON-NLS-1$
		assertEquals( "script_beforeclose", dataSourceHandle.getBeforeClose( ) ); //$NON-NLS-1$
		assertEquals( "script_afteropen", dataSourceHandle.getAfterOpen( ) ); //$NON-NLS-1$
		assertEquals( "script_afterclose", dataSourceHandle.getAfterClose( ) ); //$NON-NLS-1$

		dataSourceHandle = getDataSource( "myDataSource1" ); //$NON-NLS-1$

		assertEquals( "Driver Class", dataSourceHandle //$NON-NLS-1$
				.getStringProperty( "odaDriverClass" ) ); //$NON-NLS-1$
		assertEquals( "URL", dataSourceHandle.getStringProperty( "odaURL" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "User", dataSourceHandle.getStringProperty( "odaUser" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "New Password", dataSourceHandle //$NON-NLS-1$
				.getStringProperty( "odaPassword" ) ); //$NON-NLS-1$

		assertEquals( "1.1", dataSourceHandle.getDesigerStateVersion( ) ); //$NON-NLS-1$
		assertEquals( "content as string", dataSourceHandle //$NON-NLS-1$
				.getDesigerStateContentAsString( ) );
		assertEquals( "content as blob", new String( dataSourceHandle //$NON-NLS-1$
				.getDesigerStateContentAsBlob( ), OdaDesignerState.CHARSET ) );
	}

	/**
	 * This test sets properties, writes the design file and compares it with
	 * golden file.
	 * 
	 * @throws Exception
	 * 
	 */
	public void testWriter( ) throws Exception
	{
		// Test ExtendedDataSource

		OdaDataSourceHandle dataSourceHandle = getDataSource( "myDataSource1" ); //$NON-NLS-1$

		// write DataSource properties

		dataSourceHandle.setBeforeOpen( "My before open" ); //$NON-NLS-1$
		dataSourceHandle.setBeforeClose( "My before close" ); //$NON-NLS-1$
		dataSourceHandle.setAfterOpen( "My after open" ); //$NON-NLS-1$
		dataSourceHandle.setAfterClose( "My after close" ); //$NON-NLS-1$

		dataSourceHandle.setProperty( "odaDriverClass", "New Driver Class" ); //$NON-NLS-1$ //$NON-NLS-2$
		dataSourceHandle.setProperty( "odaURL", "New URL" ); //$NON-NLS-1$ //$NON-NLS-2$
		dataSourceHandle.setProperty( "odaUser", "New User" ); //$NON-NLS-1$ //$NON-NLS-2$
		dataSourceHandle.setProperty( "odaPassword", "New Password" ); //$NON-NLS-1$ //$NON-NLS-2$

		dataSourceHandle.setDesigerStateVersion( "2.1" ); //$NON-NLS-1$
		dataSourceHandle
				.setDesigerStateContentAsString( "new content as string" ); //$NON-NLS-1$

		String strBlob = "new content as blob"; //$NON-NLS-1$

		dataSourceHandle.setDesigerStateContentAsBlob( strBlob
				.getBytes( OdaDesignerState.CHARSET ) );

		save( );
		assertTrue( compareFile( goldenFileName ) );
	}

	/**
	 * @throws Exception
	 */

	public void testUnknowOdaParser( ) throws Exception
	{
		openDesign( unknowOdafileName );
		save( );

		assertTrue( compareFile( "OdaDataSourceParseTest_golden_2.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Test semantic errors.
	 * 
	 * @throws Exception
	 * 
	 */
	public void testSemanticCheck( ) throws Exception
	{
		openDesign( semanticCheckFileName );
		assertEquals( 2, design.getErrorList( ).size( ) );
		ErrorDetail error = (ErrorDetail) design.getErrorList( ).get( 0 );
		assertEquals( SemanticError.DESIGN_EXCEPTION_INVALID_MANIFEST, error
				.getErrorCode( ) );
	}

	private OdaDataSourceHandle getDataSource( String name ) throws Exception
	{
		openDesign( fileName );

		OdaDataSourceHandle dataSource = (OdaDataSourceHandle) designHandle
				.findDataSource( name );
		assertNotNull( dataSource );

		return dataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#teardown()
	 */

	protected void tearDown( ) throws Exception
	{
		MetaDataDictionary.reset( );

		engine = null;

		super.tearDown( );
	}

}