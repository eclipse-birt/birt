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

package org.eclipse.birt.report.engine.api;

import java.io.File;

public class RunAndRenderApp {

	protected void run(String design) {
		File file = new File(design);
		String name = file.getName();
		if (name.endsWith(".rptdesign")) {
			name = name.substring(0, name.length() - 10);
		}
		String parent = file.getParentFile().getAbsolutePath();
		String outputFolder = parent + File.separatorChar + name;

		// output to the folder with the same name.
		String document = outputFolder + File.separatorChar + name + ".zip";

		int result = new ReportRunner(new String[] { "-o", outputFolder, design }).execute();
		if (result != -1) {
			result = new ReportRunner(new String[] { "-m", "run", "-o", document, design }).execute();
			if (result != -1) {
				result = new ReportRunner(new String[] { "-m", "render", "-o", outputFolder, document }).execute();
				return;
			}
		}
		System.out.print("execute " + design + " failed");

	}

	static public void main(String[] args) {
		if (args.length == 0) {
			System.out.println("RunAndRenderApp <design file | design folder>");
			return;
		}
		RunAndRenderApp app = new RunAndRenderApp();
		File file = new File(args[0]);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile()) {
						app.run(files[i].getAbsolutePath());
					}
				}
			} else {
				app.run(file.getAbsolutePath());
			}
		}
	}
}
