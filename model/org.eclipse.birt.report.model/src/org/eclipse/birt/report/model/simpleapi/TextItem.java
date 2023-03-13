/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.ITextItem;
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;

public class TextItem extends ReportItem implements ITextItem {

	public TextItem(TextItemHandle text) {
		super(text);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.ITextItem#getContent()
	 */

	@Override
	public String getContent() {
		return ((TextItemHandle) handle).getContent();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITextItem#getDisplayContent
	 * ()
	 */

	@Override
	public String getDisplayContent() {
		return ((TextItemHandle) handle).getDisplayContent();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.ITextItem#setContent
	 * (java.lang.String)
	 */

	@Override
	public void setContent(String value) throws SemanticException {
		setProperty(ITextItemModel.CONTENT_PROP, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITextItem#getContentType ()
	 */

	@Override
	public String getContentType() {
		return ((TextItemHandle) handle).getContentType();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITextItem#setContentType
	 * (java.lang.String)
	 */

	@Override
	public void setContentType(String contentType) throws SemanticException {
		setProperty(ITextItemModel.CONTENT_TYPE_PROP, contentType);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITextItem#getContentKey ()
	 */

	@Override
	public String getContentKey() {
		return ((TextItemHandle) handle).getContentKey();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITextItem#setContentKey
	 * (java.lang.String)
	 */

	@Override
	public void setContentKey(String resourceKey) throws SemanticException {
		setProperty(ITextItemModel.CONTENT_RESOURCE_KEY_PROP, resourceKey);
	}
}
