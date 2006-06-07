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

package org.eclipse.birt.report.engine.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Defines a default action handler for HTML output format
 */
public class HTMLActionHandler implements IHTMLActionHandler
{

	/** logger */
	protected Logger log = Logger
			.getLogger( HTMLActionHandler.class.getName( ) );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IHTMLActionHandler#getURL(org.eclipse.birt.report.engine.api2.IAction,
	 *      java.lang.Object)
	 */
	public String getURL( IAction actionDefn, Object context )
	{
		if ( actionDefn == null )
		{
			return null;
		}
		String url = null;
		switch ( actionDefn.getType( ) )
		{
			case IAction.ACTION_BOOKMARK :
				if ( actionDefn.getActionString( ) != null )
				{
					url = "#" + actionDefn.getActionString( );
				}
				break;
			case IAction.ACTION_HYPERLINK :
				url = actionDefn.getActionString( );
				break;
			case IAction.ACTION_DRILLTHROUGH :
				url = buildDrillAction( actionDefn, context );
				break;
			default :
				assert false;
		}
		return url;
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
		if ( context != null )
		{
			if (context instanceof HTMLRenderContext)
			{
				baseURL = ( (HTMLRenderContext) context ).getBaseURL( );
			}
			if (context instanceof PDFRenderContext)
			{
				baseURL = ( (PDFRenderContext) context ).getBaseURL( );
			}
		}

		if ( baseURL == null )
		{
			baseURL = "run";
		}
		StringBuffer link = new StringBuffer( );
		String reportName = action.getReportName( );

		if ( reportName != null && !reportName.equals( "" ) ) //$NON-NLS-1$
		{
			String format = action.getFormat( );
			if ( !"html".equalsIgnoreCase( format ) )
			{
				link.append( baseURL.replaceFirst( "frameset", "run" ) ); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else
			{
				link.append( baseURL );
			}

			link
					.append( reportName.toLowerCase( )
							.endsWith( ".rptdocument" ) ? "?__document=" : "?__report=" ); //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$

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
			if ( /*!"pdf".equalsIgnoreCase( format )
					&&*/ action.getBookmark( ) != null )
			{

				try
				{
					// In RUN mode, don't support bookmark as parameter
					if ( baseURL.lastIndexOf( "run" ) > 0 )
					{
						link.append( "#" ); //$NON-NLS-1$
					}
					else
					{
						link.append( "&__bookmark=" ); //$NON-NLS-1$
					}

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

	protected void appendReportDesignName( StringBuffer buffer,
			String reportName )
	{
		buffer.append( "?__report=" ); //$NON-NLS-1$
		try
		{
			buffer.append( URLEncoder.encode( reportName, "UTF-8" ) ); //$NON-NLS-1$
		}
		catch ( UnsupportedEncodingException e1 )
		{
			// It should not happen. Does nothing
		}
	}

	protected void appendFormat( StringBuffer buffer, String format )
	{
		if ( format != null && format.length( ) > 0 )
		{
			buffer.append( "&__format=" + format );//$NON-NLS-1$
		}
	}

	protected void appendParamter( StringBuffer buffer, String key,
			Object valueObj )
	{
		if ( valueObj != null )
		{
			try
			{
				key = URLEncoder.encode( key, "UTF-8" );
				String value = valueObj.toString( );
				value = URLEncoder.encode( value, "UTF-8" );
				buffer.append( "&" );
				buffer.append( key );
				buffer.append( "=" );
				buffer.append( value );
			}
			catch ( UnsupportedEncodingException e )
			{
				// Does nothing
			}
		}
	}

	protected void appendBookmarkAsParamter( StringBuffer buffer,
			String bookmark )
	{
		try
		{
			if ( bookmark != null && bookmark.length( ) != 0 )
			{
				bookmark = URLEncoder.encode( bookmark, "UTF-8" );
				buffer.append( "&__bookmark=" );//$NON-NLS-1$
				buffer.append( bookmark );
			}
		}
		catch ( UnsupportedEncodingException e )
		{

		}
	}

	protected void appendBookmark( StringBuffer buffer, String bookmark )
	{
		try
		{
			if ( bookmark != null && bookmark.length( ) != 0 )
			{
				bookmark = URLEncoder.encode( bookmark, "UTF-8" );
				buffer.append( "#" );//$NON-NLS-1$
				buffer.append( bookmark );
			}
		}
		catch ( UnsupportedEncodingException e )
		{
		}
	}

}