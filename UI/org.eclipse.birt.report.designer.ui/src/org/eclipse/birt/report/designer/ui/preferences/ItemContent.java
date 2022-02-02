/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

/**
 * Class used as a trivial case of an item of the table Serves as the business
 * object for the TableViewer.
 * 
 * An item has the following properties: Default name, Custom name and
 * Description.
 */

public class ItemContent {

	private String defaultName = ""; //$NON-NLS-1$
	private String displayName = ""; //$NON-NLS-1$
	private String customName = ""; //$NON-NLS-1$
	private String description = ""; //$NON-NLS-1$

	public ItemContent(String string) {
		super();
		this.setCustomName(string);
	}

	/**
	 * 
	 * @return Return the default name of ItemContent
	 */
	public String getDefaultName() {
		return defaultName;
	}

	/**
	 * 
	 * @return Returns the display name of the ItemContent
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set default name for ItemContent
	 * 
	 * @param string
	 */
	public void setDefaultName(String string) {
		defaultName = string.trim();
	}

	/**
	 * Sets the display name for the ItemContent
	 * 
	 * @param string
	 */
	public void setDisplayName(String string) {
		displayName = string.trim();
	}

	/**
	 * 
	 * @return custom name of ItemContent
	 */
	public String getCustomName() {
		return customName;
	}

	/**
	 * Set custom name for ItemContent
	 * 
	 * @param string
	 */
	public void setCustomName(String string) {
		customName = string.trim();
	}

	/**
	 * 
	 * @return the description of ItemContent
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description for ItemContent
	 * 
	 * @param string
	 */
	public void setDescription(String string) {
		description = string.trim();
	}

}
