/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

public class PageVariableDesign {

	public final static String SCOPE_REPORT = "report";
	public final static String SCOPE_PAGE = "page";

	String name;
	String scope;
	Expression defaultValue;

	public PageVariableDesign(String name, String scope) {
		this.name = name;
		this.scope = scope;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Expression getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Expression defaultValue) {
		this.defaultValue = defaultValue;
	}
}
