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

public class TemplateDesign extends ReportItemDesign {
	String promptText;
	String promptTextKey;
	String allowedType;

	public void setPromptText(String text) {
		promptText = text;
	}

	public String getPromptText() {
		return promptText;
	}

	public String getPromptTextKey() {
		return promptTextKey;
	}

	public void setPromptTextKey(String key) {
		promptTextKey = key;
	}

	@Override
	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitTemplate(this, value);
	}

	public void setAllowedType(String allowedType) {
		this.allowedType = allowedType;
	}

	public String getAllowedType() {
		return this.allowedType;
	}
}
