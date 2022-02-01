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

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.ReportItemExecutorBase;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;

public class RunInContainerExecutor extends ReportItemExecutorBase {
	protected IReportItemExecutor executor;
	protected IContent content;
	protected LinkedList children = new LinkedList();

	public RunInContainerExecutor(IReportItemExecutor executor, IContent content) throws BirtException {
		this.executor = executor;
		this.content = content;

		boolean first = true;
		while (executor.hasNextChild()) {
			IReportItemExecutor childExecutor = executor.getNextChild();
			IContent childContent = childExecutor.execute();
			if (childContent != null) {
				if (first) {
					transferPageBreak(content, childContent);
					first = false;
				}
				if (PropertyUtil.isInlineElement(childContent)) {
					execute(childExecutor, childContent);
					childExecutor.close();
					children.addLast(new DOMReportItemExecutor(childContent));
				} else {
					children.addLast(new ItemExecutorWrapper(childExecutor, childContent));
					break;
				}
			}
		}
	}

	public void close() throws BirtException {
		executor.close();
	}

	public IContent execute() {
		return content;
	}

	public IReportItemExecutor getNextChild() throws BirtException {
		if (children.size() > 0) {
			return (IReportItemExecutor) children.removeFirst();
		} else {
			while (executor.hasNextChild()) {
				IReportItemExecutor childExecutor = executor.getNextChild();
				IContent childContent = childExecutor.execute();
				if (childContent != null) {
					if (PropertyUtil.isInlineElement(childContent)) {
						execute(childExecutor, childContent);
						childExecutor.close();
						children.addLast(new DOMReportItemExecutor(childContent));
					} else {
						children.addLast(new ItemExecutorWrapper(childExecutor, childContent));
						break;
					}
				}
			}
		}

		if (children.size() > 0) {
			return (IReportItemExecutor) children.removeFirst();
		}
		return null;

	}

	public boolean hasNextChild() throws BirtException {
		return children.size() > 0 || executor.hasNextChild();
	}

	protected void execute(IReportItemExecutor executor, IContent content) throws BirtException {
		while (executor.hasNextChild()) {
			IReportItemExecutor childExecutor = executor.getNextChild();
			if (childExecutor != null) {
				IContent childContent = childExecutor.execute();
				add(content.getChildren(), childContent);
				execute(childExecutor, childContent);
				childExecutor.close();
			}
		}
	}

	private void add(Collection collection, IContent content) {
		if (!collection.contains(content)) {
			collection.add(content);
		}
	}

	protected void transferPageBreak(IContent parent, IContent child) {
		if (parent != null && child != null) {
			IStyle childStyle = child.getStyle();
			IStyle parentStyle = parent.getStyle();
			CSSValue parentPageBreak = parentStyle.getProperty(IStyle.STYLE_PAGE_BREAK_BEFORE);
			if (IStyle.ALWAYS_VALUE.equals(parentPageBreak) || IStyle.SOFT_VALUE.equals(parentPageBreak)) {
				childStyle.setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);
			}
		}

	}

}
