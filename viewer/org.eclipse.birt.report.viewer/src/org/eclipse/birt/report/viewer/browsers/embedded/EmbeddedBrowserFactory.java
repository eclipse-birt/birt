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
import org.eclipse.help.browser.IBrowserFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Implementation of Embedded browser factory. Original implementation is from
 * HELP.
 * <p>
 */
public class EmbeddedBrowserFactory implements IBrowserFactory {

	private boolean tested;
	private boolean available;

	/**
	 * Factory Constructor.
	 */
	public EmbeddedBrowserFactory() {
		super();
	}

	/**
	 * Is embedded browser factory available.
	 * 
	 * @return browser factory available or not
	 */
	public boolean isAvailable() {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				test();
			}
		});
		tested = true;
		return available;
	}

	/**
	 * Create embedded browser.
	 * 
	 * @return embedded browser instance
	 */
	public IBrowser createBrowser() {
		return new EmbeddedBrowserAdapter();
	}

	/**
	 * Must run on UI thread
	 * 
	 * @return
	 */
	private boolean test() {
		// !remove OS check, see bugzilla#270189
		// if ( !Constants.OS_WIN32.equalsIgnoreCase( Platform.getOS( ) )
		// && !Constants.OS_LINUX.equalsIgnoreCase( Platform.getOS( ) ) )
		// {
		// return false;
		// }

		if (!tested) {
			tested = true;
			Shell sh = new Shell();
			try {
				new Browser(sh, SWT.NONE);
				available = true;
			} catch (SWTError se) {
				if (se.code == SWT.ERROR_NO_HANDLES) {
					// Browser not implemented
					available = false;
				}
			} catch (Exception e) {
				// Browser not implemented
			}
			if (sh != null && !sh.isDisposed())
				sh.dispose();
		}
		return available;
	}
}
