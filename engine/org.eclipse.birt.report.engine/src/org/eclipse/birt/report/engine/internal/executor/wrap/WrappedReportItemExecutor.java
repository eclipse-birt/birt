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
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;

public class WrappedReportItemExecutor implements IReportItemExecutor {

	protected WrappedReportExecutor reportExecutor;
	protected IReportItemExecutor executor;

	public WrappedReportItemExecutor(WrappedReportExecutor reportExecutor, IReportItemExecutor executor) {
		this.reportExecutor = reportExecutor;
		this.executor = executor;
	}

	public void setExecutor(IReportItemExecutor executor) {
		this.executor = executor;
	}

	@Override
	public void close() throws BirtException {
		executor.close();
		reportExecutor.closeWrappedExecutor(this);
	}

	@Override
	public IContent execute() throws BirtException {
		return executor.execute();
	}

	@Override
	public IContent getContent() {
		return executor.getContent();
	}

	@Override
	public IExecutorContext getContext() {
		return executor.getContext();
	}

	@Override
	public Object getModelObject() {
		return executor.getModelObject();
	}

	@Override
	public IReportItemExecutor getNextChild() throws BirtException {
		IReportItemExecutor child = executor.getNextChild();
		if (child != null) {
			return reportExecutor.createWrappedExecutor(child);
		}
		return null;
	}

	@Override
	public IReportItemExecutor getParent() {
		return executor.getParent();
	}

	@Override
	public IBaseResultSet[] getQueryResults() {
		return executor.getQueryResults();
	}

	@Override
	public boolean hasNextChild() throws BirtException {
		return executor.hasNextChild();
	}

	@Override
	public void setContext(IExecutorContext context) {
		executor.setContext(context);
	}

	@Override
	public void setModelObject(Object handle) {
		executor.setModelObject(handle);
	}

	@Override
	public void setParent(IReportItemExecutor parent) {
		executor.setParent(parent);
	}

}
