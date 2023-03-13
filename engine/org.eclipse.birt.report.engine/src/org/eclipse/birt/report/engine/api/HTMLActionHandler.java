/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;

import com.ibm.icu.math.BigDecimal;

/**
 * Defines a default action handler for HTML output format
 */
public class HTMLActionHandler implements IHTMLActionHandler {

	/** logger */
	protected Logger log = Logger.getLogger(HTMLActionHandler.class.getName());

	/**
	 * Get URL of the action.
	 *
	 * @param actionDefn
	 * @param context
	 * @return URL
	 */
	@Override
	public String getURL(IAction actionDefn, IReportContext context) {
		Object renderContext = getRenderContext(context);
		return getURL(actionDefn, renderContext);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api2.IHTMLActionHandler#getURL(org.eclipse.
	 * birt.report.engine.api2.IAction, java.lang.Object)
	 */
	@Override
	public String getURL(IAction actionDefn, Object context) {
		if (actionDefn == null) {
			return null;
		}
		String url = null;
		switch (actionDefn.getType()) {
		case IAction.ACTION_BOOKMARK:
			if (actionDefn.getActionString() != null) {
				url = "#" + actionDefn.getActionString();
			}
			break;
		case IAction.ACTION_HYPERLINK:
			url = actionDefn.getActionString();
			break;
		case IAction.ACTION_DRILLTHROUGH:
			url = buildDrillAction(actionDefn, context);
			break;
		default:
			return null;
		}
		return url;
	}

	/**
	 * builds URL for drillthrough action
	 *
	 * @param action  instance of the IAction instance
	 * @param context the context for building the action string
	 * @return a URL
	 */
	protected String buildDrillAction(IAction action, Object context) {
		String baseURL = null;
		if (context != null) {
			if (context instanceof HTMLRenderContext) {
				baseURL = ((HTMLRenderContext) context).getBaseURL();
			}
			if (context instanceof PDFRenderContext) {
				baseURL = ((PDFRenderContext) context).getBaseURL();
			}
		}

		if (baseURL == null) {
			baseURL = "run";
		}
		StringBuilder link = new StringBuilder();
		String reportName = getReportName(action);

		if (reportName != null && !reportName.equals("")) //$NON-NLS-1$
		{
			String format = action.getFormat();
			if (!"html".equalsIgnoreCase(format)) {
				link.append(baseURL.replaceFirst("frameset", "run")); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				link.append(baseURL);
			}

			link.append(reportName.toLowerCase().endsWith(".rptdocument") ? "?__document=" : "?__report="); //$NON-NLS-1$

			try {
				link.append(URLEncoder.encode(reportName, "UTF-8")); //$NON-NLS-1$
			} catch (UnsupportedEncodingException e1) {
				// It should not happen. Does nothing
			}

			// add format support
			if (format != null && format.length() > 0) {
				link.append("&__format=" + format); //$NON-NLS-1$
			}

			// Adds the parameters
			if (action.getParameterBindings() != null) {
				Iterator paramsIte = action.getParameterBindings().entrySet().iterator();
				while (paramsIte.hasNext()) {
					Map.Entry entry = (Map.Entry) paramsIte.next();
					try {
						String key = (String) entry.getKey();
						Object valueObj = entry.getValue();
						if (valueObj != null) {
							Object[] values;
							if (valueObj instanceof List) {
								valueObj = ((List) valueObj).toArray();
								values = (Object[]) valueObj;
							} else {
								values = new Object[1];
								values[0] = valueObj;
							}

							for (int i = 0; i < values.length; i++) {
								String value = getDisplayValue(values[i]);

								if (value != null) {
									link.append("&" + URLEncoder.encode(key, "UTF-8") + "="
											+ URLEncoder.encode(value, "UTF-8"));
								}
							}
						}
					} catch (UnsupportedEncodingException e) {
						// Does nothing
					}
				}
			}

			// Adding overwrite.
			link.append("&__overwrite=true"); //$NON-NLS-1$

			// The search rules are not supported yet.
			if ( /*
					 * !"pdf".equalsIgnoreCase( format ) &&
					 */action.getBookmark() != null) {

				try {
					// In RUN mode, don't support bookmark as parameter
					if (baseURL.lastIndexOf("run") > 0) {
						link.append("#"); //$NON-NLS-1$
					} else {
						link.append("&__bookmark="); //$NON-NLS-1$
					}

					link.append(URLEncoder.encode(action.getBookmark(), "UTF-8")); //$NON-NLS-1$
				} catch (UnsupportedEncodingException e) {
					// Does nothing
				}
			}
		}

		return link.toString();
	}

	/**
	 * Append report design name into a StringBuffer.
	 *
	 * @param buffer
	 * @param reportName
	 */
	protected void appendReportDesignName(StringBuffer buffer, String reportName) {
		buffer.append("?__report="); //$NON-NLS-1$
		try {
			buffer.append(URLEncoder.encode(reportName, "UTF-8")); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e1) {
			// It should not happen. Does nothing
		}
	}

	/**
	 * Append format.
	 *
	 * @param buffer
	 * @param format
	 */
	protected void appendFormat(StringBuffer buffer, String format) {
		if (format != null && format.length() > 0) {
			buffer.append("&__format=" + format);//$NON-NLS-1$
		}
	}

	/**
	 * Append parameter.
	 *
	 * @param buffer
	 * @param key
	 * @param valueObj
	 */
	protected void appendParamter(StringBuffer buffer, String key, Object valueObj) {
		if (valueObj != null) {
			try {
				key = URLEncoder.encode(key, "UTF-8");
				String value = valueObj.toString();
				value = URLEncoder.encode(value, "UTF-8");
				buffer.append("&");
				buffer.append(key);
				buffer.append("=");
				buffer.append(value);
			} catch (UnsupportedEncodingException e) {
				// Does nothing
			}
		}
	}

	/**
	 * Append bookmark as parameter .
	 *
	 * @param buffer
	 * @param bookmark
	 */
	protected void appendBookmarkAsParamter(StringBuffer buffer, String bookmark) {
		try {
			if (bookmark != null && bookmark.length() != 0) {
				bookmark = URLEncoder.encode(bookmark, "UTF-8");
				buffer.append("&__bookmark=");//$NON-NLS-1$
				buffer.append(bookmark);
			}
		} catch (UnsupportedEncodingException e) {

		}
	}

	/**
	 * Append bookmark.
	 *
	 * @param buffer
	 * @param bookmark
	 */
	protected void appendBookmark(StringBuffer buffer, String bookmark) {
		try {
			if (bookmark != null && bookmark.length() != 0) {
				bookmark = URLEncoder.encode(bookmark, "UTF-8");
				buffer.append("#");//$NON-NLS-1$
				buffer.append(bookmark);
			}
		} catch (UnsupportedEncodingException e) {
		}
	}

	/**
	 * Get report name.
	 *
	 * @param action
	 * @return
	 */
	String getReportName(IAction action) {
		String systemId = action.getSystemId();
		String reportName = action.getReportName();
		if (systemId == null) {
			return reportName;
		}
		// if the reportName is an URL, use it directly
		try {
			URL url = new URL(reportName);
			if ("file".equals(url.getProtocol())) {
				return url.getFile();
			}
			return url.toExternalForm();
		} catch (MalformedURLException ex) {
		}
		// if the system id is the URL, merget the report name with it
		try {
			URL root = new URL(systemId);
			URL url = new URL(root, reportName);
			if ("file".equals(url.getProtocol())) {
				return url.getFile();
			}
			return url.toExternalForm();
		} catch (MalformedURLException ex) {

		}
		// now the root should be a file and the report name is a file also
		File file = new File(reportName);
		if (file.isAbsolute()) {
			return reportName;
		}

		try {
			URL root = new File(systemId).toURL();
			URL url = new URL(root, reportName);
			assert "file".equals(url.getProtocol());
			return url.getFile();
		} catch (MalformedURLException ex) {
		}
		return reportName;
	}

	/**
	 * Get render context.
	 *
	 * @param context
	 * @return
	 */
	protected Object getRenderContext(IReportContext context) {
		if (context == null) {
			return null;
		}
		Map appContext = context.getAppContext();
		if (appContext != null) {
			String renderContextKey = EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT;
			String format = context.getOutputFormat();
			if ("pdf".equalsIgnoreCase(format)) {
				renderContextKey = EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT;
			}
			return appContext.get(renderContextKey);
		}
		return null;
	}

	/**
	 * Get display value.
	 *
	 * @param value
	 * @return
	 */
	String getDisplayValue(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Float || value instanceof Double || value instanceof BigDecimal) {
			return value.toString();
		}
		return ParameterValidationUtil.getDisplayValue(value);
	}

}
