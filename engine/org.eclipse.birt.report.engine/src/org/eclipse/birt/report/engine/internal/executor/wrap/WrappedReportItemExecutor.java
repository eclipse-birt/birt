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

	public void close() throws BirtException {
		executor.close();
		reportExecutor.closeWrappedExecutor(this);
	}

	public IContent execute() throws BirtException {
		return executor.execute();
	}

	public IContent getContent() {
		return executor.getContent();
	}

	public IExecutorContext getContext() {
		return executor.getContext();
	}

	public Object getModelObject() {
		return executor.getModelObject();
	}

	public IReportItemExecutor getNextChild() throws BirtException {
		IReportItemExecutor child = executor.getNextChild();
		if (child != null) {
			return reportExecutor.createWrappedExecutor(child);
		}
		return null;
	}

	public IReportItemExecutor getParent() {
		return executor.getParent();
	}

	public IBaseResultSet[] getQueryResults() {
		return executor.getQueryResults();
	}

	public boolean hasNextChild() throws BirtException {
		return executor.hasNextChild();
	}

	public void setContext(IExecutorContext context) {
		executor.setContext(context);
	}

	public void setModelObject(Object handle) {
		executor.setModelObject(handle);
	}

	public void setParent(IReportItemExecutor parent) {
		executor.setParent(parent);
	}

}
