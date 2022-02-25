/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.viewer.browsers.custom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.birt.report.viewer.ViewerPlugin;

/**
 * Used to receive output / error output from browser processes The original
 * implementation is from HELP.
 *
 * @version $
 */
public class StreamConsumer extends Thread {
	private BufferedReader bReader;

	private String lastLine;

	/**
	 * Constructor
	 *
	 * @param inputStream output stream of browser process
	 */
	public StreamConsumer(InputStream inputStream) {
		super();

		setDaemon(true);

		bReader = new BufferedReader(new InputStreamReader(inputStream));
	}

	/**
	 * Start the stream consumer thread.
	 */
	@Override
	public void run() {
		try {
			String line;

			while (null != (line = bReader.readLine())) {
				lastLine = line;

				BrowserLog.log(line);
			}

			bReader.close();
		} catch (IOException ioe) {
			ViewerPlugin.logError(ViewerPlugin.getResourceString("viewer.browser.customBrowser.errorReading"), ioe); //$NON-NLS-1$
		}
	}

	/**
	 * Get last line from browser process output stream.
	 *
	 * @return last line obtained or null
	 */
	public String getLastLine() {
		return lastLine;
	}

}
