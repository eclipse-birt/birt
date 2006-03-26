package org.eclipse.birt.report.engine.api;


public interface IHTMLRenderOption
{
	/**
	 * the teyp of the html content, it can be one of 
	 * HTML or HTML_NOCSS.
	 */
	public static final String HTML_TYPE = "HTMLType"; //$NON-NLS-1$
	/**
	 * output a complete HTML
	 */
	public static final String HTML = "HTML"; //$NON-NLS-1$
	/**
	 * output a HTML fragement without CSS defination.
	 */
	public static final String HTML_NOCSS = "HTMLNoCSS"; //$NON-NLS-1$
	/**
	 * the agent used to render the html output.
	 */
	public static final String USER_AGENT = "user-agent"; //$NON-NLS-1$
	/**
	 * for some case, the user need define the resource encoding.
	 * It is used to encoding the hyperlinks which refers to the
	 * local resource.
	 * The value is a encode name, such as "utf-8"
	 */
	public static final String URL_ENCODING = "url-encoding"; //$NON-NLS-1$
	/**
	 * the list used to contains the active object id. The value is
	 * a List object.
	 * The active object:
	 * Tempalate, Table, Chart, Label.
	 */
	public static final String INSTANCE_ID_LIST = "InstanceIdList"; //$NON-NLS-1$
	/**
	 * should the report contains paginations
	 * The value is a Boolean object, default is true.
	 */
	public static final String HTML_PAGINATION = "htmlPagination"; //$NON-NLS-1$
	/**
	 * should the report contains page header and footer
	 * The value is a Boolean object, default is true
	 */
	public static final String MASTER_PAGE_CONTENT = "htmlMasterPageContent"; //$NON-NLS-1$
	/**
	 * should we output the template. 
	 * the value is a an Boolean object, the default is false.
	 */
	public static final String OBSERVE_TEMPLATE_DEFAULT = "observeTemplateDefault"; //$NON-NLS-1$
	/**
	 * should we output the selection handle with the active object.
	 * The action 
	 * The value is an Boolean Object, the default is false.
	 */
	public static final String HTML_INCLUDE_SELECTION_HANDLE = "includeSelectionHandle"; //$NON-NLS-1$
}
