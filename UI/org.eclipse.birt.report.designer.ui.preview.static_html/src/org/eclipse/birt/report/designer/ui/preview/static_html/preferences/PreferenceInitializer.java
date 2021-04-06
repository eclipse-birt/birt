
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
