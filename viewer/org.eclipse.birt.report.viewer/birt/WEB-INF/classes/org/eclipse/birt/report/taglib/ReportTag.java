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

package org.eclipse.birt.report.taglib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.session.IViewingSession;
import org.eclipse.birt.report.session.ViewingSessionUtil;
import org.eclipse.birt.report.taglib.component.ParameterField;
import org.eclipse.birt.report.taglib.util.BirtTagUtil;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * This tag is used to preview report content fast. This tag will output report
 * to browser directly.
 * 
 */
public class ReportTag extends AbstractViewerTag {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5017824486972742042L;

	/**
	 * Report output format
	 */
	private String outputFormat;

	/**
	 * Emitter id.
	 */
	private String emitterId;

	/**
	 * Flag whether the report service has been initialized or not.
	 */
	private boolean reportServiceInitialized;

	/**
	 * Viewer Report Design Handle
	 */
	private IViewerReportDesignHandle reportDesignHandle;

	/**
	 * Input Options information
	 */
	private InputOptions options;

	/**
	 * process tag function
	 * 
	 * @see org.eclipse.birt.report.taglib.AbstractBaseTag#__process()
	 */
	public void __process() throws Exception {
		boolean isIFrame = true;

		reportServiceInitialized = false;

		// Set DIV as report container
		if (CONTAINER_DIV.equalsIgnoreCase(viewer.getReportContainer())) {
			isIFrame = false;
		}

		// read output format
		outputFormat = BirtTagUtil.getFormat(viewer.getFormat());

		emitterId = viewer.getEmitterId();
		if (emitterId != null && !"".equals(emitterId)) //$NON-NLS-1$
		{
			initializeReportService();
			String emitterFormat = ParameterAccessor.getEmitterFormat(emitterId);
			if (emitterFormat != null) {
				outputFormat = emitterFormat;
			}
		} else {
			emitterId = null;
		}

		// if output format isn't html, force to use IFrame as report container.
		if (!outputFormat.equalsIgnoreCase(ParameterAccessor.PARAM_FORMAT_HTML)) {
			isIFrame = true;
		}

		if (isIFrame) {
			__processWithIFrame();
		} else {
			__processWithDiv();
		}
	}

	/**
	 * @throws BirtException
	 * @throws Exception
	 * @throws IOException
	 */
	private void __processWithDiv() throws BirtException, Exception, IOException {
		if (!reportServiceInitialized) {
			initializeReportService();
		}

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		IViewingSession session = ViewingSessionUtil.createSession(request);
		session.lock();
		try {
			// Create Input Options
			this.options = new InputOptions();
			options.setOption(InputOptions.OPT_REQUEST, request);
			options.setOption(InputOptions.OPT_LOCALE, this.locale);
			options.setOption(InputOptions.OPT_TIMEZONE, this.timeZone);
			options.setOption(InputOptions.OPT_RTL, Boolean.valueOf(viewer.getRtl()));
			options.setOption(InputOptions.OPT_IS_MASTER_PAGE_CONTENT, Boolean.valueOf(viewer.getAllowMasterPage()));
			options.setOption(InputOptions.OPT_SVG_FLAG, Boolean.valueOf(viewer.getSvg()));
			options.setOption(InputOptions.OPT_FORMAT, outputFormat);
			options.setOption(InputOptions.OPT_EMITTER_ID, emitterId);
			options.setOption(InputOptions.OPT_IS_DESIGNER, Boolean.valueOf(false));
			options.setOption(InputOptions.OPT_SERVLET_PATH, IBirtConstants.SERVLET_PATH_PREVIEW);
			options.setOption(InputOptions.OPT_PAGE_OVERFLOW, viewer.getPageOverflow());

			// get report design handle
			reportDesignHandle = BirtTagUtil.getDesignHandle(request, viewer);

			if (viewer.isHostPage()) {
				// if set isHostPage is true, output report directly
				HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
				__handleOutputReport(response.getOutputStream(), session);
			} else {

				// output to byte array
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				__handleOutputReport(out, session);
				String content = out.toString();

				JspWriter writer = pageContext.getOut();

				// write style
				writer.write(__handleStyle(content));

				// write script
				writer.write(__handleScript(content));

				// use <div> to control report content display
				writer.write("<div id='" + viewer.getId() + "'" //$NON-NLS-1$ //$NON-NLS-2$
						+ __handleDivAppearance() + ">\n"); //$NON-NLS-1$
				writer.write("<div class='" + __handleBodyStyle(content) //$NON-NLS-1$
						+ "'>\n"); //$NON-NLS-1$
				writer.write(__handleBody(content) + "\n"); //$NON-NLS-1$
				writer.write("</div>\n"); //$NON-NLS-1$
				writer.write("</div>\n"); //$NON-NLS-1$
			}
		} finally {
			session.unlock();
		}
	}

	/**
	 * Initializes the report service, if necessary.
	 * 
	 * @throws BirtException
	 */
	private void initializeReportService() throws BirtException {
		// initialize engine context
		if (!reportServiceInitialized) {
			BirtReportServiceFactory.getReportService().setContext(pageContext.getServletContext(), null);
			reportServiceInitialized = true;
		}
	}

	/**
	 * Process report generation with IFrame
	 * 
	 * @throws Exception
	 */
	private void __processWithIFrame() throws Exception {
		if (viewer.isHostPage()) {
			__handleIFrame(viewer.createURI(IBirtConstants.VIEWER_PREVIEW, null), null);
		} else {
			__handleIFrame(viewer.createURI(IBirtConstants.VIEWER_PREVIEW, null), viewer.getId());
		}
	}

	/**
	 * DIV Appearance style
	 * 
	 * @return
	 */
	protected String __handleDivAppearance() {
		String style = " style='"; //$NON-NLS-1$

		// position
		if (viewer.getPosition() != null)
			style += "position:" + viewer.getPosition() + ";"; //$NON-NLS-1$//$NON-NLS-2$

		// height
		if (viewer.getHeight() >= 0)
			style += "height:" + viewer.getHeight() + "px;"; //$NON-NLS-1$//$NON-NLS-2$

		// width
		if (viewer.getWidth() >= 0)
			style += "width:" + viewer.getWidth() + "px;"; //$NON-NLS-1$//$NON-NLS-2$

		// top
		if (viewer.getTop() != null)
			style += "top:" + viewer.getTop() + "px;"; //$NON-NLS-1$//$NON-NLS-2$

		// left
		if (viewer.getLeft() != null)
			style = style + "left:" + viewer.getLeft() + "px;"; //$NON-NLS-1$//$NON-NLS-2$

		// scroll
		if (SCROLLING_YES.equalsIgnoreCase(viewer.getScrolling())) {
			style = style + "overflow:scroll"; //$NON-NLS-1$
		} else if (SCROLLING_AUTO.equalsIgnoreCase(viewer.getScrolling())) {
			style = style + "overflow:auto"; //$NON-NLS-1$
		}

		// style
		if (viewer.getStyle() != null)
			style += viewer.getStyle() + ";"; //$NON-NLS-1$

		style += "' "; //$NON-NLS-1$

		return style;
	}

	/**
	 * Handle style content
	 * 
	 * @param content
	 * @param Exception
	 * @return
	 */
	protected String __handleStyle(String content) throws Exception {
		String style = BLANK_STRING;

		if (content == null)
			return style;

		// parse style content
		Pattern p = Pattern.compile("<\\s*style[^\\>]*\\>", //$NON-NLS-1$
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		while (m.find()) {
			int start = m.end();
			int end = content.toLowerCase().indexOf("</style>", start); //$NON-NLS-1$
			style = style + content.substring(start + 1, end) + "\n"; //$NON-NLS-1$
		}

		// replace the style section with id
		style = style.replaceAll(".style", ".style" + viewer.getId()); //$NON-NLS-1$//$NON-NLS-2$
		style = "<style type=\"text/css\">\n" + style + "\n</style>\n"; //$NON-NLS-1$ //$NON-NLS-2$

		return style;
	}

	/**
	 * Returns body style content
	 * 
	 * @param content
	 * @return
	 */
	protected String __handleBodyStyle(String content) {
		String bodyStyleId = BLANK_STRING;

		if (content == null)
			return bodyStyleId;

		Pattern p = Pattern.compile("<\\s*body([^\\>]*)\\>", //$NON-NLS-1$
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		if (m.find()) {
			for (int i = 1; i < m.groupCount() + 1; i++) {
				String group = m.group(i);
				if (group == null)
					continue;

				Pattern pl = Pattern.compile("class\\s*=\\s*\"([^\"]+)\"", //$NON-NLS-1$
						Pattern.CASE_INSENSITIVE);
				Matcher ml = pl.matcher(group.trim());
				if (ml.find()) {
					// find body style id
					bodyStyleId = ml.group(1).trim();
					break;
				}
			}
		}

		bodyStyleId = bodyStyleId.replaceAll("style", "style" //$NON-NLS-1$ //$NON-NLS-2$
				+ viewer.getId());

		return bodyStyleId;
	}

	/**
	 * Handle script content
	 * 
	 * @param content
	 * @return
	 */
	protected String __handleScript(String content) {
		String script = BLANK_STRING;

		if (content == null)
			return script;

		// get head content
		String head = __handleHead(content);
		if (head == null)
			return script;

		// clear the comment fragments
		Pattern p = Pattern.compile("<\\s*!--"); //$NON-NLS-1$
		Matcher m = p.matcher(head);
		while (m.find()) {
			int start = m.start();
			int end = head.indexOf("-->", start); //$NON-NLS-1$
			if (end > 0) {
				String preTemp = head.substring(0, start);
				String lastTemp = head.substring(end + 3);
				head = preTemp + lastTemp;
			}
		}

		// parse the script fragments
		p = Pattern.compile("<\\s*script[^\\>]*\\>", //$NON-NLS-1$
				Pattern.CASE_INSENSITIVE);
		m = p.matcher(head);
		while (m.find()) {
			int start = m.start();
			int end = head.toLowerCase().indexOf("</script>", start); //$NON-NLS-1$
			if (end > 0)
				script = script + head.substring(start, end + 9) + "\n"; //$NON-NLS-1$
		}

		return script;
	}

	/**
	 * Handle head content
	 * 
	 * @param content
	 * @return
	 */
	protected String __handleHead(String content) {
		if (content == null)
			return BLANK_STRING;

		String head = BLANK_STRING;

		try {
			Pattern p = Pattern.compile("<\\s*head[^\\>]*\\>", //$NON-NLS-1$
					Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(content);
			if (m.find()) {
				int start = m.end();
				int end = content.toLowerCase().indexOf("</head>"); //$NON-NLS-1$
				head = content.substring(start + 1, end);
			}
		} catch (Exception e) {
		}

		return head;
	}

	/**
	 * Handle body content
	 * 
	 * @param content
	 * @return
	 */
	protected String __handleBody(String content) {
		String body = content;

		if (content == null)
			return BLANK_STRING;

		try {
			Pattern p = Pattern.compile("<\\s*body[^\\>]*\\>", //$NON-NLS-1$
					Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(content);
			if (m.find()) {
				int start = m.end();
				int end = content.toLowerCase().indexOf("</body>"); //$NON-NLS-1$
				body = content.substring(start + 1, end);
			}
		} catch (Exception e) {
			body = content;
		}

		// handle style class
		body = body.replaceAll("class=\"style", "class=\"style" //$NON-NLS-1$ //$NON-NLS-2$
				+ viewer.getId());

		return body;
	}

	/**
	 * handle generate report content
	 * 
	 * @param out
	 * @throws Exception
	 */
	protected void __handleOutputReport(OutputStream out, IViewingSession session) throws Exception {
		if (viewer.isDocumentInUrl()) {
			__renderDocument(out);
		} else {
			__renderReport(out, session);
		}
	}

	/**
	 * Render context from document file
	 * 
	 * @param out
	 * @throws Exception
	 */
	private void __renderDocument(OutputStream out) throws Exception {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		// Get document file path
		String documentFile = ParameterAccessor.getReportDocument(request, viewer.getReportDocument(), false);
		IReportDocument doc = ReportEngineService.getInstance().openReportDocument(null, documentFile,
				BirtTagUtil.getModuleOptions(viewer));
		try {
			String realReportletId = viewer.getReportletId();
			if (realReportletId == null) {
				if (viewer.getBookmark() != null && "true".equalsIgnoreCase(viewer.getIsReportlet())) //$NON-NLS-1$
				{
					realReportletId = viewer.getBookmark();
				}
			}

			if (realReportletId != null) {
				// Render the reportlet
				ReportEngineService.getInstance().renderReportlet(out, doc, this.options, realReportletId, null);
			} else {
				// Render the report document file
				ReportEngineService.getInstance().renderReport(out, doc, viewer.getPageNum(), viewer.getPageRange(),
						this.options, null);
			}
		} finally {
			if (doc != null)
				doc.close();
		}
	}

	/**
	 * Render report content from design file
	 * 
	 * @param out
	 * @throws Exception
	 */
	private void __renderReport(OutputStream out, IViewingSession session) throws Exception {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		IReportRunnable runnable = (IReportRunnable) this.reportDesignHandle.getDesignObject();

		// Get report title
		String reportTitle = ParameterAccessor.htmlDecode(BirtUtility.getTitleFromDesign(reportDesignHandle));

		// Prepare the report parameters
		Map params = __handleParameters(reportDesignHandle, null);

		// Get parameter definition list
		Collection parameterDefList = getReportService().getParameterDefinitions(reportDesignHandle, options, false);

		// Prepare the display text of report parameters
		Map displayTexts = BirtUtility.getDisplayTexts(parameterDefList, null,
				(HttpServletRequest) pageContext.getRequest());

		// Render report
		String realReportletId = viewer.getReportletId();
		if (realReportletId == null) {
			if (viewer.getBookmark() != null && "true".equalsIgnoreCase(viewer.getIsReportlet())) //$NON-NLS-1$
			{
				realReportletId = viewer.getBookmark();
			}
		}

		if (realReportletId != null) {
			Locale locale = (Locale) this.options.getOption(InputOptions.OPT_LOCALE);
			TimeZone timeZone = (TimeZone) this.options.getOption(InputOptions.OPT_TIMEZONE);

			// preview reportlet
			String documentName = session.getCachedReportDocument(viewer.getReportDesign(), viewer.getId());
			List<Exception> errors = ReportEngineService.getInstance().runReport(request, runnable, documentName,
					locale, timeZone, params, displayTexts, Integer.valueOf(viewer.getMaxRowsOfRecords()));

			if (errors != null && !errors.isEmpty()) {
				for (Iterator<Exception> i = errors.iterator(); i.hasNext();) {
					i.next().printStackTrace();
				}
			}

			// Render the reportlet
			IReportDocument doc = ReportEngineService.getInstance().openReportDocument(null, documentName,
					BirtTagUtil.getModuleOptions(viewer));

			ReportEngineService.getInstance().renderReportlet(out, doc, this.options, realReportletId, null);
		} else {
			// preview report
			ReportEngineService.getInstance().runAndRenderReport(runnable, out, this.options, params, Boolean.TRUE,
					null, null, displayTexts, reportTitle, Integer.valueOf(viewer.getMaxRowsOfRecords()));
		}
	}

	/**
	 * Handle report parameters
	 * 
	 * @param reportDesignHandle
	 * @param params
	 * @return
	 */
	protected Map __handleParameters(IViewerReportDesignHandle reportDesignHandle, Map params) throws Exception {
		if (params == null)
			params = new HashMap();

		// get report parameter handle list
		List parameterList = BirtUtility.getParameterList(reportDesignHandle);
		if (parameterList == null)
			return params;

		// get parameter map
		Map paramMap = viewer.getParameters();

		Iterator it = parameterList.iterator();
		while (it.hasNext()) {
			Object handle = it.next();
			if (handle instanceof ScalarParameterHandle) {
				ScalarParameterHandle parameterHandle = (ScalarParameterHandle) handle;
				boolean isMultiValue = DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE
						.equalsIgnoreCase(parameterHandle.getParamType());
				String paramName = parameterHandle.getName();
				ParameterField field = (ParameterField) paramMap.get(paramName);
				Object paramValue;
				Object paramObj;
				if (field != null) {
					paramObj = field.getValue();
					if (paramObj == null) {
						// if set null parameter value
						params.put(paramName, null);
						continue;
					}

					// if set parameter object
					if (!(paramObj instanceof String)) {
						if (isMultiValue) {
							Object[] values;
							if (paramObj instanceof Object[])
								values = (Object[]) paramObj;
							else
								values = new Object[] { paramObj };

							for (int i = 0; i < values.length; i++) {
								if (values[i] != null && values[i] instanceof String)
									values[i] = getParameterValue(parameterHandle, field, (String) values[i]);
							}

							params.put(paramName, values);
						} else {
							params.put(paramName, paramObj);
						}
					} else {
						// handle parameter using String value
						paramValue = getParameterValue(parameterHandle, field, (String) paramObj);

						if (isMultiValue)
							params.put(paramName, new Object[] { paramValue });
						else
							params.put(paramName, paramValue);
					}
				} else {
					// set default value as parameter value;
					paramObj = getReportService().getParameterDefaultValue(reportDesignHandle, paramName, this.options);

					params.put(paramName, paramObj);
				}
			}
		}

		return params;
	}

	/**
	 * parse parameter value by string value
	 * 
	 * @param handle
	 * @param field
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private Object getParameterValue(ScalarParameterHandle handle, ParameterField field, String value)
			throws Exception {
		// get parameter data type
		String dataType = handle.getDataType();

		// if String type, return String value
		if (DesignChoiceConstants.PARAM_TYPE_STRING.equalsIgnoreCase(dataType)) {
			return value;
		}

		// convert parameter to object
		String pattern = field.getPattern();
		if (pattern == null || pattern.length() <= 0) {
			pattern = handle.getPattern();
		}

		return DataUtil.validate(handle.getName(), handle.getDataType(), pattern, value, this.locale, this.timeZone,
				field.isLocale());
	}

	/**
	 * Returns Report Service Object
	 * 
	 * @return
	 */
	protected IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}

	/**
	 * @param reportContainer the reportContainer to set
	 */
	public void setReportContainer(String reportContainer) {
		viewer.setReportContainer(reportContainer);
	}
}
