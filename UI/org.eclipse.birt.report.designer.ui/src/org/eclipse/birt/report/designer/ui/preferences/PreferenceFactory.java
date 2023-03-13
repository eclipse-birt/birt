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

package org.eclipse.birt.report.designer.ui.preferences;

import java.util.HashMap;

import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class PreferenceFactory implements IPropertyChangeListener {

	private static PreferenceFactory instance = null;

	private PreferenceFactory() {
	}

	public static synchronized PreferenceFactory getInstance() {
		if (instance == null) {
			instance = new PreferenceFactory();
		}
		return instance;
	}

	private HashMap preferenceMap = new HashMap();

	public IPreferences getPreferences(AbstractUIPlugin plugin) {
		return getPreferences(plugin, null);
	}

	public IPreferences getPluginPreferences(String pluginId, IProject project) {
		IReportPreferenceFactory preference = (IReportPreferenceFactory) ElementAdapterManager
				.getAdapter(ReportPlugin.getDefault(), IReportPreferenceFactory.class);

		if (preference == null || project == null) {
			if (preferenceMap.containsKey(pluginId)) {
				return (PreferenceWrapper) preferenceMap.get(pluginId);
			}
		} else {
			String id = pluginId.concat("/").concat(project.getName()); //$NON-NLS-1$
			if (preferenceMap.containsKey(id)) {
				return (PreferenceWrapper) preferenceMap.get(id);
			}
		}
		return null;
	}

	public IPreferences getPreferences(AbstractUIPlugin plugin, IProject project) {
		String pluginId = plugin.getBundle().getSymbolicName();

		IReportPreferenceFactory preference = (IReportPreferenceFactory) ElementAdapterManager.getAdapter(plugin,
				IReportPreferenceFactory.class);

		PreferenceWrapper wrapper = null;
		if (preference == null || project == null) {
			if (preferenceMap.containsKey(pluginId)) {
				return (PreferenceWrapper) preferenceMap.get(pluginId);
			}
			wrapper = new PreferenceWrapper(plugin.getPreferenceStore());
			wrapper.getPrefsStore().addPropertyChangeListener(this);
			preferenceMap.put(pluginId, wrapper);
		} else {
			String id = pluginId.concat("/").concat(project.getName()); //$NON-NLS-1$
			if (preferenceMap.containsKey(id)) {
				return (PreferenceWrapper) preferenceMap.get(id);
			}
			wrapper = new PreferenceWrapper(preference, project, plugin.getPreferenceStore());
			wrapper.getPrefsStore().addPropertyChangeListener(this);
			preferenceMap.put(id, wrapper);
		}
		return wrapper;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		PreferenceWrapper[] prefs = (PreferenceWrapper[]) preferenceMap.values().toArray(new PreferenceWrapper[0]);
		for (int i = 0; i < prefs.length; i++) {
			if (prefs[i].getPrefsStore() == event.getSource()) {
				prefs[i].firePreferenceChangeEvent(event.getProperty(), event.getOldValue(), event.getNewValue());
			}
		}
	}
}
