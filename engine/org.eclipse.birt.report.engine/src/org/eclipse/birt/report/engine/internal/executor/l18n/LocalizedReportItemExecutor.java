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

package org.eclipse.birt.report.engine.internal.executor.l18n;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.wrap.WrappedReportItemExecutor;
import org.eclipse.birt.report.engine.presentation.LocalizedContentVisitor;

class LocalizedReportItemExecutor extends WrappedReportItemExecutor {

	LocalizedContentVisitor l18nVisitor;

	LocalizedReportItemExecutor(LocalizedReportExecutor reportExecutor, IReportItemExecutor executor) {
		super(reportExecutor, executor);
		this.l18nVisitor = reportExecutor.l18nVisitor;
	}

	public IContent execute() throws BirtException {
		IContent content = super.execute();
		if (content != null) {
			content = l18nVisitor.localize(content);
		}
		return content;
	}
}
