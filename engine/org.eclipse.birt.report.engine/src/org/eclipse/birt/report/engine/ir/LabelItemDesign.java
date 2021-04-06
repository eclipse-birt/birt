/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

/**
 * Label.
 * 
 */
public class LabelItemDesign extends ReportItemDesign {

	/**
	 * text content.
	 */
	protected String text;

	/**
	 * text resource key
	 */
	protected String textKey;

	/**
	 * help text
	 */
	protected String helpText;
	/**
	 * help text resource key
	 */
	protected String helpTextKey;

	public LabelItemDesign() {
	}

	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitLabelItem(this, value);
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
	 * set the help info.
	 * 
	 * @param key  resource key
	 * @param text text content
	 */
	public void setHelpText(String key, String text) {
		this.helpTextKey = key;
		this.helpText = text;
	}

	/**
	 * get the help text property.
	 * 
	 * @return help text
	 */
	public String getHelpText() {
		return this.helpText;
	}

	/**
	 * get the help text resource key property.
	 * 
	 * @return resource key of the help text
	 */
	public String getHelpTextKey() {
		return this.helpTextKey;
	}

}
