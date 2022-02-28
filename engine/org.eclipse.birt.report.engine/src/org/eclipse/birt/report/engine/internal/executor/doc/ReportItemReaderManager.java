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

import java.util.LinkedList;

import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * Manager used to create the report item readers.
 *
 * It use a free list to store the unused readres, and after the reader is
 * closed, it should be return to the freelist, so that it can be resued by
 * others.
 *
 * Once the caller close the report item reader, the reader can't be used any
 * more.
 */
class ReportItemReaderManager {

	protected LinkedList freeList = new LinkedList();
	protected ExecutionContext context;

	ReportItemReaderManager(ExecutionContext context) {
		this.context = context;
	}

	ReportItemReader createExecutor(ReportItemReader parent, long offset) {
		return createExecutor(parent, offset, null);
	}

	ReportItemReader createExecutor(ReportItemReader parent, long offset, Fragment frag) {
		ReportItemReader executor = null;
		if (!freeList.isEmpty()) {
			executor = (ReportItemReader) freeList.removeFirst();
		} else {
			executor = new PooledReportItemReader(this);
		}
		executor.initialize(parent, offset, frag);
		return executor;
	}

	void releaseExecutor(ReportItemReader executor) {
		freeList.addLast(executor);
	}
}
