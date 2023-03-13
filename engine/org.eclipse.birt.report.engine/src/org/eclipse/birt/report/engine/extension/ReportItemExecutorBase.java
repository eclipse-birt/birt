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

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;

public abstract class ReportItemExecutorBase implements IReportItemExecutor {

	/**
	 * IExecutorContext
	 */
	protected IExecutorContext executorContext;

	/**
	 * model handle
	 */
	protected Object handle;

	/**
	 * parent executor
	 */
	protected IReportItemExecutor parent;

	@Override
	public void setContext(IExecutorContext context) {
		this.executorContext = context;
	}

	@Override
	public void setModelObject(Object handle) {
		this.handle = handle;
	}

	@Override
	public void setParent(IReportItemExecutor parent) {
		this.parent = parent;
	}

	@Override
	public IExecutorContext getContext() {
		return executorContext;
	}

	@Override
	public Object getModelObject() {
		return handle;
	}

	@Override
	public IReportItemExecutor getParent() {
		return parent;
	}

	/**
	 * get resultsets of the executor
	 */
	@Override
	public IBaseResultSet[] getQueryResults() {
		return null;
	}

	/**
	 * get the content
	 */
	@Override
	public IContent getContent() {
		return null;
	}

	@Override
	public void close() throws BirtException {
		executorContext = null;
		parent = null;
		handle = null;
	}
}
