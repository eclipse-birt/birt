/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.postscript;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.eclipse.birt.report.engine.api.IPostscriptRenderOption;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.postscript.truetypefont.ITrueTypeWriter;
import org.eclipse.birt.report.engine.emitter.postscript.truetypefont.TrueTypeFont;
import org.eclipse.birt.report.engine.emitter.postscript.truetypefont.Util;
import org.eclipse.birt.report.engine.emitter.postscript.util.FileUtil;
import org.eclipse.birt.report.engine.layout.emitter.util.BackgroundImageLayout;
import org.eclipse.birt.report.engine.layout.emitter.util.Position;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo;
import org.w3c.dom.css.CSSValue;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.FontFactoryImp;
import com.lowagie.text.pdf.BaseFont;

public class PostscriptWriter {

	/** This is a possible value of a base 14 type 1 font */
	public static final String COURIER = BaseFont.COURIER;

	/** This is a possible value of a base 14 type 1 font */
	public static final String COURIER_BOLD = BaseFont.COURIER_BOLD;

	/** This is a possible value of a base 14 type 1 font */
	public static final String COURIER_OBLIQUE = BaseFont.COURIER_OBLIQUE;

	/** This is a possible value of a base 14 type 1 font */
	public static final String COURIER_BOLDOBLIQUE = BaseFont.COURIER_BOLDOBLIQUE;

	/** This is a possible value of a base 14 type 1 font */
	public static final String HELVETICA = BaseFont.HELVETICA;

	/** This is a possible value of a base 14 type 1 font */
	public static final String HELVETICA_BOLD = BaseFont.HELVETICA_BOLD;

	/** This is a possible value of a base 14 type 1 font */
	public static final String HELVETICA_OBLIQUE = BaseFont.HELVETICA_OBLIQUE;

	/** This is a possible value of a base 14 type 1 font */
	public static final String HELVETICA_BOLDOBLIQUE = BaseFont.HELVETICA_BOLDOBLIQUE;

	/** This is a possible value of a base 14 type 1 font */
	public static final String SYMBOL = BaseFont.SYMBOL;

	/** This is a possible value of a base 14 type 1 font */
	public static final String TIMES = "Times";

	/** This is a possible value of a base 14 type 1 font */
	public static final String TIMES_ROMAN = BaseFont.TIMES_ROMAN;

	/** This is a possible value of a base 14 type 1 font */
	public static final String TIMES_BOLD = BaseFont.TIMES_BOLD;

	/** This is a possible value of a base 14 type 1 font */
	public static final String TIMES_ITALIC = BaseFont.TIMES_ITALIC;

	/** This is a possible value of a base 14 type 1 font */
	public static final String TIMES_BOLDITALIC = BaseFont.TIMES_BOLDITALIC;

	/** This is a possible value of a base 14 type 1 font */
	public static final String ZAPFDINGBATS = BaseFont.ZAPFDINGBATS;

	public static final int TRAYCODE_AUTO = -1;
	public static final int TRAYCODE_MANUAL = 41;
	/**
	 * Default page height.
	 */
	final protected static int DEFAULT_PAGE_HEIGHT = 792;
	/**
	 * Default page width.
	 */
	final protected static int DEFAULT_PAGE_WIDTH = 612;
	/**
	 * Table mapping decimal numbers to hexadecimal numbers.
	 */
	final protected static char hd[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };
	/**
	 * Output stream where postscript to be output.
	 */
	protected PrintStream out = System.out;
	/**
	 * The current color
	 */
	protected Color clr = Color.white;
	/**
	 * The current font
	 */
	protected Font font = new Font(Font.HELVETICA, 12, Font.NORMAL);

	/**
	 * log
	 */
	protected static Logger log = Logger.getLogger(PostscriptWriter.class.getName());

	/**
	 * Current page index with 1 as default value.
	 */
	private int pageIndex = 1;

	/**
	 * Height of current page.
	 */
	private float pageHeight = DEFAULT_PAGE_HEIGHT;

	private float pageWidth = 0f;

	private static Set<String> intrinsicFonts = new HashSet<>();

	private int imageIndex = 0;

	private Map<String, String> cachedImageSource;

	private Stack<Graphic> graphics = new Stack<>();

	private final static String[] stringCommands = { "drawSStr", "drawStr", "drawSBStr", "drawBStr", "drawSIStr",
			"drawIStr", "drawSBIStr", "drawBIStr" };

	private boolean fitToPaper, isDuplex;

	private int paperWidth, paperHeight;

	static {
		intrinsicFonts.add(COURIER);
		intrinsicFonts.add(COURIER_BOLD);
		intrinsicFonts.add(COURIER_OBLIQUE);
		intrinsicFonts.add(COURIER_BOLDOBLIQUE);
		intrinsicFonts.add(HELVETICA);
		intrinsicFonts.add(HELVETICA_BOLD);
		intrinsicFonts.add(HELVETICA_OBLIQUE);
		intrinsicFonts.add(HELVETICA_BOLDOBLIQUE);
		intrinsicFonts.add(SYMBOL);
		intrinsicFonts.add(TIMES);
		intrinsicFonts.add(TIMES_ROMAN);
		intrinsicFonts.add(TIMES_BOLD);
		intrinsicFonts.add(TIMES_ITALIC);
		intrinsicFonts.add(TIMES_BOLDITALIC);
		intrinsicFonts.add(ZAPFDINGBATS);
	}

	public static boolean isIntrinsicFont(String fontName) {
		return intrinsicFonts.contains(fontName);
	}

	/**
	 * Constructor.
	 *
	 * @param out   Output stream for PostScript output.
	 * @param title title of the postscript document.
	 */
	public PostscriptWriter(OutputStream o, String title) {
		this.out = new PrintStream(o);
		this.cachedImageSource = new HashMap<>();
		emitProlog(title);
	}

	public void clipRect(float x, float y, float width, float height) {
		y = transformY(y);
		out.println(x + " " + y + " " + width + " " + height + " rcl");
	}

	public void clipSave() {
		gSave();
	}

	public void clipRestore() {
		gRestore();
	}

	/**
	 * Draws a image.
	 *
	 * @param imageStream the source input stream of the image.
	 * @param x           the x position.
	 * @param y           the y position.
	 * @param width       the image width.
	 * @param height      the image height.
	 * @throws IOException
	 */
	public void drawImage(String imageId, InputStream imageStream, float x, float y, float width, float height)
			throws Exception {
		Image image = ImageIO.read(imageStream);
		drawImage(imageId, image, x, y, width, height);
	}

	/**
	 * Draws a image with specified image data, position, size and background color.
	 *
	 * @param image   the source image data.
	 * @param x       the x position.
	 * @param y       the y position.
	 * @param width   the image width.
	 * @param height  the image height.
	 * @param bgcolor the background color.
	 * @throws Exception
	 */
	public void drawImage(String imageId, Image image, float x, float y, float width, float height) throws IOException {
		if (image == null) {
			throw new IllegalArgumentException("null image.");
		}
		y = transformY(y);

		// if imageId is null, the image will not be cached.
		if (imageId == null) {
			outputUncachedImage(image, x, y, width, height);
		} else {
			// NOTICE: if width or height is 0, then the width and height will
			// be replaced by the intrinsic width and height of the image. This
			// logic is hard coded in postscript code and can't be found in java
			// code.
			outputCachedImage(imageId, image, x, y, width, height);
		}
	}

	private void outputCachedImage(String imageId, Image image, float x, float y, float width, float height)
			throws IOException {
		String imageName = getImageName(imageId, image);
		out.print(imageName + " ");
		out.print(x + " " + y + " ");
		out.println(width + " " + height + " drawimage");
	}

	private void outputUncachedImage(Image image, float x, float y, float width, float height) throws IOException {
		ArrayImageSource imageSource = getImageSource(image);
		out.print(x + " " + y + " ");
		out.print(width + " " + height + " ");
		out.print(imageSource.getWidth() + " " + imageSource.getHeight());
		out.println(" drawstreamimage");
		outputImageSource(imageSource);
		out.println("grestore");
	}

	private ArrayImageSource getImageSource(Image image) throws IOException {
		ImageIcon imageIcon = new ImageIcon(image);
		int w = imageIcon.getIconWidth();
		int h = imageIcon.getIconHeight();
		int[] pixels = new int[w * h];

		try {
			PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
			pg.grabPixels();
			if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
				throw new IOException("failed to load image contents");
			}
		} catch (InterruptedException e) {
			throw new IOException("image load interrupted");
		}

		ArrayImageSource imageSource = new ArrayImageSource(w, h, pixels);
		return imageSource;
	}

	private String getImageName(String imageId, Image image) throws IOException {
		String name = (String) cachedImageSource.get(imageId);
		if (name == null) {
			name = "image" + imageIndex++;
			cachedImageSource.put(imageId, name);
			ArrayImageSource imageSource = getImageSource(image);
			outputNamedImageSource(name, imageSource);
		}
		return name;
	}

	private void outputNamedImageSource(String name, ArrayImageSource imageSource) {
		out.println("startDefImage");
		outputImageSource(imageSource);
		out.println("/" + name + " " + imageSource.getWidth() + " " + imageSource.getHeight() + " endDefImage");
	}

	private void outputImageSource(ArrayImageSource imageSource) {
		int originalWidth = imageSource.getWidth();
		int originalHeight = imageSource.getHeight();
		byte[] buffer = new byte[3 * originalHeight * originalWidth];
		int index = 0;
		for (int i = 0; i < originalHeight; i++) {
			for (int j = 0; j < originalWidth; j++) {
				int pixel = imageSource.getRGB(j, i);
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = pixel & 0xff;
				buffer[index++] = (byte) transferColor(alpha, red);
				buffer[index++] = (byte) transferColor(alpha, green);
				buffer[index++] = (byte) transferColor(alpha, blue);
			}
		}
		try {
			buffer = deflate(buffer);
			out.print(Util.toHexString(buffer) + ">");
		} catch (IOException e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	private byte[] deflate(byte[] source) throws IOException {
		ByteArrayOutputStream deflateSource = new ByteArrayOutputStream();
		DeflaterOutputStream deflateOut = new DeflaterOutputStream(deflateSource,
				new Deflater(Deflater.DEFAULT_COMPRESSION));
		deflateOut.write(source);
		deflateOut.finish();
		deflateOut.close();
		byte[] byteArray = deflateSource.toByteArray();
		deflateSource.close();
		return byteArray;
	}

	private int transferColor(int alpha, int color) {
		return 255 - (255 - color) * alpha / 255;
	}

	protected void drawRect(float x, float y, float width, float height) {
		drawRawRect(x, y, width, height);
		out.println("fill");
	}

	private void drawRawRect(float x, float y, float width, float height) {
		y = transformY(y);
		out.println(x + " " + y + " " + width + " " + height + " rect");
	}

	/**
	 * Draws background image in a rectangle area with specified repeat pattern.
	 * <br>
	 * <br>
	 * The repeat mode can be:
	 * <table border="solid">
	 * <tr>
	 * <td align="center"><B>Name</td>
	 * <td align="center"><B>What for</td>
	 * </tr>
	 * <tr>
	 * <td>no-repeat</td>
	 * <td>Don't repeat.</td>
	 * </tr>
	 * <tr>
	 * <td>repeat-x</td>
	 * <td>Only repeat on x orientation.</td>
	 * </tr>
	 * <tr>
	 * <td>repeat-y</td>
	 * <td>Only repeat on y orientation.</td>
	 * </tr>
	 * <tr>
	 * <td>repeat</td>
	 * <td>Repeat on x and y orientation.</td>
	 * </tr>
	 * </table>
	 *
	 * @param x         the x coordinate of the rectangle area.
	 * @param y         the y coordinate of the rectangle area.
	 * @param width     the width of the rectangle area.
	 * @param height    the height of the rectangle area.
	 * @param positionX the initial x position of the background image.
	 * @param positionY the initial y position of the background image.
	 * @param repeat    the repeat mode.
	 * @throws Exception
	 */
	public void drawBackgroundImage(String imageURI, byte[] imageData, float x, float y, float width, float height,
			float imageWidth, float imageHeight, float positionX, float positionY, int repeat) throws IOException {
		if (imageData == null || imageData.length == 0) {
			return;
		}
		org.eclipse.birt.report.engine.layout.emitter.Image image = EmitterUtil.parseImage(imageData, null, null);
		imageData = image.getData();

		if (imageWidth == 0 || imageHeight == 0) {
			int resolutionX = image.getPhysicalWidthDpi();
			int resolutionY = image.getPhysicalHeightDpi();
			if (resolutionX <= 0 || resolutionY <= 0) {
				resolutionX = 96;
				resolutionY = 96;
			}
			imageWidth = ((float) image.getWidth()) / resolutionX * 72;
			imageHeight = ((float) image.getHeight()) / resolutionY * 72;
		}

		Position imageSize = new Position(imageWidth, imageHeight);
		Position areaPosition = new Position(x, y);
		Position areaSize = new Position(width, height);
		Position imagePosition = new Position(x + positionX, y + positionY);
		BackgroundImageLayout layout = new BackgroundImageLayout(areaPosition, areaSize, imagePosition, imageSize);
		Collection positions = layout.getImagePositions(repeat);
		gSave();
		setColor(Color.WHITE);
		out.println("newpath");
		drawRawRect(x, y, width, height);
		out.println("closepath clip");
		Iterator iterator = positions.iterator();
		while (iterator.hasNext()) {
			Position position = (Position) iterator.next();
			try {
				drawImage(imageURI, new ByteArrayInputStream(imageData), position.getX(), position.getY(),
						imageSize.getX(), imageSize.getY());
			} catch (Exception e) {
				log.log(Level.WARNING, e.getLocalizedMessage());
			}
		}
		gRestore();
	}

	/**
	 * Draws a line from (startX, startY) to (endX, endY) with specified line width,
	 * color and line style.
	 *
	 * Line style can be "dotted", "dash", and "double".
	 *
	 * @param startX    the x coordinate of start point.
	 * @param startY    the y coordinate of start point.
	 * @param endX      the x coordinate of end point.
	 * @param endY      the y coordinate of end point.
	 * @param width     the line width.
	 * @param color     the color.
	 * @param lineStyle the line style.
	 */
	public void drawLine(float startX, float startY, float endX, float endY, float width, Color color, int lineStyle) {
		// double is not supported.
		if (null == color || 0f == width || lineStyle == BorderInfo.BORDER_STYLE_NONE || (lineStyle == BorderInfo.BORDER_STYLE_DOUBLE)) // $NON-NLS-1$
		{
			return;
		}
		int dashMode = 0;
		if (lineStyle == BorderInfo.BORDER_STYLE_DASHED) // $NON-NLS-1$
		{
			dashMode = 1;
		} else if (lineStyle == BorderInfo.BORDER_STYLE_DOTTED) // $NON-NLS-1$
		{
			dashMode = 2;
		}
		startY = transformY(startY);
		endY = transformY(endY);
		outputColor(color);
		out.print(width + " " + dashMode + " ");
		out.print(startX + " " + startY + " ");
		out.print(endX + " " + endY + " ");
		out.println("drawline");
	}

	private void gRestore() {
		out.println("grestore");
		graphics.pop();
	}

	private void gSave() {
		out.println("gsave");
		graphics.push(new Graphic());
	}

	private Map<File, ITrueTypeWriter> trueTypeFontWriters = new HashMap<>();

	private String orientation;

	private int scale;

	private boolean autoPaperSizeSelection;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.emitter.postscript.IWriter#drawString(
	 * java.lang.String, int, int,
	 * org.eclipse.birt.report.engine.layout.pdf.font.FontInfo, float, float,
	 * java.awt.Color, boolean, boolean, boolean)
	 */
	public void drawString(String str, float x, float y, FontInfo fontInfo, float letterSpacing, float wordSpacing,
			Color color, boolean linethrough, boolean overline, boolean underline, CSSValue align) {
		y = transformY(y);
		String text = str;
		boolean needSimulateItalic = false;
		boolean needSimulateBold = false;
		boolean hasSpace = wordSpacing != 0 || letterSpacing != 0;
		float offset = 0;
		if (fontInfo != null) {
			float fontSize = fontInfo.getFontSize();
			int fontStyle = fontInfo.getFontStyle();
			if (fontInfo.getSimulation()) {
				if (fontStyle == Font.BOLD || fontStyle == Font.BOLDITALIC) {
					offset = (float) (fontSize * Math.log10(fontSize) / 100);
					needSimulateBold = true;
				}
				if (fontStyle == Font.ITALIC || fontStyle == Font.BOLDITALIC) {
					needSimulateItalic = true;
				}
			}
			BaseFont baseFont = fontInfo.getBaseFont();
			String fontName = baseFont.getPostscriptFontName();
			text = applyFont(fontName, fontStyle, fontSize, text);
		}
		outputColor(color);
		out.print(x + " " + y + " ");
		if (hasSpace) {
			out.print(wordSpacing + " " + letterSpacing + " ");
		}
		out.print(text + " ");
		if (needSimulateBold) {
			out.print(offset + " ");
		}
		String command = getCommand(hasSpace, needSimulateBold, needSimulateItalic);
		out.println(command);
	}

	private String getCommand(boolean hasSpace, boolean needSimulateBold, boolean needSimulateItalic) {
		int index = toInt(hasSpace);
		index += toInt(needSimulateBold) << 1;
		index += toInt(needSimulateItalic) << 2;
		return stringCommands[index];
	}

	private int toInt(boolean b) {
		return b ? 1 : 0;
	}

	/**
	 * Top of every PS file
	 */

	protected void emitProlog(String title) {
		out.println("%!PS-Adobe-3.0");
		if (title != null) {
			out.println("%%Title: " + title);
		}
		out.println("% (C)2006 Actuate Inc.");
	}

	public void fillRect(float x, float y, float width, float height, Color color) {
		gSave();
		setColor(color);
		drawRect(x, y, width, height);
		gRestore();
	}

	/**
	 * Disposes of this graphics context once it is no longer referenced.
	 *
	 * @see #dispose
	 */

	@Override
	public void finalize() {
		dispose();
	}

	public void dispose() {
		out.println("%dispose");
	}

	public Color getColor() {
		return clr;
	}

	public Font getFont() {
		return font;
	}

	public void setColor(Color c) {
		outputColor(c);
	}

	private void outputColor(Color c) {
		if (c == null) {
			c = Color.black;
		}
		Graphic currentGraphic = getCurrentGraphic();
		if (c.equals(currentGraphic.color)) {
			return;
		}
		currentGraphic.color = c;
		out.print(c.getRed() / 255.0);
		out.print(" ");
		out.print(c.getGreen() / 255.0);
		out.print(" ");
		out.print(c.getBlue() / 255.0);
		out.print(" ");
		out.println("setrgbcolor");
	}

	public void setFont(Font f) {
		if (f != null) {
			this.font = f;
			String javaName = font.getFamilyname();
			int javaStyle = font.getStyle();
			setFont(javaName, javaStyle);
		}
	}

	private boolean needSetFont(String fontName, float fontSize) {
		Graphic graphic = getCurrentGraphic();
		float currentFontSize = graphic.fontSize;
		String currentFont = graphic.font;

		// If fontSize is changed, set font.
		// If font name is changed, set font.
		if ((fontSize != currentFontSize) || (currentFont == null && fontName != null)) {
			return true;
		}
		if (currentFont != null && !currentFont.equals(fontName)) {
			return true;
		}
		return false;
	}

	private void setFont(String font, float size) {
		if (needSetFont(font, size)) {
			setCurrentGraphic(font, size);
			out.println("/" + font + " " + size + " usefont");
		}
	}

	private void setCurrentGraphic(String font, float fontSize) {
		Graphic graphic = getCurrentGraphic();
		graphic.font = font;
		graphic.fontSize = fontSize;
	}

	private Graphic getCurrentGraphic() {
		Graphic currentGraphic = null;
		if (graphics.isEmpty()) {
			currentGraphic = new Graphic();
			graphics.push(currentGraphic);
		} else {
			currentGraphic = graphics.peek();
		}
		return currentGraphic;
	}

	private String applyFont(String fontName, int fontStyle, float fontSize, String text) {
		if (isIntrinsicFont(fontName)) {
			return applyIntrinsicFont(fontName, fontStyle, fontSize, text);
		} else {
			try {
				String fontPath = getFontPath(fontName);
				if (fontPath == null) {
					return applyIntrinsicFont(fontName, fontStyle, fontSize, text);
				}
				ITrueTypeWriter trueTypeWriter = getTrueTypeFontWriter(fontPath, fontName);

				// Space can't be included in a identity.
				trueTypeWriter.ensureGlyphsAvailable(text);
				String displayName = trueTypeWriter.getDisplayName();
				setFont(displayName, fontSize);
				return trueTypeWriter.toHexString(text);
			} catch (IOException | DocumentException de) {
				log.log(Level.WARNING, "apply font: " + fontName);
			}
			return null;
		}
	}

	private String applyIntrinsicFont(String fontName, int fontStyle, float fontSize, String text) {
		setFont(fontName, fontSize);
		text = escapeSpecialCharacter(text);
		return ("(" + text + ")");
	}

	/**
	 * Escape the characters "(", ")", and "\" in a postscript string by "\".
	 *
	 * @param source
	 * @return
	 */
	private static String escapeSpecialCharacter(String source) {
		Pattern pattern = Pattern.compile("(\\\\|\\)|\\()");
		Matcher matcher = pattern.matcher(source);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(buffer, "\\\\\\" + matcher.group(1));
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	private ITrueTypeWriter getTrueTypeFontWriter(String fontPath, String fontName)
			throws DocumentException, IOException {
		File file = new File(fontPath);
		ITrueTypeWriter trueTypeWriter = (ITrueTypeWriter) trueTypeFontWriters.get(file);
		if (trueTypeWriter != null) {
			return trueTypeWriter;
		} else {
			TrueTypeFont ttFont = TrueTypeFont.getInstance(fontPath);
			trueTypeWriter = ttFont.getTrueTypeWriter(out);
			trueTypeWriter.initialize(fontName);
			trueTypeFontWriters.put(file, trueTypeWriter);
			return trueTypeWriter;
		}
	}

	private String getFontPath(String fontName) {
		try {
			FontFactoryImp fontImpl = FontFactory.getFontImp();
			Properties trueTypeFonts = (Properties) getField(FontFactoryImp.class, "trueTypeFonts", fontImpl);
			String fontPath = trueTypeFonts.getProperty(fontName.toLowerCase());
			return fontPath;
		} catch (IllegalAccessException | NoSuchFieldException e) {
			log.log(Level.WARNING, "font path: " + fontName);
		}
		return null;
	}

	private Object getField(final Class fontFactoryClass, final String fieldName, final Object instaces)
			throws NoSuchFieldException, IllegalAccessException {
		try {
			Object field = (Object) AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {

				@Override
				public Object run() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
					Field fldTrueTypeFonts = fontFactoryClass.getDeclaredField(fieldName);// $NON-SEC-3
					fldTrueTypeFonts.setAccessible(true);// $NON-SEC-2
					return fldTrueTypeFonts.get(instaces);
				}
			});
			return field;
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) typedException;
			}
			if (typedException instanceof IllegalAccessException) {
				throw (IllegalAccessException) typedException;
			}
			if (typedException instanceof NoSuchFieldException) {
				throw (NoSuchFieldException) typedException;
			}
		}
		return null;
	}

	protected float transformY(float y) {
		return pageHeight - y;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.emitter.postscript.IWriter#translate(int,
	 * int)
	 */

	public void translate(int x, int y) {
		out.print(x);
		out.print(" ");
		out.print(y);
		out.println(" translate");
	}

	public void startRenderer() throws IOException {
		startRenderer(null, null, null, null, null, 1, false, null, false, 100, false, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.postscript.IWriter#startRenderer()
	 */
	public void startRenderer(String author, String description, String paperSize, String paperTray, Object duplex,
			int copies, boolean collate, String resolution, boolean color, int scale, boolean autoPaperSizeSelection,
			boolean fitToPaper) throws IOException {
		this.scale = scale;
		this.fitToPaper = fitToPaper;
		this.autoPaperSizeSelection = autoPaperSizeSelection;
		if (author != null) {
			out.println("%%Creator: " + author);
		}
		out.println("%%Pages: (atend)");
		out.println("%%DocumentProcessColors: Black");
		out.println("%%BeginSetup");
		setCollate(collate);
		setCopies(copies);
		int[] pageSize = getPaperSize(paperSize);
		if (pageSize != null) {
			int width = pageSize[0];
			int height = pageSize[1];
			setPaperSize(paperSize, width, height);
		}
		setPaperTray(paperTray);
		setDuplex(duplex);
		setResolution(resolution);
		setGray(color);
		FileUtil.load("org/eclipse/birt/report/engine/emitter/postscript/header.ps", out);
		out.println("%%EndResource");
		out.println("%%EndSetup");
	}

	private void setPaperTray(String paperTray) {
		String trayString = getPaperTrayCode(paperTray);
		if (trayString != null) {
			out.println("%%BeginFeature: *InputSlot tray1");
			out.println(trayString);
			out.println("%%EndFeature");
		}
	}

	private String getPaperTrayCode(String trayCode) {
		if (trayCode == null) {
			return null;
		}
		try {
			int paperTray = Integer.parseInt(trayCode);
			if (paperTray == TRAYCODE_MANUAL) {
				return "<</ManualFeed true /TraySwitch false>>setpagedevice";
			}
			// tray code should be positive number
			// bigger than
			// 257. For some printers use 257 and /MediaPosition
			// 0 for first printer, whiles some others printers
			// use code 258 and /MediaPosition 1 for first paper
			// tray.
			paperTray = paperTray - 257;
			if (paperTray < 0) {
				return null;
			}
			return "<</ManualFeed false /MediaPosition " + paperTray + " /TraySwitch false>>setpagedevice";
		} catch (NumberFormatException e) {
			return trayCode;
		}
	}

	private void setPaperSize(String paperSize, int width, int height) {
		if (paperSize != null) {
			out.println("%%BeginFeature: *PageSize " + paperSize);
			out.println("<</PageSize [" + width + " " + height + "] /ImagingBBox null>> setpagedevice");
			out.println("%%EndFeature");
		}
	}

	private void setCopies(int copies) {
		if (copies > 1) {
			out.println("%%BeginNonPPDFeature: NumCopies " + copies);
			out.println("<</NumCopies " + copies + ">> setpagedevice");
			out.println("%%EndNonPPDFeature");
		}
	}

	private void setCollate(boolean collate) {
		if (collate) {
			out.println("%%BeginFeature: *Collate true");
			out.println("1  dict dup /Collate true put setpagedevice");
			out.println("%%EndFeature");
		}
	}

	private void setDuplex(Object duplex) {
		String duplexValue = null;
		boolean tumble = false;
		if (duplex instanceof String) {
			String value = (String) duplex;
			if ("SIMPLEX".equalsIgnoreCase(value)) {
				return;
			}
			isDuplex = true;
			if ("HORIZONTAL".equalsIgnoreCase(value)) {
				duplexValue = "DuplexNoTumble";
				tumble = false;
			} else if ("VERTICAL".equalsIgnoreCase(value)) {
				duplexValue = "DuplexTumble";
				tumble = true;
			}
		} else if (duplex instanceof Integer) {
			int value = (Integer) duplex;
			if (value == IPostscriptRenderOption.DUPLEX_SIMPLEX) {
				return;
			}
			isDuplex = true;
			if (value == IPostscriptRenderOption.DUPLEX_FLIP_ON_LONG_EDGE) {
				duplexValue = "DuplexNoTumble";
				tumble = false;
			} else if (value == IPostscriptRenderOption.DUPLEX_FLIP_ON_SHORT_EDGE) {
				duplexValue = "DuplexTumble";
				tumble = true;
			}
		}
		out.println("%%BeginFeature: *Duplex " + duplexValue);
		out.println("<</Duplex true /Tumble " + tumble + ">> setpagedevice");
		if (tumble) {
			out.println("currentpagedevice /Binding known {<</Binding 3>> setpagedevice}if");
		}
		out.println("%%EndFeature");
	}

	private void setResolution(String resolution) {
		if (resolution != null && resolution.length() > 0) {
			int split = resolution.indexOf("x");
			if (split == -1) {
				split = resolution.indexOf("X");
			}
			if (split != -1) {
				int xResolution = new Integer(resolution.substring(0, split).trim());
				int yResolution = new Integer(resolution.substring(split + 1).trim());
				if (xResolution > 0 && yResolution > 0) {
					out.println("%%BeginFeature: *Resolution " + xResolution + "x" + yResolution + "dpi");
					out.println(" << /HWResolution [" + xResolution + " " + yResolution + "]");
					out.println("  /Policies << /HWResolution 2 >>");
					out.println(" >> setpagedevice");
					out.println("%%EndFeature");
				}
			}
		}
	}

	private void setGray(boolean color) {
		if (!color) {
			out.println("%%BeginFeature: *HPColorAsGray true");
			out.println("<</ProcessColorModel /DeviceGray>> setpagedevice");
			out.println("%%EndFeature");
		}
	}

	private void setScale(int height, int scale) {
		if (scale != 100) {
			float absoluteScale = scale / 100f;
			float yOffset = height * (1 - absoluteScale);
			out.println("/mysetup [ " + absoluteScale + " 0 0 " + absoluteScale + " 0 " + yOffset + "] def");
			out.println("mysetup concat");
		}
	}

	private int[] getPaperSize(String paperSize) {
		if (paperSize == null || paperSize.trim().length() == 0) {
			return null;
		}
		int width = 595;
		int height = 842;
		if ("Letter".equalsIgnoreCase(paperSize)) {
			width = 612;
			height = 792;
		} else if ("Legal".equalsIgnoreCase(paperSize)) {
			width = 612;
			height = 1008;
		} else if ("A5".equalsIgnoreCase(paperSize)) {
			width = 419;
			height = 595;
		} else if ("A4".equalsIgnoreCase(paperSize)) {
			width = 595;
			height = 842;
		} else if ("A3".equalsIgnoreCase(paperSize)) {
			width = 842;
			height = 1191;
		} else if ("B5".equalsIgnoreCase(paperSize)) {
			width = 499;
			height = 709;
		} else if ("B4".equalsIgnoreCase(paperSize)) {
			width = 729;
			height = 1032;
		}
		return new int[] { width, height };
	}

	public void fillPage(Color color) {
		if (color == null) {
			return;
		}
		gSave();
		setColor(color);
		out.println("clippath fill");
		gRestore();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.postscript.IWriter#startPage(float ,
	 * float)
	 */
	public void startPage(float pageWidth, float pageHeight, String orientation) {
		boolean isLandscape = isLandscape(orientation);
		boolean paperChanged = this.pageHeight == pageHeight && this.pageWidth == pageWidth
				&& isLandscape == isLandscape(this.orientation);
		this.orientation = orientation;
		this.pageHeight = pageHeight;
		this.pageWidth = pageWidth;
		out.println("%%Page: " + pageIndex + " " + pageIndex);

		if (autoPaperSizeSelection && (!isDuplex || paperChanged)) {
			if (isLandscape) {
				setPaperSize("auto", (int) pageHeight, (int) pageWidth);
			} else {
				setPaperSize("auto", (int) pageWidth, (int) pageHeight);
			}
		}
		out.println("%%PageBoundingBox: 0 0 " + (int) Math.round(pageWidth) + " " + (int) Math.round(pageHeight));
		out.println("%%BeginPage");
		if (fitToPaper && paperWidth != 0 && paperHeight != 0) {
			int height = isLandscape ? (int) pageWidth : (int) pageHeight;
			int width = isLandscape ? (int) pageHeight : (int) pageWidth;
			out.println(paperWidth + " " + width + " div " + paperHeight + " " + height
					+ " div 2 copy gt {exch}if pop dup scale");
		}
		if (isLandscape) {
			gSave();
			out.println("90 rotate");
			out.println("1 -1 scale");
			out.print("[1 0 0 -1 0 ");
			out.println(pageHeight + "] concat");
		}
		setScale((int) pageHeight, scale);
		++pageIndex;
	}

	public boolean isLandscape(String orientation) {
		boolean isLandscape = orientation != null && orientation.equalsIgnoreCase("Landscape");
		return isLandscape;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.emitter.postscript.IWriter#endPage()
	 */
	public void endPage() {
		if (orientation != null && orientation.equalsIgnoreCase("Landscape")) {
			gRestore();
		}
		out.println("showpage");
		out.println("%%PageTrailer");
		out.println("%%EndPage");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.emitter.postscript.IWriter#stopRenderer()
	 */
	public void stopRenderer() throws IOException {
		out.println("%%Trailer");
		out.println("%%Pages: " + (pageIndex - 1));
		out.println("%%EOF");
		out.flush();
	}

	public abstract class ImageSource {

		protected int height;
		protected int width;

		public ImageSource(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public int getHeight() {
			return height;
		}

		public int getWidth() {
			return width;
		}

		public abstract int getRGB(int x, int y);
	}

	public class ArrayImageSource extends ImageSource {

		private int[] imageSource;

		public ArrayImageSource(int width, int height, int[] imageSource) {
			super(width, height);
			this.imageSource = imageSource;
		}

		@Override
		public int getRGB(int x, int y) {
			return imageSource[y * width + x];
		}

		public int[] getData() {
			return imageSource;
		}
	}

	public void close() throws IOException {
		stopRenderer();
		out.close();
	}

	private static class Graphic {

		private String font;

		private float fontSize;

		private Color color;

		public Graphic() {

		}
	}
}
