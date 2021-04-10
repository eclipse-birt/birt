
package org.eclipse.birt.report.engine.api.script.instance;

public interface IDataItemInstance extends IReportItemInstance {

	Object getValue();

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

	/**
	 * set the display value of data item
	 * 
	 * @param value value to display
	 */
	void setDisplayValue(Object value);
}