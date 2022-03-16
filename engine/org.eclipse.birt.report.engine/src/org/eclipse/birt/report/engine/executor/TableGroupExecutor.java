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
package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;

public class TableGroupExecutor extends GroupExecutor {
	protected TableGroupExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.TABLEGROUPITEM);
	}

	@Override
	public void close() throws BirtException {
		handlePageBreakAfterExclusingLast();
		handlePageBreakAfter();
		finishGroupTOCEntry();
		super.close();
	}

	@Override
	public IContent execute() {
		TableGroupDesign groupDesign = (TableGroupDesign) getDesign();

		ITableGroupContent groupContent = report.createTableGroupContent();
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
	@Override
	public IReportItemExecutor getNextChild() {
		IReportItemExecutor executor = super.getNextChild();
		if (executor instanceof TableBandExecutor) {
			TableBandExecutor bandExecutor = (TableBandExecutor) executor;
			bandExecutor.setTableExecutor((TableItemExecutor) listingExecutor);
		}
		return executor;
	}
}
