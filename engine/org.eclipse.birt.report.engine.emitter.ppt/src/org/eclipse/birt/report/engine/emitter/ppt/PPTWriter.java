/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.ppt;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.ppt.util.PPTUtil.HyperlinkDef;
import org.eclipse.birt.report.engine.layout.emitter.util.BackgroundImageLayout;
import org.eclipse.birt.report.engine.layout.emitter.util.Position;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.AreaConstants;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UCharacter.UnicodeBlock;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

/**
 * Class to create PPT file
 *
 * @since 3.3
 *
 */
public class PPTWriter {

	protected static Logger logger = Logger.getLogger(PPTRender.class.getName());

	/**
	 * Output stream where postscript to be output.
	 */
	private PrintWriter writer = null;

	protected int currentPageNum = 0;
	private int shapeCount = 0;

	protected float pageWidth, pageHeight;

	private Map<String, ImageInfo> imageInfos = new HashMap<>();

	// Holds the files' name for each page
	private Map<Integer, List<String>> fileNamesLists = new TreeMap<>();

	private QuotedPrintableCodec quotedPrintableCodec;

	/**
	 * Constructor of PPT
	 *
	 * @param output
	 */
	public PPTWriter(OutputStream output) {
		try {
			writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), false);
		} catch (UnsupportedEncodingException e) {
			assert (false);
		}
	}

	/**
	 * Creates a PPT Document.
	 *
	 * @param author
	 * @param title
	 * @param description
	 * @param subject
	 *
	 */
	public void start(String title, String author, String description, String subject) {
		if (!imageInfos.isEmpty()) {
			imageInfos.clear();
		}
		if (!fileNamesLists.isEmpty()) {
			fileNamesLists.clear();
		}

		println("MIME-Version: 1.0"); //$NON-NLS-1$
		println("Content-Type: multipart/related; boundary=\"___Actuate_Content_Boundary___\""); //$NON-NLS-1$
		println(""); //$NON-NLS-1$
		println("--___Actuate_Content_Boundary___"); //$NON-NLS-1$
		println("Content-Location: slide-show"); //$NON-NLS-1$
		println("Content-Transfer-Encoding: quoted-printable"); //$NON-NLS-1$
		println("Content-Type: text/html; charset=\"utf-8\""); //$NON-NLS-1$
		println(""); //$NON-NLS-1$
		println("<html"); //$NON-NLS-1$
		println("xmlns=3D'http://www.w3.org/TR/REC-html40'"); //$NON-NLS-1$
		println("xmlns:o=3D'urn:schemas-microsoft-com:office:office'"); //$NON-NLS-1$
		println("xmlns:p=3D'urn:schemas-microsoft-com:office:powerpoint'"); //$NON-NLS-1$
		println("xmlns:v=3D'urn:schemas-microsoft-com:vml'"); //$NON-NLS-1$
		println(">"); //$NON-NLS-1$
		println("<head>"); //$NON-NLS-1$
		println("<meta http-equiv=3D'Content-Type' content=3D'text/html; charset=3Dutf-8'>"); //$NON-NLS-1$
		println("<meta name=3D'ProgId' content=3D'PowerPoint.Slide'>"); //$NON-NLS-1$
		println("<meta name=3D'Generator' content=3D'Actuate View Server'>"); //$NON-NLS-1$
		println("<title>" + title + "</title>"); //$NON-NLS-1$
		println("<xml><o:DocumentProperties>"); //$NON-NLS-1$
		println("<o:Author>" + author + "</o:Author>"); //$NON-NLS-1$
		println("<o:Description>" + description + "</o:Description>"); //$NON-NLS-1$
		println("<o:Subject>" + subject + "</o:Subject>"); //$NON-NLS-1$
		println("</o:DocumentProperties></xml><link rel=3DFile-List href=3D'file-list'>"); //$NON-NLS-1$
		println("<link rel=3DPresentation-XML href=3D'presentation'>"); //$NON-NLS-1$
		println("</head></body></html>"); //$NON-NLS-1$
	}

	private void print(String text) {
		writer.print(text);
	}

	private void println(String text) {
		writer.println(text);
	}

	private void print(byte[] data) {
		print(new String(data));
	}

	/**
	 * Closes the document.
	 *
	 */
	public void end() {
		int slidesizex = (int) (Math.ceil(pageWidth * 8));
		int slidesizey = (int) (Math.ceil(pageHeight * 8));
		println("--___Actuate_Content_Boundary___"); //$NON-NLS-1$
		println("Content-Location: presentation"); //$NON-NLS-1$
		println("Content-Transfer-Encoding: quoted-printable"); //$NON-NLS-1$
		println("Content-Type: text/xml; charset=\"utf-8\""); //$NON-NLS-1$
		println(""); //$NON-NLS-1$
		println("<xml"); //$NON-NLS-1$
		println(" xmlns:o=3D'urn:schemas-microsoft-com:office:office'"); //$NON-NLS-1$
		println(" xmlns:p=3D'urn:schemas-microsoft-com:office:powerpoint'"); //$NON-NLS-1$
		println(">"); //$NON-NLS-1$
		println(("<p:presentation sizeof=3D'custom' slidesizex=3D'" + slidesizex + "' slidesizey=3D'" + slidesizey //$NON-NLS-1$ //$NON-NLS-2$
				+ "'>")); //$NON-NLS-1$

		for (int i = 0; i < currentPageNum; i++) {
			println(("<p:slide id=3D'" + (i + 1) + "' href=3D's" + (i + 1) + "'/>")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		println("</p:presentation></xml>"); //$NON-NLS-1$
		println(""); //$NON-NLS-1$
		println("--___Actuate_Content_Boundary___"); //$NON-NLS-1$
		println("Content-Location: file-list"); //$NON-NLS-1$
		println("Content-Transfer-Encoding: quoted-printable"); //$NON-NLS-1$
		println("Content-Type: text/xml; charset=\"utf-8\""); //$NON-NLS-1$
		println("<xml"); //$NON-NLS-1$
		println(" xmlns:o=3D'urn:schemas-microsoft-com:office:office'"); //$NON-NLS-1$
		println(" xmlns:p=3D'urn:schemas-microsoft-com:office:powerpoint'"); //$NON-NLS-1$
		println(">"); //$NON-NLS-1$
		println("<o:MainFile href=3D'slide-show'/>"); //$NON-NLS-1$
		println("<o:File href=3D'presentation'/>"); //$NON-NLS-1$
		println("<o:File href=3D'file-list'/>"); //$NON-NLS-1$

		for (int i = 0; i < currentPageNum; i++) {
			println(("<o:File href=3D's" + (i + 1) + "'/>")); //$NON-NLS-1$ //$NON-NLS-2$
			if (fileNamesLists.containsKey(i + 1)) {
				List<String> fileNames = fileNamesLists.get(i + 1);
				for (String fileName : fileNames) {
					println(("<o:File href=3D\"" + fileName + "\"/>"));
				}
			}
		}

		println("</xml>"); //$NON-NLS-1$
		println(""); //$NON-NLS-1$
		println("--___Actuate_Content_Boundary___--"); //$NON-NLS-1$
		writer.close();
		writer = null;
	}

	/**
	 * End page
	 */
	public void endPage() {
		try {
			// Write out the image bytes
			Set<Map.Entry<String, ImageInfo>> entries = imageInfos.entrySet();
			for (Map.Entry<String, ImageInfo> entry : entries) {
				ImageInfo info = entry.getValue();
				generateImageBytes(info.imageId, info.imageData);
				println("\n");
			}
			println("</p:slide></body></html>"); //$NON-NLS-1$ 3
		} catch (IOException ioe) {
			logger.log(Level.WARNING, ioe.getMessage(), ioe);
		}
	}

	private void exportImageHeader(String imagekey) throws IOException {
		println("");
		println("--___Actuate_Content_Boundary___");
		ImageInfo imageInfo = imageInfos.get(imagekey);
		println("Content-Location: " + imageInfo.imageName + "");
		println("Content-Transfer-Encoding: base64");
		println("Content-Type: image/" + imageInfo.extension + "\n");
	}

	private void generateImageBytes(String imageTitle, byte[] imageData) throws IOException {
		exportImageHeader(imageTitle);
		print(Base64.getEncoder().encode(imageData));
	}

	/**
	 * Creates a new page.
	 *
	 * @param pageWidth       page width
	 * @param pageHeight      page height
	 * @param backgroundColor page background color
	 */
	public void newPage(float pageWidth, float pageHeight, Color backgroundColor) {
		currentPageNum++;
		imageInfos.clear();
		if (pageWidth > this.pageWidth) {
			this.pageWidth = pageWidth;
		}
		if (pageHeight > this.pageHeight) {
			this.pageHeight = pageHeight;
		}

		println("--___Actuate_Content_Boundary___"); //$NON-NLS-1$
		println("Content-Location: s" + currentPageNum + ""); //$NON-NLS-1$ //$NON-NLS-2$
		println("Content-Transfer-Encoding: quoted-printable"); //$NON-NLS-1$
		println("Content-Type: text/html; charset=\"utf-8\""); //$NON-NLS-1$
		println(""); //$NON-NLS-1$
		println("<html"); //$NON-NLS-1$
		println(" xmlns=3D'http://www.w3.org/TR/REC-html40'"); //$NON-NLS-1$
		println(" xmlns:o=3D'urn:schemas-microsoft-com:office:office'"); //$NON-NLS-1$
		println(" xmlns:p=3D'urn:schemas-microsoft-com:office:powerpoint'"); //$NON-NLS-1$
		println(" xmlns:v=3D'urn:schemas-microsoft-com:vml'"); //$NON-NLS-1$
		println(">"); //$NON-NLS-1$
		println("<head/><body><p:slide>"); //$NON-NLS-1$
		println("<meta http-equiv=3D'Content-Type' content=3D'text/html; charset=3Dutf-8'>"); //$NON-NLS-1$
		drawBackgroundColor(backgroundColor, 0, 0, pageWidth, pageHeight);
	}

	private String getFontName(BaseFont baseFont) {
		String[][] familyFontNames = baseFont.getFamilyFontName();
		String[] family = familyFontNames[familyFontNames.length - 1];
		return family[family.length - 1];
	}

	/**
	 * Draws a chunk of text on the PPT.
	 *
	 * @param text      the textArea to be drawn.
	 * @param textX     the X position of the textArea relative to current page.
	 * @param textY     the Y position of the textArea relative to current page.
	 * @param width     the Width of the textArea
	 * @param height    the height of the textArea
	 * @param textStyle the style of the textArea
	 * @param link      the hyperlink of the textArea
	 */
	public void drawText(String text, float textX, float textY, float width, float height, TextStyle textStyle,
			HyperlinkDef link) {
		FontInfo fontInfo = textStyle.getFontInfo();
		Color color = textStyle.getColor();
		boolean rtl = textStyle.isRtl();

		if (fontInfo == null) {
			return;
		}

		float descend = fontInfo.getBaseFont().getFontDescriptor(BaseFont.DESCENT, fontInfo.getFontSize());
		height = height + descend * 0.6f;

		BaseFont baseFont = fontInfo.getBaseFont();
		String fontName = getFontName(baseFont);

		println("<v:shape id=3D\"_x0000_s" + (++shapeCount) + "\" type=3D\"#_x0000_t202\""); //$NON-NLS-1$ //$NON-NLS-2$
		println(" style=3D'position:absolute;left:" + textX + "pt;top:" + textY + "pt;width:" + width + "pt;height:" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ height + "pt;v-text-anchor:bottom-baseline;mso-wrap-style:none;'"); //$NON-NLS-1$
		println(" filled=3D'f' stroked=3D'f'>"); //$NON-NLS-1$
		println("<v:textbox style=3D'mso-fit-shape-to-text:f;' inset=3D'0.00pt 0.00pt 0.00pt 0.00pt'/>"); //$NON-NLS-1$
		println("</v:shape>"); //$NON-NLS-1$
		println("<div v:shape=3D\"_x0000_s" + shapeCount + "\">"); //$NON-NLS-1$ //$NON-NLS-2$

		println("<div style=3D'mso-text-indent-alt:" //$NON-NLS-1$
				+ 0 + ";text-align:left;'>" //$NON-NLS-1$
				+ "<span style=3D'font-family:" //$NON-NLS-1$
				+ fontName + ";font-size:" //$NON-NLS-1$
				+ fontInfo.getFontSize() + "pt;color:#" //$NON-NLS-1$
				+ getColorString(color) + ";'" + buildI18nAttributes(text, rtl) + ">"); //$NON-NLS-2$

		boolean isItalic = fontInfo != null && (fontInfo.getFontStyle() & Font.ITALIC) != 0;
		boolean isBold = fontInfo != null && (fontInfo.getFontStyle() & Font.BOLD) != 0;

		boolean isUnderline = textStyle.isUnderline();

		if (isItalic) {
			print("<i>");
		}
		if (isBold) {
			print("<b>");
		}
		if (isUnderline) {
			print("<u>");
		}
		if (link != null) {
			String hyperlink = link.getLink();
			String tooltip = link.getTooltip();
			if (hyperlink != null) {
				hyperlink = codeLink(hyperlink);
				print("<p:onmouseclick  hyperlinktype=3D\"url\" href=3D\"" + hyperlink + "\"");
				if (tooltip != null) {
					tooltip = codeLink(tooltip);
					print(" tips=3D\"" + tooltip + "\"");
				}
				println("/><a href=3D\"" + hyperlink
						+ "/\" target=3D\"_parent\" onclick=3D\"window.event.cancelBubble=3Dtrue;\">");
			}
		}
		print(getEscapedStr(text));
		if (link != null) {
			print("</a>");
		}
		if (isUnderline) {
			print("</u>");
		}
		if (isBold) {
			print("</b>");
		}
		if (isItalic) {
			print("</i>");
		}
		println("</span></div>"); //$NON-NLS-1$
		println("</div>"); //$NON-NLS-1$
	}

	private String codeLink(String link) {
		try {
			if (quotedPrintableCodec == null) {
				quotedPrintableCodec = new QuotedPrintableCodec();
			}
			link = quotedPrintableCodec.encode(link);
		} catch (EncoderException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return link;
	}

	private String getColorString(Color color) {
		StringBuffer buffer = new StringBuffer();
		appendComponent(buffer, color.getRed());
		appendComponent(buffer, color.getGreen());
		appendComponent(buffer, color.getBlue());
		return buffer.toString();
	}

	private void appendComponent(StringBuffer buffer, int component) {
		String hex = Integer.toHexString(component);
		if (hex.length() == 1) {
			buffer.append('0');
		}
		buffer.append(hex);
	}

	/**
	 * Draw the imgae
	 *
	 * @param imageId   image ID
	 * @param imageData image data
	 * @param extension file extension
	 * @param imageX    image start point X
	 * @param imageY    image start point Y
	 * @param height    image height
	 * @param width     image widt
	 * @param helpText  help text
	 * @param link      link at image object
	 * @throws Exception
	 */
	public void drawImage(String imageId, byte[] imageData, String extension, float imageX, float imageY, float height,
			float width, String helpText, HyperlinkDef link) throws Exception {
		ImageInfo imageInfo = getImageInfo(imageId, imageData, extension);
		exportImageDefn(imageInfo.imageName, imageInfo.imageId, width, height, imageX, imageY, link);
	}

	private ImageInfo getImageInfo(String imageId, byte[] imageData, String extension) {
		ImageInfo imageInfo = null;
		String imageTitle = getImageTitle();
		if (imageId != null) {
			imageId = imageId + "_" + currentPageNum;
		} else {
			imageId = imageTitle;
		}
		if (imageInfos.containsKey(imageId)) {
			imageInfo = imageInfos.get(imageId);
		} else {
			String imageName = imageTitle + "." + extension;
			imageInfo = new ImageInfo(imageId, imageName, extension, imageData);
			imageInfos.put(imageId, imageInfo);
			recordFileLists(imageName);
		}
		return imageInfo;
	}

	private static class ImageInfo {
		public String imageName;
		public String extension;
		public byte[] imageData;
		public String imageId;

		public ImageInfo(String imageId, String imageName, String extension, byte[] imageData) {
			this.imageId = imageId;
			this.imageName = imageName;
			this.extension = extension;
			this.imageData = imageData;
		}
	}

	/**
	 * @param imageName
	 * @param imageTitle
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 */
	private void exportImageDefn(String imageName, String imageTitle, double width, double height, double x, double y,
			HyperlinkDef link) {
		println("<v:shape id=3D'" + (shapeCount) + "' type=3D'#_x0000_t75'"); //$NON-NLS-1$ //$NON-NLS-2$
		if (link != null) {
			String hyperlink = link.getLink();
			String tooltip = link.getTooltip();
			if (tooltip != null) {
				tooltip = codeLink(tooltip);
				print("title=3D\"" + tooltip + "\" ");
			}
			if (hyperlink != null) {
				hyperlink = codeLink(hyperlink);
				print("href=3D\"" + hyperlink + "\" target=3D\"_parent\"");
			}
		}
		Crop crop = checkCrop(x, y, width, height);
		if (crop == null) {
			println(" style=3D'position:absolute;left:" + x + "pt;top:" + y + "pt;width:" + width + "pt;height:" //$NON-NLS-3$ //$NON-NLS-4$
					+ height + "pt'"); //$NON-NLS-1$
		} else {
			ClipArea clip = clipStack.peek();
			double pX = Math.max(clip.x, x);
			double pY = Math.max(clip.y, y);
			double pWidth = Math.min(x + width, clip.x + clip.width) - pX;
			double pHeight = Math.min(y + height, clip.y + clip.height) - pY;
			println(" style=3D'position:absolute;left:" + pX + "pt;top:" + pY + "pt;width:" + pWidth + "pt;height:" //$NON-NLS-3$ //$NON-NLS-4$
					+ pHeight + "pt'"); //$NON-NLS-1$
		}
		println(" filled=3D'f' stroked=3D'f'>"); //$NON-NLS-1$
		print("<v:imagedata src=3D\"" + imageName + "\" o:title=3D\"" + imageTitle + "\"");
		if (crop != null) {
			if (crop.top != 0) {
				print(" croptop=3D\"" + crop.top + "f\"");
			}
			if (crop.left != 0d) {
				print(" cropleft=3D\"" + crop.left + "f\"");
			}
			if (crop.right != 0d) {
				print(" cropright=3D\"" + crop.right + "f\"");
			}
			if (crop.bottom != 0d) {
				print(" cropbottom=3D\"" + crop.bottom + "f\"");
			}
		}
		println("/>");
		println("<o:lock v:ext=3D'" + "edit" + "' aspectratio=3D't" + "'/>");
		println("</v:shape>"); //$NON-NLS-1$
	}

	private Crop checkCrop(double x, double y, double width, double height) {
		if (clipStack.isEmpty()) {
			return null;
		}
		ClipArea clip = clipStack.peek();
		int left = 0, right = 0, top = 0, bottom = 0;
		if (x < clip.x) {
			left = (int) ((clip.x - x) * 100);
		}
		if (y < clip.y) {
			top = (int) ((clip.y - y) * 100);
		}
		if (x + width > clip.x + clip.width) {
			right = (int) (((x + width) - (clip.x + clip.width)) * 100);
		}
		if (y + height > clip.y + clip.height) {
			bottom = (int) (((y + height) - (clip.y + clip.height)) * 100);
		}
		if (left != 0 || right != 0 || top != 0 || bottom != 0) {
			return new Crop(left, right, top, bottom);
		}
		return null;
	}

	private class Crop {

		int left, right, top, bottom;

		Crop(int left, int right, int top, int bottom) {
			this.left = left;
			this.right = right;
			this.top = top;
			this.bottom = bottom;
		}
	}

	private String getImageExtension(String imageURI) {
		String rectifiedImageURI = imageURI.replace('.', '&');
		String extension = imageURI.substring(rectifiedImageURI.lastIndexOf('&') + 1).toLowerCase();

		if (extension.equals("svg")) {
			extension = "jpg";
		}
		return extension;
	}

	/*
	 * Save the image name into file list of current page
	 */
	private void recordFileLists(String filename) {

		if (fileNamesLists.containsKey(currentPageNum)) {
			fileNamesLists.get(currentPageNum).add(filename);
		} else {
			List<String> fileNames = new ArrayList<>();
			fileNames.add(filename);
			fileNamesLists.put(currentPageNum, fileNames);
		}
	}

	/**
	 * Draws a line from the start position to the end position with the given line
	 * width, color, and style on the PPT.
	 *
	 * @param startX    the start X coordinate of the line
	 * @param startY    the start Y coordinate of the line
	 * @param endX      the end X coordinate of the line
	 * @param endY      the end Y coordinate of the line
	 * @param width     the lineWidth
	 * @param color     the color of the line
	 * @param lineStyle the given line style
	 */
	public void drawLine(double startX, double startY, double endX, double endY, double width, Color color,
			int lineStyle) {
		// if the border does NOT have color or the line width of the border
		// is zero
		// or the lineStyle is "none", just return.
		if (null == color || 0f == width || lineStyle == AreaConstants.BORDER_STYLE_NONE) // $NON-NLS-1$
		{
			return;
		}
		if (lineStyle == AreaConstants.BORDER_STYLE_SOLID || lineStyle == AreaConstants.BORDER_STYLE_DASHED
				|| lineStyle == AreaConstants.BORDER_STYLE_DOTTED || lineStyle == AreaConstants.BORDER_STYLE_DOUBLE) {
			drawRawLine(startX, startY, endX, endY, width, color, lineStyle);
		} else {
			// the other line styles, e.g. 'ridge', 'outset', 'groove', 'insert'
			// is NOT supported now.
			// We look it as the default line style -- 'solid'
			drawRawLine(startX, startY, endX, endY, width, color, AreaConstants.BORDER_STYLE_SOLID);
		}
	}

	/**
	 * Draws a line with the line-style specified in advance from the start position
	 * to the end position with the given line width, color, and style on the PPT.
	 * If the line-style is NOT set before invoking this method, "solid" will be
	 * used as the default line-style.
	 *
	 * @param startX the start X coordinate of the line
	 * @param startY the start Y coordinate of the line
	 * @param endX   the end X coordinate of the line
	 * @param endY   the end Y coordinate of the line
	 * @param width  the lineWidth
	 * @param color  the color of the line
	 */
	private void drawRawLine(double startX, double startY, double endX, double endY, double width, Color color,
			int lineStyle) {

		print("<v:line id=3D\"" + (++shapeCount) + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		print(" style=3D'position:absolute");

		print("' from=3D\"" + startX + "pt," + startY + "pt\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
																	// //$NON-NLS-4$ //$NON-NLS-5$
		print(" to=3D\"" + endX + "pt," + endY + "pt\"");

		print(" strokecolor=3D\"#" + getColorString(color) + "\""); //$NON-NLS-1$
		print(" strokeweight=3D\"" + width + "pt\""); //$NON-NLS-1$
		if (lineStyle == AreaConstants.BORDER_STYLE_DASHED) {
			println(">");
			println("<v:stroke dashstyle=3D\"dash\"/>");
		} else if (lineStyle == AreaConstants.BORDER_STYLE_DOTTED) {
			println(">");
			println("<v:stroke dashstyle=3D\"1 1\"/>");
		} else if (lineStyle == AreaConstants.BORDER_STYLE_DOUBLE) {
			println(">");
			println("<v:stroke linestyle=3D\"thinThin\"/>");
		} else {
			println("/>");
			return;
		}
		println("</v:line>");
	}

	/**
	 * Draws the background color of the PPT.
	 *
	 * @param color  the color to be drawn
	 * @param x      the start X coordinate
	 * @param y      the start Y coordinate
	 * @param width  the width of the background dimension
	 * @param height the height of the background dimension
	 */
	public void drawBackgroundColor(Color color, double x, double y, double width, double height) {
		if (color == null) {
			return;
		}
		print("<v:rect id=3D\"" + (++shapeCount) + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		print(" style=3D'position:absolute;left:" + x + "pt;top:" + y + "pt;width:" + width + "pt;height:" + height
				+ "pt'");
		print(" fillcolor=3D\"#" + getColorString(color) + "\""); //$NON-NLS-1$
		println(" stroked=3D\"f" + "\"/>"); //$NON-NLS-1$
	}

	/**
	 * Draws the background image at the contentByteUnder of the PPT with the given
	 * offset
	 *
	 * @param imageURI  the URI referring the image
	 * @param imageData
	 * @param x         the start X coordinate at the PPT where the image is
	 *                  positioned
	 * @param y         the start Y coordinate at the PPT where the image is
	 *                  positioned
	 * @param width     the width of the background dimension
	 * @param height    the height of the background dimension
	 * @param iWidth
	 * @param iHeight
	 * @param positionX the offset X percentage relating to start X
	 * @param positionY the offset Y percentage relating to start Y
	 * @param repeat    the background-repeat property
	 */
	public void drawBackgroundImage(String imageURI, byte[] imageData, float x, float y, float width, float height,
			float iWidth, float iHeight, float positionX, float positionY, int repeat) {
		if (imageData == null || imageData.length == 0) {
			return;
		}
		float imageWidth = iWidth;
		float imageHeight = iHeight;
		String extension = getImageExtension(imageURI);
		try {
			org.eclipse.birt.report.engine.layout.emitter.Image image = EmitterUtil.parseImage(imageData, null, null);
			imageData = image.getData();
			if (imageWidth == 0 || imageHeight == 0) {
				imageWidth = image.getWidth();
				imageHeight = image.getHeight();
			}

			ImageInfo imageInfo = getImageInfo(imageURI, imageData, extension);

			Position areaPosition = new Position(x, y);
			Position areaSize = new Position(width, height);
			Position imagePosition = new Position(x + positionX, y + positionY);
			Position imageSize = new Position(imageWidth, imageHeight);
			BackgroundImageLayout layout = new BackgroundImageLayout(areaPosition, areaSize, imagePosition, imageSize);
			Collection<?> positions = layout.getImagePositions(repeat);
			Iterator<?> iterator = positions.iterator();
			while (iterator.hasNext()) {
				Position position = (Position) iterator.next();
				exportImageDefn(imageInfo.imageName, imageInfo.imageId, imageWidth, imageHeight, position.getX(),
						position.getY(), null);
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getLocalizedMessage());
		}
	}

	private String getImageTitle() {
		return "slide" + currentPageNum + "_image" + (++shapeCount);
	}

	protected String getEscapedStr(String s) {
		StringBuilder result = null;
		int spacePos = 1;
		char[] s2char = s.toCharArray();

		for (int i = 0, max = s2char.length, delta = 0; i < max; i++) {
			char c = s2char[i];
			String replacement = null;
			if (c == ' ') {
				if (spacePos % 2 != 0 || i == max - 1) {
					replacement = "&#160;"; //$NON-NLS-1$
				}
				spacePos++;
			} else {
				spacePos = 0;
			}

			// Filters the char not defined.
			if (!(c == 0x9 || c == 0xA || c == 0xD || (c >= 0x20 && c <= 0xD7FF) || (c >= 0xE000 && c <= 0xFFFD))) {
				// Ignores the illegal character.
				replacement = ""; //$NON-NLS-1$
			} else if (c == '&') {
				replacement = "&amp;"; //$NON-NLS-1$
			} else if (c == '<') {
				replacement = "&lt;"; //$NON-NLS-1$
			} else if (c == '>') {
				replacement = "&gt;"; //$NON-NLS-1$
			} else if (c == '\t') {
				replacement = " "; //$NON-NLS-1$
			} else if (c == '=') {
				replacement = "=3D";
			} else if (c >= 0x80) {
				replacement = "&#x" + Integer.toHexString(c) + ';'; //$NON-NLS-1$
			}
			if (replacement != null) {
				if (result == null) {
					result = new StringBuilder(s);
				}
				result.replace(i + delta, i + delta + 1, replacement);
				delta += (replacement.length() - 1);
			}
		}
		if (result == null) {
			return s;
		}
		return result.toString();
	}

	/**
	 * Builds i18n attributes.
	 *
	 * @param rtl Whether the string being processed is right-to-left or not.
	 *
	 * @author bidi_hcg
	 */
	private String buildI18nAttributes(String text, boolean rtl) {
		if (text == null) {
			return ""; //$NON-NLS-1$
		}

		if (rtl) {
			for (int i = text.length(); i-- > 0;) {
				UnicodeBlock block = UCharacter.UnicodeBlock.of(text.charAt(i));
				// If there is a Hebrew or Arabic content, write the
				// corresponding language attribute
				if (UCharacter.UnicodeBlock.HEBREW.equals(block)) {
					return " dir=3D'rtl' lang=3D'HE-IL'"; //$NON-NLS-1$
				}
				if (UCharacter.UnicodeBlock.ARABIC.equals(block)
						|| UCharacter.UnicodeBlock.ARABIC_PRESENTATION_FORMS_A.equals(block)
						|| UCharacter.UnicodeBlock.ARABIC_PRESENTATION_FORMS_B.equals(block)
						|| UCharacter.UnicodeBlock.ARABIC_SUPPLEMENT.equals(block)) {
					return " dir=3D'rtl' lang=3D'AR-DZ'"; //$NON-NLS-1$
				}
			}
			// If no actual RTL content was found (e.g. in case the text
			// consists of sheer neutral characters), indicate Arabic language
			return " dir=3D'rtl' lang=3D'AR-DZ'"; //$NON-NLS-1$
		}
		// XXX Other language attributes can be addressed as needed
		return " dir=3D'ltr' lang=3D'EN-US'"; //$NON-NLS-1$
	}

	private Stack<ClipArea> clipStack = new Stack<>();

	private class ClipArea {

		float x, y, width, height;

		ClipArea(float x, float y, float width, float height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}

	/**
	 * Clip the stacked area
	 *
	 * @param startX start point
	 * @param startY end point
	 * @param width  width of area
	 * @param height height of area
	 */
	public void clip(float startX, float startY, float width, float height) {
		if (clipStack.isEmpty()) {
			clipStack.push(new ClipArea(startX, startY, width, height));
		} else {
			ClipArea parent = clipStack.peek();
			float newX = Math.max(parent.x, startX);
			float newY = Math.max(parent.y, startY);
			float newWidth = Math.min(startX + width, parent.x + parent.width) - newX;
			float newHeight = Math.min(startY + height, parent.y + parent.height) - newY;
			clipStack.push(new ClipArea(newX, newY, newWidth, newHeight));
		}
	}

	/**
	 * End the clip
	 */
	public void clipEnd() {
		clipStack.pop();

	}
}
