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
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.metadata.ValidationValueException;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.BirtViewerReportDesignHandle;
import org.eclipse.birt.report.service.ParameterDataTypeConverter;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.utility.BirtUtility;
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
	 * Request Type
	 */
	private String requestType;

	/**
	 * Default parameter values map
	 */
	private Map defaultValues;

	/**
	 * Locale parameter list
	 */
	private List locParams;

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
		// If GetImage operate, return directly.
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

		// Set locale information
		BirtResources.setLocale( ParameterAccessor.getLocale( request ) );

		// Set preview report max rows
		ReportEngineService.getInstance( ).setMaxRows( this.maxRows );

		// Set the request type
		this.requestType = request
				.getHeader( ParameterAccessor.HEADER_REQUEST_TYPE );

		// Determine the report design and doc 's timestamp
		processReport( request );

		// Report title.
		this.reportTitle = ParameterAccessor.getTitle( request );

		// Set whether show the report title
		this.isShowTitle = ParameterAccessor.isShowTitle( request );

		// Set whether show the toolbar
		this.isShowToolbar = ParameterAccessor.isShowToolbar( request );

		// Set whether show the navigation bar
		this.isShowNavigationbar = ParameterAccessor
				.isShowNavigationbar( request );

		// Set whether force prompting the parameter dialog. Default to false.
		this.isForceParameterPrompting = ParameterAccessor
				.isForceParameterPrompting( request );

		// get some module options
		this.moduleOptions = BirtUtility.getModuleOptions( request );

		// Initialize report parameters
		__initParameters( request );
	}

	/**
	 * Prepare the report parameters
	 * 
	 * @param request
	 * @throws Exception
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

		// Get parameter definition list
		this.parameterDefList = getReportService( ).getParameterDefinitions(
				this.reportDesignHandle, options, false );

		// when in preview model, parse parameters from config file
		if ( this.isDesigner
				&& !IBirtConstants.SERVLET_PATH_FRAMESET
						.equalsIgnoreCase( request.getServletPath( ) ) )
			parseConfigVars( request, parameterDefList );

		// Get parameters as String Map
		this.parametersAsString = getParsedParametersAsString(
				parameterDefList, request, options );

		// Check if miss parameter
		if ( documentInUrl )
			this.missingParameter = false;
		else
			this.missingParameter = BirtUtility.validateParameters(
					parameterDefList, this.parametersAsString );

		// Get parameter default values map
		this.defaultValues = getDefaultValues( this.reportDesignHandle,
				parameterDefList, request, options );

		// Get display text of select parameters
		this.displayTexts = BirtUtility.getDisplayTexts( this.displayTexts,
				request );

		// Get locale parameter list
		this.locParams = BirtUtility.getLocParams( this.locParams, request );

		// Get parameters as Object Map
		this.parameters = (HashMap) getParsedParameters(
				this.reportDesignHandle, parameterDefList, request, options );

		// Get parameters as String Map with default value
		this.parametersAsString = getParsedParametersAsStringWithDefaultValue(
				this.parametersAsString, parameterDefList, request, options );
	}

	/**
	 * parse paramenters from config file.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param parameterList
	 *            Collection
	 * @return
	 */
	protected void parseConfigVars( HttpServletRequest request,
			Collection parameterList )
	{
		this.configMap = new HashMap( );

		if ( this.displayTexts == null )
			this.displayTexts = new HashMap( );

		// get report config file
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

			// handle config vars
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
						Object paramValue = configVar.getValue( );

						// check if null parameter
						if ( paramName.toLowerCase( ).startsWith(
								ParameterAccessor.PARAM_ISNULL )
								&& paramValue != null )
						{
							String nullParamName = getParameterName(
									(String) paramValue, parameterList );
							if ( nullParamName != null )
								this.configMap.put( nullParamName, null );

							continue;
						}
						// check if display text of select parameter
						else if ( ( displayTextParam = ParameterAccessor
								.isDisplayText( paramName ) ) != null )
						{
							paramName = getParameterName( displayTextParam,
									parameterList );
							if ( paramName != null )
							{
								this.displayTexts.put( paramName, paramValue );
							}

							continue;
						}

						// check the parameter whether exist or not
						paramName = getParameterName( paramName, parameterList );

						ParameterDefinition parameter = BirtUtility
								.findParameterDefinition( parameterList,
										paramName );

						if ( paramValue != null && parameter != null )
						{
							// find cached parameter type
							String typeVarName = configVar.getName( )
									+ "_" + IBirtConstants.PROP_TYPE; //$NON-NLS-1$
							ConfigVariable typeVar = handle
									.findConfigVariable( typeVarName );

							// get cached parameter type
							String dataType = ParameterDataTypeConverter
									.ConvertDataType( parameter.getDataType( ) );
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

							this.configMap.put( paramName, paramValue );
						}
					}
				}

				handle.close( );
			}
		}
		catch ( Exception e )
		{
		}
	}

	/**
	 * if parameter existed in config file, return the correct parameter name
	 * 
	 * @param configVarName
	 *            String
	 * @param parameterList
	 *            Collection
	 * @return String
	 */
	private String getParameterName( String configVarName,
			Collection parameterList ) throws ReportServiceException
	{
		String paramName = null;
		if ( parameterList != null )
		{
			for ( Iterator iter = parameterList.iterator( ); iter.hasNext( ); )
			{
				ParameterDefinition parameter = (ParameterDefinition) iter
						.next( );

				// get current name
				String curName = null;
				if ( parameter != null && parameter.getName( ) != null )
				{
					curName = parameter.getName( ) + "_" + parameter.getId( ); //$NON-NLS-1$
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

	/**
	 * Returns the report design handle
	 * 
	 * @param request
	 */
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
			// open document instance
			IReportDocument reportDocumentInstance = ReportEngineService
					.getInstance( ).openReportDocument( this.reportDesignName,
							this.reportDocumentName, this.moduleOptions );

			if ( reportDocumentInstance != null )
			{
				reportRunnable = reportDocumentInstance.getReportRunnable( );

				if ( ParameterAccessor.getParameter( request,
						ParameterAccessor.PARAM_REPORT_DOCUMENT ) != null )
					this.documentInUrl = true;

				// in frameset mode, parse parameter values from document file
				// if the path is frameset, copy the parameter value from
				// document
				// to run the report. If the _document parameter from url is not
				// null, means user wants to preview the document, copy the
				// parameter from the document to do the preview.
				if ( IBirtConstants.SERVLET_PATH_FRAMESET
						.equalsIgnoreCase( request.getServletPath( ) )
						|| this.documentInUrl )

				{
					this.parameterMap = reportDocumentInstance
							.getParameterValues( );
				}

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
										this.moduleOptions );
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
										.getInstance( ).openReportDesign(
												url.toString( ), is,
												this.moduleOptions );

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
		File reportDesignDocFile = new File( this.reportDesignName );

		// if request is SOAP Post, don't delete document file
		if ( reportDesignDocFile != null
				&& reportDesignDocFile.exists( )
				&& reportDesignDocFile.isFile( )
				&& reportDocFile != null
				&& reportDocFile.exists( )
				&& reportDocFile.isFile( )
				&& !ParameterAccessor.HEADER_REQUEST_TYPE_SOAP
						.equalsIgnoreCase( this.requestType )
				&& !ParameterAccessor.SERVLET_PATH_DOWNLOAD
						.equalsIgnoreCase( request.getServletPath( ) ) )
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
	 * get parsed parameters with default value.
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
			// get parameter definition object
			ParameterDefinition parameter = (ParameterDefinition) iter.next( );
			if ( parameter == null )
				continue;

			String paramName = parameter.getName( );
			String paramValue = (String) this.parametersAsString
					.get( paramName );

			if ( paramValue != null )
			{
				try
				{
					// get parameter format
					String format = ParameterAccessor.getFormat( request,
							paramName );
					if ( format == null || format.length( ) <= 0 )
					{
						format = parameter.getPattern( );
					}

					// get parameter data type
					String dataType = ParameterDataTypeConverter
							.ConvertDataType( parameter.getDataType( ) );

					// check whether locale string
					boolean isLocale = this.locParams.contains( paramName );

					// convert parameter to object
					Object paramValueObj = DataUtil.validate( dataType, format,
							paramValue, locale, isLocale );

					params.put( paramName, paramValueObj );
				}
				catch ( ValidationValueException e )
				{
					// if in PREVIEW mode, then throw exception directly
					if ( ParameterAccessor.SERVLET_PATH_PREVIEW
							.equalsIgnoreCase( request.getServletPath( ) ) )
					{
						this.exception = e;
						break;
					}
				}
			}
			else
			{
				// null parameter value
				if ( this.parametersAsString.containsKey( paramName ) )
				{
					params.put( paramName, null );
				}
				else
				{
					// Get parameter default value as object
					params.put( paramName, this.defaultValues.get( paramName ) );
				}
			}
		}
		return params;
	}

	/**
	 * Returns parameter default values map
	 * 
	 * @param design
	 * @param parameterList
	 * @param request
	 * @param options
	 * @return
	 * @throws ReportServiceException
	 */
	protected Map getDefaultValues( IViewerReportDesignHandle design,
			Collection parameterList, HttpServletRequest request,
			InputOptions options ) throws ReportServiceException
	{
		Map map = new HashMap( );

		// get parameter default values
		for ( Iterator iter = parameterList.iterator( ); iter.hasNext( ); )
		{
			ParameterDefinition parameter = (ParameterDefinition) iter.next( );
			if ( parameter == null )
				continue;

			String paramName = parameter.getName( );
			if ( paramName != null )
			{
				Object paramValue = this.getReportService( )
						.getParameterDefaultValue( design, paramName, options );
				map.put( paramName, paramValue );
			}
		}

		return map;
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
			ParameterDefinition parameter = (ParameterDefinition) iter.next( );
			if ( parameter == null )
				continue;

			// get parameter name
			String paramName = parameter.getName( );
			if ( paramName == null )
				continue;

			// get parameter value
			String paramValue = null;
			if ( ParameterAccessor.isReportParameterExist( request, paramName ) )
			{
				// Get value from http request
				paramValue = ParameterAccessor.getReportParameter( request,
						paramName, null );

				params.put( paramName, paramValue );
			}
			else
			{
				Object paramValueObj = null;
				if ( this.isDesigner
						&& !IBirtConstants.SERVLET_PATH_FRAMESET
								.equalsIgnoreCase( request.getServletPath( ) )
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
				}
				else
				{
					// skip it
					continue;
				}

				// if DateTime parameter, return it
				paramValue = DataUtil.getDisplayValue( paramValueObj );
				params.put( paramName, paramValue );
			}
		}

		return params;
	}

	/**
	 * get parsed parameters as string.
	 * 
	 * @param parsedParameters
	 *            Map
	 * @param parameterList
	 *            Collection
	 * @param request
	 *            HttpServletRequest
	 * @param options
	 *            InputOptions
	 * 
	 * @return Map
	 */
	protected Map getParsedParametersAsStringWithDefaultValue(
			Map parsedParameters, Collection parameterList,
			HttpServletRequest request, InputOptions options )
			throws ReportServiceException
	{
		if ( parsedParameters == null )
			parsedParameters = new HashMap( );

		for ( Iterator iter = parameterList.iterator( ); iter.hasNext( ); )
		{
			// get parameter definition object
			ParameterDefinition parameter = (ParameterDefinition) iter.next( );
			if ( parameter == null )
				continue;

			// get parameter name
			String paramName = parameter.getName( );
			if ( paramName == null )
				continue;

			// if miss parameter, set parameter value as default value
			if ( !parsedParameters.containsKey( paramName ) )
			{
				parsedParameters
						.put( paramName, DataUtil
								.getDisplayValue( this.defaultValues
										.get( paramName ) ) );
			}
		}

		return parsedParameters;
	}

	/**
	 * find the parameter handle by parameter name
	 * 
	 * @param paramName
	 * @return
	 * @throws ReportServiceException
	 */
	public ParameterHandle findParameter( String paramName )
			throws ReportServiceException
	{
		return BirtUtility.findParameter( this.reportDesignHandle, paramName );
	}

	/**
	 * find the parameter definition object by parameter name
	 * 
	 * @param paramName
	 * @return
	 */
	public ParameterDefinition findParameterDefinition( String paramName )
	{
		return BirtUtility.findParameterDefinition( this.parameterDefList,
				paramName );
	}

	/**
	 * Returns the report title
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
	 * @return the maxRows
	 */
	public int getMaxRows( )
	{
		return maxRows;
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

	/**
	 * @return the defaultValues
	 */
	public Map getDefaultValues( )
	{
		return defaultValues;
	}

}