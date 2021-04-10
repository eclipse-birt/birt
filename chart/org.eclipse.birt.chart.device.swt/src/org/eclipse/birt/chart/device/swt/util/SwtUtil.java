/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.swt.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.CursorType;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.swt.SWT;

/**
 * DeviceUtil
 */
public class SwtUtil {

	private static final ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.extension/util"); //$NON-NLS-1$

	/** The field maps the standard cursor types to SWT cursor types. */
	public static final Map<CursorType, Integer> CURSOR_MAP = new HashMap<org.eclipse.birt.chart.model.attribute.CursorType, Integer>();
	static {
		CURSOR_MAP.put(CursorType.CROSSHAIR, Integer.valueOf(SWT.CURSOR_CROSS));
		CURSOR_MAP.put(CursorType.DEFAULT, Integer.valueOf(SWT.CURSOR_ARROW));
		CURSOR_MAP.put(CursorType.POINTER, Integer.valueOf(SWT.CURSOR_HAND));
		CURSOR_MAP.put(CursorType.MOVE, Integer.valueOf(SWT.CURSOR_SIZEALL));
		CURSOR_MAP.put(CursorType.TEXT, Integer.valueOf(SWT.CURSOR_IBEAM));
		CURSOR_MAP.put(CursorType.WAIT, Integer.valueOf(SWT.CURSOR_WAIT));
		CURSOR_MAP.put(CursorType.ERESIZE, Integer.valueOf(SWT.CURSOR_SIZEE));
		CURSOR_MAP.put(CursorType.NE_RESIZE, Integer.valueOf(SWT.CURSOR_SIZENE));
		CURSOR_MAP.put(CursorType.NW_RESIZE, Integer.valueOf(SWT.CURSOR_SIZENW));
		CURSOR_MAP.put(CursorType.NRESIZE, Integer.valueOf(SWT.CURSOR_SIZEN));
		CURSOR_MAP.put(CursorType.SE_RESIZE, Integer.valueOf(SWT.CURSOR_SIZESE));
		CURSOR_MAP.put(CursorType.SW_RESIZE, Integer.valueOf(SWT.CURSOR_SIZESW));
		CURSOR_MAP.put(CursorType.SRESIZE, Integer.valueOf(SWT.CURSOR_SIZES));
		CURSOR_MAP.put(CursorType.WRESIZE, Integer.valueOf(SWT.CURSOR_SIZEW));
	}

	/**
	 * Prevent from instanciate.
	 */
	private SwtUtil() {
	}

	/**
	 * Open the given url in default browser.
	 * 
	 * @param url
	 */
	public static void openURL(String href) {
		if (href == null) {
			return;
		}

		// format the href for an html file (file:///<filename.html>
		// required for Mac only.
		if (href.startsWith("file:")) { //$NON-NLS-1$
			href = href.substring(5);
			while (href.startsWith("/")) { //$NON-NLS-1$
				href = href.substring(1);
			}
			href = "file:///" + href; //$NON-NLS-1$
		}
		final String localHref = href;

		Process p = null;
		try {
			p = SecurityUtil.execRuntimeCommand(Runtime.getRuntime(), "/usr/bin/open " + localHref); //$NON-NLS-1$
		} catch (IOException e) {
			p = null;
		}

		if (p == null) {
			Thread launcher = new Thread() {

				public void run() {
					try {
						/*
						 * encoding the href as the browser does not open if there is a space in the
						 * url. Bug 77840
						 */
						String encodedLocalHref = urlEncodeForSpaces(localHref.toCharArray());
						Process p = openWebBrowser(encodedLocalHref);
						try {
							if (p != null) {
								p.waitFor();
							}
						} catch (InterruptedException e) {
							logger.log(e);
						}
					} catch (IOException e) {
						logger.log(e);
					}
				}
			};
			launcher.start();
		}
	}

	/**
	 * RIPPED FROM ECLIPSE BROWSER IMPLEMENTATION. <br>
	 * This method encodes the url, removes the spaces from the url and replaces the
	 * same with <code>"%20"</code>. This method is required to fix Bug 77840.
	 * 
	 */
	private static String urlEncodeForSpaces(char[] input) {
		StringBuffer retu = new StringBuffer(input.length);
		for (int i = 0; i < input.length; i++) {
			if (input[i] == ' ')
				retu.append("%20"); //$NON-NLS-1$
			else
				retu.append(input[i]);
		}
		return retu.toString();
	}

	/**
	 * RIPPED FROM ECLIPSE BROWSER IMPLEMENTATION. <br>
	 * 
	 * @param href
	 * @return
	 * @throws IOException
	 */
	private static Process openWebBrowser(String href) throws IOException {
		Process p = null;
		String webBrowser = null;

		try {
			webBrowser = "netscape"; //$NON-NLS-1$
			p = SecurityUtil.execRuntimeCommand(Runtime.getRuntime(), webBrowser + "  " + href); //$NON-NLS-1$ ;
		} catch (IOException e) {
			p = null;
			webBrowser = "mozilla"; //$NON-NLS-1$
		}

		if (p == null) {
			try {
				p = SecurityUtil.execRuntimeCommand(Runtime.getRuntime(), webBrowser + " " + href); //$NON-NLS-1$ ;
			} catch (IOException e) {
				p = null;
				webBrowser = "explorer"; //$NON-NLS-1$
			}
		}

		if (p == null) {
			try {
				p = SecurityUtil.execRuntimeCommand(Runtime.getRuntime(), webBrowser + " " + href); //$NON-NLS-1$ ;
			} catch (IOException e) {
				p = null;
				throw e;
			}
		}

		return p;
	}
}
