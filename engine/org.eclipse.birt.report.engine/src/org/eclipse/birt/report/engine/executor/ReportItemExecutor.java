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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IReportItemContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.Expression;
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
 * @version $Revision: 1.5 $ $Date: 2005/02/25 06:02:24 $
 */
public abstract class ReportItemExecutor
{

	/**
	 * the logger, log info, debug, and error message
	 */
	protected static Log logger = LogFactory.getLog( ReportItemExecutor.class );

	/**
	 * the executor context
	 */
	protected ExecutionContext context;

	/**
	 * the exectutor visitor
	 */
	protected ReportExecutorVisitor visitor;

	/**
	 * construct a report item executor by giving execution context and report
	 * executor visitor
	 * 
	 * @param context
	 *            the excutor context
	 * @param visitor
	 *            the report executor visitor
	 */
	protected ReportItemExecutor( ExecutionContext context,
			ReportExecutorVisitor visitor )
	{
		this.context = context;
		this.visitor = visitor;
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
	public abstract void execute( ReportItemDesign item, IReportEmitter emitter );

	/**
	 * reset the state of the report item executor. This operation will reset
	 * all property of this object
	 *  
	 */
	public abstract void reset( );

	/**
	 * close dataset if the dataset is not null:
	 * <p>
	 * <ul>
	 * <li>close the dataset.
	 * <li>exit current script scope.
	 * </ul>
	 * 
	 * @param ds
	 *            the dataset object, null is valid
	 */
	protected void closeResultSet( IResultSet rs )
	{
		if ( rs != null )
		{
			rs.close( );
			context.exitScope( );
		}
	}

	/**
	 * register dataset of this item.
	 * <p>
	 * if dataset design of this item is not null, create a new
	 * <code>DataSet</code> object by the dataset design. open the dataset,
	 * move cursor to the first record , register the first row to script
	 * context, and return this <code>DataSet</code> object if dataset design
	 * is null, or open error, or empty resultset, return null.
	 * 
	 * @param item
	 *            the report item design
	 * @return the DataSet object if not null, else return null
	 */
	protected IResultSet openResultSet( ReportItemDesign item )
	{
		if ( item.getQuery( ) != null )
		{
			context.newScope( );
			IResultSet rs = context.getDataEngine( ).execute( item.getQuery( ) );
			if ( rs != null )
			{
				return rs;
			}
			else
			{
				context.exitScope( );
			}
		}
		return null;

	}

	/**
	 * get Localized string by the resouce key of this item and
	 * <code>Locale</code> object in <code>context</code>
	 * 
	 * @param resourceKey
	 *            the resource key
	 * @param text
	 *            the default value
	 * @return the localized string if it is defined in report deign, else
	 *         return the default value
	 */
	protected String getLocalizedString( String resourceKey, String text )
	{
		if ( context.getReport( ) == null || resourceKey == null )
		{
			return text;
		}
		String ret = context.getReport( ).getMessage( resourceKey,
				context.getLocale( ) );
		if ( ret == null )
		{
			logger.error( "get resource error, resource key:" + resourceKey
					+ " Locale:" + context.getLocale( ).toString( ) );
			return text;
		}
		return ret;
	}

	/**
	 * Calculate the bookmark value which is set to
	 * <code>ReportItemContent</code> if the bookmark is not null
	 * 
	 * @param item
	 *            the ReportItemContent object
	 */
	/*
	 * protected void evalBookmark( ReportItemContent item ) { Expression
	 * bookmark = item.getBookmark( ); if ( bookmark != null ) { Object obj =
	 * context.evaluate( bookmark); if ( obj != null ) { item.setBookmarkValue(
	 * obj.toString( ) ); } } }
	 */

	protected String evalBookmark( ReportItemDesign item )
	{
		Expression bookmark = item.getBookmark( );
		if ( bookmark != null )
		{
			Object tmp = context.evaluate( bookmark );
			if ( tmp != null )
				return tmp.toString( );
		}

		return null;
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
	protected void processAction( ActionDesign action,
			IReportItemContent itemContent )
	{
		assert itemContent != null;

		if ( action != null )
		{
			try
			{
				switch ( action.getActionType( ) )
				{
					case ActionDesign.ACTION_HYPERLINK :
						assert action.getHyperlink( ) != null;
						Object value = context
								.evaluate( action.getHyperlink( ) );
						if ( value != null )
						{
							IHyperlinkAction obj = ContentFactory
									.createActionContent( value.toString( ) );
							itemContent.setHyperlinkAction( obj );
						}
						break;
					case ActionDesign.ACTION_BOOKMARK :
						assert action.getBookmark( ) != null;
						value = context.evaluate( action.getBookmark( ) );
						if ( value != null )
						{
							IHyperlinkAction obj = ContentFactory
									.createActionContent( value.toString( ), value
											.toString( ) );
							itemContent.setHyperlinkAction( obj );
						}
						break;
					case ActionDesign.ACTION_DRILLTHROUGH :
						// not supported for now
						break;
					default :
				}
			}
			catch ( Exception e )
			{
				itemContent.setHyperlinkAction( null );
				if ( logger.isErrorEnabled( ) )
				{
					logger.error( "Failed to process Action String" );
				}
			}
		}
	}

	/**
	 * check if <code>row</code> is visible, default is visible
	 * <ul>
	 * <li>if hide-expression is legal boolean value, return the boolean value
	 * <li>if hide-expression is null or illegal, return true
	 * </ul>
	 * 
	 * @param row
	 *            the row object
	 * @return the boolean value
	 */
	//	protected boolean isRowVisible( RowDesign row )
	//	{
	//		if ( row.getHideExpr( ) != null )
	//		{
	//			Object visible = context.evaluate( row.getHideExpr( ).getExpr( ) );
	//			if ( ( visible != null ) && ( visible instanceof Boolean ) )
	//			{
	//				return ( (Boolean) visible ).booleanValue( );
	//			}
	//		}
	//		return true;
	//	}
}