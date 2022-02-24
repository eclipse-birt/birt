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
package org.eclipse.birt.report.engine.layout.content;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.ReportItemExecutorBase;

public class LineStackingExecutor extends ReportItemExecutorBase {
	protected IReportItemExecutor executor;
	protected IReportItemExecutor current;
	protected IReportItemExecutor next;

	public LineStackingExecutor(IReportItemExecutor first, IReportItemExecutor executor) {
		this.next = first;
		this.executor = executor;
	}

	public void close() {
		// do nothing
	}

	public IContent execute() {
		return null;
	}

	public IReportItemExecutor getNextChild() throws BirtException {
		current = next;
		if (executor != null && executor instanceof BlockStackingExecutor) {
			next = ((BlockStackingExecutor) executor).nextInline();
		} else {
			next = null;
		}
		return current;
	}

	public boolean hasNextChild() {
		return next != null;
	}

}
