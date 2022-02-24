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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.util.HashMap;

import org.eclipse.birt.report.designer.ui.preferences.IReportPreferenceFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Preferences;

public class IDEReportPreferenceFactory implements IReportPreferenceFactory {

	private String pluginId = ""; //$NON-NLS-1$

	public IDEReportPreferenceFactory(Plugin plugin) {
		this.pluginId = plugin.getBundle().getSymbolicName();
	}

	protected HashMap prefsMap = new HashMap();

	public boolean hasSpecialSettings(Object adaptable, String name) {
		IProject project = getProject(adaptable);
		if (project == null)
			return false;
		else {
			Preferences preference = getReportPreference(adaptable);
			if (preference != null)
				return !preference.isDefault(name);
			else
				return false;
		}
	}

	public Preferences getReportPreference(Object adaptable) {
		IProject project = getProject(adaptable);
		if (project == null)
			return null;
		else if (!containsReportPreference(adaptable)) {
			ReportProjectPreference prefs = new ReportProjectPreference(pluginId, project);
			prefsMap.put(project.getFullPath().toOSString(), prefs);
		}
		return (ReportProjectPreference) prefsMap.get(project.getFullPath().toOSString());
	}

	public boolean containsReportPreference(Object adaptable) {
		IProject project = getProject(adaptable);
		if (project == null)
			return false;
		return prefsMap.containsKey(project.getFullPath().toOSString());
	}

	public boolean saveReportPreference(Object adaptable) {
		ReportProjectPreference prefs = (ReportProjectPreference) getReportPreference(adaptable);
		if (prefs == null)
			return true;
		else
			return prefs.save();
	}

	public boolean removeReportPreference(Object adaptable) {
		if (containsReportPreference(adaptable)) {
			IProject project = getProject(adaptable);
			ReportProjectPreference prefs = (ReportProjectPreference) prefsMap.get(project.getFullPath().toOSString());
			prefsMap.remove(project.getFullPath().toOSString());
			return prefs.delete();
		}
		return true;
	}

	private IProject getProject(Object adaptable) {
		if (adaptable instanceof IProject && adaptable != null)
			return (IProject) adaptable;
		else
			return null;
	}
}
