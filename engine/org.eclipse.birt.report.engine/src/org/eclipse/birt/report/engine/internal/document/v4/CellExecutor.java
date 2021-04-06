
package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

public class CellExecutor extends ContainerExecutor {

	private int nextItem;

	protected CellExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.CELLITEM);
		nextItem = 0;
	}

	protected IContent doCreateContent() {
		return report.createCellContent();
	}

	protected void doExecute() throws Exception {
		executeQuery();
	}

	public void close() {
		nextItem = 0;
		closeQuery();
		super.close();
	}

	protected ReportItemExecutor doCreateExecutor(long offset) throws Exception {
		CellDesign cellDesign = (CellDesign) design;
		if (nextItem < cellDesign.getContentCount()) {
			ReportItemDesign design = cellDesign.getContent(nextItem);
			nextItem++;
			return manager.createExecutor(this, design, nextOffset);
		}
		return null;
	}

	protected void doSkipToExecutor(InstanceID id, long offset) throws Exception {
		CellDesign cellDesign = (CellDesign) design;
		int itemCount = cellDesign.getContentCount();
		long designId = id.getComponentID();
		for (int i = 0; i < itemCount; i++) {
			ReportItemDesign childDesign = cellDesign.getContent(i);
			if (designId == childDesign.getID()) {
				// this one is the first executed element.
				nextItem = i;
				return;
			}
		}
		nextItem = itemCount;
	}
}
