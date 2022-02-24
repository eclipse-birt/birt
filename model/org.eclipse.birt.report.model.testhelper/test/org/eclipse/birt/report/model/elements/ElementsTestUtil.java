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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.extension.PeerExtensibilityProvider;

/**
 * Class that provides util method for elements.
 */
public class ElementsTestUtil {

	/**
	 * 
	 * @param element
	 * @return
	 */
	public static PeerExtensibilityProvider getProvider(ExtendedItem element) {
		return element == null ? null : element.provider;
	}

}
