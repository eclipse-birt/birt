/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.ui.launcher.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TreeSet;

import org.eclipse.birt.report.debug.internal.ui.launcher.IReportLauncherSettings;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.ModelEntry;
import org.eclipse.pde.internal.core.PDECore;

import com.ibm.icu.util.StringTokenizer;

/**
 * ReportLauncherUtils
 *
 * @deprecated
 */
public class ReportLauncherUtils {

//	public static HashMap getAutoStartPlugins(ILaunchConfiguration config) {
//		boolean useDefault = true;
//		String customAutoStart = ""; //$NON-NLS-1$
//		try {
//			useDefault = config.getAttribute(IReportLauncherSettings.CONFIG_USE_DEFAULT, true);
//			customAutoStart = config.getAttribute(IReportLauncherSettings.CONFIG_AUTO_START, ""); //$NON-NLS-1$
//		} catch (CoreException e) {
//		}
//		return getAutoStartPlugins(useDefault, customAutoStart);
//	}

//	public static IPath getEclipseHome() {
//		Preferences preferences = PDECore.getDefault().getPluginPreferences();
//		return new Path(preferences.getString(ICoreConstants.PLATFORM_PATH));
//	}

//	public static HashMap getAutoStartPlugins(boolean useDefault, String customAutoStart) {
//		HashMap list = new HashMap();
//		if (true)
//		// !PDECore.getDefault( ).getModelManager( ).isOSGiRuntime( ) )
//		{
//			list.put("org.eclipse.core.boot", Integer.valueOf(0)); //$NON-NLS-1$
//		} else {
//			String bundles = null;
//			if (useDefault) {
//				Properties prop = getConfigIniProperties(getEclipseHome().toOSString(), "configuration/config.ini"); //$NON-NLS-1$
//				if (prop != null)
//					bundles = prop.getProperty("osgi.bundles"); //$NON-NLS-1$
//				if (prop == null || bundles == null) {
//					String path = getOSGiPath();
//					if (path != null)
//						prop = getConfigIniProperties(path, "eclipse.properties"); //$NON-NLS-1$
//					if (prop != null)
//						bundles = prop.getProperty("osgi.bundles"); //$NON-NLS-1$
//				}
//			} else {
//				bundles = customAutoStart;
//			}
//			if (bundles != null) {
//				StringTokenizer tokenizer = new StringTokenizer(bundles, ","); //$NON-NLS-1$
//				while (tokenizer.hasMoreTokens()) {
//					String token = tokenizer.nextToken().trim();
//					int index = token.indexOf('@');
//					if (index == -1 || index == token.length() - 1)
//						continue;
//					String start = token.substring(index + 1);
//					if (start.indexOf("start") != -1 || !useDefault) { //$NON-NLS-1$
//						Integer level = index != -1 ? getStartLevel(start) : new Integer(-1);
//						list.put(index != -1 ? token.substring(0, token.indexOf('@')) : token, level);
//					}
//				}
//			}
//		}
//		return list;
//	}

	public static Properties getConfigIniProperties(String directory, String filename) {
		File iniFile = new File(directory, filename);
		if (!iniFile.exists())
			return null;
		Properties pini = new Properties();
		try {
			FileInputStream fis = new FileInputStream(iniFile);
			pini.load(fis);
			fis.close();
			return pini;
		} catch (IOException e) {
		}
		return null;
	}

	private static String getOSGiPath() {
		ModelEntry entry = PDECore.getDefault().getModelManager().findEntry("org.eclipse.osgi"); //$NON-NLS-1$
		if (entry != null && entry.getActiveModels().length > 0) {
			IPluginModelBase model = entry.getActiveModels()[0];
			if (model.getUnderlyingResource() != null) {
				return model.getUnderlyingResource().getLocation().removeLastSegments(2).toOSString();
			}
			return model.getInstallLocation();
		}
		return null;
	}

	private static Integer getStartLevel(String text) {
		StringTokenizer tok = new StringTokenizer(text, ":"); //$NON-NLS-1$
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken().trim();
			try {
				return new Integer(token);
			} catch (NumberFormatException e) {
			}
		}
		return Integer.valueOf(-1);
	}

//	public static String getPrimaryFeatureId() {
//		boolean isOSGi = true;
//		// PDECore.getDefault( ).getModelManager( ).isOSGiRuntime( );
//		String filename = isOSGi ? "configuration/config.ini" : "install.ini"; //$NON-NLS-1$ //$NON-NLS-2$
//		Properties properties = getConfigIniProperties(getEclipseHome().toOSString(), filename);
//
//		String property = isOSGi ? "eclipse.product" : "feature.default.id"; //$NON-NLS-1$ //$NON-NLS-2$
//		return (properties == null) ? null : properties.getProperty(property);
//	}

	public static File createConfigArea(String name) {
		IPath statePath = PDECore.getDefault().getStateLocation();
		File dir = new File(statePath.toOSString());
		if (name.length() > 0) {
			dir = new File(dir, name);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
		return dir;
	}

	public static TreeSet parseDeselectedWSIds(ILaunchConfiguration config) throws CoreException {
		TreeSet deselected = new TreeSet();
		String ids = config.getAttribute(IReportLauncherSettings.IMPORTPROJECT, (String) null);
		if (ids != null && ids.length() > 0) {
			StringTokenizer token = new StringTokenizer(ids, ";"); //$NON-NLS-1$
			while (token.hasMoreTokens()) {
				String str = token.nextToken();
				int index = str.lastIndexOf(File.separator);
				if (index > 0) {
					str = str.substring(index + 1);
				}
				deselected.add(str);
			}
		}
		return deselected;
	}

	public static TreeSet parseDeselectedOpenFileNames(ILaunchConfiguration config) throws CoreException {
		TreeSet deselected = new TreeSet();
		String ids = config.getAttribute(IReportLauncherSettings.OPENFILENAMES, (String) null);
		if (ids != null && ids.length() > 0) {
			StringTokenizer token = new StringTokenizer(ids, ";"); //$NON-NLS-1$
			while (token.hasMoreTokens()) {
				String str = token.nextToken();
				int index = str.lastIndexOf(File.separator);
				if (index > 0) {
					str = str.substring(index + 1);
				}
				deselected.add(str);
			}
		}
		return deselected;
	}

	public static TreeSet parseDeselectedClassIds(ILaunchConfiguration config) throws CoreException {
		TreeSet deselected = new TreeSet();
		String ids = config.getAttribute(IReportLauncherSettings.IMPORTPROJECTNAMES, (String) null);
		if (ids != null && ids.length() > 0) {
			StringTokenizer token = new StringTokenizer(ids, ";"); //$NON-NLS-1$
			while (token.hasMoreTokens()) {
				String str = token.nextToken();
				int index = str.lastIndexOf(File.separator);
				if (index > 0) {
					str = str.substring(index + 1);
				}
				deselected.add(str);
			}
		}
		return deselected;
	}

	public static IVMInstall getVMInstall(String name) {
		if (name != null) {
			IVMInstall[] installs = getAllVMInstances();
			for (int i = 0; i < installs.length; i++) {
				if (installs[i].getName().equals(name))
					return installs[i];
			}
		}
		return JavaRuntime.getDefaultVMInstall();
	}

	public static IVMInstall[] getAllVMInstances() {
		ArrayList res = new ArrayList();
		IVMInstallType[] types = JavaRuntime.getVMInstallTypes();
		for (int i = 0; i < types.length; i++) {
			IVMInstall[] installs = types[i].getVMInstalls();
			for (int k = 0; k < installs.length; k++) {
				res.add(installs[k]);
			}
		}
		return (IVMInstall[]) res.toArray(new IVMInstall[res.size()]);
	}
}