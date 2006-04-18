/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.util.BirtTimer;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * Captures the (report design to) report instance creation logic, by combining
 * the report design structure and the data. It acts as an entry point to allow
 * actual data to drive the contents that appears in a report, i.e., header
 * frames use the first data row, detail rows are repeated for each data row,
 * etc. The output of the executor, for now, is a specific output format, i.e.,
 * HTML, FO or PDF, with the help of the emitter extensions.
 * <p>
 * 
 * The report instance creation logic is subject to further abstraction, because
 * it is needed in both report generation and report presentation. This is
 * because report document (not supported for now) does not store each report
 * item instance. As a result, the report item instances need to be created at
 * presentation time too. For now, report generation and presentation are merged
 * as we do not generate report documents. The report instance creation logic is
 * therefore run only once. When the generation and presentation phases are
 * separated, the output of an executor could not only be a specific report
 * output format, but also be a report document. Data would then come from
 * database in factory engine, and from report document in the presentation
 * engine.
 * 
 * @version $Revision: 1.34 $ $Date: 2006/03/23 09:28:37 $
 */
public class ReportExecutor
{

	protected static Logger logger = Logger.getLogger( ReportExecutor.class
			.getName( ) );

	// the report execution context
	private ExecutionContext context;

	// the engine IR visitor object to drive the report execution
	private ReportExecutorVisitor builder;

	/**
	 * constructor
	 * 
	 * @param context
	 *            the executor context
	 * @param emitter
	 *            the report emitter
	 * 
	 */
	public ReportExecutor( ExecutionContext context )
	{
		this.context = context;
		builder = new ReportExecutorVisitor( context );
	}

	public void execute( ReportDesignHandle reportDesign,
			IContentEmitter emitter )
	{
		Report report = new ReportParser( ).parse( reportDesign );
		execute( report, emitter );
	}

	/**
	 * execute the report
	 */
	public void execute( Report report, IContentEmitter emitter )
	{
		BirtTimer timer = new BirtTimer( );
		context.setReport( report );
		IReportContent reportContent = ContentFactory
				.createReportContent( report );
		context.setReportContent( reportContent );
		TOCBuilder tocBuilder = new TOCBuilder( reportContent.getTOC( ) );
		context.setTOCBuilder( tocBuilder );

		// Prepare necessary data for this report
		timer.restart( );
		context.getDataEngine( ).prepare( report, context.getAppContext( ) );
		timer.stop( );
		timer.logTimeTaken( logger, Level.FINE, context.getTaskIDString( ),
				"Prepare report queries" ); // $NON-NLS-1$

		timer.restart( );
		if ( emitter != null )
		{
			emitter.start( reportContent );
		}
		IContent dummyReportContent = new LabelContent((ReportContent)reportContent);
		dummyReportContent.setStyleClass(report.getRootStyleName());
		context.pushContent(dummyReportContent);
		// only top-level elements maybe have the master page reference for now
		if ( report.getContentCount( ) > 0 )
		{
			for ( int i = 0; i < report.getContentCount( ); i++ )
			{
				if ( context.isCanceled( ) )
				{
					break;
				}
				report.getContent( i ).accept( builder, emitter );
			}
		}
		context.popContent();

		if ( emitter != null )
		{
			emitter.end( reportContent );
		}
		timer.stop( );
		timer.logTimeTaken( logger, Level.FINE, context.getTaskIDString( ),
				"Running and rendering report" ); // $NON-NLS-1$
	}

	public IPageContent executeMasterPage( int pageNo,
			MasterPageDesign masterPage )
	{
		IReportContent reportContent = context.getReportContent( );
		IPageContent pageContent = reportContent.createPageContent( );
		pageContent.setGenerateBy( masterPage );
		pageContent.setPageNumber( pageNo );

		context.setPageNumber( pageNo );
		context.pushContent(pageContent);
		if ( masterPage instanceof SimpleMasterPageDesign )
		{
			// disable the tocBuilder
			TOCBuilder tocBuilder = context.getTOCBuilder( );
			context.setTOCBuilder( null );
			SimpleMasterPageDesign pageDesign = (SimpleMasterPageDesign) masterPage;
			InstanceID iid = new InstanceID( null, pageDesign.getID( ), null );
			pageContent.setInstanceID( iid );
			
			context.pushContent( pageContent.getPageHeader( ) );
			for ( int i = 0; i < pageDesign.getHeaderCount( ); i++ )
			{
				pageDesign.getHeader( i ).accept( builder,
						new PageContentBuilder( pageContent.getHeader( ) ) );
			}
			context.popContent( );

			context.pushContent( pageContent.getPageFooter( ) );
			for ( int i = 0; i < pageDesign.getFooterCount( ); i++ )
			{
				pageDesign.getFooter( i ).accept( builder,
						new PageContentBuilder( pageContent.getFooter( ) ) );
			}
			context.popContent( );
			// reenable the TOC
			context.setTOCBuilder( tocBuilder );
		}
		context.popContent();
		return pageContent;
	}

	protected static class PageContentBuilder extends ContentEmitterAdapter
	{

		List contents;
		IContent parent;

		public PageContentBuilder( List contents )
		{
			this.contents = contents;
			this.parent = null;
		}

		public void startContent( IContent content )
		{
			if ( parent == null )
			{
				contents.add( content );
			}
			else
			{
				parent.getChildren( ).add( content );
			}
			parent = content;
		}

		public void endContent( IContent content )
		{
			if ( parent != null )
			{
				parent = (IContent) parent.getParent( );
			}
		}

	}

	public ExecutionContext getContext( )
	{
		return this.context;
	}

}
