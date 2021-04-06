/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public IContent execute() {
		context.setPageNumber(pageNumber);
		context.setExecutingMasterPage(true);

		content = super.execute();
		return content;
	}

	public void close() {
		context.setExecutingMasterPage(false);
		super.close();
	}

	ReportItemReader createExecutor(ReportItemReader parent, long offset, Fragment fragment) {
		return manager.createExecutor(parent, offset, fragment);
	}
}
