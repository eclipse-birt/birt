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

package org.eclipse.birt.report.viewer.browsers.embedded;

import org.eclipse.help.browser.IBrowser;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;

/**
 * Embedded web browser. Original implementation is from HELP.
 * <p>
 */
public class EmbeddedBrowserAdapter implements IBrowser {
	private EmbeddedBrowser browser;

	/**
	 * Adapter constructor.
	 */
	public EmbeddedBrowserAdapter() {
		// Do nothing
	}

	/**
	 * Display arbitary url
	 * 
	 * @param url
	 */
	public synchronized void displayURL(final String url) {
		Display defaultDisplay = Display.getDefault();

		if (defaultDisplay == Display.getCurrent()) {
			uiDisplayURL(url);
		} else {
			defaultDisplay.syncExec(new Runnable() {

				public void run() {
					uiDisplayURL(url);
				}
			});
		}
	}

	/**
	 * Must be run on UI thread
	 * 
	 * @param url
	 */
	private void uiDisplayURL(final String url) {
		// Clear sessions
		Browser.clearSessions();

		uiClose();

		getBrowser().displayUrl(url);
	}

	/**
	 * Close browser
	 */
	public void close() {
		Display defaultDisplay = Display.getDefault();

		if (defaultDisplay == Display.getCurrent()) {
			uiClose();
		} else {
			defaultDisplay.syncExec(new Runnable() {

				public void run() {
					uiClose();
				}
			});
		}
	}

	/**
	 * Must be run on UI thread
	 */
	private void uiClose() {
		if (browser != null && !browser.isDisposed()) {
			browser.close();
		}
	}

	private EmbeddedBrowser getBrowser() {
		if (browser == null || browser.isDisposed()) {
			browser = new EmbeddedBrowser();
		}

		return browser;
	}

	/**
	 * Is browser supports close operation.
	 * 
	 * @return browser supports close operation or not
	 */
	public boolean isCloseSupported() {
		return true;
	}

	/**
	 * Is setting browser window location supported.
	 * 
	 * @return setting browser window location or not
	 */
	public boolean isSetLocationSupported() {
		return true;
	}

	/**
	 * Is setting browser window size supported.
	 * 
	 * @return setting browser window size or not
	 */
	public boolean isSetSizeSupported() {
		return true;
	}

	/**
	 * Set browser window location.
	 * 
	 * @param x X coordinate of browser window's top-left corner
	 * @param y Y coordinate of browser window's top-left corner
	 */
	public void setLocation(final int x, final int y) {
		Display defaultDisplay = Display.getDefault();

		if (defaultDisplay == Display.getCurrent()) {
			uiSetLocation(x, y);
		} else {
			defaultDisplay.syncExec(new Runnable() {

				public void run() {
					uiSetLocation(x, y);
				}
			});
		}
	}

	/**
	 * Must be run on UI thread
	 */
	private void uiSetLocation(int x, int y) {
		getBrowser().setLocation(x, y);
	}

	/**
	 * Set browser window size.
	 * 
	 * @param width  browser window width
	 * @param height browser window height
	 */
	public void setSize(final int width, final int height) {
		Display defaultDisplay = Display.getDefault();

		if (defaultDisplay == Display.getCurrent()) {
			uiSetSize(width, height);
		} else {
			defaultDisplay.syncExec(new Runnable() {

				public void run() {
					uiSetSize(width, height);
				}
			});
		}
	}

	/**
	 * Must be run on UI thread
	 */
	private void uiSetSize(int width, int height) {
		getBrowser().setSize(width, height);
	}
}
