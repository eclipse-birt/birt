package org.eclipse.birt.report.engine.emitter.pdf;

import com.lowagie.text.pdf.PdfName;

/**
 * @since 4.19
 *
 */
public final class PdfTag {

	public static final PdfName ARTIFACT = new PdfName("Artifact");

	public static final String PAGE_HEADER = "pageHeader";
	public static final String PAGE_FOOTER = "pageFooter";
	public static final String FIGURE = "Figure";
	public static final String TABLE = "Table";
	public static final String TR = "TR";
	public static final String TH = "TH";
	public static final String TD = "TD";
}
