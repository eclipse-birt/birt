
package org.eclipse.birt.report.tests.engine.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>Test DataSetID API</b>
 */
public class DataSetIDTest extends EngineCase
{

	private String inPath = getClassFolder( ) + "/" + INPUT_FOLDER + "/";

	/**
	 * Test DataSetID methods with input report design
	 * 
	 * @throws EngineException
	 * @throws IOException
	 */
	public void test_DataSetIDFromReport( ) throws EngineException, IOException
	{
		String reportName = "dataSetID.rptdesign";
		IReportRunnable reportRunnable = engine.openReportDesign( inPath
				+ reportName );
		HTMLRenderOption options = new HTMLRenderOption( );
		options.setOutputFormat( "html" );
		ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
		options.setOutputStream( ostream );
		IRunAndRenderTask task = engine.createRunAndRenderTask( reportRunnable );
		task.setRenderOption( options );
		task.run( );
		assertTrue( task.getErrors( ).size( ) <= 0 );
		task.close( );

		// get instance id of two tables and one list in report
		ArrayList iids = new ArrayList( );
		String content = ostream.toString( "utf-8" );
		ostream.close( );
		Pattern typePattern = Pattern.compile( "(element_type=\"TABLE"
				+ "\".*iid=\".*\")" );
		Matcher matcher = typePattern.matcher( content );
		String strIid = null;
		while ( matcher.find( ) )
		{
			String tmp_type = null;
			tmp_type = matcher.group( 0 );
			strIid = tmp_type.substring( tmp_type.indexOf( "iid" ) );
			strIid = strIid.substring( 5, strIid.indexOf( "\"", 6 ) );
			iids.add( strIid );
		}
		typePattern = Pattern.compile( "(element_type=\"LIST"
				+ "\".*iid=\".*\")" );
		matcher = typePattern.matcher( content );
		while ( matcher.find( ) )
		{
			String tmp_type = null;
			tmp_type = matcher.group( 0 );
			strIid = tmp_type.substring( tmp_type.indexOf( "iid" ) );
			strIid = strIid.substring( 5, strIid.indexOf( "\"", 6 ) );
			iids.add( strIid );
		}

		// DataID: dataSet:0
		InstanceID iid = InstanceID.parse( iids.get( 0 ).toString( ) );
		DataSetID dsID = iid.getDataID( ).getDataSetID( );
		String dsName = iid.toString( ).substring(
				3,
				iid.toString( ).indexOf( ":" ) );
		assertEquals( dsName, dsID.getDataSetName( ) );
		assertEquals( 0, dsID.getRowID( ) );
		assertNull( dsID.getParentID( ) );
		assertNull( dsID.getQueryName( ) );

		// DataID: {dataSet}.0.group:0
		iid = InstanceID.parse( iids.get( 1 ).toString( ) );
		dsID = iid.getDataID( ).getDataSetID( );
		assertNull( dsID.getDataSetName( ) );
		assertEquals( 0, dsID.getRowID( ) );
		assertNotNull( dsID.getParentID( ) );
		assertEquals( dsName, dsID.getParentID( ).getDataSetName( ) );
		assertEquals( "52", dsID.getQueryName( ) );

		// DataID:{{dataSet}.0.group}.0.group1:0
		iid = InstanceID.parse( iids.get( 2 ).toString( ) );
		dsID = iid.getDataID( ).getDataSetID( );
		assertNull( dsID.getDataSetName( ) );
		assertEquals( 0, dsID.getRowID( ) );
		assertNull( dsID.getParentID( ).getDataSetName( ) );
		assertNotNull( dsID.getParentID( ).getParentID( ) );
		assertEquals( dsName, dsID
				.getParentID( )
				.getParentID( )
				.getDataSetName( ) );
		assertEquals( "61", dsID.getQueryName( ) );
	}

	/**
	 * Test getParentID() method
	 */
	public void test_getParentID( )
	{
		DataSetID dsID = new DataSetID( new DataSetID( "parent" ), 1, "query" );
		assertNotNull( dsID.getParentID( ) );
		assertEquals( "parent", dsID.getParentID( ).getDataSetName( ) );

		dsID = new DataSetID( null, 0, null );
		assertNull( dsID.getParentID( ) );

		dsID = new DataSetID( "dataset" );
		assertNull( dsID.getParentID( ) );
	}

	/**
	 * Test getDataSetName() method
	 */
	public void test_getDataSetName( )
	{
		DataSetID dsID = new DataSetID( "ds" );
		assertEquals( "ds", dsID.getDataSetName( ) );

		dsID = new DataSetID( null );
		assertNull( dsID.getDataSetName( ) );
	}

	/**
	 * Test getQueryName() method
	 */
	public void test_getQueryName( )
	{
		DataSetID dsID = new DataSetID( null, 0, "query" );
		assertEquals( "query", dsID.getQueryName( ) );

		dsID = new DataSetID( null, 0, "²éÑ¯" );
		assertEquals( "²éÑ¯", dsID.getQueryName( ) );

		dsID = new DataSetID( null, 0, "~!@#$%^&*()_+?>:" );
		assertEquals( "~!@#$%^&*()_+?>:", dsID.getQueryName( ) );

		dsID = new DataSetID( null, 0, "~!@#$%^&*()_+?>:" );
		assertEquals( "~!@#$%^&*()_+?>:", dsID.getQueryName( ) );

		dsID = new DataSetID( null, 0, null );
		assertNull( dsID.getQueryName( ) );
	}

	/**
	 * Test getRowID() method
	 */
	public void test_getRowID( )
	{
		DataSetID dsID = new DataSetID( null, 0, null );
		assertEquals( 0, dsID.getRowID( ) );

		dsID = new DataSetID( null, 1, null );
		assertEquals( 1, dsID.getRowID( ) );

		dsID = new DataSetID( null, -1, null );
		assertEquals( -1, dsID.getRowID( ) );

		dsID = new DataSetID( null, Long.MIN_VALUE, null );
		assertEquals( Long.MIN_VALUE, dsID.getRowID( ) );

		dsID = new DataSetID( null, Long.MAX_VALUE, null );
		assertEquals( Long.MAX_VALUE, dsID.getRowID( ) );
	}

	/**
	 * Test toString() method
	 */
	public void test_toString( )
	{
		DataSetID dsID = new DataSetID( "ds" );
		assertEquals( "ds", dsID.toString( ) );

		dsID = new DataSetID( null, 0, null );
		assertNull( dsID.toString( ) );

		dsID = new DataSetID( new DataSetID( "parent" ), 1, "query" );
		assertEquals( "{parent}.1.query", dsID.toString( ) );

		dsID = new DataSetID( null, 1, "query" );
		assertNull( dsID.toString( ) );

		dsID = new DataSetID( new DataSetID(
				new DataSetID( "grandpa" ),
				0,
				null ), 1, "query" );
		assertEquals( "{{grandpa}.0.null}.1.query", dsID.toString( ) );

		dsID = new DataSetID( new DataSetID( "parent" ), 1, null );
		assertEquals( "{parent}.1.null", dsID.toString( ) );
	}
}
