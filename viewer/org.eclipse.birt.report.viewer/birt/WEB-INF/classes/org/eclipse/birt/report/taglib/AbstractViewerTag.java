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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.taglib.component.ParameterField;
import org.eclipse.birt.report.taglib.util.BirtTagUtil;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Abstract class for viewer tag. List base attributes for a viewer tag.
 *
 */
public abstract class AbstractViewerTag extends AbstractBaseTag {

	private static final long serialVersionUID = -7188886543126605745L;

	/**
	 * Locale information
	 */
	protected Locale locale;

	/**
	 * Time zone information
	 */
	protected TimeZone timeZone;

	/**
	 * Report parameters
	 */
	protected Map parameters;

	/**
	 * Then entry to initialize tag
	 *
	 * @throws Exception
	 */
	@Override
	public void __init() {
		super.__init();
		parameters = new LinkedHashMap();
	}

	/**
	 * validate the tag
	 *
	 * @see org.eclipse.birt.report.taglib.AbstractBaseTag#__validate()
	 */
	@Override
	public boolean __validate() throws Exception {
		String hasHostPage = (String) pageContext.getAttribute(ATTR_HOSTPAGE);
		if ("true".equalsIgnoreCase(hasHostPage)) //$NON-NLS-1$
		{
			return false;
		}

		// get Locale
		this.locale = BirtTagUtil.getLocale((HttpServletRequest) pageContext.getRequest(), viewer.getLocale());

		// get time zone
		this.timeZone = BirtTagUtil.getTimeZone((HttpServletRequest) pageContext.getRequest(), viewer.getTimeZone());

		// Set locale information
		BirtResources.setLocale(this.locale);

		// Validate viewer id
		if (viewer.getId() == null || viewer.getId().length() <= 0) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_NO_ATTR_ID));
		}

		if (!__validateViewerId()) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_INVALID_ATTR_ID));
		}

		// validate the viewer id if unique
		if (pageContext.findAttribute(viewer.getId()) != null) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_ATTR_ID_DUPLICATE));
		}

		// Report design or document should be specified
		if (viewer.getReportDesign() == null && viewer.getReportDocument() == null) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_NO_REPORT_SOURCE));
		}

		// If preview reportlet, report document file should be specified.
		if (viewer.getReportletId() != null && viewer.getReportDocument() == null) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_NO_REPORT_DOCUMENT));
		}

		return true;
	}

	/**
	 * Validate the viewer id. Viewer id only can include number, letter and
	 * underline
	 *
	 * @return
	 */
	protected boolean __validateViewerId() {
		Pattern p = Pattern.compile("^\\w+$"); //$NON-NLS-1$
		Matcher m = p.matcher(viewer.getId());
		return m.find();
	}

	/**
	 * Handle event before doEndTag
	 */
	@Override
	protected void __beforeEndTag() {
		super.__beforeEndTag();
		viewer.setParameters(parameters);

		// Save viewer id
		pageContext.setAttribute(viewer.getId(), viewer.getId());

		// Save has HostPage
		if (viewer.isHostPage()) {
			pageContext.setAttribute(ATTR_HOSTPAGE, "true"); //$NON-NLS-1$
		}
	}

	/**
	 * Handle use IFrame to preview report. Each IFrame should have an unique id.
	 *
	 * @param src
	 * @param target
	 * @throws Exception
	 */
	protected void __handleIFrame(String src, String target) throws Exception {
		JspWriter writer = pageContext.getOut();

		// prepare parameters
		String paramContainerId = "params_" + viewer.getId(); //$NON-NLS-1$
		writer.write("<div id=\"" + paramContainerId + "\" style='display:none'>\n"); //$NON-NLS-1$ //$NON-NLS-2$

		Iterator it = viewer.getParameters().values().iterator();
		while (it.hasNext()) {
			ParameterField param = (ParameterField) it.next();

			// get parameter name
			String encParamName = ParameterAccessor.htmlEncode(param.getName());

			Collection<Object> values = param.getValues();

			boolean allValuesAreStrings = true;
			for (Object value : values) {
				if (!(value instanceof String)) {
					allValuesAreStrings = false;
				}
				// parse parameter object as standard format
				String paramValue = DataUtil.getDisplayValue(value, timeZone);

				// set NULL parameter
				if (paramValue == null) {
					writer.write("<input type = 'hidden' name=\"" + ParameterAccessor.PARAM_ISNULL + "\" \n"); //$NON-NLS-1$ //$NON-NLS-2$
					writer.write(" value=\"" + encParamName + "\">\n"); //$NON-NLS-1$//$NON-NLS-2$
					continue;
				}

				// set Parameter value
				writer.write("<input type = 'hidden' name=\"" + encParamName + "\" \n"); //$NON-NLS-1$ //$NON-NLS-2$
				writer.write(" value=\"" + paramValue + "\">\n"); //$NON-NLS-1$//$NON-NLS-2$
			}

			// if value is string/string[], check whether set isLocale flag
			if (!values.isEmpty() && allValuesAreStrings) {

				writer.write("<input type = 'hidden' name=\"" + ParameterAccessor.PARAM_ISLOCALE + "\" \n"); //$NON-NLS-1$ //$NON-NLS-2$
				writer.write(" value=\"" + encParamName + "\">\n"); //$NON-NLS-1$//$NON-NLS-2$
			}

			// set parameter pattern format
			if (param.getPattern() != null) {
				writer.write("<input type = 'hidden' name=\"" + encParamName + "_format\" \n"); //$NON-NLS-1$ //$NON-NLS-2$
				writer.write(" value=\"" + param.getPattern() + "\">\n"); //$NON-NLS-1$//$NON-NLS-2$
			}

			// set parameter display text
			for (String displayText : param.getDisplayTexts()) {
				if (displayText != null) {
					writer.write("<input type = 'hidden' name=\"" + ParameterAccessor.PREFIX_DISPLAY_TEXT + encParamName //$NON-NLS-1$
							+ "\" \n"); //$NON-NLS-1$
					writer.write(" value=\"" + displayText + "\">\n"); //$NON-NLS-1$//$NON-NLS-2$
				}
			}
		}

		writer.write("</div>\n"); //$NON-NLS-1$

		// create form
		String formId = "form_" + viewer.getId(); //$NON-NLS-1$
		writer.write("<form id=\"" + formId + "\" method=\"post\"></form>\n"); //$NON-NLS-1$ //$NON-NLS-2$

		writer.write("<script type=\"text/javascript\">\n"); //$NON-NLS-1$
		writer.write("function loadViewer" + viewer.getId() + "(){\n"); //$NON-NLS-1$//$NON-NLS-2$
		writer.write("var formObj = document.getElementById( \"" + formId + "\" );\n"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.write("var paramContainer = document.getElementById(\"" //$NON-NLS-1$
				+ paramContainerId + "\");\n"); //$NON-NLS-1$
		writer.write("var oParams = paramContainer.getElementsByTagName('input');\n"); //$NON-NLS-1$
		writer.write("if( oParams )\n"); //$NON-NLS-1$
		writer.write("{\n"); //$NON-NLS-1$
		writer.write("  for( var i=0;i<oParams.length;i++ )  \n"); //$NON-NLS-1$
		writer.write("  {\n"); //$NON-NLS-1$
		writer.write("    var param = document.createElement( \"INPUT\" );\n"); //$NON-NLS-1$
		writer.write("    param.type = \"HIDDEN\";\n"); //$NON-NLS-1$
		writer.write("    param.name= oParams[i].name;\n"); //$NON-NLS-1$
		writer.write("    param.value= oParams[i].value;\n"); //$NON-NLS-1$
		writer.write("    formObj.appendChild( param );\n"); //$NON-NLS-1$
		writer.write("  }\n"); //$NON-NLS-1$
		writer.write("}\n"); //$NON-NLS-1$

		writer.write("formObj.action = \"" + src + "\";\n"); //$NON-NLS-1$ //$NON-NLS-2$
		if (target != null) {
			writer.write("formObj.target = \"" + target + "\";\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		writer.write("formObj.submit( );\n"); //$NON-NLS-1$
		writer.write("}\n"); //$NON-NLS-1$
		writer.write("</script>\n"); //$NON-NLS-1$

		// write IFrame object
		writer.write(__handleIFrameDefinition());

		writer.write("<script type=\"text/javascript\">"); //$NON-NLS-1$
		writer.write("loadViewer" + viewer.getId() + "();"); //$NON-NLS-1$//$NON-NLS-2$
		writer.write("</script>\n"); //$NON-NLS-1$
	}

	/**
	 * Handle IFrame definition
	 *
	 * @return
	 */
	protected String __handleIFrameDefinition() {
		// create IFrame object
		StringBuilder iframe = new StringBuilder("<iframe name=\"").append(viewer.getId() //$NON-NLS-1$
		).append("\" frameborder=\"").append(viewer.getFrameborder()).append("\" "); //$NON-NLS-1$ //$NON-NLS-2$

		if (viewer.getScrolling() != null) {
			iframe.append(" scrolling = \"").append(viewer.getScrolling()).append("\" "); //$NON-NLS-1$ //$NON-NLS-2$
		}

		iframe.append(__handleAppearance()).append("></iframe>\r\n"); //$NON-NLS-1$

		return iframe.toString();
	}

	/**
	 * IFrame Appearance style
	 *
	 * @return
	 */
	protected String __handleAppearance() {
		StringBuilder style = new StringBuilder(" style='"); //$NON-NLS-1$

		// position
		if (viewer.getPosition() != null) {
			style.append("position:").append(viewer.getPosition()).append(";"); //$NON-NLS-1$//$NON-NLS-2$
		}

		// height
		if (viewer.getHeight() >= 0) {
			style.append("height:").append(viewer.getHeight()).append("px;"); //$NON-NLS-1$//$NON-NLS-2$
		}

		// width
		if (viewer.getWidth() >= 0) {
			style.append("width:").append(viewer.getWidth()).append("px;"); //$NON-NLS-1$//$NON-NLS-2$
		}

		// top
		if (viewer.getTop() != null) {
			style.append("top:").append(viewer.getTop()).append("px;"); //$NON-NLS-1$//$NON-NLS-2$
		}

		// left
		if (viewer.getLeft() != null) {
			style.append("left:").append(viewer.getLeft()).append("px;"); //$NON-NLS-1$//$NON-NLS-2$
		}

		// style
		if (viewer.getStyle() != null) {
			style.append(viewer.getStyle()).append(";"); //$NON-NLS-1$
		}

		style.append("' "); //$NON-NLS-1$

		return style.toString();
	}

	/**
	 * Add parameter into list
	 *
	 * @param field
	 */
	public void addParameter(ParameterField field) {
		if (field != null) {
			parameters.put(field.getName(), field);
		}
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(String id) {
		viewer.setId(id);
	}

	/**
	 * @param baseURL the baseURL to set
	 */
	public void setBaseURL(String baseURL) {
		viewer.setBaseURL(baseURL);
	}

	/**
	 * @param isHostPage the isHostPage to set
	 */
	public void setIsHostPage(String isHostPage) {
		viewer.setHostPage(Boolean.parseBoolean(isHostPage));
	}

	/**
	 * @param scrolling the scrolling to set
	 */
	public void setScrolling(String scrolling) {
		viewer.setScrolling(scrolling);
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		viewer.setPosition(position);
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		viewer.setStyle(style);
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		viewer.setHeight(Integer.parseInt(height));
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		viewer.setWidth(Integer.parseInt(width));
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(String left) {
		viewer.setLeft("" + Integer.parseInt(left)); //$NON-NLS-1$
	}

	/**
	 * @param top the top to set
	 */
	public void setTop(String top) {
		viewer.setTop("" + Integer.parseInt(top)); //$NON-NLS-1$
	}

	/**
	 * @param frameborder the frameborder to set
	 */
	public void setFrameborder(String frameborder) {
		viewer.setFrameborder(frameborder);
	}

	/**
	 * @param reportDesign the reportDesign to set
	 */
	public void setReportDesign(String reportDesign) {
		viewer.setReportDesign(reportDesign);
	}

	/**
	 * @param reportDocument the reportDocument to set
	 */
	public void setReportDocument(String reportDocument) {
		viewer.setReportDocument(reportDocument);
	}

	/**
	 * @param bookmark the bookmark to set
	 */
	public void setBookmark(String bookmark) {
		viewer.setBookmark(bookmark);
	}

	/**
	 * @param reportletId the reportletId to set
	 */
	public void setReportletId(String reportletId) {
		viewer.setReportletId(reportletId);
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		viewer.setLocale(locale);
	}

	/**
	 * @param timeZone the time zone to set
	 */
	public void setTimeZone(String timeZone) {
		viewer.setTimeZone(timeZone);
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		viewer.setFormat(format);
	}

	/**
	 *
	 * @param emitterId the emitter id to set
	 */
	public void setEmitterId(String emitterId) {
		viewer.setEmitterId(emitterId);
	}

	/**
	 *
	 * @param pageOverflow page overflow
	 */
	public void setPageOverflow(String pageOverflow) {
		viewer.setPageOverflow(pageOverflow);
	}

	/**
	 * @param svg the svg to set
	 */
	public void setSvg(String svg) {
		viewer.setSvg(BirtTagUtil.convertBooleanValue(svg));
	}

	/**
	 * @param rtl the rtl to set
	 */
	public void setRtl(String rtl) {
		viewer.setRtl(BirtTagUtil.convertBooleanValue(rtl));
	}

	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(String pageNum) {
		viewer.setPageNum(Long.parseLong(pageNum));
	}

	/**
	 * @param pageRange the pageRange to set
	 */
	public void setPageRange(String pageRange) {
		viewer.setPageRange(pageRange);
	}

	/**
	 * @param showParameterPage the showParameterPage to set
	 */
	public void setShowParameterPage(String showParameterPage) {
		viewer.setShowParameterPage(showParameterPage);
	}

	/**
	 * @param resourceFolder the resourceFolder to set
	 */
	public void setResourceFolder(String resourceFolder) {
		viewer.setResourceFolder(resourceFolder);
	}

	/**
	 * @param isReportlet the isReportlet to set
	 */
	public void setIsReportlet(String isReportlet) {
		viewer.setIsReportlet(BirtTagUtil.convertBooleanValue(isReportlet));
	}
}
