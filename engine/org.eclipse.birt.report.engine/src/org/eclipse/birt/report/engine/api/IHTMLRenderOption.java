
package org.eclipse.birt.report.engine.api;

import java.util.List;

/**
 * Interface of constants of the HTML Render Opitons. This interface is
 * implemented only by: <B>HTMLRenderOption</B> so far. The implementation
 * classes should have the methods in order to support then HTML render options,
 * including getter and setter.
 */
public interface IHTMLRenderOption extends IRenderOption {

	/**
	 * Define the type of the html content, it can be one of: HTML or HTML_NOCSS.
	 */
	public static final String HTML_TYPE = "HTMLType"; //$NON-NLS-1$
	/**
	 * Output a complete HTML
	 */
	public static final String HTML = "HTML"; //$NON-NLS-1$
	/**
	 * Output a HTML fragement without CSS defination.
	 */
	public static final String HTML_NOCSS = "HTMLNoCSS"; //$NON-NLS-1$
	/**
	 * The agent used to render the html output.
	 */
	public static final String USER_AGENT = "user-agent"; //$NON-NLS-1$
	/**
	 * for some case, the user needs to define the resource encoding. It is used to
	 * encoding the hyperlinks which refers to the local resource. The value is a
	 * encode name, such as "utf-8".
	 */
	public static final String URL_ENCODING = "url-encoding"; //$NON-NLS-1$
	/**
	 * The list used to contain the active object ids. The value is a List object.
	 * The active objects including: Tempalate, Table, Chart, Label. the object in
	 * the list is a string, which is : bookmark, type, id.
	 */
	public static final String INSTANCE_ID_LIST = "InstanceIdList"; //$NON-NLS-1$

	/**
	 * Should the report contains page header and footer The value is a Boolean
	 * object, default is <code>Boolean.TRUE</code> Only effect if the
	 * HTML_Pagination is true
	 */
	public static final String MASTER_PAGE_CONTENT = "htmlMasterPageContent"; //$NON-NLS-1$
	/**
	 * Should we output the selection handle with the active object. The value is a
	 * Boolean Object, the default is <code>Boolean.FALSE</code>.
	 * 
	 * @deprecated use METADATA
	 */
	public static final String HTML_INCLUDE_SELECTION_HANDLE = "includeSelectionHandle"; //$NON-NLS-1$
	/**
	 * Should we output HTML as RtL. The value is an Boolean Object, the default is
	 * <code>Boolean.FALSE</code>.
	 */
	public static final String HTML_RTL_FLAG = IRenderOption.RTL_FLAG; // $NON-NLS-1$
	/**
	 * Output the HTML default title. value???
	 */
	public static final String HTML_TITLE = "htmlTitle"; //$NON-NLS-1$
	/**
	 * Floating the page footer. The value is an Boolean Object, the default is
	 * <code>Boolean.TRUE</code>. Only effect when HTML_PAGINATION is set to
	 * <code>Boolean.TRUE</code>
	 */
	public static final String PAGEFOOTER_FLOAT_FLAG = "pageFooterFloatFlag"; //$NON-NLS-1$
	/**
	 * Should the output contain metadata. This value is a Boolean Object. And if
	 * it's set to be <code>Boolean.TRUE</code>., the output will contains metadata
	 * include: Instance id, type and so on The default value is
	 * <code>Boolean.FALSE</code>
	 */
	public static final String HTML_ENABLE_METADATA = "htmlEnableMetadata"; //$NON-NLS-1$
	/**
	 * Should the output display the filter icon. Only effect if the enable metadata
	 * is setting to <code>Boolea.TRUE</code>. The default value is
	 * <code>Boolean.FALSE</code>
	 */
	public static final String HTML_DISPLAY_FILTER_ICON = "htmlDisplayFilterIcon"; //$NON-NLS-1$
	/**
	 * Should the output display the group icon. Only effect if the enable metadata
	 * is setting to <code>Boolea.TRUE</code>. The default value is
	 * <code>Boolean.FALSE</code>
	 */
	public static final String HTML_DISPLAY_GROUP_ICON = "displayGroupIcon"; //$NON-NLS-1$ //false

	public static final String IMAGE_DIRECTROY = "imageDirectory";

	public static final String BASE_IMAGE_URL = "baseImageURL";

	public static final String LAYOUT_PREFERENCE_FIXED = "fixed"; //$NON-NLS-1$

	public static final String LAYOUT_PREFERENCE_AUTO = "auto"; //$NON-NLS-1$

	public static final String HTML_VIEWPORT_META = "viewport"; //$NON-NLS-1$

	/**
	 * @return Returns the instanceIDs.
	 */
	public List getInstanceIDs();

	/**
	 * @param instanceIDs The instanceIDs to set.
	 */
	public void setInstanceIDs(List instanceIDs);

	/**
	 * sets whether the HTML output can be embedded directly into an HTML page
	 * 
	 * @param embeddable whether the HTML output can be embedded directly into an
	 *                   HTML page
	 */
	public void setEmbeddable(boolean embeddable);

	/**
	 * @return whether the output is embeddable
	 */
	public boolean getEmbeddable();

	/**
	 * @param userAgent the user agent of the request
	 */
	public void setUserAgent(String userAgent);

	/**
	 * @return the user agent for the request
	 */
	public String getUserAgent();

	public void setUrlEncoding(String encoding);

	/**
	 * @return the user agent for the request
	 */
	public String getUrlEncoding();

	/**
	 * Set master page content.
	 * 
	 * @param show
	 */
	public void setMasterPageContent(boolean show);

	/**
	 * Get master page content.
	 * 
	 * @return
	 */
	public boolean getMasterPageContent();

	/**
	 * Set Html pagination.
	 * 
	 * @param paginate
	 */
	public void setHtmlPagination(boolean paginate);

	/**
	 * Get Html pagination.
	 * 
	 * @return
	 */
	public boolean getHtmlPagination();

	/**
	 * @deprecated includeSelectionHandle is replaced by eanableMetadata flag.
	 */
	public void setIncludeSelectionHandle(boolean option);

	/**
	 * @deprecated includeSelectionHandle is replaced by eanableMetadata flag.
	 */
	public boolean getIncludeSelectionHandle();

	/**
	 * Set Html RTL flag.
	 * 
	 * @param flag
	 */
	public void setHtmlRtLFlag(boolean flag);

	/**
	 * Get Html RTL flag.
	 * 
	 * @return
	 */
	public boolean getHtmlRtLFlag();

	/**
	 * @param htmlTile
	 */
	public void setHtmlTitle(String htmlTitle);

	/**
	 * @return the default html title
	 */
	public String getHtmlTitle();

	/**
	 * Set page footer float flag.
	 * 
	 * @param flag
	 */
	public void setPageFooterFloatFlag(boolean flag);

	/**
	 * Get page footer float flag.
	 * 
	 * @return
	 */
	public boolean getPageFooterFloatFlag();

	/**
	 * Sets the flag which indicating if metadata should be output.
	 * 
	 * @param enableMetadata the flag
	 */
	public void setEnableMetadata(boolean enableMetadata);

	/**
	 * @return the enable metadata flag value.
	 */
	public boolean getEnableMetadata();

	/**
	 * Sets the flag indicationg that if filter icons should be displayed.
	 * 
	 * @param displayFilterIcon the flag
	 */
	public void setDisplayFilterIcon(boolean displayFilterIcon);

	/**
	 * @return the display filter icon flag value.
	 */
	public boolean getDisplayFilterIcon();

	/**
	 * Sets the flag indicationg that if group expand/collapse icons should be
	 * displayed.
	 * 
	 * @param displayFilterIcon the flag
	 */
	public void setDisplayGroupIcon(boolean displayFilterIcon);

	/**
	 * @return the group expand/collapse icon flag value.
	 */
	public boolean getDisplayGroupIcon();

	/**
	 * returns the image directory that engine stores images and charts into
	 * 
	 * @return the image directory.
	 */
	public String getImageDirectory();

	/**
	 * sets the image directory that engine stores images and charts into
	 * 
	 * @param imageDirectory the image directory that engine stores images and
	 *                       charts into
	 */
	public void setImageDirectory(String imageDirectory);

	/**
	 * returns the base url for creating image URL
	 * 
	 * @return Rreturn the abse image url
	 */
	public String getBaseImageURL();

	/**
	 * sets the base image URL for image handling
	 * 
	 * @param baseImageURL the base image URL
	 */
	public void setBaseImageURL(String baseImageURL);

	/**
	 * returns whether to use compact mode for the HTML output, whose default value
	 * is false.
	 * 
	 * @return
	 */
	public boolean isEnableCompactMode();

	/**
	 * sets whether to use compact mode for the HTML output. If the compact mode was
	 * used, text indents and new line separators will be ignored.
	 * 
	 * @param enableCompactMode
	 */
	public void setEnableCompactMode(boolean enableCompactMode);

	/**
	 * Should the top-level template table be wrapped. Only effect if the enable
	 * metadata is setting to <code>Boolea.TRUE</code>. The default value is
	 * <code>Boolean.FALSE</code>
	 */
	public static final String HTML_WRAP_TEMPLATE_TABLE = "wrapTemplateTable"; //$NON-NLS-1$ //false
	/**
	 * Should the table be outed as fixed. The default value is
	 * <code>Boolean.FALSE</code>
	 */
	public static final String HTML_LAYOUT_PREFERENCE = "layoutPreference"; //$NON-NLS-1$ //false

	/**
	 * Should the HTML emitter output the BIRT styles directly to the report. The
	 * default value is <code>Boolean.FALSE</code> True: means the HTML emitter will
	 * output the BIRT styles directly to the report and depends on the browser to
	 * implement the style calculation. False: means the HTML emitter will use BIRT
	 * style engine to calculate the styles and output the result to the report.
	 */
	public static final String HTML_ENABLE_AGENTSTYLE_ENGINE = "enableAgentStyleEngine"; //$NON-NLS-1$ //false

	/**
	 * Should the HTML emitter output the master page's margins. This property is
	 * only works for the auto layout report. The fixed report will always output
	 * the master page's margins. The default value is <code>Boolean.FALSE</code>
	 * True: means the HTML emitter will output the master page's margins for auto
	 * report. False: means the HTML emitter won't output the master page's margins
	 * for auto report.
	 */
	public static final String HTML_OUTPUT_MASTER_PAGE_MARGINS = "org.eclipse.birt.report.engine.api.IHTMLRenderOption.outputMasterPageMargins"; //$NON-NLS-1$ //false

	/**
	 * HTMLIDNamespace is used to solve the name conflict problem when embedding
	 * multiple reports into one page.
	 */
	public static final String HTML_ID_NAMESPACE = "HTMLIDNamespace";

	/**
	 * whether or not the HTML is indented. The default value is true.
	 */
	public static final String HTML_INDENT = "HTMLIndent";

	/**
	 * A filter for metadata.
	 */
	public static final String METADATA_FILTER = "metadataFilter";

	/**
	 * Using inline style or not. This option only works when the report is
	 * embeddable. True: Try to using the inline style to instead of style class.
	 * False: Try to use the style class to compress the HTML source.
	 */
	public static final String HTML_ENABLE_INLINE_STYLE = "htmlEnableInlineStyle";

	/**
	 * Using compact mode or not. If compact mode is enabled, text indents and new
	 * line separators will be ignored in html output.
	 */
	public static final String HTML_ENABLE_COMPACT_MODE = "htmlEnableCompactMode";

	/**
	 * the URL of head.js file
	 */
	static final String BIRT_JS_URL_KEY = "birtJsUrl"; //$NON-NLS-1$
}
