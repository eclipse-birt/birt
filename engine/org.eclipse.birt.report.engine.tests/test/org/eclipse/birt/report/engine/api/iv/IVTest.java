
package org.eclipse.birt.report.engine.api.iv;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FolderArchiveReader;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.RunnableMonitor;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ReportEngine;

public class IVTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/iv/IV_SalesByProducts.rptdesign";
	static final String REPORT_DESIGN_WITH_PARAM_RESOURCE = "org/eclipse/birt/report/engine/api/iv/IV_ReportWithParam.rptdesign";
	static final String TEST_FOLDER = "./utest/";
	static final String REPORT_DESIGN = "./utest/design.rptdesign";
	static final String REPORT_DOCUMENT_FOLDER = "./utest/reportdocument.folder/";
	static final String REPORT_DOCUMENT = "./utest/reportdocument.rptdocument";

	IReportEngine engine;

	public void setUp( )
	{
		removeFile( TEST_FOLDER );
		EngineConfig config = new EngineConfig( );
		engine = new ReportEngine( config );
	}

	public void tearDown( )
	{
		engine.shutdown( );
		removeFile( TEST_FOLDER );
	}

	protected void createIVReportDocument( ) throws Exception
	{
		createReportDocument( new HashMap( ) );
	}

	protected void createReportDocument( Map paramValues ) throws Exception
	{
		// create the orignal report document
		IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
		IRunTask task = engine.createRunTask( report );
		try
		{
			task.setParameterValues( paramValues );
			task.run( REPORT_DOCUMENT );
		}
		finally
		{
			task.close( );
		}
	}

	public void testMutipleRun( ) throws Exception
	{
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		createIVReportDocument( );
		RunnableMonitor monitor = new RunnableMonitor( );
		for ( int i = 0; i < 8; i++ )
		{
			new IVRunnable( engine, monitor );
		}
		monitor.start( );
		monitor.printStackTrace( );
		assertTrue( monitor.getFailedRunnables( ).isEmpty( ) );
	}

	public void testRun( ) throws Exception
	{
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		createIVReportDocument( );
		new IVTask( engine, REPORT_DOCUMENT_FOLDER ).run( );
	}

	public void testRunWithParamters( ) throws Exception
	{
		copyResource( REPORT_DESIGN_WITH_PARAM_RESOURCE, REPORT_DESIGN );
		Map params = new HashMap();
		params.put( "param", new Integer(100) );
		createReportDocument( params);
		new IVTask(engine, REPORT_DOCUMENT_FOLDER).run( );
	}

	public void testRunWithNoReportDesign() throws Exception
	{
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		createIVReportDocument( );
		new UnpackTask(REPORT_DOCUMENT_FOLDER).run( );
		//remove the design file
		new File(REPORT_DOCUMENT_FOLDER + "design").delete( );

		try
		{
			new RenderTask( engine, REPORT_DOCUMENT_FOLDER ).run( );
		}
		catch(EngineException ex)
		{
			ex.printStackTrace( );
		}
	}
	
	public void testRunWithCorruptDocument( ) throws Exception
	{
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		createIVReportDocument( );
		
		new UnpackTask(REPORT_DOCUMENT_FOLDER).run( );
		// corrupt the content file
		RandomAccessFile file = new RandomAccessFile( REPORT_DOCUMENT_FOLDER
				+ "content/content.dat", "rw" );
		file.setLength( file.length( ) / 2 );
		file.close( );
		new RenderTask( engine, REPORT_DOCUMENT_FOLDER ).run( );
	}
	
	static final String REPORT_DESIGN_NO_FILTER_RESOURCE = "org/eclipse/birt/report/engine/api/iv/IV_DesignNoFilter.rptdesign";
	static final String REPORT_DESIGN_WITH_FILTER_RESOURCE = "org/eclipse/birt/report/engine/api/iv/IV_DesignWithFilter.rptdesign";
	
	public void testFilters( ) throws Exception
	{
		doTestIV( REPORT_DESIGN_NO_FILTER_RESOURCE,
				REPORT_DESIGN_WITH_FILTER_RESOURCE );
	}

	static final String REPORT_DESIGN_WITHOUT_CC_RESOURCE = "org/eclipse/birt/report/engine/api/iv/IV_DesignWithoutCC.rptdesign";
	static final String REPORT_DESIGN_WITH_CC_RESOURCE = "org/eclipse/birt/report/engine/api/iv/IV_DesignWithCC.rptdesign";
/*
    still failed.
	public void testCC( ) throws Exception
	{
		doTestIV( REPORT_DESIGN_WITHOUT_CC_RESOURCE,
				REPORT_DESIGN_WITH_CC_RESOURCE );
	}
*/
	
	protected void doTestIV(String originalDesign, String changedDesign) throws Exception
	{
		copyResource( originalDesign, REPORT_DESIGN );
		
		IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
		IRunTask task = engine.createRunTask( report );
		task.run( REPORT_DOCUMENT_FOLDER );
		task.close( );
		
		// rerun the report based on the report document
		copyResource( changedDesign, REPORT_DESIGN );
		report = engine.openReportDesign( REPORT_DESIGN );
		task = engine.createRunTask( report );
		task.setDataSource( new FolderArchiveReader( REPORT_DOCUMENT_FOLDER ) );
		task.run( REPORT_DOCUMENT_FOLDER );
		task.close( );

		// render the generated report document
		IReportDocument doc = engine.openReportDocument( REPORT_DOCUMENT_FOLDER );
		long pageCount = doc.getPageCount( );
		for ( int i = 1; i <= pageCount; i++ )
		{
			IRenderTask renderTask = engine.createRenderTask( doc );

			HTMLRenderOption option = new HTMLRenderOption( );
			option.setOutputFormat( "html" );
			option.setOutputFileName( "./utest/output" + i + ".html" );
			renderTask.setRenderOption( option );
			renderTask.setPageNumber( i );

			renderTask.render( );
			List errors = renderTask.getErrors( );
			assertEquals( 0, errors.size( ) );
			renderTask.close( );
		}
		doc.close( );
	}
	

	static private class IVRunnable extends RunnableMonitor.Runnable
	{

		static int TOTAL_THREAD = 0;
		int threadNumber;
		IReportEngine engine;

		IVRunnable( IReportEngine engine, RunnableMonitor monitor )
		{
			super( monitor );
			threadNumber = TOTAL_THREAD++;
			this.engine = engine;
		}

		public void doRun( ) throws Exception
		{
			new IVTask( engine, REPORT_DOCUMENT_FOLDER + threadNumber ).run( );
		}
	}
	
	static private class UnpackTask
	{
		String folder;
		UnpackTask( String folder )
		{
			this.folder = folder;
		}
		
		public void run() throws Exception
		{
			// unpack it to a folder
			FileArchiveReader reader = new FileArchiveReader( REPORT_DOCUMENT );
			reader.open( );
			reader.expandFileArchive( folder );
			reader.close( );
		}
	}
	
	static private class RenderTask
	{
		String folder;
		IReportEngine engine;

		RenderTask( IReportEngine engine, String folder )
		{
			this.engine = engine;
			this.folder = folder;
		}

		public void run( ) throws Exception
		{
			// render the generated report document
			IReportDocument doc = engine.openReportDocument( folder );
			long pageCount = doc.getPageCount( );
			for ( int i = 1; i <= pageCount; i++ )
			{
				IRenderTask renderTask = engine.createRenderTask( doc );

				HTMLRenderOption option = new HTMLRenderOption( );
				option.setOutputFormat( "html" );
				option.setOutputStream( new ByteArrayOutputStream( ) );
				renderTask.setRenderOption( option );
				renderTask.setPageNumber( i );

				renderTask.render( );
				List errors = renderTask.getErrors( );
				assertEquals( 0, errors.size( ) );
				renderTask.close( );
			}
			doc.close( );
		}
	}
	
	static private class IVTask
	{

		String documentFolder;
		IReportEngine engine;

		IVTask( IReportEngine engine, String folder )
		{
			this.engine = engine;
			documentFolder = folder;
		}

		public void run( ) throws Exception
		{
			// unpack it to a folder
			FileArchiveReader reader = new FileArchiveReader( REPORT_DOCUMENT );
			reader.open( );
			reader.expandFileArchive( documentFolder );
			reader.close( );

			// get the runnable in the report document
			IReportDocument doc = engine.openReportDocument( documentFolder );
			IReportRunnable report = doc.getReportRunnable( );
			doc.close( );

			// rerun the report based on the report document
			IRunTask task = engine.createRunTask( report );
			task.setDataSource( new FolderArchiveReader( documentFolder ) );
			task.run( documentFolder );
			task.close( );

			// render the generated report document
			doc = engine.openReportDocument( documentFolder );
			long pageCount = doc.getPageCount( );
			for ( int i = 1; i <= pageCount; i++ )
			{
				IRenderTask renderTask = engine.createRenderTask( doc );

				HTMLRenderOption option = new HTMLRenderOption( );
				option.setOutputFormat( "html" );
				option.setOutputStream( new ByteArrayOutputStream( ) );
				renderTask.setRenderOption( option );
				renderTask.setPageNumber( i );

				renderTask.render( );
				List errors = renderTask.getErrors( );
				assertEquals( 0, errors.size( ) );
				renderTask.close( );
			}
			doc.close( );
		}
	}
}
