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

import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.emitter.DOMBuilderEmitter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
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
 * @version $Revision: 1.36 $ $Date: 2006/05/09 08:41:49 $
 */
public class ReportExecutor implements IReportExecutor
{

	protected static Logger logger = Logger.getLogger( ReportExecutor.class
			.getName( ) );

	// the report execution context
	private ExecutionContext context;
	
	// the manager used to manage the executors.
	private ExecutorManager manager;
	
	private IContentEmitter emitter;
	
	private ReportDesignHandle design;
	
	private Report report;
	
	private IReportContent reportContent;
	
	/**
	 * constructor
	 * 
	 * @param context
	 *            the executor context
	 * @param emitter
	 *            the report emitter
	 * 
	 */
	public ReportExecutor( ExecutionContext context, ReportDesignHandle design, IContentEmitter emitter )
	{
		this.context = context;
		this.manager = new ExecutorManager(context, emitter);
		this.design = design;
		this.emitter = emitter;
	}
	
	public IReportContent execute( )
	{
		report = new ReportParser( context ).parse( design );
		context.setReport( report );
		reportContent = new ReportContent( report );
		context.setReportContent( reportContent );
		TOCBuilder tocBuilder = new TOCBuilder( reportContent.getTOC( ) );
		context.setTOCBuilder( tocBuilder );

		// Prepare necessary data for this report
		context.getDataEngine( ).prepare( report, context.getAppContext( ) );

		if ( emitter != null )
		{
			emitter.start( reportContent );
		}
		
		//prepare to execute the child
		currentItem = 0;
		
		return reportContent;
	}
	
	public void close( )
	{
		if (emitter != null)
		{
			emitter.end( reportContent );
		}
	}
	
	int currentItem;
	public IReportItemExecutor getNextChild( )
	{
		if ( hasNextChild() )
		{
			ReportItemDesign design = report.getContent( currentItem++ );
			ReportItemExecutor executor = manager.createExecutor( null, design );
			return executor;
		}
		return null;
	}

	public boolean hasNextChild( )
	{
		if (currentItem < report.getContentCount( ))
		{
			return true;
		}
		return false;
	}

	/**
	 * execute the report
	 */
	public void execute( ReportDesignHandle report, IContentEmitter emitter )
	{
		execute( );
		while ( hasNextChild( ) )
		{
			if ( context.isCanceled( ) )
			{
				break;
			}
			ReportItemExecutor executor = (ReportItemExecutor) getNextChild( );
			ReportItemDesign design = executor.getDesign( );
			executor.execute( design, emitter );
		}
		close( );
	}

	
	public IPageContent executeMasterPage( int pageNo,
			MasterPageDesign masterPage )
	{
		IReportContent reportContent = context.getReportContent( );
		IPageContent pageContent = reportContent.createPageContent( );
		
		pageContent.setGenerateBy( masterPage );
		pageContent.setPageNumber( pageNo );
		context.setPageNumber( pageNo );
		
		if ( masterPage instanceof SimpleMasterPageDesign )
		{
			// disable the tocBuilder
			TOCBuilder tocBuilder = context.getTOCBuilder( );
			context.setTOCBuilder( null );
			SimpleMasterPageDesign pageDesign = (SimpleMasterPageDesign) masterPage;
			InstanceID iid = new InstanceID( null, pageDesign.getID( ), null );
			pageContent.setInstanceID( iid );
			
			//creat header, footer and body
			IContent header = reportContent.createContainerContent( ) ;
			header.setStyleClass(masterPage.getStyleName( ));
			pageContent.setPageHeader( header );
			header.setParent( pageContent );
			IContentEmitter domEmitter = new DOMBuilderEmitter( header);
			
			ExecutorManager manager = new ExecutorManager( context, domEmitter);
			for ( int i = 0; i < pageDesign.getHeaderCount( ); i++ )
			{
				ReportItemDesign design = pageDesign.getHeader( i );
				ReportItemExecutor executor = manager.createExecutor( null, design );
				executor.execute( design, domEmitter );
			}
			
			//create body
			IContent body = reportContent.createContainerContent( ) ;
			body.setStyleClass(masterPage.getBodyStyleName( ));
			pageContent.setPageBody( body );
			body.setParent( pageContent );


			//create footer
			IContent footer = reportContent.createContainerContent( ) ;
			footer.setStyleClass(masterPage.getStyleName( ));
			pageContent.setPageFooter( footer );
			footer.setParent( pageContent );

			domEmitter = new DOMBuilderEmitter( footer);
			manager = new ExecutorManager( context, domEmitter);
			for ( int i = 0; i < pageDesign.getFooterCount( ); i++ )
			{
				ReportItemDesign design = pageDesign.getFooter( i );
				ReportItemExecutor executor = manager.createExecutor( null, design );
				executor.execute( design, domEmitter );
			}
			// reenable the TOC
			context.setTOCBuilder( tocBuilder );
		}
		return pageContent;
	}

	public ExecutionContext getContext( )
	{
		return this.context;
	}
	
	public ExecutorManager getManager()
	{
		return this.manager;
	}

	public IPageContent createPage( long pageNumber, MasterPageDesign pageDesign )
	{
		return null;
	}
}
