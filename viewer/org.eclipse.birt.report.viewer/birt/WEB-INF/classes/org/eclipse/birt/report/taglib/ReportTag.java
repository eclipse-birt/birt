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

package org.eclipse.birt.report.taglib;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.model.api.IModuleOption;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.BirtViewerReportDesignHandle;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.taglib.component.ParameterField;
import org.eclipse.birt.report.taglib.util.BirtTagUtil;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * This tag is used to preview report content fast. This tag will output report
 * to browser directly.
 * 
 */
public class ReportTag extends AbstractViewerTag
{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5017824486972742042L;

	/**
	 * Report output format
	 */
	private String outputFormat;

	/**
	 * Viewer Report Design Handle
	 */
	private IViewerReportDesignHandle reportDesignHandle;

	/**
	 * Check whether document existed in URL
	 */
	private boolean documentInUrl = false;

	/**
	 * Input Options information
	 */
	private InputOptions options;

	/**
	 * process tag function
	 * 
	 * @see org.eclipse.birt.report.taglib.AbstractBaseTag#__process()
	 */
	public void __process( ) throws Exception
	{
		boolean isIFrame = true;

		// Set DIV as report container
		if ( CONTAINER_DIV.equalsIgnoreCase( viewer.getReportContainer( ) ) )
			isIFrame = false;

		// if output format isn't html, force to use IFrame as report container.
		outputFormat = BirtTagUtil.getFormat( viewer.getFormat( ) );
		if ( !outputFormat
				.equalsIgnoreCase( ParameterAccessor.PARAM_FORMAT_HTML ) )
			isIFrame = true;

		if ( isIFrame )
		{
			__processWithIFrame( );
			return;
		}

		// Create Input Options
		this.options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST,
				(HttpServletRequest) pageContext.getRequest( ) );
		options.setOption( InputOptions.OPT_LOCALE, this.locale );
		options.setOption( InputOptions.OPT_RTL, Boolean.valueOf( viewer
				.getRtl( ) ) );
		options.setOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT, Boolean
				.valueOf( viewer.getAllowMasterPage( ) ) );
		options.setOption( InputOptions.OPT_SVG_FLAG, Boolean.valueOf( viewer
				.getSvg( ) ) );
		options.setOption( InputOptions.OPT_FORMAT, outputFormat );
		options.setOption( InputOptions.OPT_IS_DESIGNER, new Boolean( false ) );
		options.setOption( InputOptions.OPT_SERVLET_PATH,
				IBirtConstants.SERVLET_PATH_PREVIEW );

		// initialize engine context
		BirtReportServiceFactory.getReportService( ).setContext(
				pageContext.getServletContext( ), this.options );

		// get report design handle
		reportDesignHandle = getDesignHandle( );

		if ( viewer.isHostPage( ) )
		{
			// if set isHostPage is true, output report directly
			HttpServletResponse response = (HttpServletResponse) pageContext
					.getResponse( );
			__handleOutputReport( response.getOutputStream( ) );
		}
		else
		{

			// output to byte array
			ByteArrayOutputStream out = new ByteArrayOutputStream( );
			__handleOutputReport( out );
			String content = out.toString( );

			JspWriter writer = pageContext.getOut( );

			// write style
			writer.write( __handleStyle( content ) );

			// write script
			writer.write( __handleScript( content ) );

			// use <div> to control report content display
			writer.write( "<div id='" + viewer.getId( ) + "'" //$NON-NLS-1$ //$NON-NLS-2$
					+ __handleDivAppearance( ) + ">\n" ); //$NON-NLS-1$
			writer.write( "<div class='" + __handleBodyStyle( content ) //$NON-NLS-1$
					+ "'>\n" ); //$NON-NLS-1$
			writer.write( __handleBody( content ) + "\n" ); //$NON-NLS-1$
			writer.write( "</div>\n" ); //$NON-NLS-1$
			writer.write( "</div>\n" ); //$NON-NLS-1$
		}
	}

	/**
	 * Process report generation with IFrame
	 * 
	 * @throws Exception
	 */
	private void __processWithIFrame( ) throws Exception
	{
		if ( viewer.isHostPage( ) )
		{
			__handleIFrame( viewer.createURI( IBirtConstants.VIEWER_PREVIEW ),
					null );
		}
		else
		{
			__handleIFrame( viewer.createURI( IBirtConstants.VIEWER_PREVIEW ),
					viewer.getId( ) );
		}
	}

	/**
	 * DIV Appearance style
	 * 
	 * @return
	 */
	protected String __handleDivAppearance( )
	{
		String style = " style='"; //$NON-NLS-1$

		// position
		if ( viewer.getPosition( ) != null )
			style += "position:" + viewer.getPosition( ) + ";"; //$NON-NLS-1$//$NON-NLS-2$

		// height
		if ( viewer.getHeight( ) >= 0 )
			style += "height:" + viewer.getHeight( ) + "px;"; //$NON-NLS-1$//$NON-NLS-2$

		// width
		if ( viewer.getWidth( ) >= 0 )
			style += "width:" + viewer.getWidth( ) + "px;"; //$NON-NLS-1$//$NON-NLS-2$

		// top
		if ( viewer.getTop( ) != null )
			style += "top:" + viewer.getTop( ) + "px;"; //$NON-NLS-1$//$NON-NLS-2$

		// left
		if ( viewer.getLeft( ) != null )
			style = style + "left:" + viewer.getLeft( ) + "px;"; //$NON-NLS-1$//$NON-NLS-2$

		// scroll
		if ( SCROLLING_YES.equalsIgnoreCase( viewer.getScrolling( ) ) )
		{
			style = style + "overflow:scroll"; //$NON-NLS-1$
		}
		else if ( SCROLLING_AUTO.equalsIgnoreCase( viewer.getScrolling( ) ) )
		{
			style = style + "overflow:auto"; //$NON-NLS-1$
		}

		// style
		if ( viewer.getStyle( ) != null )
			style += viewer.getStyle( ) + ";"; //$NON-NLS-1$

		style += "' "; //$NON-NLS-1$	

		return style;
	}

	/**
	 * Handle style content
	 * 
	 * @param content
	 * @param Exception
	 * @return
	 */
	protected String __handleStyle( String content ) throws Exception
	{
		String style = BLANK_STRING;

		if ( content == null )
			return style;

		// parse style content
		Pattern p = Pattern.compile( "<\\s*style[^\\>]*\\>", //$NON-NLS-1$
				Pattern.CASE_INSENSITIVE );
		Matcher m = p.matcher( content );
		while ( m.find( ) )
		{
			int start = m.end( );
			int end = content.toLowerCase( ).indexOf( "</style>", start ); //$NON-NLS-1$
			style = style + content.substring( start + 1, end ) + "\n"; //$NON-NLS-1$
		}

		// replace the style section with id
		style = style.replaceAll( ".style", ".style" + viewer.getId( ) ); //$NON-NLS-1$//$NON-NLS-2$
		style = "<style type=\"text/css\">\n" + style + "\n</style>\n"; //$NON-NLS-1$ //$NON-NLS-2$

		return style;
	}

	/**
	 * Returns body style content
	 * 
	 * @param content
	 * @return
	 */
	protected String __handleBodyStyle( String content )
	{
		String bodyStyleId = BLANK_STRING;

		if ( content == null )
			return bodyStyleId;

		Pattern p = Pattern.compile( "<\\s*body([^\\>]*)\\>", //$NON-NLS-1$
				Pattern.CASE_INSENSITIVE );
		Matcher m = p.matcher( content );
		if ( m.find( ) )
		{
			for ( int i = 1; i < m.groupCount( ) + 1; i++ )
			{
				String group = m.group( i );
				if ( group == null )
					continue;

				Pattern pl = Pattern.compile( "class\\s*=\\s*\"([^\"]+)\"", //$NON-NLS-1$
						Pattern.CASE_INSENSITIVE );
				Matcher ml = pl.matcher( group.trim( ) );
				if ( ml.find( ) )
				{
					// find body style id
					bodyStyleId = ml.group( 1 ).trim( );
					break;
				}
			}
		}

		bodyStyleId = bodyStyleId.replaceAll( "style", "style" //$NON-NLS-1$ //$NON-NLS-2$
				+ viewer.getId( ) );

		return bodyStyleId;
	}

	/**
	 * Handle script content
	 * 
	 * @param content
	 * @return
	 */
	protected String __handleScript( String content )
	{
		String script = BLANK_STRING;

		if ( content == null )
			return script;

		// get head content
		String head = __handleHead( content );
		if ( head == null )
			return script;

		// clear the comment fragments
		Pattern p = Pattern.compile( "<\\s*!--" ); //$NON-NLS-1$
		Matcher m = p.matcher( head );
		while ( m.find( ) )
		{
			int start = m.start( );
			int end = head.indexOf( "-->", start ); //$NON-NLS-1$
			if ( end > 0 )
			{
				String preTemp = head.substring( 0, start );
				String lastTemp = head.substring( end + 3 );
				head = preTemp + lastTemp;
			}
		}

		// parse the script fragments
		p = Pattern.compile( "<\\s*script[^\\>]*\\>", //$NON-NLS-1$
				Pattern.CASE_INSENSITIVE );
		m = p.matcher( head );
		while ( m.find( ) )
		{
			int start = m.start( );
			int end = head.toLowerCase( ).indexOf( "</script>", start ); //$NON-NLS-1$
			if ( end > 0 )
				script = script + head.substring( start, end + 9 ) + "\n"; //$NON-NLS-1$
		}

		return script;
	}

	/**
	 * Handle head content
	 * 
	 * @param content
	 * @return
	 */
	protected String __handleHead( String content )
	{
		if ( content == null )
			return BLANK_STRING;

		String head = BLANK_STRING;

		try
		{
			Pattern p = Pattern.compile( "<\\s*head[^\\>]*\\>", //$NON-NLS-1$
					Pattern.CASE_INSENSITIVE );
			Matcher m = p.matcher( content );
			if ( m.find( ) )
			{
				int start = m.end( );
				int end = content.toLowerCase( ).indexOf( "</head>" ); //$NON-NLS-1$
				head = content.substring( start + 1, end );
			}
		}
		catch ( Exception e )
		{
		}

		return head;
	}

	/**
	 * Handle body content
	 * 
	 * @param content
	 * @return
	 */
	protected String __handleBody( String content )
	{
		String body = content;

		if ( content == null )
			return BLANK_STRING;

		try
		{
			Pattern p = Pattern.compile( "<\\s*body[^\\>]*\\>", //$NON-NLS-1$
					Pattern.CASE_INSENSITIVE );
			Matcher m = p.matcher( content );
			if ( m.find( ) )
			{
				int start = m.end( );
				int end = content.toLowerCase( ).indexOf( "</body>" ); //$NON-NLS-1$
				body = content.substring( start + 1, end );
			}
		}
		catch ( Exception e )
		{
			body = content;
		}

		// handle style class
		body = body.replaceAll( "class=\"style", "class=\"style" //$NON-NLS-1$ //$NON-NLS-2$
				+ viewer.getId( ) );

		return body;
	}

	/**
	 * handle generate report content
	 * 
	 * @param out
	 * @throws Exception
	 */
	protected void __handleOutputReport( OutputStream out ) throws Exception
	{
		// Set preview report max rows
		ReportEngineService.getInstance( ).setMaxRows(
				viewer.getMaxRowsOfRecords( ) );

		if ( this.documentInUrl )
		{
			__renderDocument( out );
		}
		else
		{
			__renderReport( out );
		}
	}

	/**
	 * Render context from document file
	 * 
	 * @param out
	 * @throws Exception
	 */
	private void __renderDocument( OutputStream out ) throws Exception
	{
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest( );

		// Get document file path
		String documentFile = ParameterAccessor.getReportDocument( request,
				viewer.getReportDocument( ), false );
		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( null, documentFile, getModuleOptions( ) );
		try
		{
			Locale locale = (Locale) this.options
					.getOption( InputOptions.OPT_LOCALE );
			Boolean isMasterPageContent = (Boolean) this.options
					.getOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT );
			boolean isMasterPage = isMasterPageContent == null
					? false
					: isMasterPageContent.booleanValue( );
			Boolean svgFlag = (Boolean) this.options
					.getOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT );
			boolean isSvg = svgFlag == null ? false : svgFlag.booleanValue( );
			Boolean isRtl = (Boolean) this.options
					.getOption( InputOptions.OPT_RTL );
			String servletPath = (String) this.options
					.getOption( InputOptions.OPT_SERVLET_PATH );

			if ( viewer.getReportletId( ) != null )
			{
				// Render the reportlet
				ReportEngineService.getInstance( )
						.renderReportlet( out, request, doc,
								viewer.getReportletId( ), isMasterPage, isSvg,
								null, locale, isRtl.booleanValue( ),
								servletPath );
			}
			else
			{
				// Render the report document file
				ReportEngineService.getInstance( ).renderReport( out, request,
						doc, viewer.getPageNum( ), viewer.getPageRange( ),
						isMasterPage, isSvg, null, locale,
						isRtl.booleanValue( ), servletPath );
			}
		}
		finally
		{
			if ( doc != null )
				doc.close( );
		}
	}

	/**
	 * Render report content from design file
	 * 
	 * @param out
	 * @throws Exception
	 */
	private void __renderReport( OutputStream out ) throws Exception
	{
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest( );
		Locale locale = (Locale) this.options
				.getOption( InputOptions.OPT_LOCALE );
		Boolean isMasterPageContent = (Boolean) this.options
				.getOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT );
		Boolean svgFlag = (Boolean) this.options
				.getOption( InputOptions.OPT_SVG_FLAG );
		String format = (String) this.options
				.getOption( InputOptions.OPT_FORMAT );
		Boolean isRtl = (Boolean) this.options.getOption( InputOptions.OPT_RTL );
		String servletPath = (String) this.options
				.getOption( InputOptions.OPT_SERVLET_PATH );
		IReportRunnable runnable = (IReportRunnable) this.reportDesignHandle
				.getDesignObject( );

		// Get report title
		String reportTitle = ParameterAccessor.htmlDecode( BirtUtility
				.getTitleFromDesign( reportDesignHandle ) );

		// Prepare the report parameters
		Map params = __handleParameters( reportDesignHandle, null );

		// Prepare the display text of report parameters
		Map displayTexts = BirtUtility.getDisplayTexts( null,
				(HttpServletRequest) pageContext.getRequest( ) );

		// Render report
		ReportEngineService.getInstance( ).runAndRenderReport( request,
				runnable, out, format, locale, isRtl.booleanValue( ), params,
				isMasterPageContent.booleanValue( ), svgFlag.booleanValue( ),
				Boolean.TRUE, null, null, displayTexts, servletPath,
				reportTitle );
	}

	/**
	 * Handle report parameters
	 * 
	 * @param reportDesignHandle
	 * @param params
	 * @return
	 */
	protected Map __handleParameters(
			IViewerReportDesignHandle reportDesignHandle, Map params )
			throws Exception
	{
		if ( params == null )
			params = new HashMap( );

		// get report parameter handle list
		List parameterList = BirtUtility.getParameterList( reportDesignHandle );
		if ( parameterList == null )
			return params;

		// get parameter map
		Map paramMap = viewer.getParameters( );

		Iterator it = parameterList.iterator( );
		while ( it.hasNext( ) )
		{
			Object handle = it.next( );
			if ( handle instanceof ScalarParameterHandle )
			{
				ScalarParameterHandle parameterHandle = (ScalarParameterHandle) handle;
				String paramName = parameterHandle.getName( );
				ParameterField field = (ParameterField) paramMap
						.get( paramName );
				String paramValue;
				Object paramObj;
				if ( field != null )
				{
					paramObj = field.getValue( );
					if ( paramObj == null )
					{
						// if set null parameter value
						params.put( paramName, null );
						continue;
					}

					// if set parameter object
					if ( !( paramObj instanceof String ) )
					{
						params.put( paramName, paramObj );
						continue;
					}
					else
					{
						paramValue = (String) paramObj;
					}

					// handle parameter using String value
					params.put( paramName, getParameterValue( parameterHandle,
							field, paramValue ) );
				}
				else
				{
					// set default value as parameter value;
					paramObj = getReportService( ).getParameterDefaultValue(
							reportDesignHandle, paramName, this.options );

					params.put( paramName, paramObj );
				}
			}
		}

		return params;
	}

	/**
	 * parse parameter value by string value
	 * 
	 * @param handle
	 * @param field
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private Object getParameterValue( ScalarParameterHandle handle,
			ParameterField field, String value ) throws Exception
	{
		// get parameter data type
		String dataType = handle.getDataType( );

		// if String type, return String value
		if ( DesignChoiceConstants.PARAM_TYPE_STRING
				.equalsIgnoreCase( dataType ) )
		{
			return value;
		}

		// convert parameter to object
		String pattern = field.getPattern( );
		if ( pattern == null || pattern.length( ) <= 0 )
		{
			pattern = handle.getPattern( );
		}

		return DataUtil.validate( handle.getDataType( ), pattern, value,
				this.locale, field.isLocale( ) );
	}

	/**
	 * Returns Report Service Object
	 * 
	 * @return
	 */
	protected IViewerReportService getReportService( )
	{
		return BirtReportServiceFactory.getReportService( );
	}

	/**
	 * If a report file name is a relative path, it is relative to document
	 * folder. So if a report file path is relative path, it's absolute path is
	 * synthesized by appending file path to the document folder path.
	 * 
	 * @param file
	 * @return
	 */

	protected String createAbsolutePath( String filePath )
	{
		if ( filePath != null && filePath.trim( ).length( ) > 0
				&& ParameterAccessor.isRelativePath( filePath ) )
		{
			return ParameterAccessor.workingFolder + File.separator + filePath;
		}
		return filePath;
	}

	/**
	 * Returns report design handle
	 * 
	 * @return IViewerReportDesignHandle
	 * @throws Exception
	 */
	protected IViewerReportDesignHandle getDesignHandle( ) throws Exception
	{
		if ( viewer == null )
			return null;

		IViewerReportDesignHandle design = null;
		IReportRunnable reportRunnable = null;

		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest( );

		// Get the absolute report design and document file path
		String designFile = ParameterAccessor.getReport( request, viewer
				.getReportDesign( ) );
		String documentFile = ParameterAccessor.getReportDocument( request,
				viewer.getReportDocument( ), false );

		// check if document file path is valid
		boolean isValidDocument = ParameterAccessor
				.isValidFilePath( ParameterAccessor.getParameter( request,
						ParameterAccessor.PARAM_REPORT_DOCUMENT ) );
		if ( documentFile != null && isValidDocument )
		{
			// open the document instance
			IReportDocument reportDocumentInstance = ReportEngineService
					.getInstance( ).openReportDocument( designFile,
							documentFile, getModuleOptions( ) );

			if ( reportDocumentInstance != null )
			{
				this.documentInUrl = true;
				reportRunnable = reportDocumentInstance.getReportRunnable( );
				reportDocumentInstance.close( );
			}
		}

		// if report runnable is null, then get it from design file
		if ( reportRunnable == null )
		{
			// if only set __document parameter, throw exception directly
			if ( documentFile != null && designFile == null )
			{
				if ( isValidDocument )
					throw new ViewerException(
							ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_FILE_ERROR,
							new String[]{documentFile} );
				else
					throw new ViewerException(
							ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_ACCESS_ERROR,
							new String[]{documentFile} );
			}

			// check if the report file path is valid
			if ( !ParameterAccessor.isValidFilePath( ParameterAccessor
					.getParameter( request, ParameterAccessor.PARAM_REPORT ) ) )
			{
				throw new ViewerException(
						ResourceConstants.GENERAL_EXCEPTION_REPORT_ACCESS_ERROR,
						new String[]{designFile} );
			}
			else
			{
				reportRunnable = BirtUtility.getRunnableFromDesignFile(
						request, designFile, this.getModuleOptions( ) );

				if ( reportRunnable == null )
				{
					throw new ViewerException(
							ResourceConstants.GENERAL_EXCEPTION_REPORT_FILE_ERROR,
							new String[]{designFile} );
				}
			}
		}

		if ( reportRunnable != null )
		{
			design = new BirtViewerReportDesignHandle(
					IViewerReportDesignHandle.RPT_RUNNABLE_OBJECT,
					reportRunnable );
		}

		return design;
	}

	/**
	 * Create Module Options
	 * 
	 * @param viewer
	 * @return
	 */
	protected Map getModuleOptions( )
	{
		Map options = new HashMap( );
		String resourceFolder = viewer.getResourceFolder( );
		if ( resourceFolder == null || resourceFolder.trim( ).length( ) <= 0 )
			resourceFolder = ParameterAccessor.birtResourceFolder;

		options.put( IModuleOption.RESOURCE_FOLDER_KEY, resourceFolder );
		return options;
	}

	/**
	 * @param reportContainer
	 *            the reportContainer to set
	 */
	public void setReportContainer( String reportContainer )
	{
		viewer.setReportContainer( reportContainer );
	}
}
