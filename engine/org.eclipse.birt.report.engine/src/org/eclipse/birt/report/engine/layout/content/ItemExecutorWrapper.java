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

public class ItemExecutorWrapper extends ReportItemExecutorBase {
	protected IReportItemExecutor executor;
	protected IContent content;

	public ItemExecutorWrapper(IReportItemExecutor executor, IContent content) {
		this.executor = executor;
		this.content = content;
	}

	public void close() throws BirtException {
		executor.close();
	}

	public IContent execute() {
		return content;
	}

	public IReportItemExecutor getNextChild() throws BirtException {
		return executor.getNextChild();
	}

	public boolean hasNextChild() throws BirtException {
		return executor.hasNextChild();
	}

}
