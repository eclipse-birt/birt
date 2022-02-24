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
package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IDynamicText;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class DynamicText extends ReportItem implements IDynamicText {

	public DynamicText(TextDataHandle textData) {
		super(textData);
	}

	public DynamicText(org.eclipse.birt.report.model.api.simpleapi.IDynamicText dynamicTextImpl) {
		super(dynamicTextImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITextData#getValueExpr()
	 */

	public String getValueExpr() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IDynamicText) designElementImpl).getValueExpr();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITextData#setValueExpr(java
	 * .lang.String)
	 */

	public void setValueExpr(String expr) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IDynamicText) designElementImpl).setValueExpr(expr);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITextData#getContentType()
	 */

	public String getContentType() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IDynamicText) designElementImpl).getContentType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITextData#setContentType(
	 * java.lang.String)
	 */

	public void setContentType(String contentType) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IDynamicText) designElementImpl).setContentType(contentType);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}
}
