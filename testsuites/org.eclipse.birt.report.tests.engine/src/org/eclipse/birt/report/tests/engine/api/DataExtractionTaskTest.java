
package org.eclipse.birt.report.tests.engine.api;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>DataExtractionTask test</b>
 * <p>
 * This case tests extracting data with filter rule from report through
 * IDataExtractionTask interface.
 */

public class DataExtractionTaskTest extends EngineCase
{

	private String report_design;

	private String report_document;

	private IReportDocument reportDoc;

	final static String INPUT_table = "DataExtraction_table.rptdesign";
	final static String INPUT_subquery = "DataExtraction_subquery.rptdesign";
	final static String INPUT_nestquery = "DataExtraction_nestquery.rptdesign";
	final static String OUTPUT_table = "DataExtraction_table.rptdocument";
	final static String OUTPUT_subquery = "DataExtraction_subquery.rptdocument";
	final static String OUTPUT_nestquery = "DataExtraction_nestquery.rptdocument";

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		copyResource_INPUT( INPUT_table, INPUT_table );
		copyResource_INPUT( INPUT_subquery, INPUT_subquery );
		copyResource_INPUT( INPUT_nestquery, INPUT_nestquery );
	}

	public void tearDown( )
	{
		removeResource( );
	}

	public void testMethods( ) throws Exception
	{
		report_design = INPUT_table;
		report_document = OUTPUT_table;
		report_document = createReportDocument( report_design, report_document );

		reportDoc = engine.openReportDocument( report_document.replace(
				'\\',
				'/' ) );
		IDataExtractionTask extractTask = engine
				.createDataExtractionTask( reportDoc );
		checkGetResultSetList( extractTask );
		checkSelectColumns( extractTask );
	}

	private void checkGetResultSetList( IDataExtractionTask task )
	{

		ArrayList results;
		try
		{
			results = (ArrayList) task.getResultSetList( );
			assertEquals( 1, results.size( ) );
		}
		catch ( EngineException e )
		{
			e.printStackTrace( );
			fail( );
		}
	}

	private void checkSelectColumns( IDataExtractionTask task )
	{
		String[] columns = {"code", "territory"};
		task.selectResultSet( "t1" );
		task.selectColumns( columns );
		try
		{
			IExtractionResults result = task.extract( );
			assertNotNull( result );
			assertEquals( 2, result.getResultMetaData( ).getColumnCount( ) );
			assertEquals( "code", result.getResultMetaData( ).getColumnName( 0 ) );
			assertEquals( "territory", result
					.getResultMetaData( )
					.getColumnName( 1 ) );

		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			fail( );
		}
	}

	/**
	 * Test normal data extraction with filter
	 * 
	 * @throws Exception
	 */
	public void testDataExtractionWithFilter( ) throws Exception
	{
		report_design = INPUT_table;
		report_document = OUTPUT_table;
		try
		{
			report_document = createReportDocument(
					report_design,
					report_document );

			reportDoc = engine.openReportDocument( report_document );
			IDataExtractionTask extractTask = engine
					.createDataExtractionTask( reportDoc );

			extractTask.selectResultSet( "t1" );
			IFilterDefinition[] filterExpression = new IFilterDefinition[1];
			filterExpression[0] = new FilterDefinition(
					new ConditionalExpression(
							"row[\"territory\"]",
							ConditionalExpression.OP_EQ,
							"\"EMEA\"",
							null ) );
			extractTask.setFilters( filterExpression );

			IExtractionResults result = extractTask.extract( );

			if ( result != null )
			{
				int officecode = 0;
				IDataIterator data = result.nextResultIterator( );
				if ( data != null )
				{
					data.next( );
					officecode = Integer.parseInt( data
							.getValue( "code" )
							.toString( ) );
					assertEquals(
							"Fail to extract filtered data1",
							4,
							officecode );
					if ( data.next( ) )
					{
						officecode = Integer.parseInt( data
								.getValue( "code" )
								.toString( ) );
						assertEquals(
								"Fail to extract filtered data2",
								7,
								officecode );
					}
				}
				data.close( );
			}
			else
			{
				fail( "Fail to extract filtered data3" );
			}

		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			fail( "Fail to extract filtered data4" );
		}
	}

	/**
	 * test setInstanceID in DataExtractionTask with subquery structure
	 * 
	 * @throws Exception
	 */
	public void testDataExtractionFromIID_subquery( ) throws Exception
	{
		report_design = INPUT_subquery;
		report_document = OUTPUT_subquery;
		try
		{
			report_document = createReportDocument(
					report_design,
					report_document );

			reportDoc = engine.openReportDocument( report_document );
			IDataExtractionTask extractTask = engine
					.createDataExtractionTask( reportDoc );

			/* extract master query data from subquery structure */
			ArrayList iids = findIID( report_document, "LIST" );
			assertEquals( 1, iids.size( ) );

			extractTask.setInstanceID( (InstanceID) iids.get( 0 ) );
			IExtractionResults result = extractTask.extract( );

			if ( result != null )
			{
				String name = null;
				IDataIterator data = result.nextResultIterator( );
				if ( data != null )
				{
					data.next( );
					name = data.getValue( "name" ).toString( );
					assertTrue( name.equalsIgnoreCase( "Atelier graphique" ) );
					if ( data.next( ) )
					{
						name = data.getValue( "name" ).toString( );
						assertTrue( name
								.equalsIgnoreCase( "Signal Gift Stores" ) );
					}
				}
				data.close( );
			}
			else
			{
				fail( );
			}

			/* extract master query data with filter */
			IFilterDefinition[] filterExpression = new IFilterDefinition[1];
			filterExpression[0] = new FilterDefinition(
					new ConditionalExpression(
							"row[\"name\"]",
							ConditionalExpression.OP_EQ,
							"\"Signal Gift Stores\"",
							null ) );
			extractTask.setFilters( filterExpression );

			result = extractTask.extract( );
			if ( result != null )
			{
				String name = null;
				IDataIterator data = result.nextResultIterator( );
				if ( data != null )
				{
					data.next( );
					name = data.getValue( "name" ).toString( );
					assertTrue( name.equalsIgnoreCase( "Signal Gift Stores" ) );
				}
				data.close( );
			}
			else
			{
				fail( );
			}

			/* extract sub query data from subquery structure */
			iids = findIID( report_document, "TABLE" );
			assertEquals( 2, iids.size( ) );

			extractTask.setInstanceID( (InstanceID) iids.get( 0 ) );
			result = extractTask.extract( );

			if ( result != null )
			{
				int num = 0;
				IDataIterator data = result.nextResultIterator( );
				if ( data != null )
				{
					data.next( );
					num = Integer.parseInt( data
							.getValue( "number" )
							.toString( ) );
					assertEquals( 103, num );
					if ( data.next( ) )
					{
						num = Integer.parseInt( data
								.getValue( "number" )
								.toString( ) );
						assertEquals( 112, num );
					}
				}
				data.close( );
			}
			else
			{
				fail( );
			}

			extractTask.setInstanceID( (InstanceID) iids.get( 1 ) );
			/* extract sub query data with filter */
			filterExpression = new IFilterDefinition[1];
			filterExpression[0] = new FilterDefinition(
					new ConditionalExpression(
							"row[\"number\"]",
							ConditionalExpression.OP_EQ,
							"112",
							null ) );
			extractTask.setFilters( filterExpression );
			result = extractTask.extract( );

			if ( result != null )
			{
				IDataIterator data = result.nextResultIterator( );
				IResultMetaData resultMetaData = result.getResultMetaData( );
				int columnCount = resultMetaData.getColumnCount( );
				if ( data != null )
				{
					data.next( );
					assertEquals( "112", data.getValue( "number" ).toString( ) );
				}
				data.close( );
			}
			else
			{
				fail( );
			}
			extractTask.close( );

		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			fail( "Fail to extract data from subquery" );
		}
	}

	/**
	 * test setInstanceID in DataExtractionTask with subquery structure
	 * 
	 * @throws Exception
	 */
	public void testDataExtractionFromIID_nestquery( ) throws Exception
	{
		report_design = INPUT_nestquery;
		report_document = OUTPUT_nestquery;
		try
		{
			report_document = createReportDocument(
					report_design,
					report_document );

			reportDoc = engine.openReportDocument( report_document );
			IDataExtractionTask extractTask = engine
					.createDataExtractionTask( reportDoc );

			/* extract master query data from subquery structure */
			ArrayList iids = findIID( report_document, "LIST" );
			assertEquals( 1, iids.size( ) );

			extractTask.setInstanceID( (InstanceID) iids.get( 0 ) );
			IExtractionResults result = extractTask.extract( );

			if ( result != null )
			{
				String name = null;
				IDataIterator data = result.nextResultIterator( );
				if ( data != null )
				{
					data.next( );
					name = data.getValue( "name" ).toString( );
					assertTrue( name.equalsIgnoreCase( "Atelier graphique" ) );
					if ( data.next( ) )
					{
						name = data.getValue( "name" ).toString( );
						assertTrue( name
								.equalsIgnoreCase( "Signal Gift Stores" ) );
					}
				}
				data.close( );
			}
			else
			{
				fail( "Fail to extract data from subquery" );
			}

			/* extract master query data with filter */
			IFilterDefinition[] filterExpression = new IFilterDefinition[1];
			filterExpression[0] = new FilterDefinition(
					new ConditionalExpression(
							"row[\"name\"]",
							ConditionalExpression.OP_EQ,
							"\"Signal Gift Stores\"",
							null ) );
			extractTask.setFilters( filterExpression );

			result = extractTask.extract( );
			if ( result != null )
			{
				String name = null;
				IDataIterator data = result.nextResultIterator( );
				if ( data != null )
				{
					data.next( );
					name = data.getValue( "name" ).toString( );
					assertTrue( name.equalsIgnoreCase( "Signal Gift Stores" ) );
				}
				data.close( );
			}
			else
			{
				fail( );
			}

			/* extract nest query data from nestquery structure */
			iids = findIID( report_document, "TABLE" );
			assertEquals( 2, iids.size( ) );
			// get first table instance
			extractTask.setInstanceID( (InstanceID) iids.get( 0 ) );
			result = extractTask.extract( );

			if ( result != null )
			{
				int num = 0;
				IDataIterator data = result.nextResultIterator( );
				if ( data != null )
				{
					data.next( );
					num = Integer.parseInt( data
							.getValue( "number" )
							.toString( ) );
					assertEquals( 103, num );
					if ( data.next( ) )
					{
						num = Integer.parseInt( data
								.getValue( "number" )
								.toString( ) );
						assertEquals( 112, num );
					}
				}
				data.close( );
			}
			else
			{
				fail( "Fail to extract data from subquery" );
			}

			/* extract nest sub query data with filter */
			filterExpression = new IFilterDefinition[1];
			filterExpression[0] = new FilterDefinition(
					new ConditionalExpression(
							"row[\"number\"]",
							ConditionalExpression.OP_EQ,
							"112",
							null ) );
			extractTask.setFilters( filterExpression );
			result = extractTask.extract( );

			if ( result != null )
			{
				int num = 0;
				IDataIterator data = result.nextResultIterator( );
				if ( data != null )
				{
					data.next( );
					num = Integer.parseInt( data
							.getValue( "number" )
							.toString( ) );
					assertEquals( 112, num );
				}
				data.close( );
			}
			else
			{
				fail( );
			}
			extractTask.close( );

		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			fail( "Fail to extract data from nestquery" );
		}
	}

	/**
	 * create the report document.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected String createReportDocument( String reportdesign,
			String reportdocument ) throws Exception
	{
		reportdesign = this.genInputFile( reportdesign );
		reportdocument = this.genOutputFile( reportdocument );

		// open an report archive, it is a folder archive.
		IDocArchiveWriter archive = new FileArchiveWriter( reportdocument );
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign( reportdesign );
		// create an IRunTask
		IRunTask runTask = engine.createRunTask( report );
		// execute the report to create the report document.
		runTask.setAppContext( new HashMap( ) );
		runTask.run( archive );
		// close the task, release the resource.
		runTask.close( );
		return reportdocument;
	}

	private ArrayList findIID( String doc, String type )
			throws EngineException, UnsupportedEncodingException
	{
		ArrayList iids = new ArrayList( );
		IRenderTask task = null;
		IReportDocument reportDoc = null;
		reportDoc = engine.openReportDocument( doc );
		task = engine.createRenderTask( reportDoc );

		IRenderOption htmlRenderOptions = new HTMLRenderOption( );
		HashMap appContext = new HashMap( );
		task.setAppContext( appContext );

		ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
		htmlRenderOptions.setOutputStream( ostream );
		htmlRenderOptions.setOutputFormat( "html" );
		( (HTMLRenderOption) htmlRenderOptions ).setEnableMetadata( true );

		task.setRenderOption( htmlRenderOptions );
		task.render( );
		task.close( );

		String content = ostream.toString( "utf-8" );
		Pattern typePattern = Pattern.compile( "(element_type=\"" + type
				+ "\".*iid=\".*\")" );
		Matcher matcher = typePattern.matcher( content );

		while ( matcher.find( ) )
		{
			String tmp_type = null, strIid = null;
			tmp_type = matcher.group( 0 );
			strIid = tmp_type.substring( tmp_type.indexOf( "iid" ) );
			strIid = strIid.substring( 5, strIid.indexOf( "\"", 6 ) );
			iids.add( InstanceID.parse( strIid ) );
		}

		return iids;
	}

}
