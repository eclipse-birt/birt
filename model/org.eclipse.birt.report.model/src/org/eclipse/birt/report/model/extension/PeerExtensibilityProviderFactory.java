/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.extension;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Factory class to create a PeerExtensibilityProvider.
 */

public class PeerExtensibilityProviderFactory {

	/**
	 * Returns the PeerExtensibilityProvider based on the element and the extension
	 * Id.
	 * 
	 * @param element     the extended element.
	 * @param extensionID The extension Id used to create the corresponding extended
	 *                    element definition.
	 * @return the PeerExtensibilityProvider instance.
	 */
	public static PeerExtensibilityProvider createProvider(DesignElement element, String extensionID) {
		if (extensionID == null)
			return new DummyPeerExtensibilityProvider(element, null);
		if (MetaDataDictionary.getInstance().getElement(extensionID) != null)
			return new SimplePeerExtensibilityProvider(element, extensionID);
		return new DummyPeerExtensibilityProvider(element, extensionID);
	}
}
