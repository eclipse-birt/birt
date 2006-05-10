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

import java.util.List;

/**
 * output settings for HTML output format
 */
public class HTMLRenderOption extends RenderOptionBase  implements IHTMLRenderOption 
{
	public static final String HTML_TYPE = "HTMLType"; //$NON-NLS-1$
	public static final String HTML = "HTML"; //$NON-NLS-1$
	public static final String HTML_NOCSS = "HTMLNoCSS"; //$NON-NLS-1$
	public static final String USER_AGENT = "user-agent"; //$NON-NLS-1$
	public static final String URL_ENCODING = "url-encoding"; //$NON-NLS-1$
	public static final String INSTANCE_ID_LIST = "InstanceIdList"; //$NON-NLS-1$
	public static final String HTML_PAGINATION = "htmlPagination"; //$NON-NLS-1$
	public static final String MASTER_PAGE_CONTENT = "htmlMasterPageContent"; //$NON-NLS-1$
	public static final String OBSERVE_TEMPLATE_DEFAULT = "observeTemplateDefault"; //$NON-NLS-1$
	public static final String HTML_INCLUDE_SELECTION_HANDLE = "includeSelectionHandle"; //$NON-NLS-1$

	/**
	 * @return Returns the instanceIDs.
	 */
	public List getInstanceIDs( )
	{
		Object list = options.get( INSTANCE_ID_LIST );
		if ( list instanceof List )
		{
			return (List) list;
		}
		return null;
	}

	/**
	 * @param instanceIDs
	 *            The instanceIDs to set.
	 */
	public void setInstanceIDs( List instanceIDs )
	{
		options.put( INSTANCE_ID_LIST, instanceIDs );
	}

	/**
	 * constructor
	 */
	public HTMLRenderOption( )
	{
	}

	/**
	 * sets whether the HTML output can be embedded directly into an HTML page
	 * 
	 * @param embeddable
	 *            whether the HTML output can be embedded directly into an HTML
	 *            page
	 */
	public void setEmbeddable( boolean embeddable )
	{
		if ( embeddable )
			options.put( HTML_TYPE, HTML_NOCSS );
		else
			options.put( HTML_TYPE, HTML );
	}

	/**
	 * @return whether the output is embeddable
	 */
	public boolean getEmbeddable( )
	{
		String htmlType = (String) options.get( HTML_TYPE );
		if ( htmlType != null && htmlType.compareTo( HTML_NOCSS ) == 0 )
			return true;
		return false;
	}

	/**
	 * @param userAgent
	 *            the user agent of the request
	 */
	public void setUserAgent( String userAgent )
	{
		options.put( USER_AGENT, userAgent );
	}

	/**
	 * @return the user agent for the request
	 */
	public String getUserAgent( )
	{
		return (String) options.get( USER_AGENT );
	}

	public void setMasterPageContent( boolean show )
	{
		options.put( MASTER_PAGE_CONTENT, Boolean.valueOf( show ) );
	}

	public boolean getMasterPageContent( )
	{
		Boolean value = (Boolean) options.get( MASTER_PAGE_CONTENT );
		if ( value != null )
		{
			return value.booleanValue( );
		}
		return true;
	}

	public void setHtmlPagination( boolean paginate )
	{
		options.put( HTML_PAGINATION, Boolean.valueOf( paginate ) );
	}

	public boolean getHtmlPagination( )
	{
		Boolean value = (Boolean) options.get( HTML_PAGINATION );
		if ( value != null )
		{
			return value.booleanValue( );
		}
		return true;
	}

	public void setActionHandle( IHTMLActionHandler handler )
	{
		options.put( ACTION_HANDLER, handler );
	}

	public IHTMLActionHandler getActionHandle( )
	{
		return (IHTMLActionHandler) options.get( ACTION_HANDLER );
	}
	
	public void setRenderTemplateUseDefault(boolean option)
	{
		options.put( OBSERVE_TEMPLATE_DEFAULT, new Boolean(option) );
	}
	
	public boolean getRenderTemplateUseDefault()
	{
		Boolean value = (Boolean) options.get( OBSERVE_TEMPLATE_DEFAULT);
		if ( value != null )
		{
			return value.booleanValue( );
	}
		return false;
	}
	
	public void setIncludeSelectionHandle(boolean option)
	{
		options.put( HTML_INCLUDE_SELECTION_HANDLE, new Boolean(option) );
	}
	
	public boolean getIncludeSelectionHandle()
	{
		Boolean value = (Boolean) options.get( HTML_INCLUDE_SELECTION_HANDLE);
		if ( value != null )
		{
			return value.booleanValue( );
		}
		return false;
	}

	public void setHtmlRtLFlag( boolean option )
	{
		options.put( HTML_RTL_FLAG, new Boolean( option ) );
	}

	public boolean getHtmlRtLFlag( )
	{
		Boolean value = (Boolean) options.get( HTML_RTL_FLAG );
		if ( value != null )
		{
			return value.booleanValue( );
		}
		return false;
	}
}
