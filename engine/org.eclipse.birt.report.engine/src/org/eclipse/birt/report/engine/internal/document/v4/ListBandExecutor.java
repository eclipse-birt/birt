
package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

public class ListBandExecutor extends ContainerExecutor {

	private int nextItem;

	protected ListBandExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.LISTBANDITEM);
		nextItem = 0;
	}

	protected IContent doCreateContent() {
		return report.createListBandContent();
	}

	protected void doExecute() throws Exception {
	}

	public void close() {
		nextItem = 0;
		super.close();
	}

	protected ReportItemExecutor doCreateExecutor(long offset) throws Exception {
		ListBandDesign bandDesign = (ListBandDesign) design;
		int contentCount = bandDesign.getContentCount();
		if (nextItem < contentCount) {
			ReportItemDesign design = bandDesign.getContent(nextItem);
			nextItem++;
			return manager.createExecutor(this, design, offset);
		}
		return null;
	}

	protected void doSkipToExecutor(InstanceID id, long offset) throws Exception {
		ListBandDesign bandDesign = (ListBandDesign) design;
		int contentCount = bandDesign.getContentCount();
		long contentDesignId = id.getComponentID();
		for (int i = 0; i < contentCount; i++) {
			ReportItemDesign childDesign = bandDesign.getContent(i);
			if (contentDesignId == childDesign.getID()) {
				// this one is the first executed element.
				nextItem = i;
				return;
			}
		}
		nextItem = contentCount;
	}
}
