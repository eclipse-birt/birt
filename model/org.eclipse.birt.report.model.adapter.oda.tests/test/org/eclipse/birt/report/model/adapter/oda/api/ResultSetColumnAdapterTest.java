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

package org.eclipse.birt.report.model.adapter.oda.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.ModelOdaAdapter;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.ModelFactory;
import org.eclipse.birt.report.model.adapter.oda.util.BaseTestCase;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.datatools.connectivity.oda.design.AxisAttributes;
import org.eclipse.datatools.connectivity.oda.design.AxisType;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.OutputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ResultSets;
import org.eclipse.datatools.connectivity.oda.design.ValueFormatHints;

/**
 * Test cases to convert Oda result set and ROM result set columns and column
 * hints.
 * 
 */

public class ResultSetColumnAdapterTest extends BaseTestCase
{

	/**
	 * Converts ROM result set columns with column hints to ODA data set
	 * resultSets.
	 * 
	 * @throws Exception
	 */

	public void testToODAResultSetsWithColumnHint( ) throws Exception
	{
		// create oda set handle.

		openDesign( "OdaDataSetConvertResultSetsTest.xml" ); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		DataSetDesign setDesign = new ModelOdaAdapter( )
				.createDataSetDesign( setHandle );

		DesignValues values = ModelFactory.eINSTANCE.createDesignValues( );
		values.setResultSets( setDesign.getResultSets( ) );

		saveDesignValuesToFile( values );

		assertTrue( compareTextFile( "ResultSetsWithHint_golden.xml" ) ); //$NON-NLS-1$

	}

	/**
	 * Converts ODA result set columns to ROM result set columns.
	 * 
	 * <ul>
	 * <li>no the latest design session response, update ROM values.
	 * <li>if the latest design session response changed, update ROM values.
	 * <li>if the latest design session response didn't change and ROM values
	 * changed, don't update ROM values.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testToROMResultSetsWithColumnHints( ) throws Exception
	{
		// create oda set handle.

		openDesign( "OdaDataSetConvertResultSetsTest.xml" ); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		// get the latest data set design.

		DataSetDesign setDesign = new ModelOdaAdapter( )
				.createDataSetDesign( setHandle );

		// oda data set design changed, update ROM values. still keep report
		// parameter link.

		ResultSets sets = setDesign.getResultSets( );
		ResultSetDefinition setDefn = (ResultSetDefinition) sets
				.getResultSetDefinitions( ).get( 0 );

		updateResultSetDefinition1( setDefn );

		new ModelOdaAdapter( )
				.updateDataSetHandle( setDesign, setHandle, false );

		save( );
		assertTrue( compareTextFile( "OdaDataSetConvertResultSetsTest_golden.xml" ) ); //$NON-NLS-1$

		openDesign( "OdaDataSetConvertResultSetsTest_1.xml" ); //$NON-NLS-1$
		setHandle = (OdaDataSetHandle) designHandle.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		// get the latest data set design.

		setDesign = new ModelOdaAdapter( ).createDataSetDesign( setHandle );

		// oda data set design changed, update ROM values.

		sets = setDesign.getResultSets( );
		setDefn = (ResultSetDefinition) sets.getResultSetDefinitions( ).get( 0 );

		// oda data set design changed, update ROM values. still keep report
		// parameter link.

		updateResultSetDefinition1( setDefn );
		new ModelOdaAdapter( )
				.updateDataSetHandle( setDesign, setHandle, false );

		save( );
		assertTrue( compareTextFile( "OdaDataSetConvertResultSetsTest_1_golden.xml" ) ); //$NON-NLS-1$

		// the oda data set design is not changed. ROM values are changed.
		// Should keep rom values.

		openDesign( "OdaDataSetConvertResultSetsTest_1.xml" ); //$NON-NLS-1$
		setHandle = (OdaDataSetHandle) designHandle.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		// get the latest data set design.

		setDesign = new ModelOdaAdapter( ).createDataSetDesign( setHandle );

		Iterator hints = setHandle.columnHintsIterator( );
		Iterator columns = setHandle.resultSetIterator( );

		updateResultSetColumnAndHint(
				(OdaResultSetColumnHandle) columns.next( ),
				(ColumnHintHandle) hints.next( ) );

		new ModelOdaAdapter( )
				.updateDataSetHandle( setDesign, setHandle, false );

		save( );
		assertTrue( compareTextFile( "OdaDataSetConvertResultSetsTest_2_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Converts ODA result set columns to ROM result set columns.
	 * 
	 * <ul>
	 * <li>the data type is BLOB.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testToROMResultSetsWithBlobType( ) throws Exception
	{
		openDesign( "OdaDataSetConvertResultSetsTest_2.xml" ); //$NON-NLS-1$

		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		// get the latest data set design.

		DataSetDesign setDesign = new ModelOdaAdapter( )
				.createDataSetDesign( setHandle );

		updateResultSetDefinition2( setHandle );
		new ModelOdaAdapter( )
				.updateDataSetHandle( setDesign, setHandle, false );

		save( );
		assertTrue( compareTextFile( "OdaDataSetConvertResultSetsTest_3_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Updates a oda result set definition.
	 * 
	 * @param param
	 */

	private void updateResultSetColumnAndHint( OdaResultSetColumnHandle column,
			ColumnHintHandle hint ) throws SemanticException
	{
		column.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL );
		hint.setDisplayName( "new display name for column 1" ); //$NON-NLS-1$
		hint.setHelpText( "new help text for column 1" ); //$NON-NLS-1$
		hint.setFormat( "new format " ); //$NON-NLS-1$
		hint.setAnalysis( DesignChoiceConstants.ANALYSIS_TYPE_MEASURE );
		hint.setOnColumnLayout( true );
	}

	/**
	 * Updates a oda result set definition.
	 * 
	 * @param param
	 */

	private void updateResultSetDefinition1( ResultSetDefinition setDefn )
	{
		List columns = setDefn.getResultSetColumns( )
				.getResultColumnDefinitions( );

		ColumnDefinition column1 = (ColumnDefinition) columns.get( 0 );
		ColumnDefinition column2 = (ColumnDefinition) columns.get( 1 );

		DataElementAttributes dataAttrs = column1.getAttributes( );
		DataElementUIHints dataUIHints = dataAttrs.getUiHints( );
		dataUIHints.setDisplayName( "new display name for column 1" ); //$NON-NLS-1$

		OutputElementAttributes usageHints = column1.getUsageHints( );
		usageHints.setHelpText( "new help text for column 1" ); //$NON-NLS-1$

		usageHints.getFormattingHints( ).setDisplayFormat(
				"new format for column 1" ); //$NON-NLS-1$

		AxisAttributes axisAttrs = DesignFactory.eINSTANCE.createAxisAttributes( );
		axisAttrs.setAxisType( AxisType.DIMENSION_MEMBER_LITERAL );
		axisAttrs.setOnColumnLayout( false );
		column1.setMultiDimensionAttributes( axisAttrs );
		
		// new display name and help text, etc.

		dataUIHints = DesignFactory.eINSTANCE.createDataElementUIHints( );
		dataUIHints.setDisplayName( "new display name for column 2" ); //$NON-NLS-1$
		dataAttrs = column2.getAttributes( );
		dataAttrs.setUiHints( dataUIHints );

		usageHints = DesignFactory.eINSTANCE.createOutputElementAttributes( );
		usageHints.setHelpText( "new help text for column 2" ); //$NON-NLS-1$

		ValueFormatHints format = DesignFactory.eINSTANCE
				.createValueFormatHints( );
		format.setDisplayFormat( "new format for column 2" ); //$NON-NLS-1$
		usageHints.setFormattingHints( format );
		column2.setUsageHints( usageHints );

	}

	/**
	 * Updates a ROM result set definition. To make sure that
	 * convertNativeTypeToROMDataType() will be called. So that can verify blob
	 * conversion.
	 * 
	 * @param param
	 */

	private void updateResultSetDefinition2( OdaDataSetHandle setHandle )
			throws SemanticException
	{
		Iterator iter1 = setHandle.resultSetIterator( );
		OdaResultSetColumnHandle column = (OdaResultSetColumnHandle) iter1
				.next( );

		column.setNativeDataType( new Integer( 10 ) );
	}
}
