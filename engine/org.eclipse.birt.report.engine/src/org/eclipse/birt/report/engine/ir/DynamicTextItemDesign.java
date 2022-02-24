/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ir;

/**
 * 
 */
public class DynamicTextItemDesign extends ReportItemDesign {
	/**
	 * content type must be one of: html, plain, rtf or auto.
	 */
	protected String contentType;
	/**
	 * content
	 */
	protected Expression content;
	private boolean jTidy = true;

	/**
	 * @return Returns the content.
	 */
	public Expression getContent() {
		return content;
	}

	/**
	 * @param content The content to set.
	 */
	public void setContent(Expression content) {
		this.content = content;
	}

	/**
	 * @return Returns the contentType.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType The contentType to set.
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.ir.ReportItemDesign#accept(org.eclipse.birt.
	 * report.engine.ir.ReportItemVisitor)
	 */
	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitDynamicTextItem(this, value);
	}

	public boolean isJTidy() {
		return jTidy;
	}

	public void setJTidy(boolean jTidy) {
		this.jTidy = jTidy;
	}

}
