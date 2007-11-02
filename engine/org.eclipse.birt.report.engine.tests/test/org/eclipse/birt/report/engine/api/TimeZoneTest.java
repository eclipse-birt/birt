package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.birt.report.engine.EngineCase;

import com.ibm.icu.util.TimeZone;



public class TimeZoneTest extends EngineCase
{
	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/timeZoneTest.xml";
	static final String TEMP_RESULT = "tempResult.html";
	public void setUp( )
	{
		removeFile( REPORT_DOCUMENT );
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		// create the report engine using default config
		engine = createReportEngine( );
	}

	public void tearDown( )
	{
		// shut down the engine.
		engine.shutdown( );
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
	}
	
	public void testRunAndRenderTask()
	{
		try
		{
			removeFile( TEMP_RESULT );
			IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
			IRunAndRenderTask task = engine.createRunAndRenderTask( report );
			task.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
			IRenderOption option = new HTMLRenderOption( );
			option.setOutputFormat( "html" ); //$NON-NLS-1$
			option.setOutputFileName( TEMP_RESULT );
			// set the render options
			task.setRenderOption( option );
			task.run( );
			
			assertTrue(compareFiles( TEMP_RESULT,"timeZone-runandrendertask.html"));
			
		}
		catch ( Exception ex )
		{
			assert false;
		}
	}
	
	public void testRenderTask() throws Exception
	{
		removeFile( TEMP_RESULT );
		removeFile( REPORT_DOCUMENT );
		
		createReportDocument( );
		IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );
		IRenderTask task = engine.createRenderTask( reportDoc );
		task.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
		IRenderOption option = new HTMLRenderOption( );
		option.setOutputFormat( "html" ); //$NON-NLS-1$
		option.setOutputFileName( TEMP_RESULT+"1" );
		// set the render options
		task.setRenderOption( option );
		// render report by page
		task.render( );
		
		assertTrue(compareFiles( TEMP_RESULT,"timeZone-runandrendertask.html"));
	}
	
	private boolean compareFiles(String src, String golden)
	{
		boolean result = false;
		try
		{
			InputStream in = this.getClass( ).getResourceAsStream(
			golden );
			assert ( in != null );
			byte[] buffer = new byte[in.available( )];
			in.read( buffer );
			String goldenString = new String( buffer );
			
			File srcFile = new File(src);
			in = new FileInputStream( src );
			assert( in != null);
			buffer = new byte[in.available( )];
			in.read( buffer );
			String srcString = new String( buffer );
			result = srcString.equals( goldenString );
		}catch(Exception ex)
		{
			ex.printStackTrace( );
			return false;
		}
		return result;
	}
}
