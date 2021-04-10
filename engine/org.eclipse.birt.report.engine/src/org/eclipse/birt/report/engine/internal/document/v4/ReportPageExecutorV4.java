/*******************************************************************************
 * Copyright (c) 2007,2009 Actuate Corporation.
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;
import org.eclipse.birt.report.engine.presentation.PageSection;

public class ReportPageExecutorV4 extends AbstractReportExecutor {

	private boolean paged;
	private ArrayList outputPages = new ArrayList();

	private PageRangeIterator pageIter;
	private ReportBodyExecutor bodyExecutor;

	public ReportPageExecutorV4(ExecutionContext context, List pages, boolean paged) throws IOException, BirtException {
		super(context);
		this.outputPages.addAll(pages);
		this.paged = paged;
		pageIter = new PageRangeIterator(outputPages);
		if (!paged) {
			Fragment fragment = loadPageHints(outputPages);
			bodyExecutor = new ReportBodyExecutor(manager, fragment);
		}
	}

	public void close() {
		pageIter = null;
		if (bodyExecutor != null) {
			bodyExecutor.close();
			bodyExecutor = null;
		}
		super.close();
	}

	public IReportContent execute() {
		if (bodyExecutor != null) {
			bodyExecutor.execute();
		}
		return reportContent;
	}

	public IReportItemExecutor getNextChild() {
		if (hasNextChild()) {
			try {
				if (paged) {
					long pageNumber = pageIter.next();
					IPageHint pageHint = getPageHint(pageNumber);
					Collection<PageVariable> vars = pageHint.getPageVariables();
					context.addPageVariables(vars);

					Fragment fragment = createFragment(pageHint);
					return new ReportBodyExecutor(manager, fragment);
				} else {
					return bodyExecutor.getNextChild();
				}
			} catch (IOException ex) {
				context.addException(new EngineException(MessageConstants.PAGES_LOADING_ERROR, ex));
			}
		}
		return null;
	}

	public boolean hasNextChild() {
		if (paged) {
			return pageIter.hasNext();
		}
		return bodyExecutor.hasNextChild();
	}

	protected Fragment loadPageHints(List pageSequence) throws IOException {
		if (pageSequence.size() == 1) {
			if (context.getReportDocument().isComplete()) {
				long[] pages = (long[]) pageSequence.get(0);
				if (pages[0] == 1 && pages[1] == hintsReader.getTotalPage()) {
					return null;
				}
			}
		}

		Fragment fragment = new Fragment(new InstanceIDComparator());

		PageRangeIterator iter = new PageRangeIterator(pageSequence);
		while (iter.hasNext()) {
			long pageNumber = iter.next();
			IPageHint pageHint = hintsReader.getPageHint(pageNumber);
			int sectCount = pageHint.getSectionCount();
			for (int i = 0; i < sectCount; i++) {
				PageSection section = pageHint.getSection(i);
				InstanceIndex[] leftEdges = section.starts;
				InstanceIndex[] rightEdges = section.ends;
				fragment.addSection(leftEdges, rightEdges);
			}
		}
		fragment.build();
		return fragment;
	}

	protected Fragment createFragment(IPageHint pageHint) {
		Fragment fragment = new Fragment(new InstanceIDComparator());

		int sectCount = pageHint.getSectionCount();
		for (int i = 0; i < sectCount; i++) {
			PageSection section = pageHint.getSection(i);
			InstanceIndex[] leftEdges = section.starts;
			InstanceIndex[] rightEdges = section.ends;
			fragment.addSection(leftEdges, rightEdges);
		}
		fragment.build();
		return fragment;
	}

	public IPageHint getPageHint(long pageNumber) throws IOException {
		return hintsReader.getPageHint(pageNumber);
	}
}
