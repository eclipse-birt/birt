/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Renamer {

	Renamer() throws IOException {
		start();
	}

	public void start() throws IOException {
		String path = this.getClassFolder();
		File packDir = new File(path);
		File[] regressionClasses = packDir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile() && pathname.getName().startsWith("Regression_")) {
					return true;
				}
				return false;
			}
		});

		for (int i = 0; i < regressionClasses.length; i++) {
			System.out.println(regressionClasses[i].getName());
			this.rename(regressionClasses[i]);
		}

		this.rename(regressionClasses[0]);
	}

	protected String getClassFolder() {

		String pathBase = null;

		ProtectionDomain domain = this.getClass().getProtectionDomain();
		if (domain != null) {
			CodeSource source = domain.getCodeSource();
			if (source != null) {
				URL url = source.getLocation();
				pathBase = url.getPath();

				if (pathBase.endsWith("bin/")) { //$NON-NLS-1$
					pathBase = pathBase.substring(0, pathBase.length() - 4);
				}
				if (pathBase.endsWith("bin")) { //$NON-NLS-1$
					pathBase = pathBase.substring(0, pathBase.length() - 3);
				}
			}
		}

		pathBase = pathBase + "src/";
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf("."); //$NON-NLS-1$
		className = className.substring(0, lastDotIndex);
		className = pathBase + className.replace('.', '/');

		return className;
	}

	boolean rename(File javaFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(javaFile));

		StringBuilder sb = new StringBuilder();
		Pattern pattern = Pattern.compile(".*test_Regression_[\\d]+.*"); //$NON-NLS-1$

		String line = null;
		while ((line = br.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (!matcher.matches()) {
				sb.append(line);
				sb.append('\n');
			} else {
				String fileName = javaFile.getName();

				String caseNo = fileName.substring(fileName.indexOf('_') + 1, fileName.indexOf('.'));

				int start = line.indexOf("test_Regression_");
				int end = line.indexOf('(', start + "test_Regression_".length());
				System.out.println(line.substring(start, end));

				String newLine = line.substring(0, start) + "test_regression_" + caseNo + line.substring(end);
				System.out.println(newLine);

				sb.append(newLine);
				sb.append('\n');
			}
		}

		br.close();

		PrintWriter writer = new PrintWriter(new FileWriter(javaFile));
		writer.print(sb.toString());
		writer.flush();
		writer.close();

		return true;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new Renamer().start();

	}

}
