/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.internal.content.wrap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.ITextContent;

/**
 * Handle of the text content of an element
 *
 * @since 3.3
 *
 */
public class TextContent extends AbstractContentWrapper implements ITextContent {
	ITextContent textContent;

	/**
	 * Constructor
	 *
	 * @param content text content of the element
	 */
	public TextContent(ITextContent content) {
		super(content);
		textContent = content;
	}

	@Override
	public String getText() {
		return textContent.getText();
	}

	@Override
	public void setText(String text) {
		textContent.setText(text);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.
	 * eclipse.birt.report.engine.content.IContentVisitor)
	 */
	@Override
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitText(this, value);
	}
}
