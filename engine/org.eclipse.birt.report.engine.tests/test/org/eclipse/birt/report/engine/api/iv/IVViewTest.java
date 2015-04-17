
package org.eclipse.birt.report.engine.api.iv;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveView;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ReportEngine;

public class IVViewTest extends EngineCase
{

	static final String ORIGINAL_REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/iv/originalReport.rptdesign";
	static final String ORIGINAL_REPORT_DESIGN = "originalReport.rptdesign";
	static final String CHANGED_REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/iv/changedReport.rptdesign";
	static final String CHANGED_REPORT_DESIGN = "changedReport.rptdesign";
	static final String ORIGINAL_REPORT_DOCUMENT = "originalReport.rptdocument";
	static final String CHANGED_REPORT_DOCUMENT = "changedReport.rptdocument";
	static final String ARCHIVE_VIEW_DOCUMENT = "archiveView.rptdocument";

	IReportEngine engine;

	public void setUp( )
	{
		removeFile( ORIGINAL_REPORT_DESIGN );
		removeFile( CHANGED_REPORT_DESIGN );
		EngineConfig config = new EngineConfig( );
		engine = new ReportEngine( config );
	}

	public void tearDown( )
	{
		removeFile( ORIGINAL_REPORT_DESIGN );
		removeFile( CHANGED_REPORT_DESIGN );
		engine.destroy( );
	}

	public void testRunWithArchiveView( ) throws Exception
	{
		// 1. create document from ORIGINAL_REPORT_DESIGN_RESOURCE first
		copyResource( ORIGINAL_REPORT_DESIGN_RESOURCE, ORIGINAL_REPORT_DESIGN );
		IReportRunnable report = engine
				.openReportDesign( ORIGINAL_REPORT_DESIGN );
		IRunTask task = engine.createRunTask( report );
		try
		{
			task.run( ORIGINAL_REPORT_DOCUMENT );
		}
		finally
		{
			task.close( );
		}

		// 2. create document from CHANGED_REPORT_DESIGN_RESOURCE
		copyResource( CHANGED_REPORT_DESIGN_RESOURCE, CHANGED_REPORT_DESIGN );

		ArchiveView view = new ArchiveView( ARCHIVE_VIEW_DOCUMENT,
				ORIGINAL_REPORT_DOCUMENT, "rw" );

		try
		{
			report = engine.openReportDesign( CHANGED_REPORT_DESIGN );
			task = engine.createRunTask( report );
			try
			{
				// 3. new view archive and render
				ArchiveWriter writer = new ArchiveWriter( view );
				task.setDataSource( new ArchiveReader( view ) );
				task.run( writer );
			}
			finally
			{
				task.close( );
			}

			// 3. create golden report document
			report = engine.openReportDesign( CHANGED_REPORT_DESIGN );
			task = engine.createRunTask( report );
			try
			{
				task.run( CHANGED_REPORT_DOCUMENT );
			}
			finally
			{
				task.close( );
			}
			IReportDocument goldenDocument = engine
					.openReportDocument( CHANGED_REPORT_DOCUMENT );
			try
			{
				IReportDocument ivDocument = engine.openReportDocument( null,
						new ArchiveReader( view ), new HashMap( ) );

				try
				{
					// 5. compare two report document
					compare( goldenDocument, ivDocument );
				}
				finally
				{
					ivDocument.close( );
				}
			}
			finally
			{
				goldenDocument.close( );
			}
		}
		finally
		{
			view.close( );
		}
	}

	protected void compare( IReportDocument src, IReportDocument tgt )
			throws Exception
	{
		assertTrue( src.getPageCount( ) == tgt.getPageCount( ) );
		long pageCount = src.getPageCount( );
		for ( long index = 1; index <= pageCount; index++ )
		{
			String golden = renderPage( src, index );
			String target = renderPage( tgt, index );
			assertEquals( golden, target );
		}
	}

	protected String renderPage( IReportDocument doc, long pageNo )
			throws Exception
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream( );
		assertTrue( pageNo <= doc.getPageCount( ) );
		IRenderTask renderTask = engine.createRenderTask( doc );
		try
		{
			HTMLRenderOption options = new HTMLRenderOption( );
			options.setOutputFormat( "html" );
			options.setOutputStream( buffer );
			renderTask.setRenderOption( options );
			renderTask.setPageNumber( (long) pageNo );
			renderTask.render( );
			List errors = renderTask.getErrors( );
			assertEquals( 0, errors.size( ) );
		}
		finally
		{
			renderTask.close( );
		}
		return new String( buffer.toString( "UTF-8" ) );
	}
}
