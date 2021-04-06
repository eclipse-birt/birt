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
