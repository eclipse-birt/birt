package org.eclipse.birt.report.engine.api.script.instance;

public interface IReportItemInstance extends IReportElementInstance {

	/**
	 * Get the hyperlink
	 * 
	 * @return the hyperlink
	 */
	String getHyperlink();

	/**
	 * Get the name
	 */
	String getName();

	/**
	 * Set the name
	 */
	void setName(String name);

	/**
	 * Get the help text
	 */
	String getHelpText();

	/**
	 * Set the help text
	 */
	void setHelpText(String helpText);

}