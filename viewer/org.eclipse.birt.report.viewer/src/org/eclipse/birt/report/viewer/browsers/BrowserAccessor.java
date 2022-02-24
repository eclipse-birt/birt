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

package org.eclipse.birt.report.viewer.browsers;

import java.net.URL;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.help.browser.IBrowser;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

/**
 * Static accessor class for available borwsers.
 * <p>
 */
public class BrowserAccessor {

	private static IBrowser browser;

	private static IBrowser internalBrowser;

	/**
	 * Get current preview browser.
	 * 
	 * @param forceExternal forece using external browser or not
	 * @return browser instance
	 */
	public static synchronized IBrowser getPreviewBrowser(boolean forceExternal) {
		if (!forceExternal && BrowserManager.getInstance().isEmbeddedBrowserPresent()) {
			if (internalBrowser == null) {
				internalBrowser = BrowserManager.getInstance().createBrowser(false);
			}

			return internalBrowser;
		}

		if (browser == null) {
			// use workbench browser support first, orginal custom browser is
			// deprecated.
			browser = new ExternalWorkbenchBrowser();
		}

		if (browser == null) {
			browser = BrowserManager.getInstance().createBrowser(true);
		}

		return browser;
	}

	/**
	 * ExternalWorkbenchBrowser
	 */
	static class ExternalWorkbenchBrowser implements IBrowser {

		private IWebBrowser browser;

		ExternalWorkbenchBrowser() {
		}

		private IWebBrowser getExternalBrowser() throws PartInitException {
			IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
			return support.getExternalBrowser();
		}

		public void close() {
			if (browser != null) {
				browser.close();
			}
		}

		public boolean isCloseSupported() {
			return true;
		}

		public void displayURL(String url) throws Exception {
			try {
				browser = getExternalBrowser();
				if (browser != null) {
					browser.openURL(new URL(url));
				}
			} catch (PartInitException pie) {
				ViewerPlugin.logError(pie.getLocalizedMessage(), pie);
			}
		}

		public boolean isSetLocationSupported() {
			return false;
		}

		public boolean isSetSizeSupported() {
			return false;
		}

		public void setLocation(int x, int y) {
		}

		public void setSize(int width, int height) {
		}
	}
}
