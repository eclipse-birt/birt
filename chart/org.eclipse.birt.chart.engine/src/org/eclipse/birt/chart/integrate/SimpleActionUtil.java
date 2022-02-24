/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.integrate;

/**
 * Utility class for integration classes
 */

public class SimpleActionUtil {

	private static final String SPLITOR = " "; //$NON-NLS-1$

	public static SimpleActionHandle deserializeAction(String strData) {
		SimpleActionHandle action = new SimpleActionHandle();
		String[] array = strData.split(SPLITOR);
		if (array != null) {
			if (array.length > 0) {
				String uri = array[0].trim();
				if (uri.length() > 1 && uri.charAt(0) == '"' && uri.charAt(uri.length() - 1) == '"') {
					// Remove double quotation marks
					uri = uri.substring(1, uri.length() - 1);
				}
				action.setURI(uri);
			}
			if (array.length > 1) {
				action.setTargetWindow(array[1]);
			}
		}
		return action;
	}

	public static String serializeAction(SimpleActionHandle action) {
		if (action == null) {
			return ""; //$NON-NLS-1$
		}
		StringBuffer sb = new StringBuffer();
		sb.append(action.getURI());
		sb.append(SPLITOR);
		sb.append(action.getTargetWindow());
		return sb.toString();
	}
}
