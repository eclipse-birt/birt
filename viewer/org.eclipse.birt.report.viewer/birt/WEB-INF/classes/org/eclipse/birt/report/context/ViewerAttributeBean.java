/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.context;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.exception.ViewerValidationException;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
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
 * <li>object that carries the data shared among different fragments</li>
 * <li>object that carries the date shared between front-end jsp page and
 * back-end class</li>
 * </ol>
 * In current implementation, ViewerAttributeBean uses request scope.
 * <p>
 */
public class ViewerAttributeBean extends BaseAttributeBean {

	/* white list of extensions for rptdocument to be produced */
	private static Set<String> allowedExtensionsForRptDocument;

	/* black list of extensions for the rptdocument to be produced */
	private static Set<String> disallowedExtensionsForRptDocument;

	private static final String KEY_RPTDOC_ALLOWED_EXTENSIONS = "reportdocument.allowed-extensions";
	private static final String KEY_RPTDOC_DISALLOWED_EXTENSIONS = "reportdocument.disallowed-extensions";

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

	private Boolean reportRtl;

	static {
		allowedExtensionsForRptDocument = new HashSet<>();
		disallowedExtensionsForRptDocument = new HashSet<>();

		String allowedExtString = (String) ParameterAccessor.getInitProp(KEY_RPTDOC_ALLOWED_EXTENSIONS);
		if (allowedExtString != null && allowedExtString.trim().length() > 0) {
			String[] allowedExtArray = allowedExtString.trim().split(",");
			for (String s : allowedExtArray) {
				allowedExtensionsForRptDocument.add(s.trim());
			}
		}

		String disallowedExtString = (String) ParameterAccessor.getInitProp(KEY_RPTDOC_DISALLOWED_EXTENSIONS);
		if (disallowedExtString != null && disallowedExtString.trim().length() > 0) {
			String[] disallowedExtArray = disallowedExtString.trim().split(",");
			for (String s : disallowedExtArray) {
				disallowedExtensionsForRptDocument.add(s.trim());
			}
		}

	}

	/**
	 * Constructor.
	 *
	 * @param request
	 */
	public ViewerAttributeBean(HttpServletRequest request) {
		try {
			init(request);
		} catch (Exception e) {
			this.exception = e;
		}
	}

	/**
	 * Init the bean.
	 *
	 * @param request
	 * @throws Exception
	 */
	@Override
	protected void __init(HttpServletRequest request) throws Exception {
		// If GetImage operate, return directly.
		String servletPath = request.getServletPath();
		if (ParameterAccessor.isGetImageOperator(request)
				&& (IBirtConstants.SERVLET_PATH_FRAMESET.equalsIgnoreCase(servletPath)
						|| IBirtConstants.SERVLET_PATH_OUTPUT.equalsIgnoreCase(servletPath)
						|| IBirtConstants.SERVLET_PATH_RUN.equalsIgnoreCase(servletPath)
						|| IBirtConstants.SERVLET_PATH_PREVIEW.equalsIgnoreCase(servletPath))) {
			return;
		}

		this.category = "BIRT"; //$NON-NLS-1$
		this.masterPageContent = ParameterAccessor.isMasterPageContent(request);
		this.isDesigner = ParameterAccessor.isDesigner();

		if (!ParameterAccessor.isBookmarkReportlet(request)) {
			this.bookmark = ParameterAccessor.getBookmark(request);
		} else {
			this.bookmark = null;
		}

		this.isToc = ParameterAccessor.isToc(request);
		this.reportPage = ParameterAccessor.getPage(request);
		this.reportPageRange = ParameterAccessor.getPageRange(request);
		this.action = ParameterAccessor.getAction(request);

		boolean checkReportDocumentExtension = false;

		// If use frameset/output/download/extract servlet pattern, generate
		// document
		// from design file
		if (IBirtConstants.SERVLET_PATH_FRAMESET.equalsIgnoreCase(servletPath)
				|| IBirtConstants.SERVLET_PATH_OUTPUT.equalsIgnoreCase(servletPath)) {
			this.reportDocumentName = ParameterAccessor.getReportDocument(request, null, true);
			checkReportDocumentExtension = true;
		} else if (IBirtConstants.SERVLET_PATH_DOWNLOAD.equalsIgnoreCase(servletPath)
				|| IBirtConstants.SERVLET_PATH_EXTRACT.equalsIgnoreCase(servletPath)) {
			this.reportDocumentName = ParameterAccessor.getReportDocument(request, null, true);
		} else {
			this.reportDocumentName = ParameterAccessor.getReportDocument(request, null, false);
			if (IBirtConstants.SERVLET_PATH_DOCUMENT.equalsIgnoreCase(servletPath) && reportDocumentName != null) {
				checkReportDocumentExtension = true;
			}
		}
		// Fix for security issue: https://bugs.eclipse.org/bugs/show_bug.cgi?id=538142
		if (checkReportDocumentExtension) {
			checkExtensionAllowedForRPTDocument(this.reportDocumentName);
		}

		this.reportDesignName = ParameterAccessor.getReport(request, null);

		this.emitterId = ParameterAccessor.getEmitterId(request);

		// If print action, force to use postscript format
		this.format = ParameterAccessor.getFormat(request);
		if (IBirtConstants.ACTION_PRINT.equalsIgnoreCase(action)) {
			// Check whether turn on this funtion
			if (ParameterAccessor.isSupportedPrintOnServer) {
				this.format = IBirtConstants.POSTSCRIPT_RENDER_FORMAT;
				this.emitterId = null;
			} else {
				this.action = null;
			}
		}

		// Set locale information
		BirtResources.setLocale(ParameterAccessor.getLocale(request));

		// Set the request type
		this.requestType = request.getHeader(ParameterAccessor.HEADER_REQUEST_TYPE);

		// Determine the report design and doc 's timestamp
		processReport(request);

		// Report title.
		this.reportTitle = ParameterAccessor.getTitle(request);

		// Set whether show the report title
		this.isShowTitle = ParameterAccessor.isShowTitle(request);

		// Set whether show the toolbar
		this.isShowToolbar = ParameterAccessor.isShowToolbar(request);

		// Set whether show the navigation bar
		this.isShowNavigationbar = ParameterAccessor.isShowNavigationbar(request);

		// get some module options
		this.moduleOptions = BirtUtility.getModuleOptions(request);

		this.reportDesignHandle = getDesignHandle(request);
		if (this.reportDesignHandle == null) {
			throw new ViewerException(ResourceConstants.GENERAL_EXCEPTION_NO_REPORT_DESIGN);
		}

		this.reportRtl = null;

		// Initialize report parameters.
		__initParameters(request);
	}

	/**
	 * Prepare the report parameters
	 *
	 * @param request
	 * @throws Exception
	 */
	protected void __initParameters(HttpServletRequest request) throws Exception {
		InputOptions options = new InputOptions();
		options.setOption(InputOptions.OPT_REQUEST, request);
		options.setOption(InputOptions.OPT_LOCALE, locale);
		options.setOption(InputOptions.OPT_TIMEZONE, timeZone);
		options.setOption(InputOptions.OPT_RTL, Boolean.valueOf(rtl));

		// Get parameter definition list
		this.parameterDefList = getReportService().getParameterDefinitions(this.reportDesignHandle, options, false);

		// when use run/parameter in designer and not SOAP request, parse
		// parameters from config file
		if (this.isDesigner && (IBirtConstants.SERVLET_PATH_RUN.equalsIgnoreCase(request.getServletPath())
				|| IBirtConstants.SERVLET_PATH_PARAMETER.equalsIgnoreCase(request.getServletPath()))) {
			parseConfigVars(request, parameterDefList);
		}

		// Get parameters as String Map
		this.parametersAsString = getParsedParametersAsString(parameterDefList, request, options);

		// Check if miss parameter
		if (documentInUrl) {
			this.missingParameter = false;
		} else {
			this.missingParameter = BirtUtility.validateParameters(parameterDefList, this.parametersAsString);
		}

		// Check if show parameter page
		this.isShowParameterPage = checkShowParameterPage(request);

		// Get parameter default values map
		this.defaultValues = getDefaultValues(this.reportDesignHandle, parameterDefList, request, options);

		// Get display text of select parameters
		this.displayTexts = BirtUtility.getDisplayTexts(this.parameterDefList, this.displayTexts, request);

		// Get locale parameter list
		this.locParams = BirtUtility.getLocParams(this.locParams, request);

		// Get parameters as Object Map
		this.parameters = (HashMap) getParsedParameters(this.reportDesignHandle, parameterDefList, request, options);

		// Get parameters as String Map with default value
		this.parametersAsString = getParsedParametersAsStringWithDefaultValue(this.parametersAsString, parameterDefList,
				request, options);
	}

	/**
	 * Check whether show parameter page or not
	 *
	 * @param request
	 * @return
	 */
	private boolean checkShowParameterPage(HttpServletRequest request) {
		if (!ParameterAccessor.HEADER_REQUEST_TYPE_SOAP.equalsIgnoreCase(this.requestType)
				&& !IBirtConstants.SERVLET_PATH_DOWNLOAD.equalsIgnoreCase(request.getServletPath())) {
			String showParameterPage = ParameterAccessor.getShowParameterPage(request);
			if ("false".equalsIgnoreCase(showParameterPage)) //$NON-NLS-1$
			{
				return false;
			}

			if ("true".equalsIgnoreCase(showParameterPage)) //$NON-NLS-1$
			{
				return true;
			}
		}

		return this.missingParameter;
	}

	/**
	 * parse paramenters from config file.
	 *
	 * @param request       HttpServletRequest
	 * @param parameterList Collection
	 * @return
	 */
	protected void parseConfigVars(HttpServletRequest request, Collection parameterList) {
		this.configMap = new HashMap();

		if (this.displayTexts == null) {
			this.displayTexts = new HashMap();
		}

		// get report config file
		String reportConfigName = ParameterAccessor.getConfigFileName(this.reportDesignName);
		if (reportConfigName == null) {
			return;
		}

		File configFile = new File(reportConfigName);

		// check if config file existed
		if (!configFile.exists() || !configFile.isFile()) {
			return;
		}

		// Generate the session handle
		SessionHandle sessionHandle = new DesignEngine(null).newSessionHandle(ULocale.US);
		ReportDesignHandle handle = null;

		try {
			// Open report config file
			handle = sessionHandle.openDesign(reportConfigName);

			// handle config vars
			if (handle != null) {
				String displayTextParam = null;
				Iterator configVars = handle.configVariablesIterator();
				while (configVars != null && configVars.hasNext()) {
					ConfigVariableHandle configVar = (ConfigVariableHandle) configVars.next();
					if (configVar != null) {
						String varName = prepareConfigVarName(configVar.getName());
						Object varValue = configVar.getValue();

						if (varName == null || varValue == null) {
							continue;
						}

						String tempName = varName;
						String paramName;

						// check if null parameter
						if (varName.toLowerCase().startsWith(ParameterAccessor.PARAM_ISNULL)) {
							tempName = (String) varValue;
						}
						// check if display text of select parameter
						else if ((displayTextParam = ParameterAccessor.isDisplayText(varName)) != null) {
							tempName = displayTextParam;
						}

						// check the parameter whether exist or not
						paramName = getParameterName(tempName, parameterList);

						ParameterDefinition parameter = BirtUtility.findParameterDefinition(parameterList, paramName);

						if (parameter != null) {
							// find cached parameter type
							String typeVarName = tempName + "_" + IBirtConstants.PROP_TYPE + "_"; //$NON-NLS-1$ //$NON-NLS-2$
							ConfigVariable typeVar = handle.findConfigVariable(typeVarName);

							// get cached parameter type
							String dataType = ParameterDataTypeConverter.convertDataType(parameter.getDataType());
							String cachedDateType = null;
							if (typeVar != null) {
								cachedDateType = typeVar.getValue();
							}

							// if null or data type changed, skip it
							if (cachedDateType == null || !cachedDateType.equalsIgnoreCase(dataType)) {
								continue;
							}

							// find cached parameter value expression
							String exprVarName = tempName + "_" //$NON-NLS-1$
									+ IBirtConstants.PROP_EXPR + "_"; //$NON-NLS-1$
							ConfigVariable exprVar = handle.findConfigVariable(exprVarName);
							String cachedExpr = null;
							if (exprVar != null) {
								cachedExpr = exprVar.getValue();
							}
							if (cachedExpr == null) {
								cachedExpr = ""; //$NON-NLS-1$
							}

							String expr = parameter.getValueExpr();
							if (expr == null) {
								expr = ""; //$NON-NLS-1$
							}

							// if value expression changed,skip it
							if (!cachedExpr.equals(expr)) {
								continue;
							}

							// multi-value parameter
							List values = null;
							if (parameter.isMultiValue()) {
								values = (List) this.configMap.get(paramName);
								if (values == null) {
									values = new ArrayList();
									this.configMap.put(paramName, values);
								}
							}

							// check if null parameter
							if (varName.toLowerCase().startsWith(ParameterAccessor.PARAM_ISNULL)) {
								if (parameter.isMultiValue()) {
									values.add(null);
								} else {
									this.configMap.put(paramName, null);
								}
							}
							// check if display text of select parameter
							else if ((displayTextParam = ParameterAccessor.isDisplayText(varName)) != null) {
								this.displayTexts.put(paramName, varValue);
							} else {
								Object varObj = null;
								try {
									varObj = DataUtil.convert(varValue, parameter.getDataType());
								} catch (Exception e) {
									varObj = varValue;
								}

								if (parameter.isMultiValue()) {
									values.add(varObj);
								} else {
									this.configMap.put(paramName, varObj);
								}
							}
						}
					}
				}

				handle.close();
			}
		} catch (Exception e) {
			// ignore any exception
		}
	}

	/**
	 * Delete the last "_" part
	 *
	 * @param name
	 * @return String
	 */
	private String prepareConfigVarName(String name) {
		int index = name.lastIndexOf("_"); //$NON-NLS-1$
		return name.substring(0, index);
	}

	/**
	 * if parameter existed in config file, return the correct parameter name
	 *
	 * @param configVarName String
	 * @param parameterList Collection
	 * @return String
	 */
	private String getParameterName(String configVarName, Collection parameterList) throws ReportServiceException {
		String paramName = null;
		if (parameterList != null) {
			for (Iterator iter = parameterList.iterator(); iter.hasNext();) {
				ParameterDefinition parameter = (ParameterDefinition) iter.next();

				// get current name
				String curName = null;
				if (parameter != null && parameter.getName() != null) {
					curName = parameter.getName() + "_" + parameter.getId(); //$NON-NLS-1$
				}

				// if find the parameter exist, return true
				if (curName != null && curName.equalsIgnoreCase(configVarName)) {
					paramName = parameter.getName();
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
	 * @throws Exception
	 * @return Report Design Handle
	 */
	protected IViewerReportDesignHandle getDesignHandle(HttpServletRequest request) throws Exception {
		IViewerReportDesignHandle design = null;
		IReportRunnable reportRunnable = null;
		IReportDocument reportDocumentInstance = null;

		boolean isDocumentExist = ParameterAccessor.isReportParameterExist(request,
				ParameterAccessor.PARAM_REPORT_DOCUMENT);
		boolean isReportExist = ParameterAccessor.isReportParameterExist(request, ParameterAccessor.PARAM_REPORT);

		// if only set __document parameter
		if (isDocumentExist && !isReportExist) {
			// check if document file path is valid
			boolean isValidDocument = ParameterAccessor.isValidFilePath(request,
					ParameterAccessor.getParameter(request, ParameterAccessor.PARAM_REPORT_DOCUMENT));

			if (isValidDocument) {
				// try to open document instance
				try {
					reportDocumentInstance = ReportEngineService.getInstance().openReportDocument(this.reportDesignName,
							this.reportDocumentName, this.moduleOptions);
				} catch (Exception e) {
				}

				if (reportDocumentInstance != null) {
					reportRunnable = reportDocumentInstance.getReportRunnable();
				} else {
					throw new ViewerException(ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_FILE_ERROR,
							new String[] { this.reportDocumentName });
				}
			} else {
				throw new ViewerException(ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_ACCESS_ERROR,
						new String[] { this.reportDocumentName });
			}

		} else if (isReportExist) {
			if (isDocumentExist && !ParameterAccessor.isValidFilePath(request,
					ParameterAccessor.getParameter(request, ParameterAccessor.PARAM_REPORT_DOCUMENT))) {
				throw new ViewerException(ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_ACCESS_ERROR,
						new String[] { this.reportDocumentName });
			}

			// try to open document instance
			try {
				reportDocumentInstance = ReportEngineService.getInstance().openReportDocument(this.reportDesignName,
						this.reportDocumentName, this.moduleOptions);
			} catch (Exception e) {
			}

			// try to get runnable from design file
			if (ParameterAccessor.isValidFilePath(request,
					ParameterAccessor.getParameter(request, ParameterAccessor.PARAM_REPORT))) {
				try {
					// get report runnable from design file
					reportRunnable = BirtUtility.getRunnableFromDesignFile(request, this.reportDesignName,
							this.moduleOptions);

					if (reportRunnable == null) {
						throw new ViewerException(ResourceConstants.GENERAL_EXCEPTION_REPORT_FILE_ERROR,
								new String[] { new File(this.reportDesignName).getName() });
					}
				} catch (EngineException e) {
					this.exception = e;
				}
			}
		}

		if (reportDocumentInstance != null) {
			this.documentInUrl = true;
			this.parameterMap = reportDocumentInstance.getParameterValues();

			// if generating document from report isn't completed
			if (!reportDocumentInstance.isComplete() && isReportExist) {
				this.isDocumentProcessing = true;
			}

			reportDocumentInstance.close();
		}

		if (reportRunnable != null) {
			design = new BirtViewerReportDesignHandle(IViewerReportDesignHandle.RPT_RUNNABLE_OBJECT, reportRunnable);
		}

		return design;
	}

	/**
	 * Determine the report design and doc 's timestamp
	 *
	 * @param request
	 * @throws Exception
	 */
	protected void processReport(HttpServletRequest request) throws Exception {
		// if request is SOAP Post or servlet path is "/download" or "/extract",
		// don't delete document file
		if (ParameterAccessor.HEADER_REQUEST_TYPE_SOAP.equalsIgnoreCase(this.requestType)
				|| IBirtConstants.SERVLET_PATH_DOWNLOAD.equalsIgnoreCase(request.getServletPath())
				|| IBirtConstants.SERVLET_PATH_EXTRACT.equalsIgnoreCase(request.getServletPath())
				|| (this.reportDocumentName == null)) {
			return;
		}

		File reportDocFile = new File(this.reportDocumentName);
		long lastModifiedOfDesign = getLastModifiedOfDesign(request);

		if (lastModifiedOfDesign != -1L && reportDocFile != null && reportDocFile.exists() && reportDocFile.isFile()) {
			if (lastModifiedOfDesign > reportDocFile.lastModified() || ParameterAccessor.isOverwrite(request)) {
				reportDocFile.delete();
			}
		}
	}

	/**
	 * Returns lastModified of report design file. If file doesn't exist, return
	 * -1L;
	 *
	 * @param request
	 * @return
	 */
	protected long getLastModifiedOfDesign(HttpServletRequest request) {
		String designFile = ParameterAccessor.getParameter(request, ParameterAccessor.PARAM_REPORT);
		if (designFile == null) {
			return -1L;
		}

		// according to the working folder
		File file = new File(this.reportDesignName);
		if (file != null && file.exists()) {
			if (file.isFile()) {
				return file.lastModified();
			}
		} else {
			// try URL resource
			try {
				if (!designFile.startsWith("/")) { // $NON-NLS-1$
					designFile = "/" + designFile; //$NON-NLS-1$
				}

				URL url = request.getSession().getServletContext().getResource(designFile);
				if (url != null) {
					return url.openConnection().getLastModified();
				}
			} catch (Exception e) {
			}
		}

		return -1L;
	}

	/**
	 * Get report service instance.
	 */
	@Override
	protected IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}

	/**
	 * get parsed parameters with default value.
	 *
	 * @param design        IViewerReportDesignHandle
	 * @param parameterList Collection
	 * @param request       HttpServletRequest
	 * @param options       InputOptions
	 *
	 * @return Map
	 */
	protected Map<String, Object> getParsedParameters(IViewerReportDesignHandle design,
			Collection<ParameterDefinition> parameterList, HttpServletRequest request, InputOptions options)
			throws ReportServiceException {
		Map<String, Object> params = new HashMap<>();
		if (parameterList == null || this.parametersAsString == null) {
			return params;
		}

		for (Iterator<ParameterDefinition> iter = parameterList.iterator(); iter.hasNext();) {
			// get parameter definition object
			ParameterDefinition parameter = iter.next();
			if (parameter == null) {
				continue;
			}

			String paramName = parameter.getName();
			Object paramObj = this.parametersAsString.get(paramName);

			if (paramObj != null) {
				// get parameter format
				String format = ParameterAccessor.getFormat(request, paramName);
				if (format == null || format.length() <= 0) {
					format = parameter.getPattern();
				}

				// get parameter data type
				String dataType = ParameterDataTypeConverter.convertDataType(parameter.getDataType());

				// check whether locale string
				boolean isLocale = this.locParams.contains(paramName);

				List paramList = null;
				if (paramObj instanceof List) {
					// multi-value parameter
					paramList = (List) paramObj;
				} else {
					paramList = new ArrayList();
					paramList.add(paramObj);
				}

				for (int i = 0; i < paramList.size(); i++) {
					try {
						// convert parameter to object
						Object paramValueObj = DataUtil.validate(paramName, dataType, format, (String) paramList.get(i),
								locale, timeZone, isLocale);
						paramList.set(i, paramValueObj);
					} catch (ViewerValidationException e) {
						// if in PREVIEW mode, then throw exception directly
						if (IBirtConstants.SERVLET_PATH_PREVIEW.equalsIgnoreCase(request.getServletPath())) {
							this.exception = e;
							break;
						}

						// set the wrong parameter
						paramList.set(i, paramList.get(i));
					}
				}

				if (paramObj instanceof List) {
					List list = (List) paramObj;

					// FIXME:if list is empty or only contains null value,
					// regard it as NULL object
					if (list.size() == 0 || (list.size() == 1 && list.get(0) == null)) {
						params.put(paramName, null);
					} else {
						params.put(paramName, paramList.toArray());
					}
				} else {
					params.put(paramName, paramList.get(0));
				}
			} else {
				Object paramValueObj = null;

				// null parameter value
				if (!this.parametersAsString.containsKey(paramName)) {
					// Get parameter default value as object
					paramValueObj = this.defaultValues.get(paramName);
				}

				if (parameter.isMultiValue()) {
					// FIXME: if multi-value only contains null value, regard it
					// as NULL object
					if (paramValueObj != null && !(paramValueObj instanceof Object[])) {
						paramValueObj = new Object[] { paramValueObj };
					}
				}
				params.put(paramName, paramValueObj);
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
	protected Map getDefaultValues(IViewerReportDesignHandle design, Collection parameterList,
			HttpServletRequest request, InputOptions options) throws ReportServiceException {
		Map map = new HashMap();

		// get parameter default values
		for (Iterator iter = parameterList.iterator(); iter.hasNext();) {
			ParameterDefinition parameter = (ParameterDefinition) iter.next();
			if (parameter == null) {
				continue;
			}

			String paramName = parameter.getName();
			if (paramName != null) {
				Object paramValue = this.getReportService().getParameterDefaultValue(design, paramName, options);
				map.put(paramName, paramValue);
			}
		}

		return map;
	}

	/**
	 * get parsed parameters as string.
	 *
	 * @param parameterList Collection
	 * @param request       HttpServletRequest
	 * @param options       InputOptions
	 *
	 * @return Map
	 */
	protected Map getParsedParametersAsString(Collection parameterList, HttpServletRequest request,
			InputOptions options) throws ReportServiceException {
		Map params = new HashMap();
		if (parameterList == null) {
			return params;
		}

		for (Iterator iter = parameterList.iterator(); iter.hasNext();) {
			ParameterDefinition parameter = (ParameterDefinition) iter.next();
			if (parameter == null) {
				continue;
			}

			// get parameter name
			String paramName = parameter.getName();
			if (paramName == null) {
				continue;
			}

			// get parameter value
			String paramValue = null;
			if (ParameterAccessor.isReportParameterExist(request, paramName)) {
				if (parameter.isMultiValue()) {
					// handle multi-value parameter
					List values = ParameterAccessor.getReportParameters(request, paramName);
					params.put(paramName, values);
				} else {
					// Get value from http request
					paramValue = ParameterAccessor.getReportParameter(request, paramName, null);

					params.put(paramName, paramValue);
				}
			} else {
				Object valueObj = null;
				if (this.isDesigner
						&& (IBirtConstants.SERVLET_PATH_RUN.equalsIgnoreCase(request.getServletPath())
								|| IBirtConstants.SERVLET_PATH_PARAMETER.equalsIgnoreCase(request.getServletPath()))
						&& this.configMap != null && this.configMap.containsKey(paramName)) {
					// Get value from config file
					valueObj = this.configMap.get(paramName);
				} else if (this.parameterMap != null && this.parameterMap.containsKey(paramName)) {
					// Get value from document
					valueObj = this.parameterMap.get(paramName);

					// Convert it to a List
					if (valueObj instanceof Object[]) {
						Object[] values = (Object[]) valueObj;
						List list = new ArrayList(values.length);
						for (int i = 0; i < values.length; i++) {
							list.add(values[i]);
						}
						valueObj = list;
					}
				} else {
					// skip it
					continue;
				}

				if (valueObj instanceof List) {
					// handle multi-value parameter
					List values = (List) valueObj;

					for (int i = 0; i < values.size(); i++) {
						paramValue = DataUtil.getDisplayValue(values.get(i), timeZone);
						values.set(i, paramValue);
					}

					params.put(paramName, values);
				} else {
					// return String parameter value
					paramValue = DataUtil.getDisplayValue(valueObj, timeZone);
					params.put(paramName, paramValue);
				}
			}
		}

		return params;
	}

	/**
	 * get parsed parameters as string.
	 *
	 * @param parsedParameters Map
	 * @param parameterList    Collection
	 * @param request          HttpServletRequest
	 * @param options          InputOptions
	 *
	 * @return Map
	 */
	protected Map getParsedParametersAsStringWithDefaultValue(Map aParsedParameters, Collection parameterList,
			HttpServletRequest request, InputOptions options) throws ReportServiceException {
		Map parsedParameters = aParsedParameters;
		if (parsedParameters == null) {
			parsedParameters = new HashMap();
		}

		for (Iterator iter = parameterList.iterator(); iter.hasNext();) {
			// get parameter definition object
			ParameterDefinition parameter = (ParameterDefinition) iter.next();
			if (parameter == null) {
				continue;
			}

			// get parameter name
			String paramName = parameter.getName();
			if (paramName == null) {
				continue;
			}

			// if miss parameter, set parameter value as default value
			if (!parsedParameters.containsKey(paramName)) {
				Object defVals = this.defaultValues.get(paramName);

				if (parameter.isMultiValue()) {
					// ignore the empty list case and handle multi default value
					// case
					if (defVals != null) {
						List values = new ArrayList();

						String paramValue;

						if (defVals.getClass().isArray()) {
							for (int i = 0; i < Array.getLength(defVals); i++) {
								paramValue = DataUtil.getDisplayValue(Array.get(defVals, i), timeZone);
								values.add(paramValue);
							}
						} else {
							paramValue = DataUtil.getDisplayValue(defVals, timeZone);
							values.add(paramValue);
						}

						parsedParameters.put(paramName, values);
					}
				} else {
					String paramValue = DataUtil.getDisplayValue(defVals, timeZone);

					// Bugzilla 259466: select empty string instead of null
					if (paramValue == null && parameter.getControlType() == ParameterDefinition.LIST_BOX
							&& !parameter.isRequired() && parameter.allowBlank() && parameter.getGroup() != null
							&& parameter.getGroup().cascade()) {
						paramValue = ""; //$NON-NLS-1$
					}
					parsedParameters.put(paramName, paramValue);
				}
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
	public ParameterHandle findParameter(String paramName) throws ReportServiceException {
		return BirtUtility.findParameter(this.reportDesignHandle, paramName);
	}

	/**
	 * find the parameter definition object by parameter name
	 *
	 * @param paramName
	 * @return
	 */
	public ParameterDefinition findParameterDefinition(String paramName) {
		return BirtUtility.findParameterDefinition(this.parameterDefList, paramName);
	}

	/**
	 * Returns the report title
	 *
	 * @see org.eclipse.birt.report.context.BaseAttributeBean#getReportTitle()
	 */

	@Override
	public String getReportTitle() throws ReportServiceException {
		String title = BirtUtility.getTitleFromDesign(reportDesignHandle);
		if (title == null || title.trim().length() <= 0) {
			title = reportTitle;
		}

		return title;
	}

	/**
	 * @return the parametersAsString
	 */
	public Map getParametersAsString() {
		return parametersAsString;
	}

	/**
	 * @return the parameterDefList
	 */
	public Collection getParameterDefList() {
		return parameterDefList;
	}

	/**
	 * @return the displayTexts
	 */
	public Map getDisplayTexts() {
		return displayTexts;
	}

	/**
	 * @return the moduleOptions
	 */
	public Map getModuleOptions() {
		return moduleOptions;
	}

	/**
	 * @return the defaultValues
	 */
	public Map getDefaultValues() {
		return defaultValues;
	}

	/**
	 * Returns whether the current report has RTL orientation.
	 *
	 * @return false for LTR, true for RTL
	 */
	public boolean isReportRtl() {
		if (reportRtl == null) {
			IReportRunnable r;
			try {
				r = (IReportRunnable) reportDesignHandle.getDesignObject();
			} catch (ReportServiceException e) {
				return false;
			}
			if (r.getDesignHandle() instanceof ReportDesignHandle) {
				ReportDesignHandle handle = (ReportDesignHandle) r.getDesignHandle();
				reportRtl = DesignChoiceConstants.BIDI_DIRECTION_RTL.equalsIgnoreCase(handle.getBidiOrientation()); // $NON-NLS-1$
			}
		}

		return (reportRtl != null) ? reportRtl.booleanValue() : false;
	}

	/**
	 * Block disallowed extensions and extensions with a suspicious name.
	 *
	 * @param rptDocumentName
	 * @throws ViewerException
	 */
	protected static void checkExtensionAllowedForRPTDocument(String rptDocumentName) throws ViewerException {

		// Parse the filename
		String report = rptDocumentName;
		try {
			report = new File(rptDocumentName).getName();
		} catch (Exception e) {
			throw new ViewerException(BirtResources.getMessage(ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_FILE_ERROR,
					new String[] { report }));
		}

		// Catch invalid document names
		if (report == null || report.trim().isEmpty() || report.trim().endsWith(".")) {
			throw new ViewerException(BirtResources.getMessage(ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_FILE_ERROR,
					new String[] { report }));
		}

		int extIndex = report.lastIndexOf(".");
		String extension = null;
		boolean validExtension = true;

		if (extIndex > -1 && (extIndex + 1) < report.length()) {
			extension = report.substring(extIndex + 1);
			if (!extension.matches("^[A-Za-z0-9]+$")) {
				validExtension = false;
			}

			if (!disallowedExtensionsForRptDocument.isEmpty()
					&& disallowedExtensionsForRptDocument.contains(extension)) {
				validExtension = false;
			}

			if (!allowedExtensionsForRptDocument.isEmpty() && !allowedExtensionsForRptDocument.contains(extension)) {
				validExtension = false;
			}

			if (!validExtension) {
				throw new ViewerException(BirtResources.getMessage(
						ResourceConstants.ERROR_INVALID_EXTENSION_FOR_DOCUMENT_PARAMETER, new String[] { extension }));
			}
		}
	}
}
