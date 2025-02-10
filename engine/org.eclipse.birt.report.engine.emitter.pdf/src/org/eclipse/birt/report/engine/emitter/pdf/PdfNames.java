package org.eclipse.birt.report.engine.emitter.pdf;

import com.lowagie.text.pdf.PdfName;

/**
 * Some standard PdfName constants which are used inside BIRT code, but are
 * missing in OpenPDF's PdfName class.
 *
 * As soon as OpenPDF also defines theses name constants, the corresponding
 * names should be removed here and references to them should be replace with
 * references to the OpenPDF's PdfName.
 *
 * A long term goal is to get rid of this class in BIRT once all names are
 * defined in OpenPDF.
 *
 * Some of these names are PDF tag names, some are names of attributes, some are
 * used for values of attributes.
 *
 * If you wonder, why we only have some constants here and create other PdfName
 * objects from strings on the fly: It's because we hope to get the best
 * performance this way. This class is related to class {@link PdfTag}. Creating
 * a {@link com.lowagie.text.pdf.PdfName} involves a little overhead, and
 * comparing it to another instance is a bit more complex than comparing two
 * strings. We create these constant values here a priori when we need the name
 * in source code, whereas we create the PdfName objects on the fly when the
 * name is read from a design file.
 *
 * For a description of the usage of the names, please refer to the PDF
 * specification EN/ISO 32000, in particular the sections about tagged PDF and
 * accessibility.
 *
 * @since 4.19
 *
 */
@SuppressWarnings("javadoc")
public final class PdfNames {

	public static final PdfName ALT = new PdfName("Alt");
	public static final PdfName ARTIFACT = new PdfName("Artifact");
	public static final PdfName BACKGROUND = PdfName.BACKGROUND;
	public static final PdfName BLOCK = new PdfName("Block");
	public static final PdfName COLSPAN = new PdfName("ColSpan");
	public static final PdfName COLUMN = new PdfName("Column");
	public static final PdfName FOOTER = new PdfName("Footer");
	public static final PdfName HEADER = new PdfName("Header");
	public static final PdfName HEADERS = new PdfName("Headers");
	public static final PdfName LAYOUT = new PdfName("Layout");
	public static final PdfName PAGINATION = new PdfName("Pagination");
	public static final PdfName PLACEMENT = new PdfName("Placement");
	public static final PdfName ROW = new PdfName("Row");
	public static final PdfName ROWSPAN = new PdfName("RowSpan");
	public static final PdfName SCOPE = new PdfName("Scope");
	public static final PdfName SUBTYPE = PdfName.SUBTYPE;
	public static final PdfName TYPE = PdfName.TYPE;
	public static final PdfName TR = new PdfName(PdfTag.TR);
}
