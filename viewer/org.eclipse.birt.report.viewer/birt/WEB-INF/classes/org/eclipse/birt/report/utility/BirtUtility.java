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

package org.eclipse.birt.report.utility;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.BaseTaskBean;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.model.api.IModuleOption;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.service.ParameterDataTypeConverter;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;

/**
 * Utilities for Birt Report Service
 * 
 */
public class BirtUtility
{

	/**
	 * Add current task in http session
	 * 
	 * @param request
	 * @param task
	 */
	public static void addTask( HttpServletRequest request, IEngineTask task )
	{
		if ( request == null || task == null )
			return;

		// get task id
		BaseAttributeBean attrBean = (BaseAttributeBean) request
				.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
		if ( attrBean == null )
			return;

		String taskid = attrBean.getTaskId( );

		// get task map
		HttpSession session = request.getSession( true );
		Map map = (Map) session.getAttribute( IBirtConstants.TASK_MAP );
		if ( map == null )
		{
			map = new HashMap( );
			session.setAttribute( IBirtConstants.TASK_MAP, map );
		}

		// add task
		synchronized ( map )
		{
			if ( taskid != null )
			{
				BaseTaskBean bean = new BaseTaskBean( taskid, task );
				map.put( taskid, bean );
			}
		}
	}

	/**
	 * Remove task from http session
	 * 
	 * @param request
	 */
	public static void removeTask( HttpServletRequest request )
	{
		if ( request == null )
			return;

		try
		{
			// get task id
			BaseAttributeBean attrBean = (BaseAttributeBean) request
					.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
			if ( attrBean == null )
				return;

			String taskid = attrBean.getTaskId( );

			// get task map
			HttpSession session = request.getSession( true );
			Map map = (Map) session.getAttribute( IBirtConstants.TASK_MAP );
			if ( map == null )
				return;

			// remove task
			synchronized ( map )
			{
				if ( taskid != null )
					map.remove( taskid );
			}
		}
		catch ( Exception e )
		{
		}
	}

	/**
	 * Cancel the current engine task by task id
	 * 
	 * @param request
	 * @param taskid
	 * @throws Exception
	 */
	public static void cancelTask( HttpServletRequest request, String taskid )
			throws Exception
	{
		if ( taskid == null )
			return;

		// get task map
		HttpSession session = request.getSession( );
		if ( session == null )
			return;

		Map map = (Map) session.getAttribute( IBirtConstants.TASK_MAP );
		if ( map != null && map.containsKey( taskid ) )
		{
			BaseTaskBean bean = (BaseTaskBean) map.get( taskid );
			if ( bean == null )
				return;

			// cancel task
			IEngineTask task = bean.getTask( );
			if ( task != null )
			{
				task.cancel( );
			}

			// remove task from task map
			synchronized ( map )
			{
				map.remove( taskid );
			}
		}
	}

	/**
	 * Returns the parameter handle list
	 * 
	 * @param reportDesignHandle
	 * @return
	 * @throws ReportServiceException
	 */
	public static List getParameterList(
			IViewerReportDesignHandle reportDesignHandle )
			throws ReportServiceException
	{
		IReportRunnable runnable = (IReportRunnable) reportDesignHandle
				.getDesignObject( );
		if ( runnable == null )
			return null;

		ModuleHandle model = runnable.getDesignHandle( ).getModuleHandle( );
		if ( model == null )
			return null;

		return model.getFlattenParameters( );
	}

	/**
	 * find the parameter definition by parameter name
	 * 
	 * @param parameterList
	 * @param paramName
	 * @return
	 */
	public static ParameterDefinition findParameterDefinition(
			Collection parameterList, String paramName )
	{
		if ( parameterList == null || paramName == null )
			return null;

		// find parameter definition
		for ( Iterator iter = parameterList.iterator( ); iter.hasNext( ); )
		{
			ParameterDefinition parameter = (ParameterDefinition) iter.next( );
			if ( parameter == null )
				continue;

			String name = parameter.getName( );
			if ( paramName.equals( name ) )
			{
				return parameter;
			}
		}

		return null;
	}

	/**
	 * find the parameter handle by parameter name
	 * 
	 * @param reportDesignHandle
	 * @param paramName
	 * @return
	 * @throws ReportServiceException
	 */
	public static ParameterHandle findParameter(
			IViewerReportDesignHandle reportDesignHandle, String paramName )
			throws ReportServiceException
	{
		if ( paramName == null )
			return null;

		IReportRunnable runnable = (IReportRunnable) reportDesignHandle
				.getDesignObject( );
		if ( runnable == null )
			return null;

		// get module handle from report runnable
		ModuleHandle model = runnable.getDesignHandle( ).getModuleHandle( );
		if ( model == null )
			return null;

		return model.findParameter( paramName );
	}

	/**
	 * Gets the module option map from the request.
	 * 
	 * @param request
	 *            the request
	 * @return the module options
	 */

	public static Map getModuleOptions( HttpServletRequest request )
	{
		Map options = new HashMap( );
		options.put( IModuleOption.RESOURCE_FOLDER_KEY, ParameterAccessor
				.getResourceFolder( request ) );
		return options;
	}

	/**
	 * Get Display Text of select parameters
	 * 
	 * @param displayTexts
	 * @param request
	 * @return Map
	 */
	public static Map getDisplayTexts( Map displayTexts,
			HttpServletRequest request )
	{
		if ( displayTexts == null )
			displayTexts = new HashMap( );

		Enumeration params = request.getParameterNames( );
		while ( params != null && params.hasMoreElements( ) )
		{
			String param = DataUtil.getString( params.nextElement( ) );
			String paramName = ParameterAccessor.isDisplayText( param );
			if ( paramName != null )
			{
				displayTexts.put( paramName, ParameterAccessor.getParameter(
						request, param ) );
			}
		}

		return displayTexts;
	}

	/**
	 * Get locale parameter list
	 * 
	 * @param locParams
	 * @param request
	 * @return List
	 */
	public static List getLocParams( List locParams, HttpServletRequest request )
	{
		if ( locParams == null )
			locParams = new ArrayList( );

		String[] arrs = request
				.getParameterValues( ParameterAccessor.PARAM_ISLOCALE );
		if ( arrs != null )
		{
			for ( int i = 0; i < arrs.length; i++ )
				locParams.add( arrs[i] );
		}

		return locParams;
	}

	/**
	 * Check whether missing parameter or not.
	 * 
	 * @param task
	 * @param parameters
	 * @return
	 */
	public static boolean validateParameters( Collection parameterList,
			Map parameters )
	{
		assert parameters != null;
		boolean missingParameter = false;

		Iterator iter = parameterList.iterator( );
		while ( iter.hasNext( ) )
		{
			ParameterDefinition parameterObj = (ParameterDefinition) iter
					.next( );

			String parameterName = parameterObj.getName( );
			Object parameterValue = parameters.get( parameterName );

			// hidden type parameter
			if ( parameterObj.isHidden( ) )
			{
				continue;
			}

			if ( parameterValue == null && !parameterObj.allowNull( ) )
			{
				missingParameter = true;
				break;
			}

			if ( parameterValue instanceof String )
			{
				String parameterStringValue = (String) parameterValue;
				if ( parameterStringValue != null
						&& parameterStringValue.length( ) <= 0
						&& !parameterObj.allowBlank( ) )
				{
					missingParameter = true;
					break;
				}
			}
		}

		return missingParameter;
	}

	/**
	 * Handle SOAP operation. Parse report parameters and display text
	 * 
	 * @param operation
	 * @param bean
	 * @param parameterMap
	 * @param displayTexts
	 * @throws Exception
	 */
	public static void handleOperation( Operation operation,
			ViewerAttributeBean bean, Map parameterMap, Map displayTexts )
			throws Exception
	{
		if ( operation == null || bean == null || parameterMap == null
				|| displayTexts == null )
			return;

		// convert parameter from SOAP operation
		List locs = new ArrayList( );
		Map params = new HashMap( );
		String displayTextParam = null;
		Oprand[] oprands = operation.getOprand( );
		for ( int i = 0; i < oprands.length; i++ )
		{
			String paramName = oprands[i].getName( );
			Object paramValue = oprands[i].getValue( );

			if ( paramName == null || paramValue == null )
				continue;

			if ( paramName.equalsIgnoreCase( ParameterAccessor.PARAM_ISLOCALE ) )
			{
				// parameter value is a locale string
				locs.add( paramValue );
			}
			// Check if parameter set to null
			else if ( ParameterAccessor.PARAM_ISNULL
					.equalsIgnoreCase( paramName ) )
			{
				// set parametet to null value
				parameterMap.put( paramValue, null );
				continue;
			}
			// display text of parameter
			else if ( ( displayTextParam = ParameterAccessor
					.isDisplayText( paramName ) ) != null )
			{
				displayTexts.put( displayTextParam, paramValue );
				continue;
			}
			else
			{
				params.put( paramName, paramValue );
			}
		}

		Iterator it = params.keySet( ).iterator( );
		while ( it.hasNext( ) )
		{
			String paramName = (String) it.next( );
			String paramValue = (String) params.get( paramName );

			// find the parameter
			ParameterDefinition parameter = bean
					.findParameterDefinition( paramName );
			if ( parameter == null )
				continue;

			String pattern = parameter.getPattern( );
			String dataType = ParameterDataTypeConverter
					.ConvertDataType( parameter.getDataType( ) );

			// check whether it is a locale String.
			boolean isLocale = locs.contains( paramName );

			// convert parameter
			Object paramValueObj = DataUtil.validate( dataType, pattern,
					paramValue, bean.getLocale( ), isLocale );

			// push to parameter map
			parameterMap.put( paramName, paramValueObj );
		}
	}

	/**
	 * Returns report runnable from design file
	 * 
	 * @param request
	 * @param path
	 * @return
	 * @throws EngineException
	 * 
	 */
	public static IReportRunnable getRunnableFromDesignFile(
			HttpServletRequest request, String designFile, Map options )
			throws EngineException
	{
		IReportRunnable reportRunnable = null;

		// check the design file if exist
		File file = new File( designFile );
		if ( file.exists( ) )
		{
			reportRunnable = ReportEngineService.getInstance( )
					.openReportDesign( designFile, options );
		}
		else
		{
			// try to get resource from war package
			InputStream is = null;
			URL url = null;
			try
			{
				designFile = ParameterAccessor.workingFolder
						+ "/" //$NON-NLS-1$
						+ ParameterAccessor.getParameter( request,
								ParameterAccessor.PARAM_REPORT );
				if ( !designFile.startsWith( "/" ) ) //$NON-NLS-1$
					designFile = "/" + designFile; //$NON-NLS-1$

				url = request.getSession( ).getServletContext( ).getResource(
						designFile );
				if ( url != null )
					is = url.openStream( );

				if ( is != null )
					reportRunnable = ReportEngineService.getInstance( )
							.openReportDesign( url.toString( ), is, options );

			}
			catch ( Exception e )
			{
			}
		}

		return reportRunnable;
	}

	/**
	 * Returns report title from design
	 * 
	 * @param reportDesignHandle
	 * @return
	 * @throws ReportServiceException
	 */
	public static String getTitleFromDesign(
			IViewerReportDesignHandle reportDesignHandle )
			throws ReportServiceException
	{
		String reportTitle = null;
		if ( reportDesignHandle != null )
		{
			Object design = reportDesignHandle.getDesignObject( );
			if ( design instanceof IReportRunnable )
			{
				IReportRunnable runnable = (IReportRunnable) design;
				reportTitle = (String) runnable
						.getProperty( IReportRunnable.TITLE );
			}
		}

		return reportTitle;
	}
}
