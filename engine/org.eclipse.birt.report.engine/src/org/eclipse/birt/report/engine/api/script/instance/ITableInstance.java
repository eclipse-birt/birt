package org.eclipse.birt.report.engine.api.script.instance;

public interface ITableInstance extends IReportItemInstance {

	/**
	 * Get the caption.
	 * 
	 */
	String getCaption();

	/**
	 * Set the caption
	 * 
	 */
	void setCaption(String caption);

	/**
	 * Get the caption key
	 */
	String getCaptionKey();

	/**
	 * Set the caption key
	 */
	void setCaptionKey(String captionKey);

	/**
	 * Get repeat header
	 * 
	 */
	boolean getRepeatHeader();

	/**
	 * Set repeat header
	 * 
	 * @param repeat
	 */
	void setRepeatHeader(boolean repeat);

	/**
	 * Get the summary.
	 * 
	 */
	String getSummary();

	/**
	 * Set the summary
	 * 
	 */
	void setSummary(String summary);

	/**
	 * Get column's count on the table.
	 * 
	 */
	int getColumnCount();

	/**
	 * Get column according to the index.
	 * 
	 */
	IColumnInstance getColumn(int index);

}