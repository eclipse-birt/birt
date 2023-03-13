/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.LogicContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

import com.ibm.icu.util.ULocale;

public class PageLayout extends BlockStackingLayout {
	final static int DEFAULT_PAGE_WIDTH = 595275;
	final static int DEFAULT_PAGE_HEIGHT = 841889;

	protected IReportContent report;
	protected IPageContent pageContent;
	protected IReportExecutor reportExecutor = null;
	protected IContentEmitter emitter;

	private int pageContentWidth = DEFAULT_PAGE_WIDTH;
	private int pageContentHeight = DEFAULT_PAGE_HEIGHT;
	private int rootWidth;
	private int rootHeight;
	private int rootLeft;
	private int rootTop;

	protected MasterPageDesign masterPage = null;

	public PageLayout(IReportExecutor executor, LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
		this.reportExecutor = executor;
		pageContent = (IPageContent) content;
		masterPage = (MasterPageDesign) pageContent.getGenerateBy();
		report = pageContent.getReportContent();
	}

	@Override
	protected void initialize() throws BirtException {
		PageContext pageContext = new PageContext();
		if (context.autoPageBreak) {
			context.pageNumber++;
			pageContext.pageContent = createPageContent(pageContent);
		} else {
			pageContext.pageContent = pageContent;
		}
		currentContext = pageContext;
		contextList.add(currentContext);
		createRoot();
		PageArea page = (PageArea) currentContext.root;
		context.setMaxHeight(page.getRoot().getHeight());
		context.setMaxWidth(page.getRoot().getWidth());
		layoutHeader(page);
		layoutFooter(page);
		updateBodySize(page);
		context.setMaxHeight(page.getBody().getHeight());
		context.setMaxWidth(page.getBody().getWidth());
		currentContext.maxAvaWidth = context.getMaxWidth();

		if (context.autoPageBreak) {
			currentContext.maxAvaHeight = context.getMaxHeight();
		} else {
			currentContext.maxAvaHeight = Integer.MAX_VALUE;
		}
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
	protected void layoutHeader(PageArea page) throws BirtException {
		IContent headerContent = ((PageContext) currentContext).pageContent.getPageHeader();
		Layout regionLayout = new RegionLayout(context, headerContent, page.getHeader());
		regionLayout.layout();

	}

	/**
	 * layout page footer area
	 *
	 */
	protected void layoutFooter(PageArea page) throws BirtException {
		IContent footerContent = ((PageContext) currentContext).pageContent.getPageFooter();
		Layout regionLayout = new RegionLayout(context, footerContent, page.getFooter());
		regionLayout.layout();
	}

	public void floatingFooter(PageArea page) {
		ContainerArea footer = (ContainerArea) page.getFooter();
		IContainerArea body = page.getBody();
		IContainerArea header = page.getHeader();
		if (footer != null) {
			footer.setPosition(footer.getX(),
					(header == null ? 0 : header.getHeight()) + (body == null ? 0 : body.getHeight()));
		}
	}

	@Override
	protected void createRoot() {
		currentContext.root = new PageArea(((PageContext) currentContext).pageContent);
		PageArea page = (PageArea) currentContext.root;

		int overFlowType = context.getPageOverflow();

		if (overFlowType == IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES) {
			// page.setExtendToMultiplePages( true );
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
		ContainerArea pageRoot = (ContainerArea) AreaFactory.createBlockContainer(pageContent);

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
		IStyle pageStyle = pageContent.getComputedStyle();
		int topBorderWidth = PropertyUtil
				.getDimensionValue(pageStyle.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH));
		int bottomBorderWidth = PropertyUtil
				.getDimensionValue(pageStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH));
		int leftBorderWidth = PropertyUtil
				.getDimensionValue(pageStyle.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH));
		int rightBorderWidth = PropertyUtil
				.getDimensionValue(pageStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH));
		int horizontalBorderWidth = leftBorderWidth + rightBorderWidth;
		headerHeight = Math.max(0, headerHeight);
		headerHeight = Math.min(pageRoot.getHeight(), headerHeight);
		ContainerArea header = new LogicContainerArea(report);
		header.setHeight(headerHeight - topBorderWidth);
		header.setWidth(headerWidth - horizontalBorderWidth);
		header.setPosition(leftBorderWidth, topBorderWidth);
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
		footer.setHeight(footerHeight - bottomBorderWidth);
		footer.setWidth(footerWidth - horizontalBorderWidth);
		footer.setPosition(leftBorderWidth, pageRoot.getHeight() - footerHeight - bottomBorderWidth);
		pageRoot.addChild(footer);
		page.setFooter(footer);

		/**
		 * set position and dimension for body
		 */
		ContainerArea body = new LogicContainerArea(report);
		int bodyLeft = getDimensionValue(pageContent.getLeftWidth(), pageRoot.getWidth());
		bodyLeft = Math.max(0, bodyLeft);
		bodyLeft = Math.min(pageRoot.getWidth(), bodyLeft);
		body.setPosition(bodyLeft + leftBorderWidth, headerHeight);
		int bodyRight = getDimensionValue(pageContent.getRightWidth(), pageRoot.getWidth());
		bodyRight = Math.max(0, bodyRight);
		bodyRight = Math.min(pageRoot.getWidth() - bodyLeft, bodyRight);

		body.setWidth(pageRoot.getWidth() - bodyLeft - bodyRight - horizontalBorderWidth);
		body.setHeight(pageRoot.getHeight() - headerHeight - footerHeight);
		page.setBody(body);
		pageRoot.addChild(body);

		if (overFlowType == IPDFRenderOption.CLIP_CONTENT
				|| overFlowType == IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES) {
			pageRoot.setNeedClip(true);
			page.getBody().setNeedClip(true);
		} else {
			pageRoot.setNeedClip(false);
		}
		// TODO add left area and right area;
	}

	@Override
	protected void closeLayout(ContainerContext currentContext, int index, boolean finished) throws BirtException {
		PageArea page = (PageArea) currentContext.root;
		int overFlowType = context.getPageOverflow();
		context.setFinished(finished);
		if (overFlowType == IPDFRenderOption.FIT_TO_PAGE_SIZE) {
			float scale = calculatePageScale(currentContext, page);
			if (1f == scale) {
				((PageContext) currentContext).pageContent.setExtension(IContent.LAYOUT_EXTENSION, page);
				outputPage(((PageContext) currentContext).pageContent);
				return;
			}
			page.setScale(scale);
			updatePageDimension(scale, page);
		} else if (overFlowType == IPDFRenderOption.ENLARGE_PAGE_SIZE) {
			updatePageDimension(page);
		}

		((PageContext) currentContext).pageContent.setExtension(IContent.LAYOUT_EXTENSION, page);
		outputPage(((PageContext) currentContext).pageContent);
		if (contextList.size() > 0) {
			this.currentContext = contextList.get(0);
		}
	}

	@Override
	public boolean isPageEmpty() {
		PageArea page = (PageArea) currentContext.root;
		if (page != null) {
			IContainerArea body = page.getBody();
			if (body.getChildrenCount() > 0) {
				return false;
			}
		}
		return true;
	}

	/*
	 * public void flushPage() { int size = contextList.size( ); if ( size > 0 ) {
	 * closeLayout( size, false ); } }
	 *
	 * public void flushFinishedPage() { int size = contextList.size( ) - 1; if (
	 * size > 0 ) { closeLayout( size, false ); } }
	 */

	public void outputPage(IPageContent page) throws BirtException {
		LayoutEmitterAdapter emitter = context.emitter;
		emitter.outputPage(page);
		ILayoutPageHandler pageHandler = emitter.getPageHandler();
		if (pageHandler != null) {
			pageHandler.onPage(context.pageNumber, context);
		}
		// context.pageNumber++;
	}

	private float calculatePageScale(ContainerContext currentContext, PageArea page) {
		float scale = 1.0f;
		if (page != null && page.getRoot().getChildrenCount() > 0) {
			int maxWidth = context.getMaxWidth();
			int maxHeight = context.getMaxHeight();
			int prefWidth = context.getPreferenceWidth();
			int prefHeight = currentContext.currentBP;
			Iterator iter = page.getBody().getChildren();
			while (iter.hasNext()) {
				AbstractArea area = (AbstractArea) iter.next();
				prefWidth = Math.max(prefWidth, area.getAllocatedX() + area.getAllocatedWidth());
			}

			if (prefHeight > maxHeight) {
				((ContainerArea) page.getBody()).setHeight(prefHeight);
				floatingFooter(page);
			}

			if (prefWidth > maxWidth || prefHeight > maxHeight) {
				scale = Math.min(maxWidth / (float) prefWidth, maxHeight / (float) prefHeight);
			}
		}
		return scale;
	}

	protected void updatePageDimension(float scale, PageArea page) {
		// 0 < scale <= 1
		page.setHeight((int) (pageContentHeight / scale));
		page.setWidth((int) (pageContentWidth / scale));
		ContainerArea pageRoot = (ContainerArea) page.getRoot();
		pageRoot.setPosition((int) (rootLeft / scale), (int) (rootTop / scale));
		pageRoot.setHeight((int) (rootHeight / scale));
		pageRoot.setWidth((int) (rootWidth / scale));
	}

	protected void updatePageDimension(PageArea page) {
		if (page != null && page.getRoot().getChildrenCount() > 0) {
			int maxWidth = context.getMaxWidth();
			int maxHeight = context.getMaxHeight();
			int prefWidth = context.getPreferenceWidth(); // 0
			int prefHeight = currentContext.currentBP;
			Iterator iter = page.getBody().getChildren();
			while (iter.hasNext()) {
				AbstractArea area = (AbstractArea) iter.next();
				prefWidth = Math.max(prefWidth, area.getAllocatedX() + area.getAllocatedWidth());
			}

			if (prefHeight > maxHeight) {
				((ContainerArea) page.getBody()).setHeight(prefHeight);
				floatingFooter(page);
				int deltaHeight = prefHeight - maxHeight;
				ContainerArea pageRoot = (ContainerArea) page.getRoot();
				pageRoot.setHeight(pageRoot.getHeight() + deltaHeight);
				page.setHeight(pageContentHeight + deltaHeight);
			}

			if (prefWidth > maxWidth) {
				((ContainerArea) page.getBody()).setWidth(prefWidth);
				int deltaWidth = prefWidth - maxWidth;
				ContainerArea pageRoot = (ContainerArea) page.getRoot();
				pageRoot.setWidth(pageRoot.getWidth() + deltaWidth);
				page.setWidth(pageContentWidth + deltaWidth);
			}
		}

	}

	@Override
	public void addToRoot(AbstractArea area) {
		currentContext.root.addChild(area);
		area.setAllocatedPosition(currentContext.currentIP + offsetX, currentContext.currentBP + offsetY);
		currentContext.currentBP += area.getAllocatedHeight();
		assert currentContext.root instanceof PageArea;
		AbstractArea body = (AbstractArea) ((PageArea) currentContext.root).getBody();
		if (currentContext.currentIP + area.getAllocatedWidth() > currentContext.root.getContentWidth() - body.getX()) {
			currentContext.root.setNeedClip(true);
		}

		if (currentContext.currentBP > currentContext.maxAvaHeight) {
			currentContext.root.setNeedClip(true);
		}
	}

	protected IPageContent createPageContent(IPageContent htmlPageContent) {
		if (context.pageNumber == htmlPageContent.getPageNumber()) {
			return htmlPageContent;
		} else {
			IPageContent pageContent = createPageContent(htmlPageContent, context.pageNumber, context.totalPage);
			return pageContent;
		}
	}

	protected IPageContent createPageContent(IPageContent pageContent, long pageNumber, long totalPageNumber) {
		return (IPageContent) cloneContent(pageContent, pageNumber, totalPageNumber);
	}

	protected IContent cloneContent(IContent content, long pageNumber, long totalPageNumber) {
		IContent newContent = content.cloneContent(false);
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
			IContent newChild = cloneContent(child, pageNumber, totalPageNumber);
			newChild.setParent(newContent);
			newContent.getChildren().add(newChild);
		}
		return newContent;
	}

	protected MasterPageDesign getMasterPage(IReportContent report) {
		if (masterPage != null) {
			return masterPage;
		}
		return getDefaultMasterPage(report);
	}

	protected MasterPageDesign getDefaultMasterPage(IReportContent report) {
		PageSetupDesign pageSetup = report.getDesign().getPageSetup();
		int pageCount = pageSetup.getMasterPageCount();
		if (pageCount > 0) {
			MasterPageDesign pageDesign = pageSetup.getMasterPage(0);
			return pageDesign;
		}
		return null;
	}

	class PageContext extends ContainerContext {
		IPageContent pageContent;
	}

}
