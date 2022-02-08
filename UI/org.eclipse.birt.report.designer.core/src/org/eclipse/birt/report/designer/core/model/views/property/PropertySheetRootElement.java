/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.core.model.views.property;

import org.eclipse.birt.report.model.api.GroupElementHandle;

/**
 * Provide the vitual root of properties view.
 */
public class PropertySheetRootElement {

	private GroupElementHandle model;
	private String displayName;

	/**
	 * Constructuor
	 * 
	 * @param model, selected element
	 */
	public PropertySheetRootElement(GroupElementHandle model) {
		this.model = model;
	}

	/**
	 * Set model
	 * 
	 * @return model
	 */
	public GroupElementHandle getModel() {
		return model;
	}

	/**
	 * Get display name
	 * 
	 * @return display name of root element
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set display name
	 * 
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof PropertySheetRootElement))
			return false;
		PropertySheetRootElement tmp = (PropertySheetRootElement) obj;
		if (tmp.getModel().getModuleHandle() == null || tmp.getDisplayName() == null)
			return false;
		if (tmp.getModel().getModuleHandle().equals(model.getModuleHandle())
				&& tmp.getDisplayName().equals(displayName))
			return true;
		return false;
	}

	public int hashCode() {
		int hashCode = 23;
		if (displayName != null)
			hashCode += displayName.hashCode() * 7;
		if (model.getModuleHandle() != null)
			hashCode += model.getModuleHandle().hashCode() * 13;
		return hashCode;
	}

}
