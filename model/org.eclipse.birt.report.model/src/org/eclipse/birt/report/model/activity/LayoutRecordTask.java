/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.activity;

import java.util.Stack;

import org.eclipse.birt.report.model.api.elements.table.BasicLayoutStrategies;
import org.eclipse.birt.report.model.api.elements.table.LayoutChangedEvent;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.TableItem;

/**
 * The task to update the layout of the table after the execution of
 * one/multiple records.
 * 
 */

public class LayoutRecordTask extends RecordTask {

	/**
	 * The report module.
	 */

	private Module module;

	/**
	 * The constructor with the given Cell, TableRow, TableGroup or TableItem.
	 * 
	 * @param module          the module
	 * @param compoundElement the table/grid expected to be updated
	 * 
	 */

	public LayoutRecordTask(Module module, ReportItem compoundElement) {
		super(compoundElement);
		this.module = module;
	}

	/**
	 * Returns <code>true</code> if need to hold the event at this time. We need to
	 * hold the event if it is sent inside a transaction that declared to filter
	 * notification events( <code>LayoutCompoundRecord</code>).
	 * 
	 * @param transStack the transaction stack.
	 * @return <code>true</code> if need to hold the event at this time, returns
	 *         <code>false</code> otherwise.
	 */

	protected final boolean holdTask(Stack<CompoundRecord> transStack) {
		if (transStack != null && !transStack.isEmpty()) {
			CompoundRecord cr = transStack.peek();
			if (cr instanceof LayoutCompoundRecord)
				return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.activity.IInterceptorTask#doTask(org
	 * .eclipse.birt.report.model.activity.ActivityRecord)
	 */

	public void doTask(ActivityRecord record, Stack<CompoundRecord> transStack) {
		if (holdTask(transStack))
			return;

		ReportItem compoundElement = (ReportItem) getTarget();

		if (compoundElement instanceof TableItem) {
			TableItem table = (TableItem) compoundElement;
			table.refreshRenderModel(module);
			BasicLayoutStrategies.appliesStrategies(table.getLayoutModel(module), false);
		}

		// sends out the notification event.

		LayoutChangedEvent event = new LayoutChangedEvent(compoundElement);
		compoundElement.broadcast(event);
	}
}
