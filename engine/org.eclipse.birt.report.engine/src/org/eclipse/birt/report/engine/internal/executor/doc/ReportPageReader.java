/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.executor.doc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.document.v4.PageRangeIterator;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.PageSection;

public class ReportPageReader extends AbstractReportReader {

	private boolean paged;
	private ArrayList outputPages = new ArrayList();
	private PageRangeIterator pageIter;
	private BodyReader bodyExecutor;

	/**
	 * does the output should keep the pagination.
	 * 
	 * For some emitter, it will has its own pagination, so the report page reader
	 * only read out the page content and merge the contente together. The emitter
	 * will re-paginate the content again. Such as output PDF using HTML paginhints.
	 * 
	 * Some emitter in the otherside, will use the same pagination with the page
	 * hint. For those emitter, the output will include the master pages. such as
	 * output HTML with the HTML emitter.
	 * 
	 * @param context      context used to read the report.
	 * @param pages        page list
	 * @param keepPaginate should the output keep pagianted.
	 */
	public ReportPageReader(ExecutionContext context, List pages, boolean paged) throws IOException, BirtException {
		super(context);
		outputPages.addAll(pages);
		this.paged = paged;
		pageIter = new PageRangeIterator(outputPages);
		if (!paged) {
			Fragment fragment = loadPageFragment(outputPages);
			bodyExecutor = new BodyReader(this, fragment);
		}
	}

	public void close() {
		if (bodyExecutor != null) {
			bodyExecutor.close();
			bodyExecutor = null;
		}
		super.close();
	}

	protected Long[] createEdges(long offset) throws IOException {
		LinkedList parents = new LinkedList();
		IContent content = reader.loadContent(offset);
		while (content != null) {
			DocumentExtension ext = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
			if (ext != null) {
				parents.addFirst(new Long(ext.getIndex()));
			}
			content = (IContent) content.getParent();
		}
		Long[] edges = new Long[parents.size()];
		Iterator iter = parents.iterator();
		int length = 0;
		while (iter.hasNext()) {
			Long value = (Long) iter.next();
			edges[length++] = value;
		}
		return edges;
	}

	protected Fragment loadPageFragment(List pages) throws IOException {
		// test if it is the whole report
		if (pages.size() == 1) {
			long[] seg = (long[]) pages.get(0);
			if (seg != null && seg.length == 2) {
				if (seg[0] == 0 && seg[1] == hintReader.getTotalPage()) {
					return null;
				}
			}
		}

		Fragment fragment = new Fragment(new LongComparator());
		PageRangeIterator iter = new PageRangeIterator(pages);
		while (iter.hasNext()) {
			long pageNumber = iter.next();
			IPageHint pageHint = hintReader.getPageHint(pageNumber);
			if (pageHint == null) {
				continue;
			}
			int sectCount = pageHint.getSectionCount();
			for (int i = 0; i < sectCount; i++) {
				PageSection section = pageHint.getSection(i);
				long left = section.startOffset;
				long right = section.endOffset;
				Long[] leftEdges = createEdges(left);
				Long[] rightEdges = createEdges(right);
				fragment.addSection(leftEdges, rightEdges);
			}
		}
		fragment.build();
		return fragment;
	}

	Fragment loadPageFragment(long pageNumber) throws IOException {
		Fragment fragment = new Fragment(new LongComparator());
		IPageHint pageHint = hintReader.getPageHint(pageNumber);
		if (pageHint != null) {
			int sectionCount = pageHint.getSectionCount();
			for (int i = 0; i < sectionCount; i++) {
				try {
					PageSection section = pageHint.getSection(i);
					long left = section.startOffset;
					long right = section.endOffset;
					Long[] leftEdges = createEdges(left);
					Long[] rightEdges = createEdges(right);
					fragment.addSection(leftEdges, rightEdges);
				} catch (IOException ex) {
					logger.log(Level.SEVERE, "Can not load the page hints", ex);
				}
			}
		}
		fragment.build();
		return fragment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.internal.executor.doc.ReportReader#
	 * getNextChild()
	 */
	public IReportItemExecutor getNextChild() {
		if (hasNextChild()) {
			try {
				if (paged) {
					long pageNumber = pageIter.next();
					Fragment fragment = loadPageFragment(pageNumber);
					return new BodyReader(this, fragment);
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

	public IPageHint getPageHint(long pageNumber) throws IOException {
		return hintReader.getPageHint(pageNumber);
	}
}
