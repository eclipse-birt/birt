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

import java.util.LinkedList;

import org.eclipse.birt.report.engine.content.IContent;

class DOMReportItemExecutorManager {

	boolean cloneContent = false;

	public DOMReportItemExecutorManager(boolean cloneContent) {
		this.cloneContent = cloneContent;
	}

	LinkedList freeList = new LinkedList();

	DOMReportItemExecutor createExecutor(IContent content) {
		return createExecutor(null, content);
	}

	DOMReportItemExecutor createExecutor(DOMReportItemExecutor parent, IContent content) {
		DOMReportItemExecutor executor = null;
		if (!freeList.isEmpty()) {
			executor = (DOMReportItemExecutor) freeList.removeFirst();
		} else {
			executor = new DOMReportItemExecutor(this);
		}
		executor.setContent(content);
		executor.setParent(parent);
		return executor;
	}

	void releaseExecutor(DOMReportItemExecutor executor) {
		freeList.addLast(executor);
	}

}
