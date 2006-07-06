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

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.PDFRenderContext;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
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
	 * if the page is embedded, the bookmark should always be a url to submit.
	 */
	protected boolean isEmbeddable = false;

	/**
	 * RTL option setting by the command line or URL parameter.
	 */

	protected boolean isRtl = false;

	/**
	 * if wanna use the master page, then set it to true.
	 */

	protected boolean isMasterPageContent = true;

	/**
	 * Constructor.
	 */
	public ViewerHTMLActionHandler( )
	{
	}

	/**
	 * Constructor. This is for renderTask.
	 * 
	 * @param document
	 * @param page
	 * @param locale
	 * @param isEmbeddable
	 * @param isRtl
	 * @param isMasterPageContent
	 */

	public ViewerHTMLActionHandler( IReportDocument document, long page,
			Locale locale, boolean isEmbeddable, boolean isRtl,
			boolean isMasterPageContent )
	{
		this.document = document;
		this.page = page;
		this.locale = locale;
		this.isEmbeddable = isEmbeddable;
		this.isRtl = isRtl;
		this.isMasterPageContent = isMasterPageContent;
	}

	/**
	 * Constructor. This is for runAndRender task.
	 * 
	 * @param locale
	 * @param isEmbeddable
	 * @param isRtl
	 * @param isMasterPageContent
	 */

	public ViewerHTMLActionHandler( Locale locale, boolean isRtl,
			boolean isMasterPageContent )
	{
		this.locale = locale;
		this.isRtl = isRtl;
		this.isMasterPageContent = isMasterPageContent;
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
		if ( action == null || context == null )
			return null;

		// Get Base URL
		String baseURL = null;
		if ( context instanceof HTMLRenderContext )
		{
			baseURL = ( (HTMLRenderContext) context ).getBaseURL( );
		}
		if ( context instanceof PDFRenderContext )
		{
			baseURL = ( (PDFRenderContext) context ).getBaseURL( );
		}

		// Get bookmark
		String bookmark = action.getBookmark( );

		// In frameset mode, use javascript function to fire Ajax request to
		// link to internal bookmark
		if ( baseURL.lastIndexOf( IBirtConstants.SERVLET_PATH_FRAMESET ) > 0 )
		{
			return "javascript:catchBookmark('" + bookmark + "')"; //$NON-NLS-2$
		}

		// Save the URL String
		StringBuffer link = new StringBuffer( );

		boolean realBookmark = false;

		if ( this.document != null )
		{
			long pageNumber = this.document
					.getPageNumber( action.getBookmark( ) );
			realBookmark = ( pageNumber == this.page && !isEmbeddable );
		}

		try
		{
			bookmark = URLEncoder.encode( bookmark,
					ParameterAccessor.UTF_8_ENCODE );
		}
		catch ( UnsupportedEncodingException e )
		{
			// Does nothing
		}

		link.append( baseURL );
		link.append( ParameterAccessor.QUERY_CHAR );

		// if the document is not null, then use it

		if ( document != null )
		{
			link.append( ParameterAccessor.PARAM_REPORT_DOCUMENT );
			link.append( ParameterAccessor.EQUALS_OPERATOR );
			String documentName = document.getName( );

			try
			{
				documentName = URLEncoder.encode( documentName,
						ParameterAccessor.UTF_8_ENCODE );
			}
			catch ( UnsupportedEncodingException e )
			{
				// Does nothing
			}
			link.append( documentName );
		}
		else if ( action.getReportName( ) != null
				&& action.getReportName( ).length( ) > 0 )
		{
			link.append( ParameterAccessor.PARAM_REPORT );
			link.append( ParameterAccessor.EQUALS_OPERATOR );
			String reportName = action.getReportName( );
			try
			{
				reportName = URLEncoder.encode( reportName,
						ParameterAccessor.UTF_8_ENCODE );
			}
			catch ( UnsupportedEncodingException e )
			{
				// do nothing
			}
			link.append( reportName );
		}
		else
		{
			// its an iternal bookmark
			return "#" + action.getActionString( ); //$NON-NLS-1$
		}

		if ( locale != null )
		{
			link.append( ParameterAccessor.getQueryParameterString(
					ParameterAccessor.PARAM_LOCALE, locale.toString( ) ) );
		}
		if ( isRtl )
		{
			link.append( ParameterAccessor.getQueryParameterString(
					ParameterAccessor.PARAM_RTL, String.valueOf( isRtl ) ) );
		}

		// add isMasterPageContent

		link.append( ParameterAccessor.getQueryParameterString(
				ParameterAccessor.PARAM_MASTERPAGE, String
						.valueOf( this.isMasterPageContent ) ) );

		if ( realBookmark )
		{
			link.append( "#" ); //$NON-NLS-1$
			link.append( bookmark );
		}
		else
		{
			link.append( ParameterAccessor.getQueryParameterString(
					ParameterAccessor.PARAM_BOOKMARK, bookmark ) );
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
		if ( action == null || context == null )
			return null;

		String baseURL = null;
		if ( context instanceof HTMLRenderContext )
		{
			baseURL = ( (HTMLRenderContext) context ).getBaseURL( );
		}
		if ( context instanceof PDFRenderContext )
		{
			baseURL = ( (PDFRenderContext) context ).getBaseURL( );
		}

		if ( baseURL == null )
			baseURL = IBirtConstants.VIEWER_RUN;

		StringBuffer link = new StringBuffer( );
		String reportName = action.getReportName( );

		if ( reportName != null && !reportName.equals( "" ) ) //$NON-NLS-1$
		{
			String format = action.getFormat( );
			if ( ParameterAccessor.PARAM_FORMAT_PDF.equalsIgnoreCase( format ) )
			{
				baseURL = baseURL.replaceFirst( IBirtConstants.VIEWER_FRAMESET,
						IBirtConstants.VIEWER_RUN );
			}

			link.append( baseURL );

			link
					.append( reportName.toLowerCase( )
							.endsWith( ".rptdocument" ) ? "?__document=" : "?__report=" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			try
			{
				link.append( URLEncoder.encode( reportName,
						ParameterAccessor.UTF_8_ENCODE ) );
			}
			catch ( UnsupportedEncodingException e1 )
			{
				// It should not happen. Does nothing
			}

			// add format support
			if ( format != null && format.length( ) > 0 )
			{
				link.append( ParameterAccessor.getQueryParameterString(
						ParameterAccessor.PARAM_FORMAT, format ) );
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
							// TODO: here need the get the format from the
							// parameter.
							String value = ParameterValidationUtil
									.getDisplayValue( valueObj );

							link
									.append( ParameterAccessor
											.getQueryParameterString(
													URLEncoder
															.encode(
																	key,
																	ParameterAccessor.UTF_8_ENCODE ),
													URLEncoder
															.encode(
																	value,
																	ParameterAccessor.UTF_8_ENCODE ) ) );
						}
					}
					catch ( UnsupportedEncodingException e )
					{
						// Does nothing
					}
				}

				// Adding overwrite.
				if ( !reportName.toLowerCase( ).endsWith(
						ParameterAccessor.SUFFIX_REPORT_DOCUMENT )
						&& baseURL
								.lastIndexOf( IBirtConstants.SERVLET_PATH_FRAMESET ) > 0 )
				{
					link.append( ParameterAccessor.getQueryParameterString(
							ParameterAccessor.PARAM_OVERWRITE, String
									.valueOf( true ) ) );
				}
			}

			if ( locale != null )
			{
				link.append( ParameterAccessor.getQueryParameterString(
						ParameterAccessor.PARAM_LOCALE, locale.toString( ) ) );
			}
			if ( isRtl )
			{
				link.append( ParameterAccessor.getQueryParameterString(
						ParameterAccessor.PARAM_RTL, String.valueOf( isRtl ) ) );
			}

			// add isMasterPageContent

			link.append( ParameterAccessor.getQueryParameterString(
					ParameterAccessor.PARAM_MASTERPAGE, String
							.valueOf( this.isMasterPageContent ) ) );

			// add bookmark
			if ( action.getBookmark( ) != null )
			{

				try
				{
					// In RUN mode, don't support bookmark as parameter
					if ( baseURL.lastIndexOf( IBirtConstants.SERVLET_PATH_RUN ) > 0 )
					{
						link.append( "#" ); //$NON-NLS-1$
					}
					else
					{
						link.append( "&__bookmark=" ); //$NON-NLS-1$
					}

					link.append( URLEncoder.encode( action.getBookmark( ),
							ParameterAccessor.UTF_8_ENCODE ) );
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