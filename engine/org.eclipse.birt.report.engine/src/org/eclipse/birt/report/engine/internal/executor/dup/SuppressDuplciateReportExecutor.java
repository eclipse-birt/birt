/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.executor.dup;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.wrap.WrappedReportExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.util.FastPool;

public class SuppressDuplciateReportExecutor extends WrappedReportExecutor {

	private IReportContent report;

	private FastPool executors;

	private SuppressDuplicateUtil suppressUtil;

	public SuppressDuplciateReportExecutor(IReportExecutor executor) {
		super(executor);
		this.executors = new FastPool();

	}

	@Override
	public IReportContent execute() throws BirtException {
		if (report == null) {
			report = super.execute();
			this.suppressUtil = new SuppressDuplicateUtil(report.getDesign());
		}
		return report;
	}

	void clearDuplicateFlags(IContent content) {
		suppressUtil.clearDuplicateFlags(content);
	}

	IContent suppressDuplicate(IContent content) throws BirtException {
		return suppressUtil.suppressDuplicate(content);
	}

	@Override
	public IReportItemExecutor createPageExecutor(long pageNumber, MasterPageDesign pageDesign) throws BirtException {
		return reportExecutor.createPageExecutor(pageNumber, pageDesign);
	}

	@Override
	protected IReportItemExecutor createWrappedExecutor(IReportItemExecutor executor) {
		SuppressDuplicateItemExecutor wrappedExecutor = null;
		if (executors.isEmpty()) {
			wrappedExecutor = new SuppressDuplicateItemExecutor(this, executor);
		} else {
			wrappedExecutor = (SuppressDuplicateItemExecutor) executors.remove();
			wrappedExecutor.setExecutor(executor);
		}
		return wrappedExecutor;
	}

	@Override
	protected void closeWrappedExecutor(IReportItemExecutor executor) {
		executors.add(executor);
	}
}
