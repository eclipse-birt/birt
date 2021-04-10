package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Represents a the design of a Label in the scripting environment
 */
public interface ILabel extends IReportItem {

	/**
	 * Returns the static text for the label.
	 * 
	 * @return the static text to display
	 */

	String getText();

	/**
	 * Returns the localized text for the label. If the localized text for the text
	 * resource key is found, it will be returned. Otherwise, the static text will
	 * be returned.
	 * 
	 * @return the localized text for the label
	 */

	String getDisplayText();

	/**
	 * Sets the text of the label. Sets the static text itself. If the label is to
	 * be externalized, then set the text ID separately.
	 * 
	 * @param text the new text for the label
	 * @throws SemanticException if the property is locked.
	 */

	void setText(String text) throws SemanticException;

	/**
	 * Returns the resource key of the static text of the label.
	 * 
	 * @return the resource key of the static text
	 */

	String getTextKey();

	/**
	 * Sets the resource key of the static text of the label.
	 * 
	 * @param resourceKey the resource key of the static text
	 * 
	 * @throws SemanticException if the resource key property is locked.
	 */

	void setTextKey(String resourceKey) throws SemanticException;

	/**
	 * Returns a handle to work with the action property, action is a structure that
	 * defines a hyperlink.
	 * 
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the label.
	 * @see ActionHandle
	 */

	IAction getAction();

	/**
	 * Add the action structure to this Label item.
	 * 
	 * @param action
	 */
	void addAction(IAction action) throws SemanticException;

	/**
	 * Returns the help text of this label item.
	 * 
	 * @return the help text
	 */

	String getHelpText();

	/**
	 * Sets the help text of this label item.
	 * 
	 * @param text the help text
	 * 
	 * @throws SemanticException if the resource key property is locked.
	 */

	void setHelpText(String text) throws SemanticException;

	/**
	 * Returns the help text key of this label item.
	 * 
	 * @return the help text key
	 */

	String getHelpTextKey();

	/**
	 * Sets the help text key of this label item.
	 * 
	 * @param resourceKey the help text key
	 * 
	 * @throws SemanticException if the resource key property of the help text is
	 *                           locked.
	 */

	void setHelpTextKey(String resourceKey) throws SemanticException;

}