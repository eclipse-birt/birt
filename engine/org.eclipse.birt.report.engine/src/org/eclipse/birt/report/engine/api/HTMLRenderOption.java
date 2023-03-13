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

import java.util.HashMap;
import java.util.List;

/**
 * output settings for HTML output format
 */
public class HTMLRenderOption extends RenderOption implements IHTMLRenderOption {

	/**
	 * Constructor
	 */
	public HTMLRenderOption(IRenderOption options) {
		super(options);
	}

	public HTMLRenderOption(HashMap options) {
		super(options);
	}

	/**
	 * Default constructor.
	 */
	public HTMLRenderOption() {
		super();
	}

	/**
	 * @return Returns the instanceIDs.
	 */
	@Override
	public List getInstanceIDs() {
		Object list = getOption(INSTANCE_ID_LIST);
		if (list instanceof List) {
			return (List) list;
		}
		return null;
	}

	/**
	 * @param instanceIDs The instanceIDs to set.
	 */
	@Override
	public void setInstanceIDs(List instanceIDs) {
		setOption(INSTANCE_ID_LIST, instanceIDs);
	}

	/**
	 * sets whether the HTML output can be embedded directly into an HTML page
	 *
	 * @param embeddable whether the HTML output can be embedded directly into an
	 *                   HTML page
	 */
	@Override
	public void setEmbeddable(boolean embeddable) {
		if (embeddable) {
			setOption(HTML_TYPE, HTML_NOCSS);
		} else {
			setOption(HTML_TYPE, HTML);
		}
	}

	/**
	 * @return whether the output is embeddable
	 */
	@Override
	public boolean getEmbeddable() {
		String htmlType = getStringOption(HTML_TYPE);
		if (HTML_NOCSS.equals(htmlType)) {
			return true;
		}
		return false;
	}

	/**
	 * @param userAgent the user agent of the request
	 */
	@Override
	public void setUserAgent(String userAgent) {
		setOption(USER_AGENT, userAgent);
	}

	/**
	 * @return the user agent for the request
	 */
	@Override
	public String getUserAgent() {
		return getStringOption(USER_AGENT);
	}

	/**
	 * Set the URL encoding for the request.
	 */
	@Override
	public void setUrlEncoding(String encoding) {
		setOption(URL_ENCODING, encoding);
	}

	/**
	 * @return the user agent for the request
	 */
	@Override
	public String getUrlEncoding() {
		return getStringOption(URL_ENCODING);
	}

	/**
	 * Set master page content.
	 */
	@Override
	public void setMasterPageContent(boolean show) {
		setOption(MASTER_PAGE_CONTENT, Boolean.valueOf(show));
	}

	/**
	 * Get master page content.
	 */
	@Override
	public boolean getMasterPageContent() {
		return getBooleanOption(MASTER_PAGE_CONTENT, true);
	}

	/**
	 * Set html pagination.
	 */
	@Override
	public void setHtmlPagination(boolean paginate) {
		setOption(HTML_PAGINATION, Boolean.valueOf(paginate));
	}

	/**
	 * Get html pagination.
	 */
	@Override
	public boolean getHtmlPagination() {
		return getBooleanOption(HTML_PAGINATION, false);
	}

	/**
	 * @deprecated includeSelectionHandle is replaced by eanableMetadata flag.
	 */
	@Deprecated
	@Override
	public void setIncludeSelectionHandle(boolean option) {
		setOption(HTML_INCLUDE_SELECTION_HANDLE, Boolean.valueOf(option));
	}

	/**
	 * @deprecated includeSelectionHandle is replaced by eanableMetadata flag.
	 */
	@Deprecated
	@Override
	public boolean getIncludeSelectionHandle() {
		return getBooleanOption(HTML_INCLUDE_SELECTION_HANDLE, false);
	}

	/**
	 * Set Html RTL flag.
	 */
	@Override
	public void setHtmlRtLFlag(boolean flag) {
		setOption(HTML_RTL_FLAG, Boolean.valueOf(flag));
	}

	/**
	 * Get Html RTL flag.
	 */
	@Override
	public boolean getHtmlRtLFlag() {
		return getBooleanOption(HTML_RTL_FLAG, false);
	}

	/**
	 * @param htmlTile
	 */
	@Override
	public void setHtmlTitle(String htmlTitle) {
		setOption(HTML_TITLE, htmlTitle);
	}

	/**
	 * @return the default html title
	 */
	@Override
	public String getHtmlTitle() {
		return getStringOption(HTML_TITLE);
	}

	/**
	 * Set page footer float flag.
	 */
	@Override
	public void setPageFooterFloatFlag(boolean flag) {
		setOption(PAGEFOOTER_FLOAT_FLAG, Boolean.valueOf(flag));
	}

	/**
	 * Get page footer float flag.
	 */
	@Override
	public boolean getPageFooterFloatFlag() {
		return getBooleanOption(PAGEFOOTER_FLOAT_FLAG, true);
	}

	/**
	 * Sets the flag which indicating if metadata should be output.
	 *
	 * @param enableMetadata the flag
	 */
	@Override
	public void setEnableMetadata(boolean enableMetadata) {
		setOption(HTML_ENABLE_METADATA, Boolean.valueOf(enableMetadata));
	}

	/**
	 * @return the enable metadata flag value.
	 */
	@Override
	public boolean getEnableMetadata() {
		return getBooleanOption(HTML_ENABLE_METADATA, false);
	}

	/**
	 * Sets the flag indicationg that if filter icons should be displayed.
	 *
	 * @param displayFilterIcon the flag
	 */
	@Override
	public void setDisplayFilterIcon(boolean displayFilterIcon) {
		setOption(HTML_DISPLAY_FILTER_ICON, Boolean.valueOf(displayFilterIcon));
	}

	/**
	 * @return the display filter icon flag value.
	 */
	@Override
	public boolean getDisplayFilterIcon() {
		return getBooleanOption(HTML_DISPLAY_FILTER_ICON, false);
	}

	/**
	 * Sets the flag indicationg that if group expand/collapse icons should be
	 * displayed.
	 *
	 * @param displayGroupIcon the flag
	 */
	@Override
	public void setDisplayGroupIcon(boolean displayGroupIcon) {
		setOption(HTML_DISPLAY_GROUP_ICON, Boolean.valueOf(displayGroupIcon));
	}

	/**
	 * @return the group expand/collapse icon flag value.
	 */
	@Override
	public boolean getDisplayGroupIcon() {
		return getBooleanOption(HTML_DISPLAY_GROUP_ICON, false);
	}

	/**
	 * returns the image directory that engine stores images and charts into
	 *
	 * @return the image directory.
	 */
	@Override
	public String getImageDirectory() {
		return getStringOption(IMAGE_DIRECTROY);
	}

	/**
	 * sets the image directory that engine stores images and charts into
	 *
	 * @param imageDirectory the image directory that engine stores images and
	 *                       charts into
	 */
	@Override
	public void setImageDirectory(String imageDirectory) {
		setOption(IMAGE_DIRECTROY, imageDirectory);
	}

	/**
	 * returns the base url for creating image URL
	 *
	 * @return Rreturn the abse image url
	 */
	@Override
	public String getBaseImageURL() {
		return getStringOption(BASE_IMAGE_URL);
	}

	/**
	 * sets the base image URL for image handling
	 *
	 * @param baseImageURL the base image URL
	 */
	@Override
	public void setBaseImageURL(String baseImageURL) {
		setOption(BASE_IMAGE_URL, baseImageURL);
	}

	/**
	 * Sets the flag indicationg that if the top-level table should be wrapped.
	 *
	 * @param wrapTemplateTable the flag
	 */
	public void setWrapTemplateTable(boolean wrapTemplateTable) {
		options.put(HTML_WRAP_TEMPLATE_TABLE, Boolean.valueOf(wrapTemplateTable));
	}

	/**
	 * @return the group expand/collapse icon flag value.
	 */
	public boolean getWrapTemplateTable() {
		Object value = options.get(HTML_WRAP_TEMPLATE_TABLE);
		if (value instanceof Boolean) {
			return ((Boolean) value).booleanValue();
		}
		return false;
	}

	/**
	 * Sets the flag indicationg that if the table should be outed as fixed.
	 *
	 * @param layoutPreference the flag
	 */
	public void setLayoutPreference(String layoutPreference) {
		options.put(HTML_LAYOUT_PREFERENCE, layoutPreference);
	}

	/**
	 * @return the table layout fixed flag value.
	 */
	public String getLayoutPreference() {
		Object value = options.get(HTML_LAYOUT_PREFERENCE);
		if (value instanceof String) {
			return (String) value;
		}
		return null;
	}

	/**
	 * Sets the flag indicationg that agentStyleEngine is enabled or not.
	 *
	 * @param enableAgentStyleEngine True: means the HTML emitter will output the
	 *                               BIRT styles directly to the report and depends
	 *                               on the browser to implement the style
	 *                               calculation. False: means the HTML emitter will
	 *                               use BIRT style engine to calculate the styles
	 *                               and output the result to the report.
	 */
	public void setEnableAgentStyleEngine(boolean enableAgentStyleEngine) {
		options.put(HTML_ENABLE_AGENTSTYLE_ENGINE, Boolean.valueOf(enableAgentStyleEngine));
	}

	/**
	 * @return the agentStyleEngine enabled flag value.
	 */
	public boolean getEnableAgentStyleEngine() {
		Object value = options.get(HTML_ENABLE_AGENTSTYLE_ENGINE);
		if (value instanceof Boolean) {
			return ((Boolean) value).booleanValue();
		}
		return false;
	}

	/**
	 * Sets the flag indicationg that output the master page's margins for the auto
	 * layout report or not.
	 *
	 * @param outputMasterPageMargins True: means the HTML emitter will output the
	 *                                master page's margins for auto report. False:
	 *                                means the HTML emitter won't output the master
	 *                                page's margins for auto report.
	 *
	 */
	public void setOutputMasterPageMargins(boolean outputMasterPageMargins) {
		options.put(HTML_OUTPUT_MASTER_PAGE_MARGINS, Boolean.valueOf(outputMasterPageMargins));
	}

	/**
	 * @return the outputMasterPageMargins flag value.
	 */
	public boolean getOutputMasterPageMargins() {
		Object value = options.get(HTML_OUTPUT_MASTER_PAGE_MARGINS);
		if (value instanceof Boolean) {
			return ((Boolean) value).booleanValue();
		}
		return false;
	}

	/**
	 * sets the HTML ID namespace.
	 *
	 * @param id the viewer id
	 */
	public void setHTMLIDNamespace(String id) {
		setOption(HTML_ID_NAMESPACE, id);
	}

	/**
	 * @return the HTML ID namespace.
	 */
	public String getHTMLIDNamespace() {
		return getStringOption(HTML_ID_NAMESPACE);
	}

	/**
	 * Sets the flag indicating that if the HTML should be indented.
	 *
	 * @param indent the flag
	 */
	public void setHTMLIndent(boolean indent) {
		options.put(HTML_INDENT, Boolean.valueOf(indent));
	}

	/**
	 * @return the HTML indent flag value. The default value is true.
	 */
	public boolean getHTMLIndent() {
		Object value = options.get(HTML_INDENT);
		if (value instanceof Boolean) {
			return ((Boolean) value).booleanValue();
		}
		return true;
	}

	/*
	 * set the metadata filter.
	 */
	public void setMetadataFilter(IMetadataFilter filter) {
		setOption(METADATA_FILTER, filter);
	}

	/*
	 * get the metadata filter.
	 */
	public IMetadataFilter getMetadataFilter() {
		Object filter = getOption(METADATA_FILTER);
		if (filter instanceof IMetadataFilter) {
			return (IMetadataFilter) filter;
		}
		return null;
	}

	/**
	 * Sets the flag indicating that Trying to use inline style instead of CSS class
	 * style. This option only works when the report is embeddable.
	 *
	 * @param inlineStyleFlag the flag
	 */
	public void setEnableInlineStyle(boolean inlineStyleFlag) {
		options.put(HTML_ENABLE_INLINE_STYLE, Boolean.valueOf(inlineStyleFlag));
	}

	/**
	 * @return the inline style flag value. The default value is false. True: Try to
	 *         using the inline style to instead of style class. Fals: Try to use
	 *         the style class to compress the HTML source.
	 */
	public boolean getEnableInlineStyle() {
		Object value = options.get(HTML_ENABLE_INLINE_STYLE);
		if (value instanceof Boolean) {
			return ((Boolean) value).booleanValue();
		}
		return false;
	}

	/**
	 * set the viewport meta information.
	 *
	 * @param viewport
	 */
	public void setViewportMeta(String viewport) {
		options.put(HTML_VIEWPORT_META, viewport);
	}

	/**
	 *
	 * @return the viewport meta information.
	 */
	public String getViewportMeta() {
		return getStringOption(HTML_VIEWPORT_META);
	}

	@Override
	public boolean isEnableCompactMode() {
		return getBooleanOption(HTML_ENABLE_COMPACT_MODE, false);
	}

	@Override
	public void setEnableCompactMode(boolean enableCompactMode) {
		setOption(HTML_ENABLE_COMPACT_MODE, enableCompactMode);
	}

	/**
	 * set the URL of head.js file
	 *
	 * @param headJsUrl
	 */
	public void setBirtJsUrl(String birtJsUrl) {
		setOption(BIRT_JS_URL_KEY, birtJsUrl);
	}

	/**
	 * get the URL of head.js file
	 *
	 * @return the URL of head.js file
	 */
	public String getBirtJsUrl() {
		return getStringOption(BIRT_JS_URL_KEY);
	}
}
