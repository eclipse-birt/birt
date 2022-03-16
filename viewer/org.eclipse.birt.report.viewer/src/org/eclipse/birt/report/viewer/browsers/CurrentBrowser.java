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

import org.eclipse.help.browser.IBrowser;

/**
 * Wrapper for individual browsers contributed through extension point. The
 * original implmentation is from Help.
 * <p>
 */
public class CurrentBrowser implements IBrowser {
	private IBrowser browserAdapter;

	private String browserAdapterId;

	/**
	 * new adapter selected in preferences but not yet shown
	 */
	private IBrowser newBrowserAdapter = null;

	private String newBrowserAdapterId = null;

	private boolean locationSet = false;

	private boolean sizeSet = false;

	private int x;

	private int y;

	private int width;

	private int height;

	boolean external;

	/**
	 * Constructor.
	 *
	 * @param browserImpl      browser instance
	 * @param browserAdapterId browser adapter id
	 * @param externalBrowser  using external browser or not
	 */
	public CurrentBrowser(IBrowser browserImpl, String browserAdapterId, boolean externalBrowser) {
		this.browserAdapter = browserImpl;

		this.browserAdapterId = browserAdapterId;

		this.external = externalBrowser;
	}

	/**
	 * Close current browser.
	 */
	@Override
	public void close() {
		browserAdapter.close();
	}

	/**
	 * Is current browser support close operation.
	 *
	 * @return current browser support close operation or not
	 */
	@Override
	public boolean isCloseSupported() {
		return browserAdapter.isCloseSupported();
	}

	/**
	 * Display url in the browser.
	 *
	 * @param url Url
	 */
	@Override
	public void displayURL(String url) throws Exception {
		checkDefaultAdapter();

		if (newBrowserAdapter != null) {
			browserAdapter.close();

			browserAdapter = newBrowserAdapter;

			newBrowserAdapter = null;

			browserAdapterId = newBrowserAdapterId;

			newBrowserAdapterId = null;

			if (locationSet) {
				browserAdapter.setLocation(x, y);
			}

			if (sizeSet) {
				browserAdapter.setSize(width, height);
			}
		}

		browserAdapter.displayURL(url);
	}

	/**
	 * Is setting browser window location supported.
	 *
	 * @return allow setting browser window location supported
	 */
	@Override
	public boolean isSetLocationSupported() {
		checkDefaultAdapter();

		if (newBrowserAdapterId == null) {
			return browserAdapter.isSetLocationSupported();
		}
		return browserAdapter.isSetLocationSupported() || newBrowserAdapter.isSetLocationSupported();
	}

	/**
	 * Is setting browser size supported.
	 *
	 * @return allow setting browser size supported
	 */
	@Override
	public boolean isSetSizeSupported() {
		checkDefaultAdapter();

		if (newBrowserAdapterId == null) {
			return browserAdapter.isSetSizeSupported();
		}
		return browserAdapter.isSetSizeSupported() || newBrowserAdapter.isSetSizeSupported();
	}

	/**
	 * Set browser window location.
	 *
	 * @param x X coordinate of window's top-left corner
	 * @param y Y coordinate of window's top-left corner
	 */
	@Override
	public void setLocation(int x, int y) {
		checkDefaultAdapter();

		browserAdapter.setLocation(x, y);

		locationSet = true;

		this.x = x;

		this.y = y;
	}

	/**
	 * Set browser window size.
	 *
	 * @param width  browser window width
	 * @param height browser window height
	 */
	@Override
	public void setSize(int width, int height) {
		checkDefaultAdapter();

		browserAdapter.setSize(width, height);

		sizeSet = true;

		this.width = width;

		this.height = height;
	}

	/**
	 * Checks whether default adapter has changed. If yes, sets the
	 * newBrowserAdapterId field
	 */
	private void checkDefaultAdapter() {
		if (external) {
			if (!browserAdapterId.equals(BrowserManager.getInstance().getCurrentBrowserID())) {
				newBrowserAdapter = BrowserManager.getInstance().createBrowser(true);

				newBrowserAdapterId = BrowserManager.getInstance().getCurrentBrowserID();
			}
		} else if (!browserAdapterId.equals(BrowserManager.getInstance().getCurrentInternalBrowserID())) {
			newBrowserAdapter = BrowserManager.getInstance().createBrowser(false);

			newBrowserAdapterId = BrowserManager.getInstance().getCurrentInternalBrowserID();
		}
	}
}
