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

import java.io.IOException;

import org.eclipse.birt.core.preference.IPreferenceChangeListener;
import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.core.preference.PreferenceChangeEvent;
import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.SafeRunnable;

public class PreferenceWrapper extends EventManager implements IPreferences {

	private final static int GLOBAL_TYPE = 0;
	private final static int SPECIAL_TYPE = 1;
	private int preferenceType = GLOBAL_TYPE;
	private IReportPreferenceFactory prefs = null;
	private IPreferenceStore prefsStore = null;
	private IProject project = null;

	public PreferenceWrapper(IReportPreferenceFactory prefs, IProject project, IPreferenceStore prefsStore) {
		preferenceType = SPECIAL_TYPE;
		this.prefs = prefs;
		this.prefsStore = prefsStore;
		this.project = project;
	}

	public PreferenceWrapper(IPreferenceStore prefsStore) {
		preferenceType = GLOBAL_TYPE;
		this.prefsStore = prefsStore;
	}

	public boolean getDefaultBoolean(String name) {
		return prefsStore.getDefaultBoolean(name);
	}

	public double getDefaultDouble(String name) {
		return prefsStore.getDefaultDouble(name);
	}

	public float getDefaultFloat(String name) {
		return prefsStore.getDefaultFloat(name);
	}

	public int getDefaultInt(String name) {
		return prefsStore.getDefaultInt(name);
	}

	public long getDefaultLong(String name) {
		return prefsStore.getDefaultLong(name);
	}

	public String getDefaultString(String name) {
		return prefsStore.getDefaultString(name);
	}

	public boolean getBoolean(String name) {
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			if (preference != null && preference.contains(name))
				return preference.getBoolean(name);
		}
		return prefsStore.getBoolean(name);
	}

	public double getDouble(String name) {
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			if (preference != null && preference.contains(name))
				return preference.getDouble(name);
		}
		return prefsStore.getDouble(name);
	}

	public float getFloat(String name) {
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			if (preference != null && preference.contains(name))
				return preference.getFloat(name);
		}
		return prefsStore.getFloat(name);
	}

	public int getInt(String name) {
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			if (preference != null && preference.contains(name))
				return preference.getInt(name);
		}
		return prefsStore.getInt(name);
	}

	public long getLong(String name) {
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			if (preference != null && preference.contains(name))
				return preference.getLong(name);
		}
		return prefsStore.getLong(name);
	}

	public String getString(String name) {
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			if (preference != null && preference.contains(name))
				return preference.getString(name);
		}
		return prefsStore.getString(name);
	}

	public boolean contains(String name) {
		return prefsStore.contains(name);
	}

	public boolean isDefault(String name) {
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			return prefs.hasSpecialSettings(project, name);
		} else
			return prefsStore.isDefault(name);
	}

	public void putValue(String name, String value) {
		prefsStore.putValue(name, value);
	}

	public void setDefault(String name, double value) {
		prefsStore.setDefault(name, value);
	}

	public void setDefault(String name, float value) {
		prefsStore.setDefault(name, value);
	}

	public void setDefault(String name, int value) {
		prefsStore.setDefault(name, value);
	}

	public void setDefault(String name, long value) {
		prefsStore.setDefault(name, value);
	}

	public void setDefault(String name, String defaultObject) {
		prefsStore.setDefault(name, defaultObject);
	}

	public void setDefault(String name, boolean value) {
		prefsStore.setDefault(name, value);
	}

	public void setToDefault(String name) {
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			preference.setToDefault(name);
			firePreferenceChangeEvent(PreferenceChangeEvent.SPECIALTODEFAULT, null, null);
		} else {
			String oldValue = prefsStore.getString(name);
			prefsStore.setToDefault(name);
			firePreferenceChangeEvent(name, oldValue, prefsStore.getDefaultString(name));
		}
	}

	public void setValue(String name, double value) {
		double oldValue = getDouble(name);
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			if (preference != null) {
				if (preference.isDefault(name) || oldValue != value) {
					preference.setValue(name, value);
					firePreferenceChangeEvent(name, new Double(oldValue), new Double(value));
				}
				return;
			}
		}
		if (oldValue != value) {
			prefsStore.setValue(name, value);
			firePreferenceChangeEvent(name, new Double(oldValue), new Double(value));
		}
	}

	public void setValue(String name, float value) {
		float oldValue = getFloat(name);
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			if (preference != null) {
				if (preference.isDefault(name) || oldValue != value) {
					preference.setValue(name, value);
					firePreferenceChangeEvent(name, new Float(oldValue), new Float(value));
				}
				return;
			}
		}
		if (oldValue != value) {
			prefsStore.setValue(name, value);
			firePreferenceChangeEvent(name, new Float(oldValue), new Float(value));
		}
	}

	public void setValue(String name, int value) {
		int oldValue = getInt(name);
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			if (preference != null) {
				if (preference.isDefault(name) || oldValue != value) {
					preference.setValue(name, value);
					firePreferenceChangeEvent(name, Integer.valueOf(oldValue), Integer.valueOf(value));
				}
				return;
			}
		}
		if (oldValue != value) {
			prefsStore.setValue(name, value);
			firePreferenceChangeEvent(name, Integer.valueOf(oldValue), Integer.valueOf(value));
		}
	}

	public void setValue(String name, long value) {
		long oldValue = getLong(name);
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			if (preference != null) {
				if (preference.isDefault(name) || oldValue != value) {
					preference.setValue(name, value);
					firePreferenceChangeEvent(name, Long.valueOf(oldValue), Long.valueOf(value));
				}
				return;
			}
		}
		if (oldValue != value) {
			prefsStore.setValue(name, value);
			firePreferenceChangeEvent(name, Long.valueOf(oldValue), Long.valueOf(value));
		}
	}

	public void setValue(String name, String value) {
		String oldValue = getString(name);
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			if (preference != null) {
				if (preference.isDefault(name) || !oldValue.equals(value)) {
					preference.setValue(name, value);
					firePreferenceChangeEvent(name, oldValue, value);
				}
				return;
			}
		}
		if (!oldValue.equals(value)) {
			prefsStore.setValue(name, value);
			firePreferenceChangeEvent(name, oldValue, value);
		}
	}

	public void setValue(String name, boolean value) {
		boolean oldValue = getBoolean(name);
		if (this.preferenceType == SPECIAL_TYPE && project != null) {
			Preferences preference = prefs.getReportPreference(project);
			if (preference != null) {
				if (preference.isDefault(name) || oldValue != value) {
					preference.setValue(name, value);
					firePreferenceChangeEvent(name, Boolean.valueOf(oldValue), Boolean.valueOf(value));
				}
				return;
			}
		}
		if (oldValue != value) {
			prefsStore.setValue(name, value);
			firePreferenceChangeEvent(name, Boolean.valueOf(oldValue), Boolean.valueOf(value));
		}
	}

	public void save() throws IOException {
		if (this.preferenceType == SPECIAL_TYPE && project != null)
			prefs.saveReportPreference(project);
		else if (prefsStore instanceof IPersistentPreferenceStore)
			((IPersistentPreferenceStore) prefsStore).save();

	}

	public void addPreferenceChangeListener(IPreferenceChangeListener pcl) {
		this.addListenerObject(pcl);
	}

	public void removePreferenceChangeListener(IPreferenceChangeListener pcl) {
		this.removeListenerObject(pcl);
	}

	public void firePreferenceChangeEvent(String name, Object oldValue, Object newValue) {
		final Object[] finalListeners = getListeners();
		// Do we need to fire an event.
		if (finalListeners.length > 0 && (oldValue == null || !oldValue.equals(newValue))) {
			final PreferenceChangeEvent pe = new PreferenceChangeEvent(this, name, oldValue, newValue);
			for (int i = 0; i < finalListeners.length; ++i) {
				final IPreferenceChangeListener l = (IPreferenceChangeListener) finalListeners[i];
				SafeRunnable.run(new SafeRunnable(JFaceResources.getString("PreferenceStore.changeError")) { //$NON-NLS-1$

					public void run() {
						l.preferenceChange(pe);
					}
				});
			}
		}
	}

	public IPreferenceStore getPrefsStore() {
		return prefsStore;
	}

}
