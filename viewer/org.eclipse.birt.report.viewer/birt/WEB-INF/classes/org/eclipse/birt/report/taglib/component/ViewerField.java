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

package org.eclipse.birt.report.taglib.component;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.taglib.ITagConstants;
import org.eclipse.birt.report.utility.ParameterAccessor;
import org.eclipse.birt.report.utility.UrlUtility;

/**
 * Specifies the viewer tag parameters.
 * <p>
 * There are the following parameter attributes:
 * <ol>
 * <li>id-Specifies viewer/requester id.</li>
 * <li>name-Specifies viewer/requester name.</li>
 * <li>baseURL-Specifies the base URL of BIRT viewer.</li>
 * <li>title-Specifies the report title displayed at the top</li>
 * <li>isHostPage</li>
 * <li>isCustom</li>
 * <li>scrolling</li>
 * <li>position</li>
 * <li>style</li>
 * <li>height</li>
 * <li>width</li>
 * <li>left</li>
 * <li>top</li>
 * <li>frameborder</li>
 * <li>reportDesign</li>
 * <li>reportDocument</li>
 * <li>reportletId</li>
 * <li>pattern</li>
 * <li>target</li>
 * <li>bookmark</li>
 * <li>locale</li>
 * <li>timeZone</li>
 * <li>format</li>
 * <li>emitterId</li>
 * <li>pageOverflow</li>
 * <li>svg</li>
 * <li>rtl</li>
 * <li>pageNum</li>
 * <li>pageRange</li>
 * <li>allowMasterPage</li>
 * <li>resourceFolder</li>
 * <li>maxRowsOfRecords</li>
 * <li>forceOverwriteDocument</li>
 * <li>showTitle</li>
 * <li>showToolBar</li>
 * <li>showNavigationBar</li>
 * <li>reportContainer</li>
 * <li>showParameterPage</li>
 * <li>isReportlet</li>
 * </ol>
 */
public class ViewerField implements Serializable, Cloneable, ITagConstants {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9087611432750518446L;

	private String id;
	private String name;
	private String baseURL;
	private String title;
	private boolean isHostPage = false;
	private boolean isCustom = false;
	private String scrolling = SCROLLING_AUTO;
	private String position;
	private String style;
	private int height = -1;
	private int width = -1;
	private String left;
	private String top;
	private String frameborder = "no"; //$NON-NLS-1$

	private String reportDesign;
	private String reportDocument;
	private String reportletId;
	private String pattern;
	private String target;

	private String bookmark;
	private String locale;
	private String timeZone;
	private String format;
	private String emitterId;
	private String pageOverflow;
	private String svg;
	private String rtl;
	private long pageNum;
	private String pageRange;
	private String allowMasterPage = "true"; //$NON-NLS-1$

	private String resourceFolder;
	private int maxRowsOfRecords = -1;
	private String forceOverwriteDocument;

	private String showTitle;
	private String showToolBar;
	private String showNavigationBar;
	private String showParameterPage;

	private String isReportlet;

	private String reportContainer = CONTAINER_IFRAME;

	/**
	 * Report parameters
	 */
	private Map parameters;

	/**
	 * Check whether document existed in URL
	 */
	private boolean documentInUrl = false;

	/**
	 * Report design handle
	 */
	private IViewerReportDesignHandle reportDesignHandle;

	/**
	 * Report parameter definitions List
	 */
	private Collection parameterDefList = null;

	/**
	 * Create the final URI string to preview report
	 *
	 * @param iPattern
	 * @return
	 */
	public String createURI(String iPattern, String viewingSessionId) {
		String uri = iPattern;
		if (uri == null) {
			uri = this.pattern;
		}

		boolean renderReportlet = false;
		if (reportDocument != null && (reportletId != null || (bookmark != null && "true" //$NON-NLS-1$
				.equalsIgnoreCase(isReportlet)))) {
			renderReportlet = true;
		}

		if (uri == null) {
			uri = IBirtConstants.VIEWER_FRAMESET;

			// frameset doesn't support reportlet. If preview reportlet, force
			// to use run pattern.
			if (renderReportlet) {
				uri = IBirtConstants.VIEWER_RUN;
			}
		}

		// whether use frameset pattern
		boolean usingFrameset = IBirtConstants.VIEWER_FRAMESET.equalsIgnoreCase(uri);

		// whether use parameter pattern
		boolean usingParameter = IBirtConstants.VIEWER_PARAMETER.equalsIgnoreCase(uri);

		// append baseURL setting
		if (baseURL != null) {
			uri = baseURL + "/" + uri; //$NON-NLS-1$
		}

		String uriSuffix = "";

		Map uriParams = new HashMap();

		if (viewingSessionId != null) {
			uriParams.put(ParameterAccessor.PARAM_VIEWING_SESSION_ID, viewingSessionId);
		}

		// append format setting
		if (format != null) {
			uriParams.put(ParameterAccessor.PARAM_FORMAT, format);
		}

		if (emitterId != null) {
			uriParams.put(ParameterAccessor.PARAM_EMITTER_ID, emitterId);
		}

		if (pageOverflow != null) {
			uriParams.put(ParameterAccessor.PARAM_PAGE_OVERFLOW, pageOverflow);
		}

		// append report design
		if (reportDesign != null) {
			uriParams.put(ParameterAccessor.PARAM_REPORT, reportDesign);
		}

		// append report document
		if (reportDocument != null) {
			uriParams.put(ParameterAccessor.PARAM_REPORT_DOCUMENT, reportDocument);
		}

		// append reportlet id
		if (reportletId != null) {
			uriParams.put(ParameterAccessor.PARAM_INSTANCEID, reportletId);
		}

		if (usingFrameset && id != null) {
			uriParams.put(ParameterAccessor.PARAM_ID, id);
		}

		// append report title
		if (usingFrameset && title != null) {
			uriParams.put(ParameterAccessor.PARAM_TITLE, title);
		}

		// append report title
		if (usingFrameset && showTitle != null) {
			uriParams.put(ParameterAccessor.PARAM_SHOW_TITLE, showTitle);
		}

		// append target serlvet pattern setting
		if (usingParameter && !isCustom && pattern != null) {
			uriParams.put(ParameterAccessor.PARAM_SERVLET_PATTERN, pattern);
		}

		// append window target setting
		if (usingParameter && !isCustom && target != null) {
			uriParams.put(ParameterAccessor.PARAM_TARGET, target);
		}

		// append Locale setting
		if (locale != null) {
			uriParams.put(ParameterAccessor.PARAM_LOCALE, locale);
		}

		// append time zone setting
		if (timeZone != null) {
			uriParams.put(ParameterAccessor.PARAM_TIMEZONE, timeZone);
		}

		// append svg setting
		if (svg != null) {
			uriParams.put(ParameterAccessor.PARAM_SVG, svg);
		}

		// append rtl setting
		if (rtl != null) {
			uriParams.put(ParameterAccessor.PARAM_RTL, rtl);
		}

		// append page number setting
		if (pageNum > 0) {
			uriParams.put(ParameterAccessor.PARAM_PAGE, Long.toString(pageNum));
		}

		if (pageRange != null) {
			uriParams.put(ParameterAccessor.PARAM_PAGE_RANGE, pageRange);
		}

		// append masterpage setting
		if (allowMasterPage != null) {
			uriParams.put(ParameterAccessor.PARAM_MASTERPAGE, allowMasterPage);
		}

		// append resource folder setting
		if (resourceFolder != null) {
			uriParams.put(ParameterAccessor.PARAM_RESOURCE_FOLDER, resourceFolder);
		}

		// append maxrows setting
		if (maxRowsOfRecords >= 0) {
			uriParams.put(ParameterAccessor.PARAM_MAXROWS, Long.toString(maxRowsOfRecords));
		}

		// append overwrite document setting
		if (forceOverwriteDocument != null) {
			uriParams.put(ParameterAccessor.PARAM_OVERWRITE, forceOverwriteDocument);
		}

		// append show toolbar setting
		if (usingFrameset && showToolBar != null) {
			uriParams.put(ParameterAccessor.PARAM_TOOLBAR, showToolBar);
		}

		// append show NavigationBar setting
		if (usingFrameset && showNavigationBar != null) {
			uriParams.put(ParameterAccessor.PARAM_NAVIGATIONBAR, showNavigationBar);
		}

		// append show ParameterPage setting
		if (showParameterPage != null) {
			uriParams.put(ParameterAccessor.PARAM_PARAMETER_PAGE, showParameterPage);
		}

		// append bookmark setting
		if (bookmark != null) {
			if (IBirtConstants.VIEWER_PREVIEW.equalsIgnoreCase(iPattern) && !"true".equalsIgnoreCase(isReportlet)) //$NON-NLS-1$
			{
				// if use PREVIEW mode, append bookmark directly
				uriSuffix += "#" + UrlUtility.urlParamValueEncode(bookmark); //$NON-NLS-1$
			} else {
				uriParams.put(ParameterAccessor.PARAM_BOOKMARK, bookmark);
			}
		}

		if (isReportlet != null) {
			uriParams.put(ParameterAccessor.PARAM_ISREPORTLET, isReportlet);
		}

		return uri += "?" + UrlUtility.makeUriString(uriParams) + uriSuffix;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the baseURL
	 */
	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * @param baseURL the baseURL to set
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the isHostPage
	 */
	public boolean isHostPage() {
		return isHostPage;
	}

	/**
	 * @param isHostPage the isHostPage to set
	 */
	public void setHostPage(boolean isHostPage) {
		this.isHostPage = isHostPage;
	}

	/**
	 * @return the isCustom
	 */
	public boolean isCustom() {
		return isCustom;
	}

	/**
	 * @param isCustom the isCustom to set
	 */
	public void setCustom(boolean isCustom) {
		this.isCustom = isCustom;
	}

	/**
	 * @return the scrolling
	 */
	public String getScrolling() {
		return scrolling;
	}

	/**
	 * @param scrolling the scrolling to set
	 */
	public void setScrolling(String scrolling) {
		this.scrolling = scrolling;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the left
	 */
	public String getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(String left) {
		this.left = left;
	}

	/**
	 * @return the top
	 */
	public String getTop() {
		return top;
	}

	/**
	 * @param top the top to set
	 */
	public void setTop(String top) {
		this.top = top;
	}

	/**
	 * @return the frameborder
	 */
	public String getFrameborder() {
		return frameborder;
	}

	/**
	 * @param frameborder the frameborder to set
	 */
	public void setFrameborder(String frameborder) {
		this.frameborder = frameborder;
	}

	/**
	 * @return the reportDesign
	 */
	public String getReportDesign() {
		return reportDesign;
	}

	/**
	 * @param reportDesign the reportDesign to set
	 */
	public void setReportDesign(String reportDesign) {
		this.reportDesign = reportDesign;
	}

	/**
	 * @return the reportDocument
	 */
	public String getReportDocument() {
		return reportDocument;
	}

	/**
	 * @param reportDocument the reportDocument to set
	 */
	public void setReportDocument(String reportDocument) {
		this.reportDocument = reportDocument;
	}

	/**
	 * @return the reportletId
	 */
	public String getReportletId() {
		return reportletId;
	}

	/**
	 * @param reportletId the reportletId to set
	 */
	public void setReportletId(String reportletId) {
		this.reportletId = reportletId;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @return the bookmark
	 */
	public String getBookmark() {
		return bookmark;
	}

	/**
	 * @param bookmark the bookmark to set
	 */
	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the emitterId
	 */
	public String getEmitterId() {
		return emitterId;
	}

	/**
	 * @param emitterId the emitterId to set
	 */
	public void setEmitterId(String emitterId) {
		this.emitterId = emitterId;
	}

	/**
	 * @return the pageOverflow
	 */
	public String getPageOverflow() {
		return pageOverflow;
	}

	/**
	 * @param pageOverflow the pageOverflow to set
	 */
	public void setPageOverflow(String pageOverflow) {
		this.pageOverflow = pageOverflow;
	}

	/**
	 * @return the svg
	 */
	public String getSvg() {
		return svg;
	}

	/**
	 * @param svg the svg to set
	 */
	public void setSvg(String svg) {
		this.svg = svg;
	}

	/**
	 * @return the rtl
	 */
	public String getRtl() {
		return rtl;
	}

	/**
	 * @param rtl the rtl to set
	 */
	public void setRtl(String rtl) {
		this.rtl = rtl;
	}

	/**
	 * @return the pageNum
	 */
	public long getPageNum() {
		return pageNum;
	}

	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(long pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * @return the pageRange
	 */
	public String getPageRange() {
		return pageRange;
	}

	/**
	 * @param pageRange the pageRange to set
	 */
	public void setPageRange(String pageRange) {
		this.pageRange = pageRange;
	}

	/**
	 * @return the allowMasterPage
	 */
	public String getAllowMasterPage() {
		return allowMasterPage;
	}

	/**
	 * @param allowMasterPage the allowMasterPage to set
	 */
	public void setAllowMasterPage(String allowMasterPage) {
		this.allowMasterPage = allowMasterPage;
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
	 * @return the maxRowsOfRecords
	 */
	public int getMaxRowsOfRecords() {
		return maxRowsOfRecords;
	}

	/**
	 * @param maxRowsOfRecords the maxRowsOfRecords to set
	 */
	public void setMaxRowsOfRecords(int maxRowsOfRecords) {
		this.maxRowsOfRecords = maxRowsOfRecords;
	}

	/**
	 * @return the forceOverwriteDocument
	 */
	public String getForceOverwriteDocument() {
		return forceOverwriteDocument;
	}

	/**
	 * @param forceOverwriteDocument the forceOverwriteDocument to set
	 */
	public void setForceOverwriteDocument(String forceOverwriteDocument) {
		this.forceOverwriteDocument = forceOverwriteDocument;
	}

	/**
	 * @return the showTitle
	 */
	public String getShowTitle() {
		return showTitle;
	}

	/**
	 * @param showTitle the showTitle to set
	 */
	public void setShowTitle(String showTitle) {
		this.showTitle = showTitle;
	}

	/**
	 * @return the showToolBar
	 */
	public String getShowToolBar() {
		return showToolBar;
	}

	/**
	 * @param showToolBar the showToolBar to set
	 */
	public void setShowToolBar(String showToolBar) {
		this.showToolBar = showToolBar;
	}

	/**
	 * @return the showNavigationBar
	 */
	public String getShowNavigationBar() {
		return showNavigationBar;
	}

	/**
	 * @param showNavigationBar the showNavigationBar to set
	 */
	public void setShowNavigationBar(String showNavigationBar) {
		this.showNavigationBar = showNavigationBar;
	}

	/**
	 * @return the showParameterPage
	 */
	public String getShowParameterPage() {
		return showParameterPage;
	}

	/**
	 * @param showParameterPage the showParameterPage to set
	 */
	public void setShowParameterPage(String showParameterPage) {
		this.showParameterPage = showParameterPage;
	}

	/**
	 * @return the isReportlet
	 */
	public String getIsReportlet() {
		return isReportlet;
	}

	/**
	 * @param isReportlet the isReportlet to set
	 */
	public void setIsReportlet(String isReportlet) {
		this.isReportlet = isReportlet;
	}

	/**
	 * @return the reportContainer
	 */
	public String getReportContainer() {
		return reportContainer;
	}

	/**
	 * @param reportContainer the reportContainer to set
	 */
	public void setReportContainer(String reportContainer) {
		this.reportContainer = reportContainer;
	}

	/**
	 * @return the parameters
	 */
	public Map getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map parameters) {
		this.parameters = parameters;
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

	/**
	 * @return the reportDesignHandle
	 */
	public IViewerReportDesignHandle getReportDesignHandle() {
		return reportDesignHandle;
	}

	/**
	 * @param reportDesignHandle the reportDesignHandle to set
	 */
	public void setReportDesignHandle(IViewerReportDesignHandle reportDesignHandle) {
		this.reportDesignHandle = reportDesignHandle;
	}

	/**
	 * @return the parameterDefList
	 */
	public Collection getParameterDefList() {
		return parameterDefList;
	}

	/**
	 * @param parameterDefList the parameterDefList to set
	 */
	public void setParameterDefList(Collection parameterDefList) {
		this.parameterDefList = parameterDefList;
	}

}
