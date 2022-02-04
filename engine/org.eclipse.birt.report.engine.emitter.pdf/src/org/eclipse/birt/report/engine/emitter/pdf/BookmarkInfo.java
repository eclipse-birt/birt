package org.eclipse.birt.report.engine.emitter.pdf;

/**
 * This describes a bookmark in the generated PDF.
 *
 * Note: This could be Record in Java 14+.
 */
public final class BookmarkInfo {

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("BookmarkInfo [name=");
		stringBuilder.append(name);
		stringBuilder.append(", pageNumber=");
		stringBuilder.append(pageNumber);
		stringBuilder.append(", x=");
		stringBuilder.append(x);
		stringBuilder.append(", y=");
		stringBuilder.append(y);
		stringBuilder.append(", width=");
		stringBuilder.append(width);
		stringBuilder.append(", height=");
		stringBuilder.append(height);
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	/**
	 * Creates a new BookmarkInfo. It consists of a unique name and the target. The
	 * target is specified by the 1-based page number and a rectangle. The
	 * rectangle's x and y coordinates and its width and height are specified in
	 * milli-points. E.g. a value of 1000 means 1pt, a value of 72000 is one inch.
	 * An A4 page is ~ 595275 x 841890.
	 *
	 * @param name       Unique name
	 * @param pageNumber page number, 1-based
	 * @param x          left position
	 * @param y          top position, 0 = top of the page.
	 * @param width      width
	 * @param height     height
	 */
	public BookmarkInfo(String name, int pageNumber, int x, int y, int width, int height) {
		super();
		this.name = name;
		this.pageNumber = pageNumber;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/** Unique name of the bookmark, used as a key */
	public final String name;

	/** Page number of the bookmark target, starting from 1. 1, 2, 3, ... */
	public final int pageNumber;

	/** Distance of the bookmark target from the left end of the page. */
	public final int x;

	/** Distance of the bookmark target from the top end of the page. */
	public final int y;

	/** Width of the bookmark target */
	public final int width;

	/** Height of the bookmark target */
	public final int height;

}
