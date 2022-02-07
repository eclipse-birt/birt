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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.eclipse.birt.report.taglib.component.ParamValueField;
import org.eclipse.birt.report.taglib.component.ParameterField;

/**
 * This tag is used to specify the report parameter.
 * 
 */
public class ParamTag extends BodyTagSupport {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4881711038987308895L;

	/**
	 * Parameter Definition
	 */
	private ParameterField param;

	/**
	 * Initialize pageContext
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#setPageContext(javax.servlet.jsp.PageContext)
	 */
	public void setPageContext(PageContext context) {
		super.setPageContext(context);
		param = new ParameterField();
	}

	/**
	 * When reach the end tag, fire this operation
	 * 
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException {
		if (param.validate()) {
			// included in viewer tag
			AbstractViewerTag viewerTag = (AbstractViewerTag) TagSupport.findAncestorWithClass(this,
					AbstractViewerTag.class);
			if (viewerTag != null)
				viewerTag.addParameter(param);
		}
		return super.doEndTag();
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		param.setName(name);
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		param.setPattern(pattern);
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		param.setValue(value);
	}

	/**
	 * @param displayText the displayText to set
	 */
	public void setDisplayText(String displayText) {
		param.setDisplayText(displayText);
	}

	/**
	 * @param isLocale the isLocale to set
	 */
	public void setIsLocale(String isLocale) {
		param.setLocale(isLocale);
	}

	/**
	 * @param delim delimiter
	 */
	public void setDelim(String delim) {
		param.setDelim(delim);
	}

	public void addValue(ParamValueField valueField) {
		param.addValue(valueField);
	}
}
