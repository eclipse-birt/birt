package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.birt.report.engine.EngineCase;

import com.ibm.icu.util.TimeZone;



public class TimeZoneTest extends EngineCase
{
	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/timeZoneTest.xml";
	static final String GOLDEN_RUNANDRENDER = "test/org/eclipse/birt/report/engine/api/timeZone-runandrendertask.html";
	static final String GOLDEN_RENDER = "test/org/eclipse/birt/report/engine/api/timeZone-rendertask.html";
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
	
	public void testRunAndRenderTask( )
	{
		try
		{
			IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
			IRunAndRenderTask task = engine.createRunAndRenderTask( report );
			task.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
			IRenderOption option = new HTMLRenderOption( );
			option.setOutputFormat( "html" ); //$NON-NLS-1$
			option.setOutputFileName( TEMP_RESULT );
			// set the render options
			task.setRenderOption( option );
			task.run( );

			assertTrue( compareFiles( TEMP_RESULT, GOLDEN_RUNANDRENDER ) );
		}
		catch ( Exception ex )
		{
			assert false;
		}
	}
	
	public void testRenderTask( ) throws Exception
	{
		createReportDocument( );
		IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );
		IRenderTask task = engine.createRenderTask( reportDoc );
		task.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
		IRenderOption option = new HTMLRenderOption( );
		option.setOutputFormat( "html" ); //$NON-NLS-1$
		option.setOutputFileName( TEMP_RESULT );
		// set the render options
		task.setRenderOption( option );
		// render report by page
		task.render( );

		assertTrue( compareFiles( TEMP_RESULT, GOLDEN_RENDER ) );
	}
	
	private boolean compareFiles(String src, String golden)
	{
		boolean result = false;
		try
		{
			InputStream goldenInputStream = new FileInputStream(new File(golden));
			assert(goldenInputStream!= null);
			StringBuffer goldenBuffer = new StringBuffer();
			byte[] buffer = new byte[5120];
			int readCount = -1;
			while ( ( readCount = goldenInputStream.read( buffer ) ) != -1 )
			{
				goldenBuffer.append( new String(buffer) );
			}
			
			InputStream srcInputStream = new FileInputStream(new File(golden));
			assert(srcInputStream!= null);
			StringBuffer srcBuffer = new StringBuffer();
			buffer = new byte[5120];
			readCount = -1;
			while ( ( readCount = srcInputStream.read( buffer ) ) != -1 )
			{
				srcBuffer.append( new String(buffer) );
			}
			
			result = (srcBuffer.toString( )).equals( goldenBuffer.toString( ) );
		}catch(Exception ex)
		{
			ex.printStackTrace( );
			return false;
		}
		return result;
	}
}
