/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
