/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IDynamicText;
import org.eclipse.birt.report.model.elements.interfaces.ITextDataItemModel;

public class DynamicText extends ReportItem implements IDynamicText {

	public DynamicText(TextDataHandle textData) {
		super(textData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITextData#getValueExpr
	 * ()
	 */

	public String getValueExpr() {
		return ((TextDataHandle) handle).getValueExpr();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITextData#setValueExpr
	 * (java.lang.String)
	 */

	public void setValueExpr(String expr) throws SemanticException {
		setProperty(ITextDataItemModel.VALUE_EXPR_PROP, expr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITextData#getContentType ()
	 */

	public String getContentType() {
		return ((TextDataHandle) handle).getContentType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITextData#setContentType
	 * (java.lang.String)
	 */

	public void setContentType(String contentType) throws SemanticException {
		setProperty(ITextDataItemModel.CONTENT_TYPE_PROP, contentType);
	}
}
