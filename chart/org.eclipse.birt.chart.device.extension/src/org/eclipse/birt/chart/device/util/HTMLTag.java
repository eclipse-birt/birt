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
