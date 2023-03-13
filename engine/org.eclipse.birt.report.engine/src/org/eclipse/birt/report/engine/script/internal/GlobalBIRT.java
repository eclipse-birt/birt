/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.mozilla.javascript.ScriptableObject;

public class GlobalBIRT extends ScriptableObject {

	private static final long serialVersionUID = -2660909218558681397L;
	protected ExecutionContext context;

	public GlobalBIRT() {
	}

	public GlobalBIRT(ExecutionContext context) {
		this.context = context;
	}

	@Override
	public String getClassName() {
		return "GlobalBIRT";
	}

	public int jsGet_CurrentPage() {
		if (context != null) {
			return (int) context.getPageNumber();
		}
		return 0;
	}

	public int jsGet_TotalPage() {
		if (context != null) {
			return (int) context.getTotalPage();
		}
		return 0;
	}
}
