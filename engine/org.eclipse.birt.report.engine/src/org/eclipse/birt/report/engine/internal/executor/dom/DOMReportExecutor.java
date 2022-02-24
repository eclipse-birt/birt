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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;

public class DOMReportExecutor implements IReportExecutor {

	DOMReportItemExecutorManager manager;
	IReportContent reportContent;

	public DOMReportExecutor(IReportContent reportContent, boolean cloneContent) {
		manager = new DOMReportItemExecutorManager(cloneContent);
	}

	public IReportContent execute() {
		childIterator = new ArrayList().iterator();
		return reportContent;
	}

	public void close() {
	}

	Iterator childIterator;

	public IReportItemExecutor getNextChild() {
		if (childIterator.hasNext()) {
			IContent child = (IContent) childIterator.next();
			return manager.createExecutor(child);
		}
		return null;
	}

	public boolean hasNextChild() {
		return childIterator.hasNext();
	}

	public IReportItemExecutor createPageExecutor(long pageNumber, MasterPageDesign pageDesign) {
		return null;
	}
}
