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

public class BodyReader extends ReportItemReader {

	ReportItemReaderManager manager;

	public BodyReader(AbstractReportReader reportReader, Fragment fragment) {
		super(reportReader.context);
		this.reader = reportReader.reader;
		this.manager = reportReader.manager;
		this.fragment = fragment;
		Fragment firstChild = fragment.getFirstFragment();
		if (firstChild != null) {
			this.child = ((Long) firstChild.getOffset()).longValue();
		} else {
			this.child = -1;
		}
	}

	public IContent execute() {
		return context.getReportContent().getRoot();
	}

	ReportItemReader createExecutor(ReportItemReader parent, long offset, Fragment fragment) {
		return manager.createExecutor(parent, offset, fragment);
	}
}
