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

package org.eclipse.birt.report.designer.core.model.views.data;

import org.eclipse.birt.report.model.api.ModuleHandle;

public class ReportDataHandle {

	private ModuleHandle module;

	public ReportDataHandle(ModuleHandle module) {
		this.module = module;
	}

	public ModuleHandle getModuleHandle() {
		return this.module;
	}
}
