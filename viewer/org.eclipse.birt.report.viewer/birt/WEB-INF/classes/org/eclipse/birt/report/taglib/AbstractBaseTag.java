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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.eclipse.birt.report.taglib.component.ViewerField;

/**
 * Abstract base tag. All BIRT tags should extend this base class.
 * 
 */
public abstract class AbstractBaseTag extends TagSupport implements ITagConstants {

	private static final long serialVersionUID = 1L;
	/**
	 * Object that specifies tag supported attributes
	 */
	public ViewerField viewer;

	/**
	 * Then entry to validate tag
	 * 
	 * @throws Exception
	 */
	public abstract boolean __validate() throws Exception;

	/**
	 * Then entry to process tag
	 * 
	 * @throws Exception
	 */
	public abstract void __process() throws Exception;

	/**
	 * Default constructor
	 */
	public AbstractBaseTag() {
	}

	/**
	 * Then entry to initialize tag
	 * 
	 * @throws Exception
	 */
	public void __init() {
		viewer = new ViewerField();

		// initialize context root
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		viewer.setBaseURL(req.getContextPath());
	}

	/**
	 * Initialize pageContext
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#setPageContext(javax.servlet.jsp.PageContext)
	 */
	public void setPageContext(PageContext context) {
		super.setPageContext(context);
		this.__init();
	}

	/**
	 * When reach the start tag, fire this operation
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		return EVAL_PAGE;
	}

	/**
	 * When reach the end tag, fire this operation
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException {
		try {
			if (__validate()) {
				__beforeEndTag();
				__process();
			}

		} catch (Exception e) {
			__handleException(e);
		}
		return EVAL_PAGE;
	}

	/**
	 * Handle event before doEndTag
	 */
	protected void __beforeEndTag() {
	}

	/**
	 * Handle Exception
	 * 
	 * @param e
	 * @throws JspException
	 */
	protected void __handleException(Exception e) throws JspException {
		JspWriter writer = pageContext.getOut();
		try {
			writer.write("<font color='red'>"); //$NON-NLS-1$
			writer.write(e.getMessage());
			writer.write("</font>"); //$NON-NLS-1$
		} catch (IOException err) {
			throw new JspException(err);
		}
	}
}
