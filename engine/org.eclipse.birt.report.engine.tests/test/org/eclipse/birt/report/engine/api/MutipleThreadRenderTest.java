
package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.RunnableMonitor;

public class MutipleThreadRenderTest extends EngineCase
{

	final static String REPORT_DOCUMENT = "./utest/report.rptdocument";
	final static String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/mutiple-thread-render.rptdesign";
	final static String REPORT_DESIGN = "./report.rptdesign";

	IReportEngine engine;

	public void setUp( )
	{
		removeFile( REPORT_DOCUMENT );
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		EngineConfig config = new EngineConfig( );
		engine = new ReportEngine( config );
	}

	public void tearDown( )
	{
		engine.shutdown( );
		removeFile( REPORT_DOCUMENT );
		removeFile( REPORT_DESIGN );
	}

	public void testMutipleThread( ) throws Exception
	{
		RunnableMonitor monitor = new RunnableMonitor( );
		new CreateDocument( monitor );
		for ( int i = 0; i < 2; i++ )
		{
			new RenderEachPageToHTML( monitor );
		}
		for ( int i = 0; i < 2; i++ )
		{
			new RenderEachPageToPDF( monitor );
		}
		for ( int i = 0; i < 2; i++ )
		{
			new RenderFullToHTML( monitor );
		}
		for ( int i = 0; i < 2; i++ )
		{
			new RenderFullToPDF( monitor );
		}
		monitor.start( );
		monitor.printStackTrace( );
		assertTrue( monitor.getFailedRunnables( ).isEmpty( ) );
	}

	class CreateDocument extends RunnableMonitor.Runnable
	{

		CreateDocument( RunnableMonitor monitor )
		{
			super( monitor );
		}

		public void doRun( ) throws Exception
		{
			System.out.println( "start run document" );
			IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
			IRunTask task = engine.createRunTask( report );
			try
			{
				task.run( REPORT_DOCUMENT );
			}
			finally
			{
				task.close( );
			}

			System.out.println( "end run document" );
		}
	}

	class RenderEachPageToHTML extends RunnableMonitor.Runnable
	{

		RenderEachPageToHTML( RunnableMonitor monitor )
		{
			super( monitor );
		}

		public void doRun( ) throws Exception
		{
			IReportDocument document = null;

			try
			{
				while ( document == null )
				{
					try
					{
						document = engine.openReportDocument( REPORT_DOCUMENT );
					}
					catch ( Exception ex )
					{
						System.out.println( "sleep 500 to reopen..." );
						sleep( 500 );
					}
				}

				long startPage = 1;

				while ( true )
				{
					long endPage = document.getPageCount( );
					while ( startPage <= endPage )
					{
						System.out.println( "render page " + startPage + " / "
								+ endPage );
						IRenderTask task = engine.createRenderTask( document );
						try
						{
							HTMLRenderOption options = new HTMLRenderOption( );
							options.setOutputFormat( "html" );
							task.setRenderOption( options );
							task.setPageNumber( startPage );
							task.render( );
							startPage++;
						}
						finally
						{
							task.close( );
						}
					}
					if ( document.isComplete( ) == false )
					{
						sleep( 1000 );
						document.refresh( );
					}
					else
					{
						break;
					}
				}
			}
			finally
			{
				if ( document != null )
				{
					document.close( );
				}
			}
		}
	}

	class RenderEachPageToPDF extends RunnableMonitor.Runnable
	{

		RenderEachPageToPDF( RunnableMonitor monitor )
		{
			super( monitor );
		}

		public void doRun( ) throws Exception
		{
			IReportDocument document = null;
			try
			{
				while ( document == null )
				{
					try
					{
						document = engine.openReportDocument( REPORT_DOCUMENT );
					}
					catch ( Exception ex )
					{
						System.out.println( "sleep 500 to reopen..." );
						sleep( 500 );
					}
				}

				long startPage = 1;

				while ( true )
				{
					long endPage = document.getPageCount( );
					while ( startPage <= endPage )
					{
						System.out.println( "render page " + startPage + " / "
								+ endPage );
						IRenderTask task = engine.createRenderTask( document );
						try
						{
							HTMLRenderOption options = new HTMLRenderOption( );
							options.setOutputFormat( "PDF" );
							task.setRenderOption( options );
							task.setPageNumber( startPage );
							task.render( );
							startPage++;
						}
						finally
						{
							task.close( );
						}
					}
					if ( document.isComplete( ) == false )
					{
						sleep( 1000 );
						document.refresh( );
					}
					else
					{
						break;
					}
				}
			}
			finally
			{
				if ( document != null )
				{
					document.close( );
				}
			}
		}
	}

	class RenderFullToHTML extends RunnableMonitor.Runnable
	{

		RenderFullToHTML( RunnableMonitor monitor )
		{
			super( monitor );
		}

		public void doRun( ) throws Exception
		{
			IReportDocument document = null;
			try
			{
				while ( document == null )
				{
					try
					{
						document = engine.openReportDocument( REPORT_DOCUMENT );
					}
					catch ( Exception ex )
					{
						System.out.println( "sleep 500 to reopen..." );
						sleep( 500 );
					}
				}

				while ( document.isComplete( ) == false )
				{
					sleep( 1000 );
					document.refresh( );
				}

				System.out.println( "render full document to HTML " );
				IRenderTask renderTask = engine.createRenderTask( document );
				HTMLRenderOption options = new HTMLRenderOption( );
				options.setOutputFormat( "html" );
				renderTask.setRenderOption( options );
				renderTask.render( );
				renderTask.close( );
				System.out.println( "render full document to PDF: succeed " );
			}
			finally
			{
				if ( document != null )
				{
					document.close( );
				}
			}
		}
	}

	class RenderFullToPDF extends RunnableMonitor.Runnable
	{

		RenderFullToPDF( RunnableMonitor monitor )
		{
			super( monitor );
		}

		public void doRun( ) throws Exception
		{
			IReportDocument document = null;
			try
			{
				while ( document == null )
				{
					try
					{
						document = engine.openReportDocument( REPORT_DOCUMENT );
					}
					catch ( Exception ex )
					{
						System.out.println( "sleep 500 to reopen..." );
						sleep( 500 );
					}
				}

				while ( document.isComplete( ) == false )
				{
					sleep( 1000 );
					document.refresh( );
				}

				System.out.println( "render full document to PDF " );
				IRenderTask renderTask = engine.createRenderTask( document );
				HTMLRenderOption options = new HTMLRenderOption( );
				options.setOutputFormat( "PDF" );
				renderTask.setRenderOption( options );
				renderTask.render( );
				renderTask.close( );
				System.out.println( "render full document to PDF: succeed " );
			}
			finally
			{
				if ( document != null )
				{
					document.close( );
				}
			}
		}
	}
}
