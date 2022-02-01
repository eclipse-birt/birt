/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.extension.internal;

import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.engine.IRenderContext;
import org.eclipse.birt.report.engine.script.internal.ReportContextImpl;

public class RenderContext extends ReportContextImpl implements IRenderContext {

	public RenderContext(ExecutionContext context) {
		super(context);
	}

	public IReportDocument getReportDocument() {
		return context.getReportDocument();
	}

	public IReportContent getReportContent() {
		return context.getReportContent();
	}

}
