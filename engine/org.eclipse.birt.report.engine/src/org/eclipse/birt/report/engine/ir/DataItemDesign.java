/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
 * DataItem.
 * 
 * reference to report design schema.
 * 
 */
public class DataItemDesign extends ReportItemDesign {

	/**
	 * binding column used by this item.
	 */
	protected String bindingColumn;

	/**
	 * help text
	 */
	protected String helpText;
	/**
	 * help text resource key
	 */
	protected String helpTextKey;

	protected boolean suppressDuplicate = false;

	/**
	 * default constructor. create an empty expression.
	 */
	public DataItemDesign() {
	}

	/**
	 * get the binding column used by this item.
	 */
	public String getBindingColumn() {
		return this.bindingColumn;
	}

	/**
	 * set the binding column of the item. the value expression will be setted
	 * either.
	 * 
	 * @param column
	 */
	public void setBindingColumn(String column) {
		this.bindingColumn = column;
	}

	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitDataItem(this, value);
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

	public void setSuppressDuplicate(boolean suppress) {
		suppressDuplicate = suppress;
	}

	public boolean getSuppressDuplicate() {
		return suppressDuplicate;
	}
}
