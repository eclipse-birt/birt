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

package org.eclipse.birt.report.engine.ir;

/**
 * AutoText.
 * 
 */
public class AutoTextItemDesign extends ReportItemDesign {

	/**
	 * text content.
	 */
	protected String text;

	/**
	 * text resource key
	 */
	protected String textKey;

	/**
	 * the auto text type
	 */
	protected String type;

	public AutoTextItemDesign() {
	}

	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitAutoTextItem(this, value);
	}

	/**
	 * get text content
	 * 
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * set text content
	 * 
	 * @param text The text to set.
	 */
	public void setText(String textKey, String text) {
		this.textKey = textKey;
		this.text = text;
	}

	/**
	 * @return Returns the id.
	 */
	public String getTextKey() {
		return textKey;
	}

	/**
	 * set autoText type
	 * 
	 * @param type The autoText type.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
}
