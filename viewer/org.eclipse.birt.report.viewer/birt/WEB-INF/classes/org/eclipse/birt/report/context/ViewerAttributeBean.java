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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
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
	 * Viewer preview max rows limited
	 */

	private int maxRows;

	/**
	 * Report parameter definitions list
	 */

	private Collection parameterList = null;

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
		this.maxRows = ParameterAccessor.getMaxRows( request );

		// Set preview report max rows

		ReportEngineService.getInstance( ).setMaxRows( this.maxRows );

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

		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, request );
		options.setOption( InputOptions.OPT_LOCALE, locale );
		options.setOption( InputOptions.OPT_RTL, new Boolean( rtl ) );

		this.parameterList = this.getReportService( ).getParameterDefinitions(
				reportDesignHandle, options, false );

		// when in preview model, parse paramenters from config file
		if ( isDesigner )
		{
			this.parseConfigVars( request );
		}

		// Change parameters to be Map, not HashMap
		this.parameters = (HashMap) getParsedParameters( reportDesignHandle,
				parameterList, request, options );

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
				Iterator paramIr = null;
				if ( parameterList != null )
					paramIr = parameterList.iterator( );

				Iterator configVars = handle.configVariablesIterator( );
				while ( configVars != null && configVars.hasNext( ) )
				{
					ConfigVariableHandle configVar = (ConfigVariableHandle) configVars
							.next( );
					if ( configVar != null && configVar.getName( ) != null )
					{
						// check the parameter whether exist or not
						String paramName = getParameterName( configVar
								.getName( ) );
						Object paramValue = configVar.getValue( );

						while ( paramIr != null && paramName != null
								&& paramValue != null && paramIr.hasNext( ) )
						{
							ParameterDefinition parameterObj = (ParameterDefinition) paramIr
									.next( );
							if ( paramName.equals( parameterObj.getName( ) ) )
							{
								ReportParameterConverter converter = new ReportParameterConverter(
										parameterObj.getPattern( ), ULocale.US );

								paramValue = converter.parse( paramValue
										.toString( ), parameterObj
										.getDataType( ) );

								break;
							}
						}

						if ( paramName != null && paramName.length( ) > 0
								&& paramValue != null )
						{
							this.configMap.put( paramName, paramValue );
						}
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

	/**
	 * if parameter existed in config file, return the correct parameter name
	 * 
	 * @param configVarName
	 * @return String
	 */
	private String getParameterName( String configVarName )
	{
		String paramName = null;
		List parameters = null;

		// get parameter list from design handle
		IReportRunnable runnable = (IReportRunnable) reportDesignHandle
				.getDesignObject( );
		ModuleHandle model = runnable.getDesignHandle( ).getModuleHandle( );

		if ( model != null )
		{
			parameters = model.getFlattenParameters( );
		}

		if ( parameters != null )
		{
			for ( int i = 0; i < parameters.size( ); i++ )
			{
				ScalarParameterHandle parameter = null;

				if ( parameters.get( i ) instanceof ScalarParameterHandle )
				{
					parameter = ( (ScalarParameterHandle) parameters.get( i ) );
				}

				// get current name
				String curName = null;
				if ( parameter != null && parameter.getName( ) != null )
				{
					curName = parameter.getName( ) + parameter.getID( );
				}

				// if find the parameter exist, return true
				if ( curName != null
						&& curName.equalsIgnoreCase( configVarName ) )
				{
					paramName = parameter.getName( );
					break;
				}
			}
		}

		return paramName;
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

		// if report runnable is null, then get it from design file
		if ( reportRunnable == null )
		{
			try
			{
				reportRunnable = ReportEngineService.getInstance( )
						.openReportDesign( this.reportDesignName );
			}
			catch ( EngineException e )
			{
				this.exception = e;
			}
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

		return paramValueObj;

	}

	/**
	 * @return the maxRows
	 */
	public int getMaxRows( )
	{
		return maxRows;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.context.BaseAttributeBean#getReportTitle()
	 */

	public String getReportTitle( )
	{
		String title = reportTitle;
		if ( reportDesignHandle != null )
		{
			Object design = reportDesignHandle.getDesignObject( );
			if ( design instanceof IReportRunnable )
			{
				IReportRunnable runnable = (IReportRunnable) design;
				String designTitle = (String) runnable
						.getProperty( IReportRunnable.TITLE );
				if ( designTitle != null && designTitle.trim( ).length( ) > 0 )
					title = designTitle;
			}
		}
		return title;
	}

	/**
	 * @return the parameterList
	 */
	public Collection getParameterList( )
	{
		return parameterList;
	}

}