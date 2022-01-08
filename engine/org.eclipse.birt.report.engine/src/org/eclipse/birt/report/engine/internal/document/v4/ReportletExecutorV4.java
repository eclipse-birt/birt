/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document.v4;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.internal.executor.doc.Segment;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;

public class ReportletExecutorV4 extends AbstractReportExecutor {

	private Fragment fragment;
	private IReportItemExecutor bodyExecutor;

	public ReportletExecutorV4(ExecutionContext context, long offset) throws IOException, BirtException

	{

		super(context);
		fragment = createFragment(offset);
		bodyExecutor = new ReportletBodyExecutor(manager, fragment, offset);
	}

	public void close() {
		try {
			if (bodyExecutor != null) {
				try {
					bodyExecutor.close();
				} catch (BirtException e) {
				}
			}
		} finally {
			bodyExecutor = null;
			super.close();
		}
	}

	public IReportContent execute() {
		return reportContent;
	}

	public IReportItemExecutor getNextChild() throws BirtException {
		if (bodyExecutor != null) {
			IReportItemExecutor executor = bodyExecutor.getNextChild();
			bodyExecutor = null;
			return executor;
		}
		return null;
	}

	public boolean hasNextChild() {
		return bodyExecutor != null;
	}

	protected Fragment createFragment(long offset) throws IOException {
		Object[] leftEdge = createIndexes(offset);
		Object[] rightEdge = new Object[leftEdge.length + 1];
		System.arraycopy(leftEdge, 0, rightEdge, 0, leftEdge.length);
		rightEdge[leftEdge.length] = Segment.RIGHT_MOST_EDGE;
		Fragment fragment = new Fragment(new InstanceIDComparator());
		fragment.addSection(leftEdge, rightEdge);
		fragment.build();
		return fragment;
	}

	protected InstanceIndex[] createIndexes(long offset) throws IOException {
		LinkedList parents = new LinkedList();
		IContent content = reader.loadContent(offset);

		while (content != null) {
			InstanceID iid = content.getInstanceID();
			DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
			if (docExt != null) {
				long index = docExt.getIndex();
				parents.addFirst(new InstanceIndex(iid, index));
			}
			content = (IContent) content.getParent();
		}
		InstanceIndex[] edges = new InstanceIndex[parents.size()];
		Iterator iter = parents.iterator();
		int length = 0;
		while (iter.hasNext()) {
			InstanceIndex index = (InstanceIndex) iter.next();

			edges[length++] = index;
		}
		return edges;
	}

}
