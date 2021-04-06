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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.utility.ParameterAccessor;

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
abstract public class BaseAttributeBean {

	/**
	 * Identify the incoming request category.
	 */
	protected String category;

	/**
	 * Need to store the exception.
	 */
	protected Exception exception = null;

	/**
	 * Get report parameters passed in by URL.
	 */
	protected HashMap parameters = null;

	/**
	 * Whether missing parameters.
	 */
	protected boolean missingParameter = false;

	/**
	 * scalar parameter bean.
	 */
	protected ParameterAttributeBean parameterBean = null;

	/**
	 * Viewer report design handle
	 */
	protected IViewerReportDesignHandle reportDesignHandle = null;

	/**
	 * Report design name.
	 */
	protected String reportDesignName = null;

	/**
	 * Report document name.
	 */
	protected String reportDocumentName = null;

	/**
	 * Report title.
	 */
	protected String reportTitle = null;

	/**
	 * Report page number.
	 */
	protected int reportPage;

	/**
	 * Report page range.
	 */
	protected String reportPageRange;

	/**
	 * Current locale.
	 */
	protected Locale locale = null;

	/**
	 * Current time zone
	 */
	protected TimeZone timeZone = null;

	/**
	 * Enable master page content.
	 */
	protected boolean masterPageContent = true;

	/**
	 * In designer context.
	 */
	protected boolean isDesigner = false;

	/**
	 * Bookmark.
	 */
	protected String bookmark = null;

	/**
	 * Reportlet id.
	 */
	protected String reportletId = null;

	/**
	 * Report format of the request.
	 */

	protected String format = ParameterAccessor.PARAM_FORMAT_HTML;

	/**
	 * Emitter id of the request.
	 */
	protected String emitterId = null;

	/**
	 * values from config file
	 */
	protected Map configMap = null;

	/**
	 * values from document file
	 */
	protected Map parameterMap = null;

	/**
	 * RTL option.
	 */

	protected boolean rtl = false;

	/**
	 * determin whether the link is a toc or bookmark
	 */
	protected boolean isToc = false;

	/**
	 * indicate whether the document is existed.
	 */
	protected boolean documentInUrl = false;

	/**
	 * current task id
	 */
	protected String taskId;

	/**
	 * indicate whether show the title
	 */
	protected boolean isShowTitle = true;

	/**
	 * indicate whether show the toolbar
	 */
	protected boolean isShowToolbar = true;

	/**
	 * indicate whether show the navigation bar
	 */
	protected boolean isShowNavigationbar = true;

	/**
	 * indicate whether show parameter dialog or not..
	 */
	protected boolean isShowParameterPage = false;

	/**
	 * If document generated completely
	 */
	protected boolean isDocumentProcessing = false;

	/**
	 * indicate action name.
	 */
	protected String action = null;

	/**
	 * Abstract methods.
	 */
	abstract protected void __init(HttpServletRequest request) throws Exception;

	abstract protected IViewerReportService getReportService();

	/**
	 * Default constructor.
	 */
	public BaseAttributeBean() {
	}

	/**
	 * Template init implementation.
	 * 
	 * @param request
	 * @throws Exception
	 */
	protected void init(HttpServletRequest request) throws Exception {
		this.locale = ParameterAccessor.getLocale(request);
		this.timeZone = ParameterAccessor.getTimeZone(request);
		this.rtl = ParameterAccessor.isRtl(request);
		this.reportletId = ParameterAccessor.getReportletId(request);
		this.__init(request);
	}

	/**
	 * @return Returns the parameterBean.
	 */
	public ParameterAttributeBean getParameterBean() {
		return parameterBean;
	}

	/**
	 * @param parameterBean The parameterBean to set.
	 */
	public void setParameterBean(ParameterAttributeBean parameterBean) {
		this.parameterBean = parameterBean;
	}

	/**
	 * @return Returns the reportTitle.
	 */
	public String getReportTitle() throws ReportServiceException {
		return reportTitle;
	}

	/**
	 * @return the reportPage
	 */
	public int getReportPage() {
		return reportPage;
	}

	/**
	 * @return the reportPageRange
	 */
	public String getReportPageRange() {
		return reportPageRange;
	}

	/**
	 * @return Returns the locale.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @return the language code for the current locale
	 */
	public String getLanguage() {
		return locale.getLanguage();
	}

	/**
	 * @return returns the time zone.
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}

	/**
	 * @return Returns the useTestConfig.
	 */
	public boolean isDesigner() {
		return isDesigner;
	}

	/**
	 * @return Returns the exception.
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * @return Returns the reportDocumentName.
	 */
	public String getReportDocumentName() {
		return reportDocumentName;
	}

	/**
	 * @param reportDocumentName the reportDocumentName to set
	 */
	public void setReportDocumentName(String reportDocumentName) {
		this.reportDocumentName = reportDocumentName;
	}

	/**
	 * @return Returns the bookmark.
	 */
	public String getBookmark() {
		return bookmark;
	}

	/**
	 * @return Returns the parameters.
	 */
	public HashMap getParameters() {
		return parameters;
	}

	/**
	 * @return Returns the masterPageContent.
	 */
	public boolean isMasterPageContent() {
		return masterPageContent;
	}

	/**
	 * @return Returns the missingParameter.
	 */
	public boolean isMissingParameter() {
		return missingParameter;
	}

	/**
	 * @return Returns incoming request's category.
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return Returns incoming request's category.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return report design name.
	 */
	public String getReportDesignName() {
		return reportDesignName;
	}

	/**
	 * @return the format
	 */

	public String getFormat() {
		return format;
	}

	/**
	 * @return the emitterId
	 */
	public String getEmitterId() {
		return emitterId;
	}

	/**
	 * @return the rtl
	 */
	public boolean isRtl() {
		return rtl;
	}

	protected Object getParamValueObject(HttpServletRequest request, ParameterDefinition parameterObj)
			throws ReportServiceException {
		String paramName = parameterObj.getName();
		String format = parameterObj.getDisplayFormat();
		if (ParameterAccessor.isReportParameterExist(request, paramName)) {
			ReportParameterConverter converter = new ReportParameterConverter(format, locale);
			// Get value from http request
			String paramValue = ParameterAccessor.getReportParameter(request, paramName, null);
			return converter.parse(paramValue, parameterObj.getDataType());
		}
		return null;
	}

	/**
	 * @return the reportDesignHandle
	 */
	public IViewerReportDesignHandle getReportDesignHandle(HttpServletRequest request) {
		return reportDesignHandle;
	}

	/**
	 * @return the isToc
	 */
	public boolean isToc() {
		return isToc;
	}

	public String getReportletId() {
		return reportletId;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the isShowNavigationbar
	 */
	public boolean isShowNavigationbar() {
		return isShowNavigationbar;
	}

	/**
	 * @return the isShowTitle
	 */
	public boolean isShowTitle() {
		return isShowTitle;
	}

	/**
	 * @return the isShowToolbar
	 */
	public boolean isShowToolbar() {
		return isShowToolbar;
	}

	/**
	 * @return the isShowParameterPage
	 */
	public boolean isShowParameterPage() {
		return isShowParameterPage;
	}

	/**
	 * @return the isDocumentProcessing
	 */
	public boolean isDocumentProcessing() {
		return isDocumentProcessing;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @return the documentInUrl
	 */
	public boolean isDocumentInUrl() {
		return documentInUrl;
	}

	/**
	 * @param documentInUrl the documentInUrl to set
	 */
	public void setDocumentInUrl(boolean documentInUrl) {
		this.documentInUrl = documentInUrl;
	}

	public String getClientInitialize() {
		IReportRunnable r;
		try {
			r = (IReportRunnable) reportDesignHandle.getDesignObject();
		} catch (ReportServiceException e) {
			return "";
		}
		if (r.getDesignHandle() instanceof ReportDesignHandle) {
			ReportDesignHandle handle = (ReportDesignHandle) r.getDesignHandle();
			return handle.getClientInitialize();
		}
		return "";
	}
}