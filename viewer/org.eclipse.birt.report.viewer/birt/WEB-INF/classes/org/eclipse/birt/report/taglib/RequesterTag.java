/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.taglib;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.session.ViewingSessionUtil;
import org.eclipse.birt.report.taglib.util.BirtTagUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * @author Administrator
 * 
 */
public class RequesterTag extends AbstractBaseTag {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4360776653926113953L;

	/**
	 * Cache the parameter values defined in paramDef tag
	 */
	private Map parameters;

	private Boolean validated;

	/**
	 * Then entry to initialize tag
	 * 
	 * @throws Exception
	 */
	public void __init() {
		super.__init();

		parameters = new HashMap();
		validated = null;
	}

	/**
	 * When reach the start tag, fire this operation If set isCustom as true, use
	 * FORM to create user-defined parameter page
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		try {

			// TODO: move this logic in AbstractBaseTag if possible
			if (__validate()) {
				if (viewer.isCustom()) {
					String viewingSessionId = ViewingSessionUtil
							.createSession((HttpServletRequest) pageContext.getRequest()).getId();
					JspWriter writer = pageContext.getOut();

					// create DIV object to contain requester page
					writer.write("<DIV "); //$NON-NLS-1$
					if (viewer.getId() != null)
						writer.write(" ID=\"" + viewer.getId() + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
					writer.write(__handleDivAppearance() + ">\n"); //$NON-NLS-1$

					// create form
					writer.write("<FORM NAME=\"" + viewer.getName() + "\" METHOD=\"post\" "); //$NON-NLS-1$ //$NON-NLS-2$
					writer.write(" action=\"" + viewer.createURI(null, viewingSessionId) + "\""); //$NON-NLS-1$ //$NON-NLS-2$
					if (viewer.getTarget() != null)
						writer.write(" target=\"" + viewer.getTarget() + "\""); //$NON-NLS-1$//$NON-NLS-2$
					writer.write(">\n"); //$NON-NLS-1$
				}
			} else {
				return SKIP_BODY;
			}
		} catch (Exception e) {
			__handleException(e);
			return SKIP_BODY;
		}

		return EVAL_PAGE;
	}

	/**
	 * Process only if the attributes are validated. Overrides default doEndTag().
	 */
	public int doEndTag() throws JspException {
		try {
			if (__validate()) {
				__beforeEndTag();
				__process();
			} else {
				return SKIP_BODY;
			}
		} catch (Exception e) {
			__handleException(e);
			return SKIP_BODY;
		}
		return EVAL_PAGE;
	}

	/**
	 * Validate the tag. *
	 * 
	 * @see org.eclipse.birt.report.taglib.AbstractBaseTag#__validate()
	 */
	public boolean __validate() throws Exception {
		if (validated != null) {
			return validated.booleanValue();
		}
		validated = Boolean.FALSE;

		// get Locale
		Locale locale = BirtTagUtil.getLocale((HttpServletRequest) pageContext.getRequest(), viewer.getLocale());

		// Set locale information
		BirtResources.setLocale(locale);

		// Validate requester id
		if (viewer.getId() == null || viewer.getId().length() <= 0) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_NO_ATTR_ID));
		}

		if (!__validateRequesterId()) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_INVALID_ATTR_ID));
		}

		// validate the viewer id if unique
		if (pageContext.findAttribute(viewer.getId()) != null) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_ATTR_ID_DUPLICATE));
		}

		// Validate requester name
		// if isCustom is true, the requester name should be required.
		if (viewer.isCustom() && (viewer.getName() == null || viewer.getName().length() <= 0)) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_NO_REQUESTER_NAME));
		}

		// Report design or document should be specified
		if (viewer.getReportDesign() == null && viewer.getReportDocument() == null) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_NO_REPORT_SOURCE));
		}

		// If preview reportlet, report document file should be specified.
		if (viewer.getReportletId() != null && viewer.getReportDocument() == null) {
			throw new JspTagException(BirtResources.getMessage(ResourceConstants.TAGLIB_NO_REPORT_DOCUMENT));
		}

		validated = Boolean.TRUE;
		return true;
	}

	/**
	 * Validate the requester id. Requester id only can include number, letter and
	 * underline
	 * 
	 * @return
	 */
	protected boolean __validateRequesterId() {
		Pattern p = Pattern.compile("^\\w+$"); //$NON-NLS-1$
		Matcher m = p.matcher(viewer.getId());
		return m.find();
	}

	/**
	 * Handle event before doEndTag
	 */
	protected void __beforeEndTag() {
		// Save requester id
		pageContext.setAttribute(viewer.getId(), viewer.getId());
	}

	/**
	 * process tag function
	 * 
	 * @see org.eclipse.birt.report.taglib.AbstractBaseTag#__process()
	 */
	public void __process() throws Exception {
		if (viewer.isCustom()) {
			JspWriter writer = pageContext.getOut();
			writer.write("</form>\n"); //$NON-NLS-1$
			writer.write("</div>\n"); //$NON-NLS-1$
		} else {
			// If isCustom is false, use BIRT parameter page
			__handleIFrame();
		}
	}

	/**
	 * Handle use IFrame to show parameter page.
	 * 
	 * @throws Exception
	 */
	protected void __handleIFrame() throws Exception {
		JspWriter writer = pageContext.getOut();

		// create IFrame object
		String iframe = "<iframe "; //$NON-NLS-1$
		if (viewer.getId() != null)
			iframe += " id=\"" + viewer.getId() + "\" ";//$NON-NLS-1$ //$NON-NLS-2$

		// name
		if (viewer.getName() != null)
			iframe += " name=\"" + viewer.getName() + "\" ";//$NON-NLS-1$ //$NON-NLS-2$

		// src, force "__cache" as false
		String src = viewer.createURI(IBirtConstants.VIEWER_PARAMETER, null) + "&" //$NON-NLS-1$
				+ ParameterAccessor.PARAM_NOCACHE_PARAMETER;
		iframe += " src=\"" + src + "\" "; //$NON-NLS-1$ //$NON-NLS-2$

		// border
		iframe += " frameborder=\"" + viewer.getFrameborder() + "\" "; //$NON-NLS-1$ //$NON-NLS-2$

		// scrolling
		if (viewer.getScrolling() != null)
			iframe += " scrolling = \"" + viewer.getScrolling() + "\" "; //$NON-NLS-1$ //$NON-NLS-2$

		iframe += __handleAppearance() + "></iframe>\r\n"; //$NON-NLS-1$

		writer.write(iframe);
	}

	/**
	 * IFRAME Appearance style
	 * 
	 * @return
	 */
	protected String __handleAppearance() {
		String style = " style='"; //$NON-NLS-1$

		// style
		if (viewer.getStyle() != null)
			style += viewer.getStyle() + ";"; //$NON-NLS-1$

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

		style += "' "; //$NON-NLS-1$

		return style;
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
		if (viewer.getScrolling() != null && SCROLLING_YES.equalsIgnoreCase(viewer.getScrolling())) {
			style = style + "overflow:scroll"; //$NON-NLS-1$
		} else {
			style = style + "overflow:auto"; //$NON-NLS-1$
		}

		// style
		if (viewer.getStyle() != null)
			style += viewer.getStyle() + ";"; //$NON-NLS-1$

		style += "' "; //$NON-NLS-1$

		return style;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		viewer.setId(id);
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		viewer.setName(name);
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		viewer.setTitle(title);
	}

	/**
	 * @param baseURL the baseURL to set
	 */
	public void setBaseURL(String baseURL) {
		viewer.setBaseURL(baseURL);
	}

	/**
	 * @param isCustom the isCustom to set
	 */
	public void setIsCustom(String isCustom) {
		viewer.setCustom(Boolean.valueOf(isCustom).booleanValue());
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
	 * @param reportletId the reportletId to set
	 */
	public void setReportletId(String reportletId) {
		viewer.setReportletId(reportletId);
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		viewer.setPattern(pattern);
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		viewer.setTarget(target);
	}

	/**
	 * @param bookmark the bookmark to set
	 */
	public void setBookmark(String bookmark) {
		viewer.setBookmark(bookmark);
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
	 * @param resourceFolder the resourceFolder to set
	 */
	public void setResourceFolder(String resourceFolder) {
		viewer.setResourceFolder(resourceFolder);
	}

	/**
	 * @param forceOverwriteDocument the forceOverwriteDocument to set
	 */
	public void setForceOverwriteDocument(String forceOverwriteDocument) {
		viewer.setForceOverwriteDocument(BirtTagUtil.convertBooleanValue(forceOverwriteDocument));
	}

	/**
	 * @param showTitle the showTitle to set
	 */
	public void setShowTitle(String showTitle) {
		viewer.setShowTitle(BirtTagUtil.convertBooleanValue(showTitle));
	}

	/**
	 * @param showToolBar the showToolBar to set
	 */
	public void setShowToolBar(String showToolBar) {
		viewer.setShowToolBar(BirtTagUtil.convertBooleanValue(showToolBar));
	}

	/**
	 * @param showNavigationBar the showNavigationBar to set
	 */
	public void setShowNavigationBar(String showNavigationBar) {
		viewer.setShowNavigationBar(BirtTagUtil.convertBooleanValue(showNavigationBar));
	}

	/**
	 * @param isReportlet the isReportlet to set
	 */
	public void setIsReportlet(String isReportlet) {
		viewer.setIsReportlet(BirtTagUtil.convertBooleanValue(isReportlet));
	}

	/**
	 * Add parameter value
	 * 
	 * @param name
	 * @param value
	 */
	public void addParameter(String name, Object value) {
		parameters.put(name, value);
	}

	/**
	 * @return the parameters
	 */
	public Map getParameters() {
		return parameters;
	}
}
