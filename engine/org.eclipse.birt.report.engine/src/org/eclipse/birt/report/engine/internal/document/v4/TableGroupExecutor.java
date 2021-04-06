/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
