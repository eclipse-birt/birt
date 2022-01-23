/***********************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout;

import java.util.Locale;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.presentation.IPageHint;

public interface IReportLayoutEngine {
	void setPageHandler(ILayoutPageHandler handle);

	void layout(IReportExecutor executor, IReportContent report, IContentEmitter output, boolean pagination)
			throws BirtException;

	void layout(ILayoutManager parent, IContent content, IContentEmitter output) throws BirtException;

	void layout(ILayoutManager parent, IReportItemExecutor executor, IContentEmitter output) throws BirtException;

	void cancel();

	void setOption(String name, Object value);

	Object getOption(String name);

	void setLocale(Locale locale);

	void setLayoutPageHint(IPageHint pageHint);

	long getPageCount();

	void setTotalPageCount(long totalPage);

	void close() throws BirtException;
}
