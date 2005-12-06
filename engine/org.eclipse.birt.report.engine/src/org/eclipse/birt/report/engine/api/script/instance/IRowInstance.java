package org.eclipse.birt.report.engine.api.script.instance;

import org.eclipse.birt.report.engine.ir.DimensionType;

public interface IRowInstance
{

	IScriptStyle getStyle( );

	/**
	 * @return the Bookmark value
	 */
	String getBookmarkValue( );

	/**
	 * @return the Bookmark value
	 */
	void setBookmark( String bookmark );

	/**
	 * @return Returns the height.
	 */
	DimensionType getHeight( );

	/**
	 * @return Returns the height.
	 */
	void setHeight( String height );

}