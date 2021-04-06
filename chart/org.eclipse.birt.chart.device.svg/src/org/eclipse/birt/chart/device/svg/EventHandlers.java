/***********************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.svg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;

/**
 * This class provides javascript helper functions to enable user interactions
 * such as tooltip support. Defines default styles for svg elements.
 */
public final class EventHandlers {
	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.svg/trace"); //$NON-NLS-1$

	public static final StringBuffer STYLES = new StringBuffer()
			.append(".tooltip.text{ text-anchor:start;font-size:12pt;fill:black;}.tooltip{fill:rgb(244,245,235)}"); //$NON-NLS-1$

	public static String CONTENT;

	public static String getJSMenuLib() {
		if (CONTENT == null) {
			StringBuilder sb = new StringBuilder();
			try {
				InputStream is = EventHandlers.class
						.getResourceAsStream("/org/eclipse/birt/chart/device/svg/SVGActionMenu.js"); //$NON-NLS-1$
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String s = null;
				while (true) {
					s = br.readLine();
					if (s == null) {
						break;
					}
					sb.append(s);
					sb.append("\n"); //$NON-NLS-1$
				}
				br.close();
			} catch (FileNotFoundException e) {
				logger.log(e);
			} catch (IOException e) {
				logger.log(e);
			}

			CONTENT = sb.toString();
		}
		return CONTENT;
	}
}
