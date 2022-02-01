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

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.data.dte.DocumentDataSource;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.engine.IRunContext;
import org.eclipse.birt.report.engine.script.internal.ReportContextImpl;

public class RunContext extends ReportContextImpl implements IRunContext {

	public RunContext(ExecutionContext context) {
		super(context);
	}

	public IReportDocument getReportDocument() {
		return context.getReportDocument();
	}

	public IDocArchiveWriter getWriter() {
		ReportDocumentWriter writer = context.getReportDocWriter();
		if (writer != null) {
			return writer.getArchive();
		}
		return null;
	}

	public IReportContent getReportContent() {
		return context.getReportContent();
	}

	public IDocArchiveReader getDataSource() {
		DocumentDataSource dataSource = context.getDataSource();
		if (dataSource != null) {
			return dataSource.getDataSource();
		}
		IReportDocument document = context.getReportDocument();
		if (document != null) {
			return document.getArchive();
		}
		return null;
	}

	public boolean isProgressiveViewingEnable() {
		return context.isProgressiveViewingEnable();
	}

	public ExecutionContext getExecutionContext() {
		return context;
	}
}
