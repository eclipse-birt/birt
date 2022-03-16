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

package org.eclipse.birt.report.model.extension;

import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Represents the the extensibility provider which supports Add-On extension.
 * The add-on extension means the third-party can provide the extension property
 * definition for one extendable element in extension definition file. But the
 * element definition can not be modified by third-party. The extension property
 * is processed by Model as same as those defined in Model. The third-party can
 * also define the I18N message file to provide the localized display name for
 * its extension property.
 */

public class AddOnExtensibilityProvider extends ModelExtensibilityProvider {

	/**
	 * Constructs the add-on extensibility provider with the extendable element and
	 * the extension name.
	 *
	 * @param element       the extendable element
	 * @param extensionName the extension name
	 */

	public AddOnExtensibilityProvider(DesignElement element, String extensionName) {
		super(element, extensionName);
	}
}
