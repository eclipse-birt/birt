/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs.parameters;

import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

/**
 * ReportHyperlinkParameter
 */
public class ReportHyperlinkParameter extends AbstractHyperlinkParameter implements IReportHyperlinkParameter {

	private ParameterHandle handle;

	public ReportHyperlinkParameter(ParameterHandle handle) {
		this.handle = handle;
	}

	public String getName() {
		return handle.getName();
	}

	public String getDataType() {
		if (handle instanceof ScalarParameterHandle) {
			return ((ScalarParameterHandle) handle).getDataType();
		}
		return null;
	}

	public ParameterHandle getParameterHandle() {
		return handle;
	}

}
