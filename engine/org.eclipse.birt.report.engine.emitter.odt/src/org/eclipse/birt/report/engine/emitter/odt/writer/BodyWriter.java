/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.odt.writer;

import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.emitter.odt.IOdtWriter;
import org.eclipse.birt.report.engine.emitter.odt.OdtEmitter.InlineFlag;
import org.eclipse.birt.report.engine.emitter.odt.OdtEmitter.TextFlag;
import org.eclipse.birt.report.engine.emitter.odt.TocInfo;
import org.eclipse.birt.report.engine.odf.DiagonalLineInfo;
import org.eclipse.birt.report.engine.odf.OdfUtil;
import org.eclipse.birt.report.engine.odf.style.HyperlinkInfo;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.eclipse.birt.report.engine.odf.writer.AbstractOdfWriter;

@SuppressWarnings("nls")
public class BodyWriter extends AbstractOdfWriter implements IOdtWriter {
	public static final Logger logger = Logger.getLogger(BodyWriter.class.getName());

	protected final String RIGHT = "right";
	protected final String LEFT = "left";
	protected final String TOP = "top";
	protected final String BOTTOM = "bottom";
	protected boolean rtl = false;

	public BodyWriter(OutputStream out) throws Exception {
		this(out, "UTF-8");
	}

	public BodyWriter(OutputStream out, String encoding) throws Exception {
		writer = new XMLWriter();
		// no indent or newlines, because newlines inside paragraphs are
		// considered as white spaces
		writer.setIndent(false);
		writer.open(out, encoding);
	}

	@Override
	public void start(boolean rtl) {
		this.rtl = rtl;
		writer.openTag("office:body");
		writer.openTag("office:text");
	}

	/**
	 *
	 * @param height image height, unit = pt
	 * @param width  image width, unit = pt
	 */
	@Override
	public void drawImage(String imageUrl, double height, double width, HyperlinkInfo hyper, StyleEntry style,
			StyleEntry pStyle, InlineFlag inlineFlag, String altText, String bookmark, TocInfo tocInfo) {
		if (inlineFlag == InlineFlag.BLOCK || inlineFlag == InlineFlag.FIRST_INLINE) {
			writer.openTag("text:p");
			if (pStyle != null) {
				writer.attribute("text:style-name", pStyle.getName());
			}
		}

		writeBookmark(bookmark);
		writeTOC(tocInfo);

		int imageId = getImageID();
		openHyperlink(hyper, "draw");
		drawImage(imageUrl, null, null, null, height, width, style, altText, null, imageId);
		closeHyperlink(hyper, "draw");

		if (inlineFlag == InlineFlag.BLOCK) {
			writer.closeTag("text:p");
		}
	}

	@Override
	public void writeContent(int type, String txt, StyleEntry style, StyleEntry inlineStyle, String fontFamily,
			HyperlinkInfo info, InlineFlag inlineFlag, TextFlag flag, int paragraphWidth, boolean runIsRtl,
			List<String> bookmark, List<TocInfo> tocs) {
		if (inlineFlag == InlineFlag.BLOCK) {
			writeText(type, txt, style, fontFamily, info, flag, paragraphWidth, runIsRtl, bookmark, tocs);
		} else {
			boolean isInline = true;
			if (inlineFlag == InlineFlag.FIRST_INLINE && flag == TextFlag.START) {
				startParagraph(style, isInline, paragraphWidth, bookmark, tocs);
			}
			if (inlineStyle != null) {
				writeTextInRun(type, txt, inlineStyle, fontFamily, info, isInline, paragraphWidth, runIsRtl);
			} else {
				writeTextInRun(type, txt, style, fontFamily, info, isInline, paragraphWidth, runIsRtl);
			}
		}
	}

	@Override
	public void startPage() {
		// writer.openTag( "text:section");
	}

	@Override
	public void endPage() {
		// writer.closeTag( "text:section");
	}

	@Override
	public void end() {
		writer.closeTag("office:text");
		writer.closeTag("office:body");
		try {
			close();
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getLocalizedMessage());
		}
	}

	@Override
	public void writeTOC(TocInfo tocInfo) {
		if (tocInfo == null || "".equals(tocInfo.tocValue)) {
			return;
		}

		writer.openTag("text:bookmark");
		writer.attribute("text:name", "_Toc" + tocInfo.tocValue);
		writer.closeTag("text:bookmark");
		bookmarkId++;

		writer.openTag("text:toc-mark");
		writer.attribute("text:string-value", tocInfo.tocValue);
		writer.attribute("text:outline-level", tocInfo.tocLevel);
		writer.closeTag("text:toc-mark");
	}

	@Override
	public void writeForeign(IForeignContent foreignContent) {
	}

	private boolean needNewParagraph(String txt) {
		return ("\n".equals(txt) || "\r".equalsIgnoreCase(txt) || "\r\n".equals(txt));
	}

	public void startParagraph(StyleEntry style, boolean isInline, int paragraphWidth, List<String> bookmarks,
			List<TocInfo> tocs) {
		writer.openTag("text:p");
		if (style != null && style.getType() == StyleConstant.TYPE_PARAGRAPH) {
			writer.attribute("text:style-name", style.getName());
		}

		writeTocs(tocs);
		writeBookmarks(bookmarks);
	}

	/**
	 * Write bookmark markers, then clear the list.
	 *
	 * @param bookmarks
	 */
	private void writeBookmarks(List<String> bookmarks) {
		if (bookmarks != null) {
			for (String bookmark : bookmarks) {
				writeBookmark(bookmark);
			}
			bookmarks.clear();
		}
	}

	@Override
	public void endParagraph() {
		writer.closeTag("text:p");
	}

	@Override
	public void writeCaption(String txt, StyleEntry style) {
		writer.openTag("text:p");
		if (style != null) {
			writer.attribute("text:style-name", style.getName());
		}
		writeString(txt);
		writer.closeTag("text:p");
	}

	private void writeTextInParagraph(int type, String txt, StyleEntry style, String fontFamily, HyperlinkInfo info,
			int paragraphWidth, boolean runIsRtl, List<String> bookmarks, List<TocInfo> tocs) {
		writer.openTag("text:p");
		if (style != null) {
			writer.attribute("text:style-name", style.getName());
		}

		writeBookmarks(bookmarks);
		writeTocs(tocs);

		writeTextInRun(type, txt, style, fontFamily, info, false, paragraphWidth, runIsRtl);
	}

	public void writeText(int type, String txt, StyleEntry style, String fontFamily, HyperlinkInfo info, TextFlag flag,
			int paragraphWidth, boolean runIsRtl, List<String> bookmark, List<TocInfo> tocs) {
		if (flag == TextFlag.START) {
			writeTextInParagraph(type, txt, style, fontFamily, info, paragraphWidth, runIsRtl, bookmark, tocs);
		} else if (flag == TextFlag.END) {
			writer.closeTag("text:p");
		} else if (flag == TextFlag.MIDDLE) {
			writeTextInRun(type, txt, style, fontFamily, info, false, paragraphWidth, runIsRtl);
		} else {
			writeTextInParagraph(type, txt, style, fontFamily, info, paragraphWidth, runIsRtl, bookmark, tocs);
			writer.closeTag("text:p");
		}
	}

	public void writeTextInRun(int type, String txt, StyleEntry style, String fontFamily, HyperlinkInfo info,
			boolean isInline, int paragraphWidth, boolean runIsRtl) {
		if ("".equals(txt)) {
			return;
		}
		if (needNewParagraph(txt)) {
			writer.closeTag("text:p");
			startParagraph(style, isInline, paragraphWidth, null, null);
			return;
		}

		openHyperlink(info);
		boolean isField = OdfUtil.isField(type);
		// String direction = style.getDirection( );

		writeSpan(txt, style, info, type, isField);

		closeHyperlink(info);
	}

	@Override
	public void drawDiagonalLine(DiagonalLineInfo diagonalLineInfo) {
		if (diagonalLineInfo.getDiagonalNumber() <= 0 && diagonalLineInfo.getAntiDiagonalNumber() <= 0) {
			return;
		}
		writer.openTag("text:p");
		writer.attribute("text:style-name", StyleConstant.HIDDEN_STYLE_NAME);
		// TODO: diagonal line
		/*
		 * writer.openTag( "w:r" ); writer.openTag( "w:pict" ); double diagonalLineWidth
		 * = diagonalLineInfo.getDiagonalLineWidth( ); String diagonalLineStyle =
		 * diagonalLineInfo.getDiagonalStyle( ); double antidiagonalLineWidth =
		 * diagonalLineInfo .getAntiDiagonalLineWidth( ); String antidiagonalLineStyle =
		 * diagonalLineInfo.getAntiDiagonalStyle( ); String lineColor =
		 * diagonalLineInfo.getColor( ); for ( Line line :
		 * diagonalLineInfo.getDiagonalLine( ) ) { drawLine( diagonalLineWidth,
		 * diagonalLineStyle, lineColor, line ); } for ( Line antiLine :
		 * diagonalLineInfo.getAntidiagonalLine( ) ) { drawLine( antidiagonalLineWidth,
		 * antidiagonalLineStyle, lineColor, antiLine ); } writer.closeTag( "w:pict" );
		 * writer.closeTag( "w:r" );
		 */
		writer.closeTag("text:p");
	}

	/**
	 * Writes a paragraph dedicated to bookmarks and toc markers.
	 */
	@Override
	public void writeMarkersParagraph(List<String> bookmarks, List<TocInfo> tableTocs) {
		if (!bookmarks.isEmpty() || !tableTocs.isEmpty()) {
			writer.openTag("text:p");
			writeBookmarks(bookmarks);
			writeTocs(tableTocs);
			tableTocs.clear();

			writer.closeTag("text:p");
			bookmarks.clear();
		}
	}

	@Override
	public void writeTableToc(List<TocInfo> tableTocs) {
		if (!tableTocs.isEmpty()) {
			writeTocs(tableTocs);
			tableTocs.clear();
		}
	}

	/**
	 * Writes the table tocs then clear it.
	 *
	 * @param tableTocs
	 */
	private void writeTocs(List<TocInfo> tableTocs) {
		if (tableTocs != null) {
			for (TocInfo toc : tableTocs) {
				writeTOC(toc);
			}
			tableTocs.clear();
		}
	}

	@Override
	protected void closeHyperlink(HyperlinkInfo info, String baseType) {
		if ((info == null) || (info.getType() == HyperlinkInfo.DRILL)) {
			return;
		}
		writer.closeTag(baseType + ":a");
	}
}
