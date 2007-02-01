
package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.report.engine.EngineCase;

/**
 * register a page hander to see if the page handle is been called.
 * 
 * This class must be running as plugin unit test.
 * 
 */
public class PageHandlerTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/page-handler.rptdesign";
	static final String REPORT_DESIGN = "page-handler.rptdesign";
	static final String REPORT_DOCUMENT = "./reportdocument.rptdocument";

	protected IReportEngine engine;

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

	long pageNumberStatus[] = new long[]{1, 2, 3, 3};
	boolean checkPointStatus[] = new boolean[]{true, false, false, true};

	class TestPageHandler implements IPageHandler
	{

		int callBackCount = 0;

		public void onPage( int pageNumber, boolean checkpoint,
				IReportDocumentInfo doc )
		{
			assertEquals( pageNumberStatus[callBackCount], pageNumber );
			assertEquals( checkPointStatus[callBackCount], checkpoint );
			callBackCount++;
		}
	}

	public void testHandler( )
	{
		try
		{
			// open the report runnable to execute.
			IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
			// create an IRunTask
			IRunTask task = engine.createRunTask( report );
			// execute the report to create the report document.
			task.setPageHandler( new TestPageHandler( ) );
			task.run( REPORT_DOCUMENT );
			// close the task, release the resource.
			task.close( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		};
	}

}
