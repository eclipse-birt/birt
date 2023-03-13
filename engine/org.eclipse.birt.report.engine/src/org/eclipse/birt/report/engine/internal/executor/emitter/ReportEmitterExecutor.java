/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.wrap.WrappedReportExecutor;
import org.eclipse.birt.report.engine.util.FastPool;

public class ReportEmitterExecutor extends WrappedReportExecutor {

	IReportContent report;

	IContentEmitter emitter;

	private FastPool executors = new FastPool();

	public ReportEmitterExecutor(IReportExecutor executor, IContentEmitter emitter) {
		super(executor);
		this.emitter = emitter;
	}

	@Override
	protected void closeWrappedExecutor(IReportItemExecutor executor) {
		executors.add(executor);
	}

	@Override
	protected IReportItemExecutor createWrappedExecutor(IReportItemExecutor executor) {
		ReportItemEmitterExecutor emitterExecutor = null;
		if (executors.isEmpty()) {
			emitterExecutor = new ReportItemEmitterExecutor(this, executor);
		} else {
			emitterExecutor = (ReportItemEmitterExecutor) executors.remove();
			emitterExecutor.setExecutor(executor);
		}
		return emitterExecutor;
	}

	@Override
	public void close() throws BirtException {
		if (report != null) {
			emitter.end(report);
		}
		super.close();
	}

	@Override
	public IReportContent execute() throws BirtException {
		report = super.execute();
		if (report != null) {
			emitter.start(report);
		}
		return report;
	}
}
