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

import org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter;
import org.eclipse.birt.report.model.adapter.oda.ModelOdaAdapter;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.ModelFactory;
import org.eclipse.birt.report.model.adapter.oda.util.BaseTestCase;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Test cases to convert Oda data set parameters and ROM data set parameter and
 * linked report parameters.
 * 
 */

public class DataSetParameterAdapterTest extends BaseTestCase
{

	/**
	 * Test parameter count.
	 * 
	 * @param setHandle
	 *            oda dataset handle.
	 */

	private void testParametersCount( OdaDataSetHandle setHandle,
			int expectValue )
	{
		int count = 0;
		Iterator iterator = setHandle.parametersIterator( );
		while ( iterator.hasNext( ) )
		{
			iterator.next( );
			++count;
		}
		assertEquals( expectValue, count );
	}

	/**
	 * Test New rule of merge parameter between <code>DataSetDesign</code> and
	 * <code>DataSetHandle</code>
	 * 
	 * @throws Exception
	 */

	public void testMergeParamDefnFromDesignToHandle( ) throws Exception
	{

		openDesign( "DataSetMergeTest.xml" ); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "Data Set" ); //$NON-NLS-1$

		DataSetDesign setDesign = new ModelOdaAdapter( )
				.createDataSetDesign( setHandle );
		setHandle.setProperty( OdaDataSetHandle.PARAMETERS_PROP, null );

		// Parameters defined in DataSetDesign , but not in DataSetHandle

		IModelOdaAdapter adapter = new ModelOdaAdapter( );
		adapter.updateDataSetHandle( setDesign, setHandle, false );
		testParametersCount( setHandle, 3 );

		// Parameter 2 defined in DataSetHandle , but not in DataSetDesign.

		setHandle.setDesignerValues( null );
		PropertyHandle propHandle = setHandle
				.getPropertyHandle( OdaDataSetHandle.PARAMETERS_PROP );
		OdaDataSetParameter parameter = (OdaDataSetParameter) propHandle.getAt(
				2 ).getStructure( ).copy( );
		propHandle.removeItem( 2 );

		DataSetDesign setDesign2 = new ModelOdaAdapter( )
				.createDataSetDesign( setHandle );
		propHandle.addItem( parameter );

		adapter.updateDataSetHandle( setDesign2, setHandle, false );
		testParametersCount( setHandle, 3 );

	}

	/**
	 * Test case:
	 * 
	 * <ul>
	 * <li> when convert default value, string type should distinguish
	 * expression and literal.
	 * <li> When convert oda data set parameter to ROM data set parameter.
	 * Default values are kept. <br>
	 * And the data type is kept even the native data type is unknown.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testDataTypeConversion( ) throws Exception
	{
		// create oda set handle.

		openDesign( "DataSetParamConvertTest_1.xml" ); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		DataSetDesign setDesign = new ModelOdaAdapter( )
				.createDataSetDesign( setHandle );

		DesignValues values = ModelFactory.eINSTANCE.createDesignValues( );
		values.setDataSetParameters( (DataSetParameters) EcoreUtil
				.copy( setDesign.getParameters( ) ) );

		saveDesignValuesToFile( values ); 
		assertTrue( compareTextFile( "DataSetParamConvertTest_golden_1.xml") ); //$NON-NLS-1$

		new ModelOdaAdapter( )
				.updateDataSetHandle( setDesign, setHandle, false );

		save( ); 
		assertTrue( compareTextFile( "DataSetParamConvertTest_golden_2.xml") ); //$NON-NLS-1$
	}

	/**
	 * Test case:
	 * 
	 * <ul>
	 * <li> Convert values from ODA parameter definition to ROM data set
	 * parameter. Focus on default values.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testValuesConversion( ) throws Exception
	{
		// create oda set handle.

		openDesign( "DataSetParamConvertTest_1.xml" ); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		DataSetDesign setDesign = new ModelOdaAdapter( )
				.createDataSetDesign( setHandle );

		// oda data set design changed, update ROM values. still keep report
		// parameter link.

		DataSetParameters params = setDesign.getParameters( );
		ParameterDefinition param = (ParameterDefinition) params
				.getParameterDefinitions( ).get( 0 );
		updateParameterDefinition1( param );

		new ModelOdaAdapter( )
				.updateDataSetHandle( setDesign, setHandle, false );

		save( ); 

		assertTrue( compareTextFile( "DataSetParamConvertTest_golden_3.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Updates a oda parameter definition. Keep the direction.
	 * 
	 * @param param
	 */

	private void updateParameterDefinition1( ParameterDefinition param )
	{
		DataElementAttributes dataAttrs = param.getAttributes( );
		dataAttrs.setNullability( ElementNullability
				.get( ElementNullability.NOT_NULLABLE ) );

		InputParameterAttributes paramAttrs = param.getInputAttributes( );
		InputElementAttributes elementAttrs = paramAttrs.getElementAttributes( );

		elementAttrs
				.setDefaultScalarValue( "new default value for report param 1" ); //$NON-NLS-1$
		elementAttrs.setOptional( true );
	}
}
