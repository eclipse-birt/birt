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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.render.ActionRendererAdapter;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.model.api.ActionHandle;
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
	private Object context;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param handler
	 */
	public BIRTActionRenderer( IHTMLActionHandler handler, Object context )
	{
		this.handler = handler;
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

			if ( StructureType.SERIES_DATA_POINT.equals( source.getType( ) ) )
			{
				final DataPointHints dph = (DataPointHints) source.getSource( );

				try
				{
					final ActionHandle handle = ModuleUtil.deserializeAction( sa );

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
							return (String) dph.getUserValue( handle.getTargetBookmark( ) );
						}

						public String getActionString( )
						{
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( handle.getLinkType( ) ) )
								return (String) dph.getUserValue( handle.getURI( ) );
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( handle.getLinkType( ) ) )
								return (String) dph.getUserValue( handle.getTargetBookmark( ) );
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
										(String) dph.getUserValue( pbh.getExpression( ) ) );
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
										(String) dph.getUserValue( skh.getExpression( ) ) );
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
							return handle.getTargetBookmark( );
						}

						public String getActionString( )
						{
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( handle.getLinkType( ) ) )
								return handle.getURI( );
							if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( handle.getLinkType( ) ) )
								return handle.getTargetBookmark( );
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
										pbh.getExpression( ) );
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
										skh.getExpression( ) );
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
		}
	}
}
