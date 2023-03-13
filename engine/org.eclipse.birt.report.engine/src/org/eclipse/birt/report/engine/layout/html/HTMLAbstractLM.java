/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.impl.EngineTask;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
import org.eclipse.birt.report.engine.layout.ILayoutManager;
import org.eclipse.birt.report.engine.layout.LayoutUtil;
import org.w3c.dom.css.CSSValue;

public abstract class HTMLAbstractLM implements ILayoutManager {

	protected final static int LAYOUT_MANAGER_UNKNOW = -1;
	protected final static int LAYOUT_MANAGER_LEAF = 0;
	protected final static int LAYOUT_MANAGER_BLOCK = 1;
	protected final static int LAYOUT_MANAGER_PAGE = 2;
	protected final static int LAYOUT_MANAGER_TABLE = 3;
	protected final static int LAYOUT_MANAGER_TABLE_BAND = 4;
	protected final static int LAYOUT_MANAGER_ROW = 5;
	protected final static int LAYOUT_MANAGER_LIST = 6;
	protected final static int LAYOUT_MANAGER_LIST_BAND = 7;
	protected final static int LAYOUT_MANAGER_GROUP = 8;

	// identy the status of layout manager
	protected final static int STATUS_INTIALIZE = 0;
	protected final static int STATUS_START = 1;
	protected final static int STATUS_INPROGRESS = 2;
	protected final static int STATUS_END = 3;
	protected final static int STATUS_END_WITH_PAGE_BREAK = 4;

	protected static Logger logger = Logger.getLogger(HTMLAbstractLM.class.getName());

	protected HTMLReportLayoutEngine engine;

	protected HTMLLayoutContext context;

	protected HTMLAbstractLM parent;

	protected IContent content;

	protected IReportItemExecutor executor;

	protected IContentEmitter emitter;

	protected boolean isVisible;

	protected int status = STATUS_INTIALIZE;

	public HTMLAbstractLM(HTMLLayoutManagerFactory factory) {
		this.engine = factory.getLayoutEngine();
		this.context = engine.getContext();
	}

	public int getType() {
		return LAYOUT_MANAGER_UNKNOW;
	}

	public void initialize(HTMLAbstractLM parent, IContent content, IReportItemExecutor executor,
			IContentEmitter emitter) throws BirtException {
		this.parent = parent;
		this.content = content;
		this.executor = executor;
		this.emitter = emitter;
		this.status = STATUS_INTIALIZE;
		this.allowPageBreak = null;
		this.isVisible = true;
	}

	public HTMLAbstractLM getParent() {
		return parent;
	}

	protected abstract void start(boolean isFirst) throws BirtException;

	protected abstract void end(boolean finished) throws BirtException;

	/**
	 * layout the content and its children.
	 *
	 * It can be called in three status: 1. start, the first time it is called, in
	 * this status, it first check if it need page-break-before,
	 *
	 * 2. inprogress, the second or more time it is called. In this status, it tries
	 * to layout the content and its children to the current page.
	 *
	 * 3. end, the last time it is called. In this status, it means all the content
	 * has been layout, it is the time to handle the page-break-after.
	 */
	@Override
	public boolean layout() throws BirtException {
		switch (status) {
		case STATUS_INTIALIZE:
			// this element is in-visible, just as it doesn't exits.
			// we must traverse all its children (to let the generate
			// engine create all the content).
			if (handleVisibility()) {
				status = STATUS_END;
				return false;
			}
			// we need put it in the new page or there is no
			// space for the content.
			if (isPageBreakBefore()) {
				status = STATUS_START;
				return true;
			}
		case STATUS_START:
			// it is the first time we handle the content
		case STATUS_INPROGRESS:

			start(status != STATUS_INPROGRESS);
			boolean hasNext = layoutChildren();
			end(!hasNext);

			if (isChildrenFinished()) {
				status = STATUS_END;
			} else {
				status = STATUS_INPROGRESS;
			}
			// We need create an extra page for the following elements, so
			// return true for next element.
			if (hasNext || isPageBreakAfter()) {
				return true;
			}
			return false;
		}
		return false;
	}

	protected abstract boolean layoutChildren() throws BirtException;

	protected abstract boolean isChildrenFinished() throws BirtException;

	@Override
	public boolean isFinished() {
		return status == STATUS_END;
	}

	protected IContentEmitter getEmitter() {
		return this.emitter;
	}

	protected boolean isPageBreakBefore() {
		if (canPageBreak()) {
			return needPageBreakBefore();
		}
		return false;
	}

	protected boolean isPageBreakAfter() {
		if (canPageBreak()) {
			return needPageBreakAfter();
		}
		return false;
	}

	protected boolean allowPageBreak() {
		return true;
	}

	private Boolean allowPageBreak;

	protected boolean canPageBreak() {
		// if the context disable the page-break, return directly
		if (!context.allowPageBreak()) {
			return false;
		}

		if (allowPageBreak == null) {
			if (!allowPageBreak()) {
				allowPageBreak = Boolean.FALSE;
			} else if (parent != null) {
				allowPageBreak = parent.canPageBreak();
			} else {
				allowPageBreak = Boolean.TRUE;
			}
		}
		return allowPageBreak.booleanValue();
	}

	protected boolean needPageBreakBefore() {
		if (content == null || content.getContentType() == IContent.CELL_CONTENT) {
			return false;
		}
		if (hasMasterPageChanged()) {
			return true;
		}
		IStyle style = content.getStyle();
		CSSValue pageBreak = style.getProperty(IStyle.STYLE_PAGE_BREAK_BEFORE);
		if (IStyle.ALWAYS_VALUE == pageBreak || IStyle.RIGHT_VALUE == pageBreak || IStyle.LEFT_VALUE == pageBreak
				|| IStyle.SOFT_VALUE == pageBreak) {
			// style.setProperty( IStyle.STYLE_PAGE_BREAK_BEFORE,
			// IStyle.AUTO_VALUE );
			return true;
		}

		if (parent instanceof HTMLListingBandLM) {
			HTMLListingBandLM bandLayout = (HTMLListingBandLM) parent;
			if (isVisible && bandLayout.needSoftPageBreak) {
				if (pageBreak == null || IStyle.AUTO_VALUE.equals(pageBreak)) {
					bandLayout.needSoftPageBreak = false; // reset page break
					return true;
				}
			}
		}
		return false;
	}

	protected boolean needPageBreakAfter() {
		if (content == null || content.getContentType() == IContent.CELL_CONTENT) {
			return false;
		}
		IStyle style = content.getStyle();
		CSSValue pageBreak = style.getProperty(IStyle.STYLE_PAGE_BREAK_AFTER);
		if (IStyle.ALWAYS_VALUE == pageBreak || IStyle.RIGHT_VALUE == pageBreak || IStyle.LEFT_VALUE == pageBreak
				|| IStyle.SOFT_VALUE == pageBreak) {
			// style.setProperty( IStyle.STYLE_PAGE_BREAK_BEFORE,
			// IStyle.AUTO_VALUE );
			return true;
		}
		return false;
	}

	protected boolean hasMasterPageChanged() {
		if (content == null || content.getContentType() == IContent.CELL_CONTENT) {
			return false;
		}
		IStyle style = content.getStyle();
		String newMasterPage = style.getMasterPage();
		if (newMasterPage == null || newMasterPage.length() == 0) {
			return false;
		}
		String masterPage = context.getMasterPage();
		if (!newMasterPage.equalsIgnoreCase(masterPage)) {
			// check if this master exist
			PageSetupDesign pageSetup = content.getReportContent().getDesign().getPageSetup();
			if (pageSetup.getMasterPageCount() > 0) {
				MasterPageDesign masterPageDesign = pageSetup.findMasterPage(newMasterPage);
				if (masterPageDesign != null) {
					context.setNextMasterPage(newMasterPage);
					return true;
				}
			}
		}
		return false;
	}

	protected MasterPageDesign getMasterPage(IReportContent report) {
		String masterPage = context.getMasterPage();
		MasterPageDesign pageDesign = null;
		if (masterPage != null && !"".equals(masterPage)) //$NON-NLS-1$
		{
			pageDesign = report.getDesign().findMasterPage(masterPage);
			if (pageDesign != null) {
				return pageDesign;
			}
		}
		return getDefaultMasterPage(report);
	}

	private MasterPageDesign getDefaultMasterPage(IReportContent report) {
		PageSetupDesign pageSetup = report.getDesign().getPageSetup();
		int pageCount = pageSetup.getMasterPageCount();
		if (pageCount > 0) {
			MasterPageDesign pageDesign = pageSetup.getMasterPage(0);
			context.setMasterPage(pageDesign.getName());
			return pageDesign;
		}
		return null;
	}

	protected boolean handleVisibility() throws BirtException {
		assert content != null;
		assert executor != null;

		// For fixed layout reports and in run task, we need to emit the
		// invisible content to PDF layout engine.
		boolean hiddenMask = context.isFixedLayout()
				&& (Integer) context.getLayoutEngine().getOption(EngineTask.TASK_TYPE) == IEngineTask.TASK_RUN;
		if (LayoutUtil.isHidden(content, emitter.getOutputFormat(), context.getOutputDisplayNone(), hiddenMask)) {
			isVisible = false;
			boolean allowPageBreak = context.allowPageBreak();
			context.setAllowPageBreak(false);
			traverse(executor, content);
			context.setAllowPageBreak(allowPageBreak);
			return true;
		}
		return false;
	}

	protected void startHiddenContent(IContentEmitter emitter, IContent content) throws BirtException {
		if (content != null) {
			switch (content.getContentType()) {
			case IContent.DATA_CONTENT:
			case IContent.LABEL_CONTENT:
			case IContent.TEXT_CONTENT:
			case IContent.IMAGE_CONTENT:
				context.getPageBufferManager().startContent(content, emitter, false);
				break;
			default:
				context.getPageBufferManager().startContainer(content, true, emitter, false);
				break;
			}
		}
	}

	protected void endHiddenContent(IContentEmitter emitter, IContent content) throws BirtException {
		if (content != null) {
			switch (content.getContentType()) {
			case IContent.DATA_CONTENT:
			case IContent.LABEL_CONTENT:
			case IContent.TEXT_CONTENT:
			case IContent.IMAGE_CONTENT:
				break;
			default:
				context.getPageBufferManager().endContainer(content, true, emitter, false);
				break;
			}
		}

	}

	/**
	 * execute the executor, drip all its children contents.
	 *
	 * @param executor
	 */
	private void traverse(IReportItemExecutor executor, IContent content) throws BirtException {
		assert executor != null;
		IContentEmitter emitter = getEmitter();
		if (content != null) {
			startHiddenContent(emitter, content);
		}
		while (executor.hasNextChild()) {
			IReportItemExecutor child = (IReportItemExecutor) executor.getNextChild();
			if (child != null) {
				IContent childContent = child.execute();
				traverse(child, childContent);
				child.close();
			}
		}
		if (content != null) {
			endHiddenContent(emitter, content);
		}
	}

	/**
	 * execute the report and add all its contents into the content.
	 *
	 * @param content
	 * @param executor
	 */
	protected void execute(IContent content, IReportItemExecutor executor) throws BirtException {
		assert executor != null;

		while (executor.hasNextChild()) {
			IReportItemExecutor childExecutor = executor.getNextChild();
			if (childExecutor != null) {
				IContent childContent = childExecutor.execute();
				if (childContent != null) {
					if (!content.getChildren().contains(childContent)) {
						content.getChildren().add(childContent);
					}
				}
				execute(childContent, childExecutor);
				childExecutor.close();
				if (childContent != null) {
					if (!executor.hasNextChild()) {
						childContent.setLastChild(true);
					} else {
						childContent.setLastChild(false);
					}
				}
			}
		}
	}

	@Override
	public void close() throws BirtException {
		engine.getFactory().releaseLayoutManager(this);
	}

	@Override
	public void cancel() {
		status = STATUS_END;
	}

	protected IContent getContent() {
		return content;
	}

}
