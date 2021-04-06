
package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.ListGroupDesign;

public class ListGroupExecutor extends GroupExecutor {

	protected ListGroupExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.LISTGROUPITEM);
	}

	public void close() throws BirtException {
		handlePageBreakAfterExclusingLast();
		handlePageBreakAfter();
		finishGroupTOCEntry();
		super.close();
	}

	public IContent execute() {
		ListGroupDesign groupDesign = (ListGroupDesign) getDesign();

		IListGroupContent groupContent = report.createListGroupContent();
		setContent(groupContent);

		restoreResultSet();

		initializeContent(groupDesign, groupContent);
		processBookmark(groupDesign, groupContent);
		handlePageBreakInsideOfGroup();
		handlePageBreakBeforeOfGroup();
		handlePageBreakAfterOfGroup();
		handlePageBreakAfterOfPreviousGroup();
		handlePageBreakBefore();
		handlePageBreakInterval();
		if (context.isInFactory()) {
			handleOnCreate(groupContent);
		}

		startGroupTOCEntry(groupContent);

		// prepare to execute the children
		prepareToExecuteChildren();

		return groupContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.GroupExecutor#getNextChild()
	 */
	public IReportItemExecutor getNextChild() {
		IReportItemExecutor executor = super.getNextChild();
		if (executor instanceof ListBandExecutor) {
			ListBandExecutor bandExecutor = (ListBandExecutor) executor;
			bandExecutor.setListingExecutor(listingExecutor);
		}
		return executor;
	}
}