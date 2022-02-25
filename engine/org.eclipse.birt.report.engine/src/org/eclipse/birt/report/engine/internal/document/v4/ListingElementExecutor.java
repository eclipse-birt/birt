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

package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * An abstract class that defines execution logic for a Listing element, which
 * is the base element for table and list items.
 */
public abstract class ListingElementExecutor extends ContainerExecutor {

	protected ListingElementExecutor(ExecutorManager manager, int type) {
		super(manager, type);
		executableElements = null;
		totalElements = 0;
		currentElement = 0;
		executorUtil = null;
	}

	@Override
	public void close() {
		executableElements = null;
		totalElements = 0;
		currentElement = 0;
		executorUtil = null;
		super.close();
	}

	@Override
	protected ReportItemExecutor doCreateExecutor(long offset) throws Exception {
		if (currentElement >= totalElements) {
			// we need get the next executable elements.
			collectExecutables();
		}

		if (currentElement < totalElements) {
			ReportItemDesign childDesign = executableElements[currentElement];
			currentElement++;
			ReportItemExecutor childExecutor = manager.createExecutor(this, childDesign, offset);
			if (childExecutor instanceof GroupExecutor) {
				GroupExecutor groupExecutor = (GroupExecutor) childExecutor;
				groupExecutor.setLisingExecutor(this);
			}
			return childExecutor;
		}
		return null;
	}

	@Override
	protected void doSkipToExecutor(InstanceID iid, long offset) throws Exception {
		IQueryResultSet rset = (IQueryResultSet) rsets[0];
		long uid = iid.getUniqueID();
		DataID dataId = iid.getDataID();
		long rowId = 0;
		if (dataId != null) {
			rowId = dataId.getRowID();
		}
		long rsetPosition = rset.getRowIndex();
		if (rsetPosition == rowId) {
			if (currentElement >= totalElements) {
				collectExecutables(uid == 0, true);
			}
		} else {
			rset.skipTo(rowId);
			collectExecutables(uid == 0, true);
		}
		for (int i = 0; i < totalElements; i++) {
			if (executableElements[i].getID() == iid.getComponentID()) {
				currentElement = i;
				return;
			}
		}
		currentElement = totalElements;
	}

	// bands to be execute in current row.
	private ReportItemDesign[] executableElements;
	// total bands in the executabelBands
	private int totalElements;
	// band to be executed
	private int currentElement;

	private ListingElementExecutorUtil executorUtil;

	protected void collectExecutables() throws BirtException {
		collectExecutables(true, false);
	}

	protected void collectExecutables(boolean includeHeader, boolean useCurrentRow) throws BirtException {
		IQueryResultSet rset = null;
		if (rsets != null) {
			rset = (IQueryResultSet) rsets[0];
		}
		currentElement = 0;
		totalElements = 0;
		if (executableElements == null) {
			ListingDesign listing = (ListingDesign) getDesign();
			// prepare the bands to be executed.
			executableElements = new ReportItemDesign[3];
			if (rset == null || rsetEmpty) {
				BandDesign header = listing.getHeader();
				if (header != null) {
					executableElements[totalElements++] = header;
				}
				BandDesign footer = listing.getFooter();
				if (footer != null) {
					executableElements[totalElements++] = footer;
				}
			} else {
				executorUtil = new ListingElementExecutorUtil(0, listing.getHeader(), listing.getFooter(),
						listing.getGroupCount() == 0 ? (ReportItemDesign) listing.getDetail()
								: (ReportItemDesign) listing.getGroup(0),
						rset, includeHeader);
			}
		}
		if (executorUtil != null) {
			if (useCurrentRow) {
				executorUtil.startFromCurrentRow();
			}
			totalElements = executorUtil.collectExecutableElements(executableElements);
		}
	}
}
