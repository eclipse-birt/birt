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

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.report.engine.content.MasterPageContent;
import org.eclipse.birt.report.engine.content.PageSequenceContent;
import org.eclipse.birt.report.engine.content.PageSetupContent;
import org.eclipse.birt.report.engine.emitter.IPageSetupEmitter;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.PageSequenceDesign;
import org.eclipse.birt.report.engine.ir.Report;

/**
 * Captures the (report design to) report instance creation logic, by combining the report 
 * design structure and the data. It acts as an entry point to allow actual data to drive 
 * the contents that appears in a report, i.e., header frames use the first data row, 
 * detail rows are repeated for each data row, etc. The output of the executor, for now, 
 * is a specific output format, i.e., HTML, FO or PDF, with the help of the emitter 
 * extensions. <p>
 * 
 * The report instance creation logic is subject to further abstraction, because it
 * is needed in both report generation and report presentation. This is because 
 * report document (not supported for now) does not store each report item instance. As
 * a result, the report item instances need to be created at presentation time too. 
 * For now, report generation and presentation are merged as we do not generate report 
 * documents. The report instance creation logic is therefore run only once. When the 
 * generation and presentation phases are separated, the output of an executor
 * could not only be a specific report output format, but also be a report document. 
 * Data would then come from database in factory engine, and from report document in 
 * the presentation engine. 
 * 
 * @version $Revision: #4 $ $Date: 2005/02/01 $
 */
public class ReportExecutor
{

	// for logging 
	private static Log log = LogFactory.getLog( ReportExecutor.class );
	
	// the report execution context
	private ExecutionContext context;

	/** the emitter to output report in different formats */
	private IReportEmitter emitter;

	// the engine IR visitor object to drive the report execution
	private ReportExecutorVisitor builder;

	/**
	 * constructor
	 *
	 * @param context the executor context
	 * @param emitter the report emitter
	 *            
	 */
	public ReportExecutor( ExecutionContext context, IReportEmitter emitter )
	{
		this.emitter = emitter;
		this.context = context;
		builder = new ReportExecutorVisitor( context, emitter );
	}

	/**
	 * execute the report
	 * 
	 * @throws Exception
	 */
	public void execute( Report report, HashMap paramValues ) throws Exception
	{
		context.setReport( report );

		// Exceute scripts defined in included libraries. For each library, executes
		// first the included scripts, then the initialize method. 
		// The current release does not supported externally included library files 
		// handle global libraries in the future
		
		// execute scripts defined in include-script element of this report
		// Parameters are not available at this stage
		Iterator iter = report.getIncludeScripts().iterator();
		while (iter.hasNext())
		{
			String fileName= (String)iter.next();
			context.loadScript(fileName);
		}
		
		// DE needs to support getInitialize() method 
		// context.execute(report.getInitialize());
		
		// call methods associated with report 
		context.execute(report.getBeforeFactory());
		     		
		// beforeRender is not supported for now
		
		// Set up report parameters
		setParameters( paramValues );
		
		// Prepare necessary data for this report
		context.getDataEngine().prepare(report);

		// Report documents are not supported for now
		// context.execute(report.getBeforeOpenDoc());
		
		emitter.startReport( report );
		
		// report documents are not supported for now
		// context.execute(report.getAfterOpenDoc());
		
		// process page set up information
		handlePageSetup(report);
		context.pushMasterPage( context.getDefaultMasterPage( ) );
		context.pushMasterPage( null );

		// process the report body
		emitter.startBody( );

		//assert ( report.getContentCount( ) >= 1 );
		
		// only top-level elements maybe have the master page reference for now
		for ( int i = 0; i < report.getContentCount( ); i++ )
		{
			builder.startPageFlow( report.getContent( i ).getStyle( )
					.getMasterPage( ) );
			report.getContent( i ).accept( builder );
			builder.endPageFlow( );
		}
		//USED TO FIX BUG 74548
		//FIXME: update the master page handle routines.
		if (report.getContentCount() > 0)
		{
			emitter.getPageSetupEmitter( ).endBody( );
		}
		emitter.endBody( );
		context.popMasterPage( );
		context.popMasterPage( );
		
		// Report document is not supported
		// context.execute(report.getBeforeCloseDoc());
		
		emitter.endReport( );
		// close eport document
		
		// Report document is not supported 
		// context.execute(report.getAfterCloseDoc());
		
		//call afterFactory method of the report
		context.execute(report.getAfterFactory());
		
		context.getDataEngine().shutdown();
	}

	/**
	 * Handles page setup output. This is mainly for presentation phase, and likely
	 * for FO only. Allows page setup definitions to be put into the FO output through
	 * Fo emitter.  
	 * 
     * @param report the report design
     */
    private void handlePageSetup(Report report) {
		// first create the master page defined in the report design
		PageSetupContent pageSetup = new PageSetupContent( report
				.getPageSetup( ) );

		IPageSetupEmitter pageSetupEmitter = emitter.getPageSetupEmitter();	
		pageSetupEmitter.startPageSetup( );
		for ( int i = 0; i < report.getPageSetup( ).getMasterPageCount( ); i++ )
		{
			//	context.initStyleStack( );
			// IMasterPageEmitter mpEmitter = emitter.getMasterPageEmitter( );
			// if ( mpEmitter != null )
			{
				MasterPageDesign masterPage = report.getPageSetup( )
						.getMasterPage( i );
				MasterPageContent masterPageContent = new MasterPageContent(
						masterPage );
				pageSetup.addMasterPage( masterPageContent );
				pageSetupEmitter.startMasterPage( masterPageContent );
				pageSetupEmitter.endMasterPage( );
			}
		}
		
		//then output the page sequence defined in the report define
		for ( int i = 0; i < report.getPageSetup( ).getPageSequenceCount( ); i++ )
		{
			//IPageSetupEmitter psEmitter = emitter.getPageSequenceEmitter( );
			//if ( psEmitter != null )
			{
				PageSequenceDesign psDesign = report.getPageSetup( )
						.getPageSequence( i );
				PageSequenceContent psContent = new PageSequenceContent(
						pageSetup, psDesign );

				pageSetup.addPageSequence( psContent );
				pageSetupEmitter.startPageSequence( psContent );
				pageSetupEmitter.endPageSequence( );
			}
		}
		pageSetupEmitter.endPageSetup( );
		context.setPageSetup( pageSetup );

		setDefaultMasterPage( report );
    }

    /**
	 * sets the default master page/page sequence of the report.
	 * 
	 * @param report entry point for the report design
	 */
	private void setDefaultMasterPage( Report report )
	{
		assert report.getPageSetup( ).getMasterPageCount( ) > 0;

		String defaultMasterPage = report.getPageSetup( ).getMasterPage( 0 )
				.getName( );
		if ( report.getPageSetup( ).getPageSequenceCount( ) > 0 )
		{
			defaultMasterPage = report.getPageSetup( ).getPageSequence( 0 )
					.getName( );
		}
		context.setDefaultMasterPage( defaultMasterPage );
	}

	/**
	 * @param paramValues values for all the report parameters used for report genration
	 */
	protected void setParameters( HashMap paramValues )
	{
		context.getParams().putAll(paramValues);
	}
}
