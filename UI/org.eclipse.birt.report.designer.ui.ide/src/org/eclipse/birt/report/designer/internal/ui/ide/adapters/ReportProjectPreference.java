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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Preferences;

public class ReportProjectPreference extends Preferences {

	protected static final String ENABLE_SPECIAL_SETTINGS = "Enable Special Settings"; //$NON-NLS-1$
	protected static final String DEFAULT_PREFERENCES_DIRNAME = ".settings"; //$NON-NLS-1$
	public static final String PREFS_FILE_EXTENSION = "prefs"; //$NON-NLS-1$
	private IProject project;
	private String pluginId;

	public ReportProjectPreference(String pluginId, IProject project) {
		this.project = project;
		this.pluginId = pluginId;
		load();
	}

	protected IPath getLocation() {
		IPath location = project.getLocation();
		return location == null ? null
				: location.append(DEFAULT_PREFERENCES_DIRNAME).append(pluginId).addFileExtension(PREFS_FILE_EXTENSION);
	}

	protected boolean checkSettingLocation() {
		IPath location = project.getLocation();
		location = location == null ? null : location.append(DEFAULT_PREFERENCES_DIRNAME);
		if (location == null)
			return false;
		else {
			File setting = location.toFile();
			if (setting.exists() && setting.isFile())
				setting.delete();
			if (!setting.exists())
				setting.mkdir();
			if (setting.isDirectory() && setting.isDirectory())
				return true;
			else
				return false;
		}
	}

	public boolean delete() {
		File file = getLocation().toFile();
		if (file.exists())
			return file.delete();
		return true;
	}

	public void load() {
		if (getLocation() != null) {
			File file = getLocation().toFile();
			if (file.exists()) {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
					load(fis);

				} catch (Exception e) {
					ExceptionUtil.handle(e);
				} finally {
					try {
						if (fis != null)
							fis.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	public boolean save() {
		if (this.propertyNames() == null || this.propertyNames().length == 0)
			return delete();
		boolean flag = false;
		if (getLocation() != null && checkSettingLocation()) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(getLocation().toFile());
				store(fos, null);
				flag = true;
			} catch (Exception e) {
				ExceptionUtil.handle(e);
				flag = false;
			} finally {
				try {
					if (fos != null)
						fos.close();
				} catch (IOException e) {
				}
			}
		}
		return flag;
	}

	public boolean hasSpecialSettings() {
		return this.getBoolean(ENABLE_SPECIAL_SETTINGS);
	}

	public void setEnableSpecialSettings(boolean enable) {
		setValue(ENABLE_SPECIAL_SETTINGS, enable);
	}

}
