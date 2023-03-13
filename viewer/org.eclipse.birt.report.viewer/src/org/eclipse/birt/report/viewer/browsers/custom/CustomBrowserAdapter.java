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

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.help.browser.IBrowser;
import org.eclipse.osgi.service.environment.Constants;

/**
 * Custom browser implementation. The original implementation is from HELP.
 * <p>
 */
public class CustomBrowserAdapter implements IBrowser {
	/**
	 * Preference key for custom browser path
	 */
	public static final String CUSTOM_BROWSER_PATH_KEY = "custom_browser_path"; //$NON-NLS-1$

	/**
	 * Close browser.
	 */
	@Override
	public void close() {
		// Do nothing
	}

	/**
	 * Is browser supports close operation.
	 *
	 * @return browser supports close operation or not
	 */
	@Override
	public boolean isCloseSupported() {
		return false;
	}

	/**
	 * Display arbitary url.
	 *
	 * @param url
	 * @exception Exception
	 */
	@Override
	public void displayURL(String url) throws Exception {
		String path = ViewerPlugin.getDefault().getPluginPreferences()
				.getString(CustomBrowserAdapter.CUSTOM_BROWSER_PATH_KEY);

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
	 * Is setting browser window location supported.
	 *
	 * @return support setting browser window location or not
	 */
	@Override
	public boolean isSetLocationSupported() {
		return false;
	}

	/**
	 * Is setting browser window size supported.
	 *
	 * @return support setting browser window size or not
	 */
	@Override
	public boolean isSetSizeSupported() {
		return false;
	}

	/**
	 * Set browser window location.
	 *
	 * @param x X coordinate of browser window's top-left cornor
	 * @param y Y coordinate of browser window's top-left cornor
	 */
	@Override
	public void setLocation(int x, int y) {
		// Do nothing
	}

	/**
	 * Set browser window size.
	 *
	 * @param width  browser window width
	 * @param height browser window height
	 */
	@Override
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
