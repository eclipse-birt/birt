package org.eclipse.birt.report.engine.api.script.instance;

public interface IRowInstance extends IReportElementInstance
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
	String getHeight( );

	/**
	 * @return Returns the height.
	 */
	void setHeight( String height );

}