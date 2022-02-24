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
import org.eclipse.birt.report.engine.content.ILabelContent;

public class LabelContent extends TextContent implements ILabelContent {
	ILabelContent labelContent;

	public LabelContent(ILabelContent content) {
		super(content);
		labelContent = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.
	 * eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitLabel(this, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ILabelContent#getHelpKey()
	 */
	public String getHelpKey() {
		return labelContent.getHelpKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ILabelContent#getLabelKey()
	 */
	public String getLabelKey() {
		return labelContent.getLabelKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ILabelContent#getLabelText()
	 */
	public String getLabelText() {
		return labelContent.getLabelText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.ILabelContent#setLabelKey(java.lang.
	 * String)
	 */
	public void setLabelKey(String labelKey) {
		labelContent.setLabelKey(labelKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.ILabelContent#setLabelText(java.lang.
	 * String)
	 */
	public void setLabelText(String labelText) {
		labelContent.setLabelText(labelText);
	}

}
