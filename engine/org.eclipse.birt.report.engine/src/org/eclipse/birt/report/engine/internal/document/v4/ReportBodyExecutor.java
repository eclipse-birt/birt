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

package org.eclipse.birt.report.engine.internal.document.v4;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.impl.IReportletDocument;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.ReportletQuery;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.internal.executor.doc.Segment;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;

public class ReportBodyExecutor extends ContainerExecutor {

	private Report reportDesign;
	private int nextItem;

	ReportBodyExecutor(ExecutorManager manager, Fragment fragment) {
		super(manager, -1);
		this.reportDesign = context.getReport();
		this.reader = manager.getReportReader();
		this.nextItem = 0;
		// if fragment is null, we starts from the first element, so set
		// the next offset to 0, else use the one defined in the fragment.
		if (fragment == null) {
			if (!reader.isEmpty()) {
				nextOffset = reader.getRootOffset();
			}
		} else {
			setFragment(fragment);
			// the first nextOffset always start from 0 or fragment.leftEdge.
			Object[][] sections = fragment.getSections();
			if (sections != null && sections.length > 0) {
				Object[] edges = sections[0];
				if (edges[0] == Segment.LEFT_MOST_EDGE) {
					if (!reader.isEmpty()) {
						nextOffset = reader.getRootOffset();
					}
				} else {
					InstanceIndex leftEdge = (InstanceIndex) edges[0];
					if (leftEdge.getOffset() == -1) {
						if (!reader.isEmpty()) {
							nextOffset = reader.getRootOffset();
						}
					}
				}
			}
		}
		this.content = report.getRoot();
		initializeReportlet();
	}

	public void close() {

		if (reportlet != null) {
			try {
				reportlet.closeReportletQueries();
			} catch (BirtException ex) {
				context.addException(ex);
			}
			reportlet = null;
		}
		nextItem = 0;
		super.close();
	}

	public IBaseResultSet[] getQueryResults() {
		if (reportlet != null) {
			return reportlet.getQueryResults();
		}
		return super.getQueryResults();
	}

	public IContent execute() {
		if (reportlet != null) {
			try {
				reportlet.openReportletQueries();
			} catch (BirtException ex) {
				context.addException(ex);
			}
		}
		return content;
	}

	protected InstanceID getInstanceID() {
		return null;
	}

	protected ReportItemExecutor doCreateExecutor(long offset) throws Exception {
		if (reportlet != null) {
			return reportlet.createExecutor(offset);
		}
		int itemCount = reportDesign.getContentCount();
		if (nextItem < itemCount) {
			ReportItemDesign itemDesign = reportDesign.getContent(nextItem);
			nextItem++;
			return manager.createExecutor(this, itemDesign, offset);
		}
		return null;
	}

	protected void doSkipToExecutor(InstanceID id, long offset) throws Exception {
		if (reportlet != null) {
			reportlet.skipToExecutor(id, offset);
			return;
		}
		int itemCount = reportDesign.getContentCount();
		long designId = id.getComponentID();
		for (int i = 0; i < itemCount; i++) {
			ReportItemDesign itemDesign = reportDesign.getContent(i);
			if (designId == itemDesign.getID()) {
				nextItem = i;
				return;
			}
		}
		nextItem = itemCount;
	}

	protected void doExecute() throws Exception {
	}

	ReportletBodyExecutor reportlet;

	void initializeReportlet() {
		if (reportlet == null) {
			IReportDocument document = context.getReportDocument();

			if (document instanceof IReportletDocument) {
				IReportletDocument reportletDocument = (IReportletDocument) document;
				try {
					if (reportletDocument.isReporltetDocument()) {
						InstanceID iid = reportletDocument.getReportletInstanceID();
						if (iid != null) {
							long id = iid.getComponentID();
							if (id != -1) {
								reportlet = new ReportletBodyExecutor(context, iid);
							}
						}
					}
				} catch (IOException ex) {
					context.addException(new EngineException(ex.getLocalizedMessage(), ex));
				}
			}
		}
	}

	private class ReportletBodyExecutor {

		boolean hasNext = true;
		ReportItemDesign reportletDesign;
		ReportletQuery reportletQuery;

		ReportletBodyExecutor(ExecutionContext context, InstanceID iid) {
			long id = iid.getComponentID();
			reportletDesign = (ReportItemDesign) context.getReport().getReportItemByID(id);
			reportletQuery = new ReportletQuery(context, iid);
		}

		ReportItemExecutor createExecutor(long offset) throws Exception {
			if (hasNext) {
				hasNext = false;
				return manager.createExecutor(ReportBodyExecutor.this, reportletDesign, offset);
			}
			return null;
		}

		void skipToExecutor(InstanceID id, long offset) throws Exception {
			assert id.getComponentID() == reportletDesign.getID();
		}

		void openReportletQueries() throws BirtException {
			reportletQuery.openReportletQueries();
		}

		void closeReportletQueries() throws BirtException {

			reportletQuery.closeReportletQueries();
		}

		public IBaseResultSet[] getQueryResults() {
			return reportletQuery.getQueryResults();
		}
	}
}
