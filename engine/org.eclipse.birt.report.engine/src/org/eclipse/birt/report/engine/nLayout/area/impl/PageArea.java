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

import java.awt.Color;
import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.pdf.emitter.LayoutEmitterAdapter;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.RegionLayoutEngine;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BackgroundImageInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.util.ResourceLocatorWrapper;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.core.Module;

import org.openpdf.text.Image;

/**
 * Definition of the page area
 *
 * @since 3.3
 *
 */
public class PageArea extends BlockContainerArea {

	final static int DEFAULT_PAGE_WIDTH = 595275;
	final static int DEFAULT_PAGE_HEIGHT = 841889;

	private transient boolean extendToMultiplePages = false;

	private transient boolean enlargePageSize = false;

	protected ContainerArea root;
	protected ContainerArea body;
	protected RegionArea header;
	protected RegionArea footer;

	protected transient IPageContent pageContent;

	protected transient LayoutEmitterAdapter emitter;

	private transient int pageContentWidth = DEFAULT_PAGE_WIDTH;
	private transient int pageContentHeight = DEFAULT_PAGE_HEIGHT;
	private transient int rootWidth;
	private transient int rootHeight;
	private transient int rootLeft;
	private transient int rootTop;

	/**
	 * Constructor context based
	 *
	 * @param context
	 * @param content
	 * @param emitter
	 */
	public PageArea(LayoutContext context, IContent content, LayoutEmitterAdapter emitter) {
		super(null, context, content);
		this.emitter = emitter;
		pageContent = (IPageContent) content;
	}

	/**
	 * Constructor page area based
	 *
	 * @param area
	 */
	public PageArea(PageArea area) {
		super(area);
	}

	/**
	 * Get the header
	 *
	 * @return Return the header area
	 */
	public IContainerArea getHeader() {
		return header;
	}

	/**
	 * Verify to use multiple pages
	 *
	 * @return true, extend to multiple pages
	 */
	public boolean isExtendToMultiplePages() {
		return extendToMultiplePages;
	}

	/**
	 * Remove header from page
	 */
	public void removeHeader() {
		root.removeChild(header);
		header = null;
	}

	/**
	 * Remove footer from page
	 */
	public void removeFooter() {
		root.removeChild(footer);
		footer = null;
	}

	/**
	 * Get the page footer
	 *
	 * @return Return page footer
	 */
	public IContainerArea getFooter() {
		return footer;
	}

	/**
	 * Get the page body
	 *
	 * @return Return page body
	 */
	public IContainerArea getBody() {
		return body;
	}

	/**
	 * Get the page root
	 *
	 * @return Return page root
	 */
	public IContainerArea getRoot() {
		return root;
	}

	/**
	 * Set the page root
	 *
	 * @param root
	 */
	public void setRoot(ContainerArea root) {
		this.root = root;
		this.children.add(root);
	}

	/**
	 * Set the page body
	 *
	 * @param body
	 */
	public void setBody(ContainerArea body) {
		if (this.body != null) {
			body.setPosition(this.body.getX(), this.body.getY());
			root.children.remove(this.body);
		}
		root.children.add(body);
		body.setParent(root);
		this.body = body;
	}

	/**
	 * Remove page body
	 */
	public void removeBody() {
		root.children.remove(body);
		this.body = null;
	}

	/**
	 * Set page header
	 *
	 * @param header
	 */
	public void setHeader(RegionArea header) {
		this.header = header;
	}

	/**
	 * Set page footer
	 *
	 * @param footer
	 */
	public void setFooter(RegionArea footer) {
		this.footer = footer;
	}

	/**
	 * Verify if the page enlarging is enabled
	 *
	 * @return true, enlarge page size is used
	 */
	public boolean isEnlargePageSize() {
		return enlargePageSize;
	}

	/**
	 * Set the option to enlarge the page size
	 *
	 * @param enlargePageSize
	 */
	public void setEnlargePageSize(boolean enlargePageSize) {
		this.enlargePageSize = enlargePageSize;
	}

	@Override
	public PageArea cloneArea() {
		return new PageArea(this);
	}

	// support page border on root area
	protected BoxStyle buildRootStyle() {
		IStyle style = pageContent.getStyle();
		if ((style != null) && !style.isEmpty()) {
			BoxStyle boxStyle = new BoxStyle();
			IStyle cs = pageContent.getComputedStyle();
			int borderWidth = getDimensionValue(cs.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH), width);
			if (borderWidth > 0) {
				boxStyle.setLeftBorder(new BorderInfo(cs.getProperty(StyleConstants.STYLE_BORDER_LEFT_COLOR),
						cs.getProperty(StyleConstants.STYLE_BORDER_LEFT_STYLE), borderWidth));

			}

			borderWidth = getDimensionValue(cs.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH), width);
			if (borderWidth > 0) {
				boxStyle.setRightBorder(new BorderInfo(cs.getProperty(StyleConstants.STYLE_BORDER_RIGHT_COLOR),
						cs.getProperty(StyleConstants.STYLE_BORDER_RIGHT_STYLE), borderWidth));

			}
			borderWidth = getDimensionValue(cs.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH), width);
			if (borderWidth > 0) {
				boxStyle.setTopBorder(new BorderInfo(cs.getProperty(StyleConstants.STYLE_BORDER_TOP_COLOR),
						cs.getProperty(StyleConstants.STYLE_BORDER_TOP_STYLE), borderWidth));

			}

			borderWidth = getDimensionValue(cs.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH), width);
			if (borderWidth > 0) {
				boxStyle.setBottomBorder(new BorderInfo(cs.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_COLOR),
						cs.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_STYLE), borderWidth));
			}
			return boxStyle;
		}
		return BoxStyle.DEFAULT;
	}

	@Override
	public void initialize() throws BirtException {
		createRoot();
		Color backgroundColor = PropertyUtil
				.getColor(pageContent.getComputedStyle().getProperty(StyleConstants.STYLE_BACKGROUND_COLOR));
		ReportDesignHandle designHandle = pageContent.getReportContent().getDesign().getReportDesign();
		IStyle style = pageContent.getStyle();

		String imageUrl = EmitterUtil.getBackgroundImageUrl(style, designHandle,
				pageContent.getReportContent().getReportContext() == null ? null
						: pageContent.getReportContent().getReportContext().getAppContext());

		if (backgroundColor != null || imageUrl != null) {
			boxStyle = new BoxStyle();
			boxStyle.setBackgroundColor(backgroundColor);
			if (imageUrl != null) {
				boxStyle.setBackgroundImage(createBackgroundImage(imageUrl, designHandle.getModule()));
			}
		}
		context.setMaxHeight(root.getHeight());
		context.setMaxWidth(root.getWidth());
		context.setMaxBP(root.getHeight());
		layoutHeader();
		layoutFooter();
		updateBodySize();

		context.setMaxHeight(body.getHeight());
		context.setMaxWidth(body.getWidth());

		int overFlowType = context.getPageOverflow();
		if (overFlowType == IPDFRenderOption.FIT_TO_PAGE_SIZE || overFlowType == IPDFRenderOption.ENLARGE_PAGE_SIZE) {
			context.setMaxBP(Integer.MAX_VALUE);
		} else {
			context.setMaxBP(body.getHeight());
		}
		maxAvaWidth = context.getMaxWidth();
		context.resetUnresolvedRowHints();
	}

	protected BackgroundImageInfo createBackgroundImage(String url, Module module) {
		ResourceLocatorWrapper rl = null;
		ExecutionContext exeContext = ((ReportContent) content.getReportContent()).getExecutionContext();
		if (exeContext != null) {
			rl = exeContext.getResourceLocator();
		}

		IStyle cs = pageContent.getComputedStyle();
		BackgroundImageInfo backgroundImage = null;
		backgroundImage = new BackgroundImageInfo(url, cs.getProperty(StyleConstants.STYLE_BACKGROUND_REPEAT),
				PropertyUtil.getDimensionValue(cs.getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_X)),
				PropertyUtil.getDimensionValue(cs.getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_Y)),
				0, 0, rl, module, cs.getProperty(StyleConstants.STYLE_BACKGROUND_IMAGE_TYPE));
		backgroundImage.setImageSize(cs);

		Image img = backgroundImage.getImageInstance();

		IStyle style = pageContent.getStyle();
		String widthStr = style.getBackgroundWidth();
		String heightStr = style.getBackgroundHeight();

		if (img != null) {
			int resolutionX = img.getDpiX();
			int resolutionY = img.getDpiY();
			if (0 == resolutionX || 0 == resolutionY) {
				resolutionX = 96;
				resolutionY = 96;
			}
			float imageWidth = img.getPlainWidth() / resolutionX * 72 * PDFConstants.LAYOUT_TO_PDF_RATIO;
			float imageHeight = img.getPlainHeight() / resolutionY * 72 * PDFConstants.LAYOUT_TO_PDF_RATIO;
			int actualWidth = (int) imageWidth;
			int actualHeight = (int) imageHeight;

			if (widthStr != null && widthStr.length() > 0 || heightStr != null && heightStr.length() > 0) {
				if ("contain".equals(widthStr) || "contain".equals(heightStr)) {
					float rh = imageHeight / height;
					float rw = imageWidth / width;
					if (rh > rw) {
						actualHeight = height;
						actualWidth = (int) (imageWidth * height / imageHeight);
					} else {
						actualWidth = width;
						actualHeight = (int) (imageHeight * width / imageWidth);
					}

				} else if ("cover".equals(widthStr) || "cover".equals(heightStr)) {
					float rh = imageHeight / height;
					float rw = imageWidth / width;
					if (rh > rw) {
						actualWidth = width;
						actualHeight = (int) (imageHeight * width / imageWidth);
					} else {
						actualHeight = height;
						actualWidth = (int) (imageWidth * height / imageHeight);
					}
				} else {
					actualHeight = backgroundImage.getHeightMetricPt();
					actualWidth = backgroundImage.getWidthMetricPt();
				}
			} else {
				actualHeight = backgroundImage.getHeightMetricPt();
				actualWidth = backgroundImage.getWidthMetricPt();
			}

			backgroundImage.setXOffset(
					getDimensionValue(cs.getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_X), width - actualWidth));
			backgroundImage.setYOffset(
					getDimensionValue(cs.getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_Y),
							height - actualHeight));
			backgroundImage.setHeightMetricPt(actualHeight);
			backgroundImage.setWidthMetricPt(actualWidth);

			return backgroundImage;
		}
		return null;
	}

	/**
	 * Support body auto resize, remove invalid header and footer
	 */
	protected void updateBodySize() {
		if (header != null && header.getHeight() >= root.getHeight()) {
			removeHeader();
			header = null;
		}
		if (footer != null && footer.getHeight() >= root.getHeight()) {
			removeFooter();
			footer = null;
		}
		if (header != null && footer != null && footer.getHeight() + header.getHeight() >= root.getHeight()) {
			removeHeader();
		}

		body.setHeight(root.getContentHeight() - (header == null ? 0 : header.getHeight())
				- (footer == null ? 0 : footer.getHeight()));
		body.setPosition(body.getX(),
				(header == null ? 0 : header.getHeight()) + root.getBoxStyle().getTopBorderWidth());
		if (footer != null) {
			footer.setPosition(footer.getX(), (header == null ? 0 : header.getHeight())
					+ root.getBoxStyle().getTopBorderWidth() + (body == null ? 0 : body.getHeight()));
		}
	}

	/**
	 * layout page header area
	 *
	 */
	protected void layoutHeader() {
		IContent headerContent = pageContent.getPageHeader();
		if (headerContent != null) {
			DimensionType h = pageContent.getHeaderHeight();
			if (h == null) {
				h = new DimensionType(0.5f, DimensionType.UNITS_IN);
			}
			headerContent.setHeight(h);
			header.content = headerContent;
			boolean autoPageBreak = context.isAutoPageBreak();
			context.setAutoPageBreak(false);
			RegionLayoutEngine rle = new RegionLayoutEngine(header, context);

			try {
				rle.layout(headerContent);
			} catch (BirtException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			context.setAutoPageBreak(autoPageBreak);

		}
	}

	/**
	 * layout page footer area
	 *
	 */
	protected void layoutFooter() {
		IContent footerContent = pageContent.getPageFooter();
		if (footerContent != null) {
			DimensionType h = pageContent.getFooterHeight();
			if (h == null) {
				h = new DimensionType(0.5f, DimensionType.UNITS_IN);
			}
			footerContent.setHeight(h);
			footer.content = footerContent;
			boolean autoPageBreak = context.isAutoPageBreak();
			context.setAutoPageBreak(false);
			RegionLayoutEngine rle = new RegionLayoutEngine(footer, context);
			try {
				rle.layout(footerContent);
			} catch (BirtException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			context.setAutoPageBreak(autoPageBreak);
		}
	}

	/**
	 * Floating footer
	 *
	 * @param page
	 */
	public void floatingFooter(PageArea page) {
		ContainerArea footer = (ContainerArea) page.getFooter();
		IContainerArea body = page.getBody();
		IContainerArea header = page.getHeader();
		if (footer != null) {
			footer.setPosition(footer.getX(),
					(header == null ? 0 : header.getHeight()) + (body == null ? 0 : body.getHeight()));
		}
	}

	protected void createRoot() {
		int overFlowType = context.getPageOverflow();

		if (overFlowType == IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES) {
			// page.setExtendToMultiplePages( true );
		}

		pageContentWidth = getDimensionValue(pageContent, pageContent.getPageWidth()) - boxStyle.getLeftBorderWidth()
				- boxStyle.getRightBorderWidth();
		pageContentHeight = getDimensionValue(pageContent, pageContent.getPageHeight()) - boxStyle.getTopBorderWidth()
				- boxStyle.getBottomBorderWidth();

		// validate page width
		if (pageContentWidth <= 0) {
			pageContentWidth = DEFAULT_PAGE_WIDTH;
		}

		// validate page height
		if (pageContentHeight <= 0) {
			pageContentHeight = DEFAULT_PAGE_HEIGHT;
		}

		setWidth(pageContentWidth);
		setHeight(pageContentHeight);

		/**
		 * set position and dimension for root
		 */
		ContainerArea pageRoot = new BlockContainerArea();
		BoxStyle boxStyle = buildRootStyle();
		if (boxStyle != BoxStyle.DEFAULT) {
			pageRoot.hasStyle = true;
		}
		pageRoot.setBoxStyle(boxStyle);
		rootLeft = getDimensionValue(pageContent, pageContent.getMarginLeft(), pageContentWidth);
		rootTop = getDimensionValue(pageContent, pageContent.getMarginTop(), pageContentHeight);
		rootLeft = Math.max(0, rootLeft);
		rootLeft = Math.min(pageContentWidth, rootLeft);
		rootTop = Math.max(0, rootTop);
		rootTop = Math.min(pageContentHeight, rootTop);
		pageRoot.setPosition(rootLeft, rootTop);
		int rootRight = getDimensionValue(pageContent, pageContent.getMarginRight(), pageContentWidth);
		int rootBottom = getDimensionValue(pageContent, pageContent.getMarginBottom(), pageContentWidth);
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
		setRoot(pageRoot);
		pageRoot.setParent(this);

		/**
		 * set position and dimension for header
		 */
		int headerHeight = getDimensionValue(pageContent, pageContent.getHeaderHeight(), pageRoot.getHeight());
		int headerWidth = pageRoot.getWidth() - boxStyle.getLeftBorderWidth() - boxStyle.getRightBorderWidth();
		headerHeight = Math.max(0, headerHeight);
		headerHeight = Math.min(pageRoot.getHeight(), headerHeight);
		RegionArea header = new RegionArea(RegionArea.AreaType.HEADER);
		header.setHeight(headerHeight);
		header.setWidth(headerWidth);
		header.context = context;
		header.needClip = true;
		header.setPosition(boxStyle.getLeftBorderWidth(), boxStyle.getTopBorderWidth());
		pageRoot.addChild(header);
		setHeader(header);
		header.setParent(pageRoot);

		/**
		 * set position and dimension for footer
		 */
		int footerHeight = getDimensionValue(pageContent, pageContent.getFooterHeight(), pageRoot.getHeight());
		int footerWidth = pageRoot.getWidth() - boxStyle.getLeftBorderWidth() - boxStyle.getRightBorderWidth();
		footerHeight = Math.max(0, footerHeight);
		footerHeight = Math.min(pageRoot.getHeight() - headerHeight, footerHeight);
		RegionArea footer = new RegionArea(RegionArea.AreaType.FOOTER);
		footer.setHeight(footerHeight);
		footer.setWidth(footerWidth);
		footer.context = context;
		footer.needClip = true;
		footer.setPosition(boxStyle.getLeftBorderWidth(),
				pageRoot.getHeight() - boxStyle.getBottomBorderWidth() - footerHeight);
		pageRoot.addChild(footer);
		setFooter(footer);
		footer.setParent(pageRoot);

		/**
		 * set position and dimension for body
		 */
		ContainerArea body = new BlockContainerArea();
		int bodyLeft = getDimensionValue(pageContent, pageContent.getLeftWidth(), pageRoot.getWidth());
		bodyLeft = Math.max(0, bodyLeft);
		bodyLeft = Math.min(pageRoot.getWidth(), bodyLeft);
		body.setPosition(boxStyle.getLeftBorderWidth() + bodyLeft, headerHeight + boxStyle.getRightBorderWidth());
		int bodyRight = getDimensionValue(pageContent, pageContent.getRightWidth(), pageRoot.getWidth());
		bodyRight = Math.max(0, bodyRight);
		bodyRight = Math.min(pageRoot.getWidth() - bodyLeft, bodyRight);

		body.setWidth(pageRoot.getWidth() - bodyLeft - bodyRight - boxStyle.getLeftBorderWidth()
				- boxStyle.getRightBorderWidth());
		body.setHeight(pageRoot.getHeight() - headerHeight - footerHeight - boxStyle.getTopBorderWidth()
				- boxStyle.getBottomBorderWidth());
		setBody(body);
		if (overFlowType == IPDFRenderOption.CLIP_CONTENT
				|| overFlowType == IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES) {
			pageRoot.setNeedClip(true);
			body.setNeedClip(true);
		} else {
			pageRoot.setNeedClip(false);
		}
		// TODO add left area and right area;
	}

	@Override
	public void close() throws BirtException {
		int overFlowType = context.getPageOverflow();
		if (overFlowType == IPDFRenderOption.FIT_TO_PAGE_SIZE) {
			float scale = calculatePageScale(this);
			if (1f == scale) {
				pageContent.setExtension(IContent.LAYOUT_EXTENSION, this);
				outputPage(pageContent);
				return;
			}
			this.setScale(scale);
			getBody().setNeedClip(false);
			updatePageDimension(scale, this);
		} else if (overFlowType == IPDFRenderOption.ENLARGE_PAGE_SIZE) {
			getBody().setNeedClip(false);
			updatePageDimension(this);
		}

		pageContent.setExtension(IContent.LAYOUT_EXTENSION, this);
		outputPage(pageContent);
		finished = true;
	}

	/**
	 * Verify if the page is empty
	 *
	 * @return true, the page is empty
	 */
	public boolean isPageEmpty() {
		if (body.getChildrenCount() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * Genereate the output page
	 *
	 * @param page
	 * @throws BirtException
	 */
	public void outputPage(IPageContent page) throws BirtException {
		FixedLayoutPageHintGenerator gen = context.getPageHintGenerator();
		if (null != gen) {
			gen.generatePageHints(page);
		}
		emitter.outputPage(page);
		// context.pageNumber++;
	}

	private float calculatePageScale(PageArea page) {
		float scale = 1.0f;
		if (page != null && page.getRoot().getChildrenCount() > 0) {
			int maxWidth = context.getMaxWidth();
			int maxHeight = context.getMaxHeight();
			int prefWidth = context.getPreferenceWidth();
			int prefHeight = body.getHeight();
			Iterator<IArea> iter = page.getBody().getChildren();
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
			int prefHeight = page.getBody().getHeight();
			Iterator<IArea> iter = page.getBody().getChildren();
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

}
