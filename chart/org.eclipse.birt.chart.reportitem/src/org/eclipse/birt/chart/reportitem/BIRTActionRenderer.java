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

package org.eclipse.birt.chart.reportitem;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.render.ActionRendererAdapter;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
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
	private Object context;
	private IDataRowExpressionEvaluator evaluator;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param handler
	 */
	public BIRTActionRenderer( DesignElementHandle eih,
			IHTMLActionHandler handler, IDataRowExpressionEvaluator evaluator,
			Object context )
	{
		this.eih = eih;
		this.handler = handler;
		this.evaluator = evaluator;

		// TODO !!!This is only a temp solution to resovle bugzilla#152948 due
		// to implementation limitation for other api team.
		// The correct way should be that just pass IReportContext directly to
		// ActionHandler and let handler find any render context it concerns.
		if ( "html".equals( ( (IReportContext) context ).getOutputFormat( ) ) ) //$NON-NLS-1$
		{
			this.context = ( (IReportContext) context ).getAppContext( )
					.get( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT );
		}
		else
		{
			this.context = ( (IReportContext) context ).getAppContext( )
					.get( EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT );
		}
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
							String rptName = handle.getReportName( );
							ModuleHandle mod = eih.getRoot( );
							if ( mod != null )
							{
								URL reportURL = mod.findResource( rptName,
										IResourceLocator.LIBRARY );
								if ( reportURL != null )
								{
									String reportFile = reportURL.getFile( );
									if ( reportFile != null )
									{
										rptName = reportFile;
									}
								}
							}

							return rptName;
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
							String rptName = handle.getReportName( );
							ModuleHandle mod = eih.getRoot( );
							if ( mod != null )
							{
								URL reportURL = mod.findResource( rptName,
										IResourceLocator.LIBRARY );
								if ( reportURL != null )
								{
									String reportFile = reportURL.getFile( );
									if ( reportFile != null )
									{
										rptName = reportFile;
									}
								}
							}

							return rptName;
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
	}

}
