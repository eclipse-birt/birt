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
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.IModuleOption;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.metadata.ValidationValueException;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.BirtViewerReportDesignHandle;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.utility.DataUtil;
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
	 * Report parameters as string map
	 */

	private Map parametersAsString = null;

	/**
	 * Report parameter definitions List
	 */

	private Collection parameterDefList = null;

	/**
	 * Display Text of Select Parameters
	 */

	private Map displayTexts = null;

	/**
	 * Module Options
	 */

	private Map moduleOptions = null;

	/**
	 * If document generated completely
	 */

	private boolean isDocumentProcessing = false;

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

		BirtResources.setLocale( ParameterAccessor.getLocale( request ) );

		// Set preview report max rows

		ReportEngineService.getInstance( ).setMaxRows( this.maxRows );

		// Determine the report design and doc 's timestamp

		processReport( request );

		// Report title.

		String title = BirtResources
				.getMessage( ResourceConstants.BIRT_VIEWER_TITLE );
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
		if ( this.reportDesignHandle == null )
			return;

		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, request );
		options.setOption( InputOptions.OPT_LOCALE, locale );
		options.setOption( InputOptions.OPT_RTL, new Boolean( rtl ) );

		// Get parameter handle list
		Collection parameterList = getParameterList( );

		// when in preview model, parse parameters from config file
		if ( this.isDesigner
				&& !IBirtConstants.SERVLET_PATH_FRAMESET
						.equalsIgnoreCase( request.getServletPath( ) ) )
			parseConfigVars( request, parameterList );

		// Get parameters as String Map
		this.parametersAsString = getParsedParametersAsString( parameterList,
				request, options );

		// Get parameter definition list
		this.parameterDefList = getReportService( ).getParameterDefinitions(
				this.reportDesignHandle, options, false );

		// Check if miss parameter
		if ( documentInUrl )
			this.missingParameter = false;
		else
			this.missingParameter = validateParameters( parameterDefList,
					this.parametersAsString );

		// Get parameters as String Map with default value
		this.parametersAsString = getParsedParametersAsStringWithDefaultValue(
				this.parametersAsString, request, options );

		// Get parameters as Object Map
		this.parameters = (HashMap) getParsedParameters(
				this.reportDesignHandle, parameterList, request, options );

		// Get display text of select parameters
		this.displayTexts = getDisplayTexts( this.displayTexts, request );

		// get some module options
		this.moduleOptions = getModuleOptions( request );
	}

	/**
	 * parse paramenters from config file.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param parameterList
	 * @return
	 */
	protected void parseConfigVars( HttpServletRequest request,
			Collection parameterList )
	{
		this.configMap = new HashMap( );

		// get report config filename
		String reportConfigName = ParameterAccessor
				.getConfigFileName( this.reportDesignName );
		if ( reportConfigName == null )
			return;

		// Generate the session handle
		SessionHandle sessionHandle = DesignEngine.newSession( ULocale.US );

		ReportDesignHandle handle = null;

		try
		{
			// Open report config file
			handle = sessionHandle.openDesign( reportConfigName );

			// initial config map
			if ( handle != null )
			{
				String displayTextParam = null;
				Iterator configVars = handle.configVariablesIterator( );
				while ( configVars != null && configVars.hasNext( ) )
				{
					ConfigVariableHandle configVar = (ConfigVariableHandle) configVars
							.next( );
					if ( configVar != null && configVar.getName( ) != null )
					{
						String paramName = configVar.getName( );
						String paramValue = configVar.getValue( );

						// check if null parameter
						if ( paramName.toLowerCase( ).startsWith(
								ParameterAccessor.PARAM_ISNULL )
								&& paramValue != null )
						{
							String nullParamName = getParameterName( paramValue );
							if ( nullParamName != null )
								this.configMap.put( nullParamName, null );

							continue;
						}
						// check if display text of select parameter
						else if ( ( displayTextParam = ParameterAccessor
								.isDisplayText( paramName ) ) != null )
						{
							paramName = getParameterName( displayTextParam );
							if ( paramName != null )
							{
								if ( this.displayTexts == null )
									this.displayTexts = new HashMap( );

								this.displayTexts.put( paramName, paramValue );
							}

							continue;
						}

						// check the parameter whether exist or not
						paramName = getParameterName( paramName );
						ScalarParameterHandle parameter = (ScalarParameterHandle) findParameter( paramName );

						// convert parameter from default locale to current
						// locale
						if ( paramValue != null && parameter != null )
						{
							// find cached parameter type
							String typeVarName = configVar.getName( )
									+ "_" + IBirtConstants.PROP_TYPE; //$NON-NLS-1$
							ConfigVariable typeVar = handle
									.findConfigVariable( typeVarName );

							// get cached parameter type
							String dataType = parameter.getDataType( );
							String cachedDateType = null;
							if ( typeVar != null )
								cachedDateType = typeVar.getValue( );

							// if null or data type changed, skip it
							if ( cachedDateType == null
									|| !cachedDateType
											.equalsIgnoreCase( dataType ) )
							{
								continue;
							}

							try
							{
								// if parameter type isn't String or DateTime,
								// convert it
								if ( !DesignChoiceConstants.PARAM_TYPE_STRING
										.equalsIgnoreCase( dataType )
										&& !DesignChoiceConstants.PARAM_TYPE_DATETIME
												.equalsIgnoreCase( dataType ) )
								{
									String pattern = parameter.getPattern( );
									Object paramValueObj = ParameterValidationUtil
											.validate( dataType, pattern,
													paramValue, ULocale.US );

									paramValue = ParameterValidationUtil
											.getDisplayValue( dataType,
													pattern, paramValueObj,
													locale );
								}
							}
							catch ( Exception err )
							{
								paramValue = configVar.getValue( );
							}

							this.configMap.put( paramName, paramValue );
						}
					}
				}

				handle.close( );
			}
		}
		catch ( Exception e )
		{
			// do nothing
		}
	}

	/**
	 * Parse report object and get the parameter default values
	 * 
	 * @param design
	 *            IViewerReportDesignHandle
	 * @param paramName
	 *            String
	 * @param options
	 *            InputOptionsF
	 * 
	 * @return String
	 */
	protected String getParameterDefaultValues(
			IViewerReportDesignHandle design, String paramName,
			InputOptions options ) throws ReportServiceException
	{
		if ( design == null )
			return null;

		String defalutValue = null;
		Object defaultValueObj = null;

		// Get parameter default value as object
		try
		{
			defaultValueObj = this.getReportService( )
					.getParameterDefaultValue( design, paramName, options );
		}
		catch ( ReportServiceException e )
		{
			e.printStackTrace( );
		}

		// Get Scalar parameter handle
		ScalarParameterHandle parameter = (ScalarParameterHandle) findParameter( paramName );

		// convert default value object to locale format
		if ( defaultValueObj != null && parameter != null )
		{
			String dataType = parameter.getDataType( );
			String pattern = parameter.getPattern( );

			if ( DesignChoiceConstants.PARAM_TYPE_DATETIME
					.equalsIgnoreCase( dataType ) )
			{
				defalutValue = ParameterValidationUtil
						.getDisplayValue( defaultValueObj );
			}
			else
			{
				if ( DesignChoiceConstants.PARAM_TYPE_STRING
						.equalsIgnoreCase( dataType ) )
				{
					pattern = null;
				}

				defalutValue = ParameterValidationUtil.getDisplayValue( null,
						pattern, defaultValueObj, locale );
			}

		}

		// get parameter default value as string
		if ( defalutValue == null && parameter != null )
		{
			defalutValue = parameter.getDefaultValue( );
		}

		return defalutValue;
	}

	/**
	 * if parameter existed in config file, return the correct parameter name
	 * 
	 * @param configVarName
	 *            String
	 * @return String
	 */
	private String getParameterName( String configVarName )
			throws ReportServiceException
	{
		String paramName = null;

		// Get parameter handle list
		List parameters = getParameterList( );

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
					curName = parameter.getName( ) + "_" + parameter.getID( ); //$NON-NLS-1$
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

		// check if document file path is valid
		boolean isValidDocument = ParameterAccessor
				.isValidFilePath( this.reportDocumentName );
		if ( isValidDocument )
		{
			IReportDocument reportDocumentInstance = ReportEngineService
					.getInstance( ).openReportDocument( this.reportDesignName,
							this.reportDocumentName,
							this.getModuleOptions( request ) );

			if ( reportDocumentInstance != null )
			{
				reportRunnable = reportDocumentInstance.getReportRunnable( );

				// in frameset mode, parse parameter values from document file
				// if the path is frameset, copy the parameter value from
				// document
				// to run the report. If the _document parameter from url is not
				// null, means user wants to preview the document, copy the
				// parameter from the document to do the preview.
				if ( IBirtConstants.SERVLET_PATH_FRAMESET
						.equalsIgnoreCase( request.getServletPath( ) ) )

				{
					this.parameterMap = reportDocumentInstance
							.getParameterValues( );
				}

				if ( ParameterAccessor.getParameter( request,
						ParameterAccessor.PARAM_REPORT_DOCUMENT ) != null )
					this.documentInUrl = true;

				// if generating document from report isn't completed
				if ( !reportDocumentInstance.isComplete( )
						&& ParameterAccessor.isReportParameterExist( request,
								ParameterAccessor.PARAM_REPORT ) )
					this.isDocumentProcessing = true;

				reportDocumentInstance.close( );
			}
		}

		// if report runnable is null, then get it from design file
		if ( reportRunnable == null )
		{
			// if only set __document parameter, throw exception directly
			if ( ParameterAccessor.isReportParameterExist( request,
					ParameterAccessor.PARAM_REPORT_DOCUMENT )
					&& !ParameterAccessor.isReportParameterExist( request,
							ParameterAccessor.PARAM_REPORT ) )
			{
				if ( isValidDocument )
					this.exception = new ViewerException(
							ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_FILE_ERROR,
							new String[]{this.reportDocumentName} );
				else
					this.exception = new ViewerException(
							ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_ACCESS_ERROR,
							new String[]{this.reportDocumentName} );

				return design;
			}

			// check if the report file path is valid
			if ( !ParameterAccessor.isValidFilePath( this.reportDesignName ) )
			{
				this.exception = new ViewerException(
						ResourceConstants.GENERAL_EXCEPTION_REPORT_ACCESS_ERROR,
						new String[]{this.reportDesignName} );
			}
			else
			{
				try
				{
					// check the design file if exist
					File file = new File( this.reportDesignName );
					if ( file.exists( ) )
					{
						reportRunnable = ReportEngineService.getInstance( )
								.openReportDesign( this.reportDesignName,
										this.getModuleOptions( request ) );
					}
					else if ( !ParameterAccessor.isWorkingFolderAccessOnly( ) )
					{
						// try to get resource from war package, when
						// WORKING_FOLDER_ACCESS_ONLY set as false
						this.reportDesignName = ParameterAccessor.getParameter(
								request, ParameterAccessor.PARAM_REPORT );

						InputStream is = null;
						URL url = null;
						try
						{
							String reportPath = this.reportDesignName;
							if ( !reportPath.startsWith( "/" ) ) //$NON-NLS-1$
								reportPath = "/" + reportPath; //$NON-NLS-1$

							url = request.getSession( ).getServletContext( )
									.getResource( reportPath );
							if ( url != null )
								is = url.openStream( );

							if ( is != null )
								reportRunnable = ReportEngineService
										.getInstance( )
										.openReportDesign( url.toString( ), is,
												this.getModuleOptions( request ) );

						}
						catch ( Exception e )
						{
						}
					}

					if ( reportRunnable == null )
					{
						this.exception = new ViewerException(
								ResourceConstants.GENERAL_EXCEPTION_REPORT_FILE_ERROR,
								new String[]{this.reportDesignName} );
					}
				}
				catch ( EngineException e )
				{
					this.exception = e;
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
	 * Determine the report design and doc 's timestamp
	 * 
	 * @param request
	 * @throws Exception
	 */
	protected void processReport( HttpServletRequest request ) throws Exception
	{
		File reportDocFile = new File( this.reportDocumentName );
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
	 * get parsed parameters.
	 * 
	 * @param design
	 *            IViewerReportDesignHandle
	 * @param parameterList
	 *            Collection
	 * @param request
	 *            HttpServletRequest
	 * @param options
	 *            InputOptions
	 * 
	 * @return Map
	 */
	protected Map getParsedParameters( IViewerReportDesignHandle design,
			Collection parameterList, HttpServletRequest request,
			InputOptions options ) throws ReportServiceException
	{
		Map params = new HashMap( );
		if ( parameterList == null || this.parametersAsString == null )
			return params;

		for ( Iterator iter = parameterList.iterator( ); iter.hasNext( ); )
		{
			ScalarParameterHandle parameter = null;
			Object parameterObj = iter.next( );

			if ( parameterObj instanceof ScalarParameterHandle )
			{
				parameter = (ScalarParameterHandle) parameterObj;
			}

			// if current object is not Scalar parameter handle, then skip it
			if ( parameter == null )
				continue;

			String paramName = parameter.getName( );
			Object paramValueObj = this.parametersAsString.get( paramName );

			if ( paramValueObj != null )
			{
				try
				{
					// convert parameter to object
					String format = ParameterAccessor.getFormat( request,
							paramName );
					if ( format == null || format.length( ) <= 0 )
					{
						format = parameter.getPattern( );
					}

					paramValueObj = DataUtil.validate(
							parameter.getDataType( ), format, paramValueObj
									.toString( ), locale );

					params.put( paramName, paramValueObj );
				}
				catch ( ValidationValueException e )
				{
					// if in RUN mode, then throw exception directly
					if ( ParameterAccessor.SERVLET_PATH_RUN
							.equalsIgnoreCase( request.getServletPath( ) ) )
					{
						this.exception = e;
						break;
					}
				}
			}
			else
			{
				params.put( paramName, null );
			}
		}
		return params;
	}

	/**
	 * get parameter object.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param parameter
	 *            ScalarParameterHandle
	 * 
	 * @return
	 */
	protected String getParamValueAsString( HttpServletRequest request,
			ScalarParameterHandle parameter )
	{
		String paramName = parameter.getName( );
		String paramValue = null;
		if ( ParameterAccessor.isReportParameterExist( request, paramName ) )
		{
			// Get value from http request
			paramValue = ParameterAccessor.getReportParameter( request,
					paramName, null );

			return paramValue;
		}

		Object paramValueObj = null;
		if ( this.isDesigner
				&& ( IBirtConstants.SERVLET_PATH_RUN.equalsIgnoreCase( request
						.getServletPath( ) ) || IBirtConstants.SERVLET_PATH_PARAMETER
						.equalsIgnoreCase( request.getServletPath( ) ) )
				&& this.configMap != null
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

			// Convert to locale string format
			paramValueObj = ParameterValidationUtil.getDisplayValue( null,
					parameter.getPattern( ), paramValueObj, locale );
		}

		if ( paramValueObj != null )
			paramValue = paramValueObj.toString( );

		return paramValue;

	}

	/**
	 * get parsed parameters as string.
	 * 
	 * @param parameterList
	 *            Collection
	 * @param request
	 *            HttpServletRequest
	 * @param options
	 *            InputOptions
	 * 
	 * @return Map
	 */
	protected Map getParsedParametersAsString( Collection parameterList,
			HttpServletRequest request, InputOptions options )
			throws ReportServiceException
	{
		Map params = new HashMap( );
		if ( parameterList == null )
			return params;

		for ( Iterator iter = parameterList.iterator( ); iter.hasNext( ); )
		{
			ScalarParameterHandle parameter = null;
			Object parameterObj = iter.next( );

			if ( parameterObj instanceof ScalarParameterHandle )
			{
				parameter = (ScalarParameterHandle) parameterObj;
			}

			// if current object is not Scalar parameter handle, then skip it
			if ( parameter == null )
				continue;

			String paramName = parameter.getName( );
			String paramValue = getParamValueAsString( request, parameter );

			if ( paramName != null )
				params.put( paramName, paramValue );
		}

		return params;
	}

	/**
	 * get parsed parameters as string.
	 * 
	 * @param parsedParameters
	 *            Map
	 * @param request
	 *            HttpServletRequest
	 * @param options
	 *            InputOptions
	 * 
	 * @return Map
	 */
	protected Map getParsedParametersAsStringWithDefaultValue(
			Map parsedParameters, HttpServletRequest request,
			InputOptions options ) throws ReportServiceException
	{
		if ( parsedParameters == null )
		{
			parsedParameters = new HashMap( );
			return parsedParameters;
		}

		for ( Iterator iter = parsedParameters.keySet( ).iterator( ); iter
				.hasNext( ); )
		{
			String paramName = iter.next( ).toString( );
			Object paramValue = parsedParameters.get( paramName );

			// if parameter value is null, then set value to default value.
			if ( paramValue == null
					&& !ParameterAccessor.isReportParameterExist( request,
							paramName )
					&& ( IBirtConstants.SERVLET_PATH_FRAMESET
							.equalsIgnoreCase( request.getServletPath( ) )
							|| this.configMap == null || !this.configMap
							.containsKey( paramName ) ) )
			{
				paramValue = this.getParameterDefaultValues(
						reportDesignHandle, paramName, options );

				parsedParameters.put( paramName, paramValue );
			}
		}

		return parsedParameters;
	}

	/**
	 * @return the parameter handle list
	 */
	private List getParameterList( ) throws ReportServiceException
	{
		IReportRunnable runnable = (IReportRunnable) this.reportDesignHandle
				.getDesignObject( );

		ModuleHandle model = null;

		if ( runnable != null )
			model = runnable.getDesignHandle( ).getModuleHandle( );

		if ( model != null )
			return model.getFlattenParameters( );
		else
			return null;
	}

	/**
	 * @return the parameter handle
	 */
	public ParameterHandle findParameter( String paramName )
			throws ReportServiceException
	{
		if ( paramName == null )
			return null;

		IReportRunnable runnable = (IReportRunnable) this.reportDesignHandle
				.getDesignObject( );

		ModuleHandle model = null;

		if ( runnable != null )
			model = runnable.getDesignHandle( ).getModuleHandle( );

		if ( model != null )
			return model.findParameter( paramName );
		else
			return null;
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

	public String getReportTitle( ) throws ReportServiceException
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
	 * Get Display Text of select parameters
	 * 
	 * @param request
	 * @return Map
	 */
	protected Map getDisplayTexts( Map displayTexts, HttpServletRequest request )
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
	 * Gets the module option map from the request.
	 * 
	 * @param request
	 *            the request
	 * @return the module options
	 */

	protected Map getModuleOptions( HttpServletRequest request )
	{
		Map options = new HashMap( );
		options.put( IModuleOption.RESOURCE_FOLDER_KEY, ParameterAccessor
				.getResourceFolder( request ) );
		return options;
	}

	/**
	 * @return the parametersAsString
	 */
	public Map getParametersAsString( )
	{
		return parametersAsString;
	}

	/**
	 * @return the parameterDefList
	 */
	public Collection getParameterDefList( )
	{
		return parameterDefList;
	}

	/**
	 * @return the displayTexts
	 */
	public Map getDisplayTexts( )
	{
		return displayTexts;
	}

	/**
	 * @return the moduleOptions
	 */
	public Map getModuleOptions( )
	{
		return moduleOptions;
	}

	/**
	 * @return the isDocumentProcessing
	 */
	public boolean isDocumentProcessing( )
	{
		return isDocumentProcessing;
	}
}