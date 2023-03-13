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

package org.eclipse.birt.report.model.extension;

import org.eclipse.birt.report.model.elements.ElementsTestUtil;
import org.eclipse.birt.report.model.elements.ExtendedItem;

/**
 * Class that provides util methods for extension.
 */
public class ExtensionTestUtil {

	/**
	 * Gets the extension value stored in the hash-map of extended-item.
	 *
	 * @param element
	 * @param propName
	 * @return
	 */
	public static Object getLocalExtensionMapValue(ExtendedItem element, String propName) {
		PeerExtensibilityProvider provider = ElementsTestUtil.getProvider(element);
		return provider == null ? null : provider.extensionPropValues.get(propName);
	}
}
