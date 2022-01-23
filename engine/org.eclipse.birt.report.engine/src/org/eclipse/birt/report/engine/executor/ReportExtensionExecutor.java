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

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.engine.IContentProcessor;
import org.eclipse.birt.report.engine.internal.executor.wrap.WrappedReportExecutor;
import org.eclipse.birt.report.engine.internal.executor.wrap.WrappedReportItemExecutor;

public class ReportExtensionExecutor extends WrappedReportExecutor {

	ExecutionContext context;
	IReportContent reportContent;
	IContentProcessor[] processors;

	public ReportExtensionExecutor(ExecutionContext context, IReportExecutor executor, IContentProcessor[] processors) {
		super(executor);
		this.context = context;
		this.processors = processors;
	}

	public IReportContent execute() throws BirtException {
		reportContent = reportExecutor.execute();
		startReportProcess(reportContent);
		return reportContent;
	}

	public void close() throws BirtException {
		endReportProcess(reportContent);
		super.close();
	}

	protected IReportItemExecutor createWrappedExecutor(IReportItemExecutor executor) {
		return new ReportExtensionItemExecutor(executor);
	}

	class ReportExtensionItemExecutor extends WrappedReportItemExecutor {

		IContent content;

		ReportExtensionItemExecutor(IReportItemExecutor executor) {
			super(ReportExtensionExecutor.this, executor);
		}

		public IContent execute() throws BirtException {
			content = super.execute();
			startItemProcess(content);
			return content;
		}

		public void close() throws BirtException {
			endItemProcess(content);
			super.close();
		}
	}

	void startReportProcess(IReportContent report) {
		if (report != null) {
			for (IContentProcessor processor : processors) {
				try {
					processor.start(report);
				} catch (EngineException ex) {
					context.addException(ex);
				}
			}
		}
	}

	void endReportProcess(IReportContent report) {
		if (report != null) {
			for (IContentProcessor processor : processors) {
				try {
					processor.end(report);
				} catch (EngineException ex) {
					context.addException(ex);
				}
			}
		}
	}

	void startItemProcess(IContent content) {
		if (content != null) {
			for (IContentProcessor processor : processors) {
				try {
					processor.startContent(content);
				} catch (EngineException ex) {
					context.addException(ex);
				}
			}
		}
	}

	void endItemProcess(IContent content) {
		if (content != null) {
			for (IContentProcessor processor : processors) {
				try {
					processor.endContent(content);
				} catch (EngineException ex) {
					context.addException(ex);
				}
			}
		}

	}

}
