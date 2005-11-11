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
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.DrillThroughActionDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * Abstract class, Represents a report item executor. Report item executor
 * execute a report item design, generate a report item instance, and pass the
 * instance to <code>emitter</code>.
 * <p>
 * According to the report item design and current context information, executor
 * calculate expression in report item design, get data instance from data
 * source, and fill it into the report item instance, and set property for the
 * report item instance.
 * <p>
 * Reset the state of report item executor by calling <code>reset()</code>
 * 
 * @version $Revision: 1.10 $ $Date: 2005/11/10 08:55:18 $
 */
public abstract class ReportItemExecutor
{

	/**
	 * the logger, log info, debug, and error message
	 */
	protected static Logger logger = Logger.getLogger( ReportItemExecutor.class
			.getName( ) );

	/**
	 * the report content
	 */
	protected IReportContent report;

	/**
	 * the executor context
	 */
	protected ExecutionContext context;

	/**
	 * the executor visitor
	 */
	protected IReportItemVisitor visitor;

	/**
	 * construct a report item executor by giving execution context and report
	 * executor visitor
	 * 
	 * @param context
	 *            the executor context
	 * @param visitor
	 *            the report executor visitor
	 */
	protected ReportItemExecutor( ExecutionContext context,
			IReportItemVisitor visitor )
	{
		
		this.context = context;
		this.visitor = visitor;
		this.report = context.getReportContent();
	}

	/**
	 * Execute a report item design and transfer report instance to emitter by
	 * calling this method.
	 * <p>
	 * According to the report item design and current context information,
	 * executor calculate expression in report item design, get data instance
	 * from data source, and fill it into the report item instance, and set
	 * property for the report item instance. At last pass the instance to
	 * <code>emitter</code>
	 * 
	 * @param item
	 *            the report item design
	 * @param emitter
	 *            the report emitter
	 */
	public abstract void execute( ReportItemDesign item, IContentEmitter emitter );
	
	/**
	 * reset the state of the report item executor. This operation will reset
	 * all property of this object
	 * 
	 */
	public void reset( )
	{
	}


	/**
	 * Calculate the bookmark value which is set to
	 * <code>ReportItemContent</code> if the bookmark is not null
	 * 
	 * @param item
	 *            the ReportItemContent object
	 */
	protected void processBookmark( ReportItemDesign item, IContent itemContent )
	{
		Expression bookmark = item.getBookmark( );
		if ( bookmark != null )
		{
			Object tmp = context.evaluate( bookmark );
			if ( tmp != null )
			{
				itemContent.setBookmark( tmp.toString( ) );
			}
		}
		Expression toc = item.getTOC();
		if (toc != null)
		{
			Object tmp = context.evaluate(toc);
			if (tmp != null)
			{
				itemContent.setTOC(tmp.toString());
			}
			
		}
	}

	/**
	 * Calculate the action value which is set to <code>ReportItemContent</code>
	 * if the action is not null.
	 * 
	 * @param action
	 *            the action design object
	 * @param itemContent
	 *            create report item content object
	 */
	protected void processAction( ReportItemDesign item, IContent itemContent )
	{
		assert itemContent != null;

		ActionDesign action = item.getAction( );
		if ( action != null )
		{

			switch ( action.getActionType( ) )
			{
				case ActionDesign.ACTION_HYPERLINK :
					assert action.getHyperlink( ) != null;
					Object value = context.evaluate( action.getHyperlink( ) );
					if ( value != null )
					{
						IHyperlinkAction obj = report.createActionContent( );
						obj.setHyperlink( value.toString( ), action
								.getTargetWindow( ) );
						itemContent.setHyperlinkAction( obj );
					}
					break;
				case ActionDesign.ACTION_BOOKMARK :
					assert action.getBookmark( ) != null;
					value = context.evaluate( action.getBookmark( ) );
					if ( value != null )
					{
						IHyperlinkAction obj = report.createActionContent( );
						obj.setBookmark( value.toString( ) );
						itemContent.setHyperlinkAction( obj );
					}
					break;
				case ActionDesign.ACTION_DRILLTHROUGH :
					assert action.getDrillThrough( ) != null;
					DrillThroughActionDesign drill = action.getDrillThrough( );
					value = context.evaluate( drill.getBookmark( ) );
					String bookmark = null;
					if ( value != null && value instanceof String )
					{
						bookmark = value.toString( );
					}
					Iterator paramsDesignIte = drill.getParameters( )
							.entrySet( ).iterator( );
					Map paramsVal = new HashMap( );
					while ( paramsDesignIte.hasNext( ) )
					{
						Map.Entry entry = (Map.Entry) paramsDesignIte.next( );
						paramsVal.put( entry.getKey( ), context
								.evaluate( (Expression) entry.getValue( ) ) );
					}
					// XXX Do not support Search criteria
					IHyperlinkAction obj = report.

					createActionContent( );
					obj.setDrillThrough( bookmark, drill.getReportName( ),
							paramsVal, null, action.getTargetWindow( ) );

					itemContent.setHyperlinkAction( obj );
					break;
				default :
					assert false;
			}

		}
	}
	

	protected DataID getDataID()
	{
		return null;
	}
	
	protected void initializeContent(IContent parent, ReportElementDesign design, IContent content)
	{
		InstanceID pid = null;
		if (parent != null)
		{
			pid = parent.getInstanceID();
		}
		InstanceID id = new InstanceID(pid, design == null ? -1 : design.getID(), getDataID());
		content.setInstanceID(id);
		content.setGenerateBy( design );
		content.setParent( parent );
	}
	
}