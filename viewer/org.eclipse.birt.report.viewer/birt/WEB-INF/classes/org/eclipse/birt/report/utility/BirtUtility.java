/*************************************************************************************
 * Copyright (c) 2004, 2024 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation	- Initial implementation.
 *     Thomas Gutmann		- Implementation of display text for multi selections
 ************************************************************************************/

package org.eclipse.birt.report.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.BaseTaskBean;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.model.api.IModuleOption;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.ParameterDataTypeConverter;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;

import com.ibm.icu.util.ULocale;

/**
 * Utilities for Birt Report Service
 *
 */
public class BirtUtility {

	/**
	 * none value
	 */
	public final static String NONE = "none"; //$NON-NLS-1$

	/**
	 * Request attribute name that indicates the viewer marker has been cleared
	 */
	public final static String VIEWER_MARKER_CLEARED = "ViewerMarkerCleared"; //$NON-NLS-1$

	/**
	 * Add current task in http session
	 *
	 * @param request
	 * @param task
	 */
	public static void addTask(HttpServletRequest request, IEngineTask task) {
		if (request == null || task == null) {
			return;
		}

		try {
			// get task id
			BaseAttributeBean attrBean = (BaseAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
			if (attrBean == null) {
				return;
			}

			String taskid = attrBean.getTaskId();
			if (taskid == null) {
				return;
			}

			// get task map
			HttpSession session = request.getSession(true);
			Map map = (Map) session.getAttribute(IBirtConstants.TASK_MAP);
			if (map == null) {
				map = new HashMap();
				session.setAttribute(IBirtConstants.TASK_MAP, map);
			}

			// add task
			synchronized (map) {
				BaseTaskBean bean = new BaseTaskBean(taskid, task);
				map.put(taskid, bean);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Remove task from http session
	 *
	 * @param request
	 */
	public static void removeTask(HttpServletRequest request) {
		if (request == null) {
			return;
		}

		try {
			// get task id
			BaseAttributeBean attrBean = (BaseAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
			if (attrBean == null) {
				return;
			}

			String taskid = attrBean.getTaskId();
			if (taskid == null) {
				return;
			}

			// get task map
			HttpSession session = request.getSession(true);
			Map map = (Map) session.getAttribute(IBirtConstants.TASK_MAP);
			if (map == null) {
				return;
			}

			// remove task
			synchronized (map) {
				map.remove(taskid);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Cancel the current engine task by task id
	 *
	 * @param request
	 * @param taskid
	 * @throws Exception
	 */
	public static void cancelTask(HttpServletRequest request, String taskid) throws Exception {
		if (taskid == null) {
			return;
		}

		// get task map
		HttpSession session = request.getSession();
		if (session == null) {
			return;
		}

		Map map = (Map) session.getAttribute(IBirtConstants.TASK_MAP);
		if (map != null && map.containsKey(taskid)) {
			BaseTaskBean bean = (BaseTaskBean) map.get(taskid);
			if (bean == null) {
				return;
			}

			// cancel task
			IEngineTask task = bean.getTask();
			if (task != null) {
				task.cancel();
			}

			// remove task from task map
			synchronized (map) {
				map.remove(taskid);
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
	public static List getParameterList(IViewerReportDesignHandle reportDesignHandle) throws ReportServiceException {
		IReportRunnable runnable = (IReportRunnable) reportDesignHandle.getDesignObject();
		if (runnable == null) {
			return null;
		}

		ModuleHandle model = runnable.getDesignHandle().getModuleHandle();
		if (model == null) {
			return null;
		}

		return model.getFlattenParameters();
	}

	/**
	 * find the parameter definition by parameter name
	 *
	 * @param parameterList
	 * @param paramName
	 * @return
	 */
	public static ParameterDefinition findParameterDefinition(Collection parameterList, String paramName) {
		if (parameterList == null || paramName == null) {
			return null;
		}

		// find parameter definition
		for (Iterator iter = parameterList.iterator(); iter.hasNext();) {
			ParameterDefinition parameter = (ParameterDefinition) iter.next();
			if (parameter == null) {
				continue;
			}

			String name = parameter.getName();
			if (paramName.equals(name)) {
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
	public static ParameterHandle findParameter(IViewerReportDesignHandle reportDesignHandle, String paramName)
			throws ReportServiceException {
		if (paramName == null) {
			return null;
		}

		IReportRunnable runnable = (IReportRunnable) reportDesignHandle.getDesignObject();
		if (runnable == null) {
			return null;
		}

		// get module handle from report runnable
		ModuleHandle model = runnable.getDesignHandle().getModuleHandle();
		if (model == null) {
			return null;
		}

		return model.findParameter(paramName);
	}

	/**
	 * Gets the module option map from the request.
	 *
	 * @param request the request
	 * @return the module options
	 */

	public static Map getModuleOptions(HttpServletRequest request) {
		Map options = new HashMap();
		options.put(IModuleOption.RESOURCE_FOLDER_KEY, ParameterAccessor.getResourceFolder(request));
		options.put(IModuleOption.PARSER_SEMANTIC_CHECK_KEY, Boolean.FALSE);
		return options;
	}

	/**
	 * Get Display Text of select parameters
	 *
	 * @param parameters
	 * @param displayTexts
	 * @param request
	 * @return Map
	 */
	public static Map<String, Serializable> getDisplayTexts(Collection<?> parameters,
			Map<String, Serializable> displayTexts,
			HttpServletRequest request) {
		if (displayTexts == null) {
			displayTexts = new HashMap<String, Serializable>();
		}

		Enumeration<String> params = request.getParameterNames();
		while (params != null && params.hasMoreElements()) {
			String param = DataUtil.getString(params.nextElement());
			String paramName = ParameterAccessor.isDisplayText(param);
			if (paramName != null) {
				ParameterDefinition parameter = findParameterDefinition(parameters, paramName);
				if (parameter != null) {
					if (parameter.isMultiValue()) {
						ArrayList<String> tmpDisplayTexts = new ArrayList<String>();
						Set<String> setDisplayText = ParameterAccessor.getParameterValues(request, param);
						Iterator<?> displayTextIter = setDisplayText.iterator();
						while (displayTextIter.hasNext()) {
							tmpDisplayTexts.add((String) displayTextIter.next());
						}
						displayTexts.put(paramName, tmpDisplayTexts);
					} else {
						displayTexts.put(paramName, ParameterAccessor.getParameter(request, param));
					}
				}
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
	public static List getLocParams(List locParams, HttpServletRequest request) {
		if (locParams == null) {
			locParams = new ArrayList();
		}

		String[] arrs = request.getParameterValues(ParameterAccessor.PARAM_ISLOCALE);
		if (arrs != null) {
			for (int i = 0; i < arrs.length; i++) {
				locParams.add(arrs[i]);
			}
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
	public static boolean validateParameters(Collection parameterList, Map parameters) {
		assert parameters != null;
		boolean missingParameter = false;

		Iterator iter = parameterList.iterator();
		while (iter.hasNext()) {
			ParameterDefinition parameter = (ParameterDefinition) iter.next();

			String parameterName = parameter.getName();
			Object parameterValue = parameters.get(parameterName);

			// hidden type parameter
			if (parameter.isHidden() || !parameter.isRequired()) {
				continue;
			}

			// Null Value
			if (parameterValue == null) {
				missingParameter = true;
				break;
			}

			if (parameterValue instanceof List) {
				// handle multi-value parameter
				List values = (List) parameterValue;
				for (int i = 0; i < values.size(); i++) {
					Object value = values.get(i);
					if ((value == null) || (value instanceof String && ((String) value).length() <= 0)) {
						missingParameter = true;
						break;
					}
				}

				if (missingParameter) {
					break;
				}
			} else // Blank Value
			if (parameterValue instanceof String && ((String) parameterValue).length() <= 0) {
				missingParameter = true;
				break;
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
	public static void handleOperation(Operation operation, ViewerAttributeBean bean, Map<String, Object> parameterMap,
			Map<String, Object> displayTexts) throws Exception {
		if (operation == null || bean == null || parameterMap == null || displayTexts == null) {
			return;
		}

		// convert parameter from SOAP operation
		List<Object> locs = new ArrayList();
		Map<String, List> params = new HashMap<String, List>();
		String displayTextParam = null;
		Oprand[] oprands = operation.getOprand();
		for (int i = 0; i < oprands.length; i++) {
			String paramName = oprands[i].getName();
			Object paramValue = oprands[i].getValue();

			if (paramName == null || paramValue == null) {
				continue;
			}

			if (paramName.equalsIgnoreCase(ParameterAccessor.PARAM_ISLOCALE)) {
				// parameter value is a locale string
				locs.add(paramValue);
			}
			// display text of parameter
			else if ((displayTextParam = ParameterAccessor.isDisplayText(paramName)) != null) {
				ParameterDefinition parameter = bean.findParameterDefinition(displayTextParam);
				if (parameter != null) {
					if (parameter.isMultiValue()) {
						ArrayList<String> tmpDisplayTexts = new ArrayList<String>();
						if (displayTexts.containsKey(displayTextParam)) {
							tmpDisplayTexts = (ArrayList<String>) displayTexts.get(displayTextParam);
							displayTexts.remove(displayTextParam);
						}
						tmpDisplayTexts.add((String) paramValue);
						displayTexts.put(displayTextParam, tmpDisplayTexts);
					} else {
						displayTexts.put(displayTextParam, paramValue);
					}
				}
				continue;
			} else // Check if it's the parameter value list set to null
			if (ParameterAccessor.PARAM_ISNULLLIST.equalsIgnoreCase(paramName)) {
				paramName = (String) paramValue;
				// remove from the map rather than set to null, so it get
				// the chance to use the default values
				params.remove(paramName);
			} else {
				// Check if it's the parameter value set to null
				if (ParameterAccessor.PARAM_ISNULL.equalsIgnoreCase(paramName)) {
					paramName = (String) paramValue;
					paramValue = null;
				}

				List list = (List) params.get(paramName);
				if (list == null) {
					list = new ArrayList();
					params.put(paramName, list);
				}
				list.add(paramValue);
			}
		}

		Iterator it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String paramName = (String) entry.getKey();
			List paramValues = (List) entry.getValue();

			// find the parameter
			ParameterDefinition parameter = bean.findParameterDefinition(paramName);
			if (parameter == null) {
				continue;
			}

			String pattern = parameter.getPattern();
			String dataType = ParameterDataTypeConverter.convertDataType(parameter.getDataType());

			// check whether it is a locale String.
			boolean isLocale = locs.contains(paramName);

			// convert parameter
			if (parameter.isMultiValue()) {
				if (paramValues == null) {
					// the value list is null
					parameterMap.put(paramName, null);
				} else {
					List values = new ArrayList();

					// convert multi-value parameter
					for (int i = 0; i < paramValues.size(); i++) {
						Object paramValueObj = DataUtil.validate(paramName, dataType, pattern,
								(String) paramValues.get(i), bean.getLocale(), bean.getTimeZone(), isLocale);
						values.add(paramValueObj);
					}

					parameterMap.put(paramName, values.toArray());
				}
			} else {
				// single parameter value
				Object paramValueObj = DataUtil.validate(paramName, dataType, pattern,
						paramValues == null ? null : (String) paramValues.get(0), bean.getLocale(), bean.getTimeZone(),
						isLocale);

				// push to parameter map
				parameterMap.put(paramName, paramValueObj);
			}
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
	public static IReportRunnable getRunnableFromDesignFile(HttpServletRequest request, String designFile, Map options)
			throws EngineException {
		IReportRunnable reportRunnable = null;

		// check the design file if exist
		File file = new File(designFile);
		if (file.exists()) {
			reportRunnable = ReportEngineService.getInstance().openReportDesign(designFile, options);
		} else {
			// try to get resource from war package
			InputStream is = null;
			URL url = null;
			try {
				if (!ParameterAccessor.isUniversalPath(designFile)) {
					designFile = ParameterAccessor.workingFolder + "/" //$NON-NLS-1$
							+ ParameterAccessor.getParameter(request, ParameterAccessor.PARAM_REPORT);
				}

				// try detect as resource path first
				String resoureFile = designFile;
				if (!resoureFile.startsWith("/")) //$NON-NLS-1$
				{
					resoureFile = "/" + resoureFile; //$NON-NLS-1$
				}

				url = request.getSession().getServletContext().getResource(resoureFile);
				if (url != null) {
					is = url.openStream();
				} else {
					// try handle the design file path as url directly
					url = new URL(designFile);
					is = url.openStream();
				}

				if (is != null) {
					reportRunnable = ReportEngineService.getInstance().openReportDesign(url.toString(), is, options);
				}

			} catch (Exception e) {
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
	public static String getTitleFromDesign(IViewerReportDesignHandle reportDesignHandle)
			throws ReportServiceException {
		String reportTitle = null;
		if (reportDesignHandle != null) {
			Object design = reportDesignHandle.getDesignObject();
			if (design instanceof IReportRunnable) {
				IReportRunnable runnable = (IReportRunnable) design;
				if (runnable.getDesignHandle() != null) {
					ModuleHandle moduleHandle = runnable.getDesignHandle().getModuleHandle();
					String key = moduleHandle.getStringProperty(ReportDesignHandle.TITLE_ID_PROP);
					if (key != null && moduleHandle.getMessage(key) != null) {
						reportTitle = moduleHandle.getMessage(key);
					} else {
						reportTitle = (String) runnable.getProperty(IReportRunnable.TITLE);
					}
				} else {
					reportTitle = (String) runnable.getProperty(IReportRunnable.TITLE);
				}
			}
		}

		return reportTitle;
	}

	public static String getStackTrace(Throwable e) {
		StringWriter stackTraceWriter = null;
		PrintWriter writer = null;
		try {
			stackTraceWriter = new StringWriter();
			writer = new PrintWriter(stackTraceWriter);
			e.printStackTrace(writer);
			return stackTraceWriter.toString();
		} finally {
			if (stackTraceWriter != null) {
				try {
					stackTraceWriter.close();
				} catch (IOException e1) {
				}
			}
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * Converts the given text into HTML code. Replaces the newlines into <BR>
	 * and the leading spaces/tabs into &nbsp;
	 *
	 * @param text
	 * @return
	 */
	public static String toHtml(String text) {
		return text.replace("\n", "<BR/>"); // .replaceAll(
		// "^([\s])*[^\s]", "&nbsp;");
	}

	/**
	 * Creates an axis fault based on a given exception.
	 *
	 * @param qName QName of the fault
	 * @param e     exception
	 * @return axis fault
	 */
	public static AxisFault makeAxisFault(String qName, Exception e) {
		AxisFault fault = makeAxisFault(e);
		fault.setFaultCode(new QName(qName));
		return fault;
	}

	/**
	 * Creates an axis fault based on a given exception.
	 *
	 * @param qName QName of the fault
	 * @param e     exception
	 * @return axis fault
	 */
	public static AxisFault makeAxisFault(Exception e) {
		if (e instanceof AxisFault) {
			return (AxisFault) e;
		} else {
			AxisFault fault = AxisFault.makeFault(e);
			fault.addFaultDetailString(BirtUtility.getStackTrace(e));
			return fault;
		}
	}

	/**
	 * Creates an axis fault grouping the given exceptions list
	 *
	 * @param qName      QName of the fault
	 * @param exceptions list of exceptions
	 * @return axis fault
	 */
	public static Exception makeAxisFault(String qName, Collection<Exception> exceptions) {
		if (exceptions.size() == 1) {
			return makeAxisFault(qName, exceptions.iterator().next());
		} else {
			QName exceptionQName = new QName("string");
			AxisFault fault = new AxisFault(
					BirtResources.getMessage(ResourceConstants.GENERAL_EXCEPTION_MULTIPLE_EXCEPTIONS));
			fault.setFaultCode(new QName(qName));

			for (Iterator i = exceptions.iterator(); i.hasNext();) {
				Exception e = (Exception) i.next();
				fault.addFaultDetail(exceptionQName, getStackTrace(e));
			}
			return fault;
		}
	}

	/**
	 * Append Error Message with stack trace
	 *
	 * @param out
	 * @param e
	 * @throws IOException
	 */
	public static void appendErrorMessage(OutputStream out, Exception e) throws IOException {
		StringBuilder message = new StringBuilder();
		message.append("<html>\n<head>\n<title>" //$NON-NLS-1$
				+ BirtResources.getMessage("birt.viewer.title.error") //$NON-NLS-1$
				+ "</title>\n"); //$NON-NLS-1$
		message.append("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=utf-8\"/>\n</head>\n") //$NON-NLS-1$
				.append("<body>\n"); //$NON-NLS-1$

		String errorId = "document.getElementById('error_detail')"; //$NON-NLS-1$
		String errorIcon = "document.getElementById('error_icon')"; //$NON-NLS-1$
		String onClick = "if (" + errorId + ".style.display == 'none') { " //$NON-NLS-1$//$NON-NLS-2$
				+ errorIcon + ".innerHTML = '- '; " + errorId //$NON-NLS-1$
				+ ".style.display = 'block'; }" + "else { " + errorIcon //$NON-NLS-1$//$NON-NLS-2$
				+ ".innerHTML = '+ '; " + errorId //$NON-NLS-1$
				+ ".style.display = 'none'; }"; //$NON-NLS-1$
		message.append("<div id=\"birt_errorPage\" style=\"color:red\">\n") //$NON-NLS-1$
				.append("<span id=\"error_icon\"  style=\"cursor:pointer\" onclick=\"" //$NON-NLS-1$
						+ onClick + "\" > + </span>\n"); //$NON-NLS-1$

		String errorMessage = null;
		if (e instanceof AxisFault) {
			errorMessage = ((AxisFault) e).getFaultString();
		} else {
			errorMessage = e.getLocalizedMessage();

		}
		if (errorMessage != null) {
			message.append(ParameterAccessor.htmlEncode(errorMessage));
		} else {
			message.append("Unknown error!"); //$NON-NLS-1$
		}

		message.append("<br>\n") //$NON-NLS-1$
				.append("<pre id=\"error_detail\" style=\"display:none;\" >\n")//$NON-NLS-1$
				.append(ParameterAccessor.htmlEncode(getDetailMessage(e))).append("</pre>\n") //$NON-NLS-1$
				.append("</div>\n") //$NON-NLS-1$
				.append("</body>\n</html>"); //$NON-NLS-1$

		out.write(message.toString().getBytes("UTF-8")); //$NON-NLS-1$
		out.flush();
		out.close();
	}

	/**
	 * Returns the detail message of exception
	 *
	 * @param tx
	 * @return
	 */
	public static String getDetailMessage(Throwable tx) {
		StringWriter out = new StringWriter();
		PrintWriter print = new PrintWriter(out);
		try {
			tx.printStackTrace(print);
		} catch (Throwable ex) {
		}

		print.flush();
		print.close();
		return out.getBuffer().toString();
	}

	/**
	 * Write message into output stream.
	 *
	 * @param out
	 * @param message
	 * @param msgType
	 * @param isCloseWin
	 */
	public static void writeMessage(OutputStream out, String content, String msgType, boolean isCloseWin)
			throws IOException {
		String fontColor = "black"; //$NON-NLS-1$
		if (IBirtConstants.MSG_ERROR.equalsIgnoreCase(msgType)) {
			fontColor = "red"; //$NON-NLS-1$
		}

		StringBuilder message = new StringBuilder();
		message.append("<html><head><title>" //$NON-NLS-1$
				+ BirtResources.getMessage("birt.viewer.title." + msgType) //$NON-NLS-1$
				+ "</title>");//$NON-NLS-1$
		message.append("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=utf-8\"></head>") //$NON-NLS-1$
				.append("<body"); //$NON-NLS-1$
		if (isCloseWin) {
			message.append(" onload=\"javascript:window.close()\""); //$NON-NLS-1$
		}
		message.append(" style=\"background-color: silver; color:" //$NON-NLS-1$
				+ fontColor + "; font-size:10pt;\">" //$NON-NLS-1$
				+ content + "</body></html>"); //$NON-NLS-1$
		out.write(message.toString().getBytes("UTF-8")); //$NON-NLS-1$
		out.flush();
		out.close();
	}

	/**
	 * Handle print action
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void doPrintAction(InputStream inputStream, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, RemoteException {
		Printer printer = PrintUtility.getPrinter(request);
		response.setContentType("text/html; charset=utf-8"); //$NON-NLS-1$
		if (printer != null) {
			PrintUtility.execPrint(inputStream, printer);
			writeMessage(response.getOutputStream(),
					BirtResources.getMessage("birt.viewer.dialog.printserver.complete"), IBirtConstants.MSG_COMPLETE, //$NON-NLS-1$
					true);
		} else {
			writeMessage(response.getOutputStream(),
					BirtResources.getMessage("birt.viewer.dialog.printserver.error.noprinter"), //$NON-NLS-1$
					IBirtConstants.MSG_ERROR, false);
		}
	}

	/**
	 * Returns toc style from TOCNode
	 *
	 * @param node
	 * @return
	 */
	public static String getTOCStyle(TOCNode node) {
		if (node == null) {
			return null;
		}

		IScriptStyle scriptStyle = node.getTOCStyle();
		if (scriptStyle == null) {
			return null;
		}

		StringBuilder style = new StringBuilder();

		// background-attachment
		style.append(getStyle(scriptStyle, "getBackgroundAttachment", //$NON-NLS-1$
				"background-attachment")); //$NON-NLS-1$

		// background-color
		style.append(getStyle(scriptStyle, "getBackgroundColor", //$NON-NLS-1$
				"background-color")); //$NON-NLS-1$

		// background-image
		style.append(getStyle(scriptStyle, "getBackgroundImage", //$NON-NLS-1$
				"background-image")); //$NON-NLS-1$

		// background-position-x/y
		style.append(getStyle(scriptStyle, "getBackgroundPositionX", //$NON-NLS-1$
				"background-position-x")); //$NON-NLS-1$
		style.append(getStyle(scriptStyle, "getBackgroundPositionY", //$NON-NLS-1$
				"background-position-y")); //$NON-NLS-1$

		// background-repeat
		style.append(getStyle(scriptStyle, "getBackgroundRepeat", //$NON-NLS-1$
				"background-repeat")); //$NON-NLS-1$

		// Border Bottom
		style.append(getStyle(scriptStyle, "getBorderBottomColor", //$NON-NLS-1$
				"border-bottom-color")); //$NON-NLS-1$
		style.append(getStyle(scriptStyle, "getBorderBottomStyle", //$NON-NLS-1$
				"border-bottom-style")); //$NON-NLS-1$
		style.append(getStyle(scriptStyle, "getBorderBottomWidth", //$NON-NLS-1$
				"border-bottom-width")); //$NON-NLS-1$

		// Border Left
		style.append(getStyle(scriptStyle, "getBorderLeftColor", //$NON-NLS-1$
				"border-left-color")); //$NON-NLS-1$
		style.append(getStyle(scriptStyle, "getBorderLeftStyle", //$NON-NLS-1$
				"border-left-style")); //$NON-NLS-1$
		style.append(getStyle(scriptStyle, "getBorderLeftWidth", //$NON-NLS-1$
				"border-left-width")); //$NON-NLS-1$

		// Border Right
		style.append(getStyle(scriptStyle, "getBorderRightColor", //$NON-NLS-1$
				"border-right-color"));//$NON-NLS-1$
		style.append(getStyle(scriptStyle, "getBorderRightStyle", //$NON-NLS-1$
				"border-right-style"));//$NON-NLS-1$
		style.append(getStyle(scriptStyle, "getBorderRightWidth", //$NON-NLS-1$
				"border-right-width"));//$NON-NLS-1$

		// Border Top
		style.append(getStyle(scriptStyle, "getBorderTopColor", "border-top-color"));//$NON-NLS-1$ //$NON-NLS-2$
		style.append(getStyle(scriptStyle, "getBorderTopStyle", "border-top-style"));//$NON-NLS-1$ //$NON-NLS-2$
		style.append(getStyle(scriptStyle, "getBorderTopWidth", "border-top-width"));//$NON-NLS-1$ //$NON-NLS-2$

		// font
		style.append(getStyle(scriptStyle, "getColor", "color"));//$NON-NLS-1$ //$NON-NLS-2$
		style.append(getStyle(scriptStyle, "getFontFamily", "font-family"));//$NON-NLS-1$ //$NON-NLS-2$
		style.append(getStyle(scriptStyle, "getFontSize", "font-size"));//$NON-NLS-1$ //$NON-NLS-2$
		style.append(getStyle(scriptStyle, "getFontStyle", "font-style"));//$NON-NLS-1$ //$NON-NLS-2$
		style.append(getStyle(scriptStyle, "getFontVariant", "font-variant"));//$NON-NLS-1$ //$NON-NLS-2$
		style.append(getStyle(scriptStyle, "getFontWeight", "font-weight"));//$NON-NLS-1$ //$NON-NLS-2$

		// letter-spacing
		style.append(getStyle(scriptStyle, "getLetterSpacing", "letter-spacing"));//$NON-NLS-1$ //$NON-NLS-2$

		// line-height
		style.append(getStyle(scriptStyle, "getLineHeight", "line-height"));//$NON-NLS-1$ //$NON-NLS-2$

		// padding
		style.append(getStyle(scriptStyle, "getPaddingBottom", "padding-bottom"));//$NON-NLS-1$ //$NON-NLS-2$
		style.append(getStyle(scriptStyle, "getPaddingLeft", "padding-left"));//$NON-NLS-1$ //$NON-NLS-2$
		style.append(getStyle(scriptStyle, "getPaddingRight", "padding-right"));//$NON-NLS-1$ //$NON-NLS-2$
		style.append(getStyle(scriptStyle, "getPaddingTop", "padding-top"));//$NON-NLS-1$ //$NON-NLS-2$

		// Text
		style.append(getStyle(scriptStyle, "getTextAlign", "text-align"));//$NON-NLS-1$ //$NON-NLS-2$
		style.append(getStyle(scriptStyle, "getTextTransform", "text-transform"));//$NON-NLS-1$ //$NON-NLS-2$

		String textDecoration = ""; //$NON-NLS-1$
		String textOverline = scriptStyle.getTextOverline();
		if (textOverline != null && !NONE.equalsIgnoreCase(textOverline)) {
			textDecoration += textOverline + " "; //$NON-NLS-1$
		}

		String textLinethrough = scriptStyle.getTextLineThrough();
		if (textLinethrough != null && !NONE.equalsIgnoreCase(textLinethrough)) {
			textDecoration += textLinethrough + " "; //$NON-NLS-1$
		}

		String textUnderline = scriptStyle.getTextUnderline();
		if (textUnderline != null && !NONE.equalsIgnoreCase(textUnderline)) {
			textDecoration += textUnderline + " "; //$NON-NLS-1$
		}

		if (textDecoration.length() > 0) {
			style.append("text-decoration:" + textDecoration + ";"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// word-spacing
		style.append(getStyle(scriptStyle, "getWordSpacing", "word-spacing"));//$NON-NLS-1$ //$NON-NLS-2$

		return style.toString();
	}

	/**
	 * Returns the CSS text
	 *
	 * @param obj
	 * @param methodName
	 * @param cssAttr
	 * @return
	 */
	private static String getStyle(Object obj, String methodName, String cssAttr) {
		assert obj != null;
		assert methodName != null;
		assert cssAttr != null;

		String style = invokeGetStyle(obj, methodName);
		if (style == null || NONE.equalsIgnoreCase(style)) {
			return ""; //$NON-NLS-1$
		}

		return cssAttr + ":" + style + ";"; //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Invoke GET method
	 *
	 * @param obj
	 * @param methodName
	 * @return
	 */
	private static String invokeGetStyle(Object obj, String methodName) {
		String style = null;

		try {
			Method method = IScriptStyle.class.getMethod(methodName, new Class[] {});
			if (method == null) {
				return null;
			}

			Object value = method.invoke(obj, new Object[] {});
			if (value != null) {
				style = (String) value;
			}
		} catch (Exception e) {
		}

		return style;
	}

	/**
	 * Returns appcontext
	 *
	 * @param request
	 * @return
	 */
	public static Map getAppContext(HttpServletRequest request) {
		HashMap context = new HashMap();
		Boolean isDesigner = ParameterAccessor.isDesigner();
		context.put("org.eclipse.birt.data.engine.dataset.cache.option", //$NON-NLS-1$
				isDesigner);
		context.put(EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST, request);

		// Client DPI setting
		context.put(EngineConstants.APPCONTEXT_CHART_RESOLUTION, ParameterAccessor.getDpi(request));

		// Max cube fetch levels
		// int maxCubeRowLevels = ParameterAccessor.getMaxCubeRowLevels( request
		// );
		// if ( maxCubeRowLevels >= 0 )
		// context.put( DataEngine.CUBECUSROR_FETCH_LIMIT_ON_ROW_EDGE,
		// Integer.valueOf( maxCubeRowLevels ) );
		//
		// int maxCubeColumnLevels = ParameterAccessor.getMaxCubeColumnLevels(
		// request );
		// if ( maxCubeColumnLevels >= 0 )
		// context.put( DataEngine.CUBECURSOR_FETCH_LIMIT_ON_COLUMN_EDGE,
		// Integer.valueOf( maxCubeColumnLevels ) );

		// Cube memory size
		int cubeMemorySize = ParameterAccessor.getCubeMemorySize(request);
		if (cubeMemorySize >= 0) {
			context.put(DataEngine.IN_MEMORY_CUBE_SIZE, Integer.valueOf(cubeMemorySize));
		}

		// add resource path to app context
		context.put(IBirtConstants.APPCONTEXT_BIRT_RESOURCE_PATH, ParameterAccessor.getResourceFolder(request));

		// Push user-defined application context
		ParameterAccessor.pushAppContext(context, request);

		if (isDesigner.booleanValue()) {
			String appContextName = ParameterAccessor.getAppContextName(request);
			getAppContextFromExtension(appContextName, context);
		}

		return context;
	}

	/**
	 * Find toc id from document instance by name
	 *
	 * @param doc
	 * @param name
	 * @param options
	 * @return
	 */
	public static String findTocByName(IReportDocument doc, String name, InputOptions options) {
		if (doc == null || name == null) {
			return null;
		}

		String tocid = null;

		// get locale information
		Locale locale = null;
		TimeZone timeZone = null;
		if (options != null) {
			locale = (Locale) options.getOption(InputOptions.OPT_LOCALE);
			timeZone = (TimeZone) options.getOption(InputOptions.OPT_TIMEZONE);
		}
		if (locale == null) {
			locale = Locale.getDefault();
		}

		// get TOC tree
		ITOCTree tocTree = null;
		if (timeZone != null) {
			tocTree = doc.getTOCTree(DesignChoiceConstants.FORMAT_TYPE_VIEWER, ULocale.forLocale(locale),
					BirtUtility.toICUTimeZone(timeZone));
		} else {
			tocTree = doc.getTOCTree(DesignChoiceConstants.FORMAT_TYPE_VIEWER, ULocale.forLocale(locale));
		}
		if (tocTree == null) {
			return null;
		}

		List tocList = tocTree.findTOCByValue(name);
		if (tocList != null && tocList.size() > 0) {
			tocid = ((TOCNode) tocList.get(0)).getBookmark();
		}

		return tocid;
	}

	/**
	 * Output file content
	 *
	 * @param filePath
	 * @param out
	 * @param isDelete
	 * @exception IOException
	 */
	public static void outputFile(String filePath, OutputStream out, boolean isDelete) throws IOException {
		if (filePath == null) {
			return;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			return;
		}

		FileInputStream in = new FileInputStream(file);
		try (in) {
			byte[] buf = new byte[512];
			int len = 0;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.flush();
		} finally {
			try {
				if (isDelete) {
					file.delete();
				}
			} catch (Exception e) {

			}

		}
	}

	/**
	 * Get current resource system id
	 *
	 * @param request
	 * @return
	 */
	private static String getSystemId(HttpServletRequest request) {
		String systemId = null;
		ViewerAttributeBean attrBean = (ViewerAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		if (attrBean != null) {
			systemId = attrBean.getReportDesignName();
		}

		return systemId;
	}

	/**
	 * Log error message in problem view
	 *
	 * @param systemId
	 * @param message
	 * @param elementId
	 * @param lineNumber
	 */
	private static void error(String systemId, String message, long elementId, int lineNumber) {
		try {
			Class clz = Class.forName("org.eclipse.birt.report.viewer.utilities.MarkerUtil"); //$NON-NLS-1$
			if (clz != null) {
				Method mt = clz.getMethod("error", new Class[] { //$NON-NLS-1$
						String.class, String.class, long.class, int.class });
				if (mt != null) {
					mt.invoke(null,
							new Object[] { systemId, message, Long.valueOf(elementId), Integer.valueOf(lineNumber) });
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Log engine error message in problem view
	 *
	 * @param request
	 * @param moduleHandle
	 * @param errors
	 */
	public static void error(HttpServletRequest request, List errors) {
		String systemId = getSystemId(request);
		if (systemId == null) {
			return;
		}

		String isCleared = (String) request.getAttribute(VIEWER_MARKER_CLEARED);
		if (isCleared == null) {
			// clear the errors
			clearErrors(systemId);
			request.setAttribute(VIEWER_MARKER_CLEARED, "true"); //$NON-NLS-1$
		}

		// no error
		if (errors == null || errors.size() <= 0) {
			return;
		}

		Iterator it = errors.iterator();
		while (it.hasNext()) {
			Exception e = (Exception) it.next();
			if (e != null) {
				int lineno = IBirtConstants.UNKNOWN_POSITION;
				long elementId = 0;

				// If EngineException ,get the lineno
				if (e instanceof EngineException) {
					try {
						Map options = getModuleOptions(request);
						options.put(IModuleOption.MARK_LINE_NUMBER_KEY, Boolean.TRUE);
						IReportRunnable reportRunnable = BirtUtility.getRunnableFromDesignFile(request, systemId,
								options);

						if (reportRunnable != null) {
							ModuleHandle moduleHandle = reportRunnable.getDesignHandle().getModuleHandle();
							elementId = ((EngineException) e).getElementID();
							Object obj = moduleHandle.getElementByID(elementId);
							lineno = moduleHandle.getLineNo(obj);
						}
					} catch (Exception err) {
					}
				}

				error(systemId, getDetailMessage(e), elementId, lineno);
			}
		}
	}

	/**
	 * clear all errors related current resource in problem view
	 *
	 * @param systemId
	 */
	private static void clearErrors(String systemId) {
		try {
			Class clz = Class.forName("org.eclipse.birt.report.viewer.utilities.MarkerUtil"); //$NON-NLS-1$
			if (clz != null) {
				Method mt = clz.getMethod("clear", new Class[] { //$NON-NLS-1$
						String.class });
				if (mt != null) {
					mt.invoke(null, new Object[] { systemId });
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Returns the application classloader
	 *
	 * @return ClassLoader
	 */
	public static ClassLoader getAppClassLoader() {
		ClassLoader classLoader = null;

		try {
			Class clz = Class.forName("org.eclipse.birt.report.viewer.utilities.WebViewer"); //$NON-NLS-1$
			if (clz != null) {
				Method mt = clz.getMethod("getAppClassLoader", new Class[] { //$NON-NLS-1$
				});
				if (mt != null) {
					classLoader = (ClassLoader) mt.invoke(null, new Object[] {});
				}
			}
		} catch (Exception e) {
		}

		return classLoader;
	}

	/**
	 * @return Returns the user specified app context
	 */
	public static Map getAppContext(Map context) {
		String appContextName = null;

		try {
			Class clz = Class.forName("org.eclipse.birt.report.viewer.utilities.WebViewer"); //$NON-NLS-1$
			if (clz != null) {
				Method mt = clz.getMethod("getAppContextName", (Class[]) null); //$NON-NLS-1$
				if (mt != null) {
					appContextName = (String) mt.invoke(null, (Object[]) null);
				}
			}
		} catch (Exception e) {
		}

		if (appContextName != null) {
			return getAppContextFromExtension(appContextName, context);
		}

		return context;
	}

	/**
	 * Get the appcontext from extension
	 *
	 * @param appContextName
	 * @param context
	 * @return
	 */
	private static Map getAppContextFromExtension(String appContextName, Map context) {
		try {
			Class clz = Class.forName("org.eclipse.birt.report.viewer.utilities.AppContextUtil"); //$NON-NLS-1$
			if (clz != null) {
				Method mt = clz.getMethod("getAppContext", new Class[] { //$NON-NLS-1$
						String.class, Map.class });
				if (mt != null) {
					context = (Map) mt.invoke(null, new Object[] { appContextName, context });
				}
			}
		} catch (Exception e) {
		}

		return context;
	}

	/**
	 * Converts a Java time zone to an ICU time zone.
	 *
	 * @param timeZone Java time zone
	 * @return ICU time zone
	 */
	public static com.ibm.icu.util.TimeZone toICUTimeZone(java.util.TimeZone timeZone) {
		if (timeZone != null) {
			return com.ibm.icu.util.TimeZone.getTimeZone(timeZone.getID());
		} else {
			return null;
		}
	}
}
