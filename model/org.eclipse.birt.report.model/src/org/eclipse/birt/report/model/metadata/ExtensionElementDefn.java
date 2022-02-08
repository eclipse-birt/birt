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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;

/**
 * Represents the extension element definition based on Model extension point.
 * This class only used for those extension definition from third-party, not the
 * Model-defined standard elements.
 * 
 * <h3>Property Visibility</h3> All extension element definition support
 * property visibility, which defines to something like read-only, or hide. This
 * is used to help UI display the property value or the entire property. When
 * extension element defines the visibility for a Model-defined property, the
 * property definition will be copied and overridden in this extension element
 * definition.
 */

public abstract class ExtensionElementDefn extends ElementDefn {

	/**
	 * The extension point that this extension definition extended from.
	 */

	protected String extensionPoint = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ElementDefn#build()
	 */

	protected void build() throws MetaDataException {
		if (isBuilt)
			return;

		buildDefn();

		// Cache data for properties defined here. Note, done here so
		// we don't repeat the work for any style properties copied below.

		buildProperties();

		buildPropertiesVisibility();

		buildContainerProperties();

		buildXmlName();

		// build slot
		buildSlots();

		// build validation trigger
		buildTriggerDefnSet();

		// if name is not defined, the set the name options
		if (cachedProperties.get(IDesignElementModel.NAME_PROP) == null) {
			nameConfig.nameOption = MetaDataConstants.NO_NAME;
			nameConfig.nameSpaceID = MetaDataConstants.NO_NAME_SPACE;
			nameConfig.holder = null;
		}

		isBuilt = true;
	}

	protected void buildXmlName() {

	}

	/**
	 * Checks whether the property has the mask defined by the peer extension given
	 * the property name.
	 * 
	 * @param propName the property name to check
	 * @return true if the style masks defined by peer extension of the item is
	 *         found, otherwise false
	 */

	public boolean isMasked(String propName) {
		// TODO: the mask for style property is not supported now.

		return false;
	}

	/**
	 * Gets the extension point of this extension element.
	 * 
	 * @return the extension point of this extension element
	 */

	public String getExtensionPoint() {
		return this.extensionPoint;
	}
}
