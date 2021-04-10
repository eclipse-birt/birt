
package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;

public class RowExecutor extends ContainerExecutor {

	private int rowId;
	private int nextItem;

	protected RowExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.ROWITEM);
		nextItem = 0;
	}

	void setRowId(int rowId) {
		this.rowId = rowId;
	}

	int getRowId() {
		return rowId;
	}

	protected IContent doCreateContent() {
		return report.createRowContent();
	}

	protected void doExecute() throws Exception {
		IRowContent rowContent = (IRowContent) content;
		rowContent.setRowID(rowId);
		executeQuery();
	}

	public void close() {
		closeQuery();
		rowId = 0;
		nextItem = 0;
		super.close();
	}

	protected ReportItemExecutor doCreateExecutor(long offset) {
		RowDesign rowDesign = (RowDesign) design;
		if (nextItem < rowDesign.getCellCount()) {
			CellDesign cellDesign = rowDesign.getCell(nextItem);
			nextItem++;
			return manager.createExecutor(this, cellDesign, offset);
		}
		return null;
	}

	protected void doSkipToExecutor(InstanceID id, long offset) {
		RowDesign rowDesign = (RowDesign) design;
		int cellCount = rowDesign.getCellCount();
		long cellDesignId = id.getComponentID();
		for (int i = 0; i < cellCount; i++) {
			ReportItemDesign childDesign = rowDesign.getCell(i);
			if (cellDesignId == childDesign.getID()) {
				// this one is the first executed element.
				nextItem = i;
				return;
			}
		}
		nextItem = rowDesign.getCellCount();
	}
}
