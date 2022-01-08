/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document.v4;

import java.util.ArrayList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.impl.AbstractBandContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.content.impl.TableGroupContent;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.internal.executor.wrap.WrappedReportExecutor;
import org.w3c.dom.css.CSSValue;

public class ReportletBodyExecutor implements IReportItemExecutor {

	long offset;
	IReportItemExecutor bodyExecutor;
	ArrayList<IReportItemExecutor> parentExecutors = new ArrayList<IReportItemExecutor>();
	IContent bodyContent;
	IReportItemExecutor childExecutor;
	WrappedReportExecutor reportExecutor;

	ReportletBodyExecutor(ExecutorManager manager, Fragment fragment, long offset) throws BirtException {
		this.bodyExecutor = new ReportBodyExecutor(manager, fragment);
		this.reportExecutor = new WrappedReportExecutor(manager.reportExecutor);
		this.offset = offset;
		parentExecutors.add(bodyExecutor);
		doExecute();
	}

	public void close() throws BirtException {
		if (!parentExecutors.isEmpty()) {
			for (IReportItemExecutor executor : parentExecutors) {
				executor.close();
			}
			parentExecutors.clear();
		}
		bodyExecutor = null;
		childExecutor = null;
	}

	protected void doExecute() throws BirtException {
		IReportItemExecutor executor = bodyExecutor;
		IContent content = null;
		executor.execute();
		while (executor.hasNextChild()) {
			executor = executor.getNextChild();
			parentExecutors.add(executor);
			content = executor.execute();
			DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
			{
				if (docExt != null) {
					if (docExt.getIndex() == offset) {
						if (content instanceof TableGroupContent || content instanceof RowContent
								|| content instanceof CellContent || content instanceof AbstractBandContent) {
							do {
								content = (IContent) content.getParent();
								// wrap parent executor which has only one child
								WrappedExecutor parentExecutor = new WrappedExecutor(executor.getParent(), content,
										executor);
								executor = parentExecutor;
							} while (!(content instanceof TableContent));
						}
						bodyContent = content;
						childExecutor = executor;
						break;
					}
				}
			}
		}
		IStyle cs = bodyContent.getComputedStyle();
		IStyle is = bodyContent.getInlineStyle();
		CSSEngine engine = bodyContent.getCSSEngine();
		IStyle mergedStyle = (is != null ? is : new StyleDeclaration(engine));
		for (int i = 0; i < StyleConstants.NUMBER_OF_STYLE; i++) {
			if (isNullValue(mergedStyle.getProperty(i)) && engine.isInheritedProperty(i)) {
				mergedStyle.setProperty(i, cs.getProperty(i));
			}
		}
		bodyContent.setInlineStyle(mergedStyle);
	}

	private boolean isNullValue(CSSValue value) {
		if (value == null) {
			return true;
		}

		if (value instanceof DataFormatValue) {
			return true;
		}

		String cssText = value.getCssText();
		return "none".equalsIgnoreCase(cssText) || "transparent".equalsIgnoreCase(cssText);
	}

	public IContent execute() {
		return null;
	}

	public IContent getContent() {
		return bodyContent;
	}

	public IExecutorContext getContext() {
		return bodyExecutor.getContext();
	}

	public Object getModelObject() {
		return bodyExecutor.getModelObject();
	}

	public IReportItemExecutor getNextChild() {
		if (childExecutor != null) {
			IReportItemExecutor executor = childExecutor;
			childExecutor = null;
			return executor;
		}
		return null;
	}

	public IReportItemExecutor getParent() {
		return null;
	}

	public IBaseResultSet[] getQueryResults() {
		return null;
	}

	public boolean hasNextChild() {
		return childExecutor != null;
	}

	public void setContext(IExecutorContext context) {
	}

	public void setModelObject(Object handle) {
	}

	public void setParent(IReportItemExecutor parent) {

	}

	static class WrappedExecutor implements IReportItemExecutor {
		IContent content;
		IReportItemExecutor executor;
		IReportItemExecutor childExecutor;

		WrappedExecutor(IReportItemExecutor executor, IContent content, IReportItemExecutor childExecutor) {
			this.content = content;
			this.executor = executor;
			this.childExecutor = childExecutor;
		}

		public void close() throws BirtException {
		}

		public IContent execute() {
			return content;
		}

		public IContent getContent() {
			return content;
		}

		public IExecutorContext getContext() {
			return executor.getContext();
		}

		public Object getModelObject() {
			return executor.getModelObject();
		}

		public IReportItemExecutor getNextChild() {
			if (childExecutor != null) {
				IReportItemExecutor executor = childExecutor;
				childExecutor = null;
				return executor;
			}
			return null;
		}

		public IReportItemExecutor getParent() {
			return executor.getParent();
		}

		public IBaseResultSet[] getQueryResults() {
			return executor.getQueryResults();
		}

		public boolean hasNextChild() {
			return childExecutor != null;
		}

		public void setContext(IExecutorContext context) {
		}

		public void setModelObject(Object handle) {
		}

		public void setParent(IReportItemExecutor parent) {
		}
	}
}
