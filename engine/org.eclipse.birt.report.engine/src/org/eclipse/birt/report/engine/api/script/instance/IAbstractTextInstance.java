package org.eclipse.birt.report.engine.api.script.instance;

public interface IAbstractTextInstance extends IReportItemInstance {

	/**
	 * Get the value
	 */
	String getText();

	/**
	 * Set the value
	 */
	void setText(String value);

}