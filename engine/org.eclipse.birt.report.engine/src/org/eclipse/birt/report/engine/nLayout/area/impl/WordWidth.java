package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;

/**
 * The width of a word consists of two values:
 *
 * First, the width of the word itself.
 *
 * Second, the width needed to display the soft hyphen if the word ends with a
 * Unicode SOFT HYPHEN and the word is shown at the end of a line.
 *
 */
public final class WordWidth {

	/**
	 * @see TextArea#isKeepTrailingSoftHyphen()
	 */
	private static final String SOFT_HYPHEN = "\u00ad";

	/*
	 * Calculates the width for a word in a given font.
	 */
	public WordWidth(FontInfo fontInfo, String text) {
		int _width = getTextWidth(fontInfo, text);
		if (text.endsWith(SOFT_HYPHEN)) {
			softHyphenWidth = getTextWidth(fontInfo, "-");
			// We are using the Unicode MINUS here for computing the hyphen dash size,
			// because getTextWidth for the SOFT HYPHEN would return zero width for some
			// fonts.
			width = _width - getTextWidth(fontInfo, SOFT_HYPHEN);
		} else {
			width = _width;
			softHyphenWidth = 0;
		}
	}

	public WordWidth(int width, int softHyphenWidth) {
		this.width = width;
		this.softHyphenWidth = softHyphenWidth;
	}

	/**
	 * The width of the word itself (without a trailing soft hyphen).
	 *
	 * This much place is needed if the word is shown in the middle of a line.
	 */
	public final int width;

	/**
	 * The width of SOFT HYPHEN.
	 *
	 * This needs to be added if the word is shown at the end of a line.
	 *
	 * A non-zero value implies that the word ended with a soft hyphen.
	 */
	public final int softHyphenWidth;

	public static int getTextWidth(FontInfo fontInfo, String text) {
		return (int) (fontInfo.getWordWidth(text) * PDFConstants.LAYOUT_TO_PDF_RATIO);
	}

}
