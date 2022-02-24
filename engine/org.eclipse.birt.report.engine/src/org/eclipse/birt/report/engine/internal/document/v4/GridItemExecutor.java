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

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;

/**
 * the gridItem excutor
 * 
 */
public class GridItemExecutor extends ContainerExecutor {

	private int nextItem;

	public GridItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.GRIDITEM);
		nextItem = 0;
	}

	protected IContent doCreateContent() {
		return report.createTableContent();
	}

	protected void doExecute() throws Exception {
		GridItemDesign gridDesign = (GridItemDesign) getDesign();
		ITableContent tableContent = (ITableContent) content;

		executeQuery();

		if (tableContent.getColumnCount() == 0) {
			for (int i = 0; i < gridDesign.getColumnCount(); i++) {
				ColumnDesign columnDesign = gridDesign.getColumn(i);
				Column column = new Column(report);
				column.setGenerateBy(columnDesign);

				InstanceID iid = new InstanceID(null, columnDesign.getID(), null);
				column.setInstanceID(iid);

				tableContent.addColumn(column);
			}
		} else {
			int columnCount = tableContent.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				Column column = (Column) tableContent.getColumn(i);
				InstanceID iid = column.getInstanceID();
				if (iid != null) {
					long componentId = iid.getComponentID();
					ReportElementDesign element = report.getDesign().getReportItemByID(componentId);
					column.setGenerateBy(element);
				}
			}
		}
	}

	public void close() {
		nextItem = 0;
		closeQuery();
		super.close();
	}

	protected ReportItemExecutor doCreateExecutor(long offset) throws Exception {
		GridItemDesign gridDesign = (GridItemDesign) getDesign();
		if (nextItem < gridDesign.getRowCount()) {
			ReportItemDesign design = gridDesign.getRow(nextItem);
			nextItem++;
			RowExecutor rowExecutor = (RowExecutor) manager.createExecutor(this, design, offset);
			rowExecutor.setRowId(nextItem);
			return rowExecutor;
		}
		return null;
	}

	protected void doSkipToExecutor(InstanceID id, long offset) throws Exception {
		GridItemDesign gridDesign = (GridItemDesign) getDesign();
		int rowCount = gridDesign.getRowCount();
		long rowId = id.getComponentID();
		for (int i = 0; i < rowCount; i++) {
			RowDesign rowDesign = gridDesign.getRow(i);
			if (rowId == rowDesign.getID()) {
				// this one is the first executed element.
				nextItem = i;
				return;
			}
		}
		nextItem = rowCount;
	}
}
