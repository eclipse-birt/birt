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

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;

public class TableBandExecutor extends ContainerExecutor {

	private int nextItem;

	protected TableBandExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.TABLEBANDITEM);
		nextItem = 0;
	}

	@Override
	protected IContent doCreateContent() {
		return report.createTableBandContent();
	}

	@Override
	protected void doExecute() throws Exception {
	}

	@Override
	public void close() {
		nextItem = 0;
		super.close();
	}

	private TableItemExecutor tableExecutor;

	void setTableExecutor(TableItemExecutor tableExecutor) {
		this.tableExecutor = tableExecutor;
	}

	TableItemExecutor getTableExecutor() {
		return this.tableExecutor;
	}

	@Override
	protected ReportItemExecutor doCreateExecutor(long offset) throws Exception {
		TableBandDesign bandDesign = (TableBandDesign) getDesign();
		int rowCount = bandDesign.getRowCount();
		if (nextItem < rowCount) {
			RowDesign rowDesign = bandDesign.getRow(nextItem);
			RowExecutor rowExecutor = (RowExecutor) manager.createExecutor(this, rowDesign, offset);
			int rowId = tableExecutor.getRowId();
			rowExecutor.setRowId(rowId);
			tableExecutor.setRowId(rowId++);
			nextItem++;
			return rowExecutor;
		}
		return null;
	}

	@Override
	protected void doSkipToExecutor(InstanceID id, long offset) {
		TableBandDesign bandDesign = (TableBandDesign) getDesign();
		int rowCount = bandDesign.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			ReportItemDesign childDesign = bandDesign.getRow(i);
			if (childDesign.getID() == id.getComponentID()) {
				// this one is the first executed element.
				nextItem = i;
				return;
			}
		}
		nextItem = rowCount;
	}
}
