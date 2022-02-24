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

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;

public class ReportReader extends AbstractReportReader {

	IReportExecutor executor = null;

	BodyReader bodyReader;

	public ReportReader(ExecutionContext context) throws IOException, BirtException {
		super(context);
		bodyReader = new BodyReader(this, null);
	}

	public IReportContent execute() {
		return reportContent;
	}

	public IReportItemExecutor getNextChild() {
		return bodyReader.getNextChild();
	}

	public boolean hasNextChild() {
		return bodyReader.hasNextChild();
	}

	public void close() {
		bodyReader.close();
		super.close();

	}
}
