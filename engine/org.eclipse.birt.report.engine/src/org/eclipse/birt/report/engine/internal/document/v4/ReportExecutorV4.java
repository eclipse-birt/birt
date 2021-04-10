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

package org.eclipse.birt.report.engine.internal.document.v4;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;

public class ReportExecutorV4 extends AbstractReportExecutor {

	protected ReportItemExecutor bodyExecutor;

	public ReportExecutorV4(ExecutionContext context) throws IOException, BirtException {
		super(context);
		bodyExecutor = new ReportBodyExecutor(manager, null);
	}

	public void close() {
		bodyExecutor.close();
		super.close();
	}

	public IReportContent execute() {
		bodyExecutor.execute();
		return reportContent;
	}

	public boolean hasNextChild() {
		return bodyExecutor.hasNextChild();
	}

	public IReportItemExecutor getNextChild() {
		return bodyExecutor.getNextChild();
	}
}
