/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.layout.content;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.ReportItemExecutorBase;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public class BlockStackingExecutor extends ReportItemExecutorBase {
	protected IContent content;
	protected IReportItemExecutor executor;
	protected IReportItemExecutor childExecutor;
	protected IContent childContent;
	protected boolean needUpdate = true;
	protected boolean hasNext = false;

	public BlockStackingExecutor(IContent content, IReportItemExecutor executor) {
		this.content = content;
		this.executor = executor;
	}

	@Override
	public void close() throws BirtException {
		executor.close();
	}

	@Override
	public IContent execute() {
		return content;
	}

	@Override
	public IReportItemExecutor getNextChild() throws BirtException {
		IReportItemExecutor ret = null;
		if (childContent != null) {
			ret = new ItemExecutorWrapper(childExecutor, childContent);
			childContent = null;
			childExecutor = null;
		} else {
			IReportItemExecutor childExecutor = executor.getNextChild();
			if (childExecutor != null) {
				IContent childContent = childExecutor.execute();
				if (childContent != null) {
					if (PropertyUtil.isInlineElement(childContent)) {
						ret = new LineStackingExecutor(new ItemExecutorWrapper(childExecutor, childContent), this);
					} else {
						ret = new ItemExecutorWrapper(childExecutor, childContent);
					}
				}
			}
		}
		needUpdate = true;
		return ret;
	}

	@Override
	public boolean hasNextChild() throws BirtException {
		if (needUpdate) {
			if (childContent != null) {
				hasNext = true;
			} else {
				hasNext = executor.hasNextChild();
			}
			needUpdate = false;
		}
		return hasNext;
	}

	public IReportItemExecutor nextInline() throws BirtException {
		if (executor.hasNextChild()) {
			IReportItemExecutor nextExecutor = (IReportItemExecutor) executor.getNextChild();
			IContent nextContent = nextExecutor.execute();

			if (PropertyUtil.isInlineElement(nextContent)) {
				return new ItemExecutorWrapper(nextExecutor, nextContent);
			} else {
				this.childContent = nextContent;
				this.childExecutor = nextExecutor;
			}
		}
		return null;
	}
}
