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

package org.eclipse.birt.report.engine.internal.executor.wrap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;

public class WrappedReportExecutor implements IReportExecutor {

	protected IReportExecutor reportExecutor;

	public WrappedReportExecutor(IReportExecutor reportExecutor) {
		this.reportExecutor = reportExecutor;
	}

	protected IReportItemExecutor createWrappedExecutor(IReportItemExecutor executor) {
		return new WrappedReportItemExecutor(this, executor);
	}

	protected void closeWrappedExecutor(IReportItemExecutor executor) {

	}

	public void close() throws BirtException {
		reportExecutor.close();
	}

	public IReportItemExecutor createPageExecutor(long pageNumber, MasterPageDesign pageDesign) throws BirtException {
		IReportItemExecutor executor = reportExecutor.createPageExecutor(pageNumber, pageDesign);
		if (executor != null) {
			return createWrappedExecutor(executor);
		}
		return null;
	}

	public IReportContent execute() throws BirtException {
		return reportExecutor.execute();
	}

	public IReportItemExecutor getNextChild() throws BirtException {
		IReportItemExecutor executor = reportExecutor.getNextChild();
		if (executor != null) {
			return createWrappedExecutor(executor);
		}
		return null;
	}

	public boolean hasNextChild() throws BirtException {
		return reportExecutor.hasNextChild();
	}

}
