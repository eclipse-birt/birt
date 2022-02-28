/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
 *
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer.testutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

/**
 * A singleton utility to help with testing functionalities by checking the log
 * patterns in a trace log file.
 */
public class TraceLogTesterUtil {
	private static TraceLogTesterUtil sm_instance = null;

	public static TraceLogTesterUtil getInstance() {
		if (sm_instance == null) {
			sm_instance = new TraceLogTesterUtil();
		}
		return sm_instance;
	}

	private TraceLogTesterUtil() {
	}

	String findInstallationDirectory(String dataSourceId) {
		final String extensionPoint = "org.eclipse.datatools.connectivity.oda.dataSource";
		return findInstallationDirectory(dataSourceId, extensionPoint);
	}

	String findInstallationDirectory(String dataSourceId, String extensionPoint) {
		URL url;
		try {
			ExtensionManifest manifest = ManifestExplorer.getInstance().getExtensionManifest(dataSourceId,
					extensionPoint);
			url = manifest.getDriverLocation();
		} catch (Exception e) {
			// couldn't find an installation directory, so use current directory
			return "./";
		}

		return (url != null) ? url.getPath() : "./";
	}

	/*
	 * Given a file name, search given patterns in sequence, assuming each pattern
	 * line begins and ends with log context info.
	 */
	public boolean matchLogPatternsInFile(String fileName, String[] patterns) throws IOException {
		BufferedReader fileBufReader = new BufferedReader(new FileReader(fileName));
		String line;
		boolean foundMatch = false;

		for (int i = 0; i < patterns.length; i++) {
			String logPatternStr = "[0-9]+\t[^\t]+\t\t" + patterns[i] + "[^\t]+";
			Pattern searchPattern = Pattern.compile(logPatternStr);
			foundMatch = false;
			while (!foundMatch && (line = fileBufReader.readLine()) != null) {
				foundMatch = searchPattern.matcher(line).matches();
			}

			if (!foundMatch) {
				fileBufReader.close();
				return false; // end of file, no match to current pattern
			}
		}

		fileBufReader.close();
		return foundMatch;
	}

	public void clearDirectory(File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			return; // nothing to clear
		}

		for (int i = 0; i < files.length; i += 1) {
			boolean deleted = files[i].delete();
			if (!deleted) {
				throw new IOException("Cannot delete file: " + files[i].getName());
			}
		}
	}

}
