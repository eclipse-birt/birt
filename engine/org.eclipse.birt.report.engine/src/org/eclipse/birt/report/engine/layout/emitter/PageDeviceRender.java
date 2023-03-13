/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.emitter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.emitter.TableBorder.Border;
import org.eclipse.birt.report.engine.layout.emitter.TableBorder.BorderSegment;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;
import org.eclipse.birt.report.engine.nLayout.area.ITemplateArea;
import org.eclipse.birt.report.engine.nLayout.area.ITextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.CellArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.RowArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TextArea;
import org.eclipse.birt.report.engine.nLayout.area.style.AreaConstants;
import org.eclipse.birt.report.engine.nLayout.area.style.BackgroundImageInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.nLayout.area.style.DiagonalInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.engine.util.FlashFile;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * Definition of the page device renderer
 *
 * @since 3.3
 *
 */
public abstract class PageDeviceRender implements IAreaVisitor {
	/**
	 * The default image folder
	 */
	public static final String IMAGE_FOLDER = "image"; //$NON-NLS-1$

	/**
	 * The value of "horizontal text space"
	 */
	public static final int H_TEXT_SPACE = 30;

	/**
	 * The value of "vertical text space"
	 */
	public static final int V_TEXT_SPACE = 100;

	/**
	 * The value of "ignore overflow"
	 */
	public static final int ignoredOverflow = 3000;

	protected float scale;

	protected IReportRunnable reportRunnable;

	protected ReportDesignHandle reportDesign;

	protected IReportContext context;

	protected IEmitterServices services;

	protected int currentX;
	protected int currentY;

	protected Stack<BoxStyle> rowStyleStack = new Stack<BoxStyle>();

	/**
	 * for any (x,y) in the ContainerArea, if x<offsetX, the (x,y) will be omitted.
	 */
	protected int offsetX = 0;

	/**
	 * for any (x,y) in the ContainerArea, if y<offsetY, the (x,y) will be omitted.
	 */
	protected int offsetY = 0;

	protected Logger logger = Logger.getLogger(PageDeviceRender.class.getName());

	protected IPageDevice pageDevice;

	protected IPage pageGraphic;

	/**
	 * Gets the output format.
	 *
	 * @return Return the output format
	 */
	public abstract String getOutputFormat();

	/**
	 * Create the page device
	 *
	 * @param title
	 * @param author
	 * @param subject
	 * @param description
	 * @param context
	 * @param report
	 * @return Return the created page device
	 * @throws Exception
	 */
	public abstract IPageDevice createPageDevice(String title, String author, String subject, String description,
			IReportContext context, IReportContent report) throws Exception;

	/**
	 * Creates a document and create a PdfWriter
	 *
	 * @param rc the report content.
	 */
	public void start(IReportContent rc) {
		ReportDesignHandle designHandle = rc.getDesign().getReportDesign();
		String title = rc.getTitle();
		String author = designHandle.getAuthor();
		String description = designHandle.getDescription();
		String subject = designHandle.getSubject();
		try {
			pageDevice = createPageDevice(title, author, subject, description, context, rc);
		} catch (Exception e) {
			log(e, Level.SEVERE);
		}
	}

	protected void log(Throwable t, Level level) {
		logger.log(level, t.getMessage(), t);
	}

	/**
	 * Closes the document.
	 *
	 * @param rc the report content.
	 */
	public void end(IReportContent rc) {
		try {
			pageDevice.close();
		} catch (Exception e) {
			log(e, Level.WARNING);
		}
	}

	/**
	 * Set the total page
	 *
	 * @param totalPage
	 */
	public void setTotalPage(ITextArea totalPage) {
	}

	@Override
	public void visitText(ITextArea textArea) {
		drawText(textArea);
	}

	@Override
	public void visitImage(IImageArea imageArea) {
		drawImage(imageArea);
	}

	@Override
	public void visitAutoText(ITemplateArea templateArea) {
	}

	/**
	 * Visits a container
	 *
	 * @param container
	 */
	@Override
	public void visitContainer(IContainerArea container) {
		if (container instanceof PageArea) {
			visitPage((PageArea) container);
		} else {
			startContainer(container);
			visitChildren(container);
			endContainer(container);
		}
	}

	protected void visitChildren(IContainerArea container) {
		Iterator<IArea> iter = container.getChildren();
		while (iter.hasNext()) {
			IArea child = iter.next();
			child.accept(this);
		}
	}

	private static final int BODY_HEIGHT = 1;
	private static final int BODY_WIDTH = 2;

	private int getActualPageBodyWidth(PageArea page) {
		return getActualPageBodySize(page, BODY_WIDTH);
	}

	private int getActualPageBodyHeight(PageArea page) {
		return getActualPageBodySize(page, BODY_HEIGHT);
	}

	private int getActualPageBodySize(PageArea page, int direction) {
		int pref = 0;
		IContainerArea body = page.getBody();
		if (body == null) {
			return 0;
		}
		Iterator<IArea> iter = page.getBody().getChildren();
		while (iter.hasNext()) {
			AbstractArea area = (AbstractArea) iter.next();
			if (direction == BODY_HEIGHT) {
				pref = Math.max(pref, area.getY() + area.getHeight());
			} else {
				pref = Math.max(pref, area.getX() + area.getWidth());
			}
		}
		return pref;
	}

	/**
	 * The container may be a TableArea, RowArea, etc. Or just the border of
	 * textArea/imageArea. This method draws the border and background of the given
	 * container.
	 *
	 * @param container the ContainerArea specified from layout
	 */
	protected void startContainer(IContainerArea container) {
		if (container.needClip()) {
			startClip(container);
		}
		if (container instanceof RowArea) {
			rowStyleStack.push(container.getBoxStyle());
		} else if (container instanceof CellArea) {
			drawCell((CellArea) container);
		}

		else {
			drawContainer(container);
		}
		currentX += getX(container);
		currentY += getY(container);
	}

	protected void drawCellDiagonal(CellArea cell) {
		DiagonalInfo diagonalInfo = cell.getDiagonalInfo();
		if (diagonalInfo != null) {
			int startX = currentX + getX(cell);
			int startY = currentY + getY(cell);

			// the dimension of the container
			int width = getWidth(cell);
			int height = getHeight(cell);
			int dw = diagonalInfo.getDiagonalWidth();
			int ds = diagonalInfo.getDiagonalStyle();
			// support double style, use solid style instead.
			if (ds == AreaConstants.BORDER_STYLE_DOUBLE) {
				ds = AreaConstants.BORDER_STYLE_SOLID;
			}
			switch (diagonalInfo.getDiagonalNumber()) {
			case 2:
				pageGraphic.drawLine(startX + width / 2, startY, startX + width, startY + height - dw / 2,
						getScaledValue(dw), diagonalInfo.getDiagonalColor(), ds);
				pageGraphic.drawLine(startX, startY + height / 2, startX + width, startY + height - dw / 2,
						getScaledValue(dw), diagonalInfo.getDiagonalColor(), ds);
				break;
			case 1:
				pageGraphic.drawLine(startX, startY + dw / 2, startX + width, startY + height - dw / 2,
						getScaledValue(dw), diagonalInfo.getDiagonalColor(), ds);
				break;

			default:
				pageGraphic.drawLine(startX, startY + dw / 2, startX + width, startY + height - dw / 2,
						getScaledValue(dw), diagonalInfo.getDiagonalColor(), ds);
				pageGraphic.drawLine(startX + width / 2, startY + dw / 2, startX + width, startY + height - dw / 2,
						getScaledValue(dw), diagonalInfo.getDiagonalColor(), ds);
				pageGraphic.drawLine(startX, startY + height / 2, startX + width, startY + height - dw / 2,
						getScaledValue(dw), diagonalInfo.getDiagonalColor(), ds);
				break;
			}
			// currently only support diagonal line, do not support antidiagonal line
			/*
			 * dw = diagonalInfo.getAntidiagonalWidth( ); ds =
			 * diagonalInfo.getAntidiagonalStyle( ); // support double style, use solid
			 * style instead. if ( ds == DiagonalInfo.BORDER_STYLE_DOUBLE ) { ds =
			 * DiagonalInfo.BORDER_STYLE_SOLID; } switch (
			 * diagonalInfo.getAntidiagonalNumber( ) ) {
			 *
			 * case 2 : pageGraphic .drawLine( startX, startY + height - dw / 2, startX +
			 * width / 2, startY + dw / 2, getScaledValue( diagonalInfo
			 * .getAntidiagonalWidth( ) ), diagonalInfo.getColor( ), ds ); pageGraphic
			 * .drawLine( startX, startY + height - dw / 2, startX + width, startY + height
			 * / 2, getScaledValue( diagonalInfo .getAntidiagonalWidth( ) ),
			 * diagonalInfo.getColor( ), ds ); break; case 3 : pageGraphic .drawLine(
			 * startX, startY + height - dw / 2, startX + width / 2, startY + dw / 2,
			 * getScaledValue( diagonalInfo .getAntidiagonalWidth( ) ),
			 * diagonalInfo.getColor( ), ds ); pageGraphic .drawLine( startX, startY +
			 * height - dw / 2, startX + width, startY + height / 2, getScaledValue(
			 * diagonalInfo .getAntidiagonalWidth( ) ), diagonalInfo.getColor( ), ds );
			 * pageGraphic .drawLine( startX, startY + height - dw / 2, startX + width,
			 * startY + dw / 2, getScaledValue( diagonalInfo .getAntidiagonalWidth( ) ),
			 * diagonalInfo.getColor( ), ds ); break; default : pageGraphic .drawLine(
			 * startX, startY + height - dw / 2, startX + width, startY + dw / 2,
			 * getScaledValue( diagonalInfo .getAntidiagonalWidth( ) ),
			 * diagonalInfo.getColor( ), ds ); break; }
			 */
		}
	}

	protected void drawCell(CellArea container) {
		drawCellDiagonal(container);
		Color rowbc = null;
		BackgroundImageInfo rowbi = null;
		BoxStyle rowStyle = null;
		// get the style of the row
		if (rowStyleStack.size() > 0) {
			rowStyle = rowStyleStack.peek();
			if (rowStyle != null) {
				rowbc = rowStyle.getBackgroundColor();
				rowbi = rowStyle.getBackgroundImage();
			}
		}

		BoxStyle style = container.getBoxStyle();
		Color bc = style.getBackgroundColor();
		BackgroundImageInfo bi = style.getBackgroundImage();
		// String imageUrl = EmitterUtil.getBackgroundImageUrl( style,reportDesign );

		if (rowbc != null || rowbi != null || bc != null || bi != null) {
			// the container's start position (the left top corner of the
			// container)
			int startX = currentX + getX(container);
			int startY = currentY + getY(container);

			// the dimension of the container
			int width = getWidth(container);
			int height = getHeight(container);

			if (rowbc != null) {
				pageGraphic.drawBackgroundColor(rowbc, startX, startY, width, height);
			}
			if (rowbi != null) {
				drawBackgroundImage(rowbi, startX, startY, width, height);
			}
			if (bc != null) {
				// Draws background color for the container, if the background
				// color is NOT set, draws nothing.
				pageGraphic.drawBackgroundColor(bc, startX, startY, width, height);
			}
			if (bi != null) {
				// Draws background image for the container. if the background
				// image is NOT set, draws nothing.
				drawBackgroundImage(bi, startX, startY, width, height);
			}
		}

	}

	/**
	 * Output a layout PageArea, extend the pageArea into multiple physical pages if
	 * needed.
	 *
	 * @param page
	 */
	protected void visitPage(PageArea page) {
		scale = page.getScale();
		if (page.isExtendToMultiplePages()) {
			// the actual used page body size.
			int pageBodyHeight = getActualPageBodyHeight(page);
			int pageBodyWidth = getActualPageBodyWidth(page);
			// get the user defined page body size.
			IContainerArea pageBody = page.getBody();
			int definedBodyHeight = 0;
			int definedBodyWidth = 0;
			if (pageBody != null) {
				definedBodyHeight = pageBody.getHeight();
				definedBodyWidth = pageBody.getWidth();
			}

			if (pageBodyHeight > definedBodyHeight) {
				addExtendDirection(EXTEND_ON_VERTICAL);
			}
			if (pageBodyWidth > definedBodyWidth) {
				addExtendDirection(EXTEND_ON_HORIZONTAL);
			}

			offsetX = 0;
			offsetY = 0;
			if (extendDirection == EXTEND_NONE) {
				addPage(page);
			} else if (extendDirection == EXTEND_ON_HORIZONTAL) {
				do {
					addPage(page);
					offsetX += definedBodyWidth;
				} while (offsetX < pageBodyWidth - ignoredOverflow);
			}

			else if (extendDirection == EXTEND_ON_VERTICAL) {
				do {
					addPage(page);
					offsetY += definedBodyHeight;
				} while (offsetY < pageBodyHeight - ignoredOverflow);
			}

			else if (extendDirection == EXTEND_ON_HORIZONTAL_AND_VERTICAL) {
				do {
					do {
						addPage(page);
						offsetX += definedBodyWidth;
					} while (offsetX < pageBodyWidth - ignoredOverflow);
					offsetX = 0;
					offsetY += definedBodyHeight;
				} while (offsetY < pageBodyHeight - ignoredOverflow);
			}
			setExtendDirection(EXTEND_NONE);
		} else {
			addPage(page);
		}
	}

	/**
	 * Creates a page in given output format.
	 *
	 * @param page a layout page.
	 */
	protected void addPage(PageArea page) {
		// PageArea -> pageRoot -> Header/footer/body
		newPage(page);
		currentX = 0;
		currentY = 0;
		IContainerArea pageRoot = page.getRoot();

		if (pageRoot != null) {
			startContainer(page.getRoot());
			IContainerArea pageHeader = page.getHeader();
			if (pageHeader != null) {
				visitContainer(pageHeader);
			}
			IContainerArea pageFooter = page.getFooter();
			if (pageFooter != null) {
				visitContainer(pageFooter);
			}
			IContainerArea pageBody = page.getBody();
			if (pageBody != null) {
				startContainer(pageBody);
				enterBody();
				visitChildren(pageBody);
				exitBody();
				endContainer(pageBody);
			}
			endContainer(page.getRoot());
		}

		endContainer(page);
	}

	private void enterBody() {
		currentX -= offsetX;
		currentY -= offsetY;
	}

	private void exitBody() {
		currentX += offsetX;
		currentY += offsetY;
	}

	/**
	 * This method will be invoked while a containerArea ends.
	 *
	 * @param container the ContainerArea specified from layout
	 */
	protected void endContainer(IContainerArea container) {
		currentX -= getX(container);
		currentY -= getY(container);

		if (container instanceof PageArea) {
			pageGraphic.dispose();
		} else {
			if (container instanceof RowArea) {
				rowStyleStack.pop();
			}
			if (container instanceof TableArea) {
				drawTableBorder((TableArea) container);
			} else if (!(container instanceof CellArea)) {
				BorderInfo[] borders = cacheBorderInfo(container);
				drawBorder(borders);
			}
			if (container.needClip()) {
				endClip();
			}
		}
	}

	/**
	 * Creates a new PDF page
	 *
	 * @param page the PageArea specified from layout
	 */
	protected void newPage(IContainerArea page) {
		int pageHeight = getHeight(page);
		int pageWidth = getWidth(page);

		BoxStyle style = page.getBoxStyle();
		Color backgroundColor = style.getBackgroundColor();
		pageGraphic = pageDevice.newPage(pageWidth, pageHeight, backgroundColor);
		BackgroundImageInfo bi = style.getBackgroundImage();
		if (bi != null) {
			// Draws background image for the new page. if the background image
			// is NOT set, draw nothing.
			drawBackgroundImage(bi, 0, 0, pageWidth, pageHeight);
		}

	}

	private int extendDirection = EXTEND_NONE;
	/**
	 * the page extend "none"
	 */
	public static final int EXTEND_NONE = 0;
	/**
	 * the page extend "horizontal"
	 */
	public static final int EXTEND_ON_HORIZONTAL = 1;
	/**
	 * the page extend "vertical"
	 */
	public static final int EXTEND_ON_VERTICAL = 2;
	/**
	 * the page extend "horizontal" & "vertical
	 */
	public static final int EXTEND_ON_HORIZONTAL_AND_VERTICAL = 3;

	protected int getExtendDirection() {
		return this.extendDirection;
	}

	protected void setExtendDirection(int direction) {
		this.extendDirection = direction;
	}

	protected void addExtendDirection(int direction) {
		this.extendDirection |= direction;
	}

	/**
	 * Start the area cliping
	 *
	 * @param area
	 */
	public void startClip(IArea area) {
		int startX = currentX + getX(area);
		int startY = currentY + getY(area);
		int width = getWidth(area);
		int height = getHeight(area);
		pageGraphic.startClip(startX, startY, width, height);
	}

	private void endClip() {
		pageGraphic.endClip();
	}

	/**
	 * draw background image for the container
	 *
	 * @param bi     background information object of the background image
	 * @param startX the absolute horizontal position of the container
	 * @param startY the absolute vertical position of the container
	 * @param width  container width
	 * @param height container height
	 */
	public void drawBackgroundImage(BackgroundImageInfo bi, int startX, int startY, int width, int height) {
		try {
			pageGraphic.drawBackgroundImage(startX, startY, width, height, bi.getWidth(), bi.getHeight(),
					bi.getRepeatedMode(), bi.getUrl(), bi.getImageData(), getScaledValue(bi.getXOffset()),
					getScaledValue(bi.getYOffset()));
		} catch (Exception e) {
			log(e, Level.WARNING);
		}
	}

	/**
	 * Draws a container's border, and its background color/image if there is any.
	 *
	 * @param container the containerArea whose border and background need to be
	 *                  drew
	 */
	protected void drawContainer(IContainerArea container) {
		// get the style of the container
		BoxStyle style = container.getBoxStyle();
		if ((null == style || style == BoxStyle.DEFAULT) && container.getHelpText() == null) {
			return;
		}

		// Draws background color for the container, if the background
		// color is NOT set, draws nothing.
		Color bc = style.getBackgroundColor();
		BackgroundImageInfo bi = style.getBackgroundImage();

		// the container's start position (the left top corner of the
		// container)
		int startX = currentX + getX(container);
		int startY = currentY + getY(container);


		// the dimension of the container
		int width = getWidth(container);
		int height = getHeight(container);

		if (bc != null) {
			pageGraphic.drawBackgroundColor(bc, startX, startY, width, height);
		}
		if (bi != null) {
			// Draws background image for the container. if the
			// background image is NOT set, draws nothing.
			drawBackgroundImage(bi, startX, startY, width, height);
		}
		if (container.getHelpText() != null) {
			// shows the help text for the container.
			pageGraphic.showHelpText(container.getHelpText(), startX, startY, width, height);
		}

	}

	private BorderInfo[] cacheCellBorder(CellArea container) {
		// get the style of the container
		BoxStyle style = container.getBoxStyle();
		if ((null == style) || (container.getContent() == null)) {
			return null;
		}
		// FIXME refactor and perform enhancement
		// the width of each border
		int borderTopWidth = getScaledValue(style.getTopBorderWidth());
		int borderLeftWidth = getScaledValue(style.getLeftBorderWidth());
		int borderBottomWidth = getScaledValue(style.getBottomBorderWidth());
		int borderRightWidth = getScaledValue(style.getRightBorderWidth());

		if (borderTopWidth > 0 || borderLeftWidth > 0 || borderBottomWidth > 0 || borderRightWidth > 0) {
			// Caches the border info
			BorderInfo[] borders = new BorderInfo[4];
			borders[BorderInfo.TOP_BORDER] = new BorderInfo(0, 0, 0, 0, borderTopWidth, style.getTopBorderColor(),
					style.getTopBorderStyle(), BorderInfo.TOP_BORDER);
			borders[BorderInfo.RIGHT_BORDER] = new BorderInfo(0, 0, 0, 0, borderRightWidth, style.getRightBorderColor(),
					style.getRightBorderStyle(), BorderInfo.RIGHT_BORDER);
			borders[BorderInfo.BOTTOM_BORDER] = new BorderInfo(0, 0, 0, 0, borderBottomWidth,
					style.getBottomBorderColor(), style.getBottomBorderStyle(), BorderInfo.BOTTOM_BORDER);
			borders[BorderInfo.LEFT_BORDER] = new BorderInfo(0, 0, 0, 0, borderLeftWidth, style.getLeftBorderColor(),
					style.getLeftBorderStyle(), BorderInfo.LEFT_BORDER);
			return borders;
		}
		return null;
	}

	/**
	 * Cache border info
	 *
	 * @param container
	 * @return Return array with border info
	 */
	public BorderInfo[] cacheBorderInfo(IContainerArea container) {
		// get the style of the container
		BoxStyle style = container.getBoxStyle();
		if (null == style || BoxStyle.DEFAULT == style) {
			return null;
		}
		// the width of each border
		int borderTopWidth = getScaledValue(style.getTopBorderWidth());
		int borderLeftWidth = getScaledValue(style.getLeftBorderWidth());
		int borderBottomWidth = getScaledValue(style.getBottomBorderWidth());
		int borderRightWidth = getScaledValue(style.getRightBorderWidth());

		if (borderTopWidth > 0 || borderLeftWidth > 0 || borderBottomWidth > 0 || borderRightWidth > 0) {
			int startX = currentX + getX(container);
			int startY = currentY + getY(container);

			// Caches the border info
			BorderInfo[] borders = new BorderInfo[4];
			borders[BorderInfo.TOP_BORDER] = new BorderInfo(startX, startY + borderTopWidth / 2,
					startX + getWidth(container), startY + borderTopWidth / 2, borderTopWidth,
					style.getTopBorderColor(), style.getTopBorderStyle(), BorderInfo.TOP_BORDER);
			borders[BorderInfo.RIGHT_BORDER] = new BorderInfo(startX + getWidth(container) - borderRightWidth / 2,
					startY, startX + getWidth(container) - borderRightWidth / 2, startY + getHeight(container),
					borderRightWidth, style.getRightBorderColor(), style.getRightBorderStyle(),
					BorderInfo.RIGHT_BORDER);
			borders[BorderInfo.BOTTOM_BORDER] = new BorderInfo(startX,
					startY + getHeight(container) - borderBottomWidth / 2, startX + getWidth(container),
					startY + getHeight(container) - borderBottomWidth / 2, borderBottomWidth,
					style.getBottomBorderColor(), style.getBottomBorderStyle(), BorderInfo.BOTTOM_BORDER);
			borders[BorderInfo.LEFT_BORDER] = new BorderInfo(startX + borderLeftWidth / 2, startY,
					startX + borderLeftWidth / 2, startY + getHeight(container), borderLeftWidth,
					style.getLeftBorderColor(), style.getLeftBorderStyle(), BorderInfo.LEFT_BORDER);
			return borders;
		}
		return null;
	}

	/**
	 * Draws a text area.
	 *
	 * @param text the textArea to be drawn.
	 */
	protected void drawText(ITextArea text) {
		if (text.needClip()) {
			startClip(text);
		}
		TextStyle style = text.getTextStyle();
		assert style != null;

		int textX = currentX + getX(text);
		int textY = currentY + getY(text);
		// style.getFontVariant(); small-caps or normal
		float fontSize = style.getFontInfo().getFontSize();
		int x = textX + getScaledValue((int) (fontSize * H_TEXT_SPACE));
		int y = textY + getScaledValue((int) (fontSize * V_TEXT_SPACE));
		if (scale != 1.0) {
			FontInfo fontInfo = new FontInfo(style.getFontInfo());
			fontInfo.setFontSize(fontInfo.getFontSize() * scale);
			style = new TextStyle(style);
			style.setFontInfo(fontInfo);
			if (style.getLetterSpacing() != 0 || style.getWordSpacing() != 0) {
				style.setLetterSpacing(getScaledValue(style.getLetterSpacing()));
				style.setWordSpacing(getScaledValue(style.getWordSpacing()));
			}
		}
		if (text instanceof TextArea) {
			TextArea ta = (TextArea) text;
			if ((ta.getRunLevel() & 1) != 0) {
				style = new TextStyle(style);
				style.setDirection(AreaConstants.DIRECTION_RTL);
			}
		}
		drawTextAt(text, x, y, getWidth(text), getHeight(text), style);
		if (text.needClip()) {
			endClip();
		}
	}

	protected void drawTextAt(ITextArea text, int x, int y, int width, int height, TextStyle textStyle) {
		pageGraphic.drawText(text.getText(), x, y, width, height, textStyle);
	}

	/**
	 * Draws image at the contentByte
	 *
	 * @param image the ImageArea specified from the layout
	 */
	protected void drawImage(IImageArea image) {
		int imageX = currentX + getX(image);
		int imageY = currentY + getY(image);
		int height = getHeight(image);
		int width = getWidth(image);
		String helpText = image.getHelpText();

		try {
			byte[] data = image.getImageData();
			String extension = image.getExtension();
			String uri = image.getImageUrl();
			if (FlashFile.isFlash(null, null, extension)) {
				ContainerArea parent = ((AbstractArea) image).getParent();
				int xFromClipParent = getX(image);
				int yFromClipParent = getY(image);
				while (parent != null && !parent.needClip()) {
					xFromClipParent += getX(parent);
					yFromClipParent += getY(parent);
					parent = parent.getParent();
				}

				if (parent != null) {
					// found a parent needs to be clipped
					if (getHeight(parent) < yFromClipParent || getWidth(parent) < xFromClipParent) {
						return;
					}
				}
			}
			pageGraphic.drawImage(uri, data, extension, imageX, imageY, height, width, helpText, image.getParameters());
		} catch (Throwable t) {
			log(t, Level.WARNING);
		}
	}

	private void drawBorder(TableBorder tb) {
		if (null == tb) {
			return;
		}

		tb.findBreakPoints();
		Border border = null;
		// draw column borders
		for (Iterator<?> i = tb.columnBorders.keySet().iterator(); i.hasNext();) {
			Integer pos = (Integer) i.next();
			if (pos == tb.tableLRX) {
				continue;
			}
			border = (Border) tb.columnBorders.get(pos);
			for (int j = 0; j < border.segments.size(); j++) {
				BorderSegment seg = (BorderSegment) border.segments.get(j);
				Border rs = (Border) tb.rowBorders.get(seg.start);
				Border re = (Border) tb.rowBorders.get(seg.end);
				if (null == rs || null == re) {
					continue;
				}
				int sy = getScaledValue(rs.position + rs.width / 2);
				int ey = getScaledValue(re.position + re.width / 2);
				int x = getScaledValue(border.position + seg.width / 2);
				if (border.breakPoints.contains(Integer.valueOf(seg.start))) {
					sy = getScaledValue(rs.position);
				}
				if (border.breakPoints.contains(Integer.valueOf(seg.end))) {
					if (seg.end == tb.tableLRY) {
						ey = getScaledValue(re.position);
					} else {
						ey = getScaledValue(re.position + re.width);
					}
				}
				drawBorder(new BorderInfo(currentX + x, currentY + sy, currentX + x, currentY + ey,
						getScaledValue(seg.width), seg.color, seg.style, BorderInfo.LEFT_BORDER));
			}
		}
		// draw right table border
		border = (Border) tb.columnBorders.get(tb.tableLRX);
		for (int j = 0; j < border.segments.size(); j++) {
			BorderSegment seg = (BorderSegment) border.segments.get(j);
			Border rs = (Border) tb.rowBorders.get(seg.start);
			Border re = (Border) tb.rowBorders.get(seg.end);
			if (null == rs || null == re) {
				continue;
			}
			int sy = getScaledValue(rs.position + rs.width / 2);
			int ey = getScaledValue(re.position + re.width / 2);
			int x = getScaledValue(border.position - seg.width / 2);
			if (border.breakPoints.contains(Integer.valueOf(seg.start))) {
				sy = getScaledValue(rs.position);
			}
			if (border.breakPoints.contains(Integer.valueOf(seg.end))) {
				if (seg.end == tb.tableLRY) {
					ey = getScaledValue(re.position);
				} else {
					ey = getScaledValue(re.position + re.width);
				}
			}
			drawBorder(new BorderInfo(currentX + x, currentY + sy, currentX + x, currentY + ey,
					getScaledValue(seg.width), seg.color, seg.style, BorderInfo.RIGHT_BORDER));
		}

		// draw row borders
		for (Iterator<?> i = tb.rowBorders.keySet().iterator(); i.hasNext();) {
			Integer pos = (Integer) i.next();
			if (pos == tb.tableLRY) {
				continue;
			}

			border = (Border) tb.rowBorders.get(pos);
			for (int j = 0; j < border.segments.size(); j++) {
				BorderSegment seg = (BorderSegment) border.segments.get(j);
				Border cs = (Border) tb.columnBorders.get(seg.start);
				Border ce = (Border) tb.columnBorders.get(seg.end);
				if (null == cs || null == ce) {
					continue;
				}
				// we can also adjust the columns in this position
				int sx = getScaledValue(cs.position + cs.width / 2);
				int ex = getScaledValue(ce.position + ce.width / 2);
				int y = getScaledValue(border.position + seg.width / 2);
				if (border.breakPoints.contains(Integer.valueOf(seg.start))) {
					if (seg.start == tb.tableX && border.position != tb.tableY) {
						sx = getScaledValue(cs.position + cs.width);
					} else {
						sx = getScaledValue(cs.position);
					}
				}
				if (border.breakPoints.contains(Integer.valueOf(seg.end))) {
					if (seg.end == tb.tableLRX) {
						if (border.position == tb.tableY) {
							ex = getScaledValue(ce.position);
						} else {
							ex = getScaledValue(ce.position - ce.width);
						}
					} else {
						ex = getScaledValue(ce.position + ce.width);
					}
				}
				drawBorder(new BorderInfo(currentX + sx, currentY + y, currentX + ex, currentY + y,
						getScaledValue(seg.width), seg.color, seg.style, BorderInfo.TOP_BORDER));
			}
		}
		// draw bottom table border
		border = (Border) tb.rowBorders.get(tb.tableLRY);
		for (int j = 0; j < border.segments.size(); j++) {
			BorderSegment seg = (BorderSegment) border.segments.get(j);
			Border cs = (Border) tb.columnBorders.get(seg.start);
			Border ce = (Border) tb.columnBorders.get(seg.end);
			if (null == cs || null == ce) {
				continue;
			}
			// we can also adjust the columns in this position
			int sx = getScaledValue(cs.position + cs.width / 2);
			int ex = getScaledValue(ce.position + ce.width / 2);
			int y = getScaledValue(border.position - seg.width / 2);
			if (border.breakPoints.contains(Integer.valueOf(seg.start))) {
				sx = getScaledValue(cs.position);
			}
			if (border.breakPoints.contains(Integer.valueOf(seg.end))) {
				if (seg.end == tb.tableLRX) {
					ex = getScaledValue(ce.position);
				} else {
					ex = getScaledValue(ce.position + ce.width);
				}
			}
			drawBorder(new BorderInfo(currentX + sx, currentY + y, currentX + ex, currentY + y,
					getScaledValue(seg.width), seg.color, seg.style, BorderInfo.BOTTOM_BORDER));
		}
	}

	/**
	 * Draws the borders of a container.
	 *
	 * @param borders the border info
	 */
	public void drawBorder(BorderInfo[] borders) {
		if (borders == null) {
			return;
		}
		// double>solid>dashed>dotted>none
		ArrayList<BorderInfo> dbl = null;
		ArrayList<BorderInfo> solid = null;
		ArrayList<BorderInfo> dashed = null;
		ArrayList<BorderInfo> dotted = null;

		for (int i = 0; i < borders.length; i++) {
			switch (borders[i].borderStyle) {
			case AreaConstants.BORDER_STYLE_DOUBLE:
				if (null == dbl) {
					dbl = new ArrayList<BorderInfo>();
				}
				dbl.add(borders[i]);
				break;
			case AreaConstants.BORDER_STYLE_DASHED:
				if (null == dashed) {
					dashed = new ArrayList<BorderInfo>();
				}
				dashed.add(borders[i]);
				break;
			case AreaConstants.BORDER_STYLE_DOTTED:
				if (null == dotted) {
					dotted = new ArrayList<BorderInfo>();
				}
				dotted.add(borders[i]);
				break;
			default:
				if (null == solid) {
					solid = new ArrayList<BorderInfo>();
				}
				solid.add(borders[i]);
				break;
			}
		}
		if (null != dotted) {
			for (Iterator<BorderInfo> it = dotted.iterator(); it.hasNext();) {
				BorderInfo bi = it.next();
				drawBorder(bi);
			}
		}
		if (null != dashed) {
			for (Iterator<BorderInfo> it = dashed.iterator(); it.hasNext();) {
				BorderInfo bi = it.next();
				drawBorder(bi);
			}
		}
		if (null != solid) {
			for (Iterator<BorderInfo> it = solid.iterator(); it.hasNext();) {
				BorderInfo bi = it.next();
				drawBorder(bi);
			}
		}
		if (null != dbl) {
			for (Iterator<BorderInfo> it = dbl.iterator(); it.hasNext();) {
				BorderInfo bi = it.next();
				drawDoubleBorder(bi);
			}
		}
	}

	private void drawBorder(BorderInfo bi) {
		if (AreaConstants.BORDER_STYLE_DOUBLE == bi.borderStyle) {
			drawDoubleBorder(bi);
		} else {
			pageGraphic.drawLine(bi.startX, bi.startY, bi.endX, bi.endY, bi.borderWidth, bi.borderColor,
					bi.borderStyle);
		}
	}

	private void drawDoubleBorder(BorderInfo bi) {
		int borderWidth = bi.borderWidth;
		int outerBorderWidth = borderWidth / 4;
		int innerBorderWidth = borderWidth / 4;

		int startX = bi.startX;
		int startY = bi.startY;
		int endX = bi.endX;
		int endY = bi.endY;
		Color borderColor = bi.borderColor;
		switch (bi.borderType) {
		// Draws the outer border first, and then the inner border.
		case BorderInfo.TOP_BORDER:
			pageGraphic.drawLine(startX, startY - borderWidth / 2 + outerBorderWidth / 2, endX,
					endY - borderWidth / 2 + outerBorderWidth / 2, outerBorderWidth, borderColor,
					AreaConstants.BORDER_STYLE_SOLID); // $NON-NLS-1$
			pageGraphic.drawLine(startX, startY + borderWidth / 2 - innerBorderWidth / 2, endX,
					endY + borderWidth / 2 - innerBorderWidth / 2, innerBorderWidth, borderColor,
					AreaConstants.BORDER_STYLE_SOLID); // $NON-NLS-1$
			break;
		case BorderInfo.RIGHT_BORDER:
			pageGraphic.drawLine(startX + borderWidth / 2 - outerBorderWidth / 2, startY,
					endX + borderWidth / 2 - outerBorderWidth / 2, endY, outerBorderWidth, borderColor,
					AreaConstants.BORDER_STYLE_SOLID); // $NON-NLS-1$
			pageGraphic.drawLine(startX - borderWidth / 2 + innerBorderWidth / 2, startY,
					endX - borderWidth / 2 + innerBorderWidth / 2, endY, innerBorderWidth, borderColor,
					AreaConstants.BORDER_STYLE_SOLID); // $NON-NLS-1$
			break;
		case BorderInfo.BOTTOM_BORDER:
			pageGraphic.drawLine(startX, startY + borderWidth / 2 - outerBorderWidth / 2, endX,
					endY + borderWidth / 2 - outerBorderWidth / 2, outerBorderWidth, borderColor,
					AreaConstants.BORDER_STYLE_SOLID); // $NON-NLS-1$
			pageGraphic.drawLine(startX, startY - borderWidth / 2 + innerBorderWidth / 2, endX,
					endY - borderWidth / 2 + innerBorderWidth / 2, innerBorderWidth, borderColor,
					AreaConstants.BORDER_STYLE_SOLID); // $NON-NLS-1$
			break;
		case BorderInfo.LEFT_BORDER:
			pageGraphic.drawLine(startX - borderWidth / 2 + outerBorderWidth / 2, startY,
					endX - borderWidth / 2 + outerBorderWidth / 2, endY, outerBorderWidth, borderColor,
					AreaConstants.BORDER_STYLE_SOLID); // $NON-NLS-1$
			pageGraphic.drawLine(startX + borderWidth / 2 - innerBorderWidth / 2, startY,
					endX + borderWidth / 2 - innerBorderWidth / 2, endY, innerBorderWidth, borderColor,
					AreaConstants.BORDER_STYLE_SOLID); // $NON-NLS-1$
			break;
		}
	}

	protected int getX(IArea area) {
		return getScaledValue(area.getX());
	}

	protected int getY(IArea area) {
		return getScaledValue(area.getY());
	}

	protected int getWidth(IArea area) {
		return getScaledValue(area.getWidth());
	}

	protected int getHeight(IArea area) {
		return getScaledValue(area.getHeight());
	}

	protected int getScaledValue(int value) {
		return (int) (value * scale);
	}

	/**
	 * Draw table border
	 *
	 * @param table
	 */
	public void drawTableBorder(TableArea table) {
		TableBorder tb = new TableBorder(table.getX(), table.getY());
		traverseRows(tb, table, tb.tableX, tb.tableY);
		drawBorder(tb);
	}

	private void traverseRows(TableBorder tb, IContainerArea container, int offsetX, int offsetY) {
		for (Iterator<IArea> i = container.getChildren(); i.hasNext();) {
			IArea area = i.next();
			if (area instanceof IContainerArea) {
				offsetX += area.getX();
				offsetY += area.getY();
				if (area instanceof RowArea) {
					handleBorderInRow(tb, (RowArea) area, offsetX, offsetY);
				} else {
					traverseRows(tb, (IContainerArea) area, offsetX, offsetY);
				}
				offsetX -= area.getX();
				offsetY -= area.getY();
			}
		}
	}

	private void handleBorderInRow(TableBorder tb, RowArea row, int offsetX, int offsetY) {
		for (Iterator<?> ri = row.getChildren(); ri.hasNext();) {
			IArea area = (IArea) ri.next();
			if (!(area instanceof CellArea)) {
				continue;
			}
			CellArea cell = (CellArea) area;
			BorderInfo[] borders = cacheCellBorder(cell);
			int cellX = offsetX + cell.getX();
			int cellY = offsetY + cell.getY();
			// the x coordinate of the cell's right boundary
			int cellRx = cellX + cell.getWidth();
			// the y coordinate of the cell's bottom boundary
			int cellBy = cellY + cell.getHeight();
			tb.addColumn(cellX);
			tb.addColumn(cellRx);
			tb.addRow(cellBy);
			if (null != borders && borders[BorderInfo.TOP_BORDER].borderWidth != 0) {
				tb.setRowBorder(cellY, cellX, cellRx, borders[BorderInfo.TOP_BORDER].borderStyle,
						borders[BorderInfo.TOP_BORDER].borderWidth, borders[BorderInfo.TOP_BORDER].borderColor);
			}
			if (null != borders && borders[BorderInfo.LEFT_BORDER].borderWidth != 0) {
				tb.setColumnBorder(cellX, cellY, cellBy, borders[BorderInfo.LEFT_BORDER].borderStyle,
						borders[BorderInfo.LEFT_BORDER].borderWidth, borders[BorderInfo.LEFT_BORDER].borderColor);
			}
			if (null != borders && borders[BorderInfo.BOTTOM_BORDER].borderWidth != 0) {
				tb.setRowBorder(cellBy, cellX, cellRx, borders[BorderInfo.BOTTOM_BORDER].borderStyle,
						borders[BorderInfo.BOTTOM_BORDER].borderWidth, borders[BorderInfo.BOTTOM_BORDER].borderColor);
			}
			if (null != borders && borders[BorderInfo.RIGHT_BORDER].borderWidth != 0) {
				tb.setColumnBorder(cellRx, cellY, cellBy, borders[BorderInfo.RIGHT_BORDER].borderStyle,
						borders[BorderInfo.RIGHT_BORDER].borderWidth, borders[BorderInfo.RIGHT_BORDER].borderColor);
			}
		}
	}

}
