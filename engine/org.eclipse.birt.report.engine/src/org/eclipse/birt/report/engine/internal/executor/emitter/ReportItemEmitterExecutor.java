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

package org.eclipse.birt.report.engine.internal.executor.emitter;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.wrap.WrappedReportItemExecutor;

public class ReportItemEmitterExecutor extends WrappedReportItemExecutor {

	IContent content;

	IContentEmitter emitter;

	ReportItemEmitterExecutor(ReportEmitterExecutor reportExecutor, IReportItemExecutor executor) {
		super(reportExecutor, executor);
		this.emitter = reportExecutor.emitter;
	}

	public void close() throws BirtException {
		if (content != null) {
			ContentEmitterUtil.endContent(content, emitter);
		}
		super.close();
	}

	public IContent execute() throws BirtException {
		content = super.execute();
		if (content != null) {
			ContentEmitterUtil.startContent(content, emitter);
		}
		return content;
	}
}
