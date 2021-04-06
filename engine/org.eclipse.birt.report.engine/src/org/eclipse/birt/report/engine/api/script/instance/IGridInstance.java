package org.eclipse.birt.report.engine.api.script.instance;

public interface IGridInstance extends IReportItemInstance {
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
	 * Get the summary.
	 * 
	 */
	String getSummary();

	/**
	 * Set the summary
	 * 
	 */
	void setSummary(String summary);

}