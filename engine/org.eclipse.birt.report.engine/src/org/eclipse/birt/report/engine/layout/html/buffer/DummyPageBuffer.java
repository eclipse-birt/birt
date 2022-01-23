/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.html.buffer;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.ReportExecutorUtil;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.LayoutUtil;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;

public class DummyPageBuffer implements IPageBuffer {

	protected HTMLLayoutContext context;
	protected IReportExecutor executor;
	protected boolean isFirstContent = false;
	protected IContent pageContent = null;
	protected IContentEmitter pageEmitter = null;

	public DummyPageBuffer(HTMLLayoutContext context, IReportExecutor executor) {
		this.context = context;
		this.executor = executor;
	}

	public void endContainer(IContent content, boolean finished, IContentEmitter emitter, boolean visible)
			throws BirtException {
		if (!visible) {
			return;
		}
		if (isFirstContent) {
			startPageContent(content);
			isFirstContent = false;
		}
		if (emitter != null) {
			if (content.getContentType() == IContent.PAGE_CONTENT) {
				ContentEmitterUtil.endContent(pageContent, emitter);
				pageBreakEvent();
				context.getPageHintManager().clearPageHint();
			} else {
				ContentEmitterUtil.endContent(content, emitter);
			}
		}

	}

	protected void pageBreakEvent() {
		long pageNumber = context.getPageNumber();
		ILayoutPageHandler pageHandler = context.getLayoutEngine().getPageHandler();
		if (pageHandler != null) {
			pageHandler.onPage(pageNumber, context);
		}

	}

	public void startContainer(IContent content, boolean isFirst, IContentEmitter emitter, boolean visible)
			throws BirtException {
		if (!visible) {
			return;
		}
		if (content.getContentType() == IContent.PAGE_CONTENT) {
			isFirstContent = true;
			pageContent = content;
			pageEmitter = emitter;
		} else {
			if (isFirstContent) {
				startPageContent(content);
				isFirstContent = false;
			}
			if (emitter != null) {
				ContentEmitterUtil.startContent(content, emitter);
			}
		}
	}

	public void startContent(IContent content, IContentEmitter emitter, boolean visible) throws BirtException {
		if (!visible) {
			return;
		}
		if (isFirstContent) {
			startPageContent(content);
			isFirstContent = false;
		}
		if (emitter != null) {
			ContentEmitterUtil.startContent(content, emitter);
			ContentEmitterUtil.endContent(content, emitter);
		}

	}

	public boolean isRepeated() {
		return false;
	}

	public void setRepeated(boolean isRepeated) {

	}

	protected void startPageContent(IContent firstContent) throws BirtException {
		String masterPage = null;
		IStyle style = firstContent.getStyle();
		if (style != null) {
			masterPage = style.getMasterPage();
		}
		if (pageContent == null || pageEmitter == null) {
			return;
		}
		if (masterPage == null || "".equals(masterPage)) {
			ContentEmitterUtil.startContent(pageContent, pageEmitter);
		} else {
			Object mp = pageContent.getGenerateBy();
			if (mp != null && mp instanceof SimpleMasterPageDesign) {
				String mpStr = ((SimpleMasterPageDesign) mp).getName();
				if (masterPage.equals(mpStr)) {
					ContentEmitterUtil.startContent(pageContent, pageEmitter);
				} else {
					IReportContent report = pageContent.getReportContent();
					MasterPageDesign defaultMasterPage = LayoutUtil.getDefaultMasterPage(report);
					if (defaultMasterPage.getName().equals(masterPage)) {
						ContentEmitterUtil.startContent(pageContent, pageEmitter);
					} else {
						pageContent = ReportExecutorUtil.executeMasterPage(executor, context.getPageNumber(),
								LayoutUtil.getMasterPage(report, masterPage));
						if (pageContent != null && context.needLayoutPageContent()) {
							context.getPageLM().layoutPageContent((IPageContent) pageContent);
						}
						if (pageContent != null) {
							ContentEmitterUtil.startContent(pageContent, pageEmitter);
						}
					}
				}
			}

		}
	}

	public void closePage(INode[] nodeList) {
		// TODO Auto-generated method stub

	}

	public boolean finished() {
		// TODO Auto-generated method stub
		return false;
	}

	public void flush() {
		// TODO Auto-generated method stub

	}

	public void openPage(INode[] nodeList) {
		// TODO Auto-generated method stub

	}

	public INode[] getNodeStack() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addTableColumnHint(TableColumnHint hint) {
		// TODO Auto-generated method stub

	}

}
