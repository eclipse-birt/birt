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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.BirtTimer;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IPageSequenceContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.MasterPageContent;
import org.eclipse.birt.report.engine.content.impl.PageSetupContent;
import org.eclipse.birt.report.engine.emitter.IPageSetupEmitter;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.executor.ExecutionContext.ElementExceptionInfo;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.PageSequenceDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.TextItemDesign;

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
 * @version $Revision: 1.18 $ $Date: 2005/07/05 09:12:49 $
 */
public class ReportExecutor
{
	protected static Logger logger = Logger.getLogger( ReportExecutor.class.getName( ) );
	
	// the report execution context
	private ExecutionContext context;

	/** the emitter to output report in different formats */
	private IReportEmitter emitter;

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
	public ReportExecutor( ExecutionContext context, IReportEmitter emitter )
	{
		this.emitter = emitter;
		this.context = context;
		builder = new ReportExecutorVisitor( context, emitter );
	}

	/**
	 * execute the report
	 */
	public void execute( Report report, HashMap paramValues )
	{
		BirtTimer timer = new BirtTimer();
		timer.start();
		
		IReportContent reportContent = ContentFactory.createReportContent( report, null );
		context.pushContentObject( reportContent );
		context.setReport( report );

		// Set up report parameters
		setParameters( paramValues );

		// Exceute scripts defined in included libraries. For each library,
		// executes
		// first the included scripts, then the initialize method.
		// The current release does not supported externally included library
		// files
		// handle global libraries in the future

		// execute scripts defined in include-script element of this report
		Iterator iter = report.getIncludeScripts( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String fileName = (String) iter.next( );
			context.loadScript( fileName );
		}
		
		// DE needs to support getInitialize() method
		 context.execute(report.getInitialize());

		// call methods associated with report
		context.execute( report.getBeforeFactory( ) );

		// beforeRender is not supported for now

		timer.stop();
		timer.logTimeTaken(logger, Level.FINE, context.getTaskIDString(), "Prepare to run report");	// $NON-NLS-1$
		
		// Prepare necessary data for this report
		timer.restart();
		context.getDataEngine( ).prepare( report, context.getAppContext() );
		timer.stop();
		timer.logTimeTaken(logger, Level.FINE, context.getTaskIDString(), "Prepare report queries");	// $NON-NLS-1$
		
		// Report documents are not supported for now
		// context.execute(report.getBeforeOpenDoc());

		timer.restart();
		emitter.startReport( reportContent );

		// report documents are not supported for now
		// context.execute(report.getAfterOpenDoc());

		// process page set up information
		handlePageSetup( report );
		context.pushMasterPage( context.getDefaultMasterPage( ) );
		context.pushMasterPage( null );

		// process the report body
		emitter.startBody( );

		//assert ( report.getContentCount( ) >= 1 );

		// only top-level elements maybe have the master page reference for now
		if ( report.getContentCount( ) > 0 )
		{
			for ( int i = 0; i < report.getContentCount( ); i++ )
			{
				builder.startPageFlow( report.getContent( i ).getStyle( )
						.getMasterPage( ) );
				report.getContent( i ).accept( builder );
				builder.endPageFlow( );
			}
		}
		else
		{
			builder.startPageFlow( null );
			builder.endPageFlow( );
		}
		//Outputs the error message at the end of the report
		if ( context.getMsgLst( ).size( ) > 0 )
		{
			TextItemDesign errText = new TextItemDesign( );
			errText.setTextType( "html" );//$NON-NLS-1$
			StringBuffer errHtmlMsg = new StringBuffer(
					"<hr style=\"color:red\"/>" );//$NON-NLS-1$
			errHtmlMsg
					.append( "<div style=\"color:red\"><div>" + 
							EngineResourceHandle.getInstance().
								getMessage( MessageConstants.ERRORS_ON_REPORT_PAGE ) + "</div>"  );//$NON-NLS-1$
						
			HashMap errLst = context.getMsgLst();
			Iterator it = errLst.values().iterator();
			int index = 0; 
			while(it.hasNext())
			{
				appendErrorMessage(index++, errHtmlMsg, (ElementExceptionInfo)it.next());
			}
			errHtmlMsg.append("</div>");
			errText.setText( null, errHtmlMsg.toString( ) );
			builder.startPageFlow( null );
			errText.accept( builder );
			builder.endPageFlow( );
		}
		
		//USED TO FIX BUG 74548
		//FIXME: update the master page handle routines.
		emitter.getPageSetupEmitter( ).endBody( );
		
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
		context.popContentObject( );
		context.execute( report.getAfterFactory( ) );
		context.getDataEngine( ).shutdown( );
		
		timer.stop();
		timer.logTimeTaken(logger, Level.FINE, context.getTaskIDString(), "Running and rendering report");	// $NON-NLS-1$

	}

	/**
	 * Handles page setup output. This is mainly for presentation phase, and
	 * likely for FO only. Allows page setup definitions to be put into the FO
	 * output through Fo emitter.
	 * 
	 * @param report
	 *            the report design
	 */
	private void handlePageSetup( Report report )
	{
		// first create the master page defined in the report design
		PageSetupContent pageSetup = (PageSetupContent)ContentFactory
				.createPageSetupContent( report.getPageSetup( ) );

		IPageSetupEmitter pageSetupEmitter = emitter.getPageSetupEmitter( );
		if ( pageSetupEmitter == null )
		{
			return;
		}
		pageSetupEmitter.startPageSetup( );
		for ( int i = 0; i < report.getPageSetup( ).getMasterPageCount( ); i++ )
		{
			//	context.initStyleStack( );
			// IMasterPageEmitter mpEmitter = emitter.getMasterPageEmitter( );
			// if ( mpEmitter != null )
			{
				MasterPageDesign masterPage = report.getPageSetup( )
						.getMasterPage( i );
				MasterPageContent masterPageContent = (MasterPageContent)ContentFactory
						.createMasterPageContent( masterPage );
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
				IPageSequenceContent psContent = ContentFactory
						.createPageSequenceContent( pageSetup, psDesign );

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
	 * @param report
	 *            entry point for the report design
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
	 * @param paramValues
	 *            values for all the report parameters used for report genration
	 */
	protected void setParameters( HashMap paramValues )
	{
		context.getParams( ).putAll( paramValues );
	}
	
	private void appendErrorMessage(int index, StringBuffer errMsg, ElementExceptionInfo info)
	{
		errMsg.append("<div><span id=\"error_icon" + index +"\"  style=\"cursor:pointer\" onclick=\"expand(" + index +  ")\" > + </span>");
		errMsg.append("<span  id=\"error_title\">There are error(s) in "+ info.getType() +":" + info.getElementInfo() + "</span>");
		errMsg.append("<pre id=\"error_detail" + index+ "\" style=\"display:block\" >");
		ArrayList errorList = info.getErrorList();
		ArrayList countList = info.getCountList();
		for(int i=0; i<errorList.size(); i++)
		{
			BirtException ex = (BirtException)errorList.get(i);
			int count = ((Integer)countList.get(i)).intValue();
			if(count==1)
			{
				errMsg.append("Error" + i + " : "+ ex.getErrorCode() + "(1 time)"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else
			{
				errMsg.append("Error" + i + " : " + ex.getErrorCode() + "(" + count + "times)"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			errMsg.append((char) Character.LINE_SEPARATOR);
			errMsg.append("detail: " + getDetailMessage(ex)); //$NON-NLS-1$
		}
		errMsg.append("</pre></div>"); //$NON-NLS-1$
	}
	
	private String getDetailMessage(Throwable t)
	{
		StringBuffer detailMsg = new StringBuffer();
		do
		{
			detailMsg.append( t.getLocalizedMessage( ));
			detailMsg.append( (char) Character.LINE_SEPARATOR );
			t = t.getCause( );
		} while (  t != null );
		
		return detailMsg.toString();
	}
}