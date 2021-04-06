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
