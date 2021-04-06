
package org.eclipse.birt.report.engine.internal.executor.dom;

import java.util.LinkedList;

import org.eclipse.birt.report.engine.content.IContent;

class DOMReportItemExecutorManager {

	boolean cloneContent = false;

	public DOMReportItemExecutorManager(boolean cloneContent) {
		this.cloneContent = cloneContent;
	}

	LinkedList freeList = new LinkedList();

	DOMReportItemExecutor createExecutor(IContent content) {
		return createExecutor(null, content);
	}

	DOMReportItemExecutor createExecutor(DOMReportItemExecutor parent, IContent content) {
		DOMReportItemExecutor executor = null;
		if (!freeList.isEmpty()) {
			executor = (DOMReportItemExecutor) freeList.removeFirst();
		} else {
			executor = new DOMReportItemExecutor(this);
		}
		executor.setContent(content);
		executor.setParent(parent);
		return executor;
	}

	void releaseExecutor(DOMReportItemExecutor executor) {
		freeList.addLast(executor);
	}

}
