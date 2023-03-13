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

import org.eclipse.birt.report.engine.content.IContent;

class PageReader extends ReportItemReader {
	long pageNumber;
	ReportItemReaderManager manager;

	PageReader(AbstractReportReader reportReader, long pageNumber) {
		super(reportReader.context);
		this.reader = reportReader.pageReader;
		this.manager = reportReader.manager;
	}

	@Override
	public IContent execute() {
		context.setPageNumber(pageNumber);
		context.setExecutingMasterPage(true);

		content = super.execute();
		return content;
	}

	@Override
	public void close() {
		context.setExecutingMasterPage(false);
		super.close();
	}

	@Override
	ReportItemReader createExecutor(ReportItemReader parent, long offset, Fragment fragment) {
		return manager.createExecutor(parent, offset, fragment);
	}
}
