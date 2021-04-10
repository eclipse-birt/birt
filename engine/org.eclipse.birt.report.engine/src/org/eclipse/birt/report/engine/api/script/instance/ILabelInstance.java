package org.eclipse.birt.report.engine.api.script.instance;

public interface ILabelInstance extends IReportItemInstance {

	/**
	 * Get the value
	 */
	String getText();

	/**
	 * Set the value
	 */
	void setText(String value);

	/**
	 * Get the text key
	 */
	String getTextKey();

	/**
	 * Set the text key
	 */
	void setTextKey(String key);

	/**
	 * Create a new action instance, witch can be bookmark, hyperlink or
	 * drillThrough. The default action instance type is NULL.
	 */
	IActionInstance createAction();

	/**
	 * Get the action instance.
	 */
	IActionInstance getAction();

	/**
	 * set the actionInstance
	 * 
	 * @param actionInstance
	 */
	void setAction(IActionInstance actionInstance);
}