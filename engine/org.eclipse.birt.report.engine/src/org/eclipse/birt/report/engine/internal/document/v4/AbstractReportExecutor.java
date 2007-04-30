
package org.eclipse.birt.report.engine.internal.document.v4;

import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.DOMBuilderEmitter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.PageHintReader;
import org.eclipse.birt.report.engine.internal.document.v3.CachedReportContentReaderV3;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.toc.DocumentTOCTree;
import org.eclipse.birt.report.engine.toc.TOCTree;

abstract public class AbstractReportExecutor implements IReportExecutor
{

	protected ExecutionContext context;
	protected IDataEngine dataEngine;
	/**
	 * the reader used to read report body.
	 */
	protected CachedReportContentReaderV3 reader;

	/**
	 * reader used to read the page hints
	 */
	protected PageHintReader hintsReader;
	/**
	 * reader used to read the master page
	 */
	protected CachedReportContentReaderV3 pageReader;

	protected Report report;
	protected ReportContent reportContent;

	protected long uniqueId;

	protected ExecutorManager manager;

	AbstractReportExecutor( ExecutionContext context ) throws IOException
	{
		assert context.getDesign( ) != null;
		assert context.getReportDocument( ) != null;

		this.manager = new ExecutorManager( this );

		this.context = context;

		report = context.getReport( );

		reportContent = (ReportContent) ContentFactory
				.createReportContent( report );
		context.setReportContent( reportContent );

		IReportDocument reportDoc = context.getReportDocument( );

		TOCTree tocTree = new DocumentTOCTree( reportDoc );
		reportContent.setTOCTree( tocTree );

		long totalPage = reportDoc.getPageCount( );
		context.setTotalPage( totalPage );
		reportContent.setTotalPage( totalPage );

		dataEngine = context.getDataEngine( );
		dataEngine.prepare( report, context.getAppContext( ) );

		try
		{
			IDocArchiveReader archive = reportDoc.getArchive( );
			RAInputStream in = archive
					.getStream( ReportDocumentConstants.CONTENT_STREAM );
			reader = new CachedReportContentReaderV3( reportContent, in );
			in = archive.getStream( ReportDocumentConstants.PAGE_STREAM );
			pageReader = new CachedReportContentReaderV3( reportContent, in );
			hintsReader = new PageHintReader( reportDoc );
		}
		catch ( IOException ex )
		{
			close( );
			throw ex;
		}
	}

	public void close( )
	{
		if ( reader != null )
		{
			reader.close( );
			reader = null;
		}
		if ( pageReader != null )
		{
			pageReader.close( );
			pageReader = null;
		}

		if ( hintsReader != null )
		{
			hintsReader.close( );
			hintsReader = null;
		}
	}

	public IReportContent execute( )
	{
		return reportContent;
	}

	protected void executeAll( IReportItemExecutor executor,
			IContentEmitter emitter )
	{
		while ( executor.hasNextChild( ) )
		{
			if ( context.isCanceled( ) )
			{
				break;
			}
			IReportItemExecutor childExecutor = executor.getNextChild( );
			if ( childExecutor != null )
			{
				IContent content = childExecutor.execute( );
				if ( content != null )
				{
					ContentEmitterUtil.startContent( content, emitter );
				}
				executeAll( childExecutor, emitter );
				if ( content != null )
				{
					ContentEmitterUtil.endContent( content, emitter );
				}
				childExecutor.close( );
			}
		}
	}

	public IReportItemExecutor createPageExecutor( long pageNumber,
			MasterPageDesign pageDesign )
	{
		return new MasterPageExecutor( manager, pageNumber );
	}

	public IPageContent createPage( long pageNumber, MasterPageDesign pageDesign )
	{
		IReportItemExecutor pageExecutor = createPageExecutor( pageNumber,
				pageDesign );
		IPageContent pageContent = (IPageContent) pageExecutor.execute( );
		IContentEmitter domEmitter = new DOMBuilderEmitter( pageContent );
		executeAll( pageExecutor, domEmitter );
		pageExecutor.close( );
		return pageContent;
	}

}
