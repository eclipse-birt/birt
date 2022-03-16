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

package org.eclipse.birt.report.engine.internal.executor.doc;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;

public class ReportletReader extends AbstractReportReader {

	Fragment reportletFragment = null;
	BodyReader bodyExecutor;

	public ReportletReader(ExecutionContext context, long offset) throws IOException, BirtException {
		super(context);
		Fragment fragment = loadFragment(offset);
		bodyExecutor = new BodyReader(this, fragment);
	}

	@Override
	public IReportItemExecutor getNextChild() {
		return bodyExecutor.getNextChild();
	}

	@Override
	public boolean hasNextChild() {
		return bodyExecutor.hasNextChild();
	}

	protected Fragment loadFragment(long offset) throws IOException {
		Object[] leftEdge = createEdges(offset);
		Object[] rightEdge = new Object[leftEdge.length + 1];
		System.arraycopy(leftEdge, 0, rightEdge, 0, leftEdge.length);
		rightEdge[leftEdge.length] = Segment.RIGHT_MOST_EDGE;
		Fragment fragment = new Fragment(new LongComparator());
		fragment.addSection(leftEdge, rightEdge);
		fragment.build();
		return fragment;
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
}
