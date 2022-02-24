/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.tools.ant.BuildException;

/**
 * 
 * @author Farrah
 * 
 *         Generate error/failure summary according into
 *         unitTestReport.properties Properties format
 *         pluginId.failure/error=<count> eg:
 *         org.eclipse.birt.tests.data.engine.AllTests.failure=1
 *         org.eclipse.birt.tests.data.engine.AllTests.error=0
 */
public class ManifestTemplateGenerator {

	public ManifestTemplateGenerator() {

	}

	public static void main(String[] args) {

		File manifestHead = new File(args[0]);
		File manifestBody = new File(args[1]);
		File mainfestRslt = new File(args[2]);
		//
		String suffix = ".new";
		String manifestBody_proc = args[1] + suffix;
		File fBody_proc = new File(manifestBody_proc);
		if (fBody_proc.exists()) {
			fBody_proc.delete();
		}
		try {
			fBody_proc.createNewFile();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		/* Read MANIFEST template header */
		try {
			FileInputStream f = new FileInputStream(manifestHead);
			int len = f.available();
			byte[] b = new byte[len];
			// int j =
			f.read(b, 0, len);
			f.close();

			String ff = new String(b, 0, len);

			StringBuffer strb = new StringBuffer(ff);
			// strb.append(" ");
			/* Read MANIFEST body and create new manifest.mf */
			ProcFile(manifestBody, fBody_proc);

			FileReader freader = new FileReader(fBody_proc);
			BufferedReader breader = new BufferedReader(freader);
			String myString;
			while ((myString = breader.readLine()) != null) {
				strb.append(myString);
				strb.append("\n");
			}

			int lastDelimer = strb.lastIndexOf(",");

			String tmp = strb.substring(0, lastDelimer);
			String result = new String(tmp.getBytes("UTF-8"));
			result = result + "\n";
			System.out.println(result);

			FileOutputStream Result = new FileOutputStream(mainfestRslt);
			Result.write(result.getBytes("UTF-8"));

			fBody_proc.delete();
			/*
			 * ByteArrayInputStream bin = new ByteArrayInputStream(result.getBytes());
			 * //Manifest mf = new Manifest(bin); //Manifest mf = new Manifest(fInStream);
			 * Map map = new HashMap(); map = mf.getEntries(); Iterator it =
			 * map.keySet().iterator(); while (it.hasNext()) { String id =
			 * (String)it.next(); String value = (String)map.get(id); System.out.println(
			 * "["+id+"]:" + value); }
			 */
			// mf.write(Result);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException("ManifestHead not found!");
		}

	}

	static void ProcFile(File source, File dest) {
		try {
			FileReader freader = new FileReader(source);
			BufferedReader breader = new BufferedReader(freader);
			String sLine;
			StringBuffer sTmp = new StringBuffer("");
			while ((sLine = breader.readLine()) != null) {
				if (sLine.startsWith("#") || sLine.startsWith(" ."))
					continue;
				sTmp.append(sLine + " ");
				sTmp.append("\n");
			}
			int lastDelimer = sTmp.lastIndexOf(",");

			String tmp = sTmp.substring(0, lastDelimer);
			String result = new String(tmp.getBytes("UTF-8"));
			result = result + " " + "\n";

			FileOutputStream Result = new FileOutputStream(dest);
			Result.write(result.getBytes("UTF-8"));

		} catch (FileNotFoundException fex) {
			fex.printStackTrace();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

	}

}
