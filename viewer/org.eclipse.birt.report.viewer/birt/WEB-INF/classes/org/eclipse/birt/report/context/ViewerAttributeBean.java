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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.BirtViewerReportDesignHandle;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.utility.ParameterAccessor;

import com.ibm.icu.util.ULocale;

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
	 * Viewer report design handle 
	 */	
	private IViewerReportDesignHandle reportDesignHandle = null;
	
	/**
	 * Constructor.
	 * 
	 * @param request
	 */
	public ViewerAttributeBean( HttpServletRequest request )
	{
		try
		{
			init( request );
		}
		catch ( Exception e )
		{
			this.exception = e;
		}
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

	/*
	 * Prepare the report parameters
	 */
	protected void __initParameters( HttpServletRequest request )
			throws Exception
	{
		this.reportDesignHandle = getDesignHandle( request );

		// when in preview model, parse paramenters from config file
		if ( isDesigner )
		{
			this.parseConfigVars( request );
		}

		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, request );
		options.setOption( InputOptions.OPT_LOCALE, locale );

		Collection parameterList = this.getReportService( )
				.getParameterDefinitions( reportDesignHandle, options, false );

		// TODO: Change parameters to be Map, not HashMap
		this.parameters = (HashMap) getParsedParameters( reportDesignHandle, parameterList,
				request, options );

		this.missingParameter = validateParameters( parameterList,
				this.parameters );
	}

	/**
	 * parse paramenters from config file.
	 * 
	 * @param request
	 * @return
	 */
	protected void parseConfigVars( HttpServletRequest request )
	{
		// get report config filename
		String reportConfigName = this.reportDesignName.replaceFirst(
				IBirtConstants.SUFFIX_DESIGN_FILE,
				IBirtConstants.SUFFIX_DESIGN_CONFIG );

		// Generate the session handle
		SessionHandle sessionHandle = DesignEngine.newSession( ULocale
				.getDefault( ) );

		ReportDesignHandle handle = null;

		try
		{
			this.configMap = new HashMap( );

			// Open report config file
			handle = sessionHandle.openDesign( reportConfigName );

			// initial config map
			if ( handle != null )
			{
				Iterator configVars = handle.configVariablesIterator( );
				while ( configVars != null && configVars.hasNext( ) )
				{
					ConfigVariableHandle configVar = (ConfigVariableHandle) configVars
							.next( );
					if ( configVar != null && configVar.getName( ) != null )
					{
						this.configMap.put( configVar.getName( ), configVar
								.getValue( ) );
					}
				}

				handle.close( );
			}
		}
		catch ( Exception e )
		{
			try
			{
				if ( handle != null )
					handle.close( );
			}
			catch ( Exception err )
			{
			}
		}
	}

	protected IViewerReportDesignHandle getDesignHandle(
			HttpServletRequest request )
	{
		IViewerReportDesignHandle design = null;
		IReportRunnable reportRunnable = null;

		IReportDocument reportDocumentInstance = ReportEngineService
				.getInstance( ).openReportDocument( this.reportDesignName,
						this.reportDocumentName );

		if ( reportDocumentInstance != null )
		{
			reportRunnable = reportDocumentInstance.getReportRunnable( );
			// in frameset mode, parse parameter values from document file
			if ( IBirtConstants.SERVLET_PATH_FRAMESET.equalsIgnoreCase( request
					.getServletPath( ) ) )
			{
				this.parameterMap = reportDocumentInstance.getParameterValues( );
			}
			reportDocumentInstance.close( );
		}
		if ( reportRunnable != null )
		{
			design = new BirtViewerReportDesignHandle(
					IViewerReportDesignHandle.RPT_RUNNABLE_OBJECT,
					reportRunnable );
		}
		else
		{
			design = new BirtViewerReportDesignHandle( null, reportDesignName );
		}

		return design;
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

	/**
	 * get parameter object.
	 * 
	 * @param request
	 * @param parameterObj
	 * @exception Throwable
	 * @return
	 */
	protected Object getParamValueObject( HttpServletRequest request,
			ParameterDefinition parameterObj ) throws ReportServiceException
	{
		String paramName = parameterObj.getName( );
		String format = parameterObj.getDisplayFormat( );
		Object paramValueObj = super
				.getParamValueObject( request, parameterObj );
		if ( paramValueObj != null )
			return paramValueObj;

		if ( ParameterAccessor.isDesigner( request ) && this.configMap != null
				&& this.configMap.containsKey( paramName ) )
		{
			// Get value from config file
			paramValueObj = this.configMap.get( paramName );
		}
		else if ( this.parameterMap != null
				&& this.parameterMap.containsKey( paramName ) )
		{
			// Get value from document
			paramValueObj = this.parameterMap.get( paramName );
		}

		if ( paramValueObj != null )
		{
			ReportParameterConverter cfgConverter = new ReportParameterConverter(
					format, Locale.US );
			return cfgConverter.parse( paramValueObj.toString( ), parameterObj
					.getDataType( ) );
		}
		else
		{
			return super.getParamValueObject( request, parameterObj );
		}
	}
	
	/**
	 * @return the reportDesignHandle
	 */
	public IViewerReportDesignHandle getReportDesignHandle( )
	{
		return reportDesignHandle;
	}	
}