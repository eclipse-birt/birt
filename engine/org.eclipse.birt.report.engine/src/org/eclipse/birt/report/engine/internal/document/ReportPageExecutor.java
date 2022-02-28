/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document;

import java.io.IOException;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.document.v3.ReportPageExecutorV3;
import org.eclipse.birt.report.engine.internal.document.v4.ReportPageExecutorV4;
import org.eclipse.birt.report.engine.presentation.IPageHint;

public class ReportPageExecutor extends ReportExecutorWrapper {

	public ReportPageExecutor(ExecutionContext context, List pages, boolean paged) throws BirtException {
		try {
			int version = getVersion(context.getReportDocument());
			switch (version) {
			case EXECUTOR_VERSION_3:
				executor = new ReportPageExecutorV3(context, pages, paged);
				break;
			case EXECUTOR_VERSION_4:
				executor = new ReportPageExecutorV4(context, pages, paged);
				break;
			default:
				throw new EngineException(MessageConstants.UNSUPPORTED_DOCUMENT_VERSION_ERROR, version);
			}
		} catch (IOException ex) {
			throw new EngineException(ex.getLocalizedMessage(), ex);
		}

	}

	public IPageHint getLayoutPageHint(long pageNumber) throws IOException {
		if (executor != null) {
			if (executor instanceof ReportPageExecutorV3) {
				return ((ReportPageExecutorV3) executor).getPageHint(pageNumber);
			} else if (executor instanceof ReportPageExecutorV4) {
				return ((ReportPageExecutorV4) executor).getPageHint(pageNumber);
			}
		}
		return null;
	}

}
