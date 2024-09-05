package org.eclipse.birt.report.engine.nLayout.area;

/**
 * @since 4.17
 *
 */
public interface ITagType {

	/**
	 * Return PDF tag name, eg. P or H1.
	 *
	 * Can return null.
	 *
	 * @return PDF tag.
	 */
	public String getTagType();

}
