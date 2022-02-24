/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.xml.i18n;

/**
 * @deprecated Please use DTP xml driver
 */
public class Messages {

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return org.eclipse.datatools.enablement.oda.xml.i18n.Messages.getString(key);
	}
}
