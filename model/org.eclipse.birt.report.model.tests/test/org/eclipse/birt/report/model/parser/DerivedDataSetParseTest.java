
package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the parser and all APIs for derived data set.
 * 
 */
public class DerivedDataSetParseTest extends BaseTestCase
{

	private static final String fileName = "DerivedDataSetParseTest.xml"; //$NON-NLS-1$

	/**
	 * Tests the parser and get APIs for derived data set.
	 * 
	 * @throws Exception
	 */
	public void testParser( ) throws Exception
	{
		openDesign( fileName );
		DerivedDataSetHandle derivedDataSetHandle = (DerivedDataSetHandle) designHandle
				.findDataSet( "derivedDataSet" ); //$NON-NLS-1$

		assertEquals(
				"derived.extension", derivedDataSetHandle.getExtensionID( ) ); //$NON-NLS-1$
		assertEquals(
				"query text for the derived data set", derivedDataSetHandle.getQueryText( ) ); //$NON-NLS-1$

		List<DataSetHandle> dataSets = derivedDataSetHandle.getInputDataSets( );
		assertEquals( designHandle.findDataSet( "DataSet1" ), dataSets.get( 0 ) ); //$NON-NLS-1$
		assertEquals( designHandle.findDataSet( "DataSet2" ), dataSets.get( 1 ) ); //$NON-NLS-1$
	}

	/**
	 * Tests the writer and write APIs for derived data set. Also test the
	 * factory method for derived data set.
	 * 
	 * @throws Exception
	 */
	public void testWriter( ) throws Exception
	{
		openDesign( fileName );

		openDesign( fileName );
		DerivedDataSetHandle derivedDataSetHandle = (DerivedDataSetHandle) designHandle
				.findDataSet( "derivedDataSet" ); //$NON-NLS-1$

		// set query-text
		derivedDataSetHandle
				.setQueryText( "updated " + derivedDataSetHandle.getQueryText( ) ); //$NON-NLS-1$

		// create another derived-data set
		derivedDataSetHandle = designHandle.getElementFactory( )
				.newDerivedDataSet( null, "derived.extensionID.new" ); //$NON-NLS-1$
		derivedDataSetHandle.addInputDataSets( "DataSet3" ); //$NON-NLS-1$
		derivedDataSetHandle.addInputDataSets( "DataSet1" ); //$NON-NLS-1$
		designHandle.getDataSets( ).add( derivedDataSetHandle );

		save( );
		assertTrue( compareFile( "DerivedDataSetParseTest_golden.xml" ) ); //$NON-NLS-1$
	}
}
