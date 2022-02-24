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

package org.eclipse.birt.report.viewer.browsers.system;

import java.net.URL;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.help.browser.IBrowser;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

/**
 * Derived from HELP's SystemBrowserAdapter. System browser only works in win32
 * system.
 * <p>
 */
public class SystemBrowserAdapter implements IBrowser {
	/**
	 * Adapter constructor.
	 */
	public SystemBrowserAdapter() {
		// Do nothing
	}

	/**
	 * Close browser
	 */
	public void close() {
		// Do nothing
	}

	/**
	 * Display arbitary url.
	 * 
	 * @param url
	 */
	public void displayURL(String url) {
		// if ( !Program.launch( url ) )
		// {
		// ViewerPlugin.logError( ViewerPlugin.getFormattedResourceString(
		// "viewer.browser.systemBrowser.noprogramforurl", //$NON-NLS-1$
		// new Object[]{
		// url
		// } ),
		// null );
		// }

		// use WorkbenchBrowserSupport so we needn't to provide browser
		// configuration
		IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
		try {
			IWebBrowser browser = support.getExternalBrowser();
			browser.openURL(new URL(url));
		} catch (Exception e) {
			ViewerPlugin
					.logError(ViewerPlugin.getFormattedResourceString("viewer.browser.systemBrowser.noprogramforurl", //$NON-NLS-1$
							new Object[] { url }), null);
		}
	}

	/**
	 * Is browser supports close operation.
	 * 
	 * @return browser supports close operation
	 */
	public boolean isCloseSupported() {
		return false;
	}

	/**
	 * Is setting browser window location supported.
	 * 
	 * @return setting browser window location or not
	 */
	public boolean isSetLocationSupported() {
		return false;
	}

	/**
	 * Is setting browser window size supported.
	 * 
	 * @return setting browser window size or not
	 */
	public boolean isSetSizeSupported() {
		return false;
	}

	/**
	 * Set browser window location.
	 * 
	 * @param x X coordinate of browser window's top-left corner
	 * @param y Y coordinate of browser window's top-left corner
	 */
	public void setLocation(int x, int y) {
		// Do nothing
	}

	/**
	 * Set browser window size.
	 * 
	 * @param width  browser window width
	 * @param height browser window height
	 */
	public void setSize(int width, int height) {
		// Do nothing
	}
}
