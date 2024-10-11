/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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
package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.executor.ReportExecutorUtil;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.layout.pdf.emitter.LayoutEmitterAdapter;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;

import com.ibm.icu.util.ULocale;

/**
 * Definition of the root area
 *
 * @since 3.3
 *
 */
public class RootArea extends BlockContainerArea {

	protected transient LayoutEmitterAdapter emitter;

	protected PageArea page;

	/**
	 * Constructor context based
	 *
	 * @param context
	 * @param content
	 * @param emitter
	 */
	public RootArea(LayoutContext context, IContent content, LayoutEmitterAdapter emitter) {
		super(null, context, content);
		this.emitter = emitter;
	}

	/**
	 * Constructor based on root area
	 *
	 * @param area
	 */
	public RootArea(RootArea area) {
		super(area);
	}

	@Override
	public int getMaxAvaHeight() {
		return context.getMaxBP();
	}

	@Override
	public boolean autoPageBreak() throws BirtException {
		int height = context.getMaxBP();
		SplitResult result = split(height, false);

		if (result == SplitResult.BEFORE_AVOID_WITH_NULL || result == SplitResult.SUCCEED_WITH_NULL) {
			result = split(height, true);
		}
		if (result.getResult() != null) {
			// If size overflow, one row may move to next page. Set a flag to
			// handle it properly later
			context.setSizeOverflowPageBreak(true);

			page.setBody(result.getResult());
			page.close();

			// Reset overflow page break state
			context.setSizeOverflowPageBreak(false);
		}
		updateChildrenPosition();
		initialize();
		return true;
	}

	@Override
	public RootArea cloneArea() {
		return new RootArea(this);
	}

	@Override
	public void initialize() throws BirtException {
		IPageContent pageContent = (IPageContent) content;

		if (context.getEngineTaskType() == IEngineTask.TASK_RENDER) {
			if (context.isFixedLayout()) {
				if (context.isReserveDocumentPageNumbers() && context.getHtmlLayoutContext() != null
						&& context.getHtmlLayoutContext().isPaged()) {
					long number = pageContent.getPageNumber();
					if (number > 0) {
						context.setPageNumber(number);
					}
				} else {
					context.setPageNumber(context.getPageNumber() + 1);
					pageContent = createPageContent(pageContent);
				}
			} else {
				if (context.isReserveDocumentPageNumbers()) {
					long number = pageContent.getPageNumber();
					if (number > 0) {
						context.setPageNumber(number);
					}
				} else {
					context.setPageNumber(context.getPageNumber() + 1);
					pageContent = createPageContent(pageContent);
				}
			}
		} else if (context.isAutoPageBreak()) {
			context.setPageNumber(context.getPageNumber() + 1);
			pageContent = createPageContent(pageContent);
		} else {
			long number = pageContent.getPageNumber();
			if (number > 0) {
				context.setPageNumber(number);
			}
		}

		createNewPage(pageContent);
		maxAvaWidth = page.getBody().getWidth();
		// this.height = page.getBody( ).getHeight( );
		width = maxAvaWidth;
	}

	protected void createNewPage(IPageContent pageContent) throws BirtException {
		page = new PageArea(context, pageContent, emitter);
		page.initialize();
	}

	protected IPageContent createPageContent(IPageContent htmlPageContent) {
		if (context.getPageNumber() == htmlPageContent.getPageNumber()) {
			return htmlPageContent;
		} else if (context.getEngineTaskType() == IEngineTask.TASK_RUNANDRENDER) {

			IPageContent pageContent = (IPageContent) cloneContent((IContent) htmlPageContent.getParent(),
					htmlPageContent, context.getPageNumber(), context.getTotalPage());
			pageContent.setPageNumber(context.getPageNumber());
			return pageContent;
		} else {
			IPageContent pageContent = htmlPageContent;
			try {
				pageContent = ReportExecutorUtil.executeMasterPage(context.getHtmlLayoutContext().getReportExecutor(),
						context.getPageNumber(), (MasterPageDesign) pageContent.getGenerateBy());
				HTMLLayoutContext htmlContext = context.getHtmlLayoutContext();
				if (htmlContext != null && htmlContext.needLayoutPageContent()) {
					htmlContext.getPageLM().layoutPageContent(pageContent);
				}
			} catch (BirtException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			return pageContent;
		}
	}

	protected IContent cloneContent(IContent parent, IContent content, long pageNumber, long totalPageNumber) {
		IContent newContent = content.cloneContent(false);
		newContent.setParent(parent);
		if (newContent.getContentType() == IContent.AUTOTEXT_CONTENT) {
			IAutoTextContent autoText = (IAutoTextContent) newContent;
			int type = autoText.getType();
			if (type == IAutoTextContent.PAGE_NUMBER || type == IAutoTextContent.UNFILTERED_PAGE_NUMBER) {
				DataFormatValue format = autoText.getComputedStyle().getDataFormat();
				NumberFormatter nf = null;
				if (format == null) {
					nf = new NumberFormatter();
				} else {
					String pattern = format.getNumberPattern();
					String locale = format.getNumberLocale();
					if (locale == null) {
						nf = new NumberFormatter(pattern);
					} else {
						nf = new NumberFormatter(pattern, new ULocale(locale));
					}
				}
				autoText.setText(nf.format(pageNumber));
			}
		}
		Iterator iter = content.getChildren().iterator();
		while (iter.hasNext()) {
			IContent child = (IContent) iter.next();
			IContent newChild = cloneContent(newContent, child, pageNumber, totalPageNumber);
			newChild.setParent(newContent);
			newContent.getChildren().add(newChild);
		}
		return newContent;
	}

	@Override
	public void close() throws BirtException {
		page.setBody(this);
		page.close();
		finished = true;
	}

	public String getTagType() {
		return null;
	}

}
