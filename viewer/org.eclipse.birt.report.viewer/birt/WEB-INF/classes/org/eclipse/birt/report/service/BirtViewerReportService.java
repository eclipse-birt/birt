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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.ICascadingParameterGroup;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.api.ExportedColumn;
import org.eclipse.birt.report.service.api.ExportedResultSet;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.OutputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ParameterGroupDefinition;
import org.eclipse.birt.report.service.api.ParameterSelectionChoice;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.service.api.ToC;
import org.eclipse.birt.report.soapengine.api.Column;
import org.eclipse.birt.report.soapengine.api.ResultSet;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.ParameterAccessor;

import com.ibm.icu.util.ULocale;

public class BirtViewerReportService implements IViewerReportService
{

	public BirtViewerReportService( ServletContext context )
	{
		try
		{
			ReportEngineService.initEngineInstance( context );
		}
		catch ( BirtException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
	}

	public String runReport( IViewerReportDesignHandle design,
			String outputDocName, InputOptions runOptions, Map parameters )
			throws ReportServiceException
	{
		return runReport( design, outputDocName, runOptions, parameters, null );
	}

	public String runReport( IViewerReportDesignHandle design,
			String outputDocName, InputOptions runOptions, Map parameters,
			Map displayTexts ) throws ReportServiceException
	{
		IReportRunnable runnable;
		HttpServletRequest request = (HttpServletRequest) runOptions
				.getOption( InputOptions.OPT_REQUEST );
		Locale locale = (Locale) runOptions.getOption( InputOptions.OPT_LOCALE );

		ViewerAttributeBean attrBean = (ViewerAttributeBean) request
				.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
		// Set parameters
		Map parsedParams = attrBean.getParameters( );
		if ( parameters != null )
		{
			parsedParams.putAll( parameters );
		}

		// Set display Text of select parameters
		Map displayTextMap = attrBean.getDisplayTexts( );
		if ( displayTexts != null )
		{
			displayTextMap.putAll( displayTexts );
		}

		runnable = (IReportRunnable) design.getDesignObject( );

		try
		{
			ReportEngineService.getInstance( ).runReport( request, runnable,
					outputDocName, locale, parsedParams, displayTextMap );
		}
		catch ( RemoteException e )
		{
			e.printStackTrace( );
			throw new ReportServiceException( e.getLocalizedMessage( ) );
		}
		return outputDocName;
	}

	public IViewerReportDesignHandle getReportDesignHandle( String docName,
			InputOptions options )
	{
		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( getReportDesignName( options ), docName,
						getModuleOptions( options ) );
		String fileName = doc.getReportRunnable( ).getReportName( );
		doc.close( );
		// TODO: What is content type?
		IViewerReportDesignHandle design = new BirtViewerReportDesignHandle(
				null, fileName );
		return design;
	}

	// TODO: Maybe change pageID to long?
	public ByteArrayOutputStream getPage( String docName, String pageID,
			InputOptions renderOptions, List activeIds )
			throws ReportServiceException
	{
		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( getReportDesignName( renderOptions ),
						docName, getModuleOptions( renderOptions ) );
		HttpServletRequest request = (HttpServletRequest) renderOptions
				.getOption( InputOptions.OPT_REQUEST );
		Locale locale = (Locale) renderOptions
				.getOption( InputOptions.OPT_LOCALE );
		Boolean isMasterPageContent = (Boolean) renderOptions
				.getOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT );
		Boolean svgFlag = (Boolean) renderOptions
				.getOption( InputOptions.OPT_SVG_FLAG );
		Long pageNum = Long.valueOf( pageID );
		Boolean isRtl = (Boolean) renderOptions
				.getOption( InputOptions.OPT_RTL );
		try
		{
			ByteArrayOutputStream os = ReportEngineService.getInstance( )
					.renderReport( request, doc, pageNum.longValue( ),
							isMasterPageContent.booleanValue( ),
							svgFlag.booleanValue( ), activeIds, locale,
							isRtl.booleanValue( ) );
			doc.close( );
			return os;

		}
		catch ( RemoteException e )
		{
			doc.close( );
			throw new ReportServiceException( e.getLocalizedMessage( ) );
		}
	}

	public ByteArrayOutputStream getPageByBookmark( String docName,
			String bookmark, InputOptions renderOptions, List activeIds )
			throws ReportServiceException
	{
		long pageNum = getPageNumberByBookmark( docName, bookmark,
				renderOptions );
		// TODO: Why String and not long in getPage?
		return getPage( docName, pageNum + "", renderOptions, activeIds ); //$NON-NLS-1$
	}

	public ByteArrayOutputStream getPageByObjectId( String docName,
			String objectId, InputOptions renderOptions, List activeIds )
			throws ReportServiceException
	{
		// TODO: Implement
		return null;
	}

	public void renderReportlet( String docName, String objectId,
			InputOptions renderOptions, List activeIds, OutputStream out )
			throws ReportServiceException
	{
		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( getReportDesignName( renderOptions ),
						docName, getModuleOptions( renderOptions ) );
		HttpServletRequest request = (HttpServletRequest) renderOptions
				.getOption( InputOptions.OPT_REQUEST );
		Locale locale = (Locale) renderOptions
				.getOption( InputOptions.OPT_LOCALE );
		Boolean isMasterPageContent = (Boolean) renderOptions
				.getOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT );
		boolean isMasterPage = isMasterPageContent == null
				? false
				: isMasterPageContent.booleanValue( );
		Boolean svgFlag = (Boolean) renderOptions
				.getOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT );
		boolean isSvg = svgFlag == null ? false : svgFlag.booleanValue( );
		Boolean isRtl = (Boolean) renderOptions
				.getOption( InputOptions.OPT_RTL );
		String servletPath = (String) renderOptions
				.getOption( InputOptions.OPT_SERVLET_PATH );
		try
		{
			ReportEngineService.getInstance( ).renderReportlet( out, request,
					doc, objectId, isMasterPage, isSvg, null, locale,
					isRtl.booleanValue( ), servletPath );
			doc.close( );
		}
		catch ( RemoteException e )
		{
			doc.close( );
			throw new ReportServiceException( e.getLocalizedMessage( ) );
		}
	}

	public void renderReport( String docName, String pageRange,
			InputOptions renderOptions, OutputStream out )
			throws ReportServiceException
	{
		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( getReportDesignName( renderOptions ),
						docName, getModuleOptions( renderOptions ) );
		HttpServletRequest request = (HttpServletRequest) renderOptions
				.getOption( InputOptions.OPT_REQUEST );
		Locale locale = (Locale) renderOptions
				.getOption( InputOptions.OPT_LOCALE );
		Boolean isMasterPageContent = (Boolean) renderOptions
				.getOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT );
		boolean isMasterPage = isMasterPageContent == null
				? true
				: isMasterPageContent.booleanValue( );
		Boolean svgFlag = (Boolean) renderOptions
				.getOption( InputOptions.OPT_SVG_FLAG );
		boolean isSvg = svgFlag == null ? false : svgFlag.booleanValue( );
		Boolean isRtl = (Boolean) renderOptions
				.getOption( InputOptions.OPT_RTL );
		String servletPath = (String) renderOptions
				.getOption( InputOptions.OPT_SERVLET_PATH );

		Long pageNum = null;
		if ( pageRange != null && pageRange.trim( ).length( ) >= 0 )
			pageNum = Long.valueOf( pageRange );
		long page = 1;
		if ( pageNum != null )
			page = pageNum.longValue( );
		try
		{
			ReportEngineService.getInstance( ).renderReport( out, request, doc,
					page, isMasterPage, isSvg, null, locale,
					isRtl.booleanValue( ), servletPath );
			doc.close( );

		}
		catch ( RemoteException e )
		{
			doc.close( );
			throw new ReportServiceException( e.getLocalizedMessage( ) );
		}
	}

	public void extractResultSet( String docName, String resultSetId,
			Collection columns, Set filters, InputOptions options,
			OutputStream out ) throws ReportServiceException
	{

		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( getReportDesignName( options ), docName,
						getModuleOptions( options ) );
		Locale locale = (Locale) options.getOption( InputOptions.OPT_LOCALE );
		HttpServletRequest request = (HttpServletRequest) options
				.getOption( InputOptions.OPT_REQUEST );
		// TODO: Filters are not used...
		try
		{
			ReportEngineService.getInstance( ).extractData( doc, resultSetId,
					columns, locale, out,
					ParameterAccessor.getExportEncoding( request ) );
			doc.close( );
		}
		catch ( RemoteException e )
		{
			doc.close( );
			throw new ReportServiceException( e.getLocalizedMessage( ) );
		}
	}

	public List getResultSetsMetadata( String docName, InputOptions options )
			throws ReportServiceException
	{
		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( getReportDesignName( options ), docName,
						getModuleOptions( options ) );

		// if doucment file doesn't exist, throw exception
		if ( doc == null )
		{
			throw new ReportServiceException(
					BirtResources
							.getMessage( ResourceConstants.REPORT_SERVICE_EXCEPTION_EXTRACT_DATA_NO_DOCUMENT ) );
		}

		ResultSet[] resultSetArray;
		try
		{
			resultSetArray = ReportEngineService.getInstance( ).getResultSets(
					doc );
			doc.close( );
		}
		catch ( RemoteException e )
		{
			doc.close( );
			e.printStackTrace( );
			throw new ReportServiceException( e.getLocalizedMessage( ) );
		}
		if ( resultSetArray == null || resultSetArray.length < 0 )
		{
			throw new ReportServiceException(
					BirtResources
							.getMessage( ResourceConstants.REPORT_SERVICE_EXCEPTION_EXTRACT_DATA_NO_RESULT_SET ) );
		}

		return transformResultSetArray( resultSetArray );
	}

	public List getResultSetsMetadata( String docName, String instanceId,
			InputOptions options ) throws ReportServiceException
	{
		// TODO: Implement
		return null;
	}

	public void getImage( String docName, String imageId, OutputStream out,
			InputOptions options ) throws ReportServiceException
	{
		try
		{
			// TODO: docName is not used. why?
			ReportEngineService.getInstance( ).renderImage( imageId, out );
		}
		catch ( RemoteException e )
		{
			throw new ReportServiceException( e.getLocalizedMessage( ) );
		}

	}

	public ToC getTOC( String docName, String tocId, InputOptions options )
			throws ReportServiceException
	{
		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( getReportDesignName( options ), docName,
						getModuleOptions( options ) );

		TOCNode node = null;
		if ( doc != null )
		{
			Locale locale = null;
			if ( options != null )
				locale = (Locale) options.getOption( InputOptions.OPT_LOCALE );
			if ( locale == null )
				locale = Locale.getDefault( );
			ITOCTree tocTree = doc.getTOCTree(
					DesignChoiceConstants.FORMAT_TYPE_VIEWER, ULocale
							.forLocale( locale ) );

			if ( tocId != null )
			{
				node = tocTree.findTOC( tocId );

			}
			else
			{
				node = tocTree.findTOC( null );
			}
		}

		if ( node == null )
		{
			if ( doc != null )
				doc.close( );
			throw new ReportServiceException(
					BirtResources
							.getMessage( ResourceConstants.REPORT_SERVICE_EXCEPTION_INVALID_TOC ) );
		}

		if ( doc != null )
			doc.close( );
		return transformTOCNode( node );
	}

	public String findTocByName( String docName, String name,
			InputOptions options )
	{

		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( getReportDesignName( options ), docName,
						getModuleOptions( options ) );

		String tocId = null;

		if ( doc != null )
		{
			Locale locale = null;
			if ( options != null )
				locale = (Locale) options.getOption( InputOptions.OPT_LOCALE );
			if ( locale == null )
				locale = Locale.getDefault( );
			ITOCTree tocTree = doc.getTOCTree(
					DesignChoiceConstants.FORMAT_TYPE_VIEWER, ULocale
							.forLocale( locale ) );
			assert tocTree != null;
			List tocList = tocTree.findTOCByValue( name );
			if ( tocList != null && tocList.size( ) > 0 )
				return ( (TOCNode) tocList.get( 0 ) ).getBookmark( );

			doc.close( );
		}
		return tocId;

	}

	public long getPageCount( String docName, InputOptions options,
			OutputOptions outputOptions ) throws ReportServiceException
	{
		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( getReportDesignName( options ), docName,
						getModuleOptions( options ) );
		long count = doc.getPageCount( );
		doc.close( );
		return count;
	}

	public Collection getParameterDefinitions(
			IViewerReportDesignHandle design, InputOptions runOptions,
			boolean includeGroups ) throws ReportServiceException
	{
		IGetParameterDefinitionTask task = null;
		try
		{
			task = getParameterDefinitionTask( design, runOptions );
			if ( task != null )
			{
				Collection params = task.getParameterDefns( true );
				return convertEngineParameters( params, includeGroups );
			}
		}
		finally
		{
			if ( task != null )
				task.close( );
		}

		return null;
	}

	public Map getParameterValues( String docName, InputOptions options )
			throws ReportServiceException
	{
		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( getReportDesignName( options ), docName,
						getModuleOptions( options ) );
		Map paramValues = doc.getParameterValues( );
		doc.close( );
		return paramValues;
	}

	public Collection getSelectionListForCascadingGroup(
			IViewerReportDesignHandle design, String groupName,
			Object[] groupKeys, InputOptions options )
			throws ReportServiceException
	{
		IGetParameterDefinitionTask task = null;
		HttpServletRequest request = (HttpServletRequest) options
				.getOption( InputOptions.OPT_REQUEST );
		try
		{
			task = getParameterDefinitionTask( design, options );
			if ( task != null )
			{
				ViewerAttributeBean bean = getViewerAttrBean( options );
				if ( bean != null )
				{
					task.setLocale( bean.getLocale( ) );
					task.setParameterValues( bean.getParameters( ) );
				}

				// Add task into session
				BirtUtility.addTask( request, task );

				task.evaluateQuery( groupName );
				Collection selectionList = task
						.getSelectionListForCascadingGroup( groupName,
								groupKeys );
				return convertEngineParameterSelectionChoice( selectionList );
			}

		}
		finally
		{
			// Remove task from http session
			BirtUtility.removeTask( request );

			if ( task != null )
				task.close( );
		}

		return null;
	}

	public Collection getParameterSelectionList(
			IViewerReportDesignHandle design, InputOptions runOptions,
			String paramName ) throws ReportServiceException
	{
		IGetParameterDefinitionTask task = null;
		try
		{
			task = getParameterDefinitionTask( design, runOptions );
			if ( task != null )
			{
				ViewerAttributeBean bean = getViewerAttrBean( runOptions );
				if ( bean != null )
				{
					task.setLocale( bean.getLocale( ) );
					task.setParameterValues( bean.getParameters( ) );
				}

				Collection selectionList = task.getSelectionList( paramName );
				return convertEngineParameterSelectionChoice( selectionList );
			}
		}
		finally
		{
			if ( task != null )
				task.close( );
		}

		return null;
	}

	private IGetParameterDefinitionTask getParameterDefinitionTask(
			IViewerReportDesignHandle design, InputOptions options )
			throws ReportServiceException
	{
		IGetParameterDefinitionTask task;
		if ( design.getContentType( ) == IViewerReportDesignHandle.RPT_RUNNABLE_OBJECT )
		{
			// IReportRunnable is specified in IViewerReportDesignHandle.
			IReportRunnable runnable = (IReportRunnable) design
					.getDesignObject( );
			task = ReportEngineService.getInstance( )
					.createGetParameterDefinitionTask( runnable );
		}
		else
		{
			// report design name is specified in IViewerReportDesignHandle.
			try
			{
				task = getParameterDefinitionTask( design.getFileName( ),
						options );
			}
			catch ( EngineException e )
			{
				throw new ReportServiceException( e.getLocalizedMessage( ) );
			}
		}
		return task;
	}

	public long getPageNumberByBookmark( String docName, String bookmark,
			InputOptions options ) throws ReportServiceException
	{
		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( getReportDesignName( options ), docName,
						getModuleOptions( options ) );
		long pageNumber = doc.getPageNumber( bookmark );
		doc.close( );
		return pageNumber;
	}

	public long getPageNumberByObjectId( String docName, String objectId,
			InputOptions options ) throws ReportServiceException
	{
		IReportDocument doc = ReportEngineService.getInstance( )
				.openReportDocument( getReportDesignName( options ), docName,
						getModuleOptions( options ) );
		long pageNumber = doc.getPageNumber( objectId );
		doc.close( );
		return pageNumber;
	}

	public void runAndRenderReport( IViewerReportDesignHandle design,
			String outputDocName, InputOptions options, Map parameters,
			OutputStream out, List activeIds ) throws ReportServiceException
	{
		runAndRenderReport( design, outputDocName, options, parameters, out,
				activeIds, null );
	}

	public void runAndRenderReport( IViewerReportDesignHandle design,
			String outputDocName, InputOptions options, Map parameters,
			OutputStream out, List activeIds, Map displayTexts )
			throws ReportServiceException
	{
		// TODO: outputDocName is not used...
		HttpServletRequest request = (HttpServletRequest) options
				.getOption( InputOptions.OPT_REQUEST );
		Locale locale = (Locale) options.getOption( InputOptions.OPT_LOCALE );
		Boolean isMasterPageContent = (Boolean) options
				.getOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT );
		Boolean svgFlag = (Boolean) options
				.getOption( InputOptions.OPT_SVG_FLAG );
		String format = (String) options
				.getOption( InputOptions.OPT_RENDER_FORMAT );
		Boolean isRtl = (Boolean) options.getOption( InputOptions.OPT_RTL );
		String servletPath = (String) options
				.getOption( InputOptions.OPT_SERVLET_PATH );
		try
		{
			IReportRunnable runnable = (IReportRunnable) design
					.getDesignObject( );

			ReportEngineService.getInstance( ).runAndRenderReport( request,
					runnable, out, format, locale, isRtl.booleanValue( ),
					parameters, isMasterPageContent.booleanValue( ),
					svgFlag.booleanValue( ), displayTexts, servletPath );
		}
		catch ( RemoteException e )
		{
			throw new ReportServiceException( e.getLocalizedMessage( ) );
		}
	}

	public Object getParameterDefaultValue( IViewerReportDesignHandle design,
			String parameterName, InputOptions options )
			throws ReportServiceException
	{
		IGetParameterDefinitionTask task = null;
		Object defaultValue = null;
		try
		{
			task = getParameterDefinitionTask( design, options );
			if ( task != null )
				defaultValue = task.getDefaultValue( parameterName );
		}
		finally
		{
			if ( task != null )
				task.close( );
		}

		return defaultValue;
	}

	public void setContext( Object context, InputOptions options )
	{
		HttpServletRequest request = (HttpServletRequest) options
				.getOption( InputOptions.OPT_REQUEST );
		ServletContext servletContext = (ServletContext) context;
		ReportEngineService.getInstance( ).setEngineContext( servletContext,
				request );
	}

	public IReportRunnable getReportRunnable( IViewerReportDesignHandle design,
			Map moduleOptions ) throws ReportServiceException
	{
		IReportRunnable runnable;
		if ( design.getContentType( ) == IViewerReportDesignHandle.RPT_RUNNABLE_OBJECT )
		{
			// IReportRunnable is specified in IViewerReportDesignHandle.
			runnable = (IReportRunnable) design.getDesignObject( );
		}
		else
		{
			// report design name is specified in IViewerReportDesignHandle.
			try
			{
				runnable = ReportEngineService.getInstance( ).openReportDesign(
						design.getFileName( ), moduleOptions );
			}
			catch ( EngineException e )
			{
				throw new ReportServiceException( e.getLocalizedMessage( ) );
			}
		}
		return runnable;
	}

	private static ToC transformTOCNode( TOCNode node )
	{
		ToC toc = new ToC( node.getNodeID( ), node.getDisplayString( ), node
				.getBookmark( ) );
		toc.setChildren( getToCChildren( node ) );
		return toc;
	}

	private static List getToCChildren( TOCNode node )
	{
		if ( node.getChildren( ) == null )
			return null;
		List children = node.getChildren( );
		List ret = new ArrayList( );
		Iterator it = children.iterator( );
		while ( it.hasNext( ) )
		{
			TOCNode childNode = (TOCNode) it.next( );
			ToC child = new ToC( childNode.getNodeID( ), childNode
					.getDisplayString( ), childNode.getBookmark( ) );
			// Recursion to transform all children etc...
			child.setChildren( getToCChildren( childNode ) );
			ret.add( child );
		}
		return ret;
	}

	private IGetParameterDefinitionTask getParameterDefinitionTask(
			String reportDesignName, InputOptions options )
			throws EngineException
	{

		IReportRunnable runnable = ReportEngineService
				.getInstance( )
				.openReportDesign( reportDesignName, getModuleOptions( options ) );
		IGetParameterDefinitionTask paramTask = ReportEngineService
				.getInstance( ).createGetParameterDefinitionTask( runnable );
		return paramTask;
	}

	private List transformResultSetArray( ResultSet[] resultSetArray )
	{
		List ret = new ArrayList( );
		for ( int i = 0; i < resultSetArray.length; i++ )
		{
			ResultSet rs = resultSetArray[i];
			String queryName = rs.getQueryName( );
			Column[] columnArray = rs.getColumn( );
			List columns = new ArrayList( );
			for ( int j = 0; j < columnArray.length; j++ )
			{
				Column column = columnArray[j];
				ExportedColumn exportedColumn = new ExportedColumn( column
						.getName( ), column.getLabel( ), column.getVisibility( )
						.booleanValue( ) );
				columns.add( exportedColumn );
			}
			ExportedResultSet exportedResultSet = new ExportedResultSet(
					queryName, columns );
			ret.add( exportedResultSet );
		}
		return ret;
	}

	/**
	 * Gets the report design name from the input options.
	 * 
	 * @param options
	 *            the input options
	 * @return the report design name if the request contains a valid name,
	 *         otherwise null
	 */

	private String getReportDesignName( InputOptions options )
	{
		String reportDesignName = null;

		if ( options != null )
		{
			HttpServletRequest request = (HttpServletRequest) options
					.getOption( InputOptions.OPT_REQUEST );
			if ( request != null )
			{
				ViewerAttributeBean attrBean = (ViewerAttributeBean) request
						.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
				assert attrBean != null;

				reportDesignName = attrBean.getReportDesignName( );
				if ( reportDesignName != null )
				{
					// if the report design name is not a valid file, then set
					// it to null

					if ( reportDesignName.endsWith( "\\" ) //$NON-NLS-1$
							|| reportDesignName.endsWith( "/" ) ) //$NON-NLS-1$
						reportDesignName = null;
				}
			}
		}
		return reportDesignName;
	}

	/**
	 * Convert engine parameters (IScalarParameterDefn and IParameterGroupDefn)
	 * into service api parameters (ParameterDefinition and
	 * ParameterGroupDefinition)
	 * 
	 * @param params
	 *            a Collection of IScalarParameterDefn or IParameterGroupDefn
	 * @param includeGroups
	 *            if true, include groups (ParameterGroupDefinition) in the
	 *            result, otherwise flatten the result (i.e. include the
	 *            contents of the groups in the result)
	 * 
	 * @return a Collection of ParameterDefinition and ParameterGroupDefinition,
	 *         or a Collection of only ParameterDefinition if includeGroups ==
	 *         false
	 */
	private static Collection convertEngineParameters( Collection params,
			boolean includeGroups )
	{
		if ( params == null )
			return Collections.EMPTY_LIST;
		List ret = new ArrayList( );
		for ( Iterator it = params.iterator( ); it.hasNext( ); )
		{
			Object o = it.next( );
			if ( o instanceof IScalarParameterDefn )
			{
				IScalarParameterDefn engineParam = (IScalarParameterDefn) o;
				ParameterGroupDefinition group = null;
				ParameterDefinition param = convertScalarParameter(
						engineParam, group );

				ret.add( param );
			}
			else if ( o instanceof IParameterGroupDefn )
			{
				IParameterGroupDefn engineParam = (IParameterGroupDefn) o;
				ParameterGroupDefinition paramGroup = convertParameterGroup( engineParam );
				ret.add( paramGroup );
			}
		}

		if ( includeGroups )
			return ret;
		// If we are not including the groups, flatten the results
		return flattenGroups( ret );
	}

	// Flatten the results
	private static List flattenGroups( List params )
	{
		if ( params == null || params.size( ) == 0 )
			return Collections.EMPTY_LIST;
		List ret = new ArrayList( );
		for ( Iterator it = params.iterator( ); it.hasNext( ); )
		{
			Object o = it.next( );
			if ( o instanceof ParameterGroupDefinition )
			{
				ParameterGroupDefinition group = (ParameterGroupDefinition) o;
				ret.addAll( group.getParameters( ) );
			}
			else
				ret.add( o );
		}

		return ret;
	}

	private static List convertParametersInGroup( Collection scalarParameters,
			ParameterGroupDefinition group )
	{
		if ( scalarParameters == null )
			return null;
		List ret = new ArrayList( );
		for ( Iterator it = scalarParameters.iterator( ); it.hasNext( ); )
		{
			IScalarParameterDefn engineParam = (IScalarParameterDefn) it.next( );
			ParameterDefinition param = convertScalarParameter( engineParam,
					group );
			ret.add( param );
		}
		return ret;
	}

	private static ParameterGroupDefinition convertParameterGroup(
			IParameterGroupDefn engineParam )
	{
		boolean cascade = engineParam instanceof ICascadingParameterGroup;
		String name = engineParam.getName( );
		String displayName = engineParam.getDisplayName( );
		String helpText = engineParam.getHelpText( );
		String promptText = engineParam.getPromptText( );
		ParameterGroupDefinition paramGroup = new ParameterGroupDefinition(
				name, displayName, promptText, null, cascade, helpText );
		List contents = convertParametersInGroup( engineParam.getContents( ),
				paramGroup );
		paramGroup.setParameters( contents );

		return paramGroup;
	}

	private static ParameterDefinition convertScalarParameter(
			IScalarParameterDefn engineParam, ParameterGroupDefinition group )
	{
		Object handle = engineParam.getHandle( );
		ScalarParameterHandle scalarParamHandle = null;
		if ( handle instanceof ScalarParameterHandle )
			scalarParamHandle = (ScalarParameterHandle) handle;
		String name = engineParam.getName( );
		long id = scalarParamHandle != null ? scalarParamHandle.getID( ) : 0L;
		String pattern = scalarParamHandle == null ? "" : scalarParamHandle //$NON-NLS-1$
				.getPattern( );
		String displayFormat = engineParam.getDisplayFormat( );
		String displayName = engineParam.getDisplayName( );
		String helpText = engineParam.getHelpText( );
		String promptText = engineParam.getPromptText( );
		int dataType = engineParam.getDataType( );
		int controlType = engineParam.getControlType( );
		boolean hidden = engineParam.isHidden( );
		boolean allowNull = engineParam.allowNull( );
		boolean allowBlank = engineParam.allowBlank( );
		boolean mustMatch = scalarParamHandle == null
				? false
				: scalarParamHandle.isMustMatch( );
		boolean concealValue = engineParam.isValueConcealed( );

		ParameterDefinition param = new ParameterDefinition( id, name, pattern,
				displayFormat, displayName, helpText, promptText, dataType,
				controlType, hidden, allowNull, allowBlank, mustMatch,
				concealValue, group, null );
		return param;
	}

	// Convert collection of IParameterSelectionChoice to collection of
	// ParameterSelectionChoice
	private static Collection convertEngineParameterSelectionChoice(
			Collection params )
	{
		if ( params == null )
			return Collections.EMPTY_LIST;
		List ret = new ArrayList( );
		for ( Iterator it = params.iterator( ); it.hasNext( ); )
		{
			IParameterSelectionChoice engineChoice = (IParameterSelectionChoice) it
					.next( );
			ParameterSelectionChoice paramChoice = new ParameterSelectionChoice(
					engineChoice.getLabel( ), engineChoice.getValue( ) );
			ret.add( paramChoice );
		}
		return ret;
	}

	/**
	 * Get Module options from ViewerAttributeBean
	 * 
	 * @param options
	 * @return
	 */

	private Map getModuleOptions( InputOptions options )
	{
		ViewerAttributeBean bean = getViewerAttrBean( options );
		if ( bean != null )
			return bean.getModuleOptions( );
		return null;
	}

	/**
	 * 
	 * Get ViewerAttributeBean from InputOptions
	 * 
	 * @param options
	 * @return
	 */
	private ViewerAttributeBean getViewerAttrBean( InputOptions options )
	{
		if ( options != null )
		{
			HttpServletRequest request = (HttpServletRequest) options
					.getOption( InputOptions.OPT_REQUEST );
			if ( request != null )
			{
				return (ViewerAttributeBean) request
						.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
			}
		}
		return null;
	}
}