/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;

/**
 * Represents a the design of a report element in the scripting environment
 */
public interface IReportElement extends IDesignElement {

	/**
	 * Sets the custom XML.
	 * 
	 * @param customXml the custom XML to set
	 * @throws ScriptException if the custom XML is locked or not defined on this
	 *                         element.
	 */

	void setCustomXml(String customXml) throws ScriptException;

	/**
	 * Gets the custom XML.
	 * 
	 * @return the custom XML
	 */

	String getCustomXml();

	/**
	 * Sets the comments of the report element.
	 * 
	 * @param theComments the comments to set
	 * @throws ScriptException if the comments property is locked or not defined on
	 *                         this element.
	 */

	void setComments(String theComments) throws ScriptException;

	/**
	 * Gets the comments of the report element.
	 * 
	 * @return the comments of the report element
	 */

	String getComments();

	/**
	 * Sets the resource key of the display name.
	 * 
	 * @param displayNameKey the resource key of the display name
	 * @throws ScriptException if the display name resource-key property is locked
	 *                         or not defined on this element.
	 */

	void setDisplayNameKey(String displayNameKey) throws ScriptException;

	/**
	 * Gets the resource key of the display name.
	 * 
	 * @return the resource key of the display name
	 */

	String getDisplayNameKey();

	/**
	 * Sets the display name.
	 * 
	 * @param displayName the display name
	 * @throws ScriptException if the display name property is locked or not defined
	 *                         on this element.
	 */

	void setDisplayName(String displayName) throws ScriptException;

	/**
	 * Gets the display name.
	 * 
	 * @return the display name
	 */

	String getDisplayName();

	/**
	 * Returns the name of this element. Returns <code>null</code> if the element
	 * does not have a name. Many elements do not require a name. The name does not
	 * inherit. If this element does not have a name, it will not inherit the name
	 * of its parent element.
	 * 
	 * @return the element name, or null if the name is not set
	 */

	String getName();

	/**
	 * Sets the name of this element. If the name is <code>null</code>, then the
	 * name is cleared if this element does not require a name.
	 * 
	 * @param name the new name
	 * @throws ScriptException if the name is duplicate, or if the name is
	 *                         <code>null</code> and this element requires a name.
	 */

	void setName(String name) throws ScriptException;

}
