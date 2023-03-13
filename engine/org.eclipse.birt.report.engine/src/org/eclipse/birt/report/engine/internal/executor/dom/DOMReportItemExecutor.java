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

package org.eclipse.birt.report.engine.internal.executor.dom;

import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.ReportItemExecutorBase;

public class DOMReportItemExecutor extends ReportItemExecutorBase {

	IContent content;
	DOMReportItemExecutorManager manager;
	boolean cloneContent;

	DOMReportItemExecutor(DOMReportItemExecutorManager manager) {
		this.manager = manager;
		this.cloneContent = manager.cloneContent;
	}

	public DOMReportItemExecutor(IContent content, boolean cloneContent) {
		this.content = content;
		this.cloneContent = cloneContent;
		this.manager = new DOMReportItemExecutorManager(cloneContent);
	}

	public DOMReportItemExecutor(IContent content) {
		this(content, false);
	}

	void setContent(IContent content) {
		this.content = content;
	}

	@Override
	public IContent getContent() {
		return this.content;
	}

	@Override
	public void close() {
		manager.releaseExecutor(this);
	}

	@Override
	public IContent execute() {
		if (null == content) {
			return null;
		}
		childIterator = content.getChildren().iterator();
		if (cloneContent) {
			content = content.cloneContent(false);
			IReportItemExecutor parent = getParent();
			if (parent != null) {
				content.setParent(parent.getContent());
			}
		}
		return content;
	}

	Iterator childIterator;

	@Override
	public IReportItemExecutor getNextChild() {
		if (null != childIterator && childIterator.hasNext()) {
			IContent child = (IContent) childIterator.next();
			return manager.createExecutor(this, child);
		}
		return null;
	}

	@Override
	public boolean hasNextChild() {
		if (null == childIterator) {
			return false;
		}
		return childIterator.hasNext();
	}

}
