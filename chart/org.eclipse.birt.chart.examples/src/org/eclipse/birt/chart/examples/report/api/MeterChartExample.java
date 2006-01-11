/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.examples.report.api;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

public class MeterChartExample
{

	ReportDesignHandle reportDesignHandle = null;

	ElementFactory elementFactory = null;

	MetaDataDictionary dict = null;

	public static void main( String[] args ) throws SemanticException,
			IOException
	{
		new MeterChartExample( ).createReport( );
	}

	void createReport( ) throws SemanticException, IOException
	{
		// A session handle for all open reports
		SessionHandle session = DesignEngine.newSession( null );

		// Create a new report
		reportDesignHandle = session.createDesign( );

		// Element factory is used to create instances of BIRT elements.
		elementFactory = reportDesignHandle.getElementFactory( );

		dict = MetaDataDictionary.getInstance( );

		createMasterPages( );
		createDataSources( );
		createDataSets( );
		createBody( );

		String outputPath = "output";//$NON-NLS-1$
		File outputFolder = new File( outputPath );
		if ( !outputFolder.exists( ) && !outputFolder.mkdir( ) )
		{
			throw new IOException( "Can not create the output folder" );//$NON-NLS-1$
		}
		reportDesignHandle.saveAs( outputPath + "/" + "MeterChartExample.rptdesign" );//$NON-NLS-1$//$NON-NLS-2$
	}

	private void createDataSources( ) throws SemanticException
	{
		ScriptDataSourceHandle dataSourceHandle = elementFactory.newScriptDataSource( "Data Source" );//$NON-NLS-1$
		reportDesignHandle.getDataSources( ).add( dataSourceHandle );
	}

	private void createDataSets( ) throws SemanticException
	{
		// Data Set
		ScriptDataSetHandle dataSetHandle = elementFactory.newScriptDataSet( "Data Set" );//$NON-NLS-1$
		dataSetHandle.setDataSource( "Data Source" );//$NON-NLS-1$

		// Set open( ) in code
		dataSetHandle.setOpen( "i=0;"//$NON-NLS-1$
				+ "sourcedata = new Array( new Array(3), new Array(3), new Array(3),  new Array(3));"//$NON-NLS-1$
				
				+ "sourcedata[0][0] = 10; "//$NON-NLS-1$
				+ "sourcedata[0][1] = \"Ice Bella\";"//$NON-NLS-1$
				+ "sourcedata[0][2] = 304;"//$NON-NLS-1$

				+ "sourcedata[1][0] = 10; "//$NON-NLS-1$
				+ "sourcedata[1][1] = \"Nola Dicci\";"//$NON-NLS-1$
				+ "sourcedata[1][2] = 258;"//$NON-NLS-1$

				+ "sourcedata[2][0] = 11; "//$NON-NLS-1$
				+ "sourcedata[2][1] = \"Ice Bella\";"//$NON-NLS-1$
				+ "sourcedata[2][2] = 202;"//$NON-NLS-1$

				+ "sourcedata[3][0] = 11; "//$NON-NLS-1$
				+ "sourcedata[3][1] = \"Nola Dicci\";"//$NON-NLS-1$
				+ "sourcedata[3][2] = 181;" );//$NON-NLS-1$

		// Set fetch( ) in code
		dataSetHandle.setFetch( "if ( i < 4 ){"//$NON-NLS-1$
				+ "row[\"Month\"] = sourcedata[i][0];"//$NON-NLS-1$
				+ "row[\"Product\"] = sourcedata[i][1];"//$NON-NLS-1$
				+ "row[\"Amount\"] = sourcedata[i][2];"//$NON-NLS-1$
				+ "i++;"//$NON-NLS-1$
				+ "return true;}" + "else return false;" );//$NON-NLS-1$//$NON-NLS-2$

		// Set Output Columns in Data Set
		ColumnHint ch1 = StructureFactory.createColumnHint( );
		ch1.setProperty( "columnName", "Month" );//$NON-NLS-1$//$NON-NLS-2$

		ColumnHint ch2 = StructureFactory.createColumnHint( );
		ch2.setProperty( "columnName", "Product" );//$NON-NLS-1$//$NON-NLS-2$

		ColumnHint ch3 = StructureFactory.createColumnHint( );
		ch3.setProperty( "columnName", "Amount" );//$NON-NLS-1$//$NON-NLS-2$

		PropertyHandle columnHint = dataSetHandle.getPropertyHandle( ScriptDataSetHandle.COLUMN_HINTS_PROP );
		columnHint.addItem( ch1 );
		columnHint.addItem( ch2 );
		columnHint.addItem( ch3 );

		// Set Preview Results columns in Data Set
		ResultSetColumn rs1 = StructureFactory.createResultSetColumn( );
		rs1.setColumnName( "Month" );//$NON-NLS-1$
		rs1.setPosition( new Integer( 1 ) );
		rs1.setDataType( "integer" );//$NON-NLS-1$

		ResultSetColumn rs2 = StructureFactory.createResultSetColumn( );
		rs2.setColumnName( "Product" );//$NON-NLS-1$
		rs2.setPosition( new Integer( 2 ) );
		rs2.setDataType( "string" );//$NON-NLS-1$

		ResultSetColumn rs3 = StructureFactory.createResultSetColumn( );
		rs3.setColumnName( "Amount" );//$NON-NLS-1$
		rs3.setPosition( new Integer( 3 ) );
		rs3.setDataType( "integer" );//$NON-NLS-1$

		PropertyHandle resultSet = dataSetHandle.getPropertyHandle( ScriptDataSetHandle.RESULT_SET_PROP );
		resultSet.addItem( rs1 );
		resultSet.addItem( rs2 );
		resultSet.addItem( rs3 );

		reportDesignHandle.getDataSets( ).add( dataSetHandle );
	}

	private void createMasterPages( ) throws ContentException, NameException
	{
		DesignElementHandle simpleMasterPage = elementFactory.newSimpleMasterPage( "Master Page" );//$NON-NLS-1$
		reportDesignHandle.getMasterPages( ).add( simpleMasterPage );
	}

	private void createBody( ) throws SemanticException
	{
		ExtendedItemHandle eih = elementFactory.newExtendedItem( null, "Chart" );//$NON-NLS-1$
		try
		{
			eih.setHeight( "288pt" );//$NON-NLS-1$
			eih.setWidth( "252pt" );//$NON-NLS-1$
			eih.setProperty( ExtendedItemHandle.DATA_SET_PROP, "Data Set" );//$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}
		reportDesignHandle.getBody( ).add( eih );
		
		ChartReportItemImpl crii;
		try
		{
			//Add ChartReportItemImpl to ExtendedItemHandle
			crii = (ChartReportItemImpl) eih.getReportItem( );
			//Add chart instance to ChartReportItemImpl
			crii.setProperty( "chart.instance", createMeterChart( ) );//$NON-NLS-1$
		}
		catch ( ExtendedElementException e )
		{
			e.printStackTrace( );
		}
	}

	private Chart createMeterChart( )
	{			
		DialChart dChart = (DialChart) DialChartImpl.create( );
		dChart.setDialSuperimposition( true );
		dChart.setType( "Meter Chart" );//$NON-NLS-1$
		dChart.setSubType( "Superimposed Meter Chart" );//$NON-NLS-1$
		dChart.getBlock( ).setBounds( BoundsImpl.create( 0, 0, 252, 288 ) );
		dChart.getLegend( ).setItemType( LegendItemType.SERIES_LITERAL );

		SampleData sd = DataFactory.eINSTANCE.createSampleData( );
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData( );
		sdBase.setDataSetRepresentation( "A, B, C" );//$NON-NLS-1$//$NON-NLS-2$
		sd.getBaseSampleData( ).add( sdBase );

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		sdOrthogonal.setDataSetRepresentation( "5, 4, 12" );//$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex( 0 );
		sd.getOrthogonalSampleData( ).add( sdOrthogonal );

		dChart.setSampleData( sd );

		Series seCategory = SeriesImpl.create( );
		Query query1 = QueryImpl.create( "row[\"Product\"]" );//$NON-NLS-1$
		seCategory.getDataDefinition( ).add( query1 );

		SeriesDefinition series = SeriesDefinitionImpl.create( );
		series.getSeries( ).add( seCategory );
		dChart.getSeriesDefinitions( ).add( series );

		DialSeries seDial = (DialSeries) DialSeriesImpl.create( );
		Query query2 = QueryImpl.create( "row[\"Amount\"]" );//$NON-NLS-1$
		seDial.getDataDefinition( ).add( query2 );

		SeriesDefinition seGroup = SeriesDefinitionImpl.create( );
		Query query3 = QueryImpl.create( "row[\"Month\"]" );//$NON-NLS-1$
		seGroup.setQuery( query3 );
		seGroup.getSeriesPalette( ).update( -2 );
		series.getSeriesDefinitions( ).add( seGroup );
		seGroup.getSeries( ).add( seDial );

		return dChart;
	}
}
