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

package org.eclipse.birt.report.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IDataAction;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;
import org.eclipse.birt.report.utility.UrlUtility;

/**
 * HTML action handler for url generation.
 */
class ViewerHTMLActionHandler extends HTMLActionHandler {

	/**
	 * Logger for this handler.
	 */
	private Logger log = Logger.getLogger(ViewerHTMLActionHandler.class.getName());

	/**
	 * Document instance.
	 */

	protected IReportDocument document = null;

	/**
	 * Locale of the requester.
	 */

	protected Locale locale = null;

	/**
	 * Time zone
	 */
	protected TimeZone timeZone = null;

	/**
	 * Page number of the action requester.
	 */

	protected long page = -1;

	/**
	 * if the page is embedded, the bookmark should always be a url to submit.
	 */
	protected boolean isEmbeddable = false;

	/**
	 * RTL option setting by the command line or URL parameter.
	 */

	protected boolean isRtl = false;

	/**
	 * if wanna use the master page, then set it to true.
	 */

	protected Boolean isMasterPageContent = null;

	/**
	 * host format
	 */
	protected String hostFormat = null;

	/**
	 * resource folder setting
	 */
	protected String resourceFolder = null;

	/**
	 * SVG option setting by URL parameter.
	 */
	protected Boolean svg = null;

	/**
	 * indicates whether under designer mode
	 */
	protected String isDesigner = null;

	/**
	 * page overflow mode
	 */
	protected int pageOverflow = 0;

	/**
	 * BIRT viewing session id
	 */
	protected String viewingSessionId = null;

	/**
	 * Constructor.
	 */
	public ViewerHTMLActionHandler() {
	}

	/**
	 * Constructor. This is for renderTask.
	 *
	 * @param document
	 * @param page
	 * @param locale
	 * @param isEmbeddable
	 * @param isRtl
	 * @param isMasterPageContent
	 * @param format
	 * @param svg
	 * @param isDesigner
	 */

	public ViewerHTMLActionHandler(IReportDocument document, long page, Locale locale, TimeZone timeZone,
			boolean isEmbeddable, boolean isRtl, boolean isMasterPageContent, String format, Boolean svg,
			String isDesigner) {
		this.document = document;
		this.page = page;
		this.locale = locale;
		this.timeZone = timeZone;
		this.isEmbeddable = isEmbeddable;
		this.isRtl = isRtl;
		// leave unset if it's default value
		this.isMasterPageContent = isMasterPageContent ? null : Boolean.FALSE;
		this.hostFormat = format;
		// leave unset if it's default value
		this.svg = svg ? Boolean.TRUE : null;
		this.isDesigner = isDesigner;
	}

	/**
	 * Constructor. This is for runAndRender task.
	 *
	 * @param locale
	 * @param isEmbeddable
	 * @param isRtl
	 * @param isMasterPageContent
	 * @param format
	 * @param svg
	 * @param isDesigner
	 */

	public ViewerHTMLActionHandler(Locale locale, TimeZone timeZone, boolean isRtl, boolean isMasterPageContent,
			String format, Boolean svg, String isDesigner) {
		this.locale = locale;
		this.timeZone = timeZone;
		this.isRtl = isRtl;
		// leave unset if it's default value
		this.isMasterPageContent = isMasterPageContent ? null : Boolean.FALSE;
		this.hostFormat = format;
		// leave unset if it's default value
		this.svg = svg ? Boolean.TRUE : null;
		this.isDesigner = isDesigner;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.HTMLActionHandler#getURL(org.eclipse
	 * .birt.report.engine.api.IAction,
	 * org.eclipse.birt.report.engine.api.script.IReportContext)
	 */

	@Override
	public String getURL(IAction actionDefn, IReportContext context) {
		if (actionDefn == null) {
			return null;
		}

		switch (actionDefn.getType()) {
		case IAction.ACTION_BOOKMARK: {
			return buildBookmarkAction(actionDefn, context);
		}
		case IAction.ACTION_HYPERLINK: {
			return buildHyperlink(actionDefn, context);
		}
		case IAction.ACTION_DRILLTHROUGH: {
			return buildDrillAction(actionDefn, context);
		}
		case IDataAction.ACTION_DATA: {
			return buildDataAction((IDataAction) actionDefn, context);
		}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.HTMLActionHandler#getURL(org.eclipse
	 * .birt.report.engine.api.IAction, java.lang.Object)
	 */
	@Override
	public String getURL(IAction actionDefn, Object context) {
		if (actionDefn == null) {
			return null;
		}
		if (context instanceof IReportContext) {
			return getURL(actionDefn, (IReportContext) context);
		}

		throw new IllegalArgumentException("The context is of wrong type."); //$NON-NLS-1$
	}

	/**
	 * Build URI
	 *
	 * @param action
	 * @param context
	 * @return
	 */
	private String buildHyperlink(IAction action, IReportContext context) {
		IReportRunnable runnable = context.getReportRunnable();
		String actionURL = action.getActionString();
		if (runnable != null) {
			ModuleHandle moduleHandle = runnable.getDesignHandle().getModuleHandle();
			URL url = moduleHandle.findResource(actionURL, -1);
			if (url != null) {
				actionURL = url.toString();
			}
		}

		return actionURL;
	}

	/**
	 * Build URL for bookmark.
	 *
	 * @param action
	 * @param context
	 * @return the bookmark url
	 */

	protected String buildBookmarkAction(IAction action, IReportContext context) {
		if (action == null || context == null) {
			return null;
		}

		// Get Base URL
		String baseURL = getBaseUrl(context);

		// Get bookmark
		String bookmark = action.getBookmark();

		if (baseURL.lastIndexOf(IBirtConstants.SERVLET_PATH_FRAMESET) > 0
				|| baseURL.lastIndexOf(IBirtConstants.SERVLET_PATH_RUN) > 0) {
			// In frameset mode, use javascript function to fire Ajax request to
			// link to internal bookmark
			// In run mode, append bookmark at the end of URL
			String func = "catchBookmark('" + ParameterAccessor.htmlEncode(bookmark) + "');"; //$NON-NLS-1$ //$NON-NLS-2$
			return "javascript:try{" + func + "}catch(e){parent." + func + "};"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		// Save the URL String
		StringBuilder link = new StringBuilder();

		boolean realBookmark = false;

		if (this.document != null) {
			long pageNumber = this.document.getPageNumber(action.getBookmark());
			realBookmark = (pageNumber == this.page && !isEmbeddable);
		}

		try {
			bookmark = URLEncoder.encode(bookmark, ParameterAccessor.UTF_8_ENCODE);
		} catch (UnsupportedEncodingException e) {
			// Does nothing
		}

		link.append(baseURL);
		link.append(ParameterAccessor.QUERY_CHAR);

		// if the document is not null, then use it
		if (document != null) {
			link.append(ParameterAccessor.PARAM_REPORT_DOCUMENT);
			link.append(ParameterAccessor.EQUALS_OPERATOR);
			String documentName = document.getName();

			try {
				documentName = URLEncoder.encode(documentName, ParameterAccessor.UTF_8_ENCODE);
			} catch (UnsupportedEncodingException e) {
				// Does nothing
			}
			link.append(documentName);
		} else if (action.getReportName() != null && action.getReportName().length() > 0) {
			link.append(ParameterAccessor.PARAM_REPORT);
			link.append(ParameterAccessor.EQUALS_OPERATOR);
			String reportName = getReportName(context, action);
			try {
				reportName = URLEncoder.encode(reportName, ParameterAccessor.UTF_8_ENCODE);
			} catch (UnsupportedEncodingException e) {
				// do nothing
			}
			link.append(reportName);
		} else {
			// its an iternal bookmark
			return "#" + action.getActionString(); //$NON-NLS-1$
		}

		if (locale != null) {
			link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_LOCALE, locale.toString()));
		}

		if (timeZone != null) {
			link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_TIMEZONE, timeZone.getID()));
		}

		if (isRtl) {
			link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_RTL, String.valueOf(isRtl)));
		}

		if (svg != null) {
			link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_SVG,
					String.valueOf(svg.booleanValue())));
		}

		if (isDesigner != null) {
			link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_DESIGNER,
					String.valueOf(isDesigner)));
		}

		if (pageOverflow > 0) {
			link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_PAGE_OVERFLOW,
					String.valueOf(pageOverflow)));
		}

		// add isMasterPageContent
		if (isMasterPageContent != null) {
			link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_MASTERPAGE,
					String.valueOf(isMasterPageContent)));
		}

		// append resource folder setting
		try {
			if (resourceFolder != null) {
				String res = URLEncoder.encode(resourceFolder, ParameterAccessor.UTF_8_ENCODE);
				link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_RESOURCE_FOLDER, res));
			}
		} catch (UnsupportedEncodingException e) {
		}

		if (realBookmark) {
			link.append("#"); //$NON-NLS-1$
			link.append(bookmark);
		} else {
			link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_BOOKMARK, bookmark));

			// Bookmark is TOC name.
			if (!action.isBookmark()) {
				link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_ISTOC, "true")); //$NON-NLS-1$
			}
		}

		return link.toString();
	}

	/**
	 * Build URL for extract data.
	 *
	 * @param action
	 * @param context
	 * @return the data url
	 */
	protected String buildDataAction(IDataAction action, IReportContext context) {
		if (action == null || context == null) {
			return null;
		}

		boolean encodePaths;
		String actionString = action.getActionString();
		Map params = UrlUtility.extractUriParameters(actionString);
		String anchor = UrlUtility.getAnchor(actionString);

		String encodedPathsString = (String) params.get(ParameterAccessor.PARAM_ENCODED_PATHS);
		encodePaths = (encodedPathsString != null && Boolean.parseBoolean(encodedPathsString));

		// Get Base URL
		String baseURL = getBaseUrl(context);

		// replace the servlet pattern using extract
		baseURL = createBaseURLWithExtractPattern(baseURL);

		// if the document is not null, then use it
		if (document != null) {
			String documentName = document.getName();
			if (encodePaths) {
				documentName = ParameterAccessor.encodeBase64(documentName);
			}
			params.put(ParameterAccessor.PARAM_REPORT_DOCUMENT, documentName);
		} else if (action.getReportName() != null && action.getReportName().length() > 0) {
			String reportName = getReportName(context, action);
			if (encodePaths) {
				reportName = ParameterAccessor.encodeBase64(reportName);
			}
			params.put(ParameterAccessor.PARAM_REPORT, reportName);
		}

		// append extract options
		createURLWithExtractInfo(action, params);

		if (locale != null) {
			params.put(ParameterAccessor.PARAM_LOCALE, locale.toString());
		}

		if (timeZone != null) {
			params.put(ParameterAccessor.PARAM_TIMEZONE, timeZone.getID());
		}

		if (isRtl) {
			params.put(ParameterAccessor.PARAM_RTL, String.valueOf(isRtl));
		}

		// append resource folder setting
		if (resourceFolder != null) {
			if (encodePaths) {
				resourceFolder = ParameterAccessor.encodeBase64(resourceFolder);
			}

			params.put(ParameterAccessor.PARAM_RESOURCE_FOLDER, resourceFolder);
		}

		if (viewingSessionId != null) {
			params.put(ParameterAccessor.PARAM_VIEWING_SESSION_ID, viewingSessionId);
		}

		return UrlUtility.buildUrl(baseURL, params, anchor);
	}

	/**
	 * Returns the base URL from the report context.
	 *
	 * @param context report context
	 * @return base URL
	 */
	private String getBaseUrl(IReportContext context) {
		String baseURL = context.getRenderOption().getBaseURL();
		String servletPath = (String) context.getRenderOption().getOption(IBirtConstants.SERVLET_PATH);

		if (servletPath == null) {
			servletPath = IBirtConstants.SERVLET_PATH_PREVIEW;
		}

		if (baseURL == null) {
			baseURL = servletPath;
		} else {
			baseURL += servletPath;
		}

		return baseURL;
	}

	private String encodeParamValue(String value) {
		try {
			return URLEncoder.encode(value, ParameterAccessor.UTF_8_ENCODE);
		} catch (UnsupportedEncodingException ex) {
			return value;
		}
	}

	/**
	 * builds URL for drillthrough action
	 *
	 * @param action  instance of the IAction instance
	 * @param context the context for building the action string
	 * @return a URL
	 */
	protected String buildDrillAction(IAction action, IReportContext context) {
		if (action == null || context == null) {
			return null;
		}

		String baseURL = getBaseUrl(context);
		StringBuilder link = new StringBuilder();
		String reportName = getReportName(context, action);

		if (reportName != null && !reportName.equals("")) //$NON-NLS-1$
		{
			link.append(baseURL);

			link.append(reportName.toLowerCase().endsWith(".rptdocument") ? "?__document=" : "?__report="); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			try {
				link.append(URLEncoder.encode(reportName, ParameterAccessor.UTF_8_ENCODE));
			} catch (UnsupportedEncodingException e1) {
				// It should not happen. Does nothing
			}

			// add format support
			String format = action.getFormat();
			if (format == null || format.length() == 0) {
				format = hostFormat;
			}
			if (format != null && format.length() > 0) {
				link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_FORMAT, format));
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
							if (valueObj instanceof List) {
								if (((List) valueObj).size() == 1) {
									valueObj = ((List) valueObj).get(0);
								} else {
									valueObj = ((List) valueObj).toArray();
								}
							}

							Object[] values;
							if (valueObj instanceof Object[]) {
								values = (Object[]) valueObj;
							} else {
								values = new Object[1];
								values[0] = valueObj;
							}

							for (int i = 0; i < values.length; i++) {
								// TODO: here need the get the format from the
								// parameter.
								String value = DataUtil.getDisplayValue(values[i], timeZone);

								if (value != null) {
									link.append(ParameterAccessor.getQueryParameterString(
											URLEncoder.encode(key, ParameterAccessor.UTF_8_ENCODE),
											URLEncoder.encode(value, ParameterAccessor.UTF_8_ENCODE)));
								} else {
									// pass NULL value
									link.append(
											ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_ISNULL,
													URLEncoder.encode(key, ParameterAccessor.UTF_8_ENCODE)));
								}
							}
						}
					} catch (UnsupportedEncodingException e) {
						// Does nothing
					}
				}

				// Adding overwrite.
				if (!reportName.toLowerCase().endsWith(IBirtConstants.SUFFIX_REPORT_DOCUMENT)
						&& baseURL.lastIndexOf(IBirtConstants.SERVLET_PATH_FRAMESET) > 0) {
					link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_OVERWRITE,
							String.valueOf(true)));
				}
			}

			if (locale != null) {
				link.append(
						ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_LOCALE, locale.toString()));
			}

			if (timeZone != null) {
				link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_TIMEZONE,
						encodeParamValue(timeZone.getID())));
			}

			if (isRtl) {
				link.append(
						ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_RTL, String.valueOf(isRtl)));
			}

			if (svg != null) {
				link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_SVG,
						String.valueOf(svg.booleanValue())));
			}

			if (isDesigner != null) {
				link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_DESIGNER,
						String.valueOf(isDesigner)));
			}

			if (pageOverflow > 0) {
				link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_PAGE_OVERFLOW,
						String.valueOf(pageOverflow)));
			}

			// add isMasterPageContent
			if (isMasterPageContent != null) {
				link.append(ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_MASTERPAGE,
						String.valueOf(isMasterPageContent)));
			}

			// append resource folder setting
			try {
				if (resourceFolder != null) {
					String res = URLEncoder.encode(resourceFolder, ParameterAccessor.UTF_8_ENCODE);
					link.append(
							ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_RESOURCE_FOLDER, res));
				}
			} catch (UnsupportedEncodingException e) {
			}

			// add bookmark
			String bookmark = action.getBookmark();
			if (bookmark != null) {

				try {
					// In PREVIEW mode or pdf format, don't support bookmark as
					// parameter
					if (baseURL.lastIndexOf(IBirtConstants.SERVLET_PATH_PREVIEW) > 0
							|| IBirtConstants.PDF_RENDER_FORMAT.equalsIgnoreCase(format)) {
						// use TOC to find bookmark, only link to document file
						if (!action.isBookmark() && reportName.toLowerCase().endsWith(".rptdocument")) //$NON-NLS-1$
						{
							InputOptions options = new InputOptions();
							options.setOption(InputOptions.OPT_LOCALE, locale);
							options.setOption(InputOptions.OPT_TIMEZONE, timeZone);
							bookmark = BirtReportServiceFactory.getReportService().findTocByName(reportName, bookmark,
									options);
						}

						if (bookmark != null) {
							link.append("#"); //$NON-NLS-1$
							link.append(URLEncoder.encode(bookmark, ParameterAccessor.UTF_8_ENCODE));
						}
					} else {
						bookmark = URLEncoder.encode(bookmark, ParameterAccessor.UTF_8_ENCODE);
						link.append(
								ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_BOOKMARK, bookmark));

						// Bookmark is TOC name.
						if (!action.isBookmark()) {
							link.append(
									ParameterAccessor.getQueryParameterString(ParameterAccessor.PARAM_ISTOC, "true")); //$NON-NLS-1$
						}
					}

				} catch (UnsupportedEncodingException e) {
					// Does nothing
				}
			}
		}

		return link.toString();
	}

	/**
	 * Gets the effective report path.
	 *
	 * @param context
	 * @param action
	 * @return the effective report path
	 */

	private String getReportName(IReportContext context, IAction action) {
		assert context != null;
		assert action != null;
		String reportName = action.getReportName();

		if (reportName != null) {
			if (reportName.startsWith("/")) //$NON-NLS-1$
			{
				// check if it's a valid logical path that always relative
				// to working folder or an absolute file path
				if (reportName.indexOf(':') == -1 && !new File(reportName).exists()) {
					// this is a logical path
					return reportName;
				}
			}

			IReportRunnable runnable = context.getReportRunnable();

			if (runnable != null) {
				ModuleHandle moduleHandle = runnable.getDesignHandle().getModuleHandle();

				// if WORKING_FOLDER_ACCESS_ONLY is false, return absolute path.
				// else, return the relative path based on working folder.

				if (ParameterAccessor.isWorkingFolderAccessOnly()) {
					String fullPath = URIUtil.resolveAbsolutePath(moduleHandle.getSystemId().toExternalForm(),
							reportName);

					reportName = URIUtil.getRelativePath(ParameterAccessor.workingFolder, fullPath);
				} else {
					URL url = moduleHandle.findResource(reportName, -1);
					if (url != null) {
						if ("file".equals(url.getProtocol())) //$NON-NLS-1$
						{

							try {
								reportName = new File(url.toURI()).getPath();
							} catch (URISyntaxException e) {
								// ignore
							}
						} else {
							reportName = url.toExternalForm();
						}
					}
				}
			}
		}
		return reportName;
	}

	/**
	 * @return the resourceFolder
	 */
	public String getResourceFolder() {
		return resourceFolder;
	}

	/**
	 * @param resourceFolder the resourceFolder to set
	 */
	public void setResourceFolder(String resourceFolder) {
		this.resourceFolder = resourceFolder;
	}

	/**
	 * Replace URL with extract servlet pattern
	 *
	 * @param baseURL
	 * @return
	 */
	private String createBaseURLWithExtractPattern(String baseURL) {
		String url = baseURL;
		// replace servlet pattern to extract path
		while (url.endsWith("/")) //$NON-NLS-1$
		{
			url = url.substring(0, url.length() - 2);
		}

		int index = url.lastIndexOf("/"); //$NON-NLS-1$
		if (index >= 0) {
			url = url.substring(0, index);
		}

		return url + IBirtConstants.SERVLET_PATH_EXTRACT;
	}

	/**
	 * Create the extract URL with options
	 *
	 * @param action
	 * @param link
	 * @return
	 */
	private void createURLWithExtractInfo(IDataAction action, Map params) {
		assert action != null;
		assert params != null;

		// append extract format
		if (action.getDataType() != null) {
			params.put(ParameterAccessor.PARAM_DATA_EXTRACT_FORMAT, action.getDataType());
		}

		// append instance id
		if (action.getInstanceID() != null) {
			params.put(ParameterAccessor.PARAM_INSTANCEID, action.getInstanceID().toUniqueString());
		}

		// append bookmark
		if (action.getBookmark() != null) {
			params.put(ParameterAccessor.PARAM_BOOKMARK, action.getBookmark());
		}
	}

	/**
	 * @return the pageOverflow
	 */
	public int getPageOverflow() {
		return pageOverflow;
	}

	/**
	 * @param pageOverflow the pageOverflow to set
	 */
	public void setPageOverflow(int pageOverflow) {
		this.pageOverflow = pageOverflow;
	}

	/**
	 * Returns the viewing session ID.
	 *
	 * @return the viewing session ID
	 */
	public String getViewingSessionId() {
		return viewingSessionId;
	}

	/**
	 * Sets the viewing session ID.
	 *
	 * @param viewingSessionId the viewing session id
	 */
	public void setViewingSessionId(String viewingSessionId) {
		this.viewingSessionId = viewingSessionId;
	}
}
