/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * HTML action handler for url generation.
 */
class ViewerHTMLActionHandler implements IHTMLActionHandler
{

	/**
	 * Logger for this handler.
	 */

	protected Logger log = Logger.getLogger( ViewerHTMLActionHandler.class
			.getName( ) );

	/**
	 * Document instance.
	 */

	protected IReportDocument document = null;

	/**
	 * Locale of the requester.
	 */

	protected Locale locale = null;

	/**
	 * Page number of the action requester.
	 */

	protected long page = -1;

	/**
	 * Constructor.
	 */
	public ViewerHTMLActionHandler( )
	{
	}

	/**
	 * Constructor.
	 * 
	 * @param document
	 * @param page
	 * @param locale
	 */

	public ViewerHTMLActionHandler( IReportDocument document, long page,
			Locale locale )
	{
		this.document = document;
		this.page = page;
		this.locale = locale;
	}

	/**
	 * Get URL
	 */
	public String getURL( IAction actionDefn, Object context )
	{
		if ( actionDefn == null )
		{
			return null;
		}
		switch ( actionDefn.getType( ) )
		{
			case IAction.ACTION_BOOKMARK :
			{
				return buildBookmarkAction( actionDefn, context );
			}
			case IAction.ACTION_HYPERLINK :
			{
				return actionDefn.getActionString( );
			}
			case IAction.ACTION_DRILLTHROUGH :
			{
				return buildDrillAction( actionDefn, context );
			}
		}

		return null;
	}

	/**
	 * Build URL for bookmark.
	 * 
	 * @param action
	 * @param context
	 * @return the bookmark url
	 */

	protected String buildBookmarkAction( IAction action, Object context )
	{
		StringBuffer link = new StringBuffer( );

		boolean realBookmark = false;

		if ( this.document != null )
		{
			long pageNumber = this.document
					.getPageNumber( action.getBookmark( ) );
			realBookmark = pageNumber == this.page;
		}

		String bookmark = action.getBookmark( );
		try
		{
			bookmark = URLEncoder.encode( bookmark, "UTF-8" ); //$NON-NLS-1$
		}
		catch ( UnsupportedEncodingException e )
		{
			// Does nothing
		}

		if ( realBookmark )
		{
			link.append( "#" ); //$NON-NLS-1$
			link.append( bookmark );
			if ( locale != null )
			{
				link.append( "&__locale=" ); //$NON-NLS-1$
				link.append( locale.toString( ) );
			}

		}
		else
		{
			String baseURL = null;
			if ( context != null && context instanceof HTMLRenderContext )
			{
				baseURL = ( (HTMLRenderContext) context ).getBaseURL( );
			}
			link.append( baseURL );

			link.append( "?__document=" ); //$NON-NLS-1$
			String documentName = document.getName( );
			try
			{
				documentName = URLEncoder.encode( documentName, "UTF-8" ); //$NON-NLS-1$
			}
			catch ( UnsupportedEncodingException e )
			{
				// Does nothing
			}
			link.append( documentName );
			link.append( "&__bookmark=" ); //$NON-NLS-1$
			link.append( bookmark );
			if ( locale != null )
			{
				link.append( "&__locale=" ); //$NON-NLS-1$
				link.append( locale.toString( ) );
			}
		}

		return link.toString( );
	}

	/**
	 * builds URL for drillthrough action
	 * 
	 * @param action
	 *            instance of the IAction instance
	 * @param context
	 *            the context for building the action string
	 * @return a URL
	 */
	protected String buildDrillAction( IAction action, Object context )
	{
		String baseURL = null;
		if ( context != null && context instanceof HTMLRenderContext )
		{
			baseURL = ( (HTMLRenderContext) context ).getBaseURL( );
		}

		StringBuffer link = new StringBuffer( );
		String reportName = action.getReportName( );

		if ( reportName != null && !reportName.equals( "" ) ) //$NON-NLS-1$
		{
			String format = action.getFormat( );
			if ( ParameterAccessor.PARAM_FORMAT_PDF.equalsIgnoreCase( format ) )
			{
				link.append( baseURL.replaceFirst( "frameset", "run" ) ); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else
			{
				link.append( baseURL );
			}

			link
					.append( reportName.toLowerCase( )
							.endsWith( ".rptdocument" ) ? "?__document=" : "?__report=" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			try
			{
				link.append( URLEncoder.encode( reportName, "UTF-8" ) ); //$NON-NLS-1$
			}
			catch ( UnsupportedEncodingException e1 )
			{
				// It should not happen. Does nothing
			}

			// add format support
			if ( format != null && format.length( ) > 0 )
			{
				link.append( "&__format=" + format ); //$NON-NLS-1$
			}

			// Adds the parameters
			if ( action.getParameterBindings( ) != null )
			{
				Iterator paramsIte = action.getParameterBindings( ).entrySet( )
						.iterator( );
				while ( paramsIte.hasNext( ) )
				{
					Map.Entry entry = (Map.Entry) paramsIte.next( );
					try
					{
						String key = (String) entry.getKey( );
						Object valueObj = entry.getValue( );
						if ( valueObj != null )
						{
							String value = valueObj.toString( );
							link
									.append( "&" + URLEncoder.encode( key, "UTF-8" ) //$NON-NLS-1$ //$NON-NLS-2$
											+ "=" + URLEncoder.encode( value, "UTF-8" ) ); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
					catch ( UnsupportedEncodingException e )
					{
						// Does nothing
					}
				}
			}

			// Adding overwrite.
			link.append( "&__overwrite=true" ); //$NON-NLS-1$

			// The search rules are not supported yet.
			if ( !ParameterAccessor.PARAM_FORMAT_PDF.equalsIgnoreCase( format )
					&& action.getBookmark( ) != null )
			{

				try
				{
					link.append( "&__bookmark=" ); //$NON-NLS-1$
					link.append( URLEncoder.encode( action.getBookmark( ),
							"UTF-8" ) ); //$NON-NLS-1$
				}
				catch ( UnsupportedEncodingException e )
				{
					// Does nothing
				}
			}
		}

		return link.toString( );
	}
}