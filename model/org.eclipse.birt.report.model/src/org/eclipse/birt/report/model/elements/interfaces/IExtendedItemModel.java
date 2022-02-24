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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for ExtendedItem to store the constants.
 */

public interface IExtendedItemModel {

	/**
	 * The property name of alt text.
	 */

	public static final String ALT_TEXT_PROP = "altText";//$NON-NLS-1$

	/**
	 * The property name of alt text id.
	 */

	public static final String ALT_TEXT_KEY_PROP = "altTextID";//$NON-NLS-1$

	/**
	 * Name of the property that identifies the name of the extension. BIRT uses the
	 * property to find extension definition in our meta-data dictionary.
	 */

	public static final String EXTENSION_NAME_PROP = "extensionName"; //$NON-NLS-1$

	/**
	 * The property name of the filters to apply to the extended item.
	 */

	public static final String FILTER_PROP = "filter"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the version of the extended element model.
	 */
	String EXTENSION_VERSION_PROP = "extensionVersion"; //$NON-NLS-1$
}
