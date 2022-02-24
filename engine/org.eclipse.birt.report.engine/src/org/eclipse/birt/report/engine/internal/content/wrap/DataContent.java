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

package org.eclipse.birt.report.engine.internal.content.wrap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;

public class DataContent extends TextContent implements IDataContent {
	IDataContent dataContent;

	public DataContent(IDataContent content) {
		super(content);
		dataContent = content;

	}

	public Object getValue() {
		return dataContent.getValue();
	}

	public void setValue(Object value) {
		this.dataContent.setValue(value);
	}

	public String getLabelText() {
		return dataContent.getLabelText();
	}

	public void setLabelText(String text) {
		this.dataContent.setLabelText(text);
		;
	}

	public String getLabelKey() {
		return dataContent.getLabelKey();
	}

	public void setLabelKey(String key) {
		dataContent.setLabelKey(key);
		;
	}

	public String getHelpText() {
		return dataContent.getHelpText();
	}

	public String getHelpKey() {
		return dataContent.getHelpKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.
	 * eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitData(this, value);
	}
}
