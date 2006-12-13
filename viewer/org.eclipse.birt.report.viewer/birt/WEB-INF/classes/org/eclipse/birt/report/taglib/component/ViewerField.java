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

package org.eclipse.birt.report.taglib.component;

import java.io.Serializable;
import java.util.Map;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Specifies the viewer tag parameters.
 * <p>
 * There are the following parameter attributes:
 * <ol>
 * <li>id-Specifies viewer/requester id.</li>
 * <li>name-Specifies viewer/requester name.</li>
 * <li>contextRoot-Specifies the context root of web application.</li>
 * <li>title-Specifies the report title displayed at the top</li>
 * <li>isHostPage</li>
 * <li>isCustom</li>
 * <li>scrolling</li>
 * <li>position</li>
 * <li>style</li>
 * <li>height</li>
 * <li>width</li>
 * <li>left</li>
 * <li>top</li>
 * <li>frameborder</li>
 * <li>reportDesign</li>
 * <li>reportDocument</li>
 * <li>reportletId</li>
 * <li>pattern</li>
 * <li>target</li>
 * <li>bookmark</li>
 * <li>locale</li>
 * <li>format</li>
 * <li>svg</li>
 * <li>rtl</li>
 * <li>allowMasterPage</li>
 * <li>allowPageBreak</li>
 * <li>resourceFolder</li>
 * <li>maxRowsOfRecords</li>
 * <li>forceOverwriteDocument</li>
 * <li>forceParameterPrompting</li>
 * <li>showTitle</li>
 * <li>showToolBar</li>
 * <li>showNavigationBar</li>
 * <li>forceIFrame</li>
 * </ol>
 */
public class ViewerField implements Serializable, Cloneable
{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9087611432750518446L;

	private String id;
	private String name;
	private String contextRoot;
	private String title;
	private boolean isHostPage = false;
	private boolean isCustom = false;
	private String scrolling;
	private String position;
	private String style;
	private int height = -1;
	private int width = -1;
	private int left = -1;
	private int top = -1;
	private String frameborder = "no"; //$NON-NLS-1$

	private String reportDesign;
	private String reportDocument;
	private String reportletId;
	private String pattern;
	private String target;

	private String bookmark;
	private String locale;
	private String format;
	private String svg;
	private String rtl;
	private String allowMasterPage = "true"; //$NON-NLS-1$
	private boolean allowPageBreak = true;

	private String resourceFolder;
	private int maxRowsOfRecords = -1;
	private String forceOverwriteDocument;
	private String forceParameterPrompting;

	private String showTitle;
	private String showToolBar;
	private String showNavigationBar;

	private boolean forceIFrame = false;

	/**
	 * Report parameters
	 */
	private Map parameters;

	/**
	 * Create the final URI string to preview report
	 * 
	 * @param iPattern
	 * @return
	 */
	public String createURI( String iPattern )
	{
		String uri = iPattern;
		if ( uri == null )
			uri = this.pattern;

		if ( uri == null )
		{
			uri = IBirtConstants.VIEWER_FRAMESET;
			// frameset doesn't support reportlet. If preview reportlet, force
			// to use run pattern.
			if ( ( reportDocument != null && reportletId != null )
					|| !allowPageBreak )
			{
				uri = IBirtConstants.VIEWER_RUN;
			}
		}

		// append context root setting
		if ( contextRoot != null )
			uri = "/" + contextRoot + "/" + uri; //$NON-NLS-1$//$NON-NLS-2$

		// append format setting
		if ( format != null )
		{
			uri += "?" + ParameterAccessor.PARAM_FORMAT + "=" //$NON-NLS-1$//$NON-NLS-2$
					+ urlParamValueEncode( format );
		}
		else
		{
			uri += "?" + ParameterAccessor.PARAM_FORMAT + "=" //$NON-NLS-1$//$NON-NLS-2$
					+ ParameterAccessor.PARAM_FORMAT_HTML;
		}

		// append report design
		if ( reportDesign != null )
			uri += "&" + ParameterAccessor.PARAM_REPORT + "=" + urlParamValueEncode( reportDesign ); //$NON-NLS-1$ //$NON-NLS-2$

		// append report document
		if ( reportDocument != null )
			uri += "&" + ParameterAccessor.PARAM_REPORT_DOCUMENT + "=" //$NON-NLS-1$ //$NON-NLS-2$
					+ urlParamValueEncode( reportDocument );

		// append reportlet id
		if ( reportletId != null )
			uri += "&" + ParameterAccessor.PARAM_INSTANCEID + "=" + urlParamValueEncode( reportletId ); //$NON-NLS-1$ //$NON-NLS-2$

		if ( allowPageBreak && id != null )
			uri += "&" + ParameterAccessor.PARAM_ID + "=" + urlParamValueEncode( id ); //$NON-NLS-1$//$NON-NLS-2$

		// append report title
		if ( allowPageBreak && title != null )
			uri += "&" + ParameterAccessor.PARAM_TITLE + "=" + urlParamValueEncode( title ); //$NON-NLS-1$//$NON-NLS-2$

		// append report title
		if ( allowPageBreak && showTitle != null )
			uri += "&" + ParameterAccessor.PARAM_SHOW_TITLE + "=" + urlParamValueEncode( showTitle ); //$NON-NLS-1$//$NON-NLS-2$

		// append bookmark setting
		if ( bookmark != null )
			uri += "&" + ParameterAccessor.PARAM_BOOKMARK + "=" //$NON-NLS-1$ //$NON-NLS-2$
					+ urlParamValueEncode( bookmark );

		// append target serlvet pattern setting
		if ( !isCustom && pattern != null )
			uri += "&" + ParameterAccessor.PARAM_SERVLET_PATTERN + "=" //$NON-NLS-1$ //$NON-NLS-2$
					+ urlParamValueEncode( pattern );

		// append window target setting
		if ( !isCustom && target != null )
			uri += "&" + ParameterAccessor.PARAM_TARGET + "=" //$NON-NLS-1$ //$NON-NLS-2$
					+ urlParamValueEncode( target );

		// append Locale setting
		if ( locale != null )
			uri += "&" + ParameterAccessor.PARAM_LOCALE + "=" //$NON-NLS-1$ //$NON-NLS-2$
					+ urlParamValueEncode( locale );

		// append svg setting
		if ( svg != null )
			uri += "&" + ParameterAccessor.PARAM_SVG + "=" + urlParamValueEncode( svg ); //$NON-NLS-1$//$NON-NLS-2$

		// append rtl setting
		if ( rtl != null )
			uri += "&" + ParameterAccessor.PARAM_RTL + "=" + urlParamValueEncode( rtl ); //$NON-NLS-1$ //$NON-NLS-2$

		// append masterpage setting
		if ( allowMasterPage != null )
			uri += "&" + ParameterAccessor.PARAM_MASTERPAGE + "=" //$NON-NLS-1$ //$NON-NLS-2$
					+ urlParamValueEncode( allowMasterPage );

		// append resource folder setting
		if ( resourceFolder != null )
			uri += "&" + ParameterAccessor.PARAM_RESOURCE_FOLDER + "=" //$NON-NLS-1$//$NON-NLS-2$
					+ urlParamValueEncode( resourceFolder );

		// append maxrows setting
		if ( maxRowsOfRecords >= 0 )
			uri += "&" + ParameterAccessor.PARAM_MAXROWS + "=" //$NON-NLS-1$ //$NON-NLS-2$
					+ maxRowsOfRecords;

		// append overwrite document setting
		if ( forceOverwriteDocument != null )
			uri += "&" + ParameterAccessor.PARAM_OVERWRITE + "=" //$NON-NLS-1$ //$NON-NLS-2$
					+ urlParamValueEncode( forceOverwriteDocument );

		// append parameter prompting setting
		if ( forceParameterPrompting != null )
			uri += "&" + ParameterAccessor.PARAM_PARAMETER_PROMPTING + "=" + urlParamValueEncode( forceParameterPrompting ); //$NON-NLS-1$ //$NON-NLS-2$

		// append show toolbar setting
		if ( showToolBar != null )
			uri += "&" + ParameterAccessor.PARAM_TOOLBAR + "=" + urlParamValueEncode( showToolBar ); //$NON-NLS-1$ //$NON-NLS-2$

		// append show NavigationBar setting
		if ( showNavigationBar != null )
			uri += "&" + ParameterAccessor.PARAM_NAVIGATIONBAR + "=" + urlParamValueEncode( showNavigationBar ); //$NON-NLS-1$ //$NON-NLS-2$

		return uri;
	}

	/**
	 * Encode the url parameter value
	 * 
	 * @param plain
	 * @return
	 */
	private String urlParamValueEncode( String plain )
	{
		return ParameterAccessor.urlEncode( plain,
				ParameterAccessor.UTF_8_ENCODE );
	}

	/**
	 * @return the id
	 */
	public String getId( )
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId( String id )
	{
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName( )
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * @return the contextRoot
	 */
	public String getContextRoot( )
	{
		return contextRoot;
	}

	/**
	 * @param contextRoot
	 *            the contextRoot to set
	 */
	public void setContextRoot( String contextRoot )
	{
		this.contextRoot = contextRoot;
	}

	/**
	 * @return the title
	 */
	public String getTitle( )
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle( String title )
	{
		this.title = title;
	}

	/**
	 * @return the isHostPage
	 */
	public boolean isHostPage( )
	{
		return isHostPage;
	}

	/**
	 * @param isHostPage
	 *            the isHostPage to set
	 */
	public void setHostPage( boolean isHostPage )
	{
		this.isHostPage = isHostPage;
	}

	/**
	 * @return the isCustom
	 */
	public boolean isCustom( )
	{
		return isCustom;
	}

	/**
	 * @param isCustom
	 *            the isCustom to set
	 */
	public void setCustom( boolean isCustom )
	{
		this.isCustom = isCustom;
	}

	/**
	 * @return the scrolling
	 */
	public String getScrolling( )
	{
		return scrolling;
	}

	/**
	 * @param scrolling
	 *            the scrolling to set
	 */
	public void setScrolling( String scrolling )
	{
		this.scrolling = scrolling;
	}

	/**
	 * @return the position
	 */
	public String getPosition( )
	{
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition( String position )
	{
		this.position = position;
	}

	/**
	 * @return the style
	 */
	public String getStyle( )
	{
		return style;
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle( String style )
	{
		this.style = style;
	}

	/**
	 * @return the height
	 */
	public int getHeight( )
	{
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight( int height )
	{
		this.height = height;
	}

	/**
	 * @return the width
	 */
	public int getWidth( )
	{
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth( int width )
	{
		this.width = width;
	}

	/**
	 * @return the left
	 */
	public int getLeft( )
	{
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft( int left )
	{
		this.left = left;
	}

	/**
	 * @return the top
	 */
	public int getTop( )
	{
		return top;
	}

	/**
	 * @param top
	 *            the top to set
	 */
	public void setTop( int top )
	{
		this.top = top;
	}

	/**
	 * @return the frameborder
	 */
	public String getFrameborder( )
	{
		return frameborder;
	}

	/**
	 * @param frameborder
	 *            the frameborder to set
	 */
	public void setFrameborder( String frameborder )
	{
		this.frameborder = frameborder;
	}

	/**
	 * @return the reportDesign
	 */
	public String getReportDesign( )
	{
		return reportDesign;
	}

	/**
	 * @param reportDesign
	 *            the reportDesign to set
	 */
	public void setReportDesign( String reportDesign )
	{
		this.reportDesign = reportDesign;
	}

	/**
	 * @return the reportDocument
	 */
	public String getReportDocument( )
	{
		return reportDocument;
	}

	/**
	 * @param reportDocument
	 *            the reportDocument to set
	 */
	public void setReportDocument( String reportDocument )
	{
		this.reportDocument = reportDocument;
	}

	/**
	 * @return the reportletId
	 */
	public String getReportletId( )
	{
		return reportletId;
	}

	/**
	 * @param reportletId
	 *            the reportletId to set
	 */
	public void setReportletId( String reportletId )
	{
		this.reportletId = reportletId;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern( )
	{
		return pattern;
	}

	/**
	 * @param pattern
	 *            the pattern to set
	 */
	public void setPattern( String pattern )
	{
		this.pattern = pattern;
	}

	/**
	 * @return the target
	 */
	public String getTarget( )
	{
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget( String target )
	{
		this.target = target;
	}

	/**
	 * @return the bookmark
	 */
	public String getBookmark( )
	{
		return bookmark;
	}

	/**
	 * @param bookmark
	 *            the bookmark to set
	 */
	public void setBookmark( String bookmark )
	{
		this.bookmark = bookmark;
	}

	/**
	 * @return the locale
	 */
	public String getLocale( )
	{
		return locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale( String locale )
	{
		this.locale = locale;
	}

	/**
	 * @return the format
	 */
	public String getFormat( )
	{
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat( String format )
	{
		this.format = format;
	}

	/**
	 * @return the svg
	 */
	public String getSvg( )
	{
		return svg;
	}

	/**
	 * @param svg
	 *            the svg to set
	 */
	public void setSvg( String svg )
	{
		this.svg = svg;
	}

	/**
	 * @return the rtl
	 */
	public String getRtl( )
	{
		return rtl;
	}

	/**
	 * @param rtl
	 *            the rtl to set
	 */
	public void setRtl( String rtl )
	{
		this.rtl = rtl;
	}

	/**
	 * @return the allowMasterPage
	 */
	public String getAllowMasterPage( )
	{
		return allowMasterPage;
	}

	/**
	 * @param allowMasterPage
	 *            the allowMasterPage to set
	 */
	public void setAllowMasterPage( String allowMasterPage )
	{
		this.allowMasterPage = allowMasterPage;
	}

	/**
	 * @return the allowPageBreak
	 */
	public boolean isAllowPageBreak( )
	{
		return allowPageBreak;
	}

	/**
	 * @param allowPageBreak
	 *            the allowPageBreak to set
	 */
	public void setAllowPageBreak( boolean allowPageBreak )
	{
		this.allowPageBreak = allowPageBreak;
	}

	/**
	 * @return the resourceFolder
	 */
	public String getResourceFolder( )
	{
		return resourceFolder;
	}

	/**
	 * @param resourceFolder
	 *            the resourceFolder to set
	 */
	public void setResourceFolder( String resourceFolder )
	{
		this.resourceFolder = resourceFolder;
	}

	/**
	 * @return the maxRowsOfRecords
	 */
	public int getMaxRowsOfRecords( )
	{
		return maxRowsOfRecords;
	}

	/**
	 * @param maxRowsOfRecords
	 *            the maxRowsOfRecords to set
	 */
	public void setMaxRowsOfRecords( int maxRowsOfRecords )
	{
		this.maxRowsOfRecords = maxRowsOfRecords;
	}

	/**
	 * @return the forceOverwriteDocument
	 */
	public String getForceOverwriteDocument( )
	{
		return forceOverwriteDocument;
	}

	/**
	 * @param forceOverwriteDocument
	 *            the forceOverwriteDocument to set
	 */
	public void setForceOverwriteDocument( String forceOverwriteDocument )
	{
		this.forceOverwriteDocument = forceOverwriteDocument;
	}

	/**
	 * @return the forceParameterPrompting
	 */
	public String getForceParameterPrompting( )
	{
		return forceParameterPrompting;
	}

	/**
	 * @param forceParameterPrompting
	 *            the forceParameterPrompting to set
	 */
	public void setForceParameterPrompting( String forceParameterPrompting )
	{
		this.forceParameterPrompting = forceParameterPrompting;
	}

	/**
	 * @return the showTitle
	 */
	public String getShowTitle( )
	{
		return showTitle;
	}

	/**
	 * @param showTitle
	 *            the showTitle to set
	 */
	public void setShowTitle( String showTitle )
	{
		this.showTitle = showTitle;
	}

	/**
	 * @return the showToolBar
	 */
	public String getShowToolBar( )
	{
		return showToolBar;
	}

	/**
	 * @param showToolBar
	 *            the showToolBar to set
	 */
	public void setShowToolBar( String showToolBar )
	{
		this.showToolBar = showToolBar;
	}

	/**
	 * @return the showNavigationBar
	 */
	public String getShowNavigationBar( )
	{
		return showNavigationBar;
	}

	/**
	 * @param showNavigationBar
	 *            the showNavigationBar to set
	 */
	public void setShowNavigationBar( String showNavigationBar )
	{
		this.showNavigationBar = showNavigationBar;
	}

	/**
	 * @return the forceIFrame
	 */
	public boolean isForceIFrame( )
	{
		return forceIFrame;
	}

	/**
	 * @param forceIFrame
	 *            the forceIFrame to set
	 */
	public void setForceIFrame( boolean forceIFrame )
	{
		this.forceIFrame = forceIFrame;
	}

	/**
	 * @return the parameters
	 */
	public Map getParameters( )
	{
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters( Map parameters )
	{
		this.parameters = parameters;
	}

}
