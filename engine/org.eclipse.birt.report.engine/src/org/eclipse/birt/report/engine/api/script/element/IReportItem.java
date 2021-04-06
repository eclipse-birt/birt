
package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.model.api.simpleapi.ScriptConstants;

/**
 * Represents a the design of a report item in the scripting environment
 */

public interface IReportItem extends IReportElement {
	static ScriptConstants constants = new ScriptConstants();

	/**
	 * Gets the item's x (horizontal) position.
	 * 
	 * @return The item's x position.
	 */

	String getX();

	/**
	 * Gets the item's y (vertical) position.
	 * 
	 * @return The item's y position.
	 */

	String getY();

	/**
	 * Sets the item's x position using a dimension string with optional unit suffix
	 * such as "10" or "10pt". If no suffix is provided, then the units are assumed
	 * to be in the design's default units. Call this method to set a string typed
	 * in by the user.
	 * 
	 * @param dimension dimension string with optional unit suffix.
	 * @throws ScriptException if the string is not valid
	 */

	void setX(String dimension) throws ScriptException;

	/**
	 * Sets the item's x position to a value in default units. The default unit may
	 * be defined by the property in BIRT or the application unit defined in the
	 * design session.
	 * 
	 * @param dimension the new value in application units.
	 * @throws ScriptException if the property is locked.
	 */

	void setX(double dimension) throws ScriptException;

	/**
	 * Sets the item's y position using a dimension string with optional unit suffix
	 * such as "10" or "10pt". If no suffix is provided, then the units are assumed
	 * to be in the design's default units. Call this method to set a string typed
	 * in by the user.
	 * 
	 * @param dimension dimension string with optional unit suffix.
	 * @throws ScriptException if the string is not valid
	 */

	void setY(String dimension) throws ScriptException;

	/**
	 * Sets the item's y position to a value in default units. The default unit may
	 * be defined by the property in BIRT or the application unit defined in the
	 * design session.
	 * 
	 * @param dimension the new value in application units.
	 * @throws ScriptException if the property is locked.
	 */

	void setY(double dimension) throws ScriptException;

	/**
	 * Sets the item's height using a dimension string with optional unit suffix
	 * such as "10" or "10pt". If no suffix is provided, then the units are assumed
	 * to be in the design's default units. Call this method to set a string typed
	 * in by the user.
	 * 
	 * @param dimension dimension string with optional unit suffix.
	 * @throws ScriptException if the string is not valid
	 */

	void setHeight(String dimension) throws ScriptException;

	/**
	 * Sets the item's height to a value in default units. The default unit may be
	 * defined by the property in BIRT or the application unit defined in the design
	 * session.
	 * 
	 * @param dimension the new value in application units.
	 * @throws ScriptException if the property is locked.
	 */

	void setHeight(double dimension) throws ScriptException;

	/**
	 * Sets the item's width using a dimension string with optional unit suffix such
	 * as "10" or "10pt". If no suffix is provided, then the units are assumed to be
	 * in the design's default units. Call this method to set a string typed in by
	 * the user.
	 * 
	 * @param dimension dimension string with optional unit suffix.
	 * @throws ScriptException if the string is not valid
	 */

	void setWidth(String dimension) throws ScriptException;

	/**
	 * Sets the item's width to a value in default units. The default unit may be
	 * defined by the property in BIRT or the application unit defined in the design
	 * session.
	 * 
	 * @param dimension the new value in application units.
	 * @throws ScriptException if the property is locked.
	 */

	void setWidth(double dimension) throws ScriptException;

	/**
	 * Gets a the item's width.
	 * 
	 * @return a the item's width.
	 */

	String getWidth();

	/**
	 * Gets the item's height.
	 * 
	 * @return the item's height.
	 */
	String getHeight();

	/**
	 * Returns the bookmark of the report item. The bookmark value is evaluated as
	 * an expression.
	 * 
	 * @return the book mark as a string
	 */

	String getBookmark();

	/**
	 * Sets the bookmark of the report item. The bookmark value is evaluated as an
	 * expression. If you want the bookmark to be the string "bookmark", you need to
	 * use setBookmark("\"bookmark\"");
	 * 
	 * If bookmark is a JavaScript variable, use setBookmark("bookmark");
	 * 
	 * @param value the bookmark expression
	 * @throws ScriptException if the property is locked.
	 */

	void setBookmark(String value) throws ScriptException;

	/**
	 * Sets a table of contents entry for this item. The TOC property defines an
	 * expression that returns a string that is to appear in the Table of Contents
	 * for this item or its container.
	 * 
	 * @param expression the expression that returns a string
	 * @throws ScriptException if the TOC property is locked by the property mask.
	 * 
	 * @see #getTocExpression()
	 */

	void setTocExpression(String expression) throws ScriptException;

	/**
	 * Returns the expression evalueated as a table of contents entry for this item.
	 * 
	 * @return the expression evaluated as a table of contents entry for this item
	 * @see #setTocExpression(String)
	 */

	String getTocExpression();

	/**
	 * Returns array of all column bindings
	 * 
	 * @return all column bindings
	 */

	IDataBinding[] getDataBindings();

	/**
	 * Returns column binding.
	 * 
	 * @param bindingName
	 * @return column binding expression.
	 */
	String getDataBinding(String bindingName);

	/**
	 * Removes special column binding
	 * 
	 * @param bindingName
	 * @throws ScriptException
	 */
	void removeDataBinding(String bindingName) throws ScriptException;

	/**
	 * Removes all column bindings
	 * 
	 * @throws ScriptException
	 */
	void removeDataBindings() throws ScriptException;

	/**
	 * Add ComputedColumn.name , expression of IDataBinding are required.
	 * 
	 * @param binding
	 * @throws ScriptException
	 */

	void addDataBinding(IDataBinding binding) throws ScriptException;

	/**
	 * Gets all high light rules.
	 * 
	 * @return all high light rules
	 */

	IHighlightRule[] getHighlightRules();

	/**
	 * Adds high light rule.
	 * 
	 * @param rule
	 * @throws ScriptException
	 */

	void addHighlightRule(IHighlightRule rule) throws ScriptException;

	/**
	 * Removes all high light rules.
	 * 
	 * @throws ScriptException
	 */

	void removeHighlightRules() throws ScriptException;

	/**
	 * Removes high light rule.
	 * 
	 * @param rule
	 * @throws ScriptException
	 */

	void removeHighlightRule(IHighlightRule rule) throws ScriptException;

	/**
	 * Removes all hide rules that matches formatType.
	 * 
	 * @param rule
	 * @exception ScriptException
	 */

	void removeHideRule(IHideRule rule) throws ScriptException;

	/**
	 * Removes all hide rules
	 * 
	 * @throws ScriptException
	 */

	void removeHideRules() throws ScriptException;

	/**
	 * Returns array of hide rule expression
	 * 
	 * @return array of hide rule expression
	 */

	IHideRule[] getHideRules();

	/**
	 * Add HideRule
	 * 
	 * @param rule
	 * @throws ScriptException
	 */

	void addHideRule(IHideRule rule) throws ScriptException;

	/**
	 * Sets the view to be used. If the given element is not in the multiple view,
	 * it will be added and set as the active view.
	 * 
	 * @param viewElement the view element, must not be <code>this</code>. Can be
	 *                    <code>null</code>.
	 * 
	 * @throws ScriptException if the given element resides in the other elements.
	 */

	public void setCurrentView(IDesignElement viewElement) throws ScriptException;

}