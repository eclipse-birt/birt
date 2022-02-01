/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

abstract public class GroupExecutor extends ContainerExecutor {

	private ListingElementExecutor listingExecutor;

	protected GroupExecutor(ExecutorManager manager, int type) {
		super(manager, type);
		listingExecutor = null;
		executableElements = null;
		totalElements = 0;
		currentElement = 0;
		executorUtil = null;
	}

	public void close() {
		listingExecutor = null;
		executableElements = null;
		totalElements = 0;
		currentElement = 0;
		executorUtil = null;

		super.close();
	}

	void setLisingExecutor(ListingElementExecutor executor) {
		listingExecutor = executor;
		rsets = listingExecutor.rsets;
	}

	ListingElementExecutor getListingExecutor() {
		return listingExecutor;
	}

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
				groupExecutor.setLisingExecutor(listingExecutor);
			}
			return childExecutor;
		}
		return null;
	}

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

	protected void collectExecutables(boolean includeHeader, boolean startFromCurrentRow) throws BirtException {
		currentElement = 0;
		totalElements = 0;
		if (executableElements == null) {
			executableElements = new ReportItemDesign[3];
			ListingDesign listing = (ListingDesign) listingExecutor.getDesign();
			GroupDesign group = (GroupDesign) getDesign();
			int groupId = group.getGroupLevel() + 1;

			ReportItemDesign header = group.getHeader();
			ReportItemDesign footer = group.getFooter();
			ReportItemDesign detail = null;
			boolean hiddenDetail = group.getHideDetail();
			if (!hiddenDetail) {
				int groupCount = listing.getGroupCount();
				if (groupId >= groupCount) {
					detail = listing.getDetail();
				} else {
					detail = listing.getGroup(groupId);
				}
			}
			IQueryResultSet rset = (IQueryResultSet) rsets[0];
			executorUtil = new ListingElementExecutorUtil(groupId, header, footer, detail, rset, includeHeader);
		}
		if (startFromCurrentRow) {
			executorUtil.startFromCurrentRow();
		}
		totalElements = executorUtil.collectExecutableElements(executableElements);
	}
}
