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

package org.eclipse.birt.report.model.plugin;

import org.eclipse.datatools.connectivity.oda.util.manifest.DataSetType;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

/**
 * The utility class for get ODA extension.
 */

class ODAManifestUtil {

	/**
	 * Returns the extensin for ODA data source with the given extension ID. If the
	 * extension is not found, runtime exception will be thrown because the
	 * following cannot know how to handle it.
	 * 
	 * @param extensionID ID of the extension
	 * @return the extension for ODA data source
	 */

	public static ExtensionManifest getDataSourceExtension(String extensionID) {
		ExtensionManifest manifest = null;

		try {
			manifest = ManifestExplorer.getInstance().getExtensionManifest(extensionID);
		} catch (Exception e) {
			// Do nothing.
		}

		return manifest;
	}

	/**
	 * Returns the extensin for ODA data set type with the given extension ID.
	 * 
	 * @param extensionID ID of the extension
	 * @return the extension for ODA data set type.
	 */

	public static DataSetType getDataSetExtension(String extensionID) {
		if (extensionID == null)
			return null;

		ExtensionManifest[] extensions = ManifestExplorer.getInstance().getExtensionManifests();

		for (int i = 0; i < extensions.length; i++) {
			DataSetType[] types = extensions[i].getDataSetTypes();
			for (int j = 0; j < types.length; j++) {
				if (types[j].getID().equals(extensionID))
					return types[j];
			}
		}

		return null;
	}

}
