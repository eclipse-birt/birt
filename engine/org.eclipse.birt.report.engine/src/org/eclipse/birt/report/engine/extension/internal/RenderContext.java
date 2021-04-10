/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
