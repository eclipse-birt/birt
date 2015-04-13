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

import org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter;
import org.eclipse.birt.report.model.adapter.oda.ModelOdaAdapter;
import org.eclipse.birt.report.model.adapter.oda.util.BaseTestCase;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDesignerStateHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;
import org.eclipse.datatools.connectivity.oda.design.DesignerStateContent;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;

/**
 * Test cases to test the function of ModelOdaAdapter.
 */

public class OdaDataSetAdapterTest extends BaseTestCase
{

	private final static String INPUT_FILE = "OdaDataSetConvertTest.xml"; //$NON-NLS-1$	
	private final static String GOLDEN_FILE = "OdaDataSetConvertTest_golden.xml"; //$NON-NLS-1$

	private final static String INPUT_FILE_WITH_EMPTY_PROPS = "OdaDataSetEmptyProps.xml"; //$NON-NLS-1$

	private final static String INPUT_FILE_WITH_LIB = "OdaDataSetEmptyProps_1.xml"; //$NON-NLS-1$

	private final static String GOLDEN_FILE_WITH_EMPTY_PROPS = "OdaDataSetEmptyProps_golden.xml"; //$NON-NLS-1$

	private final static String GOLDEN_FILE1_WITH_EMPTY_PROPS = "OdaDataSetEmptyProps_golden_1.xml"; //$NON-NLS-1$

	final static String DATA_SET_EXTENSIONID = "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet"; //$NON-NLS-1$
	final static String DATA_SOURCE_EXTENSIONID = "org.eclipse.birt.report.data.oda.jdbc"; //$NON-NLS-1$

	/**
	 * Test case: <br>
	 * To read a design file, uses adapter to create a data source design.
	 * Checks values of the created data source design.
	 * 
	 * @throws Exception
	 */

	public void testROMDataSetToODADataSet( ) throws Exception
	{
		openDesign( INPUT_FILE );
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		DataSetDesign setDesign = new ModelOdaAdapter( )
				.createDataSetDesign( setHandle );

		assertEquals( "myDataSet1", setDesign.getName( ) ); //$NON-NLS-1$
		assertEquals( DATA_SET_EXTENSIONID, setDesign
				.getOdaExtensionDataSetId( ) );

		assertEquals( "My Data Set One", setDesign.getDisplayName( ) ); //$NON-NLS-1$
		Properties props = setDesign.getPublicProperties( );
		//queryTimeout , OdaConnProfileStorePath, OdaConnProfileName.

		assertEquals( "30000", props.findProperty( "queryTimeOut" ).getValue( ) ); //$NON-NLS-1$ //$NON-NLS-2$

		props = setDesign.getPrivateProperties( );
		assertEquals( 1, props.getProperties( ).size( ) );

		assertEquals( "10000", props.findProperty( "queryTimeOut" ).getValue( ) ); //$NON-NLS-1$//$NON-NLS-2$

		DataSetParameters params = setDesign.getParameters( );
		ParameterDefinition paramDefn = (ParameterDefinition) params
				.getParameterDefinitions( ).get( 0 );
		DataElementAttributes dataAttrs = paramDefn.getAttributes( );
		//assertEquals( "name", dataAttrs.getName( ) ); //$NON-NLS-1$
		assertEquals( ElementNullability.NULLABLE, dataAttrs.getNullability( )
				.getValue( ) );
		assertEquals( 1, dataAttrs.getPosition( ) );

		// unset status

		assertEquals( 0, dataAttrs.getNativeDataTypeCode( ) );
		assertEquals( ParameterMode.IN, paramDefn.getInOutMode( ).getValue( ) );
		InputParameterAttributes inParamAttrs = paramDefn.getInputAttributes( );
		InputElementAttributes inElementAttrs = inParamAttrs
				.getElementAttributes( );
		assertTrue( inElementAttrs.isOptional( ) );
		assertEquals( "default value 1", inElementAttrs.getDefaultScalarValue( ) ); //$NON-NLS-1$
		assertNull(  setDesign.getResultSets( ) );
		
		// unset status

		assertEquals( 0, dataAttrs.getNativeDataTypeCode( ) );

		assertEquals( "userid", setDesign.getPrimaryResultSetName( ) ); //$NON-NLS-1$

		assertEquals( "select * from user", setDesign.getQueryText( ) ); //$NON-NLS-1$

		// verify properties about the data source design

		DataSourceDesign sourceDesign = setDesign.getDataSourceDesign( );
		assertNotNull( sourceDesign );

		assertEquals( "myDataSource1", sourceDesign.getName( ) ); //$NON-NLS-1$
		assertEquals( DATA_SOURCE_EXTENSIONID, sourceDesign.getOdaExtensionId( ) );

		assertEquals( "My Data Source One", sourceDesign.getDisplayName( ) ); //$NON-NLS-1$
		props = sourceDesign.getPublicProperties( );

		assertEquals( "com.mysql.jdbc.Driver", props //$NON-NLS-1$
				.findProperty( "odaDriverClass" ).getValue( ) ); //$NON-NLS-1$
		assertEquals( "jdbc:mysql://localhost:3306/birt", props //$NON-NLS-1$
				.findProperty( "odaURL" ).getValue( ) ); //$NON-NLS-1$
		assertNull( props.findProperty( "odaUser" ).getValue( ) ); //$NON-NLS-1$
		assertNull( props.findProperty( "odaPassword" ) //$NON-NLS-1$ 
				.getValue( ) );

		props = sourceDesign.getPrivateProperties( );
		assertEquals( 2, props.getProperties( ).size( ) );

		assertEquals( "User", props.findProperty( "odaUser" ).getValue( ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "Password", props.findProperty( "odaPassword" ) //$NON-NLS-1$ //$NON-NLS-2$
				.getValue( ) );
	}

	/**
	 * Test case: <br>
	 * 
	 * <ul>
	 * <li> Create a data source design, uses the adapter to create a data
	 * source handle. Saves the new datasource handle to the design file.
	 * <li>No ActivityStack action should be invovled.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testODADataSetToROMDataSet( ) throws Exception
	{
		DataSetDesign setDesign = createDataSetDesign( );

		createDesign( );

		OdaDataSetHandle setHandle = new ModelOdaAdapter( )
				.createDataSetHandle( setDesign, designHandle );
		assertFalse( designHandle.getCommandStack( ).canUndo( ) );
		assertFalse( designHandle.getCommandStack( ).canRedo( ) );

		designHandle.getDataSets( ).add( setHandle );
		designHandle.getDataSources( ).add(
				new ModelOdaAdapter( ).createDataSourceHandle( setDesign
						.getDataSourceDesign( ), designHandle ) );

		assertNotNull( setHandle.getDataSource( ) );
		assertEquals( "myDataSource1", setHandle.getDataSource( ).getName( ) ); //$NON-NLS-1$
		
		verifyODADataSetToROMDataSet();
		/*save( );

		assertTrue( compareTextFile( GOLDEN_FILE ) );*/
	}

	private void verifyODADataSetToROMDataSet() throws Exception {
		saveAndOpenDesign();
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet( "myDataSet1" );
		assertNotNull( setHandle );
		assertEquals( "data set display name", setHandle.getDisplayName() );
		assertEquals( DATA_SET_EXTENSIONID, setHandle.getProperty( "extensionID" ) );
		
		assertEquals( "new public query time out", setHandle.getProperty("queryTimeOut") );
		assertEquals( "new private query time out", setHandle.getPrivateDriverProperty( "queryTimeOut" ) );
		
		List<Object> params = ( List<Object> ) setHandle.getProperty("parameters");
		OdaDataSetParameter param = ( OdaDataSetParameter ) params.get(0);
		assertEquals( "param1", param.getNativeName() );
		assertFalse( param.allowNull() );
		assertEquals( Integer.valueOf( 2 ), param.getPosition() );
		assertEquals( Integer.valueOf( 1 ), param.getNativeDataType() );
		assertEquals( true, param.isInput() );
		assertEquals( true, param.isOutput() );
		assertEquals( "default param value", param.getDefaultValue() );
		
		List<Object> resultSets = ( List<Object> ) setHandle.getProperty( "resultSet" );
		OdaResultSetColumn column = ( OdaResultSetColumn ) resultSets.get(0);
		assertEquals( "column1", column.getNativeName() );
		assertEquals( Integer.valueOf(2), column.getPosition() );
		assertEquals( Integer.valueOf(3), column.getNativeDataType() );
		
		assertEquals("resultset1", setHandle.getResultSetName() );
		assertEquals("new query text", setHandle.getQueryText() );
		
		
	}

	/**
	 * Test case: <br>
	 * 
	 * <ul>
	 * <li>Have a data source design and a data source handle, copied all
	 * values from the ODA element to ROM element.
	 * <li>ActivityStack action should be invovled as a transaction.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testUpdateROMDataSetWithODADataSet( ) throws Exception
	{
		openDesign( INPUT_FILE_WITH_EMPTY_PROPS );

		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$
		DataSetDesign setDesign = createDataSetDesign( );

		new ModelOdaAdapter( ).updateDataSetHandle( setDesign, setHandle,
				true );
		
		String tmpFile2 = saveTempFile();

		assertTrue( designHandle.getCommandStack( ).canUndo( ) );
		assertFalse( designHandle.getCommandStack( ).canRedo( ) );

		designHandle.getCommandStack( ).undo( );

		assertFalse( designHandle.getCommandStack( ).canUndo( ) );
		assertTrue( designHandle.getCommandStack( ).canRedo( ) );

		/*save( );
		assertTrue( compareTextFile( GOLDEN_FILE_WITH_EMPTY_PROPS ) );*/
		verifyDataSourceAndDataSetWithEmptyProp();
		

		designHandle.getCommandStack( ).redo( );

		save( );
		verifyODADataSetToROMDataSet();
		//assertTrue( compareTextFile( GOLDEN_FILE1_WITH_EMPTY_PROPS ) );

		openDesign( INPUT_FILE_WITH_LIB );

		// the source is in the library, so no change.

		setHandle = (OdaDataSetHandle) designHandle.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		new ModelOdaAdapter( ).updateDataSetHandle( setDesign, setHandle,
				false );

		assertEquals( "Library Data Source One", setHandle.getDataSource( ) //$NON-NLS-1$
				.getDisplayName( ) );
	}

	private void verifyDataSourceAndDataSetWithEmptyProp() {
		OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) designHandle.findDataSource( "myDataSource1" );
		assertNotNull( sourceHandle );
		assertNull( sourceHandle.getProperty("privateDriverProperties") );
		assertNull( sourceHandle.getProperty("userProperties") );
		
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet("myDataSet1");
		assertNotNull( setHandle );
		assertNull( setHandle.getProperty("privateDriverProperties") );
		assertNull( setHandle.getProperty("userProperties") );
	}

	/**
	 * Creates a new <code>DataSetDesign</code>.
	 * 
	 * @return an object of <code>DataSetDesign</code>.
	 */

	static DataSetDesign createDataSetDesign( )
	{
		DataSetDesign setDesign = DesignFactory.eINSTANCE.createDataSetDesign( );
		setDesign.setName( "myDataSet1" ); //$NON-NLS-1$
		setDesign.setDisplayName( "data set display name" ); //$NON-NLS-1$
		setDesign.setOdaExtensionDataSetId( DATA_SET_EXTENSIONID );

		Properties props = DesignFactory.eINSTANCE.createProperties( );
		props.setProperty( "queryTimeOut", "new public query time out" ); //$NON-NLS-1$//$NON-NLS-2$
		setDesign.setPublicProperties( props );

		props = DesignFactory.eINSTANCE.createProperties( );
		props.setProperty( "queryTimeOut", "new private query time out" ); //$NON-NLS-1$ //$NON-NLS-2$
		setDesign.setPrivateProperties( props );

		DataSetParameters params = DesignFactory.eINSTANCE
				.createDataSetParameters( );
		ParameterDefinition paramDefn = DesignFactory.eINSTANCE
				.createParameterDefinition( );
		DataElementAttributes dataAttrs = DesignFactory.eINSTANCE
				.createDataElementAttributes( );
		dataAttrs.setName( "param1" ); //$NON-NLS-1$
		dataAttrs.setNullability( ElementNullability
				.get( ElementNullability.NOT_NULLABLE ) );
		dataAttrs.setPosition( 2 );
		dataAttrs.setNativeDataTypeCode( 1 );
		paramDefn.setInOutMode( ParameterMode.get( ParameterMode.IN_OUT ) );
		paramDefn.setAttributes( dataAttrs );

		InputParameterAttributes inParamAttrs = DesignFactory.eINSTANCE
				.createInputParameterAttributes( );
		InputElementAttributes inputElementAttrs = DesignFactory.eINSTANCE
				.createInputElementAttributes( );
		inputElementAttrs.setOptional( false );
		inputElementAttrs.setDefaultScalarValue( "default param value" ); //$NON-NLS-1$
		inParamAttrs.setElementAttributes( inputElementAttrs );
		paramDefn.setInputAttributes( inParamAttrs );

		params.getParameterDefinitions( ).add( paramDefn );
		setDesign.setParameters( params );

		ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE
				.createResultSetDefinition( );
		ResultSetColumns setColumns = DesignFactory.eINSTANCE
				.createResultSetColumns( );
		ColumnDefinition columnDefn = DesignFactory.eINSTANCE
				.createColumnDefinition( );
		dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes( );
		dataAttrs.setName( "column1" ); //$NON-NLS-1$
		dataAttrs.setPosition( 2 );
		dataAttrs.setNativeDataTypeCode( 3 );
		columnDefn.setAttributes( dataAttrs );
		setColumns.getResultColumnDefinitions( ).add( columnDefn );
		resultSetDefn.setResultSetColumns( setColumns );
		setDesign.setPrimaryResultSet( resultSetDefn );
		setDesign.setPrimaryResultSetName( "resultset1" ); //$NON-NLS-1$

		setDesign.setQueryText( "new query text" ); //$NON-NLS-1$

		// create the corresponding data source design

		setDesign.setDataSourceDesign( createDataSourceDesign( ) );
		return setDesign;
	}

	/**
	 * Creates a new <code>DataSourceDesign</code>.
	 * 
	 * @return an object of <code>DataSourceDesign</code>.
	 */

	private static DataSourceDesign createDataSourceDesign( )
	{
		DataSourceDesign sourceDesign = DesignFactory.eINSTANCE
				.createDataSourceDesign( );
		sourceDesign.setName( "myDataSource1" ); //$NON-NLS-1$
		sourceDesign.setDisplayName( "data source display name" ); //$NON-NLS-1$
		sourceDesign.setOdaExtensionId( DATA_SOURCE_EXTENSIONID );

		Properties props = DesignFactory.eINSTANCE.createProperties( );
		props.setProperty( "odaDriverClass", "new drivers" ); //$NON-NLS-1$//$NON-NLS-2$
		props.setProperty( "odaURL", "jdbc:sqlserver://localhost" ); //$NON-NLS-1$//$NON-NLS-2$
		props.setProperty( "odaUser", "new user" ); //$NON-NLS-1$ //$NON-NLS-2$
		sourceDesign.setPublicProperties( props );

		props = DesignFactory.eINSTANCE.createProperties( );
		props.setProperty( "odaDriverClass", "new drivers" ); //$NON-NLS-1$ //$NON-NLS-2$
		props.setProperty( "odaPassword", "new password" ); //$NON-NLS-1$ //$NON-NLS-2$
		sourceDesign.setPrivateProperties( props );

		return sourceDesign;
	}

	/**
	 * Tests functions to convert Designer State on a set Design.
	 * 
	 * @throws Exception
	 */

	public void testDesignerState( ) throws Exception
	{
		openDesign( "OdaDataSetConvertDesignerState.xml" ); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		DesignerState designerState = new ModelOdaAdapter( )
				.newOdaDesignerState( setHandle );

		assertEquals( "1.1", designerState.getVersion( ) ); //$NON-NLS-1$
		DesignerStateContent stateContent = designerState.getStateContent( );

		assertEquals( "content as string", stateContent //$NON-NLS-1$
				.getStateContentAsString( ) );
		assertNull( stateContent.getStateContentAsBlob( ) );

		designerState.setVersion( "2.0" ); //$NON-NLS-1$

		byte[] data = {0x34, 0x32, 0x33, 0x44, 0x52};
		stateContent.setStateContentAsBlob( data );

		new ModelOdaAdapter( )
				.updateROMDesignerState( designerState, setHandle );

		save( ); 
		compareTextFile( "OdaDataSetDesignerState_golden.xml"); //$NON-NLS-1$

		setHandle.setDesignerState( null );
		assertNull( setHandle.getDesignerState( ) );

		new ModelOdaAdapter( )
				.updateROMDesignerState( designerState, setHandle );

		OdaDesignerStateHandle romDesignerState = setHandle.getDesignerState( );
		assertNotNull( romDesignerState );

		assertEquals( "2.0", romDesignerState.getVersion( ) ); //$NON-NLS-1$
	}

	/**
	 * Tests that the data source properties are not localized when creating a
	 * data set using the data source.
	 * 
	 * @throws Exception
	 */

	public void testCreateDataSetReferDataSource( ) throws Exception
	{
		openDesign( "CreateDataSetReferDataSource.xml" ); //$NON-NLS-1$
		designHandle.includeLibrary( "Library_2.xml", "lib" ); //$NON-NLS-1$ //$NON-NLS-2$
		DataSourceHandle parent = designHandle
				.getLibrary( "lib" ).findDataSource( "Data Source" ); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull( parent );
		IModelOdaAdapter adapter = new ModelOdaAdapter( );

		OdaDataSourceHandle dataSource = (OdaDataSourceHandle) designHandle
				.getElementFactory( ).newElementFrom( parent, "testSource" ); //$NON-NLS-1$ 
		designHandle.getDataSources( ).add( dataSource );
		assertEquals( designHandle, dataSource.getRoot( ) );
		adapter.createDataSourceDesign( dataSource );

		OdaDataSetHandle dataSet = designHandle
				.getElementFactory( )
				.newOdaDataSet(
						"testDataSet", "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet" ); //$NON-NLS-1$ //$NON-NLS-2$
		dataSet.setDataSource( "testSource" ); //$NON-NLS-1$
		designHandle.getDataSets( ).add( dataSet );
		assertEquals( designHandle, dataSet.getRoot( ) );

		DataSetDesign dataSetDesign = adapter.createDataSetDesign( dataSet );
		dataSetDesign.setQueryText( "new query text" ); //$NON-NLS-1$

		// update data set handle

		adapter.updateDataSetHandle( dataSetDesign, dataSet, false );

		/* save( ); 
		assertTrue( compareTextFile( "CreateDataSetReferDataSource_golden.xml" ) ); //$NON-NLS-1$*/
		saveAndOpenDesign();
		OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) designHandle.findDataSource("testSource");
		assertNotNull(sourceHandle);
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet( "testDataSet" );
		assertNotNull(setHandle);
		assertEquals( "new query text", setHandle.getQueryText() );
	}

}
