/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.render.ActionRendererAdapter;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.SearchKeyHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * A BIRT action renderer implementation.
 */
public class BIRTActionRenderer extends ActionRendererAdapter
{

	private IHTMLActionHandler handler;
	private DesignElementHandle eih;
	private IReportContext context;
	private IDataRowExpressionEvaluator evaluator;
	
	/**
	 * This map is used to cache evaluated script for reducing evaluation
	 * overhead
	 */
	private Map cacheScriptEvaluator;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param handler
	 */
	public BIRTActionRenderer( DesignElementHandle eih,
			IHTMLActionHandler handler, IDataRowExpressionEvaluator evaluator,
			IReportContext context )
	{
		this.eih = eih;
		this.handler = handler;
		this.evaluator = evaluator;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.IActionRenderer#processAction(org.eclipse.birt.chart.model.data.Action,
	 *      org.eclipse.birt.chart.event.StructureSource)
	 */
	public void processAction( Action action, StructureSource source )
	{
		if ( ActionType.URL_REDIRECT_LITERAL.equals( action.getType( ) ) )
		{
			URLValue uv = (URLValue) action.getValue( );

			String sa = uv.getBaseUrl( );
			String target = null;

			if ( StructureType.SERIES_DATA_POINT.equals( source.getType( ) ) )
			{
				final DataPointHints dph = (DataPointHints) source.getSource( );

				try
				{
					final ActionHandle handle = ModuleUtil.deserializeAction( sa );

					target = handle.getTargetWindow( );
					// use engine api to convert actionHandle to a final url
					// value.
					sa = handler.getURL( new IAction( ) {

						public int getType( )
						{
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( handle.getLinkType( ) ) )
								return IAction.ACTION_HYPERLINK;
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( handle.getLinkType( ) ) )
								return IAction.ACTION_BOOKMARK;
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( handle.getLinkType( ) ) )
								return IAction.ACTION_DRILLTHROUGH;
							return 0;
						}

						public String getBookmark( )
						{
							return ChartUtil.stringValue( dph.getUserValue( handle.getTargetBookmark( ) ) );
						}

						public String getActionString( )
						{
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( handle.getLinkType( ) ) )
								return ChartUtil.stringValue( dph.getUserValue( handle.getURI( ) ) );
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( handle.getLinkType( ) ) )
								return ChartUtil.stringValue( dph.getUserValue( handle.getTargetBookmark( ) ) );
							return null;
						}

						public String getReportName( )
						{
							return handle.getReportName( );
						}

						public Map getParameterBindings( )
						{
							Map map = new HashMap( );

							for ( Iterator itr = handle.getParamBindings( )
									.iterator( ); itr.hasNext( ); )
							{
								ParamBindingHandle pbh = (ParamBindingHandle) itr.next( );
								map.put( pbh.getParamName( ),
										dph.getUserValue( pbh.getExpression( ) ) );
							}

							return map;
						}

						public Map getSearchCriteria( )
						{
							Map map = new HashMap( );

							for ( Iterator itr = handle.getSearch( ).iterator( ); itr.hasNext( ); )
							{
								SearchKeyHandle skh = (SearchKeyHandle) itr.next( );
								map.put( skh.getExpression( ),
										dph.getUserValue( skh.getExpression( ) ) );
							}

							return map;
						}

						public String getTargetWindow( )
						{
							return handle.getTargetWindow( );
						}

						public String getFormat( )
						{
							return handle.getFormatType( );
						}

						public boolean isBookmark( )
						{
							return DesignChoiceConstants.ACTION_BOOKMARK_TYPE_BOOKMARK.equals( handle.getTargetBookmarkType( ) );
						}

						public String getSystemId( )
						{
							ModuleHandle mod = eih.getRoot( );
							if ( mod != null )
							{
								return mod.getFileName( );
							}
							return null;
						}

						public String getTargetFileType( )
						{
							return handle.getTargetFileType( );
						}

					},
							context );
				}
				catch ( Exception e )
				{
					sa = ""; //$NON-NLS-1$
					logger.log( e );
				}
			}
			else
			{
				try
				{
					final ActionHandle handle = ModuleUtil.deserializeAction( sa );

					target = handle.getTargetWindow( );

					// use engine api to convert actionHandle to a final url
					// value.
					sa = handler.getURL( new IAction( ) {

						public int getType( )
						{
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( handle.getLinkType( ) ) )
								return IAction.ACTION_HYPERLINK;
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( handle.getLinkType( ) ) )
								return IAction.ACTION_BOOKMARK;
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( handle.getLinkType( ) ) )
								return IAction.ACTION_DRILLTHROUGH;
							return 0;
						}

						public String getBookmark( )
						{
							return ChartUtil.stringValue( evaluator.evaluate( handle.getTargetBookmark( ) ) );
						}

						public String getActionString( )
						{
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( handle.getLinkType( ) ) )
								return ChartUtil.stringValue( evaluator.evaluate( handle.getURI( ) ) );
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( handle.getLinkType( ) ) )
								return ChartUtil.stringValue( evaluator.evaluate( handle.getTargetBookmark( ) ) );
							return null;
						}

						public String getReportName( )
						{
							return handle.getReportName( );
						}

						public Map getParameterBindings( )
						{
							Map map = new HashMap( );

							for ( Iterator itr = handle.getParamBindings( )
									.iterator( ); itr.hasNext( ); )
							{
								ParamBindingHandle pbh = (ParamBindingHandle) itr.next( );
								map.put( pbh.getParamName( ),
										evaluator.evaluate( pbh.getExpression( ) ) );
							}

							return map;
						}

						public Map getSearchCriteria( )
						{
							Map map = new HashMap( );

							for ( Iterator itr = handle.getSearch( ).iterator( ); itr.hasNext( ); )
							{
								SearchKeyHandle skh = (SearchKeyHandle) itr.next( );
								map.put( skh.getExpression( ),
										evaluator.evaluate( skh.getExpression( ) ) );
							}

							return map;
						}

						public String getTargetWindow( )
						{
							return handle.getTargetWindow( );
						}

						public String getFormat( )
						{
							return handle.getFormatType( );
						}

						public boolean isBookmark( )
						{
							return DesignChoiceConstants.ACTION_BOOKMARK_TYPE_BOOKMARK.equals( handle.getTargetBookmarkType( ) );
						}

						public String getSystemId( )
						{
							ModuleHandle mod = eih.getRoot( );
							if ( mod != null )
							{
								return mod.getFileName( );
							}
							return null;
						}

						public String getTargetFileType( )
						{
							return handle.getTargetFileType( );
						}

					},
							context );
				}
				catch ( Exception e )
				{
					sa = ""; //$NON-NLS-1$
					logger.log( e );
				}
			}

			uv.setBaseUrl( sa );
			uv.setTarget( target );
		}
		else if ( ActionType.SHOW_TOOLTIP_LITERAL.equals( action.getType( ) ) )
		{
			TooltipValue tv = (TooltipValue) action.getValue( );

			if ( StructureType.SERIES_DATA_POINT.equals( source.getType( ) ) )
			{
				final DataPointHints dph = (DataPointHints) source.getSource( );
				tv.setText( ChartUtil.stringValue( dph.getUserValue( tv.getText( ) ) ) );
			}
		}
		else if ( ActionType.INVOKE_SCRIPT_LITERAL.equals( action.getType( ) ) )
		{
			ScriptValue sv = (ScriptValue) action.getValue( );
			if ( cacheScriptEvaluator == null )
			{
				cacheScriptEvaluator = new HashMap( );
			}
			String evaluatResult = (String) cacheScriptEvaluator.get( sv.getScript( ) );
			if ( evaluatResult == null )
			{
				evaluatResult = evaluateExpression( sv.getScript( ) );
				cacheScriptEvaluator.put( sv.getScript( ), evaluatResult );
			}
			sv.setScript( evaluatResult );
		}
	}

	private String evaluateExpression( String script )
	{
		if ( script == null || script.trim( ).length( ) == 0 )
		{
			return ""; //$NON-NLS-1$
		}
		String expression = findParameterExp( script, 0 );
		while ( expression != null )
		{
			// Do not use JAVA 5.0 API
			// script = script.replace( expression,
			// (String) evaluator.evaluate( expression ) );
			script = Pattern.compile( expression, Pattern.LITERAL )
					.matcher( script )
					.replaceAll( evaluator.evaluate( expression ).toString( ) );			
			expression = findParameterExp( script, 0 );
		}
		return script;
	}

	private static String findParameterExp( String script, int fromIndex )
	{
		int iStart = script.indexOf( ExpressionUtil.PARAMETER_INDICATOR + '[',
				fromIndex );
		if ( iStart < fromIndex )
		{
			return null;
		}
		int iEnd = script.indexOf( ']', iStart );
		if ( iEnd < iStart + ExpressionUtil.PARAMETER_INDICATOR.length( ) )
		{
			return null;
		}
		return script.substring( iStart, iEnd + 1 );
	}

}
