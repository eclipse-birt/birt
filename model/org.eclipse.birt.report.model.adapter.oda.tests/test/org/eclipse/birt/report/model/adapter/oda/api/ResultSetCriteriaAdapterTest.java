/*******************************************************************************
 * Copyright (c) 2004, 2005, 2006, 2007, 2008, 2009 Actuate Corporation.
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

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.model.adapter.oda.impl.ResultSetCriteriaAdapter;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.ModelFactory;
import org.eclipse.birt.report.model.adapter.oda.util.BaseTestCase;
import org.eclipse.birt.report.model.api.DynamicFilterParameterHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.SortHintHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.datatools.connectivity.oda.design.AndExpression;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.CompositeFilterExpression;
import org.eclipse.datatools.connectivity.oda.design.CustomFilterExpression;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.DynamicFilterExpression;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.ExpressionArguments;
import org.eclipse.datatools.connectivity.oda.design.ExpressionParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.FilterExpression;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputPromptControlStyle;
import org.eclipse.datatools.connectivity.oda.design.NullOrderingType;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetCriteria;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.SortDirectionType;
import org.eclipse.datatools.connectivity.oda.design.SortKey;
import org.eclipse.datatools.connectivity.oda.design.SortSpecification;
import org.eclipse.datatools.connectivity.oda.design.util.DesignUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Test cases for FilterAdapter
 */

public class ResultSetCriteriaAdapterTest extends BaseTestCase
{

	private final static String INPUT_FILE_ODA = "OdaDataSetCovertFilterExpression.xml"; //$NON-NLS-1$		
	private final static String GOLDEN_FILE_ODA = "OdaDataSetCovertFilterExpression_golden.xml"; //$NON-NLS-1$

	private final static String INPUT_FILE_REPORT = "OdaDataSetCovertFilterCondition.xml"; //$NON-NLS-1$
	private final static String GOLDEN_FILE_REPORT = "OdaDataSetCovertFilterCondition_golden.xml"; //$NON-NLS-1$

	private final static String[] COLUMNS = {"name", "date", "id" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$		
	};

	private final static int[] TYPES = {java.sql.Types.CHAR,
			java.sql.Types.DATE, java.sql.Types.INTEGER,};

	private final static String[] ROM_TYPES = {
			DesignChoiceConstants.PARAM_TYPE_STRING,
			DesignChoiceConstants.PARAM_TYPE_DATE,
			DesignChoiceConstants.PARAM_TYPE_INTEGER,};

	/**
	 * Creates a blank oda data set design
	 */

	private DataSetDesign createDataSetDesign( )
	{
		DataSetDesign setDesign = DesignFactory.eINSTANCE.createDataSetDesign( );
		setDesign
				.setOdaExtensionDataSetId( "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet" ); //$NON-NLS-1$
		DataSourceDesign dataSource = DesignFactory.eINSTANCE
				.createDataSourceDesign( );
		dataSource
				.setOdaExtensionDataSourceId( "org.eclipse.birt.report.data.oda.jdbc" ); //$NON-NLS-1$
		setDesign.setDataSourceDesign( dataSource );
		ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE
				.createResultSetDefinition( );

		// no exception in conversion; go ahead and assign to specified
		// dataSetDesign
		setDesign.setPrimaryResultSet( resultSetDefn );
		return setDesign;
	}

	/**
	 * Creates an oda data set design for test
	 * 
	 * @param containsFilter
	 *            specify if the set design contains filter expressions
	 */
	private DataSetDesign createTestDataSetDesign( boolean containsFilter )
	{
		DataSetDesign setDesign = createDataSetDesign( );
		ResultSetDefinition resultSetDefn = setDesign.getPrimaryResultSet( );
		updateResultSetCriteria( resultSetDefn );
		if ( containsFilter )
		{
			resultSetDefn.getCriteria( ).setFilterSpecification(
					createFilterSpec( resultSetDefn ) );
		}
		return setDesign;
	}

	/**
	 * Updates result set criteria for test
	 */

	private void updateResultSetCriteria( ResultSetDefinition resultSetDefn )
	{
		resultSetDefn.setCriteria( DesignFactory.eINSTANCE
				.createResultSetCriteria( ) );
		ResultSetColumns resultSetColumns = DesignFactory.eINSTANCE
				.createResultSetColumns( );
		for ( int i = 0; i < COLUMNS.length; i++ )
		{
			ColumnDefinition column = DesignFactory.eINSTANCE
					.createColumnDefinition( );
			column.setAttributes( DesignFactory.eINSTANCE
					.createDataElementAttributes( ) );
			column.getAttributes( ).setName( COLUMNS[i] );
			resultSetColumns.getResultColumnDefinitions( ).add( column );
		}
		resultSetDefn.setResultSetColumns( resultSetColumns );
	}

	/**
	 * Creates filter expressions for test
	 */
	private FilterExpression createFilterSpec( ResultSetDefinition resultSetDefn )
	{
		// hard-code filter expression tree for testing purpose

		// create a static filter for testing; expects to be ignored by BIRT
		// ModelOdaAdapter
		CustomFilterExpression customStaticExpr = DesignFactory.eINSTANCE
				.createCustomFilterExpression( );
		customStaticExpr
				.setDeclaringExtensionId( "org.eclipse.datatools.connectivity.oda.consumer.testdriver.dynamicResultSetExtension" ); //$NON-NLS-1$
		customStaticExpr.setId( "IdentityEq" ); //$NON-NLS-1$		
		customStaticExpr.setContextVariable( DesignUtil.createFilterVariable(
				resultSetDefn.getResultSetColumns( ), 0 ) );

		// create different types of dynamic filters

		// a required dynamic filter with no expression argument: expects to be
		// ignored
		DynamicFilterExpression dynamicFilter1 = DesignFactory.eINSTANCE
				.createDynamicFilterExpression( );
		dynamicFilter1.setContextVariable( DesignUtil.createFilterVariable(
				resultSetDefn.getResultSetColumns( ), 1 ) );

		// an optional dynamic filter with expression definition containing
		// static ExpressionParameterDefinition
		DynamicFilterExpression dynamicFilter2 = DesignFactory.eINSTANCE
				.createDynamicFilterExpression( );
		dynamicFilter2.setContextVariable( DesignUtil.createFilterVariable(
				resultSetDefn.getResultSetColumns( ), 1 ) );
		dynamicFilter2.setIsOptional( true );
		dynamicFilter2.setContextArguments( DesignFactory.eINSTANCE
				.createExpressionArguments( ) );
		ExpressionArguments exprArgs2 = dynamicFilter2.getContextArguments( );
		exprArgs2.addDynamicParameter( createParameterDefinition( 1, false ) );
		exprArgs2.addStaticParameter( new Integer( 100 ) ); // Column1 has
		// integer type
		exprArgs2.getExpressionParameterDefinitions( ).get( 0 ).addStaticValue(
				new Integer( 200 ) );

		// a required dynamic filter with expression definition containing
		// dynamic input ExpressionParameterDefinition;
		// a dynamic parameter takes precedence over a static parameter
		DynamicFilterExpression dynamicFilter3 = DesignFactory.eINSTANCE
				.createDynamicFilterExpression( );
		dynamicFilter3.setContextArguments( DesignFactory.eINSTANCE
				.createExpressionArguments( ) );
		dynamicFilter3.setIsOptional( false );
		ExpressionArguments exprArgs4 = dynamicFilter3.getContextArguments( );
		exprArgs4.addDynamicParameter( createParameterDefinition( 2, false ) );
		exprArgs4.addDynamicParameter( createParameterDefinition( 3, true ) );
		// adds static value for testing, expects to be ignored by
		// ExpressionParameterDefinition#hasEffectiveStaticValues()
		exprArgs4.getExpressionParameterDefinitions( ).get( 0 ).addStaticValue(
				"dummy" ); //$NON-NLS-1$

		// add individual filter expressions to the root

		AndExpression filterExprRoot = DesignFactory.eINSTANCE
				.createAndExpression( );
		filterExprRoot.add( customStaticExpr );
		filterExprRoot.add( dynamicFilter1 );

		AndExpression andExpr = DesignFactory.eINSTANCE.createAndExpression( );
		andExpr.add( dynamicFilter2 );
		andExpr.add( dynamicFilter3 );
		filterExprRoot.add( andExpr );

		return filterExprRoot;
	}

	/**
	 * Creates filter parameters for test
	 */
	private ParameterDefinition createParameterDefinition( int position,
			boolean containDefaultValue )
	{
		ParameterDefinition paramDefn = DesignFactory.eINSTANCE
				.createParameterDefinition( );

		paramDefn.setAttributes( DesignFactory.eINSTANCE
				.createDataElementAttributes( ) );
		DataElementAttributes paramAttrs = paramDefn.getAttributes( );
		paramAttrs.setName( COLUMNS[position - 1] );
		paramAttrs.setPosition( position );
		paramAttrs.setNativeDataTypeCode( TYPES[position - 1] );
		paramAttrs.setNullability( ElementNullability.NULLABLE_LITERAL );

		if ( containDefaultValue )
		{
			for ( int i = 1; i <= 3; i++ )
			{
				paramDefn.addDefaultValue( "value" + i ); //$NON-NLS-1$
			}
			InputElementAttributes inputAttrs = paramDefn.getInputAttributes( )
					.getElementAttributes( );
			inputAttrs
					.setUiPromptStyle( InputPromptControlStyle.SELECTABLE_LIST_WITH_TEXT_FIELD_LITERAL );
			inputAttrs.getUiHints( ).setAutoSuggestThreshold( 3 );

		}
		return paramDefn;
	}

	/**
	 * Test for converting from oda to report
	 * 
	 * @throws Exception
	 */
	public void testODAToReport( ) throws Exception
	{
		openDesign( INPUT_FILE_ODA );
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$
		ResultSetCriteriaAdapter adapter = new ResultSetCriteriaAdapter(
				setHandle, createTestDataSetDesign( true ) );
		adapter.updateROMSortAndFilter( );

		List filters = setHandle.getListProperty( IDataSetModel.FILTER_PROP );

		assertEquals( 4, filters.size( ) );

		// first valid filter expression: custom filter
		FilterCondition filter = (FilterCondition) filters.get( 0 );
		assertEquals( COLUMNS[0], filter.getExpr( ) );
		assertEquals(
				"org.eclipse.datatools.connectivity.oda.consumer.testdriver.dynamicResultSetExtension", //$NON-NLS-1$
				filter.getExtensionName( ) );
		assertEquals( "IdentityEq", filter.getExtensionExprId( ) ); //$NON-NLS-1$
		assertTrue( filter.pushDown( ) );

		for ( int i = 0; i < COLUMNS.length; i++ )
		{
			checkDynamicFilter( setHandle, (FilterCondition) filters
					.get( i + 1 ), i );
		}
		// second valid filter expression: dynamic filter
		filter = (FilterCondition) filters.get( 1 );
		assertTrue( filter.isOptional( ) );

		// third valid filter expression: separated dynamic filter
		filter = (FilterCondition) filters.get( 2 );
		assertFalse( filter.isOptional( ) );

		// forth valid filter expression: separated dynamic filter with default
		// values
		filter = (FilterCondition) filters.get( 3 );
		DynamicFilterParameterHandle parameter = (DynamicFilterParameterHandle) designHandle
				.findParameter( filter.getDynamicFilterParameter( ) );
		assertFalse( filter.isOptional( ) );
		assertTrue( parameter.isRequired( ) );
		List<Expression> defaultValues = parameter.getDefaultValueList( );
		assertNotNull( defaultValues );
		assertEquals( 3, defaultValues.size( ) );
		for ( int i = 0; i < 3; i++ )
		{
			assertEquals( "value" + ( i + 1 ), defaultValues.get( i ) //$NON-NLS-1$
					.getExpression( ) );
		}

		save( );
		assertTrue( compareTextFile( GOLDEN_FILE_ODA ) );

		adapter = new ResultSetCriteriaAdapter( setHandle,
				createTestDataSetDesign( false ) );
		// Doing nothing expected
		adapter.updateROMSortAndFilter( );

		save( );
		assertTrue( compareTextFile( GOLDEN_FILE_ODA ) );
	}

	private void checkDynamicFilter( OdaDataSetHandle setHandle,
			FilterCondition filterCondition, int position )
	{
		assertEquals( ExpressionUtil
				.createDataSetRowExpression( COLUMNS[position] ),
				filterCondition.getExpr( ) );
		assertNotNull( filterCondition.getDynamicFilterParameter( ) );
		DynamicFilterParameterHandle parameter = (DynamicFilterParameterHandle) designHandle
				.findParameter( filterCondition.getDynamicFilterParameter( ) );
		assertEquals( setHandle.getName( ), parameter.getDataSetName( ) );
		assertEquals( COLUMNS[position], parameter.getColumn( ) );
		assertEquals( TYPES[position], parameter.getNativeDataType( ) );
		assertEquals( ROM_TYPES[position], parameter.getDataType( ) );
	}

	/**
	 * Test for converting from report to oda
	 * 
	 * @throws Exception
	 */

	public void testReportToOda( ) throws Exception
	{
		openDesign( INPUT_FILE_REPORT );
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$
		DataSetDesign setDesign = createDataSetDesign( );
		ResultSetCriteriaAdapter adapter = new ResultSetCriteriaAdapter(
				setHandle, setDesign );
		adapter.updateODAResultSetCriteria( );

		ResultSetDefinition resultSet = setDesign.getPrimaryResultSet( );
		FilterExpression filterExpr = resultSet.getCriteria( )
				.getFilterSpecification( );
		assertTrue( filterExpr instanceof CompositeFilterExpression );
		assertEquals( 3, ( (CompositeFilterExpression) filterExpr )
				.getChildren( ).size( ) );
		CompositeFilterExpression compoisteFilterExpr = (CompositeFilterExpression) filterExpr;
		assertTrue( compoisteFilterExpr.getChildren( ).get( 0 ) instanceof CustomFilterExpression );
		assertTrue( compoisteFilterExpr.getChildren( ).get( 1 ) instanceof DynamicFilterExpression );
		assertTrue( compoisteFilterExpr.getChildren( ).get( 2 ) instanceof DynamicFilterExpression );

		// first filter: custom filter
		CustomFilterExpression customFilterExpr = (CustomFilterExpression) compoisteFilterExpr
				.getChildren( ).get( 0 );
		assertEquals( COLUMNS[0], customFilterExpr.getContextVariable( )
				.getIdentifier( ) );
		assertEquals(
				"org.eclipse.datatools.connectivity.oda.consumer.testdriver.dynamicResultSetExtension", //$NON-NLS-1$
				customFilterExpr.getDeclaringExtensionId( ) );
		assertEquals( "IdentityEq", customFilterExpr.getId( ) ); //$NON-NLS-1$

		// second filter: dynamic filter
		DynamicFilterExpression dynamicFilterExpr = (DynamicFilterExpression) compoisteFilterExpr
				.getChildren( ).get( 1 );
		DataElementAttributes attrs = dynamicFilterExpr.getContextArguments( )
				.getExpressionParameterDefinitions( ).get( 0 )
				.getDynamicInputParameter( ).getAttributes( );
		assertEquals( COLUMNS[1], attrs.getName( ) );
		assertEquals( TYPES[1], attrs.getNativeDataTypeCode( ) );
		assertTrue( dynamicFilterExpr.isOptional( ) );
		assertFalse( attrs.allowsNull( ) );

		// third filter: dynamic filter with default values
		dynamicFilterExpr = (DynamicFilterExpression) compoisteFilterExpr
				.getChildren( ).get( 2 );
		ExpressionParameterDefinition paramDefn = dynamicFilterExpr
				.getContextArguments( ).getExpressionParameterDefinitions( )
				.get( 0 );
		attrs = paramDefn.getDynamicInputParameter( ).getAttributes( );
		assertEquals( COLUMNS[2], attrs.getName( ) );
		assertEquals( TYPES[2], attrs.getNativeDataTypeCode( ) );
		assertFalse( dynamicFilterExpr.isOptional( ) );

		// assertTrue( paramDefn.getDynamicInputParameter( ).getAttributes(
		// ).allowsNull( ) );
		assertEquals( 3, paramDefn.getDynamicInputParameter( )
				.getDefaultValueCount( ) );
		for ( int i = 0; i < 3; i++ )
		{
			Object defualtValue = paramDefn.getDynamicInputParameter( )
					.getDefaultValues( ).getValues( ).get( i );
			assertEquals( "value" + ( i + 1 ), defualtValue ); //$NON-NLS-1$
		}

		DesignValues values = ModelFactory.eINSTANCE.createDesignValues( );
		values.setResultSets( setDesign.getResultSets( ) );
		saveDesignValuesToFile( values );

		saveOutputFile( GOLDEN_FILE_REPORT );
		assertTrue( compareTextFile( GOLDEN_FILE_REPORT ) );
	}

	/**
	 * Tests convert sort hint from report to oda.
	 * 
	 * @throws Exception
	 */
	public void testSortHintFromReportToOda( ) throws Exception
	{
		openDesign( "SortHintTest.xml" ); //$NON-NLS-1$

		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "MyDataSet" ); //$NON-NLS-1$
		DataSetDesign setDesign = createDataSetDesign( );
		ResultSetCriteriaAdapter adapter = new ResultSetCriteriaAdapter(
				setHandle, setDesign );
		adapter.updateODAResultSetCriteria( );

		ResultSetDefinition resultSet = setDesign.getPrimaryResultSet( );
		EList<SortKey> list = resultSet.getCriteria( ).getRowOrdering( )
				.getSortKeys( );

		assertEquals( 3, list.size( ) );

		SortKey key = list.get( 0 );
		assertEquals( "sortHint1", key.getColumnName( ) ); //$NON-NLS-1$
		assertEquals( 1, key.getColumnPosition( ) );
		assertEquals( SortDirectionType.DESCENDING, key.getSortDirection( ) );
		assertEquals( NullOrderingType.NULLS_FIRST, key.getNullValueOrdering( ) );
		assertTrue( key.isOptional( ) );

		key = list.get( 1 );
		assertEquals( "sortHint2", key.getColumnName( ) ); //$NON-NLS-1$
		assertEquals( 2, key.getColumnPosition( ) );
		assertEquals( SortDirectionType.ASCENDING, key.getSortDirection( ) );
		assertEquals( NullOrderingType.NULLS_FIRST, key.getNullValueOrdering( ) );
		assertFalse( key.isOptional( ) );

		key = list.get( 2 );
		assertEquals( "sortHint3", key.getColumnName( ) ); //$NON-NLS-1$
		assertEquals( 3, key.getColumnPosition( ) );
		assertEquals( SortDirectionType.DESCENDING, key.getSortDirection( ) );
		assertEquals( NullOrderingType.NULLS_LAST, key.getNullValueOrdering( ) );
		assertTrue( key.isOptional( ) );

		DesignValues values = ModelFactory.eINSTANCE.createDesignValues( );
		values.setResultSets( setDesign.getResultSets( ) );
		saveDesignValuesToFile( values );

		assertTrue( compareTextFile( "SortHintFromReportToOdaTest_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests convert sort hint from report to oda.
	 * 
	 * @throws Exception
	 */
	public void testEmptySortHintFromReportToOda( ) throws Exception
	{

		openDesign( "EmptySortHintTest.xml" ); //$NON-NLS-1$

		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "MyDataSet" ); //$NON-NLS-1$
		DataSetDesign setDesign = createDataSetDesign( );

		assertNull( setDesign.getPrimaryResultSet( ).getCriteria( )
				.getRowOrdering( ) );

		ResultSetCriteriaAdapter adapter = new ResultSetCriteriaAdapter(
				setHandle, setDesign );
		adapter.updateODAResultSetCriteria( );

		ResultSetDefinition resultSet = setDesign.getPrimaryResultSet( );

		// if an Oda data set has no BIRT sort hints, the Adapter should create
		// an empty SortSpecification.
		assertNotNull( resultSet.getCriteria( ).getRowOrdering( ) );
	}

	/**
	 * Tests convert sort hint from oda to report.
	 * 
	 * @throws Exception
	 */
	public void testSortHintFromOdaToReport( ) throws Exception
	{
		openDesign( "SortHintTest.xml" ); //$NON-NLS-1$

		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "MyDataSet" ); //$NON-NLS-1$
		DataSetDesign setDesign = createTestSortHintDataSetDesign( false );
		ResultSetCriteriaAdapter adapter = new ResultSetCriteriaAdapter(
				setHandle, setDesign );
		adapter.updateROMSortAndFilter( );

		Iterator iter = setHandle.sortHintsIterator( );

		SortHintHandle handle = (SortHintHandle) iter.next( );

		assertEquals( "1", handle.getColumnName( ) ); //$NON-NLS-1$
		assertEquals( 1, handle.getPosition( ) );
		assertEquals(
				DesignChoiceConstants.NULL_VALUE_ORDERING_TYPE_NULLISFIRST,
				handle.getNullValueOrdering( ) );
		assertEquals( DesignChoiceConstants.SORT_DIRECTION_ASC, handle
				.getDirection( ) );
		assertFalse( handle.isOptional( ) );

		handle = (SortHintHandle) iter.next( );

		assertEquals( "2", handle.getColumnName( ) ); //$NON-NLS-1$
		assertEquals( 2, handle.getPosition( ) );
		assertEquals(
				DesignChoiceConstants.NULL_VALUE_ORDERING_TYPE_NULLISLAST,
				handle.getNullValueOrdering( ) );
		assertEquals( DesignChoiceConstants.SORT_DIRECTION_DESC, handle
				.getDirection( ) );
		assertTrue( handle.isOptional( ) );

		save( );
		assertTrue( compareTextFile( "SortHintFromOdaToReportTest_golden.xml" ) ); //$NON-NLS-1$

		adapter = new ResultSetCriteriaAdapter( setHandle,
				createTestSortHintDataSetDesign( true ) );

		// Doing nothing expected
		adapter.updateROMSortAndFilter( );

		save( );
		assertTrue( compareTextFile( "SortHintFromOdaToReportTest_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Creates data set design with sort hint.
	 * 
	 * @param sortSpecIsNull
	 *            Indicates if the sort specification is null
	 * @return data set design.
	 */
	private DataSetDesign createTestSortHintDataSetDesign(
			boolean sortSpecIsNull )
	{
		DataSetDesign setDesign = createDataSetDesign( );

		if ( sortSpecIsNull )
			return setDesign;

		ResultSetDefinition resultSetDefn = setDesign.getPrimaryResultSet( );

		ResultSetCriteria criteria = resultSetDefn.getCriteria( );

		SortSpecification sortSpec = criteria.getRowOrdering( );

		if ( sortSpec == null )
		{
			sortSpec = DesignFactory.eINSTANCE.createSortSpecification( );
			criteria.setRowOrdering( sortSpec );
		}

		EList<SortKey> list = sortSpec.getSortKeys( );

		SortKey key = DesignFactory.eINSTANCE.createSortKey( );

		key.setColumnName( "1" ); //$NON-NLS-1$
		key.setColumnPosition( 1 );
		key.setNullValueOrdering( NullOrderingType.NULLS_FIRST );
		key.setOptional( false );
		key.setSortDirection( SortDirectionType.ASCENDING );

		list.add( key );

		key = DesignFactory.eINSTANCE.createSortKey( );

		key.setColumnName( "2" ); //$NON-NLS-1$
		key.setColumnPosition( 2 );
		key.setNullValueOrdering( NullOrderingType.NULLS_LAST );
		key.setOptional( true );
		key.setSortDirection( SortDirectionType.DESCENDING );

		list.add( key );

		return setDesign;
	}
}
