package org.eclipse.birt.report.engine.api;

/**
 * Interface of constants of the HTML Render Opitons. This interface is
 * implemented only by: <B>HTMLRenderOption</B> so far. The implementation classes
 * should have the methods in order to support then HTML render options,
 * including getter and setter.
 */
public interface IHTMLRenderOption
{
	/**
	 * Define the type of the html content, it can be one of:  
	 * HTML or HTML_NOCSS.
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
	 * for some case, the user needs to define the resource encoding.
	 * It is used to encoding the hyperlinks which refers to the
	 * local resource.
	 * The value is a encode name, such as "utf-8".
	 */
	public static final String URL_ENCODING = "url-encoding"; //$NON-NLS-1$
	/**
	 * The list used to contain the active object ids. The value is
	 * a List object.
	 * The active objects including:
	 * Tempalate, Table, Chart, Label.
	 * the object in the list is a string, which is :
	 * bookmark, type, id.
	 */
	public static final String INSTANCE_ID_LIST = "InstanceIdList"; //$NON-NLS-1$
	/**
	 * Should the report contains paginations
	 * The value is a Boolean object, default is <code>Boolean.TRUE</code>.
	 */
	public static final String HTML_PAGINATION = "htmlPagination"; //$NON-NLS-1$
	/**
	 * Should the report contains page header and footer
	 * The value is a Boolean object, default is <code>Boolean.TRUE</code>
	 * Only effect if the HTML_Pagination is true
	 */
	public static final String MASTER_PAGE_CONTENT = "htmlMasterPageContent"; //$NON-NLS-1$
	/**
	 * Should we output the selection handle with the active object.
	 * The value is a Boolean Object, the default is <code>Boolean.FALSE</code>.
	 * @deprecated use METADATA
	 */
	public static final String HTML_INCLUDE_SELECTION_HANDLE = "includeSelectionHandle"; //$NON-NLS-1$
	/**
	 * Should we output HTML as RtL.
	 * The value is an Boolean Object, the default is <code>Boolean.FALSE</code>.
	 */
	public static final String HTML_RTL_FLAG = "htmlRtLFlag"; //$NON-NLS-1$
	/**
	 * Output the HTML default title. value???
	 */
	public static final String HTML_TITLE = "htmlTitle"; //$NON-NLS-1$
	/**
	 * Floating the page footer.
	 * The value is an Boolean Object, the default is <code>Boolean.TRUE</code>.
	 * Only effect when HTML_PAGINATION is set to <code>Boolean.TRUE</code>
	 */
	public static final String PAGEFOOTER_FLOAT_FLAG = "pageFooterFloatFlag"; //$NON-NLS-1$
	/**
	 * Should the output contain metadata. This value is a Boolean Object. And
	 * if it's set to be <code>Boolean.TRUE</code>., the output will contains
	 * metadata include: Instance id, type and so on
	 * The default value is <code>Boolean.FALSE</code>
	 */
	public static final String HTML_ENABLE_METADATA = "htmlEnableMetadata"; //$NON-NLS-1$
	/**
	 * Should the output display the filter icon.
	 * Only effect if the enable metadata is setting to <code>Boolea.TRUE</code>.
	 * The default value is <code>Boolean.FALSE</code>
	 */
	public static final String HTML_DISPLAY_FILTER_ICON = "htmlDisplayFilterIcon"; //$NON-NLS-1$
	/**
	 * Should the output display the group icon.
	 * Only effect if the enable metadata is setting to <code>Boolea.TRUE</code>.
	 * The default value is <code>Boolean.FALSE</code>
	 */
	public static final String HTML_DISPLAY_GROUP_ICON = "displayGroupIcon"; //$NON-NLS-1$//false
}
