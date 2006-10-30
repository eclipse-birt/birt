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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests api compatibility.
 */

public class APICompatibleTest extends BaseTestCase
{

	/**
	 * Supports the obsolete setValueExpr() method.
	 * 
	 * @throws Exception
	 */

	public void testDataValueExpr( ) throws Exception
	{
		createDesign( );

		DataItemHandle data = designHandle.getElementFactory( ).newDataItem(
				"data1" ); //$NON-NLS-1$

		designHandle.getBody( ).add( data );
		data.setValueExpr( "row[\"column1\"] + row[\"column2\"]" ); //$NON-NLS-1$

		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"table1", 2 ); //$NON-NLS-1$
		data = designHandle.getElementFactory( ).newDataItem( "data2" ); //$NON-NLS-1$
		data.setValueExpr( "row[\"value1\"] + row[\"value2\"]" ); //$NON-NLS-1$

		table.getCell( 1, 1 ).getContent( ).add( data );

		designHandle.getBody( ).add( table );

		saveAs( "DataCompatibleValueExpr_out.xml" ); //$NON-NLS-1$
		compareTextFile( "DataCompatibleValueExpr_golden.xml", //$NON-NLS-1$
				"DataCompatibleValueExpr_out.xml" ); //$NON-NLS-1$
	}

	/**
	 * Supports the misc setting expression methods.
	 * 
	 * @throws Exception
	 */

	public void testBoundColumnWriterExpression( ) throws Exception
	{
		createDesign( );

		DataItemHandle data = designHandle.getElementFactory( ).newDataItem(
				"data1" ); //$NON-NLS-1$

		designHandle.getBody( ).add( data );
		Action action = new Action( );
		data.setAction( action );
		ActionHandle actionHandle = data.getActionHandle( );
		actionHandle
				.setLinkType( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK );
		actionHandle.setTargetBookmark( "row[\"actionBookMark\"]" ); //$NON-NLS-1$

		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"table1", 2 ); //$NON-NLS-1$
		ImageHandle image = designHandle.getElementFactory( ).newImage(
				"image1" ); //$NON-NLS-1$
		image.setBookmark( "row[\"bookmark1\"] + row[\"bookmark2\"]" ); //$NON-NLS-1$
		image.setValueExpression( "row[\"image1\"] + row[\"valueExpr\"]" ); //$NON-NLS-1$

		table.getCell( 1, 1 ).getContent( ).add( image );

		TableHandle nestedTable = designHandle.getElementFactory( )
				.newTableItem( "table2", 2 ); //$NON-NLS-1$
		ParamBinding paramBinding = new ParamBinding( );
		paramBinding.setParamName( "binding1" ); //$NON-NLS-1$
		paramBinding.setExpression( "row[\"value1\"]" ); //$NON-NLS-1$
		nestedTable.getPropertyHandle( TableHandle.PARAM_BINDINGS_PROP )
				.addItem( paramBinding );

		ColumnHandle column = (ColumnHandle) nestedTable.getColumns( ).get( 0 );
		HideRule hideRule = new HideRule( );
		hideRule.setExpression( "row[\"hide1Expr\"]" ); //$NON-NLS-1$
		hideRule.setFormat( DesignChoiceConstants.FORMAT_TYPE_REPORTLET );
		column.getPropertyHandle( ColumnHandle.VISIBILITY_PROP ).addItem(
				hideRule );

		table.getCell( 2, 2 ).getContent( ).add( nestedTable );

		designHandle.getBody( ).add( table );

		TextDataHandle textData = designHandle.getElementFactory( )
				.newTextData( "textData1" ); //$NON-NLS-1$
		textData.setValueExpr( "row[\"textData1ValueExpr\"]" ); //$NON-NLS-1$
		GridHandle grid = designHandle.getElementFactory( ).newGridItem(
				"grid1", 2, 2 ); //$NON-NLS-1$
		grid.getCell( 2, 2 ).getContent( ).add( textData );

		table.getCell( 3, 1 ).getContent( ).add( grid );
		table.getCell( 3, 1 ).setOnCreate( "row[\"onCreateValueExpr\"] + 1" );//$NON-NLS-1$

		ListHandle list = designHandle.getElementFactory( ).newList( "list1" ); //$NON-NLS-1$
		FilterCondition filter = new FilterCondition( );
		filter.setExpr( "row[\"filter1ValueExpr\"]" ); //$NON-NLS-1$
		filter.setValue1( "row[\"filter1Value1\"]" ); //$NON-NLS-1$
		filter.setValue2( "row[\"filter1Value2\"]" ); //$NON-NLS-1$
		list.getPropertyHandle( ListHandle.FILTER_PROP ).addItem( filter );

		SortKey sort = new SortKey( );
		sort.setKey( "row[\"sort1Key\"]" ); //$NON-NLS-1$
		list.getPropertyHandle( ListHandle.SORT_PROP ).addItem( sort );

		designHandle.getBody( ).add( list );

		ScalarParameterHandle param = designHandle.getElementFactory( )
				.newScalarParameter( "param1" ); //$NON-NLS-1$
		param.setValueExpr( "row[\"param1ValueExpr\"]" ); //$NON-NLS-1$
		param.setLabelExpr( "row[\"param1LabelExpr\"]" ); //$NON-NLS-1$

		designHandle.getParameters( ).add( param );

		param = designHandle.getElementFactory( ).newScalarParameter( "param2" ); //$NON-NLS-1$
		param.setValueExpr( "param2ValueExpr" ); //$NON-NLS-1$
		param.setLabelExpr( "param2LabelExpr" ); //$NON-NLS-1$
		designHandle.getParameters( ).add( param );

		textData = designHandle.getElementFactory( ).newTextData( "textData2" ); //$NON-NLS-1$

		designHandle.getBody( ).add( textData );

		ComputedColumn boundColumn = new ComputedColumn( );
		boundColumn.setName( "New Column" ); //$NON-NLS-1$
		boundColumn.setExpression( "row[\"textData2ValueExpr\"]" ); //$NON-NLS-1$

		textData.addColumnBinding( boundColumn, true );
		textData.setValueExpr( "row[\"New Column\"]" ); //$NON-NLS-1$

		saveAs( "CompatibleExpression_out.xml" ); //$NON-NLS-1$
		assertTrue( compareTextFile( "CompatibleExpression_golden.xml", //$NON-NLS-1$
				"CompatibleExpression_out.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Supports the misc setting expression methods.
	 * 
	 * @throws Exception
	 */

	public void testBoundColumnWriterNoExpression( ) throws Exception
	{
		createDesign( );
		design.getVersionManager( ).setVersion( "1" ); //$NON-NLS-1$

		DataItemHandle data = designHandle.getElementFactory( ).newDataItem(
				"data1" ); //$NON-NLS-1$

		data.setTocExpression( "row[\"value1\"]" ); //$NON-NLS-1$
		data.setValueExpr( "row[\"valueExpr\"]" );//$NON-NLS-1$
		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"table1", 2 ); //$NON-NLS-1$

		table.getCell( 1, 1 ).getContent( ).add( data );

		designHandle.getBody( ).add( table );

		FilterCondition filter = new FilterCondition( );
		filter.setExpr( "row[\"filter1ValueExpr\"]" ); //$NON-NLS-1$
		filter.setValue1( "row[\"filter1Value1\"]" ); //$NON-NLS-1$
		filter.setValue2( "row[\"filter1Value2\"]" ); //$NON-NLS-1$
		table.getPropertyHandle( ListHandle.FILTER_PROP ).addItem( filter );

		saveAs( "CompatibleExpression_out_1.xml" ); //$NON-NLS-1$

		assertTrue( compareTextFile( "CompatibleExpression_golden_1.xml", //$NON-NLS-1$
				"CompatibleExpression_out_1.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Supports the misc setting expression methods.
	 * 
	 * @throws Exception
	 */

	public void testBoundColumnWithGroup( ) throws Exception
	{
		createDesign( );

		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"table1", 3 ); //$NON-NLS-1$
		designHandle.getBody( ).add( table );

		TableGroupHandle group = designHandle.getElementFactory( )
				.newTableGroup( );
		group.getFooter( ).add(
				designHandle.getElementFactory( ).newTableRow( 3 ) );
		table.getGroups( ).add( group );

		group = designHandle.getElementFactory( ).newTableGroup( );
		group.getFooter( ).add(
				designHandle.getElementFactory( ).newTableRow( 3 ) );
		table.getGroups( ).add( group );

		DataItemHandle data = designHandle.getElementFactory( ).newDataItem(
				"data1" ); //$NON-NLS-1$
		data.setValueExpr( "row[\"valueData\"]" ); //$NON-NLS-1$

		table.getCell( 3, 1 ).getContent( ).add( data );

		data = designHandle.getElementFactory( ).newDataItem( "data1" ); //$NON-NLS-1$
		data.setValueExpr( "row[\"valueData\"]" ); //$NON-NLS-1$

		table.getCell( 4, 1 ).getContent( ).add( data );

		saveAs( "CompatibleExpression_out_2.xml" ); //$NON-NLS-1$
		assertTrue( compareTextFile( "CompatibleExpression_golden_2.xml", //$NON-NLS-1$
				"CompatibleExpression_out_2.xml" ) ); //$NON-NLS-1$		
	}

	/**
	 * Bugzilla 156977. Result set property is replaced by result hints
	 * property.
	 * 
	 * @throws Exception
	 */

	public void testScriptResultSet( ) throws Exception
	{
		createDesign( );

		ScriptDataSetHandle ds = designHandle.getElementFactory( )
				.newScriptDataSet( "dataSet1" ); //$NON-NLS-1$

		PropertyHandle ph = ds
				.getPropertyHandle( ScriptDataSetHandle.RESULT_SET_PROP );

		assertNotNull( ph );

		for ( int i = 0; i < 2; i++ )
		{
			ResultSetColumn rsc = StructureFactory.createResultSetColumn( );
			rsc.setPosition( new Integer( i + 1 ) );
			rsc.setColumnName( "COLUMN_" + i ); //$NON-NLS-1$
			rsc.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL );

			ph.addItem( rsc );
		}
	}
}
