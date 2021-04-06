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

package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.toc.ITreeNode;
import org.eclipse.birt.report.engine.toc.ViewFilter;

public class VisiblePageFilter implements ViewFilter {

	IReportDocument document;
	LogicalPageSequence visiblePages;

	public VisiblePageFilter(IReportDocument document, LogicalPageSequence visiblePages) {
		this.document = document;
		this.visiblePages = visiblePages;
	}

	public boolean isVisible(ITreeNode node) {
		String id = node.getNodeId();
		if (id == null || "/".equals(id)) {
			return true;
		}
		String bookmark = node.getBookmark();
		long physicalPageNumber = document.getPageNumber(bookmark);
		long pageNumber = visiblePages.getLogicalPageNumber(physicalPageNumber);

		return pageNumber != -1;
	}
}
