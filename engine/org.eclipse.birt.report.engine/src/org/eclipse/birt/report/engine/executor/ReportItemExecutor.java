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

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DrillThroughActionDesign;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.VisibilityDesign;
import org.eclipse.birt.report.engine.ir.VisibilityRuleDesign;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

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
 * @version $Revision: 1.32 $ $Date: 2006/05/17 01:27:55 $
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
		this.report = context.getReportContent( );
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
		String bookmark = item.getBookmark( );
		if ( bookmark != null )
		{
			Object tmp = context.evaluate( bookmark );
			if ( tmp != null )
			{
				itemContent.setBookmark( tmp.toString( ) );
			}
		}
		String toc = item.getTOC( );
		if ( toc != null )
		{
			Object tmp = context.evaluate( toc );
			if ( tmp != null )
			{
				String tocLabel = "";
				if (tmp instanceof Number)
				{
					NumberFormatter fmt = context.getNumberFormatter( null );
					tocLabel = fmt.format( (Number) tmp );
				}
				else if (tmp instanceof Date)
				{
					DateFormatter fmt = context.getDateFormatter( null);
					tocLabel = fmt.format( (Date) tmp );
				}
				else if (tmp instanceof String)
				{
					tocLabel = (String)tmp;
				}
				else
				{
					tocLabel = tmp.toString();
				}
				itemContent.setTOC( tocLabel );
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
						obj.setBookmarkType( action.isBookmark( ) );
						itemContent.setHyperlinkAction( obj );
					}
					break;
				case ActionDesign.ACTION_DRILLTHROUGH :
					assert action.getDrillThrough( ) != null;
					DrillThroughActionDesign drill = action.getDrillThrough( );
					String bookmark = null;
					String bookmarkExpr = drill.getBookmark( );
					if ( bookmarkExpr != null )
					{
						value = context.evaluate( drill.getBookmark( ) );
						if ( value != null )
						{
							bookmark = value.toString( );
						}
					}
					boolean isBookmark = drill.isBookmark( );
					Map paramsVal = new HashMap( );
					Map params = drill.getParameters( );
					if ( params != null )
					{
						Iterator paramsDesignIte = params.entrySet( )
								.iterator( );
						while ( paramsDesignIte.hasNext( ) )
						{
							Map.Entry entry = (Map.Entry) paramsDesignIte
									.next( );
							Object valueObj = entry.getValue( );
							Object paramValue = null;
							if ( valueObj != null )
							{
								String valueExpr = valueObj.toString( );
								paramValue = context.evaluate( valueExpr );
							}
							paramsVal.put( entry.getKey( ), paramValue );
						}
					}

					String reportName = drill.getReportName( );
					ReportDesignHandle design = context.getDesign( );
					if ( design != null )
					{
						URL reportURL = design.findResource( reportName,
								IResourceLocator.LIBRARY );
						if ( reportURL != null )
						{
							String reportFile = reportURL.getFile( );
							if ( reportFile != null )
							{
								reportName = reportFile;
							}
						}
					}
					String format = drill.getFormat( );
					// XXX Do not support Search criteria
					IHyperlinkAction obj = report.createActionContent( );
					obj.setDrillThrough( bookmark, isBookmark, reportName, paramsVal, null,
							action.getTargetWindow( ), format );

					itemContent.setHyperlinkAction( obj );
					break;
				default :
					assert false;
			}

		}
	}

	/**
	 * Sets the visibility property for ReportItem.
	 * 
	 * @param design
	 *            The <code>ReportItemDesign</code> object.
	 * @param content
	 *            The <code>ReportItemContent</code> object.
	 */
	protected void processVisibility( ReportItemDesign design, IContent content )
	{
		VisibilityDesign visibility = design.getVisibility( );
		boolean isFirst = true;
		if ( visibility != null )
		{
			StringBuffer buffer = new StringBuffer( );
			for ( int i = 0; i < visibility.count( ); i++ )
			{
				VisibilityRuleDesign rule = visibility.getRule( i );
				String expr = rule.getExpression( );
				Object result = null;
				if ( expr != null )
				{
					result = context.evaluate( expr );
				}
				if ( result == null || !( result instanceof Boolean ) )
				{
					logger
							.log(
									Level.WARNING,
									"The following visibility expression does not evaluate to a legal boolean value: {0}", //$NON-NLS-1$
									rule.getExpression( ) );
					continue;
				}
				boolean isHidden = ( (Boolean) result ).booleanValue( );
				// The report element appears by default and if the result is
				// not hidden, then ignore it.
				if ( !isHidden )
				{
					continue;
				}
				// we should use rule as the string as
				if ( isFirst )
				{
					isFirst = false;
				}
				else
				{
					buffer.append( ", " ); //$NON-NLS-1$
				}
				buffer.append( rule.getFormat( ) );
			}
			content.getStyle( ).setVisibleFormat( buffer.toString( ) );
		}
	}
	
	/**
	 * Sets the visibility property for column.
	 */
	protected void processColumnVisibility( ColumnDesign design, Column column )
	{
		VisibilityDesign visibility = design.getVisibility( );
		boolean isFirst = true;
		if ( visibility != null )
		{
			StringBuffer buffer = new StringBuffer( );
			for ( int i = 0; i < visibility.count( ); i++ )
			{
				VisibilityRuleDesign rule = visibility.getRule( i );
				String expr = rule.getExpression( );
				Object result = null;
				if ( expr != null )
				{
					result = context.evaluate( expr );
				}
				if ( result == null || !( result instanceof Boolean ) )
				{
					logger
							.log(
									Level.WARNING,
									"The following visibility expression does not evaluate to a legal boolean value: {0}", //$NON-NLS-1$
									rule.getExpression( ) );
					continue;
				}
				boolean isHidden = ( (Boolean) result ).booleanValue( );
				// The report element appears by default and if the result is
				// not hidden, then ignore it.
				if ( !isHidden )
				{
					continue;
				}
				// we should use rule as the string as
				if ( isFirst )
				{
					isFirst = false;
				}
				else
				{
					buffer.append( ", " ); //$NON-NLS-1$
				}
				buffer.append( rule.getFormat( ) );
			}
			column.setVisibleFormat( buffer.toString( ) );
		}
	}
	
	protected DataID getDataID( )
	{
		IResultSet curRset = context.getDataEngine( ).getResultSet( );
		if ( curRset != null )
		{
			return new DataID( curRset.getID( ), curRset.getCurrentPosition( ) );
		}
		return null;
	}

	protected void initializeContent( IContent parent,
			ReportElementDesign design, IContent content )
	{
		InstanceID pid = null;
		if ( parent != null )
		{
			pid = parent.getInstanceID( );
		}
		InstanceID id = new InstanceID( pid, design == null ? -1 : design
				.getID( ), getDataID( ) );
		content.setInstanceID( id );
		content.setGenerateBy( design );
		content.setParent( parent );
	}

	/**
	 * starts a TOC entry, mostly used for non-leaf TOC entry, which can not be
	 * closed until its children have been written.
	 * 
	 * @param content
	 *            report item content object
	 */
	protected void startTOCEntry( IContent content )
	{
		TOCBuilder tocBuilder = context.getTOCBuilder( );
		if ( tocBuilder != null )
		{
			if ( content != null )
			{
				String tocLabel = content.getTOC( );
				if ( tocLabel != null )
				{
					String bookmark = content.getBookmark( );
					String tocId = tocBuilder.startEntry( tocLabel, bookmark );
					if ( bookmark == null )
					{
						content.setBookmark( tocId );
					}
					return;
				}
			}
			tocBuilder.startEntry( null, null ); // starts a TOC entry
		}
	}

	/**
	 * finishes a TOC entry, mostly used for non-leaf TOC entry, which can not
	 * be closed until its children have been written.
	 * 
	 * @param content
	 *            report item content object
	 */
	protected void finishTOCEntry( )
	{
		TOCBuilder tocBuilder = context.getTOCBuilder( );
		if ( tocBuilder != null )
		{
			tocBuilder.closeEntry( );
		}
	}

	protected void startGroupTOCEntry( )
	{
		TOCBuilder tocBuilder = context.getTOCBuilder( );
		if ( tocBuilder != null )
		{
			tocBuilder.startGroupEntry( );
		}
	}

	protected void finishGroupTOCEntry( )
	{
		TOCBuilder tocBuilder = context.getTOCBuilder( );
		if ( tocBuilder != null )
		{
			tocBuilder.closeGroupEntry( );
		}
	}
}