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

package org.eclipse.birt.report.context;

import java.io.File;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Data bean for viewing request. Birt viewer distributes process logic into
 * viewer fragments. Each fragment seperates its front-end and back-end process
 * into jsp page and "code behand" fragment class. Viewer attribute bean serves
 * as:
 * <ol>
 * <li> object that carries the data shared among different fragments</li>
 * <li> object that carries the date shared between front-end jsp page and
 * back-end class</li>
 * </ol>
 * In current implementation, ViewerAttributeBean uses request scope.
 * <p>
 */
public class ViewerAttributeBean extends BaseAttributeBean
{

	/**
	 * Constructor.
	 * 
	 * @param request
	 */
	public ViewerAttributeBean( HttpServletRequest request )
	{
		super( request );
	}

	/**
	 * Init the bean.
	 * 
	 * @param request
	 * @throws Exception
	 */
	protected void __init( HttpServletRequest request ) throws Exception
	{
		if ( ParameterAccessor.isGetImageOperator( request ) )
		{
			return;
		}

		this.category = "BIRT"; //$NON-NLS-1$
		this.masterPageContent = ParameterAccessor
				.isMasterPageContent( request );
		this.isDesigner = ParameterAccessor.isDesigner( request );
		this.bookmark = ParameterAccessor.getBookmark( request );
		this.reportPage = String.valueOf( ParameterAccessor.getPage( request ) );
		this.reportDocumentName = ParameterAccessor.getReportDocument( request );
		this.reportDesignName = ParameterAccessor.getReport( request );
		this.format = ParameterAccessor.getFormat( request );

		// Determine the report design and doc 's timestamp

		processReport( request );

		// Report title.

		String title = null;

		if ( title == null || title.trim( ).length( ) <= 0 )
		{
			title = BirtResources.getString( "birt.viewer.title" ); //$NON-NLS-1$
		}
		this.reportTitle = ParameterAccessor.htmlEncode( title );
		this.__initParameters( request );
	}

	/**
	 * Determine the report design and doc 's timestamp
	 * 
	 * @param request
	 * @throws Exception
	 */
	protected void processReport( HttpServletRequest request ) throws Exception
	{
		File reportDocFile = new File( this.reportDocumentName );
		// String reportDesignName = ParameterAccessor.getReport( request );
		File reportDesignDocFile = new File( reportDesignName );

		if ( reportDesignDocFile != null && reportDesignDocFile.exists( )
				&& reportDesignDocFile.isFile( ) && reportDocFile != null
				&& reportDocFile.exists( ) && reportDocFile.isFile( )
				&& "get".equalsIgnoreCase( request.getMethod( ) ) ) //$NON-NLS-1$
		{
			if ( reportDesignDocFile.lastModified( ) > reportDocFile
					.lastModified( )
					|| ParameterAccessor.isOverwrite( request ) )
			{
				reportDocFile.delete( );
			}
		}
	}

	/**
	 * Get report service instance.
	 */
	protected IViewerReportService getReportService( )
	{
		return BirtReportServiceFactory.getReportService( );
	}

	/**
	 * Clear our resources.
	 * 
	 * @exception Throwable
	 * @return
	 */
	protected void __finalize( ) throws Throwable
	{
	}

	protected Object getParamValueObject( HttpServletRequest request,
			IScalarParameterDefn parameterObj ) throws ReportServiceException
	{
		String paramName = parameterObj.getName( );
		String format = parameterObj.getDisplayFormat( );
		Object paramValueObj = super
				.getParamValueObject( request, parameterObj );
		if ( paramValueObj != null )
			return paramValueObj;

		// Get config map
		IReportRunnable runnable;
		try
		{
			runnable = ReportEngineService.getInstance( ).openReportDesign(
					reportDesignName );
		}
		catch ( EngineException e )
		{
			throw new ReportServiceException( e.getLocalizedMessage( ) );
		}
		Map configMap = runnable.getTestConfig( );
		if ( ParameterAccessor.isDesigner( request )
				&& configMap.containsKey( paramName ) )
		{
			// Get value from test config
			String configValue = (String) configMap.get( paramName );
			ReportParameterConverter cfgConverter = new ReportParameterConverter(
					format, Locale.US );
			return cfgConverter
					.parse( configValue, parameterObj.getDataType( ) );
		}
		else
			return super.getParamValueObject( request, parameterObj );
	}
}