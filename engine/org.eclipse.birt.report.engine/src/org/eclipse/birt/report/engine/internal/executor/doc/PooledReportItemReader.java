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

package org.eclipse.birt.report.engine.internal.executor.doc;

public class PooledReportItemReader extends ReportItemReader {

	ReportItemReaderManager manager;

	PooledReportItemReader(ReportItemReaderManager manager) {
		super(manager.context);
		this.manager = manager;
	}

	ReportItemReader createExecutor(ReportItemReader parent, long offset, Fragment fragment) {
		return manager.createExecutor(parent, offset, fragment);
	}

	public void close() {
		super.close();
		manager.releaseExecutor(this);
	}

}
