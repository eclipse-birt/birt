/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.viewer.browsers.custom;

import java.util.*;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.core.runtime.*;
import org.eclipse.help.browser.*;
import org.eclipse.osgi.service.environment.*;

/**
 * Custom browser implementation. The original implementation is from HELP.
 * <p>
 */
public class CustomBrowser implements IBrowser {
	public static final String CUSTOM_BROWSER_PATH_KEY = "custom_browser_path"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.help.browser.IBrowser#close()
	 */
	public void close() {
		// Do nothing
	}

	/**
	 * @see org.eclipse.help.browser.IBrowser#isCloseSupported()
	 */
	public boolean isCloseSupported() {
		return false;
	}

	/**
	 * @see org.eclipse.help.browser.IBrowser#displayURL(java.lang.String)
	 */
	public void displayURL(String url) throws Exception {
		String path = ViewerPlugin.getDefault().getPluginPreferences().getString(CustomBrowser.CUSTOM_BROWSER_PATH_KEY);

		String[] command = prepareCommand(path, url);

		try {
			Process pr = Runtime.getRuntime().exec(command);
			Thread outConsumer = new StreamConsumer(pr.getInputStream());
			outConsumer.setName("Custom browser adapter output reader"); //$NON-NLS-1$
			outConsumer.start();
			Thread errConsumer = new StreamConsumer(pr.getErrorStream());
			errConsumer.setName("Custom browser adapter error reader"); //$NON-NLS-1$
			errConsumer.start();
		} catch (Exception e) {
			ViewerPlugin.logError(ViewerPlugin.getFormattedResourceString("viewer.browser.customBrowser.errorLaunching", //$NON-NLS-1$
					new Object[] { url, path }), e);
			throw new Exception(ViewerPlugin.getFormattedResourceString("viewer.browser.customBrowser.errorLaunching", //$NON-NLS-1$
					new Object[] { url, path }));
		}
	}

	/**
	 * @see org.eclipse.help.browser.IBrowser#isSetLocationSupported()
	 */
	public boolean isSetLocationSupported() {
		return false;
	}

	/**
	 * @see org.eclipse.help.browser.IBrowser#isSetSizeSupported()
	 */
	public boolean isSetSizeSupported() {
		return false;
	}

	/**
	 * @see org.eclipse.help.browser.IBrowser#setLocation(int, int)
	 */
	public void setLocation(int x, int y) {
		// Do nothing
	}

	/**
	 * @see org.eclipse.help.browser.IBrowser#setSize(int, int)
	 */
	public void setSize(int width, int height) {
		// Do nothing
	}

	/**
	 * Creates the final command to launch.
	 * 
	 * @param path
	 * @param url
	 * @return String[]
	 */
	private String[] prepareCommand(String path, String url) {
		ArrayList tokenList = new ArrayList();
		// Divide along quotation marks
		StringTokenizer qTokenizer = new StringTokenizer(path.trim(), "\"", true); //$NON-NLS-1$
		boolean withinQuotation = false;
		String quotedString = ""; //$NON-NLS-1$
		while (qTokenizer.hasMoreTokens()) {
			String curToken = qTokenizer.nextToken();
			if (curToken.equals("\"")) //$NON-NLS-1$
			{
				if (withinQuotation) {
					if (Constants.OS_WIN32.equalsIgnoreCase(Platform.getOS())) {
						// need to quote URLs on Windows
						tokenList.add("\"" + quotedString + "\""); //$NON-NLS-1$ //$NON-NLS-2$
					} else {
						// qotes prevent launching on Unix 35673
						tokenList.add(quotedString);
					}
				} else {
					quotedString = ""; //$NON-NLS-1$
				}
				withinQuotation = !withinQuotation;
				continue;
			} else if (withinQuotation) {
				quotedString = curToken;
				continue;
			} else {
				// divide unquoted strings along white space
				StringTokenizer parser = new StringTokenizer(curToken.trim());
				while (parser.hasMoreTokens()) {
					tokenList.add(parser.nextToken());
				}
			}
		}
		// substitute %1 by url
		boolean substituted = false;
		for (int i = 0; i < tokenList.size(); i++) {
			String token = (String) tokenList.get(i);
			if ("%1".equals(token)) //$NON-NLS-1$
			{
				tokenList.set(i, url);
				substituted = true;
			} else if ("\"%1\"".equals(token)) //$NON-NLS-1$
			{
				tokenList.set(i, "\"" + url + "\""); //$NON-NLS-1$ //$NON-NLS-2$
				substituted = true;
			}
		}
		// add the url if not substituted already
		if (!substituted) {
			tokenList.add(url);
		}

		String[] command = new String[tokenList.size()];
		tokenList.toArray(command);
		return command;
	}
}
