package org.eclipse.birt.report.engine.emitter.pdf;

/**
 * String constants for PDF tag names which are used by BIRT. These are tag
 * names used for tagged PDF and PDF/UA, with a few exceptions which are
 * meaningful only inside BIRT.
 *
 * @since 4.19
 */
public final class PdfTag {

	/**
	 * This is not a PDF tag name, but used as a place-holder.
	 *
	 * The actual PDF tag name will be set depending on the context.
	 */
	public static final String AUTO = "auto";

	/** PDF tag used for the page header. */
	public static final String PAGE_HEADER = "pageHeader";

	/** PDF tag used for the page footer. */
	public static final String PAGE_FOOTER = "pageFooter";

	/** PDF tag used for images and charts. */
	public static final String FIGURE = "Figure";

	/** PDF tag used for hyperlinks. */
	public static final String LINK = "Link";

	/** PDF tag used for tables (but not for grids!). */
	public static final String TABLE = "Table";

	/** PDF tag used for table rows. */
	public static final String TR = "TR";

	/** PDF tag used for table header cells. */
	public static final String TH = "TH";

	/** PDF tag used for table data cells. */
	public static final String TD = "TD";
}
