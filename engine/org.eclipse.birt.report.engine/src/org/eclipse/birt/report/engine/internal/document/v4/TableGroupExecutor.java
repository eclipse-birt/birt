/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import org.eclipse.birt.report.engine.content.IContent;

public class TableGroupExecutor extends GroupExecutor {

	protected TableGroupExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.TABLEGROUPITEM);
	}

	protected ReportItemExecutor doCreateExecutor(long offset) throws Exception {
		ReportItemExecutor executor = super.doCreateExecutor(offset);
		if (executor instanceof TableBandExecutor) {
			TableBandExecutor bandExecutor = (TableBandExecutor) executor;
			bandExecutor.setTableExecutor((TableItemExecutor) getListingExecutor());
		}
		return executor;
	}

	protected IContent doCreateContent() {
		return report.createTableGroupContent();
	}
}
