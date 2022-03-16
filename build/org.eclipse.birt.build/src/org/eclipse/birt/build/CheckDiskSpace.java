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

package org.eclipse.birt.build;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 *
 * check if the disk space is enough <br>
 * <li><code>path</code> the path will be check
 * <li><code>threshold</code> the threshold of warning, the unit is M byte
 * <li><code>property</code> if available space smaller than threshold, this
 * property will be set.
 * <li><code>value</code> available disk space. <br>
 *
 */
public class CheckDiskSpace extends Task {

	protected long threshold = 200;

	protected String property = "notEnoughSpace"; //$NON-NLS-1$

	protected String path = "."; //$NON-NLS-1$

	protected String value = "availableSpace"; //$NON-NLS-1$

	public void setThreshold(long value) {
		this.threshold = value;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void execute() {
		try {
			long ava = getFreeSpace(path);
			long m = 1024 * 1024;

			log("Available space in path " + path + " is " + ava / m + "M"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

			getProject().setProperty(value, Long.toString(ava / m));
			if (threshold > ava / m) {
				getProject().setProperty(property, "true"); //$NON-NLS-1$
			}
		} catch (Exception ex) {
			log("Error occurred when checking the disk, the path :[" + path //$NON-NLS-1$
					+ "] may be incorrect for the current os", Project.MSG_WARN); //$NON-NLS-1$
			log(ex.getMessage(), Project.MSG_WARN);
		}

	}

	private long getFreeSpaceOnLinux(String path) throws Exception {
		long bytesFree = -1;

		Process p = Runtime.getRuntime().exec("df " + "/" + path); //$NON-NLS-1$ //$NON-NLS-2$
		InputStream reader = new BufferedInputStream(p.getInputStream());
		StringBuilder buffer = new StringBuilder();
		for (;;) {
			int c = reader.read();
			if (c == -1) {
				break;
			}
			buffer.append((char) c);
		}
		String outputText = buffer.toString();
		reader.close();

		// parse the output text for the bytes free info
		StringTokenizer tokenizer = new StringTokenizer(outputText, "\n"); //$NON-NLS-1$
		tokenizer.nextToken();
		if (tokenizer.hasMoreTokens()) {
			String line2 = tokenizer.nextToken();
			StringTokenizer tokenizer2 = new StringTokenizer(line2, " "); //$NON-NLS-1$
			if (tokenizer2.countTokens() >= 4) {
				tokenizer2.nextToken();
				tokenizer2.nextToken();
				tokenizer2.nextToken();
				bytesFree = Long.parseLong(tokenizer2.nextToken());
				return bytesFree * 1024;
			}

			return bytesFree * 1024;
		}

		throw new Exception("Can not read the free space of " + path + " path"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private long getFreeSpace(String path) throws Exception {
		if (System.getProperty("os.name").startsWith("Windows")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			return getFreeSpaceOnWindows(path);
		}
		if (System.getProperty("os.name").startsWith("Linux")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			return getFreeSpaceOnLinux(path);
		}

		throw new UnsupportedOperationException(
				"The method getFreeSpace(String path) has not been implemented for this operating system."); //$NON-NLS-1$
	}

	private long getFreeSpaceOnWindows(String path) throws Exception {
		long bytesFree;

		File script = new File(System.getProperty("java.io.tmpdir"), //$NON-NLS-1$
				"script.bat"); //$NON-NLS-1$
		PrintWriter writer = new PrintWriter(new FileWriter(script, false));
		writer.println("dir \"" + path + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		writer.close();

		// get the output from running the .bat file
		Process p = Runtime.getRuntime().exec(script.getAbsolutePath());
		InputStream reader = new BufferedInputStream(p.getInputStream());
		StringBuilder buffer = new StringBuilder();
		for (;;) {
			int c = reader.read();
			if (c == -1) {
				break;
			}
			buffer.append((char) c);
		}
		String outputText = buffer.toString();
		reader.close();

		StringTokenizer tokenizer = new StringTokenizer(outputText, "\n"); //$NON-NLS-1$
		String line = null;
		while (tokenizer.hasMoreTokens()) {
			line = tokenizer.nextToken().trim();
			// see if line contains the bytes free information

		}
		tokenizer = new StringTokenizer(line, " "); //$NON-NLS-1$
		tokenizer.nextToken();
		tokenizer.nextToken();
		bytesFree = Long.parseLong(tokenizer.nextToken().replace(",", "")); //$NON-NLS-1$//$NON-NLS-2$
		return bytesFree;
	}

}
