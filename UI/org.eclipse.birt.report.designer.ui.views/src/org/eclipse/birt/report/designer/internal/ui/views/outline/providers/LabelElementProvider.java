/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * Abstract class for those nodes that need to display description
 */

abstract public class LabelElementProvider extends DefaultNodeProvider {

	private static final int DESC_MAX_LENGTH = 12;

	private static final String SUSPENSION_POINTS = "..."; //$NON-NLS-1$

	public String getNodeDisplayName(Object model) {
		DesignElementHandle handle = (DesignElementHandle) model;
		String elementName = handle.getDefn().getDisplayName();
		String displayName = handle.getDisplayLabel();
		String description = getDescription(handle);
		if (elementName.equals(displayName)) {
			return elementName + description;
		}
		return elementName + description + " - " + displayName; //$NON-NLS-1$
	}

	/**
	 * Gets the description for the specified element
	 * 
	 * @param handle the handle of the element
	 * 
	 * @return Returns the description
	 */
	abstract protected String getDescription(DesignElementHandle handle);

	/**
	 * Eliminates long description, and changes the length of substring to
	 * <code>DESC_MAX_LENGTH</code>
	 * 
	 * @param longDesc         description needs to change
	 * @param includeQuotation if includes double quotation marks
	 * @return Returns the description
	 */

	protected String getDescription(String longDesc, boolean includeQuotation) {
		if (longDesc == null || longDesc.equals("") //$NON-NLS-1$
				|| longDesc.equals("null")) //$NON-NLS-1$
			longDesc = ""; //$NON-NLS-1$
		else {
			if (longDesc.length() > DESC_MAX_LENGTH) {
				longDesc = longDesc.substring(0, DESC_MAX_LENGTH) + SUSPENSION_POINTS;
			}
			if (includeQuotation) {
				longDesc = ": \"" + longDesc + "\""; //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				longDesc = "(" + longDesc + ")";//$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return longDesc;
	}

}
