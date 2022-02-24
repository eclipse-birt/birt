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

/**
 * This tag is used to specify the report parameter.
 * 
 */
public class ParamValueTag extends BodyTagSupport {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4881711038987308895L;

	/**
	 * Parameter value fields
	 */
	private ParamValueField param;

	/**
	 * Initialize pageContext
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#setPageContext(javax.servlet.jsp.PageContext)
	 */
	public void setPageContext(PageContext context) {
		super.setPageContext(context);
		param = new ParamValueField();
	}

	/**
	 * When reach the end tag, fire this operation
	 * 
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException {
		// included in viewer tag
		ParamTag paramTag = (ParamTag) TagSupport.findAncestorWithClass(this, ParamTag.class);
		if (paramTag != null) {
			if (bodyContent != null) {
				String bodyString = bodyContent.getString();
				if (bodyString != null) {
					bodyString = bodyString.trim();
					if (!"".equals(bodyString)) {
						// replace the value attribute with the content, if empty
						if (param.getValue() == null || "".equals(param.getValue())) {
							param.setValue(bodyString);
						}
					}
				}
			}
			paramTag.addValue(param);
		}
		return super.doEndTag();
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

}
