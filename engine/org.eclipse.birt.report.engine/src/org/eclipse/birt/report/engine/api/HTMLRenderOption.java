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
public class HTMLRenderOption extends RenderOption implements IHTMLRenderOption
{

	/**
	 * constructor
	 */
	public HTMLRenderOption( IRenderOption options )
	{
		super( options );
	}

	public HTMLRenderOption( )
	{
		super( );
	}

	/**
	 * @return Returns the instanceIDs.
	 */
	public List getInstanceIDs( )
	{
		Object list = getOption( INSTANCE_ID_LIST );
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
		setOption( INSTANCE_ID_LIST, instanceIDs );
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
			setOption( HTML_TYPE, HTML_NOCSS );
		else
			setOption( HTML_TYPE, HTML );
	}

	/**
	 * @return whether the output is embeddable
	 */
	public boolean getEmbeddable( )
	{
		String htmlType = getStringOption( HTML_TYPE );
		if ( HTML_NOCSS.equals( htmlType ) )
			return true;
		return false;
	}

	/**
	 * @param userAgent
	 *            the user agent of the request
	 */
	public void setUserAgent( String userAgent )
	{
		setOption( USER_AGENT, userAgent );
	}

	/**
	 * @return the user agent for the request
	 */
	public String getUserAgent( )
	{
		return getStringOption( USER_AGENT );
	}

	public void setUrlEncoding( String encoding )
	{
		setOption( URL_ENCODING, encoding );
	}

	/**
	 * @return the user agent for the request
	 */
	public String getUrlEncoding( )
	{
		return getStringOption( URL_ENCODING );
	}

	public void setMasterPageContent( boolean show )
	{
		setOption( MASTER_PAGE_CONTENT, Boolean.valueOf( show ) );
	}

	public boolean getMasterPageContent( )
	{
		return getBooleanOption( MASTER_PAGE_CONTENT, true );
	}

	public void setHtmlPagination( boolean paginate )
	{
		setOption( HTML_PAGINATION, Boolean.valueOf( paginate ) );
	}

	public boolean getHtmlPagination( )
	{
		return getBooleanOption( HTML_PAGINATION, false );
	}

	/**
	 * @deprecated includeSelectionHandle is replaced by eanableMetadata flag.
	 */
	public void setIncludeSelectionHandle( boolean option )
	{
		setOption( HTML_INCLUDE_SELECTION_HANDLE, new Boolean( option ) );
	}

	/**
	 * @deprecated includeSelectionHandle is replaced by eanableMetadata flag.
	 */
	public boolean getIncludeSelectionHandle( )
	{
		return getBooleanOption( HTML_INCLUDE_SELECTION_HANDLE, false );
	}

	public void setHtmlRtLFlag( boolean flag )
	{
		setOption( HTML_RTL_FLAG, new Boolean( flag ) );
	}

	public boolean getHtmlRtLFlag( )
	{
		return getBooleanOption( HTML_RTL_FLAG, false );
	}

	/**
	 * @param htmlTile
	 */
	public void setHtmlTitle( String htmlTitle )
	{
		setOption( HTML_TITLE, htmlTitle );
	}

	/**
	 * @return the default html title
	 */
	public String getHtmlTitle( )
	{
		return getStringOption( HTML_TITLE );
	}

	public void setPageFooterFloatFlag( boolean flag )
	{
		setOption( PAGEFOOTER_FLOAT_FLAG, new Boolean( flag ) );
	}

	public boolean getPageFooterFloatFlag( )
	{
		return getBooleanOption( PAGEFOOTER_FLOAT_FLAG, true );
	}

	/**
	 * Sets the flag which indicating if metadata should be output.
	 * 
	 * @param enableMetadata
	 *            the flag
	 */
	public void setEnableMetadata( boolean enableMetadata )
	{
		setOption( HTML_ENABLE_METADATA, new Boolean( enableMetadata ) );
	}

	/**
	 * @return the enable metadata flag value.
	 */
	public boolean getEnableMetadata( )
	{
		return getBooleanOption( HTML_ENABLE_METADATA, false );
	}

	/**
	 * Sets the flag indicationg that if filter icons should be displayed.
	 * 
	 * @param displayFilterIcon
	 *            the flag
	 */
	public void setDisplayFilterIcon( boolean displayFilterIcon )
	{
		setOption( HTML_DISPLAY_FILTER_ICON, new Boolean( displayFilterIcon ) );
	}

	/**
	 * @return the display filter icon flag value.
	 */
	public boolean getDisplayFilterIcon( )
	{
		return getBooleanOption( HTML_DISPLAY_FILTER_ICON, false );
	}

	/**
	 * Sets the flag indicationg that if group expand/collapse icons should be
	 * displayed.
	 * 
	 * @param displayGroupIcon
	 *            the flag
	 */
	public void setDisplayGroupIcon( boolean displayGroupIcon )
	{
		setOption( HTML_DISPLAY_GROUP_ICON, new Boolean( displayGroupIcon ) );
	}

	/**
	 * @return the group expand/collapse icon flag value.
	 */
	public boolean getDisplayGroupIcon( )
	{
		return getBooleanOption( HTML_DISPLAY_GROUP_ICON, false );
	}

	/**
	 * returns the image directory that engine stores images and charts into
	 * 
	 * @return the image directory.
	 */
	public String getImageDirectory( )
	{
		return getStringOption( IMAGE_DIRECTROY );
	}

	/**
	 * sets the image directory that engine stores images and charts into
	 * 
	 * @param imageDirectory
	 *            the image directory that engine stores images and charts into
	 */
	public void setImageDirectory( String imageDirectory )
	{
		setOption( IMAGE_DIRECTROY, imageDirectory );
	}

	/**
	 * returns the base url for creating image URL
	 * 
	 * @return Rreturn the abse image url
	 */
	public String getBaseImageURL( )
	{
		return getStringOption( BASE_IMAGE_URL );
	}

	/**
	 * sets the base image URL for image handling
	 * 
	 * @param baseImageURL
	 *            the base image URL
	 */
	public void setBaseImageURL( String baseImageURL )
	{
		setOption( BASE_IMAGE_URL, baseImageURL );
	}
	
	/**
	 * Sets the flag indicationg that if the top-level table should be wrapped.
	 * 
	 * @param wrapTemplateTable
	 *            the flag
	 */
	public void setWrapTemplateTable( boolean wrapTemplateTable )
	{
		options.put( HTML_WRAP_TEMPLATE_TABLE, new Boolean( wrapTemplateTable ) );
	}

	/**
	 * @return the group expand/collapse icon flag value.
	 */
	public boolean getWrapTemplateTable( )
	{
		Object value = options.get( HTML_WRAP_TEMPLATE_TABLE );
		if ( value instanceof Boolean )
		{
			return ( (Boolean) value ).booleanValue( );
		}
		return false;
	}
	
	/**
	 * Sets the flag indicationg that if the table should be outed as fixed.
	 * 
	 * @param layoutPreference
	 *            the flag
	 */
	public void setLayoutPreference( String layoutPreference )
	{
		options.put( HTML_LAYOUT_PREFERENCE, layoutPreference );
	}

	/**
	 * @return the table layout fixed flag value.
	 */
	public String getLayoutPreference( )
	{
		Object value = options.get( HTML_LAYOUT_PREFERENCE );
		if ( value instanceof String )
		{
			return (String)value;
		}
		return null;
	}
}
