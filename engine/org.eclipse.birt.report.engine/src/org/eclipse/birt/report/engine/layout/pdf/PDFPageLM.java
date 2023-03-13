/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.ReportExecutorUtil;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.LogicContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;
import org.eclipse.birt.report.engine.layout.content.BlockStackingExecutor;

/**
 *
 * TODO add multi-column support
 */
public class PDFPageLM extends PDFBlockContainerLM implements IBlockStackingLayoutManager {

	final static int DEFAULT_PAGE_WIDTH = 595275;
	final static int DEFAULT_PAGE_HEIGHT = 841889;
	/**
	 * current page area
	 */
	protected PageArea page;

	protected IReportContent report;
	protected IPageContent pageContent;
	protected IReportExecutor reportExecutor = null;
	protected PDFReportLayoutEngine engine;
	protected IContentEmitter emitter;

	private int pageContentWidth = DEFAULT_PAGE_WIDTH;
	private int pageContentHeight = DEFAULT_PAGE_HEIGHT;
	private int rootWidth;
	private int rootHeight;
	private int rootLeft;
	private int rootTop;

	public PDFPageLM(PDFReportLayoutEngine engine, PDFLayoutEngineContext context, IReportContent report,
			IContentEmitter emitter, IReportExecutor executor) {
		super(context, null, null, null);
		this.reportExecutor = executor;
		this.engine = engine;
		this.report = report;
		this.emitter = emitter;
	}

	@Override
	protected void initialize() throws BirtException {
		createRoot();
		context.setMaxHeight(page.getRoot().getHeight());
		context.setMaxWidth(page.getRoot().getWidth());
		layoutHeader();
		layoutFooter();
		updateBodySize(page);
		context.setMaxHeight(page.getBody().getHeight());
		context.setMaxWidth(page.getBody().getWidth());
		maxAvaWidth = context.getMaxWidth();
		if (context.pagebreakPaginationOnly()) {
			maxAvaHeight = Integer.MAX_VALUE;
		} else {
			maxAvaHeight = context.getMaxHeight();
		}
		setCurrentIP(0);
		setCurrentBP(0);
	}

	/**
	 * support body auto resize, remove invalid header and footer
	 *
	 * @param page
	 */
	protected void updateBodySize(PageArea page) {
		IContainerArea header = page.getHeader();
		ContainerArea footer = (ContainerArea) page.getFooter();
		ContainerArea body = (ContainerArea) page.getBody();
		ContainerArea root = (ContainerArea) page.getRoot();
		if (header != null && header.getHeight() >= root.getHeight()) {
			page.removeHeader();
			header = null;
		}
		if (footer != null && footer.getHeight() >= root.getHeight()) {
			page.removeHeader();
			footer = null;
		}
		if (header != null && footer != null && footer.getHeight() + header.getHeight() >= root.getHeight()) {
			page.removeFooter();
			page.removeHeader();
			header = null;
			footer = null;
		}

		body.setHeight(root.getHeight() - (header == null ? 0 : header.getHeight())
				- (footer == null ? 0 : footer.getHeight()));
		body.setPosition(body.getX(), (header == null ? 0 : header.getHeight()));
		if (footer != null) {
			footer.setPosition(footer.getX(),
					(header == null ? 0 : header.getHeight()) + (body == null ? 0 : body.getHeight()));
		}
	}

	/**
	 * layout page header area
	 *
	 */
	protected void layoutHeader() throws BirtException {
		IContent headerContent = pageContent.getPageHeader();
		IReportItemExecutor headerExecutor = new DOMReportItemExecutor(headerContent);
		headerExecutor.execute();
		PDFRegionLM regionLM = new PDFRegionLM(context, page.getHeader(), headerContent, headerExecutor);
		boolean allowPB = context.allowPageBreak();
		context.setAllowPageBreak(false);
		regionLM.layout();
		context.setAllowPageBreak(allowPB);
	}

	/**
	 * layout page footer area
	 *
	 */
	protected void layoutFooter() throws BirtException {
		IContent footerContent = pageContent.getPageFooter();
		IReportItemExecutor footerExecutor = new DOMReportItemExecutor(footerContent);
		footerExecutor.execute();
		PDFRegionLM regionLM = new PDFRegionLM(context, page.getFooter(), footerContent, footerExecutor);
		boolean allowPB = context.allowPageBreak();
		context.setAllowPageBreak(false);
		regionLM.layout();
		context.setAllowPageBreak(allowPB);
	}

	public void removeHeader() {
		page.removeHeader();
	}

	public void removeFooter() {
		page.removeFooter();
	}

	public void floatingFooter() {
		ContainerArea footer = (ContainerArea) page.getFooter();
		IContainerArea body = page.getBody();
		IContainerArea header = page.getHeader();
		if (footer != null) {
			footer.setPosition(footer.getX(),
					(header == null ? 0 : header.getHeight()) + (body == null ? 0 : body.getHeight()));
		}
	}

	@Override
	public boolean layout() throws BirtException {
		if (!context.isCancel()) {
			boolean childBreak;
			startPage();
			childBreak = layoutChildren();
			if (!childBreak) {
				isLast = true;
			}
			endPage();
			return childBreak;
		} else {
			cancel();
			return false;
		}
	}

	protected void pageBreakEvent() {
		ILayoutPageHandler pageHandler = engine.getPageHandler();
		if (pageHandler != null) {
			pageHandler.onPage(context.getPageNumber(), context);
		}
	}

	protected void startPage() throws BirtException {
		MasterPageDesign pageDesign = getMasterPage(report);
		pageContent = ReportExecutorUtil.executeMasterPage(reportExecutor, context.getPageNumber(), pageDesign);
		this.content = pageContent;
	}

	protected void endPage() throws BirtException {
		if (context.isAutoPageBreak()) {
			context.setAutoPageBreak(false);
			autoPageBreak();
		}
		if (isPageEmpty()) {
			if (!isFirst) {
				if (isLast) {
					context.setPageNumber(context.getPageNumber() - 1);
					context.setPageCount(context.getPageCount() - 1);
				}
				return;
			} else if (!isLast) {
				return;
			}
		}

		MasterPageDesign mp = getMasterPage(report);

		if (mp instanceof SimpleMasterPageDesign) {
			if (isFirst && !((SimpleMasterPageDesign) mp).isShowHeaderOnFirst()) {
				removeHeader();
				isFirst = false;
			}
			if (isLast && !((SimpleMasterPageDesign) mp).isShowFooterOnLast()) {
				removeFooter();
			}
			if (((SimpleMasterPageDesign) mp).isFloatingFooter()) {
				floatingFooter();
			}
		}
		if (isFirst) {
			isFirst = false;
		}
		emitter.startPage(pageContent);
		emitter.endPage(pageContent);
		pageBreakEvent();
		if (!isLast) {
			context.setPageNumber(context.getPageNumber() + 1);
			context.setPageCount(context.getPageCount() + 1);
		}
	}

	@Override
	public boolean isPageEmpty() {
		if (page != null) {
			IContainerArea body = page.getBody();
			if (body.getChildrenCount() > 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void createRoot() {
		root = new PageArea(pageContent);
		page = (PageArea) root;

		int overFlowType = context.getPageOverflow();

		if (overFlowType == IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES) {
			page.setExtendToMultiplePages(true);
		}

		pageContentWidth = getDimensionValue(pageContent.getPageWidth());
		pageContentHeight = getDimensionValue(pageContent.getPageHeight());

		// validate page width
		if (pageContentWidth <= 0) {
			pageContentWidth = DEFAULT_PAGE_WIDTH;
		}

		// validate page height
		if (pageContentHeight <= 0) {
			pageContentHeight = DEFAULT_PAGE_HEIGHT;
		}

		page.setWidth(pageContentWidth);
		page.setHeight(pageContentHeight);

		/**
		 * set position and dimension for root
		 */
		ContainerArea pageRoot = new LogicContainerArea(report);

		rootLeft = getDimensionValue(pageContent.getMarginLeft(), pageContentWidth);
		rootTop = getDimensionValue(pageContent.getMarginTop(), pageContentWidth);
		rootLeft = Math.max(0, rootLeft);
		rootLeft = Math.min(pageContentWidth, rootLeft);
		rootTop = Math.max(0, rootTop);
		rootTop = Math.min(pageContentHeight, rootTop);
		pageRoot.setPosition(rootLeft, rootTop);
		int rootRight = getDimensionValue(pageContent.getMarginRight(), pageContentWidth);
		int rootBottom = getDimensionValue(pageContent.getMarginBottom(), pageContentWidth);
		rootRight = Math.max(0, rootRight);
		rootBottom = Math.max(0, rootBottom);
		if (rootLeft + rootRight > pageContentWidth) {
			rootRight = 0;
		}
		if (rootTop + rootBottom > pageContentHeight) {
			rootBottom = 0;
		}

		rootWidth = pageContentWidth - rootLeft - rootRight;
		rootHeight = pageContentHeight - rootTop - rootBottom;
		pageRoot.setWidth(rootWidth);
		pageRoot.setHeight(rootHeight);
		page.setRoot(pageRoot);

		/**
		 * set position and dimension for header
		 */
		int headerHeight = getDimensionValue(pageContent.getHeaderHeight(), pageRoot.getHeight());
		int headerWidth = pageRoot.getWidth();
		headerHeight = Math.max(0, headerHeight);
		headerHeight = Math.min(pageRoot.getHeight(), headerHeight);
		ContainerArea header = new LogicContainerArea(report);
		header.setHeight(headerHeight);
		header.setWidth(headerWidth);
		header.setPosition(0, 0);
		pageRoot.addChild(header);
		page.setHeader(header);

		/**
		 * set position and dimension for footer
		 */
		int footerHeight = getDimensionValue(pageContent.getFooterHeight(), pageRoot.getHeight());
		int footerWidth = pageRoot.getWidth();
		footerHeight = Math.max(0, footerHeight);
		footerHeight = Math.min(pageRoot.getHeight() - headerHeight, footerHeight);
		ContainerArea footer = new LogicContainerArea(report);
		footer.setHeight(footerHeight);
		footer.setWidth(footerWidth);
		footer.setPosition(0, pageRoot.getHeight() - footerHeight);
		pageRoot.addChild(footer);
		page.setFooter(footer);

		/**
		 * set position and dimension for body
		 */
		ContainerArea body = new LogicContainerArea(report);
		int bodyLeft = getDimensionValue(pageContent.getLeftWidth(), pageRoot.getWidth());
		bodyLeft = Math.max(0, bodyLeft);
		bodyLeft = Math.min(pageRoot.getWidth(), bodyLeft);
		body.setPosition(bodyLeft, headerHeight);
		int bodyRight = getDimensionValue(pageContent.getRightWidth(), pageRoot.getWidth());
		bodyRight = Math.max(0, bodyRight);
		bodyRight = Math.min(pageRoot.getWidth() - bodyLeft, bodyRight);

		body.setWidth(pageRoot.getWidth() - bodyLeft - bodyRight);
		body.setHeight(pageRoot.getHeight() - headerHeight - footerHeight);
		page.setBody(body);
		pageRoot.addChild(body);

		if (overFlowType == IPDFRenderOption.CLIP_CONTENT) {
			pageRoot.setNeedClip(true);
			page.getBody().setNeedClip(true);
		} else {
			pageRoot.setNeedClip(false);
		}
		// TODO add left area and right area;

	}

	@Override
	protected IReportItemExecutor createExecutor() {
		return new BlockStackingExecutor(content, new ReportStackingExecutor(reportExecutor));
	}

	@Override
	protected void closeLayout() {
		int overFlowType = context.getPageOverflow();

		if (overFlowType == IPDFRenderOption.FIT_TO_PAGE_SIZE) {
			float scale = calculatePageScale();
			if (1f == scale) {
				return;
			}
			page.setScale(scale);
			updatePageDimension(scale);
		} else if (overFlowType == IPDFRenderOption.ENLARGE_PAGE_SIZE) {
			float scale = calculatePageScale();
			if (1f == scale) {
				return;
			}
			updatePageDimension(scale);
		}
	}

	private float calculatePageScale() {
		float scale = 1.0f;
		if (page != null && page.getRoot().getChildrenCount() > 0) {
			int maxWidth = context.getMaxWidth();
			int maxHeight = context.getMaxHeight();
			int prefWidth = context.getPreferenceWidth();
			int prefHeight = getCurrentBP();
			Iterator iter = page.getBody().getChildren();
			while (iter.hasNext()) {
				AbstractArea area = (AbstractArea) iter.next();
				prefWidth = Math.max(prefWidth, area.getAllocatedWidth());
			}

			if (prefHeight > maxHeight) {
				((ContainerArea) page.getBody()).setHeight(prefHeight);
				floatingFooter();
			}

			if (prefWidth > maxWidth || prefHeight > maxHeight) {
				scale = Math.min(maxWidth / (float) prefWidth, maxHeight / (float) prefHeight);
			}
		}
		return scale;
	}

	protected void updatePageDimension(float scale) {
		// 0 < scale <= 1
		page.setHeight((int) (pageContentHeight / scale));
		page.setWidth((int) (pageContentWidth / scale));
		ContainerArea pageRoot = (ContainerArea) page.getRoot();
		pageRoot.setPosition((int) (rootLeft / scale), (int) (rootTop / scale));
		pageRoot.setHeight((int) (rootHeight / scale));
		pageRoot.setWidth((int) (rootWidth / scale));
	}

	@Override
	protected boolean addToRoot(AbstractArea area) {
		root.addChild(area);
		area.setAllocatedPosition(currentIP + offsetX, currentBP + offsetY);
		currentBP += area.getAllocatedHeight();
		assert root instanceof PageArea;
		AbstractArea body = (AbstractArea) ((PageArea) root).getBody();
		if (currentIP + area.getAllocatedWidth() > root.getContentWidth() - body.getX()) {
			root.setNeedClip(true);
		}

		if (currentBP > maxAvaHeight) {
			root.setNeedClip(true);
		}
		return true;
	}

	@Override
	protected boolean isRootEmpty() {
		if (page != null) {
			IContainerArea body = page.getBody();
			if (body.getChildrenCount() > 0 || isFirst && isLast) {
				return false;
			}
		}
		return true;
	}

}
