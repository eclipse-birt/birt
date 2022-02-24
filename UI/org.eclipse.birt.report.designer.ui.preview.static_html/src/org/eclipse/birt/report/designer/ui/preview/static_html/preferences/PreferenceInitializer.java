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

package org.eclipse.birt.report.designer.ui.preview.static_html.preferences;

import java.io.File;

import org.eclipse.birt.report.designer.ui.preview.static_html.StaticHTMLPrviewPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = StaticHTMLPrviewPlugin.getDefault().getPreferenceStore();
		String path = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		if (path.endsWith(File.separator))
			path = path + "BIRT"; //$NON-NLS-1$
		else
			path = path + File.separator + "BIRT"; //$NON-NLS-1$
		File file = new File(path);
		if (!file.exists() || file.isFile())
			file.mkdirs();
		store.setDefault(PreferenceConstants.TEMP_PATH, path);
		store.setDefault(PreferenceConstants.CLEAM_TEMP, false);
	}

}
