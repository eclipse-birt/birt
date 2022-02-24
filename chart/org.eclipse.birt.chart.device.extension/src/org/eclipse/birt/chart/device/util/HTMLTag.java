/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.device.util;

public class HTMLTag {
	protected StringBuffer buffer = null;

	public HTMLTag(String tagName) {
		buffer = new StringBuffer("<"); //$NON-NLS-1$
		buffer.append(tagName);
		buffer.append(' ');
	}

	public void addAttribute(HTMLAttribute tagName, String value) {
		buffer.append(' ');
		buffer.append(tagName.getName());
		buffer.append("=\""); //$NON-NLS-1$
		buffer.append(value);
		buffer.append('"');

	}

	public String toString() {
		return buffer + "/>"; //$NON-NLS-1$
	}

}
